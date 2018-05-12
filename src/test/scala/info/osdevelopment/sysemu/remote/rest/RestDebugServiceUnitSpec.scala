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
        status must_== StatusCodes.OK
      }
    }
    "with a POST at /shutdown" >> {
      "should return 200/OK" >> {
        val server = TestProbe()
        val service = new RestDebugService(server.ref)
        Post("/shutdown") ~> service.route ~> check {
          status must_== StatusCodes.OK
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
