package com.teambytes.inflatable.raft.example

import com.teambytes.inflatable.raft.RaftActor
import protocol._

class WordConcatRaftActor extends RaftActor {

  type Command = Cmnd

  var words = Vector[String]()

  /** Called when a command is determined by Raft to be safe to apply */
  def apply = {
    case AppendWord(word) =>
      words = words :+ word
      log.info(s"Applied command [AppendWord($word)], full words is: $words")

      word

    case GetWords =>
      log.info("Replying with {}", words.toList)
      words.toList
  }

  override def onIsLeader(): Unit = log.info("onIsLeader")

  override def onIsNotLeader(): Unit = log.info("onIsNotLeader")

}
