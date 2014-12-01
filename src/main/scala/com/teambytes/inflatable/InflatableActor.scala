package com.teambytes.inflatable

import com.teambytes.inflatable.raft.RaftActor

private[inflatable] class InflatableActor(handler: InflatableLeader) extends RaftActor {
  import protocol._

  private[inflatable] override type Command = Cmnd

  private[inflatable] override def apply = {
    case _ =>
      // Do nothing, we only care about leader election
  }

  override def onIsLeader(): Unit = handler.onIsLeader()

  override def onIsNotLeader(): Unit = handler.onIsNotLeader()
}
