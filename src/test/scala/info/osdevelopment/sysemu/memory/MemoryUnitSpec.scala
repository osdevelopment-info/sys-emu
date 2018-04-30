package info.osdevelopment.sysemu.memory

import org.specs2._

class MemoryUnitSpec extends mutable.Specification {

  class TestMemory extends Memory {

    private val memory = new Array[Byte](1024)

    override def size: Long = memory length

    override def readByte(address: Long): Byte = {
      if (address >= memory.length) throw new IllegalAddressException
      memory(address.asInstanceOf[Int])
    }

    override def writeByte(address: Long, value: Byte): Unit = {
      if (address >= memory.length) throw new IllegalAddressException
      memory(address.asInstanceOf[Int]) = value
    }

  }

  "A memory" >> {
    "when accessed as short" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        0x0000.asInstanceOf[Short] must_== memory.readShort(0x00)
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeShort(0x02, 0x0123.asInstanceOf[Short])
        0x0123.asInstanceOf[Short] must_== memory.readShort(0x02)
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x02, 0x23.asInstanceOf[Byte])
        memory.writeByte(0x03, 0x45.asInstanceOf[Byte])
        0x4523.asInstanceOf[Short] must_== memory.readShort(0x02)
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeShort(0x02, 0x2345.asInstanceOf[Short])
        0x45.asInstanceOf[Byte] must_== memory.readByte(0x02)
        0x23.asInstanceOf[Byte] must_== memory.readByte(0x03)
      }
      "should throw an exception if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readShort(0x03ff) must throwA[IllegalAddressException]
      }
      "should throw an exception if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeShort(0x03ff, 0x2345.asInstanceOf[Short]) must throwA[IllegalAddressException]
      }
    }
    "when accessed as int" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        0x00000000 must_== memory.readInt(0x00)
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeInt(0x04, 0x01234567)
        0x01234567 must_== memory.readInt(0x04)
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x04, 0x23.asInstanceOf[Byte])
        memory.writeByte(0x05, 0x45.asInstanceOf[Byte])
        memory.writeByte(0x06, 0x67.asInstanceOf[Byte])
        memory.writeByte(0x07, 0x89.asInstanceOf[Byte])
        0x89674523 must_== memory.readInt(4)
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeInt(0x04, 0x23456789)
        0x89.asInstanceOf[Byte] must_== memory.readByte(0x04)
        0x67.asInstanceOf[Byte] must_== memory.readByte(0x05)
        0x45.asInstanceOf[Byte] must_== memory.readByte(0x06)
        0x23.asInstanceOf[Byte] must_== memory.readByte(0x07)
      }
      "should throw an exception if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readInt(0x03fe) must throwA[IllegalAddressException]
      }
      "should throw an exception if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeInt(0x03fe, 0x23456789) must throwA[IllegalAddressException]
      }
    }
    "when accessed as long" >> {
      "returns 0 if not initialized" >> {
        val memory = new TestMemory
        0L must_== memory.readLong(0x00)
      }
      "returns the value that was written into" >> {
        val memory = new TestMemory
        memory.writeLong(0x08, 0x0123456789abcdefL)
        0x0123456789abcdefL must_== memory.readLong(0x08)
      }
      "returns the byte values combined" >> {
        val memory = new TestMemory
        memory.writeByte(0x08, 0x01.asInstanceOf[Byte])
        memory.writeByte(0x09, 0x23.asInstanceOf[Byte])
        memory.writeByte(0x0a, 0x45.asInstanceOf[Byte])
        memory.writeByte(0x0b, 0x67.asInstanceOf[Byte])
        memory.writeByte(0x0c, 0x89.asInstanceOf[Byte])
        memory.writeByte(0x0d, 0xab.asInstanceOf[Byte])
        memory.writeByte(0x0e, 0xcd.asInstanceOf[Byte])
        memory.writeByte(0x0f, 0xef.asInstanceOf[Byte])
        0xefcdab8967452301L must_== memory.readLong(0x08)
      }
      "returns the value as bytes" >> {
        val memory = new TestMemory
        memory.writeLong(0x08, 0x0123456789abcdefL)
        0xef.asInstanceOf[Byte] must_== memory.readByte(0x08)
        0xcd.asInstanceOf[Byte] must_== memory.readByte(0x09)
        0xab.asInstanceOf[Byte] must_== memory.readByte(0x0a)
        0x89.asInstanceOf[Byte] must_== memory.readByte(0x0b)
        0x67.asInstanceOf[Byte] must_== memory.readByte(0x0c)
        0x45.asInstanceOf[Byte] must_== memory.readByte(0x0d)
        0x23.asInstanceOf[Byte] must_== memory.readByte(0x0e)
        0x01.asInstanceOf[Byte] must_== memory.readByte(0x0f)
      }
      "should throw an exception if reading partly out of scope" >> {
        val memory = new TestMemory
        memory.readLong(0x03fc) must throwA[IllegalAddressException]
      }
      "should throw an exception if writing partly out of scope" >> {
        val memory = new TestMemory
        memory.writeLong(0x03fc, 0x0123456789abcdefL) must throwA[IllegalAddressException]
      }
    }
  }

}
