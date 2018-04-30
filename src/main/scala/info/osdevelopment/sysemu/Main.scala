package info.osdevelopment.sysemu

import info.osdevelopment.sysemu.memory.SimpleReadWriteMemory
import org.apache.commons.cli.{DefaultParser, Option, Options}

object Main {

  def main(args: Array[String]): Unit = {
    new Main run(args)
  }

}

class Main {

  def compose = {
    val mem = SimpleReadWriteMemory(1024 * 1024)

    println(mem.size)
  }

  def run(args: Array[String]): Unit = {
    val options = new Options()
    val cpu = Option builder("c") hasArg() longOpt("cpu") optionalArg(true) build()
    options addOption(cpu)
    val parser = new DefaultParser
    val commandLine = parser parse(options, args)

    print("CPU: ")
    if (commandLine hasOption("c")) {
      println(commandLine getOptionValue("c"))
    } else {
      println("default")
    }

    compose

  }

}
