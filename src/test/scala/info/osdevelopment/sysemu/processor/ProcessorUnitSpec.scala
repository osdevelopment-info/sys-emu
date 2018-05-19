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
package info.osdevelopment.sysemu.processor

import info.osdevelopment.sysemu.memory.ReadWriteMemory
import info.osdevelopment.sysemu.processor.test.TestProcessor
import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class ProcessorUnitSpec extends mutable.Specification {

  "A processor" >> {
    "when adding memory" >> {
      "should accept one memory" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        memory must beSuccessfulTry
        processor.addMemory(0x0000, memory.get)
        success
      }
      "should accept two non-overlapping memories" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        memory1 must beSuccessfulTry
        val memory2 = ReadWriteMemory(512.Ki)
        memory2 must beSuccessfulTry
        processor.addMemory(0x00000, memory1.get)
        processor.addMemory(0x80000, memory2.get)
        success
      }
      "should not accept two overlapping memories (higher added last)" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        memory1 must beSuccessfulTry
        val memory2 = ReadWriteMemory(512.Ki)
        memory2 must beSuccessfulTry
        processor.addMemory(0x00000, memory1.get)
        processor.addMemory(0x7ffff, memory2.get) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept two overlapping memories (higher added first)" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        memory1 must beSuccessfulTry
        val memory2 = ReadWriteMemory(512.Ki)
        memory2 must beSuccessfulTry
        processor.addMemory(0x7ffff, memory2.get)
        processor.addMemory(0x00000, memory1.get) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept a memory beyond the end of addressable space" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        memory must beSuccessfulTry
        processor.addMemory(0xc0000, memory.get) must throwAn[IllegalMemoryLayoutException]
      }
    }
  }

}
