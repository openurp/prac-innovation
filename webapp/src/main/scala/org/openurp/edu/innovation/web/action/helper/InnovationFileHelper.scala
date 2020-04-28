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
package org.openurp.edu.innovation.web.action.helper

import java.io.{File, InputStream}

import org.beangle.blob.model.{BlobMeta, Uploader}
import org.openurp.app.Urp

object InnovationFileHelper {

  val uploader = new Uploader(Urp.home + "/blobs", "edu", "innovation")

  def remove(path: String): Unit = {
    uploader.remove(path)
  }

  def get(path: String): Option[File] = {
    uploader.get(path)
  }

  def upload(batch: String, name: String, is: InputStream): BlobMeta = {
    uploader.upload(batch, name, is)
  }
}
