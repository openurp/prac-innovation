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
package org.openurp.edu.innovation.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.innovation.model.{ Batch, StageType }
import org.openurp.edu.innovation.model.Stage
import java.time.LocalDate
import java.time.ZoneId
import java.time.LocalDateTime
import java.time.Instant

class BatchAction extends RestfulAction[Batch] {

  override protected def editSetting(entity: Batch): Unit = {
    val stageTypes = entityDao.search(OqlBuilder.from(classOf[StageType]))
    put("stageTypes", stageTypes)
  }

  override protected def saveAndRedirect(entity: Batch): View = {
    val stageTypes = entityDao.getAll(classOf[StageType])
    stageTypes foreach { st =>
      val stage =
        entity.stages.find(_.stageType == st) match {
          case None =>
            val s = new Stage()
            s.stageType = st
            s.batch = entity
            entity.stages += s
            s
          case Some(s) => s
        }

      var i = 0
      getDateTime("stageType" + st.id + ".beginAt") foreach { beginAt =>
        stage.beginAt = beginAt.atZone(ZoneId.systemDefault).toInstant
        i += 1
      }

      getDateTime("stageType" + st.id + ".endAt") foreach { endAt =>
        stage.endAt = endAt.atZone(ZoneId.systemDefault).toInstant
        i += 1
      }

      if (i < 2) {
        entity.stages -= stage
      }
    }

    if (!entity.stages.isEmpty) {
      val dateOrdering = new Ordering[Instant]() {
        def compare(a: Instant, b: Instant) = a.compareTo(b)
      }
      entity.beginOn = LocalDateTime.ofInstant(entity.stages.map(_.beginAt).min(dateOrdering), ZoneId.systemDefault).toLocalDate
      entity.endOn = LocalDateTime.ofInstant(entity.stages.map(_.endAt).max(dateOrdering), ZoneId.systemDefault).toLocalDate
    }
    super.saveAndRedirect(entity)
  }
}
