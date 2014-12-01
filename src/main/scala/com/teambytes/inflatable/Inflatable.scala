package com.teambytes.inflatable

import akka.actor.{Props, ActorSystem}
import com.teambytes.inflatable.raft.cluster.ClusterRaftActor
import org.slf4j.LoggerFactory

object Inflatable {

  def startLeaderElection(handler: InflatableLeader): Unit = new Inflatable(handler)

}

class Inflatable(handler: InflatableLeader) {

  private val logger = LoggerFactory.getLogger(classOf[Inflatable])
  private val clusterSystem = ActorSystem("inflatable-raft-cluster", AkkaConfig.config)

  private val inflatableActor = clusterSystem.actorOf(Props(classOf[InflatableActor], handler), name = "inflatable-raft-actor")

  clusterSystem.actorOf(
    Props(
      classOf[ClusterRaftActor],
      inflatableActor,
      AkkaConfig.seeds.size
    ),
    name = "inflatable-raft-cluster-actor"
  )

  logger.info("Started inflatable raft system.")

}