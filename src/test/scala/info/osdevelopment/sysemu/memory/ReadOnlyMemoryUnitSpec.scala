/* sys-emu - A system emulator
 * Copyright (C) 2018 U. Plonus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.osdevelopment.sysemu.memory

import info.osdevelopment.sysemu.support.Utilities._
import java.nio.file.{FileSystems, Files, StandardOpenOption}
import org.specs2._
import scala.util.Random

class ReadOnlyMemoryUnitSpec extends mutable.Specification {

  "A ReadOnlyMemory" >> {
    "should be initializable with an array" >> {
      val data = new Array[Byte](1.Mi.asInstanceOf[Int])
      ReadOnlyMemory(data) must beSuccessfulTry
    }
    "should be initializable with a seekable byte channel" >> {
      val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
      val channel = Files.newByteChannel(image, StandardOpenOption.READ)
      ReadOnlyMemory(channel) must beSuccessfulTry
    }
    "when initialized with an array" >> {
      "should be able to read a byte" >> {
        val random = new Random
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        random.nextBytes(data)
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(0x0001) must beSuccessfulTry(data(0x0001))
      }
      "should ignore writing a byte" >> {
        val random = new Random
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        random.nextBytes(data)
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        val change: Byte = (data(0x0002) + 1).asInstanceOf[Byte]
        memory.writeByte(0x0002, change)
        memory.readByte(0x0002) must beSuccessfulTry
        memory.readByte(0x0002).get must_!= change
      }
      "should fail when the written address is negative" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the written address is too large" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is negative" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(-1) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is too large" >> {
        val data = new Array[Byte](1.Mi.asInstanceOf[Int])
        val tryMemory = ReadOnlyMemory(data)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(Int.MaxValue) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
    "when initialized with a seekable byte channel" >> {
      "should be able to read a byte" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(0x0001) must beSuccessfulTry(0xce.asInstanceOf[Byte])
      }
      "should ignore writing a byte" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "smallrom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(0x0002, 0x2f.asInstanceOf[Byte])
        memory.readByte(0x0002) must beSuccessfulTry
        memory.readByte(0x0002) must_!= 0x2f.asInstanceOf[Byte]
      }
      "should be able to read a byte from a high address (and update the cache)" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(0x0000000000000002L) must beSuccessfulTry.withValue(0xc9.asInstanceOf[Byte])
        memory.readByte(0x0000000000040002L) must beSuccessfulTry.withValue(0x36.asInstanceOf[Byte])
      }
      "should fail when the written address is negative" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(-1, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the written address is too large" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.writeByte(Int.MaxValue, 0xef.asInstanceOf[Byte]) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is negative" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(-1) must beFailedTry.withThrowable[IllegalAddressException]
      }
      "should fail when the read address is too large" >> {
        val image = FileSystems.getDefault.getPath("src", "test", "resources", "largerom.img")
        val channel = Files.newByteChannel(image, StandardOpenOption.READ)
        val tryMemory = ReadOnlyMemory(channel)
        tryMemory must beSuccessfulTry
        val memory = tryMemory.get
        memory.readByte(Int.MaxValue) must beFailedTry.withThrowable[IllegalAddressException]
      }
    }
  }

}
