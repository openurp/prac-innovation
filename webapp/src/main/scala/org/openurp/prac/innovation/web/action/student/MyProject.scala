/*
 * Copyright (C) 2005, The OpenURP Software.
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

import org.beangle.ems.app.EmsApp
import org.beangle.web.action.support.ServletSupport
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.prac.innovation.model.{Material, Project, StageType}

trait MyProject {
  self: EntityAction[_] with ServletSupport =>

  def attachment(): View = {
    val material = entityDao.get(classOf[Material], longId("material"))
    val path = EmsApp.getBlobRepository(true).url(material.path)
    response.sendRedirect(path.get.toString)
    null
  }

  def isIntime(project: Project, stageTypeId: Int): Boolean = {
    val s = new StageType(stageTypeId)
    project.batch.getStage(s) match {
      case None => false
      case Some(stage) => stage.intime
    }
  }
}
