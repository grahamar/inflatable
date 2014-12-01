package com.teambytes.inflatable.raft.model

private[inflatable] final case class Term(termNr: Long) extends AnyVal {
  def prev = this - 1
  def next = this + 1

  def -(n: Long): Term = Term(termNr - n)
  def +(n: Long): Term = Term(termNr + n)

  def >(otherTerm: Term): Boolean = this.termNr > otherTerm.termNr
  def <(otherTerm: Term): Boolean = this.termNr < otherTerm.termNr

  def >=(otherTerm: Term): Boolean = this.termNr >= otherTerm.termNr
  def <=(otherTerm: Term): Boolean = this.termNr <= otherTerm.termNr
}

private[inflatable] object Term {
  val Zero = Term(0)
}

