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

import info.osdevelopment.sysemu.processor.x86.ProcessorX86
import info.osdevelopment.sysemu.support.Utilities._

/**
  * A concrete 8086 processor.
  */
class Processor8086 extends ProcessorX86 {

  /**
    * The maximum memory that can be handled by the processor. The 8086 can handle at maximum 1 MiB of memory.
    *
    * @return the maximum memory that can be handled by the 8086.
    */
  override def maxMemory: Long = 1.Mi

}
