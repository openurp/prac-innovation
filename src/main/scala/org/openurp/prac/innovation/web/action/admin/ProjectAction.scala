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

package org.openurp.prac.innovation.web.action.admin

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.doc.transfer.exporter.ExportContext
import org.beangle.ems.app.EmsApp
import org.beangle.web.action.annotation.ignore
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.base.hr.model.Teacher
import org.openurp.base.model.Department
import org.openurp.base.std.model.Student
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model.*
import org.openurp.prac.innovation.web.action.helper.ExportProject
import org.openurp.starter.web.support.ProjectSupport

import java.io.FileInputStream
import java.time.LocalDate

class ProjectAction extends RestfulAction[Project], ProjectSupport, ExportSupport[Project] {

  protected override def indexSetting(): Unit = {
    val batches = entityDao.getAll(classOf[Batch])
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
    put("projectStates", entityDao.getAll(classOf[ProjectState]))
  }

  protected override def getQueryBuilder: OqlBuilder[Project] = {
    val builder = super.getQueryBuilder
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from project.members m where m.std.code like :stdCode or m.std.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    get("instructor") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from project.instructors i where i.staff.code like :teacherCode or i.name like :teacherName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    builder
  }

  protected override def editSetting(project: Project): Unit = {
    if (!project.persisted) {
      project.batch = entityDao.get(classOf[Batch], getInt("project.batch.id").get)
    }
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
    put("projectStates", entityDao.getAll(classOf[ProjectState]))
    put("disciplines", entityDao.search(OqlBuilder.from(classOf[Discipline], "d").where("length(d.code)=3").orderBy("d.code")))

    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", project.batch.beginOn)
    put("departments", entityDao.search(departQuery))
  }

  override protected def saveAndRedirect(project: Project): View = {
    entityDao.saveOrUpdate(project)

    project.instructors.clear()
    val instructors = entityDao.findBy(classOf[Teacher], "id", getAll("instructorId").map(_.toString.toLong))
    project.instructors ++= instructors

    //保存项目负责人和成员
    val stdQuery = OqlBuilder.from(classOf[Student], "s").where("s.code=:code", get("managerCode").get)
    val stds = entityDao.search(stdQuery)

    val manager = populateEntity(classOf[Member], "manager")
    if (!manager.persisted) {
      if null == manager.duty then manager.duty = "项目负责人"
      if null == manager.phone then manager.phone = "--"
    }
    manager.std = stds.head
    manager.project = project

    project.manager = Some(manager)

    val inMember = project.members.exists(_.std == manager.std)
    if !inMember then project.members += manager
    val students = entityDao.findBy(classOf[Student], "id", getAll("studentId").map(_.toString.toLong)).toSet
    students foreach { s =>
      val exists = project.members.exists(_.std == s)
      if !exists then project.members += Member(project, s)
    }
    val removed = project.members filter (a => !(a.std == manager.std || students.contains(a.std)))
    project.members --= removed
    //保存项目简介
    val intro = populateEntity(classOf[Intro], "intro")
    intro.project = project
    project.intro = Some(intro)
    entityDao.saveOrUpdate(intro, manager, project)

    super.saveAndRedirect(project)
  }

  def teacher(): View = {
    val codeOrName = get("q").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where(":project in elements(teacher.projects)", getProject)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      val codeNameParam = s"%${codeOrName}%"
      query.where("(teacher.name like :name or teacher.staff.code like :code)", codeNameParam, codeNameParam);
    }
    val now = LocalDate.now
    query.where(":now1 >= teacher.beginOn and (teacher.endOn is null or :now2 <= teacher.endOn)", now, now)
      .orderBy("teacher.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit)
    put("teachers", entityDao.search(query))
    put("pageLimit", pageLimit)
    forward()
  }

  def student(): View = {
    val codeOrName = get("q").orNull
    val query = OqlBuilder.from(classOf[Student], "student")
    query.where("student.project=:project", getProject)
    populateConditions(query)

    if (Strings.isNotEmpty(codeOrName)) {
      val codeNameParam = s"%${codeOrName}%"
      query.where("(student.name like :name or student.code like :code)", codeNameParam,
        codeNameParam);
    }
    query.orderBy("student.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    put("students", entityDao.search(query))
    put("pageLimit", pageLimit)
    forward()
  }

  @ignore
  override def configExport(context: ExportContext): Unit = {
    val query = getQueryBuilder
    query.limit(null)
    val closures = entityDao.search(query)
    val projects = closures.map(x => new ExportProject(x))
    context.put("items", projects)
  }

  def attachment(): View = {
    val material = entityDao.get(classOf[Material], getLongId("material"))
    val path = EmsApp.getBlobRepository(true).url(material.filePath)
    response.sendRedirect(path.get.toString)
    null
  }

  override def removeAndRedirect(projects: Seq[Project]): View = {
    val repo = EmsApp.getBlobRepository(true)
    projects foreach { project =>
      project.materials foreach { material =>
        repo.remove(material.filePath)
      }
    }
    super.removeAndRedirect(projects)
  }

}
