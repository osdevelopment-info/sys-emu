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
package info.osdevelopment.sysemu.processor

import info.osdevelopment.sysemu.memory.Memory

trait Processor {

  val memoryMap = collection.mutable.Map[Long, Memory]()

  /**
    * Adds a memory area to this processor. The memory has a base address (the lowest address handled by this
    * processor).
    * @param baseAddress
    * @param memory
    */
  def addMemory(baseAddress: Long, memory: Memory): Unit = {
    memoryMap.foreach(address => {
      val startAddress = address._1
      val endAddress = startAddress + address._2.size
      if (baseAddress >= startAddress & baseAddress < endAddress)
        throw new IllegalMemoryLayoutException("Memory overlaps")
      if (baseAddress + memory.size >= startAddress & baseAddress + memory.size < endAddress)
        throw new IllegalMemoryLayoutException("Memory overlaps")
    })
    if (baseAddress + memory.size > maxMemory) throw new IllegalMemoryLayoutException("Memory exceeds max memory")
    memoryMap += baseAddress -> memory
  }

  /**
    * The maximum memory that can be handled by the processor
    * @return
    */
  def maxMemory: Long

}
