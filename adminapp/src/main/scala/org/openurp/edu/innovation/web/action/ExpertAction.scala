package org.openurp.edu.innovation.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.code.edu.model.Discipline
import org.openurp.edu.innovation.model.Expert

class ExpertAction extends RestfulAction[Expert] {

  override protected def editSetting(entity: Expert): Unit = {
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

}