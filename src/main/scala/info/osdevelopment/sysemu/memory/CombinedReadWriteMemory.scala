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

object CombinedReadWriteMemory {

  def apply(): CombinedReadWriteMemory = {
    return new CombinedReadWriteMemory(2.Gi)
  }

  def apply(size: Long): CombinedReadWriteMemory = {
    return new CombinedReadWriteMemory(size)
  }

}

/**
  * A read-write memory that can hold up to 2^60^ Bytes (1 EiB). Please note that the size is immediately allocated.
  * @param size
  */
class CombinedReadWriteMemory private(val size: Long) extends Memory {

  if (size <= 0) {
    throw new IllegalArgumentException("Size must be greater than 0")
  }
  if (size > 1.Ei) {
    throw new IllegalArgumentException("Max size supported is 1 EiB")
  }
  val remaining = size % 1.Gi
  val numberModules = (if (remaining == 0) size / (1.Gi) else size / (1.Gi) + 1).asInstanceOf[Int]
  val modules = Array.fill(numberModules){ SimpleReadWriteMemory((1.Gi).asInstanceOf[Int]) }

  /**
    * Read a single byte from the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override def readByte(address: Long): Byte = {
    if (address < 0 | address > size) throw new IllegalAddressException
    val module = address / (1.Gi)
    val offset = address % (1.Gi)
    modules(module.asInstanceOf[Int]).readByte(offset)
  }

  /**
    * Write a single byte to the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override def writeByte(address: Long, value: Byte): Unit = {
    if (address < 0 | address > size) throw new IllegalAddressException
    val module = address / (1.Gi)
    val offset = address % (1.Gi)
    modules(module.asInstanceOf[Int]).writeByte(offset.asInstanceOf[Int], value)
  }

}
