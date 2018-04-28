package info.osdevelopment.x86emulator.memory

import java.nio.channels.SeekableByteChannel

object ReadOnlyMemory {

  def apply(data: Array[Byte]): ReadOnlyMemory = {
    new ArrayReadOnlyMemory(data)
  }

  def apply(data: SeekableByteChannel): ReadOnlyMemory = {
    new ChannelReadOnlyMemory(data)
  }

}

abstract class ReadOnlyMemory protected() extends Memory {

  /**
    * Read a single byte from the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def readByte(address: Long): Byte = {
    if (address < 0 | address >= size) throw new IllegalAddressException
    doRead(address)
  }

  protected def doRead(address: Long): Byte

  /**
    * Write a single byte to the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def writeByte(address: Long, value: Byte): Unit = {
    if (address < 0 | address >= size) throw new IllegalAddressException
  }

}
