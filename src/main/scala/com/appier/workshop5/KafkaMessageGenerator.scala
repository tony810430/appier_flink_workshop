package com.appier.workshop5

import java.util.Properties

import com.appier.utils.CheckpointedSimpleSourceFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.kafka.clients.producer.ProducerConfig

object KafkaMessageGenerator {
  val outputTopic = "input_topic"
  val bootstrapServers = "kafka:9092"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setLatencyTrackingInterval(-1L)
    env.setStateBackend(new MemoryStateBackend().asInstanceOf[StateBackend])
    env.enableCheckpointing(30 * 1000L)
    buildGenerator(env)
    env.execute()
  }

  def buildGenerator(env: StreamExecutionEnvironment): Unit = {
    val simpleSourceFunction = new CheckpointedSimpleSourceFunction

    val properties = new Properties()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

    val flinkKafkaProducer011 = new FlinkKafkaProducer011[String](
      outputTopic,
      new KeyedSerializationSchemaWrapper(new SimpleStringSchema()),
      properties,
      FlinkKafkaProducer011.Semantic.EXACTLY_ONCE
    )

    env.addSource(simpleSourceFunction).setParallelism(1).name("source_a").uid("source_a").disableChaining()
      .addSink(flinkKafkaProducer011).name("sink_a").uid("sink_a")
  }
}
