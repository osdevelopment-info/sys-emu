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
package info.osdevelopment.sysemu.remote.rest

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.Specs2RouteTest
import akka.testkit.TestProbe
import org.specs2._

class RestDebugServiceUnitSpec extends mutable.Specification with Specs2RouteTest {

  "A RestDebugService" >> {
    "with a /GET at /v0.1/systems" >> {
      "should return a JSON array from RestSystemService" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Get("/v0.1/systems") ~> service.route ~> check {
          responseAs[String] must be matching "\\[.*\\]"
        }
      }
    }
    "with a POST at /v0.1/shutdown" >> {
      "should return 200/OK" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Post("/v0.1/shutdown") ~> service.route ~> check {
          status must_== StatusCodes.OK
        }
      }
      "should initiate a shutdown at the server" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Post("/v0.1/shutdown") ~> service.route ~> check {
          server.expectMsg("shutdown") must_== "shutdown"
        }
      }
    }
    "with a GET on /v0.1/processors" >> {
      "should return 200/OK" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Get("/v0.1/processors") ~> service.route ~> check {
          status must_== StatusCodes.OK
        }
      }
    }
  }

}
