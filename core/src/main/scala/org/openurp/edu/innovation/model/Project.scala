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
package org.openurp.edu.innovation.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.TemporalOn
import org.beangle.commons.collection.Collections
import org.openurp.base.model.Department
import org.openurp.base.model.User
/**
 * 项目
 */
class Project extends LongId with TemporalOn {

  /** 项目编号 */
  var code: Option[String] = _

  /**批次*/
  var session: Session = _

  /**项目名称*/
  var title: String = _

  /**成员*/
  var members = Collections.newBuffer[Member]

  /**院系*/
  var department: Department = _

  /**申请人*/
  var manager: Member = _

  /**项目级别*/
  var level: Option[ProjectLevel] = None

  /**项目类型*/
  var category: ProjectCategory = _

  /**学科*/
  var discipline: Discipline = _

  /**指导老师*/
  var teachers = Collections.newBuffer[User]

  /**状态*/
  var state: ProjectState = _

  /**简介*/
  var intro: Intro = _
}
