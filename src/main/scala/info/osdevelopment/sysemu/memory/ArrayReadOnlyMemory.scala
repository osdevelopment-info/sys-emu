package info.osdevelopment.sysemu.memory

class ArrayReadOnlyMemory (val data: Array[Byte]) extends ReadOnlyMemory {

  /** Return the size of the memory. */
  override def size: Long = data.length

  override protected def doRead(address: Long): Byte = {
    data(address.asInstanceOf[Int])
  }

}
