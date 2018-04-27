package info.osdevelopment.x86emulator.memory

import java.nio.channels.SeekableByteChannel

object ReadOnlyMemory {

  def apply(data: Array[Byte]): ReadOnlyMemory = {
    new ReadOnlyMemory(data)
  }

}

class ReadOnlyMemory(var data: Array[Byte]) {

  def this(bc: SeekableByteChannel) {
    this(new Array[Byte](0))
    if (bc.size() > Int.MaxValue) {
      throw new IllegalArgumentException()
    }
  }

}
