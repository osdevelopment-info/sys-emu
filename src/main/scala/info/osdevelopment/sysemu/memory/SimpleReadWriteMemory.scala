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
  * The object SimpleReadWriteMemory used to create instances of the memory. The maximum memory that can be handled is
  * 1 GiB.
  */
object SimpleReadWriteMemory {

  /**
    * Creates a read-write memory with the given `size`.
    * @param size the size of the memory
    * @return a `Success` with the read-write memory with the given size
    */
  def apply(size: Long): Try[SimpleReadWriteMemory] = {
    if (size <= 0 | size > 1.Gi) Failure(new IllegalArgumentException("Max size supported is 1 GiB"))
    else Try(new SimpleReadWriteMemory(size))
  }

}

/**
  * A read-write memory (aka RAM) with the given `size`. The maximum size is 2^30^ Bytes (1 GiB). The memory is backed
  * by an array.
  * @param size the size of the memory
  */
class SimpleReadWriteMemory private(val size: Long) extends ReadWriteMemory {

  /** The array to hold the memory data. */
  val memory = new Array[Byte](size.asInstanceOf[Int])

  /**
    * The read method to read the value from the array.
    * @param address the address to read
    * @return a `Success` with the byte read at the given address
    */
  protected override def doRead(address: Long): Try[Byte] = {
    Try(memory(address.asInstanceOf[Int]))
  }

  /**
    * The write method to write the value to the array.
    * @param address the address to write
    * @param value the `value` to write
    * @return a `Success` when the byte is written successfully, `Failure` otherwise
    */
  protected override def doWrite(address: Long, value: Byte): Try[Unit] = {
    Try(memory(address.asInstanceOf[Int]) = value)
  }

}
