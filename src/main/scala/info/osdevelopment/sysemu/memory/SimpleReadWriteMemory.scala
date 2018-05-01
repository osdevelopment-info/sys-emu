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

/**
  * The object ReadWriteMemory used to create instances of the memory. The maximum memory that can be handled is 1 GiB.
  */
object SimpleReadWriteMemory {

  def apply(size: Long): SimpleReadWriteMemory = {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0")
    }
    if (size > 1.Gi) {
      throw new IllegalArgumentException("Max size supported is 1 GiB")
    }
    return new SimpleReadWriteMemory(size)
  }

}

/**
  * A read-write memory (aka RAM) with a default size of 8MiB. The maximum size is 2^30^ Bytes (1 GiB).
  * @param size the size of the memory
  */
class SimpleReadWriteMemory private(val size: Long) extends ReadWriteMemory {

  val memory = new Array[Byte](size.asInstanceOf[Int])

  @throws(classOf[IllegalAddressException])
  protected override def doRead(address: Long): Byte = {
    memory(address.asInstanceOf[Int])
  }

  @throws(classOf[IllegalAddressException])
  protected override def doWrite(address: Long, value: Byte): Unit = {
    memory(address.asInstanceOf[Int]) = value
  }

}
