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
import scala.util.Try

/**
  * A read-only memory backed by a `SeekableByteChannel`, e.g. a file. This
  * implementation reads part of the file into a cache and tries to serve the data from this cache.
  * @param data the data of the read-only memory
  */
class ChannelReadOnlyMemory(val data: SeekableByteChannel) extends ReadOnlyMemory {

  /** The number of the block that is cached. */
  private var cachedBlock = -1L
  /** The cache of the read-only memory. */
  private val romCache = ByteBuffer.allocate(256.Ki.asInstanceOf[Int])
  /** The number of bytes read from the channel during the last caching operation. */
  private var cachedBytes = -1

  /**
    * Returns the size of the memory.
    * @return the size of the memory
    */
  override def size: Long = data.size

  /**
    * The read method which caches a block of data and returns the value to be read from the cache.
    * @param address the address to read
    * @return a `Success` with the byte read at the given address or a `Failure`
    */
  override protected def doRead(address: Long): Try[Byte] = {
    val block = address / 256.Ki
    Try {
      // In the cache is the wrong block
      if (block != cachedBlock) {
        data.position(block * 256.Ki)
        romCache.clear
        cachedBytes = data.read(romCache)
        cachedBlock = block
      }
      val indexInCache = (address % 256.Ki).asInstanceOf[Int]
      if (indexInCache > cachedBytes) throw new IllegalAddressException("Address outside memory")
      romCache.get(indexInCache)
    }
  }

}
