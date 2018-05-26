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
import org.specs2.mutable

class RestSystemServiceUnitSpec extends mutable.Specification with Specs2RouteTest {

  "A RestDebugService" >> {
    "with a GET on /v0.1" >> {
      "should return 200/OK" >> {
        val service = new RestSystemService
        Get() ~> service.route ~> check {
          status must_== StatusCodes.OK
        }
      }
      "should return content type 'application/json'" >> {
        val service = new RestSystemService
        Get() ~> service.route ~> check {
          contentType must_== ContentTypes.`application/json`
        }
      }
      "should return a JSON array" >> {
        val service = new RestSystemService
        Get() ~> service.route ~> check {
          responseAs[String] must be matching "\\[.*\\]"
        }
      }
    }
    "with a POST on /" >> {
      "should return a 201/CREATED" >> {
        val service = new RestSystemService
        Post() ~> service.route ~> check {
          status must_== StatusCodes.Created
        }
      }
      "should return content type 'application/json'" >> {
        val service = new RestSystemService
        Post() ~> service.route ~> check {
          contentType must_== ContentTypes.`application/json`
        }
      }
      "should return a JSON object with a UUID" >> {
        val service = new RestSystemService
        Post() ~> service.route ~> check {
          responseAs[String] must be matching "\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}"
        }
      }
    }
    "with a GET on /<uuid>" >> {
      "and an invalid UUID" >> {
        "should return a 404/NOT FOUND" >> {
          val service = new RestSystemService
          Get("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            status must_== StatusCodes.NotFound
          }
        }
        "should return content type 'application/json'" >> {
          val service = new RestSystemService
          Get("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            contentType must_== ContentTypes.`application/json`
          }
        }
        "should return a JSON object with a message" >> {
          val service = new RestSystemService
          Get("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"message\"\\s*:\\s*\".*\".*\\}"
          }
        }
      }
      "and a valid UUID" >> {
        "should return a 200/OK" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Get("/" + responseUuid) ~> service.route ~> check {
            status must_== StatusCodes.OK
          }
        }
        "should return content type 'application/json'" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Get("/" + responseUuid) ~> service.route ~> check {
            contentType must_== ContentTypes.`application/json`
          }
        }
        "should return a JSON object with the same UUID as requested" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Get("/" + responseUuid) ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"uuid\"\\s*:\\s*\"" + responseUuid + "\".*\\}"
          }
        }
      }
    }
    "with a DELETE on /<uuid>" >> {
      "and an invalid UUID" >> {
        "should return a 404/NOT FOUND" >> {
          val service = new RestSystemService
          Delete("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            status must_== StatusCodes.NotFound
          }
        }
        "should return content type 'application/json'" >> {
          val service = new RestSystemService
          Delete("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            contentType must_== ContentTypes.`application/json`
          }
        }
        "should return a JSON object with a message" >> {
          val service = new RestSystemService
          Delete("/00000000-0000-0000-0000-000000000000") ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"message\"\\s*:\\s*\".*\".*\\}"
          }
        }
      }
      "and a valid UUID" >> {
        "should return a 202/ACCEPTED" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Delete("/" + responseUuid) ~> service.route ~> check {
            status must_== StatusCodes.Accepted
          }
        }
        "should return content type 'application/json'" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Delete("/" + responseUuid) ~> service.route ~> check {
            contentType must_== ContentTypes.`application/json`
          }
        }
        "should return a JSON object with a message" >> {
          val uuidPattern = ("\\{\\s*\"uuid\"\\s*:" +
            "\\s*\"(\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})\".*\\}").r
          val service = new RestSystemService
          var responseUuid: String = ""
          Post() ~> service.route ~> check {
            responseAs[String] match {
              case uuidPattern(uuid) => responseUuid = uuid
              case _ => failure
            }
          }
          Delete("/" + responseUuid) ~> service.route ~> check {
            responseAs[String] must be matching "\\{\\s*\"message\"\\s*:\\s*\".*\".*\\}"
          }
        }
      }
    }
  }

}
