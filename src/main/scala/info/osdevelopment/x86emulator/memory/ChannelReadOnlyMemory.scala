package info.osdevelopment.x86emulator.memory

import info.osdevelopment.x86emulator.support.Utilities._
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
    if (indexInCache > cachedBytes) throw new IllegalAddressException
    romCache.get(indexInCache)
  }

}
