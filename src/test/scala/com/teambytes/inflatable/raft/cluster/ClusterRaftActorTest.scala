package com.teambytes.inflatable.raft.cluster

import com.teambytes.inflatable.raft.ClusterRaftSpec
import akka.actor.Kill

class ClusterRaftActorTest extends ClusterRaftSpec {

  def initialMembers: Int = 1

  behavior of "ClusterRaftActor"

  it should "stop when the watched raftActor dies" in {
    // given
    val member = members().head
    val clusterActor = system.actorOf(ClusterRaftActor.props(member, 1))

    probe watch clusterActor

    // when
    member ! Kill

    // then
    probe.expectTerminated(clusterActor)
  }
}
