package com.appier.utils

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

class DummySourceFunction extends SourceFunction[String]() {
  override def run(ctx: SourceContext[String]): Unit = {
    while (true) {}
  }

  override def cancel(): Unit = {}
}
