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

import java.util.UUID
import org.specs2._

class SystemUnitSpec extends mutable.Specification with mock.Mockito {

  "A system should" >> {
    "be possible to be created with a UUID" >> {
      val uuid = UUID.randomUUID
      val system = new System(uuid)
      system.uuid must_== uuid
    }
    "possible to be created without a UUID" >> {
      val system = new System()
      system.uuid must beAnInstanceOf[UUID]
    }
  }

}
