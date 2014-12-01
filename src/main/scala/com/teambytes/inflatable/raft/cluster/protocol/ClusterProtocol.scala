package com.teambytes.inflatable.raft.cluster.protocol

import com.teambytes.inflatable.raft.protocol._
import akka.actor.Address

private[cluster] trait ClusterProtocol {

  /**
   * Sent by ClusterRaftActor, to itself in order facilitate a retry to identify if unable to reach remote raft members
   */
  private[cluster] case class RaftMembersIdentifyTimedOut(address: Address, retryMoreTimes: Int)  extends Message[Internal] {
    require(retryMoreTimes >= 0, "Retry number must be positive!")

    def shouldRetry = retryMoreTimes > 0
    def forRetry = copy(retryMoreTimes = retryMoreTimes - 1)
  }
}
