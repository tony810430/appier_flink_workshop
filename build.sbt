name := "appier_flink_workshop"

version := "0.0.1-SNAPSHOT"

organization := "com.appier"

scalaVersion := "2.11.8"

val flinkVersion = "1.9.1"

libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion % "provided"
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided"
libraryDependencies += "org.apache.flink" %% "flink-streaming-java" % flinkVersion % "provided"
libraryDependencies += "org.apache.flink" % "flink-core" % flinkVersion % "provided"
libraryDependencies += "org.apache.flink" %% "flink-test-utils" % flinkVersion % Test
libraryDependencies += "org.apache.flink" %% "flink-runtime" % flinkVersion % Test classifier "tests"
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka-0.11" % flinkVersion

libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "org.rogach" %% "scallop" % "2.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test

fork := true
