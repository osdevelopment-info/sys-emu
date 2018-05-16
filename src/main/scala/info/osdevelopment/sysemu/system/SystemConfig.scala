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
package info.osdevelopment.sysemu.system

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions}
import info.osdevelopment.sysemu.memory.Memory
import info.osdevelopment.sysemu.processor.{IllegalMemoryLayoutException, Processor}
import java.io.File
import scala.collection.LinearSeq
import scala.util.{Failure, Success, Try}

/**
  * This class is used to create a system configuration which then can be used to create a system.
  *
  * @param configFile the file to read the configuration from
  */
class SystemConfig (val configFile: Option[File]) {

  /**
    * The CPU(s) of the system.
    */
  private var _cpu: Option[String] = None

  /**
    * The number of CPU(s) of the type _cpu in the system.
    */
  private var _cpuCount: Int = 0

  /**
    * A convenient constructor to create a system configuration without any configuration file.
    * @return a new empty system configuration
    */
  def this() = {
    this(None)
  }

  val parseOptions = ConfigParseOptions.defaults.setAllowMissing(false)
  /**
    * The read configuration from the file.
    */
  private val config: Option[Config] = {
    configFile match {
      case Some(cFile) =>
        Try(ConfigFactory.parseFileAnySyntax(cFile, parseOptions)) match {
          case Success(conf) => Some(conf)
          case _ => None
        }
      case None => None
    }
  }
  config match {
    case Some(conf) =>
      cpu = Try(conf.getString("system.cpu")).getOrElse("8086")
      cpuCount = Try(conf.getInt("system.cpuCount")).getOrElse(1)
    case None => None
  }

  /**
    * Sets the CPU of the system.
    * @param cpu the CPU of the system.
    */
  def cpu_=(cpu: String) = {
    if (cpu == null) {
      _cpu = None
    } else {
      _cpu = Some(cpu)
    }
  }

  /**
    * Returns the currently set CPU of the system.
    * @return the currently set CPU
    */
  def cpu: Option[String] = _cpu

  /**
    * Sets the number of CPU(s) of the type _cpu in the system.
    * @param cpuCount the number of CPU(s) in the system
    */
  def cpuCount_=(cpuCount: Int) = {
    _cpuCount = cpuCount
  }

  /**
    * Returns the currently set number of CPU(s) of the type _cpu of the system.
    * @return the currently set number of CPU(s)
    */
  def cpuCount: Int = {
    _cpuCount
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
