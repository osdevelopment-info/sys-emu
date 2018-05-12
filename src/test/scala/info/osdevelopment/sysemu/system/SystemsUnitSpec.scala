package info.osdevelopment.sysemu.system

import java.util.UUID
import org.specs2.mutable

class SystemsUnitSpec extends mutable.Specification {

  "Systems should" >> {
    "when adding" >> {
      "accept a new system" >> {
        val uuid = UUID.randomUUID
        val system = Some(new System(Some(uuid)))
        Systems.add(system) must_== true
      }
      "deny a known system" >> {
        val uuid = UUID.randomUUID
        val system = Some(new System(Some(uuid)))
        Systems.add(system)
        Systems.add(system) must_== false
      }
      "deny a system if it is not a system" >> {
        Systems.add(None) must_== false
      }
      "deny a system without a UUID" >> {
        val system = Some(new System(None))
        Systems.add(system) must_== false
      }
    }
    "when removing" >> {
      "delete a known system" >> {
        val uuid = UUID.randomUUID
        val system = Some(new System(Some(uuid)))
        Systems.add(system)
        Systems.remove(uuid) must_== true
      }
      "indicate an unknown system" >> {
        Systems.remove(UUID.fromString("00000000-0000-0000-0000-000000000000")) must_== false
      }
    }
  }

}
