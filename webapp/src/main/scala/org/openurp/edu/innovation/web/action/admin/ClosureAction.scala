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

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.UrpApp
import org.openurp.base.model.Department
import org.openurp.edu.innovation.model._

class ClosureAction extends RestfulAction[Closure] with ServletSupport {

  protected override def indexSetting(): Unit = {
    val batches = entityDao.search(OqlBuilder.from(classOf[Batch], "b").orderBy("b.beginOn desc"))
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("closureStage", new StageType(StageType.Closure))
  }

  protected override def getQueryBuilder: OqlBuilder[Closure] = {
    val builder = super.getQueryBuilder
    getBoolean("need_audit") foreach { m =>
      builder.where(
        "(closure.applyExemptionReply=true and closure.exemptionConfirmed is " + (if (m) "" else "not") + " null)")
    }
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from closure.project.members m where m.std.user.code like :stdCode or m.std.user.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    builder
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[Closure]): View = {
    val materials = entities.flatten(_.project.closureMaterial)
    remove(entities, materials)
    val repo = UrpApp.getBlobRepository(true)
    materials foreach { m =>
      repo.remove(m.path)
    }
    redirect("search", "info.remove.success")
  }

  def batchEditReply(): View = {
    val closures = entityDao.find(classOf[Closure], longIds("closure")).filter(!_.exemptionConfirmed.getOrElse(false))
    put("closures", closures)
    forward()
  }

  def saveBatchEditReply(): View = {
    val closures = entityDao.find(classOf[Closure], longIds("closure"))
    closures.foreach { c =>
      c.replyScore = getInt("closure_" + c.id + ".replyScore")
    }
    entityDao.saveOrUpdate(closures)
    redirect("search", "info.save.success")
  }

  def attachment(): View = {
    val materials = entityDao.findBy(classOf[Material], "project.id", getLong("project.id"))
    if (materials.isEmpty) {
      null
    } else {
      materials.find(_.stageType.id == StageType.Closure) match {
        case Some(material) =>
          val path = UrpApp.getBlobRepository(true).url(material.path)
          response.sendRedirect(path.get.toString)
          null
        case None => null
      }
    }
  }

  def audit(): View = {
    val id = longId("closure")
    val entity = getModel(id)
    editSetting(entity)
    put(simpleEntityName, entity)
    forward()
  }

}
