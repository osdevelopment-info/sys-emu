package info.osdevelopment.sysemu.memory

import info.osdevelopment.sysemu.support.Utilities._

object ReadWriteMemory {

  def apply(): ReadWriteMemory = {
    apply(1.Gi)
  }

  def apply(size: Long): ReadWriteMemory = {
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0")
    }
    if (size > 1.Ei) {
      throw new IllegalArgumentException("Max size supported is 1 EiB")
    }
    if (size > 1.Gi) {
      CombinedReadWriteMemory(size)
    } else {
      SimpleReadWriteMemory(size)
    }
  }

}

abstract class ReadWriteMemory protected() extends Memory {

  /**
    * Read a single byte from the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def readByte(address: Long): Byte = {
    if (address < 0 | address >= size) throw new IllegalAddressException("Address outside memory")
    doRead(address)
  }

  protected def doRead(address: Long): Byte

  /**
    * Write a single byte to the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override final def writeByte(address: Long, value: Byte): Unit = {
    if (address < 0 | address >= size) throw new IllegalAddressException("Address outside memory")
    doWrite(address, value)
  }

  protected def doWrite(address: Long, value: Byte)

}
