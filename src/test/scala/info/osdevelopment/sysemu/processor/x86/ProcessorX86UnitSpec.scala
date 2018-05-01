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
package info.osdevelopment.sysemu.processor.x86

import info.osdevelopment.sysemu.memory.ReadWriteMemory
import info.osdevelopment.sysemu.processor.IllegalMemoryLayoutException
import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class ProcessorX86UnitSpec extends mutable.Specification {

  class TestProcessor extends ProcessorX86 {
    /**
      * The maximum memory that can be handled by the processor
      *
      * @return
      */
    override def maxMemory: Long = 1.Mi
  }

  "A processor" >> {
    "when adding memory" >> {
      "should accept one memory" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        processor.addMemory(0x0000, memory)
        success
      }
      "should accept two non-overlapping memories" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        processor.addMemory(0x00000, memory1)
        processor.addMemory(0x80000, memory2)
        success
      }
      "should not accept two overlapping memories" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        processor.addMemory(0x00000, memory1)
        processor.addMemory(0x7ffff, memory2) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept a memory beyond the end of addressable space" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        processor.addMemory(0xc0000, memory) must throwAn[IllegalMemoryLayoutException]
      }
    }
  }

}
