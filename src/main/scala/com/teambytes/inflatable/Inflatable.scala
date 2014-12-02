package com.teambytes.inflatable

import akka.actor.{ActorSystem, Props}
import com.teambytes.inflatable.raft.ClusterConfiguration
import com.teambytes.inflatable.raft.protocol._
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

object Inflatable {

  def startLeaderElection(handler: InflatableLeader)(implicit ec: ExecutionContext): Unit =
    new Inflatable(handler, AkkaConfig.apply())(ec)

  def startLeaderElection(handler: InflatableLeader, defaults: Config)(implicit ec: ExecutionContext): Unit =
    new Inflatable(handler, AkkaConfig(defaults))(ec)

}

class Inflatable(handler: InflatableLeader, akkaConfig: AkkaConfig)(implicit ec: ExecutionContext) {

  private val logger = LoggerFactory.getLogger(classOf[Inflatable])

  logger.info("Inflating raft system...")
  logger.info(s"Seeds: ${akkaConfig.seeds}")

  private val clusterSystem = ActorSystem("inflatable-raft", akkaConfig.config)

  val members = (0 to akkaConfig.seeds.size + 1).map { i =>
    clusterSystem.actorOf(
      Props(
        classOf[InflatableActor],
        handler
      ),
      name = s"raft-member-$i")
  }

  logger.info("Inflatable raft system fully inflated!")

  val clusterConfiguration = ClusterConfiguration(akkaConfig.singleNodeCluster, members: _*)

  members foreach { _ ! ChangeConfiguration(clusterConfiguration) }

}