package com.appier.workshop4

import com.appier.utils.DummySourceFunction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object JobGraphQuestionOne extends JobGraphQuestionBase {
  override def buildJobGraph(env: StreamExecutionEnvironment): Unit = {
    val windowDataStream = env
      .addSource(new DummySourceFunction)
      .keyBy(x => x)
      .timeWindow(Time.hours(1))

    windowDataStream.reduce(_ + _)
    windowDataStream.reduce((x, y) => y + x)
  }
}
