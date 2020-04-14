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
package org.openurp.edu.innovation.web.action.admin

import java.time.Instant

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.code.edu.model.Discipline
import org.openurp.edu.innovation.model.{Closure, _}

class ReviewAssignAction extends RestfulAction[Closure] {

  protected override def indexSetting(): Unit = {
    val batches = entityDao.search(OqlBuilder.from(classOf[Batch], "b").orderBy("b.beginOn desc"))
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("closureStage", new StageType(StageType.Closure))
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

  override def search(): View = {
    put("closures", entityDao.search(getQueryBuilder))
    put("groups", entityDao.getAll(classOf[ReviewGroup]))
    val stat = OqlBuilder.from[Array[_]](classOf[Project].getName, "p")
    stat.where("p.batch.id=:batchId", intId("closure.project.batch"))
    stat.where("p.reviewGroup is not null")
    stat.select("p.reviewGroup.id,count(*)")
    stat.groupBy("p.reviewGroup.id")

    val stats = entityDao.search(stat).map(x => x(0).toString -> x(1)).toMap
    put("stats", stats)
    forward()
  }

  protected override def getQueryBuilder: OqlBuilder[Closure] = {
    val builder = super.getQueryBuilder
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from closure.project.members m where m.std.user.code like :stdCode or m.std.user.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    get("expert") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from " + classOf[Review].getName + " m where m.project=closure.project and m.expert.code like :stdCode or m.expert.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    getBoolean("has_group") foreach{g=>
      if(g){
        builder.where("closure.project.reviewGroup is not null")
      }else{
        builder.where("closure.project.reviewGroup is null")
      }
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
    val closures = entityDao.find(classOf[Closure], longIds("closure"))
    val group = entityDao.get(classOf[ReviewGroup], longId("reviewGroup"))
    val projects = closures.map(_.project)
    projects foreach { p =>
      if (p.reviewScore.isEmpty) {
        val expertIter = group.experts.iterator
        p.reviews foreach { r =>
          if (r.score.isEmpty && expertIter.hasNext) {
            r.expert = expertIter.next()
            r.updatedAt = Instant.now
          }
        }
        while (group.experts.size > p.reviews.size) {
          val r = new Review
          r.project = p
          r.updatedAt = Instant.now
          r.expert = expertIter.next()
          p.reviews += r
        }
        p.reviewGroup = Some(group)
        entityDao.saveOrUpdate(p)
      }
    }
    redirect("search", "info.save.success")
  }

  def clear(): View = {
    val closures = entityDao.find(classOf[Closure], longIds("closure"))
    closures.map(_.project) foreach { p =>
      p.reviewGroup = None
      p.reviews --= p.reviews.filter(_.score.isEmpty)
      entityDao.saveOrUpdate(p)
    }
    redirect("search", "info.save.success")
  }
}
