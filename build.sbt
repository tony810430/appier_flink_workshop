name := "appier_flink_workshop"

version := "0.0.1-SNAPSHOT"

organization := "com.appier"

scalaVersion := "2.11.8"

val flinkVersion = "1.7.1"

libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-streaming-java" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-test-utils" % flinkVersion % Test
libraryDependencies += "org.apache.flink" %% "flink-runtime" % flinkVersion

libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "org.rogach" %% "scallop" % "2.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test

fork := true
