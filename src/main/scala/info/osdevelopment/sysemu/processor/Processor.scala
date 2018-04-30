package info.osdevelopment.sysemu.processor

import info.osdevelopment.sysemu.memory.Memory

trait Processor {

  def addMemory(baseAddress: Long, memory: Memory)

}
