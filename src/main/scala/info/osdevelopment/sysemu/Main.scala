/* sys-emu - A system emulator
 * Copyright (C) 2018 U. Plonus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.osdevelopment.sysemu


import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import info.osdevelopment.sysemu.config.Configuration
import info.osdevelopment.sysemu.memory.{Memory, ReadOnlyMemory}
import info.osdevelopment.sysemu.processor.Processor
import info.osdevelopment.sysemu.processor.x86.i86.Processor8086
import info.osdevelopment.sysemu.remote.rest.RestDebugServer
import info.osdevelopment.sysemu.support.Utilities._
import info.osdevelopment.sysemu.system.{System, SystemConfig}
import java.nio.file.{Files, Paths, StandardOpenOption}
import org.apache.commons.cli.{CommandLine, DefaultParser, Option, Options}
import scala.util.{Failure, Success, Try}

object Main {

  def main(args: Array[String]): Unit = {
    new Main run args
  }

}

class Main extends Configuration {

  def create(config: SystemConfig): System = {
    null
  }

  def run(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("emu-system", config)
    val system = new System
    val httpRouter = actorSystem.actorOf(Props(classOf[RestDebugServer], actorSystem, this), "restDebugServer")
    httpRouter ! "start"

    /*val sysConfig = createConfigFromCommandLine(args)
    sysConfig match {
      case Success(c) => create(c)
      case Failure(msg) =>
        println(msg)
        sys.exit(-1)
    }*/
  }

  @throws[IllegalConfigurationException]
  def createConfigFromCommandLine(args: Array[String]): Try[SystemConfig] = {
    val options = new Options()
    val biosOption = (Option builder("b") hasArg() longOpt("bios") optionalArg(true)
      desc("A file that contains a BIOS image") build())
    options.addOption(biosOption)

    val cpuOption = (Option builder("c") hasArg() longOpt("cpu") optionalArg(true)
      desc("The CPU to emulate") build())
    options addOption(cpuOption)

    val parser = new DefaultParser
    val commandLine = parser parse(options, args)

    val systemConfig = new SystemConfig

    val cpu = if (commandLine hasOption("c")) {
      commandLine getOptionValue("c") match {
        case "8086" => new Processor8086
        case _ =>
          return Failure(new IllegalConfigurationException("Invalid CPU"))
      }
    } else {
      new Processor8086
    }
    systemConfig.addProcessor(cpu)

    val bios = if (commandLine hasOption("b")) {
      readExternalBios(commandLine getOptionValue ("b"))
    } else {
      readDefaultBios(cpu)
    }
    if (bios.isEmpty) {
      return Failure(new IllegalConfigurationException("BIOS file does not exist"))
    }
    val biosSize = bios.get.size
    val biosStart = if (cpu.maxMemory < 4.Gi) {
      cpu.maxMemory - biosSize
    } else {
      4.Gi - biosSize
    }
    systemConfig.addMemory(biosStart, bios.get)
    Success(systemConfig)
  }

  private def readExternalBios(fileName: String): scala.Option[Memory] = {
    val biosFile = Paths.get(fileName)
    if (!Files.exists(biosFile)) {
      None
    } else {
      Some(ReadOnlyMemory(Files.newByteChannel(biosFile, StandardOpenOption.READ)))
    }
  }

  private def readDefaultBios(processor: Processor): scala.Option[Memory] = {
    processor match {
      case p: Processor8086 =>
        val is = getClass.getResourceAsStream("bios86")
        val rom = new Array[Byte](is.available())
        is.read(rom)
        Some(ReadOnlyMemory(rom))
      case _ =>
        val is = getClass.getResourceAsStream("bios86")
        val rom = new Array[Byte](is.available())
        is.read(rom)
        Some(ReadOnlyMemory(rom))
    }
  }

}
