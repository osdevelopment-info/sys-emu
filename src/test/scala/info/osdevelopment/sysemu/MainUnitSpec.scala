package info.osdevelopment.sysemu

import info.osdevelopment.sysemu.config.SystemConfig
import info.osdevelopment.sysemu.memory.Memory
import info.osdevelopment.sysemu.processor.x86.i86.Processor8086
import org.specs2._
import scala.util.{Failure, Success}

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
