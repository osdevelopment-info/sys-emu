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
package info.osdevelopment.sysemu.system

import info.osdevelopment.sysemu.memory.ReadWriteMemory
import info.osdevelopment.sysemu.processor.{IllegalMemoryLayoutException, Processor}
import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class SystemConfigUnitSpec extends mutable.Specification {

  class TestProcessor extends Processor {
    /**
      * The maximum memory that can be handled by the processor
      *
      * @return
      */
    override def maxMemory: Long = 1.Mi

    override def reset = {}

    override def step: Unit = {}

  }

  "A system config" >> {
    "when adding a processor" >> {
      "should accept the first processor" >> {
        val config = new SystemConfig
        config.addProcessor(new TestProcessor)
        success
      }
      "should accept a second processor" >> {
        val config = new SystemConfig
        config.addProcessor(new TestProcessor)
        config.addProcessor(new TestProcessor)
        success
      }
      "should return an added processor" >> {
        val processor = new TestProcessor
        val config = new SystemConfig
        config.addProcessor(processor)
        config.processors must contain(processor)
      }
    }
    "when adding memory" >> {
      "should accept one memory" >> {
        val config = new SystemConfig
        val memory = ReadWriteMemory(512.Ki)
        config.addMemory(0x00000, memory)
        success
      }
      "should accept two non-overlapping memories" >> {
        val config = new SystemConfig
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        config.addMemory(0x00000, memory1)
        config.addMemory(0x80000, memory2)
        success
      }
      "should not accept two overlapping memories (higher added last)" >> {
        val config = new SystemConfig
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        config.addMemory(0x00000, memory1)
        config.addMemory(0x7ffff, memory2) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept two overlapping memories (higher added first)" >> {
        val config = new SystemConfig
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        config.addMemory(0x7ffff, memory2)
        config.addMemory(0x00000, memory1) must throwAn[IllegalMemoryLayoutException]
      }
      "should return an added memory" >> {
        val config = new SystemConfig
        val memory = ReadWriteMemory(512.Ki)
        config.addMemory(0x00000, memory)
        config.memory must havePair(0x00000, memory)
      }
    }
  }

}
