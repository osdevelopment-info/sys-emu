package info.osdevelopment.sysemu.remote.rest

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory

class RestDebugService(val server: ActorRef) {

  val log = LoggerFactory getLogger classOf[RestDebugService]

  def route = {
    pathSingleSlash {
      get {
        complete(StatusCodes.OK)
      }
    } ~
    path("shutdown") {
      post {
        server ! "shutdown"
        complete(StatusCodes.OK)
      }
    }
  }

}
