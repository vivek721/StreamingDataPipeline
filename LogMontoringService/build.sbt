name := "LogMontoringService"

version := "0.1"

scalaVersion := "2.13.7"

val typesafeConfigVersion = "1.4.1"
val AkkaVersion = "2.6.17"
val kafkaVersion = "2.6.2"
val sfl4sVersion = "2.0.0-alpha5"
val scalacticVersion = "3.2.9"

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % typesafeConfigVersion,
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "org.scalactic" %% "scalactic" % scalacticVersion,
  "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test
)

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}