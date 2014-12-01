name := "inflatable"

organization := "com.teambytes"

scalaVersion := "2.11.4"

version := "git describe --tags --dirty --always".!!.stripPrefix("v").trim

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "com.typesafe.akka" %% "akka-actor" % "2.3.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.7",
  "com.amazonaws" % "aws-java-sdk" % "1.9.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

