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
package info.osdevelopment.sysemu.memory

import info.osdevelopment.sysemu.support.Utilities._
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

class ChannelReadOnlyMemory(val data: SeekableByteChannel) extends ReadOnlyMemory {

  var startAddress = -1L
  val romCache = ByteBuffer.allocate(256.Ki.asInstanceOf[Int])
  var cachedBytes = -1

  /** Return the size of the memory. */
  override def size: Long = data.size

  override protected def doRead(address: Long): Byte = {
    val block = address / 256.Ki
    // In the cache is the wrong block
    if (block != startAddress) {
      data.position(block * 256.Ki)
      romCache.clear
      cachedBytes = data.read(romCache)
      startAddress = block
    }
    val indexInCache = (address % 256.Ki).asInstanceOf[Int]
    if (indexInCache > cachedBytes) throw new IllegalAddressException("Address outside memory")
    romCache.get(indexInCache)
  }

}
