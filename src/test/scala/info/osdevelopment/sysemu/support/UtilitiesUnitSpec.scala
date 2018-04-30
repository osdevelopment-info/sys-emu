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
package info.osdevelopment.sysemu.support

import org.specs2._
import Utilities._

class BinaryUnitLongUnitSpec extends mutable.Specification {

  "A binary unit" >> {
    "should be 0x0000000000000400L (1024) when 1.Ki is used" >> {
      0x0000000000000400L must_== 1.Ki
    }
    "should be 0x0000000000000800L (2048) when 2.Ki is used" >> {
      0x0000000000000800L must_== 2.Ki
    }
    "should be 0x0000000000040000L (262144) when 256.Ki is used" >> {
      0x0000000000040000L must_== 256.Ki
    }
    "should be 0x0000000000100000L when 1.Mi is used" >> {
      0x0000000000100000L must_== 1.Mi
    }
    "should be 0x0000000000200000L when 2.Mi is used" >> {
      0x0000000000200000L must_== 2.Mi
    }
    "should be 0x0000000010000000L when 256.Mi is used" >> {
      0x0000000010000000L must_== 256.Mi
    }
    "should be 0x0000000040000000L when 1.Gi is used" >> {
      0x0000000040000000L must_== 1.Gi
    }
    "should be 0x0000000080000000L when 2.Gi is used" >> {
      0x0000000080000000L must_== 2.Gi
    }
    "should be 0x0000000400000000L when 256.Gi is used" >> {
      0x0000004000000000L must_== 256.Gi
    }
    "should be 0x0000010000000000L when 1.Ti is used" >> {
      0x0000010000000000L must_== 1.Ti
    }
    "should be 0x0000020000000000L when 2.Ti is used" >> {
      0x0000020000000000L must_== 2.Ti
    }
    "should be 0x0001000000000000L when 256.Ti is used" >> {
      0x0001000000000000L must_== 256.Ti
    }
    "should be 0x0004000000000000L when 1.Pi is used" >> {
      0x0004000000000000L must_== 1.Pi
    }
    "should be 0x0008000000000000L when 2.Pi is used" >> {
      0x0008000000000000L must_== 2.Pi
    }
    "should be 0x0400000000000000L when 256.Pi is used" >> {
      0x0400000000000000L must_== 256.Pi
    }
    "should be 0x1000000000000000L when 1.Ei is used" >> {
      0x1000000000000000L must_== 1.Ei
    }
    "should be 0x2000000000000000L when 2.Ei is used" >> {
      0x2000000000000000L must_== 2.Ei
    }
    "should be 0x8000000000000000L when 8.Ei is used" >> {
      0x8000000000000000L must_== 8.Ei
    }
  }

}
