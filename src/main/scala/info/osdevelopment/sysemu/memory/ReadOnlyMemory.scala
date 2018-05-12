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

import java.nio.channels.SeekableByteChannel

object ReadOnlyMemory {

  def apply(data: Array[Byte]): ReadOnlyMemory = {
    new ArrayReadOnlyMemory(data)
  }

  def apply(data: SeekableByteChannel): ReadOnlyMemory = {
    new ChannelReadOnlyMemory(data)
  }

}

abstract class ReadOnlyMemory protected() extends Memory {

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
  }

}
