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
import info.osdevelopment.sysemu.processor.test.TestProcessor
import info.osdevelopment.sysemu.processor.{IllegalMemoryLayoutException, Processor}
import info.osdevelopment.sysemu.support.Utilities._
import java.io.File
import org.specs2._

class SystemConfigUnitSpec extends mutable.Specification {

  "A system config" >> {
    "should load a HOCON file" >> {
      new SystemConfig(Some(new File("src/test/resources/config/unknown-system.conf")))
      success
    }
    "should not fail on a missing file" >> {
      new SystemConfig(Some(new File("invalid.conf")))
      success
    }
    "when loading a HOCON file" >> {
      "should read the CPU" >> {
        val systemConfig = new SystemConfig(Some(new File("src/test/resources/config/unknown-system.conf")))
        systemConfig.cpu must_== Some("68000")
      }
      "should read the CPU count" >> {
        val systemConfig = new SystemConfig(Some(new File("src/test/resources/config/unknown-system.conf")))
        systemConfig.cpuCount must_== 3
      }
    }
    "when loading an empty HOCON file" >> {
      "should return the default CPU" >> {
        val systemConfig = new SystemConfig(Some(new File("src/test/resources/config/empty-system.conf")))
        systemConfig.cpu must_== Some("8086")
      }
      "should read the CPU count" >> {
        val systemConfig = new SystemConfig(Some(new File("src/test/resources/config/empty-system.conf")))
        systemConfig.cpuCount must_== 1
      }
    }
    "when trying to load a not existing file" >> {
      "should return the uninitialized CPU" >> {
        val systemConfig = new SystemConfig(Some(new File("invalid.conf")))
        systemConfig.cpu must beNone
      }
      "should read no CPU count" >> {
        val systemConfig = new SystemConfig(Some(new File("invalid.conf")))
        systemConfig.cpuCount must_== 0
      }
    }
    "when creating from scratch" >> {
      "should have no CPU by default" >> {
        val systemConfig = new SystemConfig()
        systemConfig.cpu must beNone
      }
      "should have the CPU set" >> {
        val systemConfig = new SystemConfig()
        systemConfig.cpu = "Z8"
        systemConfig.cpu must_== Some("Z8")
      }
      "should have no CPU when set to null" >> {
        val systemConfig = new SystemConfig()
        systemConfig.cpu = "Z8"
        systemConfig.cpu = null
        systemConfig.cpu must beNone
      }
      "should have 0 CPU count by default" >> {
        val systemConfig = new SystemConfig()
        systemConfig.cpuCount must_== 0
      }
      "should have the CPU count set" >> {
        val systemConfig = new SystemConfig()
        systemConfig.cpuCount = 16
        systemConfig.cpuCount must_== 16
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
