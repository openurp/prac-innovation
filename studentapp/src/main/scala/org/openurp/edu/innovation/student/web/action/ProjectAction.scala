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
package org.openurp.edu.innovation.student.web.action

import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.edu.boot.web.ProjectSupport
import org.openurp.edu.innovation.model.Project
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.base.model.Department
import org.openurp.edu.innovation.model.ProjectCategory
import org.openurp.code.edu.model.Discipline
import java.time.LocalDate
import org.openurp.edu.base.model.Teacher
import org.beangle.commons.lang.Strings
import org.openurp.edu.innovation.model.ProjectState
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.innovation.model.Intro
import org.openurp.edu.innovation.model.ProjectLevel
import org.openurp.edu.base.model.Student
import org.openurp.edu.innovation.model.Member
import org.beangle.webmvc.api.view.View
import org.openurp.edu.innovation.model.Batch
import org.beangle.security.Securities
import org.openurp.edu.innovation.model.StageType
import java.time.Instant

class ProjectAction extends ActionSupport with EntityAction[Project] with ProjectSupport {

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
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
    put("projectStates", entityDao.getAll(classOf[ProjectState]))
    put("disciplines", entityDao.search(OqlBuilder.from(classOf[Discipline], "d").where("length(d.code)=3").orderBy("d.code")))

    put("managerCode", Securities.user)
    put("project", project)
    forward()
  }

  def save(): View = {
    val project = populateEntity(classOf[Project], "project")
    project.state = new ProjectState(ProjectState.Intial)
    //保存项目负责人和成员
    val stdQuery = OqlBuilder.from(classOf[Student], "s").where("s.user.code=:code", Securities.user)
    val stds = entityDao.search(stdQuery)

    project.department = stds.head.state.get.department
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
    redirect("index", "&batch.id=" + project.batch.id, "info.save.success")
  }

  def teacher(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where("teacher.project=:project", getProject())
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
    query.where("student.project=:project", getProject())
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
