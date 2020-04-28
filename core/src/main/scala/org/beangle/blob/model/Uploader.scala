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
package org.beangle.blob.model

import java.io.{ByteArrayOutputStream, File, FileOutputStream, InputStream}

import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.file.digest.Sha1
import org.beangle.commons.io.{Files, IOs}
import org.beangle.commons.lang.Strings

class Uploader(val base: String, val group: String, val owner: String) {

  def remove(batch: String, sha: String): Boolean = {
    remove(s"$batch/${sha}")
  }

  def remove(p: String): Boolean = {
    val path = s"$base/$group/$owner/${p}"
    val file = new File(path)
    if (file.exists()) {
      file.delete()
    } else {
      false
    }
  }

  def get(path: String): Option[File] = {
    val file = new File(s"$base/$group/$owner/${path}")
    if (file.exists()) {
      Some(file)
    } else {
      None
    }
  }

  def upload(batch: String, name: String, is: InputStream): BlobMeta = {
    val time=System.currentTimeMillis()
    val file = new File(s"$base/$group/$owner/$batch/${time}")
    Files.touch(file)
    IOs.copy(is,new FileOutputStream(file))
    val sha=Sha1.digest(file)
    val target=new File(s"$base/$group/$owner/${batch}/$sha")
    file.renameTo(target)
    val ext = getExt(name)
    val meta = new BlobMeta()
    meta.sha = sha
    meta.size=target.length().asInstanceOf[Int]
    meta.mediaType = MediaTypes.get(ext, MediaTypes.ApplicationOctetStream).toString()
    meta.path = s"/$batch/${sha}"
    meta
  }

  private def getExt(fileName: String): String = {
    Strings.substringAfterLast(fileName, ".")
  }
}
