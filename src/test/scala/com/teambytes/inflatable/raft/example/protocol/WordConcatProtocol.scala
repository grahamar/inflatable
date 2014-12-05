package com.teambytes.inflatable.raft.example.protocol

trait WordConcatProtocol {
  sealed trait Cmnd
  case class AppendWord(word: String) extends Cmnd
  case object GetWords                extends Cmnd
}
