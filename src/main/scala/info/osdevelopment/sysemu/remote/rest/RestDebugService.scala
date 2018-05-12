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

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory

class RestDebugService(val server: ActorRef) {

  val log = LoggerFactory getLogger classOf[RestDebugService]

  val systemService = new RestSystemService

  def route = {
    pathSingleSlash {
      get {
        complete(StatusCodes.OK)
      }
    } ~
    pathPrefix("system") {
      systemService.route
    } ~
    pathPrefix("shutdown") {
      post {
        server ! "shutdown"
        complete(StatusCodes.OK)
      }
    }
  }

}
