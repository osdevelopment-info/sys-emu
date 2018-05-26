package info.osdevelopment.sysemu.processor

/**
  * Defines a register including a name, a content and a size. The size is optional. This class should be used to ask a
  * processor for registers including content and to set the register content of a processor. Processors should always
  * set the size whereas external users should leave it empty.
  *
  * @param core the core to which the given register belongs (starting with 0)
  * @param name the name of the register, e.g. "A" or "SI"
  * @param content the current content of the register or the value that should be set to the register
  * @param size the size of the register in bits. Should always be set by processors.
  */
final class Register(val core: Int, val name: String, val content: BigInt, val size: Int = 0) {


}
