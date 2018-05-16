package info.osdevelopment.sysemu.processor.x86.i86

import info.osdevelopment.sysemu.processor.{Processor, Register}
import info.osdevelopment.sysemu.support.Utilities._
import java.util.ServiceLoader
import org.specs2._
import scala.collection.JavaConverters._
import scala.util.Try

class Processor8086UnitSpec extends mutable.Specification {

  "A processor 8086" >> {
    "should be loadable by a ServiceLoader" >> {
      "without an exception" >> {
        val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
        val processorTry = Try(processorLoader.asScala.find(_.name == "8086"))
        processorTry must beSuccessfulTry
      }
      "with some result" >> {
        val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
        val processorTry = Try(processorLoader.asScala.find(_.name == "8086"))
        val processor = processorTry.get
        processor must beSome
      }
      "with the correct name" >> {
        val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
        val processorTry = Try(processorLoader.asScala.find(_.name == "8086"))
        val processor = processorTry.get
        processor.get.name must_== "8086"
      }
    }
    "should have a max memory of 1 MiB" >> {
      val processor = new Processor8086
      processor.maxMemory must_== 1.Mi
    }
    "when accessing registers" >> {
      "should return registers" >> {
        val processor = new Processor8086
        processor.registers must beSuccessfulTry
      }
      "should return 14 registers" >> {
        val processor = new Processor8086
        processor.registers(0).get.size must_== 14
      }
      "should return correct register names" >> {
        val processor = new Processor8086
        processor.registers(0).get.keys must contain("AX", "BX", "CX", "DX", "SI", "DI", "BP", "SP", "IP", "FLAGS",
          "CS", "DS", "ES", "SS")
      }
      "should return registers for core 0" >> {
        val processor = new Processor8086
        processor.registers.get.keys must contain(0)
      }
      "should be able to" >> {
        "access SI" >> {
          val processor = new Processor8086
          val si = processor.register(0, "SI")
          si must beSuccessfulTry
        }
        "read/write SI with unsigned values" >> {
          val processor = new Processor8086
          val si = new Register(0, "SI", 65535)
          val setSi = processor.register(si)
          setSi must beSuccessfulTry
          setSi.get.content must_== -1
          setSi.get.content must_== 0xffff.toShort
        }
        "read/write SI with small values" >> {
          val processor = new Processor8086
          val si = new Register(0, "SI", 32767)
          val setSi = processor.register(si)
          setSi must beSuccessfulTry
          setSi.get.content must_== 32767
          setSi.get.content must_== 0x7fff.toShort
        }
        "read/write SI with signed values" >> {
          val processor = new Processor8086
          val si = new Register(0, "SI", -32768)
          val setSi = processor.register(si)
          setSi must beSuccessfulTry
          setSi.get.content must_== -32768
          setSi.get.content must_== 0x8000.toShort
        }
        "access BH" >> {
          val processor = new Processor8086
          val bh = processor.register(0, "BH")
          bh must beSuccessfulTry
        }
        "read/write BH with unsigned values" >> {
          val processor = new Processor8086
          val bh = new Register(0, "BH", 255)
          processor.register(bh)
          val readBx = processor.register(0, "BX")
          readBx must beSuccessfulTry
          readBx.get.content must_== 0xff00.toShort
        }
        "read/write BH with small values" >> {
          val processor = new Processor8086
          val bh = new Register(0, "BH", 127)
          processor.register(bh)
          val readBx = processor.register(0, "BX")
          readBx must beSuccessfulTry
          readBx.get.content must_== 0x7f00.toShort
        }
        "read/write BH with negative values" >> {
          val processor = new Processor8086
          val bh = new Register(0, "BH", -128)
          processor.register(bh)
          val readBx = processor.register(0, "BX")
          readBx must beSuccessfulTry
          readBx.get.content must_== 0x8000.toShort
        }
        "access CL" >> {
          val processor = new Processor8086
          val cl = processor.register(0, "CL")
          cl must beSuccessfulTry
        }
        "read/write CL unsigned values" >> {
          val processor = new Processor8086
          val cl = new Register(0, "CL", 255)
          processor.register(cl)
          val readCx = processor.register(0, "CX")
          readCx must beSuccessfulTry
          readCx.get.content must_== 0x00ff.toShort
        }
        "read/write CL small values" >> {
          val processor = new Processor8086
          val cl = new Register(0, "CL", 127)
          processor.register(cl)
          val readCx = processor.register(0, "CX")
          readCx must beSuccessfulTry
          readCx.get.content must_== 0x007f.toShort
        }
        "read/write CL signed values" >> {
          val processor = new Processor8086
          val cl = new Register(0, "CL", -128)
          processor.register(cl)
          val readCx = processor.register(0, "CX")
          readCx must beSuccessfulTry
          readCx.get.content must_== 0x0080.toShort
        }
      }
      "should reject" >> {
        "read/write DI with too large positive values" >> {
          val processor = new Processor8086
          val di = new Register(0, "DI", 65536)
          processor.register(di) must beFailedTry
        }
        "read/write DI with too large negative values" >> {
          val processor = new Processor8086
          val di = new Register(0, "DI", -32769)
          processor.register(di) must beFailedTry
        }
        "read/write AH with too large positive values" >> {
          val processor = new Processor8086
          val ah = new Register(0, "AH", 256)
          processor.register(ah) must beFailedTry
        }
        "read/write AH with too large negative values" >> {
          val processor = new Processor8086
          val ah = new Register(0, "AH", -129)
          processor.register(ah) must beFailedTry
        }
        "read/write DL with too large positive values" >> {
          val processor = new Processor8086
          val dl = new Register(0, "DL", 256)
          processor.register(dl) must beFailedTry
        }
        "read/write DL with too large negative values" >> {
          val processor = new Processor8086
          val dl = new Register(0, "DL", -129)
          processor.register(dl) must beFailedTry
        }
        "reading an unknown register" >> {
          val processor = new Processor8086
          processor.register(0, "D0") must beFailedTry
        }
        "writing an unknown register" >> {
          val processor = new Processor8086
          val d0 = new Register(0, "D0", 0)
          processor.register(d0) must beFailedTry
        }
      }
    }
  }

}
