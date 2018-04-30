package info.osdevelopment.sysemu.memory

import java.nio.file.{FileSystems, Files, StandardOpenOption}
import java.util.EnumSet

import info.osdevelopment.sysemu.support.Utilities._
import org.specs2._

import scala.util.Random

class ReadOnlyMemoryUnitSpec extends mutable.Specification {

  "A ReadOnlyMemory" >> {
    "should be initializable with an array" >> {
      val data = new Array[Byte](1.Mi.asInstanceOf[Int])
      val memory = ReadOnlyMemory(data)
      success
    }
    "should be initializable with a seekable byte channel" >> {
      val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
      val channel = Files.newByteChannel(image, StandardOpenOption.READ)
      val memory = ReadOnlyMemory(channel)
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
    "when initialized with a seekable byte channel" >> {
      "should be able to read a byte" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        0x15.asInstanceOf[Byte] must_== memory.readByte(0x0001)
      }
      "should ignore writing a byte" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        memory.writeByte(0x0002, 0x2f.asInstanceOf[Byte])
        0x2f.asInstanceOf[Byte] must_!== memory.readByte(0x0002)
      }
      "should be able to read a byte from a high address (and update the cache)" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        0xc9.asInstanceOf[Byte] must_== memory.readByte(0x0000000000000002L)
        0x36.asInstanceOf[Byte] must_== memory.readByte(0x0000000000040002L)
      }
      "should throw an exception when the written address is negative" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the written address is too large" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is negative" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        memory.readByte(-1) must throwA[IllegalAddressException]
      }
      "should throw an exception when the read address is too large" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val memory = ReadOnlyMemory(channel)
        memory.readByte(Int.MaxValue) must throwA[IllegalAddressException]
      }
    }
  }

}
