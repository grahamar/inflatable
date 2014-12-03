package com.teambytes.inflatable

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.teambytes.inflatable.raft.ClusterConfiguration
import com.teambytes.inflatable.raft.cluster.ClusterRaftActor
import com.teambytes.inflatable.raft.protocol._
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.concurrent.{Future, ExecutionContext}

object Inflatable {

  def startLeaderElection(handler: InflatableLeader)(implicit ec: ExecutionContext): Unit =
    new Inflatable(handler, AkkaConfig.apply())(ec)

  def startLeaderElection(handler: InflatableLeader, defaults: Config)(implicit ec: ExecutionContext): Unit =
    new Inflatable(handler, AkkaConfig(defaults))(ec)

}

class Inflatable(handler: InflatableLeader, akkaConfig: AkkaConfig)(implicit ec: ExecutionContext) {

  import scala.concurrent.duration._

  private val logger = LoggerFactory.getLogger(classOf[Inflatable])

  logger.info("Inflating raft system...")
  logger.info(s"Seeds: ${akkaConfig.seeds}")

  private val clusterSystem = ActorSystem("inflatable-raft", akkaConfig.config)

  private val inflatableActor = clusterSystem.actorOf(
    Props(
      classOf[InflatableActor],
      handler
    ),
    name = s"raft-member-0"
  )

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