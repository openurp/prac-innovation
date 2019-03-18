package org.openurp.edu.innovation.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{ Coded, Named }
import org.openurp.code.edu.model.Discipline

class Expert extends LongId with Coded with Named {

  var intro: String = _

  var discipline: Discipline = _
}