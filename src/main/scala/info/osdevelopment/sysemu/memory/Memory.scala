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

import scala.util.{Failure, Success, Try}

/**
  * A generic memory type. A memory type can address bytes up to a maximum of 2^64^ bytes.
  */
trait Memory {

  /**
    * Returns the size of the memory.
    * @return the size of the memory
    */
  def size: Long

  /**
    * Read a single `Byte` from the memory at the given address.
    * @param address the `address` to read from
    * @return a `Success` with the Byte read or a `Failure`
    */
  def readByte(address: Long): Try[Byte]

  /**
    * Write a single `Byte` to the memory at the given address.
    * @param address the `address` to write to
    * @param value the `value` to write
    * @return a `Success` when the byte was written successfully or a `Failure` otherwise
    */
  def writeByte(address: Long, value: Byte): Try[Unit]

  /**
    * Read a `Short` from the memory at `address ... address + 1`. The value of `address` will be the least significant
    * `Byte`.
    * @param address the `address` to read from
    * @return a `Success` with the `Short` read or a `Failure`
    */
  final def readShort(address: Long): Try[Short] = {
    val bytes = List.range(0, 2).map(offs => readByte(address + offs))
    val failures = bytes.filter(x => x.isFailure)
    if (failures.isEmpty) {
      Success((readSumBytes(bytes) & 0xffffL).asInstanceOf[Short])
    } else {
      failures.head.asInstanceOf[Failure[Short]]
    }
  }

  /**
    * Write a `Short` to `address ... address + 1`. The least significant `Byte` will be written to `address`.
    * @param address the `address` to write to
    * @param value the `value` to write
    * @return a `Success` when the short was written successfully or a `Failure` otherwise
    */
  final def writeShort(address: Long, value: Short): Try[Unit] = {
    writeDivided(address, value, 2)
  }

  /**
    * Read an `Int` from the memory at `address ... address + 3`. The value of `address` will be the least significant
    * `Byte`.
    * @param address the `address` to read from
    * @return a `Success` with the `Int` read or a `Failure`
    */
  final def readInt(address: Long): Try[Int] = {
    val bytes = List.range(0, 4).map(offs => readByte(address + offs))
    val failures = bytes.filter(x => x.isFailure)
    if (failures.isEmpty) {
      Success((readSumBytes(bytes) & 0xffffffffL).asInstanceOf[Int])
    } else {
      failures.head.asInstanceOf[Failure[Int]]
    }
  }

  /**
    * Write an `Int` at `address ... address + 3`. The least significant `Byte` will be written to `address`.
    * @param address the `address` to write to
    * @param value the `value` to write
    * @return a `Success` when the int was written successfully or a `Failure` otherwise
    */
  final def writeInt(address: Long, value: Int): Try[Unit] = {
    writeDivided(address, value, 4)
  }

  /**
    * Read a `Long` from the memory at `address ... address + 7`. The value of `address` will be the least significant
    * `Byte`.
    * @param address the `address` to read from
    * @return a `Success` with the `Long` read or a `Failure`
    */
  final def readLong(address: Long): Try[Long] = {
    val bytes = List.range(0, 8).map(offs => readByte(address + offs))
    val failures = bytes.filter(x => x.isFailure)
    if (failures.isEmpty) {
      Success(readSumBytes(bytes) & 0xffffffffffffffffL)
    } else {
      failures.head.asInstanceOf[Failure[Long]]
    }
  }

  /**
    * Write a `Long` at `address ... address + 7`. The least significant `Byte` will be written to `address`.
    * @param address the `address` to write to
    * @param value the `value` to write
    * @return a `Success` when the long was written successfully or a `Failure` otherwise
    */
  final def writeLong(address: Long, value: Long): Try[Unit] = {
    writeDivided(address, value, 8)
  }

  /**
    * Write the `value` into the memory starting at `address` divided to `numberBytes` bytes.
    * @param address the start address of the write
    * @param value the value to write
    * @param numberBytes the number of bytes to write
    * @return a `Success` if no error occurred during the write, `Failure` otherwiese
    */
  private def writeDivided(address: Long, value: Long, numberBytes: Int): Try[Unit] = {
    val offsets = List.range(0, numberBytes)
    val result = offsets.map(offset => writeByte(address + offset, (value >> (offset * 8) & 0xff).asInstanceOf[Byte]))
    val failures = result.filter(x => x.isFailure)
    if (failures.isEmpty) {
      Success((): Unit)
    } else {
      failures.head
    }
  }

  /**
    * Sums up the bytes according. The first byte in the list is the least significant byte.
    * @param bytes the bytes to sum up
    * @return the combined bytes
    */
  private def readSumBytes(bytes: List[Try[Byte]]): Long = {
    bytes.collect { case Success(b) => b.asInstanceOf[Long] & 0xff }
        .zipWithIndex
        .map { case (l, i) => l << (i * 8) }
        .sum
  }

}
