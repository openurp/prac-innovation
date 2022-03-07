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

import java.util.Random

import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.prac.innovation.model.Expert

class ExpertAction extends RestfulAction[Expert] {

  def genPassword(): View = {
    val experts = entityDao.find(classOf[Expert], longIds("expert"))
    val rdm = new Random
    experts foreach { expert =>
      expert.password = random(8, true, true, rdm)
    }
    entityDao.saveOrUpdate(experts)
    redirect("search", "info.save.success")
  }

  def updateBeginOn(): View = {
    val experts = entityDao.find(classOf[Expert], longIds("expert"))
    getDate("beginOn") foreach { endOn =>
      experts foreach { expert =>
        expert.beginOn = endOn
      }
    }
    entityDao.saveOrUpdate(experts)
    redirect("search", "info.save.success")
  }

  def updateEndOn(): View = {
    val experts = entityDao.find(classOf[Expert], longIds("expert"))
    getDate("endOn") foreach { endOn =>
      experts foreach { expert =>
        expert.endOn = endOn
      }
    }
    entityDao.saveOrUpdate(experts)
    redirect("search", "info.save.success")
  }

  def random(cnt: Int, letters: Boolean, numbers: Boolean, random: Random): String = {
    if (cnt <= 0) return ""
    val builder = new java.lang.StringBuilder(cnt)
    var count = cnt
    val gap = ('z' + 1) - ' '
    while (count > 0) {
      val codePoint = random.nextInt(gap)
      var validPoint = true;
      Character.getType(codePoint) match {
        case Character.UNASSIGNED | Character.PRIVATE_USE | Character.SURROGATE =>
          validPoint = false
        case _ =>
      }
      if (validPoint) {
        val numberOfChars = Character.charCount(codePoint)
        var hasSpace = true
        if (count == 1 && numberOfChars > 1) {
          hasSpace = false
        }
        if (hasSpace) {
          if (letters && Character.isLetter(codePoint) || numbers && Character.isDigit(codePoint) || !letters && !numbers) {
            builder.appendCodePoint(codePoint)
            count -= numberOfChars
          }
        }
      }
    }
    builder.toString
  }
}
