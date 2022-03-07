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

package org.openurp.prac.innovation.web.action.admin

import java.time.Instant

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportSetting
import org.beangle.web.action.annotation.ignore
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.prac.innovation.model._
import org.openurp.prac.innovation.web.action.helper.ExportProject

class LevelAction extends RestfulAction[Project] {

  protected override def indexSetting(): Unit = {
    val query = OqlBuilder.from(classOf[LevelJounal], "j")
    query.select("distinct j.year").orderBy("j.year desc")
    put("years", entityDao.search(query))
    val batches = entityDao.getAll(classOf[Batch])
    put("batches", batches)
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
    put("projectCategories", entityDao.getAll(classOf[ProjectCategory]))
    val departQuery = OqlBuilder.from(classOf[Department], "d").where("d.endOn is null or d.endOn >=:endOn", batches.head.beginOn)
    put("departments", entityDao.search(departQuery))
  }

  protected override def getQueryBuilder: OqlBuilder[Project] = {
    val builder = super.getQueryBuilder
    get("student") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from project.members m where m.std.user.code like :stdCode or m.std.user.name like :stdName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    get("instructor") foreach { m =>
      if (!m.isEmpty) {
        builder.where(
          "exists(from project.instructors i where i.user.code like :teacherCode or i.user.name like :teacherName)",
          "%" + m + "%", "%" + m + "%")
      }
    }
    val year = getInt("year")
    val levelId = getInt("level.id")
    if (year.isDefined && levelId.isDefined) {
      builder.where("exists(from project.levels l where l.year=:year and l.level.id=:levelId)", year.get, levelId.get)
    } else if (year.isDefined) {
      builder.where("exists(from project.levels l where l.year=:year)", year.get)
    } else if (levelId.isDefined) {
      builder.where("exists(from project.levels l where l.level.id=:levelId)", levelId.get)
    }
    builder
  }

  def levelSetting: View = {
    put("projectLevels", entityDao.getAll(classOf[ProjectLevel]))
    val projects = entityDao.find(classOf[Project], longIds("project"))
    put("projects", projects)
    forward()
  }

  def updateLevel(): View = {
    val projects = entityDao.find(classOf[Project], longIds("project"))
    val levels = entityDao.getAll(classOf[ProjectLevel])
    projects.foreach { p =>
      levels foreach { level =>
        getInt("year_level_" + level.id) match {
          case None =>
            p.levels.find(j => j.level == level) foreach (lj => p.levels -= lj)
          case Some(year) =>
            p.levels.find(j => j.level == level) match {
              case None =>
                val lj = new LevelJounal(year, p, level)
                p.levels += lj
              case Some(j) => j.year = year
                j.updatedAt = Instant.now
            }
        }
      }
    }
    entityDao.saveOrUpdate(projects)
    redirect("search", "info.save.success")
  }

  @ignore
  override def configExport(setting: ExportSetting): Unit = {
    val query = getQueryBuilder
    query.limit(null)
    val closures = entityDao.search(query);
    val projects = closures.map(x => new ExportProject(x))
    setting.context.put("items", projects)
  }
}
