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
package org.openurp.edu.innovation.web.action

import java.io.ByteArrayInputStream

import org.beangle.commons.activation.MimeTypes
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.innovation.model.{ Batch, Closure, Material, Project, ProjectCategory, StageType }

class ClosureAction extends RestfulAction[Closure] {

  protected override def indexSetting() {
    val batches = entityDao.getAll(classOf[Batch])
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("closureStage", new StageType(StageType.Closure))
  }

  protected override def getQueryBuilder(): OqlBuilder[Closure] = {
    val builder = super.getQueryBuilder()
    getBoolean("need_audit") foreach { m =>
      builder.where(
        "(closure.applyExemptionReply=true and closure.exemptionConfirmed is " + (if (m) "" else "not") + " null)")
    }
    builder
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[Closure]): View = {
    val attachments = entities.map(_.project.closureMaterial).flatten.map(_.attachment)
    remove(entities, attachments)
    redirect("search", "info.remove.success")
  }

  def batchEditReply(): View = {
    var closures = entityDao.find(classOf[Closure], longIds("closure")).filter(!_.exemptionConfirmed.getOrElse(false))
    put("closures", closures);
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
          val attachment = material.attachment
          Stream(new ByteArrayInputStream(attachment.content), decideContentType(attachment.fileName),
            attachment.fileName)
        case None => null
      }
    }
  }

  def audit(): View = {
    val id = longId("closure")
    var entity = getModel(id)
    editSetting(entity)
    put(simpleEntityName, entity)
    forward()
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(Strings.substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }
}
