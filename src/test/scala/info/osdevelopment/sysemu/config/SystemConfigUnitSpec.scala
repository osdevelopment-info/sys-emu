package info.osdevelopment.sysemu.config

import info.osdevelopment.sysemu.memory.ReadWriteMemory
import info.osdevelopment.sysemu.processor.{IllegalMemoryLayoutException, Processor}
import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class SystemConfigUnitSpec extends  mutable.Specification {

  class TestProcessor extends Processor {
    /**
      * The maximum memory that can be handled by the processor
      *
      * @return
      */
    override def maxMemory: Long = 1.Mi

  }

  "A system config" >> {
    "when adding a processor" >> {
      "should accept the first processor" >> {
        val config = new SystemConfig
        config.addProcessor(new TestProcessor)
        success
      }
      "should accept a second processor" >> {
        val config = new SystemConfig
        config.addProcessor(new TestProcessor)
        config.addProcessor(new TestProcessor)
        success
      }
      "should return an added processor" >> {
        val processor = new TestProcessor
        val config = new SystemConfig
        config.addProcessor(processor)
        config.processors must contain(processor)
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
