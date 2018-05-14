package info.osdevelopment.sysemu.processor.x86.i86

import info.osdevelopment.sysemu.processor.Processor
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
  }

}
