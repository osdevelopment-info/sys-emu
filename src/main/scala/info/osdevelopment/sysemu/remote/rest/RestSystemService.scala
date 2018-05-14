/* sys-emu - A system emulator
 * Copyright (C) 2018 U. Plonus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.osdevelopment.sysemu.remote.rest

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Location, `Content-Type`}
import akka.http.scaladsl.server.Directives._
import info.osdevelopment.sysemu.remote.json.SysEmuJsonProtocol
import info.osdevelopment.sysemu.system.{System, Systems}
import java.util.UUID
import org.slf4j.LoggerFactory
import spray.json.{JsObject, JsString}

class RestSystemService extends SysEmuJsonProtocol {

  val log = LoggerFactory getLogger classOf[RestSystemService]

  //var system: Option[System] = None

  def route = {
    respondWithHeader(`Content-Type`(MediaTypes.`application/json`))
    path(JavaUUID) { uuid =>
      get {
        Systems.byUUID(uuid) match {
          case Some(system) => complete(StatusCodes.OK, system)
          case None => complete(StatusCodes.NotFound,
            JsObject(("message", JsString("No system with UUID " + uuid + " can be found."))))
        }
      } ~
      delete {
        if (Systems.remove(uuid)) {
          complete(StatusCodes.Accepted,
            JsObject(("message", JsString("Removed system with UUID " + uuid + "."))))
        } else {
          complete(StatusCodes.NotFound,
            JsObject(("message", JsString("No system with UUID " + uuid + " can be found."))))
        }
      }
    } ~
    pathEndOrSingleSlash {
      get {
        complete(StatusCodes.OK, Systems.all)
      } ~
      post {
        extractUri { uri =>
          val uuid = UUID.randomUUID
          val system = Some(new System(uuid))
          Systems.add(system)
          complete(StatusCodes.Created, List(Location(uri.copy(path = uri.path + "/" + uuid.toString))), system)
        }
      }
    }
  }

}
