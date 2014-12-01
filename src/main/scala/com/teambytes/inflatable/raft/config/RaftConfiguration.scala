package com.teambytes.inflatable.raft.config

import akka.actor.{ExtendedActorSystem, ExtensionIdProvider, ExtensionId}

private[inflatable] object RaftConfiguration extends ExtensionId[RaftConfig] with ExtensionIdProvider {

  def lookup() = RaftConfiguration

  def createExtension(system: ExtendedActorSystem): RaftConfig = new RaftConfig(system.settings.config)
}
