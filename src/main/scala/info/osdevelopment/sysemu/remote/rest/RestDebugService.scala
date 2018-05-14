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
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.Directives._
import info.osdevelopment.sysemu.processor.Processor
import info.osdevelopment.sysemu.remote.json.SysEmuJsonProtocol
import java.util.ServiceLoader
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import spray.json.{JsObject, JsString}

class RestDebugService(val server: ActorRef) extends SysEmuJsonProtocol {

  val log = LoggerFactory getLogger classOf[RestDebugService]

  val systemService = new RestSystemService

  def route = {
    pathPrefix("v0.1") {
      pathPrefix("systems") {
        systemService.route
      } ~
      pathPrefix("processors") {
        pathPrefix(Segment) { name =>
          get {
            val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
            val processorTry = Try(processorLoader.asScala.find(_.name == name))
            processorTry match {
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Malformed service in server")
              case Success(processor) =>
                processor.find(_.name == name) match {
                  case Some(processor) =>
                    complete(StatusCodes.OK, processor)
                  case None =>
                    complete(StatusCodes.NotFound,
                      JsObject(("message", JsString("No processor with name " + name + " can be found."))))
                }
            }
          }
        } ~
        get {
          respondWithHeader(`Content-Type`(MediaTypes.`application/json`))
          val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
          val processors = processorLoader.asScala.map((p) => p.name)
          complete(StatusCodes.OK, processors)
        }
      } ~
      pathPrefix("shutdown") {
        post {
          server ! "shutdown"
          complete(StatusCodes.OK)
        }
      } ~
      get {
        complete(StatusCodes.OK)
      }
    }
  }

}
