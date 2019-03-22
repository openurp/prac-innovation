package org.openurp.edu.innovation.model

import org.beangle.data.model.LongId
import org.openurp.base.model.Department
import org.beangle.data.model.pojo.TemporalAt
import java.time.LocalDate
import org.openurp.edu.base.model.Classroom
import org.beangle.data.model.pojo.InstantRange
import org.beangle.commons.lang.time.HourMinute

class Lesson extends LongId {

  var crn: String = _

  var subject: String = _

  var teachers: String = _

  var teachDepart: Department = _

  var date: LocalDate = _
  
  var beginAt: HourMinute = _
  
  var endAt: HourMinute = _

  var room: Option[Classroom] = _

  var location: Option[String] = _

  var capacity: Int = _

  var actual: Int = _
}