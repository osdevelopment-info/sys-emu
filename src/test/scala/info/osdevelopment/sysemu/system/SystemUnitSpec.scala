package info.osdevelopment.sysemu.system

import org.specs2._

class SystemUnitSpec extends mutable.Specification with mock.Mockito {

  "A system should" >> {
    "perform a single step" >> {
      val system = mock[System]
      system.step
      there was one(system).step
    }
    "perform run" >> {
      val system = mock[System]
      system.run
      there was one(system).run
    }
  }

}
