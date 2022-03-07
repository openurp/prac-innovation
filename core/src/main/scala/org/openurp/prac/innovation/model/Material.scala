/*
 * Copyright (C) 2005, The OpenURP Software.
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

package org.openurp.prac.innovation.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

class Material extends LongId with Updated {
  var project: Project = _
  var stageType: StageType = _
  var fileName: String = _
  var size: Int = _
  var sha: String = _
  var path: String = _

  def this(project: Project, stageType: StageType) = {
    this()
    this.project = project
    this.stageType = stageType
    project.materials += (this)
  }
}
