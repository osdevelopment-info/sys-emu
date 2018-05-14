package info.osdevelopment.sysemu.processor.test

import info.osdevelopment.sysemu.processor.Processor
import info.osdevelopment.sysemu.support.Utilities._

class TestProcessor extends Processor {

  override def name = "TestProcessor"

  /**
    * The maximum memory that can be handled by the processor
    *
    * @return
    */
  override def maxMemory: Long = 1.Mi

  override def reset = {}

  override def step: Unit = {}

}
