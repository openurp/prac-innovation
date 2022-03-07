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

package org.openurp.prac.innovation.web.action.expert

import org.beangle.commons.codec.digest.Digests
import org.beangle.data.dao.OqlBuilder
import org.beangle.ems.app.EmsApp
import org.beangle.web.action.annotation.param
import org.beangle.web.action.support.ServletSupport
import org.beangle.web.action.view.View
import org.beangle.web.servlet.util.{CookieUtils, RequestUtils}
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.prac.innovation.model.*

import java.time.{Instant, LocalDate}

class InitReviewAction extends EntityAction[InitReviewDetail] with ServletSupport {

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
      val builder = OqlBuilder.from(classOf[InitReviewDetail], "detail")
      builder.where("detail.expert = :expert", expert)
      //按照标题排序
      builder.orderBy("detail.review.project.title")
      val details = entityDao.search(builder)
      var detail = details.head
      getLong("detail.id") foreach { id =>
        details.find(_.id == id) foreach { r => detail = r }
      }
      put("detail", detail)
      put("details", details)
      put("expert", expert)
      put("InitStageId", StageType.Initial)
      forward()
    }
  }

  def info(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      put("detail", getInitReviewDetail(id, expert2.head))
      forward()
    }
  }

  def save(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      val reviewDetail = getInitReviewDetail(id, expert2.head)
      this.populate(reviewDetail, "detail")
      reviewDetail.updatedAt = Instant.now
      entityDao.saveOrUpdate(reviewDetail)
      var allReviewed = true
      var sum: Float = 0f
      reviewDetail.review.details foreach { r =>
        r.score match {
          case Some(s) => sum += s
          case None => allReviewed = false
        }
      }
      if (allReviewed) {
        reviewDetail.review.score = Some(sum / reviewDetail.review.details.size)
        entityDao.saveOrUpdate(reviewDetail.review)
      }
      redirect("project", "&id=" + reviewDetail.id, "info.save.success")
    }
  }

  def attachment(@param("id") id: String): View = {
    val expert2 = getExpert
    if (expert2.isEmpty) {
      redirect("index", null)
    } else {
      val detail = getInitReviewDetail(id, expert2.head)
      val materials = entityDao.findBy(classOf[Material], "project", List(detail.review.project))
      if (materials.isEmpty) {
        null
      } else {
        materials.find(_.stageType.id == StageType.Initial) match {
          case Some(material) =>
            val path = EmsApp.getBlobRepository(true).url(material.path)
            response.sendRedirect(path.get.toString)
            null
          case None => null
        }
      }
    }
  }

  private def getInitReviewDetail(id: String, expert: Expert): InitReviewDetail = {
    val builder = OqlBuilder.from(classOf[InitReviewDetail], "detail")
    builder.where("detail.expert=:expert", expert)
    builder.where("detail.id=:id", id.toLong)
    entityDao.search(builder).head
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
