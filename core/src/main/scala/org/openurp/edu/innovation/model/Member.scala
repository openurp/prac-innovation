package org.openurp.edu.innovation.model

import org.beangle.data.model.LongId
import org.openurp.base.model.User
import org.beangle.data.model.pojo.Remark

class Member extends LongId with Remark {

  var user: User = _

  var project: Project = _

  var duty: String = _

  var grade: String = _

  var major: String = _

  var hobby: String = _

  var phone: String = _

  var email: Option[String] = None
}