package info.osdevelopment.sysemu.remote.rest

import akka.actor.Actor

class DummyServer extends Actor {

  var shutdownCalled = 0

  override def receive = {
    case "shutdown" => shutdownCalled += 1
  }

}
