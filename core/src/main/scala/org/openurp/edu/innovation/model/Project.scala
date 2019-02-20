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

  /**成员*/
  var teachers = Collections.newBuffer[User]

  /**状态*/
  var state: ProjectState = _
}
