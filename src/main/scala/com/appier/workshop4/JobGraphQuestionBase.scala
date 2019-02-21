package com.appier.workshop4

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

trait JobGraphQuestionBase {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    buildJobGraph(env)
    env.execute()
  }

  def buildJobGraph(env: StreamExecutionEnvironment): Unit
}
