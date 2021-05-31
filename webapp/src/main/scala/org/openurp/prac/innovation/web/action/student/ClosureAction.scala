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
package org.openurp.prac.innovation.web.action.student

import jakarta.servlet.http.Part
import org.beangle.data.dao.OqlBuilder
import org.beangle.ems.app.EmsApp
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.EntityAction
import org.beangle.webmvc.entity.helper.PopulateHelper
import org.openurp.prac.innovation.model._

import java.time.Instant

class ClosureAction extends ActionSupport with EntityAction[Project] with ServletSupport with MyProject {

  def index(): View = {
    val user = Securities.user;
    val query = OqlBuilder.from(classOf[Project], "p")
    query.where("p.manager.std.user.code=:code", user)
    val projects = entityDao.search(query);
    put("projects", projects)
    put("closureStage", new StageType(StageType.Closure))
    forward()
  }

  def closureForm(): View = {
    val projectId = longId("project")
    val project = entityDao.get(classOf[Project], projectId)
    put("project", project)
    val closureQuery = OqlBuilder.from(classOf[Closure], "closure").where("closure.project=:project", project)
    put("closures", entityDao.search(closureQuery))
    put("closureStage", new StageType(StageType.Closure))
    put("applyExemptionReplyStage", new StageType(StageType.ApplyExemptionReply))
    forward()
  }

  def saveClosure(): View = {
    val projectId = longId("project")
    val project = entityDao.get(classOf[Project], projectId)
    if (isIntime(project, StageType.Closure)) {
      val closureStage = new StageType(StageType.Closure)
      val closure =
        getId("closure", classOf[Long]) match {
          case None => new Closure(project)
          case Some(id) => entityDao.get(classOf[Closure], id)
        }
      val originalExceptionReply = closure.applyExemptionReply
      PopulateHelper.populate(closure, classOf[Closure].getName, "closure")
      //如果不在免答辩的申请时间内，则恢复原来的值
      if (!isIntime(project, StageType.ApplyExemptionReply)) {
        if (originalExceptionReply) {
          closure.applyExemptionReply = closure.exemptionConfirmed.getOrElse(false)
        } else {
          closure.applyExemptionReply = originalExceptionReply
        }
      }
      if (closure.applyExemptionReply) {
        if (!closure.exemptionConfirmed.getOrElse(false)) {
          closure.applyRejectComment = None
          closure.exemptionConfirmed = None
        }
      } else {
        closure.exemptionConfirmed = None
        closure.applyRejectComment = None
        closure.exemptionReason = None
      }
      val parts = getAll("attachment", classOf[Part])
      if (parts.size > 0 && parts.head.getSize > 0) {
        val material =
          project.materials.find(_.stageType == closureStage) match {
            case None => new Material(project, closureStage)
            case Some(m) => m
          }
        val part = getAll("attachment", classOf[Part]).head
        val fileName = part.getSubmittedFileName
        val now = Instant.now
        material.fileName = fileName
        material.updatedAt = now
        val blob = EmsApp.getBlobRepository(true)
        if (null != material.path) {
          blob.remove(material.path)
        }
        val me = project.manager.get.std
        val meta = blob.upload("/" + project.batch.beginOn.getYear.toString + "/" + project.id.toString,
          part.getInputStream, part.getSubmittedFileName,
          me.user.code + " " + me.user.name)
        material.size = meta.fileSize
        material.sha = meta.sha
        material.path = meta.filePath
        entityDao.saveOrUpdate(material)
      }
      closure.updatedAt = Instant.now
      entityDao.saveOrUpdate(project, closure)
      redirect("index", "保存成功")
    } else {
      redirect("index", "不在时间范围内")
    }
  }

}
