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

package org.openurp.prac.innovation.web.action.admin.init

import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.code.edu.model.Discipline
import org.openurp.prac.innovation.model.{Batch, Expert, InitReview, InitReviewGroup}

class ReviewGroupAction extends RestfulAction[InitReviewGroup] {

  override def simpleEntityName: String = {
    "reviewGroup"
  }

  protected override def indexSetting(): Unit = {
    val batches = entityDao.search(OqlBuilder.from(classOf[Batch], "b").orderBy("b.beginOn desc"))
    put("batches", batches)
  }

  override protected def editSetting(entity: InitReviewGroup): Unit = {
    val builder = OqlBuilder.from(classOf[Expert], "e")
    builder.where("not exists(from " + classOf[InitReviewGroup].getName + " rg join rg.experts as occupied where occupied=e and rg.batch=:batch)", entity.batch)
    val experts = entityDao.search(builder)
    put("experts", experts)
    put("disciplines", entityDao.getAll(classOf[Discipline]))
  }

  override def search(): View = {
    put("reviewGroups", entityDao.search(getQueryBuilder))
    val stat = OqlBuilder.from[Array[_]](classOf[InitReview].getName, "ir")
    stat.where("ir.project.batch.id=:batchId", intId("reviewGroup.batch"))
    stat.where("ir.group is not null")
    stat.select("ir.group.id,count(*)")
    stat.groupBy("ir.group.id")

    val stats = entityDao.search(stat).map(x => x(0).toString -> x(1)).toMap
    put("stats", stats)
    forward()
  }

  override protected def saveAndRedirect(entity: InitReviewGroup): View = {
    entity.experts.clear()
    entity.experts ++= entityDao.find(classOf[Expert], longIds("expert"))
    super.saveAndRedirect(entity)
  }
}
