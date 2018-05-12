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

import info.osdevelopment.sysemu.support.Utilities._

object ReadWriteMemory {

  def apply(): ReadWriteMemory = {
    apply(1.Gi)
  }

  def apply(size: Long): ReadWriteMemory = {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0")
    }
    if (size > 1.Ei) {
      throw new IllegalArgumentException("Max size supported is 1 EiB")
    }
    if (size > 1.Gi) {
      CombinedReadWriteMemory(size)
    } else {
      SimpleReadWriteMemory(size)
    }
  }

}

abstract class ReadWriteMemory protected() extends Memory {

  /**
    * Read a single byte from the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def readByte(address: Long): Byte = {
    if (address < 0 | address >= size) throw new IllegalAddressException("Address outside memory")
    doRead(address)
  }

  protected def doRead(address: Long): Byte

  /**
    * Write a single byte to the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def writeByte(address: Long, value: Byte): Unit = {
    if (address < 0 | address >= size) throw new IllegalAddressException("Address outside memory")
    doWrite(address, value)
  }

  protected def doWrite(address: Long, value: Byte)

}
