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

package repositories

import javax.inject.{Inject, Singleton}
import models.BusinessPartnerModel
import play.modules.reactivemongo.MongoDbConnection
import reactivemongo.api.commands._
import uk.gov.hmrc.domain.Nino

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BusinessPartnerRepository @Inject()() extends MongoDbConnection {

  lazy val repository = new BusinessPartnerRepositoryBase {

    def findAllVersionsBy(o: Nino)(implicit ec: ExecutionContext): Future[Map[Nino, List[BusinessPartnerModel]]] = {
      find("nino" -> o.nino).map { allBP =>
        allBP.groupBy(_.nino)
      }
    }

    def findLatestVersionBy(o: Nino)(implicit ec: ExecutionContext): Future[List[BusinessPartnerModel]] = {
      findAllVersionsBy(o).map {
        _.values.toList.map {
          _.head
        }
      }
    }

    def removeBy(o: Nino)(implicit ec: ExecutionContext): Future[Unit] = {
      remove("nino" -> o.nino).map { _ => }
    }

    def removeAll()(implicit ec: ExecutionContext): Future[Unit] = {
      removeAll(WriteConcern.Acknowledged).map { _ => }
    }

    def addEntry(t: BusinessPartnerModel)(implicit ec: ExecutionContext): Future[Unit] = {
      insert(t).map { _ => }
    }

    def addEntries(entries: Seq[BusinessPartnerModel])(implicit ec: ExecutionContext): Future[Unit] = {
      entries.foreach {
        addEntry
      }
      Future.successful({})
    }
  }

  def apply(): CgtRepository[BusinessPartnerModel, Nino] = repository

}
