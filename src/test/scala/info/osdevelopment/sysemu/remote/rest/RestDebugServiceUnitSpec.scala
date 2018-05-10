package info.osdevelopment.sysemu.remote.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import akka.testkit.TestProbe
import org.specs2._

class RestDebugServiceUnitSpec extends mutable.Specification with Specs2RouteTest {

  "A RestDebugService" >> {
    "should return 200/OK on GET /" >> {
      val server = TestProbe()
      val service = new RestDebugService(server.ref)
      Get() ~> service.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "with a POST at /shutdown" >> {
      "should return 200/OK" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Post("/shutdown") ~> service.route ~> check {
          status shouldEqual StatusCodes.OK
        }
      }
      "should initiate a shutdown at the server" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Post("/shutdown") ~> service.route ~> check {
          server.expectMsg("shutdown") must_== "shutdown"
        }
      }
    }
  }

}
