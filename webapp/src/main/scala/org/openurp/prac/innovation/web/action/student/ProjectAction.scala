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

import java.time.{Instant, LocalDate}
import jakarta.servlet.http.Part
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.ems.app.EmsApp
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.code.edu.model.Discipline
import org.openurp.base.edu.model.{Student, Teacher}
import org.openurp.boot.edu.helper.ProjectSupport
import org.openurp.prac.innovation.model._

class ProjectAction extends ActionSupport with EntityAction[Project] with ProjectSupport with MyProject {

  def index(): View = {
    val user = Securities.user
    val initialStage = new StageType(StageType.Initial)
    val query = OqlBuilder.from(classOf[Batch], "b")
    query.where("exists(from b.stages s where :now <= s.endAt and s.stageType=:init)", Instant.now, initialStage)
    val batches = entityDao.search(query)
    if (batches.nonEmpty) {
      val query = OqlBuilder.from(classOf[Project], "p")
      query.where("p.manager.std.user.code=:code", user)
      query.where("p.batch=:batch", batches.head)
      put("projects", entityDao.search(query))
      put("initialStage", batches.head.getStage(initialStage))
    } else {
      put("projects", List.empty[Project])
    }
    put("batches", batches)
    forward()
  }

  def edit(): View = {
    val project = getEntity(classOf[Project], "project")
    if (!project.persisted) {
      project.batch = entityDao.get(classOf[Batch], getInt("project.batch.id").get)
    }
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    put("projectStates", entityDao.getAll(classOf[ProjectState]))
    put("disciplines", entityDao.search(OqlBuilder.from(classOf[Discipline], "d").where("length(d.code)=3").orderBy("d.code")))

    put("initialStage", project.batch.getStage(new StageType(StageType.Initial)))
    put("managerCode", Securities.user)
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
    val stdQuery = OqlBuilder.from(classOf[Student], "s").where("s.user.code=:code", Securities.user)
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
      if (null != material.path) {
        blob.remove(material.path)
      }
      val meta = blob.upload("/" + project.batch.beginOn.getYear.toString, part.getInputStream, part.getSubmittedFileName,
        me.user.code + " " + me.user.name)
      material.size = meta.fileSize
      material.sha = meta.sha
      material.path = meta.filePath
      entityDao.saveOrUpdate(material)
    }

    redirect("index", "&batch.id=" + project.batch.id, "info.save.success")
  }

  def teacher(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where("teacher.project=:project", getProject)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(teacher.user.name like :name or teacher.user.code like :code)", '%' + codeOrName + '%',
        '%' + codeOrName + '%');
    }
    val now = LocalDate.now
    query.where(":now1 >= teacher.beginOn and (teacher.endOn is null or :now2 <= teacher.endOn)", now, now)
      .orderBy("teacher.user.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    put("teachers", entityDao.search(query));
    put("pageLimit", pageLimit);
    forward()
  }

  def student(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Student], "student")
    query.where("student.project=:project", getProject)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(student.user.name like :name or student.user.code like :code)", '%' + codeOrName + '%',
        '%' + codeOrName + '%');
    }
    query.orderBy("student.user.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    put("students", entityDao.search(query));
    put("pageLimit", pageLimit);
    forward()
  }
}
