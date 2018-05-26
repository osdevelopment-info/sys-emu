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
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.size must_== 1.Gi
      }
      "should be configurable in size to smaller sizes" >> {
        val tryMemory = ReadWriteMemory(1.Mi)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.size must_== 1.Mi
      }
      "should be configurable in size to larger sizes (with a multiple of 1.Gi)" >> {
        val tryMemory = ReadWriteMemory(2.Gi)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.size must_== 2.Gi
      }
      "should be configurable in size to larger sizes (without a multiple of 1.Gi)" >> {
        val tryMemory = ReadWriteMemory(1536.Mi)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.size must_== 1536.Mi
      }
      "should fail when size is to large" >> {
        ReadWriteMemory(2.Ei) must beFailedTry.withThrowable[IllegalArgumentException]
      }
      "should fail when size is negative" >> {
        ReadWriteMemory(-2.Gi) must beFailedTry.withThrowable[IllegalArgumentException]
      }
    }
    "when accessed" >> {
      "return 0 if not initialized" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(0x0000) must beSuccessfulTry.withValue(0)
      }
      "return the byte value that was written into" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(1, 0xef.asInstanceOf[Byte])
        memory.readByte(0x0001) must beSuccessfulTry.withValue(0xef.asInstanceOf[Byte])
      }
      "return the byte value that was written into another module" >> {
        val tryMemory = ReadWriteMemory(2.Gi)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(0x0000000040000002L, 0xdf.asInstanceOf[Byte])
        memory.readByte(0x0000000040000002L) must beSuccessfulTry.withValue(0xdf.asInstanceOf[Byte])
      }
      "should fail when the written address is negative" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the written address is too large" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is negative" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(-1) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is too large" >> {
        val tryMemory = ReadWriteMemory()
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(Int.MaxValue) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
  }

}
