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
  * A generic memory type. A memory type can address bytes up to a maximum of Long.MAX_VALUE bytes.
  */
trait Memory {

  /** Return the size of the memory. */
  def size: Long

  /**
    * Read a single byte from the memory at the given address.
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  @throws(classOf[IllegalAddressException])
  def readByte(address: Long): Byte

  /**
    * Write a single byte to the memory at the given address.
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  @throws(classOf[IllegalAddressException])
  def writeByte(address: Long, value: Byte): Unit

  @throws(classOf[IllegalAddressException])
  final def readShort(address: Long): Short = {
    (readByte(address) | readByte(address + 1) << 8).asInstanceOf[Short]
  }

  @throws(classOf[IllegalAddressException])
  final def writeShort(address: Long, value: Short): Unit = {
    writeByte(address, (value & 0xff).asInstanceOf[Byte])
    writeByte(address + 1, (value >> 8 & 0xff).asInstanceOf[Byte])
  }

  @throws(classOf[IllegalAddressException])
  final def readInt(address: Long): Int = {
    (readByte(address) | readByte(address + 1) << 8 |
      readByte(address + 2) << 16 | readByte(address + 3) << 24)
  }

  @throws(classOf[IllegalAddressException])
  final def writeInt(address: Long, value: Int): Unit = {
    writeByte(address, (value & 0xff).asInstanceOf[Byte])
    writeByte(address + 1, (value >> 8 & 0xff).asInstanceOf[Byte])
    writeByte(address + 2, (value >> 16 & 0xff).asInstanceOf[Byte])
    writeByte(address + 3, (value >> 24 & 0xff).asInstanceOf[Byte])
  }

  @throws(classOf[IllegalAddressException])
  final def readLong(address: Long): Long = {
    ((readByte(address).asInstanceOf[Long] & 0xff) |
      (readByte(address + 1).asInstanceOf[Long] & 0xff) << 8 |
      (readByte(address + 2).asInstanceOf[Long] & 0xff) << 16 |
      (readByte(address + 3).asInstanceOf[Long] & 0xff) << 24 |
      (readByte(address + 4).asInstanceOf[Long] & 0xff) << 32 |
      (readByte(address + 5).asInstanceOf[Long] & 0xff) << 40 |
      (readByte(address + 6).asInstanceOf[Long] & 0xff) << 48 |
      (readByte(address + 7).asInstanceOf[Long] & 0xff) << 56)
      .asInstanceOf[Long]
  }

  @throws(classOf[IllegalAddressException])
  final def writeLong(address: Long, value: Long): Unit = {
    writeByte(address, (value & 0xff).asInstanceOf[Byte])
    writeByte(address + 1, (value >> 8 & 0xff).asInstanceOf[Byte])
    writeByte(address + 2, (value >> 16 & 0xff).asInstanceOf[Byte])
    writeByte(address + 3, (value >> 24 & 0xff).asInstanceOf[Byte])
    writeByte(address + 4, (value >> 32 & 0xff).asInstanceOf[Byte])
    writeByte(address + 5, (value >> 40 & 0xff).asInstanceOf[Byte])
    writeByte(address + 6, (value >> 48 & 0xff).asInstanceOf[Byte])
    writeByte(address + 7, (value >> 56 & 0xff).asInstanceOf[Byte])
  }

}
