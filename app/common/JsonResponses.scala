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

package common

import play.api.libs.json.{JsObject, Json}

object JsonResponses {

  def desRegisterResponse(sap: String): JsObject = {
    Json.obj(
      "safeId" -> sap,
      "isEditable" -> true,
      "isAnAgent" -> false,
      "isAnASAgent" -> false,
      "isAnIndividual" -> true,
      "individual" -> Json.obj(
        "firstName" -> "Stephen",
        "lastName" -> "Wood",
        "dateOfBirth" -> "1990-04-03"
      ),
      "address" -> Json.obj(
        "addressLine1" -> "100 SuttonStreet",
        "addressLine2" -> "Wokingham",
        "addressLine3" -> "Surrey",
        "addressLine4" -> "London",
        "postalCode" -> "DH14EJ",
        "countryCode" -> "GB"
      ),
      "contactDetails" -> Json.obj(
        "primaryPhoneNumber" -> "01332752856",
        "secondaryPhoneNumber" -> "07782565326",
        "faxNumber" -> "01332754256",
        "emailAddress" -> "stephen@manncorpone.co.uk"
      )
    )
  }
}
