package info.osdevelopment.sysemu.processor.test

import info.osdevelopment.sysemu.processor.{Processor, Register}
import info.osdevelopment.sysemu.support.Utilities._
import scala.util.{Failure, Try}

/**
  * A processor used for unit testing.
  */
class TestProcessor extends Processor {

  /**
    * The name of the processor, e.g. "8086" or "68000"
    *
    * @return the name of the processor
    */
  override def name = "TestProcessor"

  /**
    * All registers of all cores when the cores are stopped.
    *
    * The result is Success if the cores can return the registers. When Failure is returned then the cores
    * cannot return the registers at the moment, e.g. because any is running at the moment.
    *
    * The key of the map is the register name.
    *
    * @return A Try containing a Map with the core as key and a Map with the register name as key and the register as
    *         value.
    */
  override def registers: Try[Map[Int, Map[String, Register]]] = {
    Failure(new UnsupportedOperationException("not supported in TestProcessor"))
  }

  /**
    * All registers of a core when the core is stopped.
    *
    * The result is Success if the core can return the registers. When Failure is returned then the core
    * cannot return the registers at the moment, e.g. because it is running at the moment.
    *
    * The key of the map is the register name.
    *
    * @param core the core for which the registers should be queried
    * @return A Try containing a Map with the register name as key and the register as value.
    */
  override def registers(core: Int): Try[Map[String, Register]] = {
    Failure(new UnsupportedOperationException("not supported in TestProcessor"))
  }

  /**
    * Return the register with the given name of the given core, e.g. "AX" or "D1".
    *
    * @param core the core for which the register should be returned
    * @param name the name of the register to return
    * @return the register named by name. Failure if the register does not exist or cannot be returned.
    */
  override def register(core: Int, name: String): Try[Register] = {
    Failure(new UnsupportedOperationException("not supported in TestProcessor"))
  }

  /**
    * Sets the register of the processor to the given content.
    *
    * The returned register (in case of Success(_)) contains the new register content. Failure is returned in case that
    *  - the processor is running and therefore the register cannot be set
    *  - the register is unknown to the processor
    *  - the value of the register is invalid
    *
    * @param register the register to set
    * @return Success with the new register value or Failure if the register cannot be set.
    */
  override def register(register: Register): Try[Register] = {
    Failure(new UnsupportedOperationException("not supported in TestProcessor"))
  }

  /**
    * The maximum memory that can be handled by the processor.
    *
    * @return the maximum addressable memory
    */
  override def maxMemory: Long = 1.Mi

  /**
    * The suggested ROM/BIOS name for the processor. The ROM can then be loaded as resource.
    *
    * @return the suggested ROM/BIOS name
    */
  override def romName: String = "testrom"

  /**
    * Reset the processor and start it new.
    */
  override def reset = {}

  /**
    * Do a single step of the processor (normally execute the next instruction).
    */
  override def step: Unit = {}

}
