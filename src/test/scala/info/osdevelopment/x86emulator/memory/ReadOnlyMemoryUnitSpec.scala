package info.osdevelopment.x86emulator.memory

import info.osdevelopment.x86emulator.support.Utilities._
import org.specs2._

import scala.util.Random

class ReadOnlyMemoryUnitSpec extends mutable.Specification {

  "A ReadOnlyMemory" >> {
    "should be initializable with an array" >> {
      val data = new Array[Byte](1.Mi.asInstanceOf[Int])
      val memory = ReadOnlyMemory(data)
      success
    }
    "when initialized with an array" >> {
      "should be able to read a byte" >> {
        val random = new Random
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        random.nextBytes(data)
        val memory = ReadOnlyMemory(data)
        data(0x0001) must_== memory.readByte(0x0001)
      }
      "should ignore writing a byte" >> {
        val random = new Random
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        random.nextBytes(data)
        val memory = ReadOnlyMemory(data)
        val change: Byte = (data(0x0002) + 1).asInstanceOf[Byte]
        memory.writeByte(0x0002, change)
        change must_!== memory.readByte(0x0002)
      }
      "should throw an exception when the written address is negative" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val memory = ReadOnlyMemory(data)
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the written address is too large" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val memory = ReadOnlyMemory(data)
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is negative" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val memory = ReadOnlyMemory(data)
        memory.readByte(-1) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is too large" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val memory = ReadOnlyMemory(data)
        memory.readByte(Int.MaxValue) must throwA[IllegalAddressException]
      }
    }
  }

}
