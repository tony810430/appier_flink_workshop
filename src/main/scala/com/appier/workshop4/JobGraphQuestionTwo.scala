package com.appier.workshop4

import com.appier.base.JobGraphQuestionBase
import com.appier.utils.DummySourceFunction
import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector

object JobGraphQuestionTwo extends JobGraphQuestionBase {
  override def buildJobGraph(env: StreamExecutionEnvironment): Unit = {
    val partitioner = new Partitioner[String]() {
      override def partition(key: String, numPartitions: Int): Int = (math.random * numPartitions).toInt
    }

    val processFunction: ProcessFunction[String, String] = new ProcessFunction[String, String]() {
      val stateDescriptor = new ValueStateDescriptor("state", BasicTypeInfo.STRING_TYPE_INFO)

      override def processElement(value: String, ctx: ProcessFunction[String, String]#Context, out: Collector[String]): Unit = {
        val state = getRuntimeContext.getState(stateDescriptor)
        state.update(value)
      }
    }

    env.addSource(new DummySourceFunction)
      .keyBy(x => x)
      .partitionCustom(partitioner, x => x)
      .process(processFunction)
  }
}
