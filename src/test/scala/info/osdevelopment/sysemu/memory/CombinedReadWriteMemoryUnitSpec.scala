package info.osdevelopment.sysemu.memory

import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

class CombinedReadWriteMemoryUnitSpec extends mutable.Specification {

  /** This specification needs to be sequential because lese we will get an OOME */
  sequential

  "A SimpleReadWriteMemory" >> {
    "when created" >> {
      "should have a default size of 2 GiB" >> {
        val memory = CombinedReadWriteMemory()
        2.Gi must_== memory.size
      }
      "should be configurable in size" >> {
        val memory = CombinedReadWriteMemory(1536 Mi)
        1536.Mi must_== memory.size
      }
      "should throw an IllegalArgumentException when size is to large" >> {
        CombinedReadWriteMemory(2 Ei) must throwA[IllegalArgumentException]
      }
      "should throw an IllegalArgumentException when size is negative" >> {
        CombinedReadWriteMemory((-2 Gi).asInstanceOf[Int]) must throwA[IllegalArgumentException]
      }
    }
    "when accessed" >> {
      "return 0 if not initialized" >> {
        val memory = CombinedReadWriteMemory()
        0 must_== memory.readByte(0)
      }
      "return the byte value that was written into" >> {
        val memory = CombinedReadWriteMemory()
        memory.writeByte(2, 0xef.asInstanceOf[Byte])
        0xef.asInstanceOf[Byte] must_== memory.readByte(2)
      }
      "return the byte value that was written into another module" >> {
        val memory = CombinedReadWriteMemory()
        memory.writeByte(0x0000000040000002L, 0xdf.asInstanceOf[Byte])
        0xdf.asInstanceOf[Byte] must_== memory.readByte(0x0000000040000002L)
      }
      "should throw an exception when the written address is negative" >> {
        val memory = CombinedReadWriteMemory()
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the written address is too large" >> {
        val memory = CombinedReadWriteMemory()
        memory.writeByte(Long.MaxValue, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is negative" >> {
        val memory = CombinedReadWriteMemory()
        memory.readByte(-1) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is too large" >> {
        val memory = CombinedReadWriteMemory()
        memory.readByte(Long.MaxValue) must throwA[IllegalAddressException]
      }
    }
  }

}
