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

import java.time.LocalDate

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.code.edu.model.Discipline
import org.openurp.edu.base.model.{ Student, Teacher }
import org.openurp.edu.innovation.model.{ Batch, Intro, Member, Project, ProjectCategory, ProjectLevel, ProjectState }

class ProjectAction extends RestfulAction[Project] {

  protected override def indexSetting() {
    val batches = entityDao.getAll(classOf[Batch])
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
  }

  protected override def getQueryBuilder(): OqlBuilder[Project] = {
    val builder = super.getQueryBuilder()
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from project.members m where m.std.user.code like :stdCode or m.std.user.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    builder
  }

  protected override def editSetting(project: Project) {
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

    //保存项目成员
    val manager = populateEntity(classOf[Member], "manager")
    val stdQuery = OqlBuilder.from(classOf[Student], "s").where("s.user.code=:code", manager.std.user.code)
    val stds = entityDao.search(stdQuery)
    manager.std = stds.head
    manager.project = project

    project.manager = Some(manager)

    val inMember = project.members.exists(_.std == manager.std)
    if (!inMember) {
      project.members += manager;
    }

    val students = entityDao.findBy(classOf[Student], "id", getAll("studentId").map(_.toString.toLong)).toSet
    students foreach { s =>
      val exists = project.members.exists(_.std == s)
      if (!exists) {
        val newMember = new Member()
        newMember.std = s
        newMember.duty = "--"
        newMember.project = project
        newMember.phone = "--"
        project.members += newMember
      }
    }
    val allMembers = Collections.newBuffer(project.members)
    project.members.clear()
    project.members ++= (allMembers.filter(a => a.std == manager.std || students.contains(a.std)))

    //保存项目简介
    val intro = populateEntity(classOf[Intro], "intro")
    intro.project = project
    project.intro = Some(intro)

    entityDao.saveOrUpdate(intro, manager, project)

    super.saveAndRedirect(project)
  }

  def teacher(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where("teacher.project.id=:projectId", getInt("project").get)
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
    query.where("student.project.id=:projectId", getInt("project").get)
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
