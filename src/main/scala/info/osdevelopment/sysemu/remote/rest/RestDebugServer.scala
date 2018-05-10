package info.osdevelopment.sysemu.remote.rest

import akka.actor.{Actor, ActorSystem}
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import info.osdevelopment.sysemu.config.Configuration
import scala.concurrent.Future

class RestDebugServer(implicit val actorSystem: ActorSystem, val config: Configuration)
  extends Actor with SLF4JLogging {

  var bindingFuture: Option[Future[ServerBinding]] = None

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  val service = new RestDebugService(self)

  override def receive = {
    case "start" => start
    case "shutdown" => shutdown
  }

  def start = {
    bindingFuture match {
      case None => bindingFuture = Some(Http().bindAndHandle(service.route, config.serviceHost, config.servicePort))
      case _ => log.warn("Tried to start an already running server")
    }
  }

  def shutdown = {
    bindingFuture match {
      case Some(future) =>
        future.flatMap(_.unbind())
        actorSystem.terminate()
      case None => log.warn("Tried to shutdown a not running server")
    }
  }

}
