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
package info.osdevelopment.sysemu

import info.osdevelopment.sysemu.processor.x86.i86.Processor8086
import org.specs2._

class MainUnitSpec extends mutable.Specification {

  "The program" >> {
    "should create a CPU" >> {
      "without command line arguments" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig must beSuccessfulTry
        systemConfig.get.processors must not be empty
      }
      "of type 8086 as default" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig must beSuccessfulTry
        systemConfig.get.processors must contain(haveClass[Processor8086])
      }
      "of type 8086 with the appropriate command line argument" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("-c8086"))
        systemConfig must beSuccessfulTry
        systemConfig.get.processors must contain(haveClass[Processor8086])
      }
    }
    "should throw an exception for an unknown CPU" >> {
      val program = new Main
      val systemConfig = program.createConfigFromCommandLine(Array("-c invalid"))
      systemConfig must beFailedTry.withThrowable[IllegalConfigurationException]("Invalid CPU")
    }
    "should add a BIOS" >> {
      "without command line arguments" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig must beSuccessfulTry
        systemConfig.get.memory must not be empty
      }
      "with base address 0xF0000 as default" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig must beSuccessfulTry
        systemConfig.get.memory must haveKey(0xf0000L)
      }
      "with base address 0xF0000 when loading a 64 KiB image" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("-bsrc/test/resources/smallrom.img"))
        systemConfig must beSuccessfulTry
        systemConfig.get.memory must haveKey(0xf0000L)
      }
    }
    "should throw an exception when the given BIOS is not found" >> {
      val program = new Main
      val systemConfig = program.createConfigFromCommandLine(Array("-b invalid"))
      systemConfig must beFailedTry.withThrowable[IllegalConfigurationException]("BIOS file does not exist")
    }
  }

}
