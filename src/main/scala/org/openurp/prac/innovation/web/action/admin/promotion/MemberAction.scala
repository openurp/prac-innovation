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

package org.openurp.prac.innovation.web.action.admin.promotion

import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.prac.innovation.model.{Batch, PromotionDefenseMember}

class MemberAction extends RestfulAction[PromotionDefenseMember], ExportSupport[PromotionDefenseMember] {
  protected override def indexSetting(): Unit = {
    val batches = entityDao.getAll(classOf[Batch])
    put("batches", batches.sortBy(_.name).reverse)
  }
}
