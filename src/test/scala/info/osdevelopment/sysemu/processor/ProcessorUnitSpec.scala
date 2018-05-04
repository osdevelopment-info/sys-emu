package info.osdevelopment.sysemu.processor

import info.osdevelopment.sysemu.memory.ReadWriteMemory
import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class ProcessorUnitSpec extends mutable.Specification {

  class TestProcessor extends Processor {
    /**
      * The maximum memory that can be handled by the processor
      *
      * @return
      */
    override def maxMemory: Long = 1.Mi

  }

  "A processor" >> {
    "when adding memory" >> {
      "should accept one memory" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        processor.addMemory(0x0000, memory)
        success
      }
      "should accept two non-overlapping memories" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        processor.addMemory(0x00000, memory1)
        processor.addMemory(0x80000, memory2)
        success
      }
      "should not accept two overlapping memories (higher added last)" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        processor.addMemory(0x00000, memory1)
        processor.addMemory(0x7ffff, memory2) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept two overlapping memories (higher added first)" >> {
        val processor = new TestProcessor
        val memory1 = ReadWriteMemory(512.Ki)
        val memory2 = ReadWriteMemory(512.Ki)
        processor.addMemory(0x7ffff, memory2)
        processor.addMemory(0x00000, memory1) must throwAn[IllegalMemoryLayoutException]
      }
      "should not accept a memory beyond the end of addressable space" >> {
        val processor = new TestProcessor
        val memory = ReadWriteMemory(512.Ki)
        processor.addMemory(0xc0000, memory) must throwAn[IllegalMemoryLayoutException]
      }
    }
  }

}
