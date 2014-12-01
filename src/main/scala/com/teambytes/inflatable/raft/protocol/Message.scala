package com.teambytes.inflatable.raft.protocol

/** Message with phantom type, used to differenciate between Internal and Raft messages */
private[inflatable] class Message[T <: MessageType]

private[inflatable] sealed trait MessageType
private[inflatable] class Raft      extends MessageType
private[raft] class Internal        extends MessageType
private[raft] class InternalCluster extends MessageType
private[raft] class Testing         extends Internal
