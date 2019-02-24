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

import org.beangle.data.orm.MappingModule

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    bind[Discipline]
    bind[Member]
    bind[Project].on(e => declare(
      e.members is depends("project")))

    bind[Intro].on(e => declare(
      e.summary is length(500),
      e.innovation is length(200),
      e.product is length(200)))

    bind[ProjectCategory]
    bind[ProjectLevel]
    bind[ProjectState]

    bind[Session]

    bind[Section].on(e => declare(
      e.children is depends("parent"),
      e.name is length(100),
      e.remark is length(100)))

    bind[Template].on(e => declare(
      e.sections is depends("template")))
  }
}
