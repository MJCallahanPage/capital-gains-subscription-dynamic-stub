/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.play.microservice.controller.BaseController
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.HashMap
import play.api.mvc.Results.Ok
import scala.concurrent.duration._
import staticTestData.StaticStore._

import scala.concurrent.{Future, Promise}

object StubController extends StubController {
}

trait StubController extends BaseController {

  def readBP(nino: String) = Action.async { implicit request =>
   val bp = ninoBpLinkings.getOrElse(nino, -1)
    /*if(bp == -1) {
      val x = for {
        bpx <- generateRandomBp(nino)
      } yield bp
      Future.successful(Ok(x.toString))
    }*/
      Future.successful(Ok(bp.toString))
  }

  /*def generateRandomBp(nino:String): Future[Int] = {

    def prevEntryExists(nino:): Unit ={

    }

    def generate(generated: Int): Int ={
     generated match {
       case -1 => generate(scala.util.Random.nextInt(10000))
       case ninoBpLinkings.exists(_ == (`nino`-> generated)) =>
     }
    }
    Future.successful(generate(-1))
  }*/


}
