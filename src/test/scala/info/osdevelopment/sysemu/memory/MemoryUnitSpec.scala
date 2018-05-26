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

import org.specs2._
import scala.util.{Failure, Success, Try}

class MemoryUnitSpec extends mutable.Specification {

  class TestMemory extends Memory {

    private val memory = new Array[Byte](1024)

    override def size: Long = memory length

    override def readByte(address: Long): Try[Byte] = {
      if (address >= memory.length) Failure(new IllegalAddressException("Address outside memory"))
      else Success(memory(address.asInstanceOf[Int]))
    }

    override def writeByte(address: Long, value: Byte): Try[Unit] = {
      if (address >= memory.length) Failure(new IllegalAddressException("Address outside memory"))
      else Try(memory(address.asInstanceOf[Int]) = value)
    }

  }

  "A memory" >> {
    "when accessed as short" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        memory.readShort(0x0000) must beSuccessfulTry.withValue(0x0000.asInstanceOf[Short])
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeShort(0x0002, 0x0123.asInstanceOf[Short])
        memory.readShort(0x0002) must beSuccessfulTry.withValue(0x0123.asInstanceOf[Short])
      }
      "returns the negative value that was written into" >> {
        val memory = new TestMemory
        memory.writeShort(0x0002, 0xfedc.asInstanceOf[Short])
        memory.readShort(0x0002) must beSuccessfulTry.withValue(0xfedc.asInstanceOf[Short])
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x0002, 0x67.asInstanceOf[Byte])
        memory.writeByte(0x0003, 0x89.asInstanceOf[Byte])
        memory.readShort(0x0002) must beSuccessfulTry.withValue(0x8967.asInstanceOf[Short])
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeShort(0x0002, 0x89ab.asInstanceOf[Short])
        memory.readByte(0x0002) must beSuccessfulTry.withValue(0xab.asInstanceOf[Byte])
        memory.readByte(0x0003) must beSuccessfulTry.withValue(0x89.asInstanceOf[Byte])
      }
      "should fail if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readShort(0x03ff) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeShort(0x03ff, 0x2345.asInstanceOf[Short]) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
    "when accessed as int" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        memory.readInt(0x0000) must beSuccessfulTry.withValue(0x00000000)
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeInt(0x0004, 0x01234567)
        memory.readInt(0x0004) must beSuccessfulTry.withValue(0x01234567)
      }
      "returns the negative value that was written into" >> {
        val memory = new TestMemory
        memory.writeInt(0x0004, 0xfedcba98)
        memory.readInt(0x0004) must beSuccessfulTry.withValue(0xfedcba98)
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x0004, 0x23.asInstanceOf[Byte])
        memory.writeByte(0x0005, 0x45.asInstanceOf[Byte])
        memory.writeByte(0x0006, 0x67.asInstanceOf[Byte])
        memory.writeByte(0x0007, 0x89.asInstanceOf[Byte])
        memory.readInt(0x0004) must beSuccessfulTry.withValue(0x89674523)
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeInt(0x0004, 0x89abcdef)
        memory.readByte(0x0004) must beSuccessfulTry.withValue(0xef.asInstanceOf[Byte])
        memory.readByte(0x0005) must beSuccessfulTry.withValue(0xcd.asInstanceOf[Byte])
        memory.readByte(0x0006) must beSuccessfulTry.withValue(0xab.asInstanceOf[Byte])
        memory.readByte(0x0007) must beSuccessfulTry.withValue(0x89.asInstanceOf[Byte])
      }
      "should fail if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readInt(0x03fe) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeInt(0x03fe, 0x23456789) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
    "when accessed as long" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        memory.readLong(0x0000) must beSuccessfulTry.withValue(0L)
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeLong(0x0008, 0x0123456789abcdefL)
        memory.readLong(0x0008) must beSuccessfulTry.withValue(0x0123456789abcdefL)
      }
      "returns the negative value that was written into" >> {
        val memory = new TestMemory
        memory.writeLong(0x0008, 0xfedcba9876543210L)
        memory.readLong(0x0008) must beSuccessfulTry.withValue(0xfedcba9876543210L)
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x0008, 0x01.asInstanceOf[Byte])
        memory.writeByte(0x0009, 0x23.asInstanceOf[Byte])
        memory.writeByte(0x000a, 0x45.asInstanceOf[Byte])
        memory.writeByte(0x000b, 0x67.asInstanceOf[Byte])
        memory.writeByte(0x000c, 0x89.asInstanceOf[Byte])
        memory.writeByte(0x000d, 0xab.asInstanceOf[Byte])
        memory.writeByte(0x000e, 0xcd.asInstanceOf[Byte])
        memory.writeByte(0x000f, 0xef.asInstanceOf[Byte])
        memory.readLong(0x0008) must beSuccessfulTry.withValue(0xefcdab8967452301L)
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeLong(0x0008, 0xfedcba9876543210L)
        memory.readByte(0x0008) must beSuccessfulTry.withValue(0x10.asInstanceOf[Byte])
        memory.readByte(0x0009) must beSuccessfulTry.withValue(0x32.asInstanceOf[Byte])
        memory.readByte(0x000a) must beSuccessfulTry.withValue(0x54.asInstanceOf[Byte])
        memory.readByte(0x000b) must beSuccessfulTry.withValue(0x76.asInstanceOf[Byte])
        memory.readByte(0x000c) must beSuccessfulTry.withValue(0x98.asInstanceOf[Byte])
        memory.readByte(0x000d) must beSuccessfulTry.withValue(0xba.asInstanceOf[Byte])
        memory.readByte(0x000e) must beSuccessfulTry.withValue(0xdc.asInstanceOf[Byte])
        memory.readByte(0x000f) must beSuccessfulTry.withValue(0xfe.asInstanceOf[Byte])
      }
      "should fail if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readLong(0x03fc) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeLong(0x03fc, 0x0123456789abcdefL) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
  }

}
