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

import scala.collection.mutable.HashMap
import play.api.mvc.Results.Ok
import staticTestData.StaticStore._

import scala.concurrent.Future

object StubController extends StubController {
}

trait StubController extends BaseController {

  def readBP(nino: String) = Action.async { implicit request =>
   val bp = ninoBpLinkings.getOrElse(nino, -1)
    if(bp == -1) {
      val newBp = generateRandomBp(nino)
      Future.successful(Ok(newBp.toString))
    }
    Future.successful(Ok(bp.toString))
  }

  def generateRandomBp(nino:String): Future[Int] = {
    def generate(generated: Int): Int ={
      if(generated == -1){
        //edge case: first instance
        generate(scala.util.Random.nextInt())
      }
      if(!ninoBpLinkings.exists(_ == (nino -> generated))){
        //if generated BP not associated with a nino return
        ninoBpLinkings.put(nino, generated)
        //place as new entry
        generated
        //return
      }
      else{
        //otherwise generate a new BP
        generate(scala.util.Random.nextInt())
      }
    }
    Future.successful(generate(-1))
  }


}
