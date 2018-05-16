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
package info.osdevelopment.sysemu.memory

/**
  * A read-only memory backed by an array.
  * @param data the data for the read-only memory.
  */
class ArrayReadOnlyMemory (val data: Array[Byte]) extends ReadOnlyMemory {

  /** Return the size of the memory. */
  override def size: Long = data.length

  /**
    * The implementation of the read.
    * @param address the address that should be read
    * @return the byte read at the given address
    */
  override protected def doRead(address: Long): Byte = {
    data(address.asInstanceOf[Int])
  }

}
