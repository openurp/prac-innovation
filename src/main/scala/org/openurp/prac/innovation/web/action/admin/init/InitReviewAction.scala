/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.prac.innovation.web.action.admin.init

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.annotation.{ignore, mapping, param}
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model.*

import java.time.Instant

class InitReviewAction extends RestfulAction[InitReview] {

  @ignore
  protected override def simpleEntityName: String = {
    "review"
  }

  protected override def indexSetting(): Unit = {
    val batches = entityDao.search(OqlBuilder.from(classOf[Batch], "b").orderBy("b.beginOn desc"))
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("closureStage", new StageType(StageType.Initial))
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

  def generate(): View = {
    val batchId = getIntIds("review.project.batch").head
    val query = OqlBuilder.from(classOf[Project], "p")
    query.where("not exists(from " + classOf[InitReview].getName + " ir where ir.project=p)")
    query.where("p.batch.id=:batchId", batchId)
    val projects = entityDao.search(query)
    val irs = projects.map { p =>
      val ir = new InitReview
      ir.project = p
      ir
    }
    entityDao.saveOrUpdate(irs)
    redirect("search", "info.save.success")
  }

  override def search(): View = {
    put("reviews", entityDao.search(getQueryBuilder))
    put("groups", entityDao.getAll(classOf[InitReviewGroup]))
    val stat = OqlBuilder.from[Array[_]](classOf[InitReview].getName, "ir")
    stat.where("ir.project.batch.id=:batchId", getIntIds("review.project.batch").head)
    stat.where("ir.group is not null")
    stat.select("ir.group.id,count(*)")
    stat.groupBy("ir.group.id")

    val stats = entityDao.search(stat).map(x => x(0).toString -> x(1)).toMap
    put("stats", stats)
    forward()
  }

  protected override def getQueryBuilder: OqlBuilder[InitReview] = {
    val builder = super.getQueryBuilder
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from review.project.members m where m.std.code like :stdCode or m.std.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    get("expert") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from review.details  as m where m.expert.code like :expertCode or m.expert.name like :expertName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    getBoolean("has_group") foreach { g =>
      builder.where("review.group is " + (if (g) " not " else " ") + " null")
    }
    builder
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val project = entityDao.get(classOf[Project], getLongId("project"))
    put("project", project)
    forward()
  }

  def assign(): View = {
    val reviews = entityDao.find(classOf[InitReview], getLongIds("review"))
    val group = entityDao.get(classOf[InitReviewGroup], getLongId("reviewGroup"))

    reviews foreach { ir =>
      assignExperts(ir, group)
    }
    redirect("search", "info.save.success")
  }

  private def assignExperts(ir: InitReview, group: InitReviewGroup): Unit = {
    if (ir.score.isEmpty) {
      val expertIter = group.experts.iterator
      ir.details foreach { r =>
        if (r.score.isEmpty && expertIter.hasNext) {
          r.expert = expertIter.next()
          r.updatedAt = Instant.now
        }
      }
      while (group.experts.size > ir.details.size) {
        val r = new InitReviewDetail
        r.review = ir
        r.updatedAt = Instant.now
        r.expert = expertIter.next()
        ir.details += r
      }
      ir.group = Some(group)
      entityDao.saveOrUpdate(ir)
    }
  }

  def clear(): View = {
    val reviews = entityDao.find(classOf[InitReview], getLongIds("review"))
    reviews foreach { cr =>
      cr.group = None
      cr.details --= cr.details.filter(_.score.isEmpty)
      entityDao.saveOrUpdate(cr)
    }
    redirect("search", "info.save.success")
  }
}
