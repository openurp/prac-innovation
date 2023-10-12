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

package org.openurp.prac.innovation.web.action.student

import jakarta.servlet.http.Part
import org.beangle.data.dao.{EntityDao, Operation, OqlBuilder}
import org.beangle.ems.app.EmsApp
import org.beangle.security.Securities
import org.beangle.web.action.support.{ActionSupport, ServletSupport}
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.beangle.webmvc.support.helper.PopulateHelper
import org.openurp.prac.innovation.model.*

import java.time.Instant

class ClosureAction extends ActionSupport with EntityAction[Project] with ServletSupport with MyProject {

  var entityDao: EntityDao = _

  def index(): View = {
    val user = Securities.user;
    val query = OqlBuilder.from(classOf[Project], "p")
    query.where("p.manager.std.code=:code", user)
    val projects = entityDao.search(query);
    put("projects", projects)
    put("closureStage", new StageType(StageType.Closure))
    forward()
  }

  def closureForm(): View = {
    val projectId = getLongId("project")
    val project = entityDao.get(classOf[Project], projectId)
    put("project", project)
    val closureQuery = OqlBuilder.from(classOf[Closure], "closure").where("closure.project=:project", project)
    put("closures", entityDao.search(closureQuery))
    put("closureStage", new StageType(StageType.Closure))
    put("applyExemptionReplyStage", new StageType(StageType.ApplyExemptionReply))
    forward()
  }

  def removeClosure(): View = {
    val projectId = getLongId("project")
    val project = entityDao.get(classOf[Project], projectId)
    if (isIntime(project, StageType.Closure)) {
      val me = project.manager.get.std.code
      if (Securities.user == me) {
        val closureQuery = OqlBuilder.from(classOf[Closure], "c").where("c.project=:project", project)
        val closures = entityDao.search(closureQuery)
        if (closures.size == 1) {
          val closure = closures.head
          project.closureMaterial foreach { m =>
            val blob = EmsApp.getBlobRepository(true)
            blob.remove(m.filePath)
          }
          project.materials --= project.closureMaterial
          entityDao.execute(Operation.saveOrUpdate(project).remove(closure))
        }
      }
      redirect("index", "取消结项成功")
    } else {
      redirect("index", "不在时间范围内")
    }
  }

  def saveClosure(): View = {
    val projectId = getLongId("project")
    val project = entityDao.get(classOf[Project], projectId)
    if (isIntime(project, StageType.Closure)) {
      val closureStage = new StageType(StageType.Closure)
      val closure =
        getId("closure", classOf[Long]) match {
          case None => new Closure(project)
          case Some(id) => entityDao.get(classOf[Closure], id)
        }
      val originalExceptionReply = closure.applyExemptionReply
      PopulateHelper.populate(closure, PopulateHelper.getType(classOf[Closure]), "closure")
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
      if (parts.nonEmpty && parts.head.getSize > 0) {
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
        if (null != material.filePath) {
          blob.remove(material.filePath)
        }
        val me = project.manager.get.std
        logger.info("starting upload file to remote repository...")
        val meta = blob.upload("/innovation/" + project.batch.beginOn.getYear.toString + "/" + project.id.toString,
          part.getInputStream, part.getSubmittedFileName,
          me.code + " " + me.name)
        material.fileSize = meta.fileSize
        material.sha = meta.sha
        material.filePath = meta.filePath
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
