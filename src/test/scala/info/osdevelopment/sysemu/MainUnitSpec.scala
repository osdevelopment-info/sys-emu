package info.osdevelopment.sysemu

import info.osdevelopment.sysemu.memory.Memory
import info.osdevelopment.sysemu.processor.x86.i86.Processor8086
import org.specs2._

class MainUnitSpec extends mutable.Specification {

  "The program" >> {
    "should create a CPU" >> {
      "without command line arguments" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig.processors must not be empty
      }
      "of type 8086 as default" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig.processors must contain(haveClass[Processor8086])
      }
      "of type 8086 with the appropriate command line argument" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("-c8086"))
        systemConfig.processors must contain(haveClass[Processor8086])
      }
    }
    "should throw an exception for an unknown CPU" >> {
      val program = new Main
      program.createConfigFromCommandLine(Array("-c invalid")) must throwAn[IllegalConfigurationException]
    }
    "should add a BIOS" >> {
      "without command line arguments" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig.memory must not be empty
      }
      "with base address 0xF0000 as default" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(new Array(0))
        systemConfig.memory must haveKey(0xf0000L)
      }
      "with base address 0xF0000 when loading a 64 KiB image" >> {
        val program = new Main
        val systemConfig = program.createConfigFromCommandLine(Array("-bsrc/test/resources/smallrom.img"))
        systemConfig.memory must haveKey(0xf0000L)
      }
    }
    "should throw an exception when the given BIOS is not found" >> {
      val program = new Main
      program.createConfigFromCommandLine(Array("-b invalid")) must throwAn[IllegalConfigurationException]
    }
  }

}
