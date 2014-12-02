package com.teambytes.inflatable

import akka.actor.ActorLogging
import com.teambytes.inflatable.raft.RaftActor
import org.slf4j.LoggerFactory

private[inflatable] class InflatableActor(handler: InflatableLeader) extends RaftActor with ActorLogging {
  import protocol._

  private val logger = LoggerFactory.getLogger(getClass)

  private[inflatable] override type Command = Cmnd

  private[inflatable] override def apply = {
    case _ =>
      // Do nothing, we only care about leader election
    logger.info("apply")
  }

  override def onIsLeader() = {
    logger.info("onIsLeader")
    handler.onIsLeader()
  }

  override def onIsNotLeader() = {
    logger.info("onIsNotLeader")
    handler.onIsNotLeader()
  }
}
