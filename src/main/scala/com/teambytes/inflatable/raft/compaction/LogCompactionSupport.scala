package com.teambytes.inflatable.raft.compaction

import com.teambytes.inflatable.raft.model.{RaftSnapshot, ReplicatedLog}
import akka.actor.Extension

/**
 * Log Compaction API
 * Thought to be useful for implementing via different snapshot stores (see akka-persistence).
 */
// todo rethink if this design makes sense
// todo implement the default log compacter as akka-persistence snapshot store user
private[inflatable] trait LogCompactionSupport extends Extension {

  /**
   * Applies the compaction to the given [[com.teambytes.inflatable.raft.model.ReplicatedLog]].
   * The log's entries up until `meta.lastIncludedIndex` will be replaced with an [[com.teambytes.inflatable.raft.model.SnapshotEntry]].
   *
   * @return the compacted log, guaranteed to not change any items after the snapshot term/index, entries previous to the snapshot will be dropped.
   */
   def compact[Command](replicatedLog: ReplicatedLog[Command], snapshot: RaftSnapshot): ReplicatedLog[Command]

 }
