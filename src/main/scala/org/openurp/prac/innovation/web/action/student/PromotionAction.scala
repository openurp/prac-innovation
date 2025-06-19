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

package org.openurp.prac.innovation.web.action.student

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.Securities
import org.beangle.webmvc.support.{ActionSupport, ServletSupport}
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.prac.innovation.model.*

import java.time.Instant
import scala.util.Random

class PromotionAction extends ActionSupport, EntityAction[Project], ServletSupport, MyProject {

  var entityDao: EntityDao = _

  def index(): View = {
    val stateLevel = entityDao.get(classOf[ProjectLevel], ProjectLevel.School)
    put("stateLevel", stateLevel)
    put("promotionStage", new StageType(StageType.PromotionState))

    val m = OqlBuilder.from(classOf[PromotionDefenseMember], "m")
    m.where("m.project.manager.std.code=:code", Securities.user)

    val members = entityDao.search(m)
    put("members", members)

    val q = OqlBuilder.from(classOf[Stage], "stage")
    q.where("stage.stageType.id=:stageTypeId", StageType.Closure)
    q.where(":now between stage.beginAt and stage.endAt", Instant.now)
    val stage = entityDao.search(q).headOption
    put("stage", stage)
    stage match {
      case None => put("projects", List.empty)
      case Some(s) =>
        val query = OqlBuilder.from(classOf[Project], "p")
        query.where("p.manager.std.code=:code", Securities.user)
        query.where("p.batch=:batch", s.batch)
        val projects = entityDao.search(query).filter { x =>
            x.levels.exists(_.level == stateLevel) && !members.exists(_.project == x)
        }
        put("projects", projects)
    }
    forward()
  }

  def draw(): View = {
    val project = entityDao.findBy(classOf[Project], "id" -> getLongId("project"), "manager.std.code" -> Securities.user).head
    val members = entityDao.findBy(classOf[PromotionDefenseMember], "project", project)
    if (members.isEmpty) {
      var groups = entityDao.findBy(classOf[PromotionDefenseGroup], "batch", project.batch).filter(x => x.members.size < x.capacity)
      groups = Random.shuffle(groups)
      val group = groups.head
      var seats = (1 to group.capacity).toBuffer
      group.members foreach { x => seats.subtractOne(x.idx) }
      seats = Random.shuffle(seats)
      val member = new PromotionDefenseMember
      member.idx = seats.head
      member.project = project
      member.group = group
      member.updatedAt = Instant.now
      group.members.addOne(member)
      entityDao.saveOrUpdate(group, member)
    }

    redirect("index", "抽签成功")
  }
}
