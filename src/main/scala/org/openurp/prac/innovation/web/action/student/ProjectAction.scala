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
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.ems.app.EmsApp
import org.beangle.security.Securities
import org.beangle.web.action.support.ActionSupport
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.base.hr.model.Teacher
import org.openurp.base.std.model.Student
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model.*
import org.openurp.starter.web.support.ProjectSupport

import java.time.{Instant, LocalDate}

class ProjectAction extends ActionSupport with EntityAction[Project] with ProjectSupport with MyProject {

  var entityDao: EntityDao = _

  def index(): View = {
    val user = Securities.user
    val initialStage = new StageType(StageType.Initial)
    val query = OqlBuilder.from(classOf[Batch], "b")
    query.where("exists(from b.stages s where :now <= s.endAt and s.stageType=:init)", Instant.now, initialStage)
    val batches = entityDao.search(query)
    var projects: Seq[Project] = null
    if (batches.nonEmpty) {
      val query = OqlBuilder.from(classOf[Project], "p")
      query.where("p.manager.std.code=:code", user)
      query.where("p.batch=:batch", batches.head)
      projects = entityDao.search(query)
      put("initialStage", batches.head.getStage(initialStage))
    } else {
      val query = OqlBuilder.from(classOf[Project], "p")
      query.where("p.manager.std.code=:code", user)
      projects = entityDao.search(query)
    }
    if (projects.nonEmpty) {
      put("initReviewDetails", entityDao.findBy(classOf[InitReviewDetail], "review.project" -> projects).groupBy(_.review.project))
    } else {
      put("initReviewDetails", Map.empty)
    }
    put("projects", projects)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    put("batches", batches)
    forward()
  }

  def edit(): View = {
    val project = getEntity(classOf[Project], "project")
    if (!project.persisted) {
      project.batch = entityDao.get(classOf[Batch], getIntId("project.batch"))
      project.category = entityDao.get(classOf[ProjectCategory], getIntId("project.category"))
    }

    put("projectStates", entityDao.getAll(classOf[ProjectState]))
    put("disciplines", entityDao.search(OqlBuilder.from(classOf[Discipline], "d").where("length(d.code)=3").orderBy("d.code")))

    put("initialStage", project.batch.getStage(new StageType(StageType.Initial)))
    put("managerCode", Securities.user)
    val student = entityDao.findBy(classOf[Student], "code", Securities.user).headOption
    put("student", student)
    put("project", project)
    forward()
  }

  def save(): View = {
    val project = populateEntity(classOf[Project], "project")
    project.state = new ProjectState(ProjectState.Intial)
    val batch = entityDao.get(classOf[Batch], project.batch.id)
    project.batch = batch
    val initialStageType = new StageType(StageType.Initial)
    if (!isIntime(project, StageType.Initial)) {
      return redirect("index", "不在时间范围内");
    }
    //保存项目负责人和成员
    val stdQuery = OqlBuilder.from(classOf[Student], "s").where("s.code=:code", Securities.user)
    val stds = entityDao.search(stdQuery)

    val me = stds.head
    project.department = me.state.get.department
    project.level = new ProjectLevel(ProjectLevel.School)
    entityDao.saveOrUpdate(project)

    project.instructors.clear()
    val instructors = entityDao.findBy(classOf[Teacher], "id", getAll("instructorId").map(_.toString.toLong))
    project.instructors ++= instructors

    val manager = populateEntity(classOf[Member], "manager")
    if (!manager.persisted) {
      if (null == manager.duty) {
        manager.duty == "项目负责人"
      }
      if (null == manager.phone) {
        manager.phone = "--"
      }
    }
    manager.std = stds.head
    manager.project = project

    project.manager = Some(manager)

    val inMember = project.members.exists(_.std == manager.std)
    if (!inMember) {
      project.members += manager
    }
    val students = entityDao.findBy(classOf[Student], "id", getAll("studentId").map(_.toString.toLong)).toSet
    students foreach { s =>
      val exists = project.members.exists(_.std == s)
      if (!exists) {
        project.members += Member(project, s)
      }
    }
    val removed = project.members filter (a => !(a.std == manager.std || students.contains(a.std)))
    project.members --= removed
    //保存项目简介
    val intro = populateEntity(classOf[Intro], "intro")
    intro.project = project
    project.intro = Some(intro)
    entityDao.saveOrUpdate(intro, manager, project)

    val parts = getAll("attachment", classOf[Part])
    if (parts.size > 0 && parts.head.getSize > 0) {
      val material =
        project.materials.find(_.stageType == initialStageType) match {
          case None => new Material(project, initialStageType)
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
      val meta = blob.upload("/innovation/" + project.batch.beginOn.getYear.toString + "/" + project.id.toString,
        part.getInputStream, part.getSubmittedFileName, me.code + " " + me.name)
      material.fileSize = meta.fileSize
      material.sha = meta.sha
      material.filePath = meta.filePath
      entityDao.saveOrUpdate(material)
    }

    redirect("index", "&batch.id=" + project.batch.id, "info.save.success")
  }

  def teacher(): View = {
    val codeOrName = get("q").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where(":project in elements(teacher.projects)", getProject)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(teacher.name like :name or teacher.staff.code like :code)", s"%${codeOrName}%",
        s"%${codeOrName}%")
    }
    val now = LocalDate.now
    query.where(":now1 >= teacher.beginOn and (teacher.endOn is null or :now2 <= teacher.endOn)", now, now)
      .orderBy("teacher.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    put("teachers", entityDao.search(query));
    put("pageLimit", pageLimit);
    forward()
  }

  def student(): View = {
    val codeOrName = get("q").orNull
    val query = OqlBuilder.from(classOf[Student], "student")
    query.where("student.project=:project", getProject)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(student.name like :name or student.code like :code)", s"%${codeOrName}%",
        s"%${codeOrName}%");
    }
    query.orderBy("student.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    put("students", entityDao.search(query));
    put("pageLimit", pageLimit);
    forward()
  }

  def savePromotion(): View = {
    val projectId = getLongId("project")
    val project = entityDao.get(classOf[Project], projectId)
    val levelId = getInt("promotion_level_id", 0)
    val stageTypeId = if (levelId == ProjectLevel.Nation) StageType.PromotionNation else StageType.PromotionState
    val promotionStage = new StageType(stageTypeId)
    val parts = getAll("promotion_report", classOf[Part])
    if (parts.nonEmpty && parts.head.getSize > 0) {
      val material =
        project.materials.find(_.stageType == promotionStage) match {
          case None => new Material(project, promotionStage)
          case Some(m) => m
        }
      val part = getAll("promotion_report", classOf[Part]).head
      val fileName = part.getSubmittedFileName
      val now = Instant.now
      material.fileName = fileName
      material.updatedAt = now
      val blob = EmsApp.getBlobRepository(true)
      if (null != material.filePath) {
        blob.remove(material.filePath)
      }
      val me = project.manager.get.std
      val meta = blob.upload("/innovation/" + project.batch.beginOn.getYear.toString + "/" + project.id.toString,
        part.getInputStream, part.getSubmittedFileName, me.code + " " + me.name)
      material.fileSize = meta.fileSize
      material.sha = meta.sha
      material.filePath = meta.filePath
      entityDao.saveOrUpdate(material)
    }
    redirect("index", "保存成功")
  }
}
