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
import scala.util.{Failure, Success, Try}

/**
  * Companion object used to create a new read-only memory.
  */
object ReadOnlyMemory {

  /**
    * Create a read-only memory that is backed by a [[scala.Byte Byte]] array.
    * @param data the data of the read-only memory
    * @return a [[Memory]]
    */
  def apply(data: Array[Byte]): Try[ReadOnlyMemory] = {
    Try(new ArrayReadOnlyMemory(data))
  }

  /**
    * Create a read-only memory that is backed by a `SeekableByteChannel`.
    * @param data the data of the read-only memory
    * @return a [[Memory]]
    */
  def apply(data: SeekableByteChannel): Try[ReadOnlyMemory] = {
    Try(new ChannelReadOnlyMemory(data))
  }

}

/**
  * An abstract class to emulate a read-only memory. Subclasses can implement different possibilities to read the
  * content.
  */
abstract class ReadOnlyMemory protected() extends Memory {

  /**
    * Read a single byte from the memory at the given address.
    * @return a `Success` with the byte read, `Failure` otherwise
    */
  override final def readByte(address: Long): Try[Byte] = {
    if (address < 0 | address >= size) Failure(new IllegalAddressException("Address outside memory"))
    else doRead(address)
  }

  /**
    * The read method to be implemented by a subclass.
    * @param address the address to read
    * @return a `Success` with the byte read at the given address, `Failure` otherwise
    */
  protected def doRead(address: Long): Try[Byte]

  /**
    * Write a single byte to the memory at the given address.
    * @param address the address to write
    * @param value the value to write
    * @return a `Failure` when the address is outside the area of the memory
    */
  override final def writeByte(address: Long, value: Byte): Try[Unit] = {
    if (address < 0 | address >= size) Failure(new IllegalAddressException("Address outside memory"))
    else Success((): Unit)
  }

}
