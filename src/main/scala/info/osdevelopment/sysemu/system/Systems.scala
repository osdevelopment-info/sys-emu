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
package info.osdevelopment.sysemu.system

import java.util.UUID
import scala.collection.mutable

object Systems {

  val systemMap = mutable.Map[UUID, Option[System]]()

  /**
    * Adds the given unknown system to the list of known systems. Returns true if the system has been added
    * successfully, false otherwise.
    * @param system the system to add
    * @return true, if the system was successfully added
    */
  def add(system: Option[System]): Boolean = {
    system match {
      case Some(sys) =>
        if (systemMap contains sys.uuid) {
          false
        } else {
          systemMap += (sys.uuid -> system)
          true
        }
      case _ => false
    }
  }

  /**
    * Remove the system with the given UUID from the list of systems. Returns true if the system was removed, false if
    * no system with the given UUID was found.
    * @param uuid the UUID of the system to delete
    * @return true, if the system existed and was removed
    */
  def remove(uuid: UUID): Boolean = {
    if (systemMap contains uuid) {
      systemMap remove uuid
      true
    } else {
      false
    }
  }

  def byUUID(uuid: UUID): Option[System] = {
    systemMap.getOrElse(uuid, None)
  }

  def all(): Iterable[System] = {
    systemMap.values.flatten(s => s)
  }

}
