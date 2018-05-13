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
package info.osdevelopment.sysemu.config

import com.typesafe.config.ConfigFactory
import scala.util.Try

/**
  * The global configuration of the application, read from `application.conf`.
  */
trait Configuration {

  /** The config read from the file. */
  val config = ConfigFactory.load

  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")

  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)

}
