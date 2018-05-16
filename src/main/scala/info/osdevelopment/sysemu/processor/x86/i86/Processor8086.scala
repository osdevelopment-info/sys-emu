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
package info.osdevelopment.sysemu.processor.x86.i86

import info.osdevelopment.sysemu.processor.Register
import info.osdevelopment.sysemu.processor.x86.ProcessorX86
import info.osdevelopment.sysemu.support.Utilities._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * A concrete 8086 processor.
  */
class Processor8086 extends ProcessorX86 {

  /**
    * Thr registers of the 8086 processor.
    */
  private val _registers = mutable.Map[String, Int](
    ("AX" -> 0),
    ("BX" -> 0),
    ("CX" -> 0),
    ("DX" -> 0),
    ("DI" -> 0),
    ("SI" -> 0),
    ("BP" -> 0),
    ("SP" -> 0),
    ("FLAGS" -> 0),
    ("IP" -> 0),
    ("CS" -> 0xffff),
    ("DS" -> 0),
    ("ES" -> 0),
    ("SS" -> 0)
  )

  /**
    * Thr name of the processor, "8086" in this case.
    * @return the name of the processor
    */
  override def name = "8086"

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
  override def registers: Try[Map[Int, Map[String, Register]]] = {
    Success(Map((0 -> internalRegisters)))
  }

  /**
    * All registers of a core when the core is stopped.
    *
    * The result is Success if the core can return the registers. When Failure is returned then the core
    * cannot return the registers at the moment, e.g. because it is running at the moment.
    *
    * The key of the map is the register name.
    *
    * @param core the core for which the registers should be queried. This parameter is ignored by this processor.
    * @return A Try containing a Map with the register name as key and the register as value.
    */
  override def registers(core: Int): Try[Map[String, Register]] = {
    Success(internalRegisters)
  }

  /**
    * Maps the internal registers to a register Map structure.
    *
    * @return a Map containing the register name as key and the register as value.
    */
  private def internalRegisters: Map[String, Register] = {
    _registers.toMap.transform((name, content) => new Register(0, name, content, 16))
  }

  /**
    * Return the register with the given name of the given core, e.g. "AX".
    *
    * @param core the core for which the register should be returned. This parameter is ignored by this processor.
    * @param name the name of the register to return
    * @return the register named by name. Failure if the register does not exist or cannot be returned.
    */
  override def register(core: Int, name: String): Try[Register] = {
    name match {
      // first check for the 8bit-aliases
      case "AH" | "AL" | "BH" | "BL" | "CH" | "CL" | "DH" | "DL" =>
        get8BitRegister(name)
      case _ =>
        if (_registers.contains(name)) {
          Success(new Register(0, name, BigInt(_registers(name).toShort), 16))
        } else {
          Failure(new IllegalArgumentException("The register " + name + " is not known by this processor."))
        }
    }
  }

  /**
    * Returns the content of an 8 bit register (AH, AL, BH, BL, CH, CL, DH or DL). This registers are part of the 16 bit
    * registers AX, BX, CX or DX.
    *
    * @param name the 8 bit name of a register.
    * @return the register addressed by name.
    */
  private def get8BitRegister(name: String): Try[Register] = {
    val _16bitName = name.substring(0, 1) + "X"
    name.charAt(1) match {
      case 'H' =>
        Success(new Register(0, name, BigInt(((_registers(_16bitName) >> 8) & 0xff).toShort), 8))
      case 'L' =>
        Success(new Register(0, name, BigInt((_registers(_16bitName) & 0xff).toShort), 8))
      case _ =>
        Failure(new IllegalArgumentException("The register " + name + " is not known by this processor."))
    }
  }

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
  override def register(register: Register): Try[Register] = {
    register.name match {
      case "AH" | "AL" | "BH" | "BL" | "CH" | "CL" | "DH" | "DL" =>
        set8BitRegister(register)
      case _ =>
        if (_registers.contains(register.name)) {
          set16Bit(register)
        } else {
          Failure(new IllegalArgumentException("The register " + name + " is not known by the processor."))
        }
    }
  }

  /**
    * Sets the contents of the 8 bit register (AH, AL, BH, BL, CH, CL, DH or DL) to the value given in register. This
    * registers are part of the 8 bit registers AX, BX, CX or DX.
    *
    * @param register the register to set.
    * @return the new register.
    */
  private def set8BitRegister(register: Register): Try[Register] = {
    if (register.content <= 255 & register.content >= -128) {
      val _16bit = register.name.substring(0, 1) + "X"
      register.name.charAt(1) match {
        case 'H' =>
          _registers(_16bit) = (_registers(_16bit) & 0x00ff) | ((register.content & 0xff) << 8).toShort
        case 'L' =>
          _registers(_16bit) = (_registers(_16bit) & 0xff00) | (register.content & 0xff).toShort
      }
      this.register(0, register.name)
    } else {
      Failure(new IllegalArgumentException("The register " + name + " is not known by this processor."))
    }
  }

  /**
    * Sets the contents of the 16 bit register to the value given in register.
    *
    * @param register the register with the new content.
    * @return the new register.
    */
  private def set16Bit(register: Register): Try[Register] = {
    if (register.content <= 65535 & register.content >= -32768) {
      _registers(register.name) = (register.content & 0xffff).toShort
      this.register(0, register.name)
    } else {
      Failure(new IllegalArgumentException("The register " + name + " is not known by this processor."))
    }
  }

  /**
    * The maximum memory that can be handled by the processor. The 8086 can handle a maximum memory of 1 MiB.
    *
    * @return the maximum addressable memory
    */
  override def maxMemory: Long = 1.Mi

  /**
    * The suggested ROM/BIOS name for the processor. The ROM can then be loaded as resource.
    *
    * @return the suggested ROM/BIOS name
    */
  override def romName: String = "bios86"

  /**
    * Resets the processor and starts it new.
    */
  override def reset = {
    _registers.transform((name, _) => {
      name match {
        case "CS" => 0xffff
        case _ => 0
      }
    })
  }

  /**
    * Do a single step of the processor (normally execute the next instruction).
    */
  override def step: Unit = {
  }

}
