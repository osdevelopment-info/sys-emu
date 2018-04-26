package org.sw4j.x86emulator.memory

import org.specs2._

class ReadWriteMemoryUnitSpec extends mutable.Specification {

  "A ReadWriteMemory" >> {
    "when created" >> {
      "should have a default size of 8MiB" >> {
        val memory = ReadWriteMemory()
        8388608 must_== memory.size
      }
      "should be configurable in size" >> {
        val memory = ReadWriteMemory(1048576)
        1048576 must_== memory.size
      }
      "should throw an IllegalArgumentException when size is to large" >> {
        ReadWriteMemory((1.5 * 1024 * 1024 * 1024).asInstanceOf[Int]) must throwA[IllegalArgumentException]
      }
      "should throw an IllegalArgumentException when size is negative" >> {
        ReadWriteMemory(-2 * 1024 * 1024 * 1024) must throwA[IllegalArgumentException]
      }
    }
    "when accessed" >> {
      "return 0 if not initialized" >> {
        val memory = ReadWriteMemory()
        0 must_== memory.readByte(0)
      }
      "return the byte value that was written into" >> {
        val memory = ReadWriteMemory()
        memory.writeByte(1, 0xef.asInstanceOf[Byte])
        0xef.asInstanceOf[Byte] must_== memory.readByte(1)
      }
      /*"return the short value that was written into" >> {
        val memory = ReadWriteMemory()
        memory.writeShort(1, 0xcdef.asInstanceOf[Short])
        0xef.asInstanceOf[Byte] must_== memory.readByte(1)
      }*/
    }
  }

}
