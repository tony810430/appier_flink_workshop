package com.appier.utils

import java.util
import java.util.Collections

import org.apache.flink.api.scala.metrics.ScalaGauge
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Gauge
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

class CheckpointedSimpleSourceFunction extends RichParallelSourceFunction[String] with ListCheckpointed[java.lang.Long] {
  private var isCancel = false

  private var number: Long = 0L

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    getRuntimeContext.getMetricGroup.gauge[Long, Gauge[Long]]("number", ScalaGauge[Long](() => number))
  }

  override def run(ctx: SourceContext[String]): Unit = {
    while (!isCancel) {
      ctx.collect(s"message: $number")
      number += 1
      Thread.sleep(500)
    }
  }

  override def cancel(): Unit = {
    isCancel = true
  }

  override def snapshotState(checkpointId: Long, timestamp: Long): util.List[java.lang.Long] = Collections.singletonList(number)

  override def restoreState(state: util.List[java.lang.Long]): Unit = {
    if (!state.isEmpty) {
      this.number = state.get(0)
    }
  }
}
