import sbtrelease.ReleasePlugin.ReleaseKeys._

name := "inflatable"

organization := "net.redhogs.inflatable"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "com.typesafe.akka" %% "akka-actor" % "2.3.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.7",
  "com.amazonaws" % "aws-java-sdk" % "1.9.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

licenses := Seq("Apache License 2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("https://github.com/grahamar/inflatable"))

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>git@github.com:grahamar/inflatable.git</url>
    <connection>scm:git:git@github.com:grahamar/inflatable.git</connection>
  </scm>
    <developers>
      <developer>
        <id>grhodes</id>
        <name>Graham Rhodes</name>
        <url>https://github.com/grahamar</url>
      </developer>
      <developer>
        <id>ktoso</id>
        <name>Konrad Malawski</name>
        <url>https://github.com/ktoso</url>
      </developer>
      <developer>
        <id>chrisloy</id>
        <name>Chris Loy</name>
        <url>https://github.com/chrisloy</url>
      </developer>
    </developers>)

releaseSettings