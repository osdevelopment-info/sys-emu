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
import info.osdevelopment.sysemu.system.{System, SystemConfig, Systems}
import java.io.IOException
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.util.ServiceLoader
import org.apache.commons.cli.{CommandLine, DefaultParser, Option, Options}
import scala.collection.JavaConverters._
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

    val createOption = (Option builder("c") hasArg() longOpt("config") optionalArg(true)
      desc("Create a system from a config file") build())

    val biosOption = (Option builder() hasArg() longOpt("bios") optionalArg(true)
      desc("A file that contains a BIOS image") build())
    options.addOption(biosOption)

    val cpuOption = (Option builder() hasArg() longOpt("cpu") optionalArg(true)
      desc("The CPU to emulate, defaults to 8086") build())
    options addOption(cpuOption)

    val parser = new DefaultParser
    val commandLine = parser parse(options, args)

    val systemConfig = new SystemConfig

    systemConfig.cpu = if (commandLine hasOption("cpu")) {
      commandLine getOptionValue("cpu")
    } else {
      "8086"
    }

    val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
    val processor = systemConfig.cpu match {
      case Some(name) => processorLoader.asScala.find(_.name == name)
      case _ => None
    }

    val bios = if (commandLine hasOption("bios")) {
      readExternalBios(commandLine getOptionValue ("bios"))
    } else {
      readDefaultBios(processor)
    }
    if (bios.isEmpty) {
      return Failure(new IllegalConfigurationException("BIOS file does not exist"))
    }
    val biosSize = bios.get.size
    val biosStart = if (processor.get.maxMemory < 4.Gi) {
      processor.get.maxMemory - biosSize
    } else {
      4.Gi - biosSize
    }
    systemConfig.addMemory(biosStart, bios.get)
    Success(systemConfig)
  }

  private def readExternalBios(fileName: String): scala.Option[Memory] = {
    val biosFile = Paths.get(fileName)
    if (Files.exists(biosFile)) {
      Some(ReadOnlyMemory(Files.newByteChannel(biosFile, StandardOpenOption.READ)))
    } else {
      None
    }
  }

  private def readDefaultBios(processor: scala.Option[Processor]): scala.Option[Memory] = {
    val is = processor match {
      case Some(proc) => Try(proc.getClass.getResourceAsStream(proc.romName))
      case _ => return None
    }
    val romSize = is match {
      case Success(is) => Try(is.available)
      case _ => return None
    }
    val bios = romSize match {
      case Success(size) => Some(new Array[Byte](size))
      case _ => return None
    }
    val read = bios match {
      case Some(b) => Try(is.get.read(b))
      case _ => return None
    }
    read match {
      case Success(_) => Some(ReadOnlyMemory(bios.get))
      case _ => None
    }
  }

}
