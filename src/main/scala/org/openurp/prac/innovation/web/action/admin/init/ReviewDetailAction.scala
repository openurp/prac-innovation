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

package org.openurp.prac.innovation.web.action.admin.init

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model._

class ReviewDetailAction extends RestfulAction[InitReviewDetail] {

  override def simpleEntityName: String = {
    "detail"
  }

  protected override def indexSetting(): Unit = {
    val batches = entityDao.search(OqlBuilder.from(classOf[Batch], "b").orderBy("b.beginOn desc"))
    put("batches", batches)
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

  def clear(): View = {
    val details = entityDao.find(classOf[InitReviewDetail], getLongIds("detail"))
    val reviews = Collections.newSet[InitReview]
    details foreach { r =>
      reviews += r.review
      r.score = None
      r.passed = None
      r.comments = None
      r.review.score = None
    }
    entityDao.saveOrUpdate(reviews, details)
    redirect("search", "info.save.success")
  }

}
