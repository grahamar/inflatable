package com.teambytes.inflatable

import akka.actor.ActorLogging
import com.teambytes.inflatable.raft.RaftActor

private[inflatable] case object JoinCluster

private[inflatable] class InflatableActor(handler: InflatableLeader) extends RaftActor with ActorLogging {
  import com.teambytes.inflatable.protocol._

  private[inflatable] override type Command = Cmnd

  private[inflatable] override def apply = {
    case _ => // Do nothing, we only care about leader election
  }

  override def onIsLeader() = handler.onIsLeader()

  override def onIsNotLeader() = handler.onIsNotLeader()

}
