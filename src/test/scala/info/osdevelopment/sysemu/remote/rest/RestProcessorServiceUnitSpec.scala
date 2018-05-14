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
import org.specs2._

class RestProcessorServiceUnitSpec extends mutable.Specification with Specs2RouteTest {

  "A RestDebugService" >> {
    "with a GET on /processors" >> {
      "should return 200/OK" >> {
        val service = new RestProcessorService()
        Get() ~> service.route ~> check {
          status must_== StatusCodes.OK
        }
      }
      "should return content type 'application/json'" >> {
        val service = new RestProcessorService()
        Get() ~> service.route ~> check {
          contentType must_== ContentTypes.`application/json`
        }
      }
      "should return a JSON array" >> {
        val service = new RestProcessorService()
        Get() ~> service.route ~> check {
          responseAs[String] must be matching "\\[.*\\]"
        }
      }
    }
    "with a GET on /processors/TestProcessor" >> {
      "should return 200/OK" >> {
        val service = new RestProcessorService()
        Get("/TestProcessor") ~> service.route ~> check {
          status must_== StatusCodes.OK
        }
      }
      "should return content type 'application/json'" >> {
        val service = new RestProcessorService()
        Get("/TestProcessor") ~> service.route ~> check {
          contentType must_== ContentTypes.`application/json`
        }
      }
      "should return a JSON object" >> {
        "with the correct name" >> {
          val service = new RestProcessorService()
          Get("/TestProcessor") ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"name\"\\s*:\\s*\"(TestProcessor)\".*\\}"
          }
        }
        "with the max memory filled" >> {
          val service = new RestProcessorService()
          Get("/TestProcessor") ~> service.route ~> check {
            responseAs[String] must be matching "\\{.*\"maxMemory\"\\s*:\\s*(\\d*).*\\}"
          }
        }
      }
    }
    "with a GET on /processors/Unknown" >> {
      "should return 404/NotFound" >> {
        val service = new RestProcessorService()
        Get("/Unknown") ~> service.route ~> check {
          status must_== StatusCodes.NotFound
        }
      }
      "should return content type 'application/json'" >> {
        val service = new RestProcessorService()
        Get("/Unknown") ~> service.route ~> check {
          contentType must_== ContentTypes.`application/json`
        }
      }
      "should return a JSON object" >> {
        "with a message" >> {
          val service = new RestProcessorService()
          Get("/Unknown") ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"message\"\\s*:\\s*\".*\".*\\}"
          }
        }
      }
    }
  }

}
