package info.osdevelopment.sysemu.memory

import info.osdevelopment.sysemu.support.Utilities._

object CombinedReadWriteMemory {

  def apply(): CombinedReadWriteMemory = {
    return new CombinedReadWriteMemory(2.Gi)
  }

  def apply(size: Long): CombinedReadWriteMemory = {
    return new CombinedReadWriteMemory(size)
  }

}

/**
  * A read-write memory that can hold up to 2^60^ Bytes (1 EiB). Please note that the size is immediately allocated.
  * @param size
  */
class CombinedReadWriteMemory private(val size: Long) extends Memory {

  if (size <= 0) {
    throw new IllegalArgumentException("Size must be greater than 0")
  }
  if (size > 1.Ei) {
    throw new IllegalArgumentException("Max size supported is 1 EiB")
  }
  val remaining = size % 1.Gi
  val numberModules = (if (remaining == 0) size / (1.Gi) else size / (1.Gi) + 1).asInstanceOf[Int]
  val modules = Array.fill(numberModules){ SimpleReadWriteMemory((1.Gi).asInstanceOf[Int]) }

  /**
    * Read a single byte from the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override def readByte(address: Long): Byte = {
    if (address < 0 | address > size) throw new IllegalAddressException
    val module = address / (1.Gi)
    val offset = address % (1.Gi)
    modules(module.asInstanceOf[Int]).readByte(offset)
  }

  /**
    * Write a single byte to the memory at the given address.
    *
    * @throws IllegalAddressException if the address is out of range (not between 0 and size() - 1)
    */
  override def writeByte(address: Long, value: Byte): Unit = {
    if (address < 0 | address > size) throw new IllegalAddressException
    val module = address / (1.Gi)
    val offset = address % (1.Gi)
    modules(module.asInstanceOf[Int]).writeByte(offset.asInstanceOf[Int], value)
  }

}
