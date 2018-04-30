package info.osdevelopment.sysemu.processor.x86

import info.osdevelopment.sysemu.memory.Memory
import info.osdevelopment.sysemu.processor.Processor

class Processor8086 extends Processor {

  val memoryMap = collection.mutable.Map[Long, Memory]()

  override def addMemory(baseAddress: Long, memory: Memory): Unit = {

  }

}
