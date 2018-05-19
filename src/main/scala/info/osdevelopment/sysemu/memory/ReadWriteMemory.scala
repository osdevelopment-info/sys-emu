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
import scala.util.{Failure, Try}

/**
  * Companion object used to create a new read-write memory up to 2^60^ (1 EiB). Please note that the memory will be
  * allocated immediately at creation on your host.
  */
object ReadWriteMemory {

  /**
    * Creates a default read-write memory with 1 GiB.
    * @return a read-write memory with 1 GiB
    */
  def apply(): Try[ReadWriteMemory] = {
    apply(1.Gi)
  }

  /**
    * Creates a read-write memory with the given `size`.
    * @param size the size of the memory
    * @return the read-write memory with the given size
    * @throws IllegalArgumentException when the size is either negative or too large
    */
  @throws[IllegalArgumentException]
  def apply(size: Long): Try[ReadWriteMemory] = {
    if (size <= 0 | size > 1.Ei) Failure(new IllegalArgumentException("Max size supported is 1 EiB"))
    else {
      if (size > 1.Gi) {
        CombinedReadWriteMemory(size)
      } else {
        SimpleReadWriteMemory(size)
      }
    }
  }

}

abstract class ReadWriteMemory protected() extends Memory {

  /**
    * Read a single byte from the memory at the given address.
    * @param address the `address` to read from
    * @return the byte read.
    * @throws IllegalAddressException when the address is outside the memory
    */
  @throws[IllegalAddressException]
  override final def readByte(address: Long): Try[Byte] = {
    if (address < 0 | address >= size) Failure(new IllegalAddressException("Address outside memory"))
    else doRead(address)
  }

  /**
    * The read method to be implemented by a subclass.
    * @param address the address to read
    * @return the byte read at the given address
    */
  protected def doRead(address: Long): Try[Byte]

  /**
    * Write a single [[scala.Byte Byte]] to the memory at the given address.
    * @param address the `address` to write to
    * @param value the `value` to write
    * @throws IllegalAddressException when the address is outside the range of the memory
    */
  @throws[IllegalAddressException]
  override final def writeByte(address: Long, value: Byte): Try[Unit] = {
    if (address < 0 | address >= size) Failure(new IllegalAddressException("Address outside memory"))
    else doWrite(address, value)
  }

  /**
    * The write method to be implemented by a subclass.
    * @param address the address to write
    * @param value the `value` to write
    */
  protected def doWrite(address: Long, value: Byte): Try[Unit]

}
