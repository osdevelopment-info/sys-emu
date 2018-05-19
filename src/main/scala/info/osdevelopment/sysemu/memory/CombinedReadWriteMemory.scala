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
import scala.util.{Failure, Success, Try}

/**
  * The object CombinedReadWriteMemory used to create instances of the memory. The maximum memory that can be handled is
  * 1 EiB.
  */
object CombinedReadWriteMemory {

  /**
    * Creates a read-write memory with the given `size`.
    * @param size the size of the memory
    * @return a read-write memory with the given size
    * @throws IllegalArgumentException when the memory size is either negative or too large
    */
  def apply(size: Long): Try[CombinedReadWriteMemory] = {
    if (size <= 0 | size > 1.Ei) Failure(new IllegalArgumentException("Max size supported is 1 EiB"))
    else Try(new CombinedReadWriteMemory(size))
  }

}

/**
  * A read-write memory that can hold up to 2^60^ Bytes (1 EiB). Please note that the size is immediately allocated.
  * @param size
  */
class CombinedReadWriteMemory private(val size: Long) extends ReadWriteMemory {

  /** The size of the last "memory module". */
  private val remaining = size % 1.Gi
  /** The number of modules needed to create the memory. */
  private val numberModules = (if (remaining == 0) size / 1.Gi else size / 1.Gi + 1).asInstanceOf[Int]
  /** The modules for the memory. */
  private val modules = Array
    .fill(numberModules){ SimpleReadWriteMemory(1.Gi.asInstanceOf[Int]) }
    .collect{ case Success(m) => m}
  if (modules.length != numberModules) {
    throw new IllegalStateException("Cannot create ReadWriteMemory")
  }

  /**
    * The read method to read the value from the array of arrays.
    * @param address the address to read
    * @return the byte read at the given address
    */
  protected override def doRead(address: Long): Try[Byte] = {
    val module = address / 1.Gi
    val offset = address % 1.Gi
    modules(module.asInstanceOf[Int]).readByte(offset)
  }

  /**
    * The write method to write the value to the array.
    * @param address the address to write
    * @param value the `value` to write
    * @return `Some` if the write was successful, `None` otherwise
    */
  protected override def doWrite(address: Long, value: Byte): Try[Unit] = {
    val module = address / 1.Gi
    val offset = address % 1.Gi
    modules(module.asInstanceOf[Int]).writeByte(offset.asInstanceOf[Int], value)
  }

}
