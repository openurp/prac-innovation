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
package org.openurp.prac.innovation.web.action.helper

import org.openurp.prac.innovation.model.Project

class ExportProject(project: Project) {

  def getYear: String = {
    project.batch.name
  }
  def getSchoolCode: String = {
    project.batch.school.code
  }

  def getSchoolName: String = {
    project.batch.school.name
  }

  def getCode: String = {
    project.code.getOrElse("--")
  }

  def getTitle: String = {
    project.title
  }
  def getCategory: String = {
    project.category.name
  }
  def getManagerName: String = {
    project.manager.get.std.user.name
  }
  def getManagerCode: String = {
    project.manager.get.std.user.code
  }

  def getMemberCount: Int = {
    project.members.size
  }

  def getMemberNames: String = {
    var i = 0
    val names = project.members map { x =>
      i += 1;
      x.std.user.name + " " + i + "/" + x.std.user.code
    }
    names.mkString(",")
  }

  def getInstructorNames: String = {
    project.instructors.map(_.user.name).mkString(",")
  }

  def getInstructorTitles: String = {
    project.instructors.map(_.title.map(_.name).getOrElse("")).mkString(",")
  }

  def getDiscipline: String = {
    project.discipline.code + project.discipline.name
  }

  def getDepartmentName: String = {
    project.manager.get.std.state.get.department.name
  }

  def getSummary: String = {
    project.intro.map(_.summary).getOrElse("--")
  }

}
