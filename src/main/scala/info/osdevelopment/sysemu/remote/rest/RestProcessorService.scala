package info.osdevelopment.sysemu.remote.rest

import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.model.{MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import info.osdevelopment.sysemu.processor.Processor
import info.osdevelopment.sysemu.remote.json.SysEmuJsonProtocol
import java.util.ServiceLoader
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import spray.json.{JsObject, JsString}

class RestProcessorService extends SysEmuJsonProtocol {

  val log = LoggerFactory getLogger classOf[RestProcessorService]

  def route = {
    respondWithHeader(`Content-Type`(MediaTypes.`application/json`))
    pathPrefix(Segment) { name =>
      get {
        val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
        val processorTry = Try(processorLoader.asScala.find(_.name == name))
        processorTry match {
          case Failure(_) =>
            complete(StatusCodes.InternalServerError, "Malformed service in server")
          case Success(processor) =>
            processor.find(_.name == name) match {
              case Some(processor) =>
                complete(StatusCodes.OK, processor)
              case None =>
                complete(StatusCodes.NotFound,
                  JsObject(("message", JsString("No processor with name " + name + " can be found."))))
            }
        }
      }
    } ~
    get {
      val processorLoader: ServiceLoader[Processor] = ServiceLoader.load(classOf[Processor])
      val processorTry = Try(processorLoader.asScala.map((p) => p.name))
      processorTry match {
        case Failure(_) =>
          complete(StatusCodes.InternalServerError, "Malformed service in server")
        case Success(processors) =>
          complete(StatusCodes.OK, processors)
      }
    }
  }

}
