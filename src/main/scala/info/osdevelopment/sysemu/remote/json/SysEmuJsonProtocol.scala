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
package info.osdevelopment.sysemu.remote.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import info.osdevelopment.sysemu.system.System
import java.util.UUID
import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonFormat}

trait SysEmuJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object SystemJsonFormat extends RootJsonFormat[System] {

    override def read(json: JsValue): System = {
      json.asJsObject.getFields("uuid") match {
        case Seq(JsString(uuid)) =>
          new System(Some(UUID.fromString(uuid)))
      }
    }

    override def write(obj: System): JsValue = {
      JsObject(("uuid", JsString(obj.uuid.get.toString)))
    }

  }

}
