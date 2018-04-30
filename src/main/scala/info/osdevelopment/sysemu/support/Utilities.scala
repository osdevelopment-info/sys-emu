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
package info.osdevelopment.sysemu.support

object Utilities {

  implicit class BinaryUnitLong(val self: Long) extends AnyVal {

    def Ki: Long = self * 1024L

    def Mi: Long = self * 1024L * 1024L

    def Gi: Long = self * 1024L * 1024L * 1024L

    def Ti: Long = self * 1024L * 1024L * 1024L * 1024L

    def Pi: Long = self * 1024L * 1024L * 1024L * 1024L * 1024L

    def Ei: Long = self * 1024L * 1024L * 1024L * 1024L * 1024L * 1024L

  }

}
