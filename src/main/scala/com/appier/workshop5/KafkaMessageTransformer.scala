package com.appier.workshop5

import java.lang
import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.kafka.common.requests.IsolationLevel

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
    consumerProperties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED)

    val flinkKafkaConsumer = new FlinkKafkaConsumer[String](
      inputTopic,
      new SimpleStringSchema(),
      consumerProperties
    )

    val producerProperties = new Properties()
    producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    producerProperties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "60000")

    val flinkKafkaProducer = new FlinkKafkaProducer[String](
      outputTopic,
      new KafkaSerializationSchema[String]() {
        override def serialize(element: String, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
          new ProducerRecord(outputTopic, element.getBytes())
        }
      },
      producerProperties,
      FlinkKafkaProducer.Semantic.EXACTLY_ONCE
    )

    env.addSource(flinkKafkaConsumer).name("source_b").uid("source_b").disableChaining()
      .map(x => s"!!! $x !!!").name("map_b").uid("map_b").disableChaining()
      .addSink(flinkKafkaProducer).name("sink_b").uid("sink_b")
  }

}
