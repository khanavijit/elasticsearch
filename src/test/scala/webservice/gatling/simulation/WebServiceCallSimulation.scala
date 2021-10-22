package webservice.gatling.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.language.postfixOps
import scala.concurrent.duration._


import java.util.Locale

class WebServiceCallSimulation extends Simulation {

  val rampUpTimeSecs = 5
  val testTimeSecs = 20
  val noOfUsers = 10
  val minWaitMs = 50 milliseconds
  val maxWaitMs = 500 milliseconds

  val baseURL = "http://localhost:8080"
  val baseName = "/v1"
  val requestName1 = baseName + "/searchById"
  val requestName2 = baseName + "/searchBySubject"
  val requestName3 = baseName + "/searchByPriority"
  val requestName4 = baseName + "/searchByDescription"



  val scenarioName1 = baseName + "-searchById"
  val scenarioName2 = baseName + "-searchBySubject"
  val scenarioName3 = baseName + "-searchByPriority"
  val scenarioName4 = baseName + "-searchByDescription"

  val URI1 = "/v1/search?attribute=id&value=67"
  val URI2 = "/v1/search?attribute=subject&value=missing"
  val URI3 = "/v1/search?attribute=priority&value=high"
  val URI4 = "/v1/search?attribute=description&value=Glory"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario(scenarioName1)
    .during(testTimeSecs) {
      exec(
        http(requestName1)
          .get(URI1)
          .check(status.is(200))
      ).pause(minWaitMs, maxWaitMs)
    }

  val scn2 = scenario(scenarioName2)
    .during(testTimeSecs) {
      exec(
        http(requestName2)
          .get(URI2)
          .check(status.is(200))
      ).pause(minWaitMs, maxWaitMs)
    }
  val scn3 = scenario(scenarioName3)
    .during(testTimeSecs) {
      exec(
        http(requestName3)
          .get(URI3)
          .check(status.is(200))
      ).pause(minWaitMs, maxWaitMs)
    }
  val scn4 = scenario(scenarioName4)
    .during(testTimeSecs) {
      exec(
        http(requestName4)
          .get(URI4)
          .check(status.is(200))
      ).pause(minWaitMs, maxWaitMs)
    }

  setUp(
    scn.inject(rampUsers(noOfUsers) during (rampUpTimeSecs)) ,
    scn2.inject(rampUsers(noOfUsers) during (rampUpTimeSecs)),
    scn3.inject(rampUsers(noOfUsers) during (rampUpTimeSecs)),
    scn4.inject(rampUsers(noOfUsers) during (rampUpTimeSecs))
  ).protocols(httpConf)
}