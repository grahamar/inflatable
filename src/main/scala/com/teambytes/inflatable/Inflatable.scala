package com.teambytes.inflatable

import akka.actor.{ActorSystem, Props}
import com.teambytes.inflatable.raft.cluster.ClusterRaftActor
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

object Inflatable {

  def startLeaderElection(handler: InflatableLeader): Unit = new Inflatable(handler, AkkaConfig.apply())

  def startLeaderElection(handler: InflatableLeader, defaults: Config): Unit = new Inflatable(handler, AkkaConfig(defaults))

}

class Inflatable(handler: InflatableLeader, akkaConfig: AkkaConfig) {

  private val logger = LoggerFactory.getLogger(classOf[Inflatable])

  logger.info("Inflating raft system...")

  private val clusterSystem = ActorSystem("inflatable-raft-cluster", akkaConfig.config)

  private val inflatableActor = clusterSystem.actorOf(Props(classOf[InflatableActor], handler), name = "inflatable-raft-actor")

  clusterSystem.actorOf(
    Props(
      classOf[ClusterRaftActor],
      inflatableActor,
      akkaConfig.seeds.size
    ),
    name = "inflatable-raft-cluster-actor"
  )

  logger.info("Inflatable raft system fully inflated!")

}