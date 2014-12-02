package com.teambytes.inflatable

import akka.actor.{ActorSystem, Props}
import com.teambytes.inflatable.raft.ClusterConfiguration
import com.teambytes.inflatable.raft.cluster.ClusterRaftActor
import com.teambytes.inflatable.raft.protocol._
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object Inflatable {

  def startLeaderElection(handler: InflatableLeader): Unit = new Inflatable(handler, AkkaConfig.apply())

  def startLeaderElection(handler: InflatableLeader, defaults: Config): Unit = new Inflatable(handler, AkkaConfig(defaults))

}

class Inflatable(handler: InflatableLeader, akkaConfig: AkkaConfig) {

  import scala.concurrent.duration._

  private val logger = LoggerFactory.getLogger(classOf[Inflatable])

  logger.info("Inflating raft system...")

  private val clusterSystem = ActorSystem("inflatable-raft", akkaConfig.config)

  private val inflatableActor = clusterSystem.actorOf(Props(classOf[InflatableActor], handler), name = "inflatable-raft-actor")

  logger.info(s"Waiting in Init state with ${akkaConfig.seeds.size} members.")

  private val inflatableCluster = clusterSystem.actorOf(
    Props(
      classOf[ClusterRaftActor],
      inflatableActor,
      akkaConfig.seeds.size
    ),
    name = "inflatable-raft-cluster-actor"
  )

  private val memberFutures = Future.sequence(akkaConfig.seeds.map(clusterSystem.actorSelection).map(_.resolveOne(20.seconds)))
  memberFutures.map { members =>
    logger.info("Inflatable raft system fully inflated!")

    val clusterConfiguration = ClusterConfiguration(members: _*)

    inflatableCluster ! ChangeConfiguration(clusterConfiguration)
  }

}