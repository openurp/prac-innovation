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
package org.openurp.edu.innovation.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ DateRange, Named }
import org.beangle.data.model.pojo.Remark
import java.time.LocalDate

class Stage extends IntId with DateRange with Remark {

  var stageType: StageType = _

  var batch: Batch = _

  def intime: Boolean = {
    val now = LocalDate.now
    !endOn.isBefore(now) && !now.isBefore(beginOn)
  }
}
