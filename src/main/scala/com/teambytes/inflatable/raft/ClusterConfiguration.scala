package com.teambytes.inflatable.raft

import akka.actor.ActorRef

private[inflatable] sealed trait ClusterConfiguration {
  def members: Set[ActorRef]

  def sequenceNumber: Long

  def isOlderThan(that: ClusterConfiguration) = this.sequenceNumber <= that.sequenceNumber

  def isNewerThan(that: ClusterConfiguration) = this.sequenceNumber > that.sequenceNumber

  def isTransitioning: Boolean

  def singleNodeCluster: Boolean

  def transitionTo(newConfiguration: ClusterConfiguration): ClusterConfiguration

  /**
   * Basically "drop" ''old configuration'' and keep using only the new one.
   *
   * {{{
   *   StableConfiguration                   => StableConfiguration
   *   JointConsensusConfuguration(old, new) => StableConfiguration(new)
   * }}}
   */
  def transitionToStable: ClusterConfiguration

  /** When in the middle of a configuration migration we may need to know if we're part of the new config (in order to step down if not) */
  def isPartOfNewConfiguration(member: ActorRef): Boolean
}

private[inflatable] object ClusterConfiguration {
  def apply(isLocal: Boolean, members: Iterable[ActorRef]): ClusterConfiguration =
    StableClusterConfiguration(0, members.toSet, isLocal)

  def apply(isLocal: Boolean, members: ActorRef*): ClusterConfiguration =
    StableClusterConfiguration(0, members.toSet, isLocal)
}

/**
 * Used for times when the cluster is NOT undergoing membership changes.
 * Use `transitionTo` in order to enter a [[com.teambytes.inflatable.raft.JointConsensusClusterConfiguration]] state.
 */
private[inflatable] case class StableClusterConfiguration(sequenceNumber: Long, members: Set[ActorRef], singleNodeCluster: Boolean) extends ClusterConfiguration {
  val isTransitioning = false

  /**
   * Implementation detail: The resulting configurations `sequenceNumber` will be equal to the current one.
   */
  def transitionTo(newConfiguration: ClusterConfiguration): JointConsensusClusterConfiguration =
    JointConsensusClusterConfiguration(sequenceNumber, members, newConfiguration.members, singleNodeCluster)

  def isPartOfNewConfiguration(ref: ActorRef) = members contains ref

  def transitionToStable = this

  override def toString = s"StableRaftConfiguration(${members.map(_.path.elements.last)})"

}

/**
 * Configuration during transition to new configuration consists of both old / new member sets.
 * As the configuration is applied, the old configuration may be discarded.
 *
 * During the transition phase:
 *
 *  - Log entries are replicated to all members in both configurations
 *  - Any member from either configuration may serve as Leader
 *  - Agreement (for elections and entry commitment) requires majoritis from ''both'' old and new configurations
 */
private[inflatable] case class JointConsensusClusterConfiguration(sequenceNumber: Long, oldMembers: Set[ActorRef], newMembers: Set[ActorRef], singleNodeCluster: Boolean) extends ClusterConfiguration {

  /** Members from both configurations participate in the joint consensus phase */
  val members = oldMembers union newMembers

  val isTransitioning = true

  /**
   * Implementation detail: The resulting stable configurations `sequenceNumber` will be incremented from the current one, to mark the following "stable phase".
   */
  def transitionTo(newConfiguration: ClusterConfiguration) =
    throw new IllegalStateException(s"Cannot start another configuration transition, already in progress! " +
      s"Migrating from [${oldMembers.size}] $oldMembers to [${newMembers.size}] $newMembers")

  /** When in the middle of a configuration migration we may need to know if we're part of the new config (in order to step down if not) */
  def isPartOfNewConfiguration(member: ActorRef): Boolean = newMembers contains member

  def transitionToStable = StableClusterConfiguration(sequenceNumber + 1, newMembers, singleNodeCluster)

  override def toString =
    s"JointConsensusRaftConfiguration(old:${oldMembers.map(_.path.elements.last)}, new:${newMembers.map(_.path.elements.last)})"
}
