package org.sw4j.x86emulator.memory

trait LittleEndian {

  def readByte(address: Int): Byte

  def writeByte(address: Int, value: Byte)

  def readShort(address: Int): Short = {
    return (readByte(address) | readByte(address + 1) << 8).asInstanceOf[Short]
  }

  def writeShort(address: Int, value: Short): Unit = {
    writeByte(address, (value & 0xff).asInstanceOf[Byte])
    writeByte(address + 1, (value >> 8 & 0xff).asInstanceOf[Byte])
  }

}
