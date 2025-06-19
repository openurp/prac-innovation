/*
 * Copyright (C) 2014, The OpenURP Software.
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

package org.openurp.prac.innovation.web.action.admin

import java.time.{Instant, LocalDateTime, ZoneId}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.starter.web.support.ProjectSupport
import org.openurp.prac.innovation.model.{Batch, Stage, StageType}

class BatchAction extends RestfulAction[Batch] with ProjectSupport {

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

      stage.noticeHref = get("stageType_noticeHref" + st.id)

      getDateTime("stageType" + st.id + ".endAt") foreach { endAt =>
        stage.endAt = endAt.atZone(ZoneId.systemDefault).toInstant
        i += 1
      }

      if (i < 2) {
        entity.stages -= stage
      }
    }

    if (entity.stages.nonEmpty) {
      val dateOrdering = new Ordering[Instant]() {
        def compare(a: Instant, b: Instant): Int = a.compareTo(b)
      }
      entity.beginOn = LocalDateTime.ofInstant(entity.stages.map(_.beginAt).min(dateOrdering), ZoneId.systemDefault).toLocalDate
      entity.endOn = LocalDateTime.ofInstant(entity.stages.map(_.endAt).max(dateOrdering), ZoneId.systemDefault).toLocalDate
    }
    entity.school = getProject.school
    super.saveAndRedirect(entity)
  }
}
