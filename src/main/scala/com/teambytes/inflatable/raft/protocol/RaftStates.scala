package com.teambytes.inflatable.raft.protocol

/**
 * States used by the Raft FSM.
 *
 * Use by importing the protocol package:
 * {{{import akka.raft.protocol._}}}
 */
private[inflatable] trait RaftStates {

  sealed trait RaftState

  /** In this phase the member awaits to get it's [[com.teambytes.inflatable.raft.ClusterConfiguration]] */
  case object Init      extends RaftState

  /** A Follower can take writes from a Leader; If doesn't get any heartbeat, may decide to become a Candidate */
  case object Follower  extends RaftState

  /** A Candidate tries to become a Leader, by issuing [[com.teambytes.inflatable.raft.protocol.RaftProtocol.RequestVote]] */
  case object Candidate extends RaftState

  /** The Leader is responsible for taking writes, and commiting entries, as well as keeping the heartbeat to all members */
  case object Leader    extends RaftState
}
