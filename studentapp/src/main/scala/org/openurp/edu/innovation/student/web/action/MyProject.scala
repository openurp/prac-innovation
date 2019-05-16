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

import org.beangle.commons.activation.MimeTypes
import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.commons.lang.Strings
import java.io.ByteArrayInputStream
import org.beangle.webmvc.api.view.{View, Stream}
import org.openurp.edu.innovation.model.Attachment
import org.beangle.webmvc.api.action.EntitySupport
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.edu.innovation.model.StageType
import org.openurp.edu.innovation.model.Project

trait MyProject {
  self: EntityAction[_] =>

  def attachment(): View = {
    val attachment = entityDao.get(classOf[Attachment], longId("attachment"))
    Stream(new ByteArrayInputStream(attachment.content), decideContentType(attachment.fileName),
      attachment.fileName)
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(Strings.substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }

  def isIntime(project: Project, stageTypeId: Int): Boolean = {
    val s = new StageType(stageTypeId)
    project.batch.getStage(s) match {
      case None => false
      case Some(stage) => stage.intime
    }
  }
}
