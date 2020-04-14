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
package org.openurp.edu.innovation.web.action.expert

import java.io.ByteArrayInputStream
import java.time.{Instant, LocalDate}

import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.lang.Strings
import org.beangle.commons.web.util.{CookieUtils, RequestUtils}
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.EntityAction
import org.openurp.edu.innovation.model._

class ReviewAction extends EntityAction[Review] with ServletSupport {

  def index(): View = {
    forward()
  }

  def login(@param("code") code: String, @param("password") password: String): View = {
    val builder = OqlBuilder.from(classOf[Expert], "expert")
    builder.where("expert.code=:code and expert.password=:password", code, password)
    val experts = entityDao.search(builder)
    if (experts.isEmpty) {
      this.addFlashMessage("用户和密码错误")
      redirect("index", null)
    } else {
      val expert = experts.head
      if (!expert.within(LocalDate.now)) {
        this.addFlashMessage("账号过期")
        redirect("index", null)
      } else {
        addCredential(expert)
        redirect("project", null)
      }
    }
  }

  def logout(): View = {
    CookieUtils.deleteCookieByName(request, response, "expert_code")
    CookieUtils.deleteCookieByName(request, response, "expert_credential")
    redirect("index", null)
  }

  def project(): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      val expert = expert2.head
      val builder = OqlBuilder.from(classOf[Review], "review")
      builder.where("review.expert = :expert", expert)
      builder.orderBy("review.project.title")
      val reviews = entityDao.search(builder)
      var review = reviews.head
      getLong("review.id") foreach { id =>
        reviews.find(_.id == id) foreach { r => review = r }
      }
      put("review", review)
      put("reviews", reviews)
      put("expert", expert)
      put("ClosureStageId", StageType.Closure)
      put("levels", entityDao.getAll(classOf[ProjectLevel]))
      forward()
    }
  }

  def info(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      put("review", getReview(id, expert2.head))
      forward()
    }
  }

  def save(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      val review = getReview(id, expert2.head)
      this.populate(review, "review")
      review.updatedAt = Instant.now
      entityDao.saveOrUpdate(review)
      var allReviewed = true
      var sum: Float = 0f
      review.project.reviews foreach { r =>
        r.score match {
          case Some(s) => sum += s
          case None => allReviewed = false
        }
      }
      if (allReviewed) {
        review.project.reviewScore = Some(sum / review.project.reviews.size)
        entityDao.saveOrUpdate(review.project)
      }
      redirect("project", "&id=" + review.id, "info.save.success")
    }
  }

  def attachment(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      val review = getReview(id, expert2.head)
      val materials = entityDao.findBy(classOf[Material], "project", List(review.project))
      if (materials.isEmpty) {
        null
      } else {
        materials.find(_.stageType.id == StageType.Closure) match {
          case Some(material) =>
            val attachment = material.attachment
            Stream(new ByteArrayInputStream(attachment.content), decideContentType(attachment.fileName),
              attachment.fileName)
          case None => null
        }
      }
    }
  }

  private def getReview(id: String, expert: Expert): Review = {
    val builder = OqlBuilder.from(classOf[Review], "review")
    builder.where("review.expert=:expert", expert)
    builder.where("review.id=:id", id.toLong)
    entityDao.search(builder).head
  }

  private def decideContentType(fileName: String): String = {
    MediaTypes.get(Strings.substringAfterLast(fileName, "."), MediaTypes.ApplicationOctetStream).toString
  }

  private def addCredential(expert: Expert): Unit = {
    CookieUtils.addCookie(request, response, "expert_code", expert.code, -1)
    CookieUtils.addCookie(request, response, "expert_credential", genCredential(expert), -1)
  }

  private def genCredential(expert: Expert): String = {
    Digests.md5Hex(expert.code + "_" + expert.password + "_" + RequestUtils.getIpAddr(request))
  }

  private def getExpert: Option[Expert] = {
    val expertCode = CookieUtils.getCookieValue(request, "expert_code")
    val expertCredential = CookieUtils.getCookieValue(request, "expert_credential")
    val experts = entityDao.findBy(classOf[Expert], "code", List(expertCode))
    if (experts.nonEmpty) {
      val expert = experts.head
      if (expertCredential == genCredential(expert)) {
        Some(expert)
      } else {
        None
      }
    } else {
      None
    }
  }
}
