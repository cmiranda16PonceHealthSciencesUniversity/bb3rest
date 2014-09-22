package org.biobank.rest

import org.biobank.rest.domain._

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.io.IO

import spray.routing.RequestContext
import spray.httpx.SprayJsonSupport

import scala.util.{ Success, Failure }

object PatientService {
  case class StudySpecimens(pnumber: String)
  case class SpecimenCounts(pnumber: String)
}

class PatientService(requestContext: RequestContext) extends Actor {
  import system.dispatcher
  import PatientService._
  import StudySpecimensJsonProtocol._
  import SpecimenCountsJsonProtocol._
  import SprayJsonSupport._

  implicit val system = context.system
  val log = Logging(system, getClass)

  def receive = {
    case StudySpecimens(pnumber) => {
      studySpecimens(pnumber)
      context.stop(self)
    }

    case SpecimenCounts(pnumber) => {
      specimenCounts(pnumber)
      context.stop(self)
    }
  }

  def studySpecimens(pnumber: String) = {
    log.info(s"request for study specimens: $pnumber")
    requestContext.complete(PatientRepository.studySpecimens(pnumber))
  }

  def specimenCounts(pnumber: String) = {
    log.info(s"request for specimen counts: $pnumber")
    requestContext.complete(PatientRepository.specimenCounts(pnumber))
  }

}

