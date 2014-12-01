package com.teambytes.inflatable

import akka.actor.{Props, ActorSystem}
import org.slf4j.LoggerFactory

object Inflatable {

  def startLeaderElection(handler: InflatableLeader): Unit = new Inflatable(handler)

}

class Inflatable(handler: InflatableLeader) {

  private lazy val logger = LoggerFactory.getLogger(classOf[Inflatable])
  private lazy val clusterSystem = ActorSystem("inflatable-raft-cluster", AkkaConfig.config)

  clusterSystem.actorOf(Props(classOf[InflatableActor], handler), name = "inflatable-raft-actor")

  logger.info("Started inflatable raft system.")

}