package org.sw4j.x86emulator.memory

/**
  * The object ReadWriteMemory used to create instances of the memory. The maximum memory that can be handled is 1 GiB.
  */
object ReadWriteMemory {

  private val KIB = 1024
  private val MIB = 1024 * 1024
  private val GIB = 1024 * 1024 * 1024

  def apply(): ReadWriteMemory = {
    return new ReadWriteMemoryLittleEndian(8388608)
  }

  def apply(size: Int): ReadWriteMemory = {
    return new ReadWriteMemoryLittleEndian(size)
  }

  class ReadWriteMemoryLittleEndian(size: Int) extends ReadWriteMemory(size) with LittleEndian

  object Endianness extends Enumeration {

    type Endianness = Value
    val LittleEndian, BigEndian = Value

  }

}

/**
  * A read-write memory (aka RAM) with a default size of 8MiB.
  * @param size the size of the memory
  */
abstract class ReadWriteMemory private (val size: Int) {

  if (size <= 0) {
    throw new IllegalArgumentException("Size must be greater than 0")
  }
  if (size > ReadWriteMemory.GIB) {
    throw new IllegalArgumentException("Max size supported is 1 GiB")
  }
  val memory = new Array[Byte](size)

  def readByte(address: Int): Byte = {
    return memory(address)
  }

  def writeByte(address: Int, value: Byte): Unit = {
    memory(address) = value
  }

}
