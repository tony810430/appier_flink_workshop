package com.appier.workshop5

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig

object KafkaMessageTransformer {
  val inputTopic = "input_topic"
  val outputTopic = "output_topic"
  val bootstrapServers = "kafka:9092"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setLatencyTrackingInterval(-1L)
    env.setStateBackend(new MemoryStateBackend().asInstanceOf[StateBackend])
    env.enableCheckpointing(30 * 1000L)
    buildGraph(env)
    env.execute()
  }

  def buildGraph(env: StreamExecutionEnvironment): Unit = {
    val consumerProperties = new Properties()
    consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "simple-consumer")
    consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val flinkKafkaConsumer011 = new FlinkKafkaConsumer011[String](
      inputTopic,
      new SimpleStringSchema(),
      consumerProperties
    )

    val producerProperties = new Properties()
    producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    producerProperties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "60000")

    val flinkKafkaProducer011 = new FlinkKafkaProducer011[String](
      outputTopic,
      new KeyedSerializationSchemaWrapper(new SimpleStringSchema()),
      producerProperties,
      FlinkKafkaProducer011.Semantic.EXACTLY_ONCE
    )

    env.addSource(flinkKafkaConsumer011).name("source_b").uid("source_b").disableChaining()
      .map(x => s"!!! $x !!!").name("map_b").uid("map_b").disableChaining()
      .addSink(flinkKafkaProducer011).name("sink_b").uid("sink_b")
  }

}
