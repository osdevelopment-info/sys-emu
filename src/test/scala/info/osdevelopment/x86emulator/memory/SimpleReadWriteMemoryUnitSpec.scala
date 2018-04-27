package info.osdevelopment.x86emulator.memory

import info.osdevelopment.x86emulator.support.Utilities._
import org.specs2._

class SimpleReadWriteMemoryUnitSpec extends mutable.Specification {

  "A SimpleReadWriteMemory" >> {
    "when created" >> {
      "should have a default size of 8 MiB" >> {
        val memory = SimpleReadWriteMemory()
        8.Mi must_== memory.size
      }
      "should be configurable in size" >> {
        val memory = SimpleReadWriteMemory((1 Mi).asInstanceOf[Int])
        1.Mi must_== memory.size
      }
      "should throw an IllegalArgumentException when size is to large" >> {
        SimpleReadWriteMemory((1536 Mi).asInstanceOf[Int]) must throwA[IllegalArgumentException]
      }
      "should throw an IllegalArgumentException when size is negative" >> {
        SimpleReadWriteMemory((-2 Gi).asInstanceOf[Int]) must throwA[IllegalArgumentException]
      }
    }
    "when accessed" >> {
      "return 0 if not initialized" >> {
        val memory = SimpleReadWriteMemory()
        0 must_== memory.readByte(0)
      }
      "return the byte value that was written into" >> {
        val memory = SimpleReadWriteMemory()
        memory.writeByte(1, 0xef.asInstanceOf[Byte])
        0xef.asInstanceOf[Byte] must_== memory.readByte(1)
      }
      "should throw an exception when the written address is negative" >> {
        val memory = SimpleReadWriteMemory()
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the written address is too large" >> {
        val memory = SimpleReadWriteMemory()
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is negative" >> {
        val memory = SimpleReadWriteMemory()
        memory.readByte(-1) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is too large" >> {
        val memory = SimpleReadWriteMemory()
        memory.readByte(Int.MaxValue) must throwA[IllegalAddressException]
      }
    }
  }

}
