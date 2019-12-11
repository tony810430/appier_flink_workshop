package com.appier.workshop4

import com.appier.base.JobGraphQuestionBase
import com.appier.utils.DummySourceFunction
import org.apache.flink.api.common.functions.JoinFunction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object JobGraphQuestionThree extends JobGraphQuestionBase {
  override def buildJobGraph(env: StreamExecutionEnvironment): Unit = {
    val sourceOne = env.addSource(new DummySourceFunction)
    val sourceTwo = env.addSource(new DummySourceFunction)

    sourceOne.join(sourceTwo)
      .where(x => x)
      .equalTo(x => x)
      .window(TumblingEventTimeWindows.of(Time.hours(1)))
      .apply(new JoinFunction[String, String, String]() {
        override def join(first: String, second: String): String = first
      })
  }
}
