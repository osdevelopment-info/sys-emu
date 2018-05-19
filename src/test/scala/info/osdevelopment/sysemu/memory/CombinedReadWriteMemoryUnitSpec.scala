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

class CombinedReadWriteMemoryUnitSpec extends mutable.Specification {

  /** This specification needs to be sequential because else we will get an OOME */
  sequential

  "A CombinedReadWriteMemory" >> {
    "when created" >> {
      "should fail when size is to large" >> {
        CombinedReadWriteMemory(2.Ei) must beFailedTry.withThrowable[IllegalArgumentException]
      }
      "should fail when size is negative" >> {
        CombinedReadWriteMemory(-2.Gi) must beFailedTry.withThrowable[IllegalArgumentException]
      }
    }
  }

}
