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
import scala.util.Try

/**
  * A trait defining the basics of a processor in a system.
  */
trait Processor {

  /**
    * The memory map of the processor.
    */
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
    * The name of the processor, e.g. "8086" or "68000"
    *
    * @return the name of the processor
    */
  def name: String

  /**
    * All registers of all cores when the cores are stopped.
    *
    * The result is Success if the cores can return the registers. When Failure is returned then the cores
    * cannot return the registers at the moment, e.g. because any is running at the moment.
    *
    * The key of the map is the register name.
    *
    * @return A Try containing a Map with the core as key and a Map with the register name as key and the register as
    *         value.
    */
  def registers: Try[Map[Int, Map[String, Register]]]

  /**
    * All registers of a core when the core is stopped.
    *
    * The result is Success if the core can return the registers. When Failure is returned then the core
    * cannot return the registers at the moment, e.g. because it is running at the moment.
    *
    * The key of the map is the register name.
    *
    * @param core the core for which the registers should be queried
    * @return A Try containing a Map with the register name as key and the register as value.
    */
  def registers(core: Int): Try[Map[String, Register]]

  /**
    * Return the register with the given name of the given core, e.g. "AX" or "D1".
    *
    * @param core the core for which the register should be returned
    * @param name the name of the register to return
    * @return the register named by name. Failure if the register does not exist or cannot be returned.
    */
  def register(core: Int, name: String): Try[Register]

  /**
    * Sets the register of the processor to the given content.
    *
    * The returned register (in case of Success(_)) contains the new register content. Failure is returned in case that
    *  - the processor is running and therefore the register cannot be set
    *  - the register is unknown to the processor
    *  - the value of the register is invalid
    *
    * @param register the register to set
    * @return Success with the new register value or Failure if the register cannot be set.
    */
  def register(register: Register): Try[Register]

  /**
    * The maximum memory that can be handled by the processor.
    *
    * @return the maximum addressable memory
    */
  def maxMemory: Long

  /**
    * Resets the processor and starts it new.
    */
  def reset: Unit

  /**
    * Do a single step of the processor (normally execute the next instruction).
    */
  def step: Unit

}
