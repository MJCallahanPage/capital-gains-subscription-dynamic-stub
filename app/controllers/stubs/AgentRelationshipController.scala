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

package controllers.stubs

import javax.inject.{Inject, Singleton}

import actions.ExceptionTriggersActions
import common.RouteIds
import models.{AgentClientSubmissionModel, RelationshipModel}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Result}
import repositories.{AgentClientRelationshipRepository, DesAgentClientRelationshipRepository}
import uk.gov.hmrc.play.microservice.controller.BaseController
import utils.SchemaValidation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

@Singleton
class AgentRelationshipController @Inject()(repository: AgentClientRelationshipRepository,
                                            desRepository: DesAgentClientRelationshipRepository,
                                            guardedActions: ExceptionTriggersActions,
                                            schemaValidation: SchemaValidation) extends BaseController {

  val invalidJsonBodySub = Json.toJson("")

  def createAgentClientRelationship(arn: String): Action[AnyContent] = {
    Logger.info("Received request to make Agent-Client relationship on GG")
    guardedActions.AgentExceptionTriggers(RouteIds.createRelationship, arn).async {
      implicit request => {
        Try {
          val body = request.body.asJson.get
          val cgtRef = ((body \ "clientAllocation" \ "identifiers").head \ "value").as[String]
          val serviceName = (body \ "clientAllocation" \ "serviceName").as[String]

          val relationshipModel = RelationshipModel(arn, cgtRef)
          val modelToStore = AgentClientSubmissionModel(relationshipModel, serviceName)

          repository().addEntry(modelToStore)
        } match {
          case Success(_) => {
            Logger.info("Created Agent-Client relationship on GG")
            Future.successful(NoContent)
          }
          case Failure(e) => {
            Logger.warn("Bad Request made creating Agent-Client relationship on GG")
            Future.successful(BadRequest(s"${e.getMessage}"))
          }
        }
      }
    }
  }

  def createDesAgentClientRelationship: Action[AnyContent] = {
    Logger.info("Received request to make Agent-Client relationship on DES")
    guardedActions.DesAgentExceptionTriggers(RouteIds.createDesRelationship).async {
      implicit request => {

        val body = request.body.asJson
        val validJsonFlag = schemaValidation.validateJson(RouteIds.createDesRelationship, body.getOrElse(invalidJsonBodySub))

        def handleJsonValidity(flag: Boolean): Result = {
          if (flag) {
            val model = request.body.asJson.get.as[RelationshipModel]

            desRepository().addEntry(model)

            NoContent
          }
          else {
            BadRequest("JSON request body inconsistent with requirements of schema for DES Agent Client Relationship creation")
          }
        }

        validJsonFlag.map(x => handleJsonValidity(x))

        }
      }
    }
}
