/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.prac.innovation.web.action.admin.close

import java.time.Instant

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.{ignore, mapping, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model.{Closure, _}

class ClosureReviewAction extends RestfulAction[ClosureReview] {

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
    put("closureStage", new StageType(StageType.Closure))
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

  def generate(): View = {
    val batchId = intId("review.project.batch")
    val query = OqlBuilder.from(classOf[Project], "p")
    query.where("not exists(from " + classOf[ClosureReview].getName + " ir where ir.project=p)")
    query.where("p.batch.id=:batchId", batchId)
    val projects = entityDao.search(query)
    val crs = projects.map { p =>
      val cr = new ClosureReview
      cr.project = p
      cr
    }
    entityDao.saveOrUpdate(crs)
    redirect("search", "info.save.success")
  }

  override def search(): View = {
    put("reviews", entityDao.search(getQueryBuilder))
    val batchId = intId("review.project.batch")
    put("groups", entityDao.search(OqlBuilder.from(classOf[ClosureReviewGroup], "crg").where("crg.batch.id=:batchId", batchId)))
    val stat = OqlBuilder.from[Array[_]](classOf[ClosureReview].getName, "cr")
    stat.where("cr.project.batch.id=:batchId", batchId)
    stat.where("cr.group is not null")
    stat.select("cr.group.id,count(*)")
    stat.groupBy("cr.group.id")

    val stats = entityDao.search(stat).map(x => x(0).toString -> x(1)).toMap
    put("stats", stats)
    forward()
  }

  protected override def getQueryBuilder: OqlBuilder[ClosureReview] = {
    val builder = super.getQueryBuilder
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from review.project.members m where m.std.user.code like :stdCode or m.std.user.name like :stdName)",
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
    val closure = entityDao.get(classOf[Closure], longId("closure"))
    put("closure", closure)
    forward()
  }

  def assign(): View = {
    val reviews = entityDao.find(classOf[ClosureReview], longIds("review"))
    val group = entityDao.get(classOf[ClosureReviewGroup], longId("reviewGroup"))

    reviews foreach { cr =>
      assignExperts(cr, group)
    }
    redirect("search", "info.save.success")
  }

  private def assignExperts(cr: ClosureReview, group: ClosureReviewGroup): Unit = {
    if (cr.score.isEmpty) {
      val expertIter = group.experts.iterator
      cr.details foreach { r =>
        if (r.score.isEmpty && expertIter.hasNext) {
          r.expert = expertIter.next()
          r.updatedAt = Instant.now
        }
      }
      while (group.experts.size > cr.details.size) {
        val r = new ClosureReviewDetail
        r.review = cr
        r.updatedAt = Instant.now
        r.expert = expertIter.next()
        cr.details += r
      }
      cr.group = Some(group)
      entityDao.saveOrUpdate(cr)
    }
  }

  def clear(): View = {
    val reviews = entityDao.find(classOf[ClosureReview], longIds("review"))
    reviews foreach { cr =>
      cr.group = None
      cr.details --= cr.details.filter(_.score.isEmpty)
      entityDao.saveOrUpdate(cr)
    }
    redirect("search", "info.save.success")
  }
}
