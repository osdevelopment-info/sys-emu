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
package info.osdevelopment.sysemu.config

import info.osdevelopment.sysemu.memory.Memory
import info.osdevelopment.sysemu.processor.{IllegalMemoryLayoutException, Processor}

import scala.collection.LinearSeq

class SystemConfig {

  private val _processors = collection.mutable.ListBuffer[Processor]()

  def addProcessor(processor: Processor): SystemConfig = {
    _processors += processor
    this
  }

  def processors: LinearSeq[Processor] = {
    _processors.toList
  }

  private val memoryMap = collection.mutable.Map[Long, Memory]()

  @throws[IllegalMemoryLayoutException]
  def addMemory(baseAddress: Long, memory: Memory): SystemConfig = {
    memoryMap.foreach(address => {
      val startAddress = address._1
      val endAddress = startAddress + address._2.size
      if (baseAddress >= startAddress & baseAddress < endAddress)
        throw new IllegalMemoryLayoutException("Memory overlaps")
      if (baseAddress + memory.size >= startAddress & baseAddress + memory.size < endAddress)
        throw new IllegalMemoryLayoutException("Memory overlaps")
    })
    memoryMap += baseAddress -> memory
    this
  }

  def memory: collection.immutable.Map[Long, Memory] = {
    memoryMap.toMap
  }

}
