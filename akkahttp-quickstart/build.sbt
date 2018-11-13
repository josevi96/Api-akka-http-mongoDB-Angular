name := "akkahttp-quickstart"

version := "0.1"

scalaVersion := "2.12.6"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.17",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.17" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.17",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.17" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5" % Test,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.reactivemongo" %% "reactivemongo" % "0.16.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",
  "org.slf4j" % "slf4j-simple" % "1.6.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
