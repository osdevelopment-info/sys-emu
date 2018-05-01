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
import org.specs2._

class ReadWriteMemoryUnitSpec extends mutable.Specification {

  /** This specification needs to be sequential because else we will get an OOME */
  sequential

  "A ReadWriteMemory" >> {
    "when created" >> {
      "should have a default size of 1 GiB" >> {
        val memory = ReadWriteMemory()
        1.Gi must_== memory.size
      }
      "should be configurable in size to smaller sizes" >> {
        val memory = ReadWriteMemory(1.Mi)
        1.Mi must_== memory.size
      }
      "should be configurable in size to larger sizes (with a multiple of 1.Gi)" >> {
        val memory = ReadWriteMemory(2.Gi)
        2.Gi must_== memory.size
      }
      "should be configurable in size to larger sizes (without a multiple of 1.Gi)" >> {
        val memory = ReadWriteMemory(1536.Mi)
        1536.Mi must_== memory.size
      }
      "should throw an IllegalArgumentException when size is to large" >> {
        ReadWriteMemory(2.Ei) must throwAn[IllegalArgumentException]
      }
      "should throw an IllegalArgumentException when size is negative" >> {
        ReadWriteMemory(-2.Gi) must throwAn[IllegalArgumentException]
      }
    }
    "when accessed" >> {
      "return 0 if not initialized" >> {
        val memory = ReadWriteMemory()
        0 must_== memory.readByte(0)
      }
      "return the byte value that was written into" >> {
        val memory = ReadWriteMemory()
        memory.writeByte(1, 0xef.asInstanceOf[Byte])
        0xef.asInstanceOf[Byte] must_== memory.readByte(1)
      }
      "return the byte value that was written into another module" >> {
        val memory = ReadWriteMemory(2.Gi)
        memory.writeByte(0x0000000040000002L, 0xdf.asInstanceOf[Byte])
        0xdf.asInstanceOf[Byte] must_== memory.readByte(0x0000000040000002L)
      }
      "should throw an exception when the written address is negative" >> {
        val memory = ReadWriteMemory()
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must throwAn[IllegalAddressException]
      }
      "should throw an exception when the written address is too large" >> {
        val memory = ReadWriteMemory()
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must throwAn[IllegalAddressException]
      }
      "should throw an exception when the read address is negative" >> {
        val memory = ReadWriteMemory()
        memory.readByte(-1) must throwAn[IllegalAddressException]
      }
      "should throw an exception when the read address is too large" >> {
        val memory = ReadWriteMemory()
        memory.readByte(Int.MaxValue) must throwAn[IllegalAddressException]
      }
    }
  }

}
