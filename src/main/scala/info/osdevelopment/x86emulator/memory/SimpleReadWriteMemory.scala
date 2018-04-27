package info.osdevelopment.x86emulator.memory

import info.osdevelopment.x86emulator.support.Utilities._

/**
  * The object ReadWriteMemory used to create instances of the memory. The maximum memory that can be handled is 1 GiB.
  */
object SimpleReadWriteMemory {

  def apply(): SimpleReadWriteMemory = {
    return new SimpleReadWriteMemory(8.Mi)
  }

  def apply(size: Int): SimpleReadWriteMemory = {
    return new SimpleReadWriteMemory(size)
  }

}

/**
  * A read-write memory (aka RAM) with a default size of 8MiB. The maximum size is 2^30^ Bytes (1 GiB).
  * @param size the size of the memory
  */
class SimpleReadWriteMemory private(val size: Long) extends Memory {

  if (size <= 0) {
    throw new IllegalArgumentException("Size must be greater than 0")
  }
  if (size > 1.Gi) {
    throw new IllegalArgumentException("Max size supported is 1 GiB")
  }
  val memory = new Array[Byte](size.asInstanceOf[Int])

  @throws(classOf[IllegalAddressException])
  override def readByte(address: Long): Byte = {
    if (address >= size | address < 0) throw new IllegalAddressException
    memory(address.asInstanceOf[Int])
  }

  @throws(classOf[IllegalAddressException])
  override def writeByte(address: Long, value: Byte): Unit = {
    if (address >= size | address < 0) throw new IllegalAddressException
    memory(address.asInstanceOf[Int]) = value
  }

}
