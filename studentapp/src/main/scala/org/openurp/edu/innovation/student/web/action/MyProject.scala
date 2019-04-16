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