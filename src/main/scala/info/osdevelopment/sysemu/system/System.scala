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
import org.slf4j.LoggerFactory

/**
  * A system (emulated computer) with a given set of hardware.
  *
  * @param uuid the UUID of the system to be created
  * @throws IllegalArgumentException if the given UUID is `null`
  */
@throws[IllegalArgumentException]
class System(val uuid: UUID) {

  /**
    * Creates a new system with a random UUID.
    * @return the created system
    */
  def this() = {
    this(UUID.randomUUID)
  }

  if (uuid == null) {
    throw new IllegalArgumentException("The UUID of a system may not be null.")
  }

  private val log = LoggerFactory getLogger classOf[System]

  /**
    * Executes a single step of the system (most likely of the processor).
    */
  def step: Unit = {
    log.info("System step")
  }

  /**
    * Runs the system.
    */
  def run: Unit = {
    log.info("System run")
  }

}
