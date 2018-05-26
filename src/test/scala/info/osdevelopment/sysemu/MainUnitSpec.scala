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

import org.specs2._

class MainUnitSpec extends mutable.Specification {

  "The program" >> {
    "should create no config" >> {
      "without command line arguments" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig must beNone
      }
      "when the a BIOS witout CPU is given" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("--bios", "invalid"))
        systemConfig must beNone
      }
    }
    "should create a config" >> {
      "with CPU type 8086 with the appropriate command line argument" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("--cpu=8086"))
        systemConfig must beSome
        systemConfig.get.cpu must_== Some("8086")
      }
      "with a BIOS at base address 0xF0000 when loading a 64 KiB image for 8086" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(
          Array("--cpu=8086", "--bios=src/test/resources/smallrom.img"))
        systemConfig must beSome
        systemConfig.get.memory must haveKey(0xf0000L)
      }
    }
  }

}
