package com.appier.workshop5

import java.lang
import java.util.Properties

import com.appier.utils.CheckpointedSimpleSourceFunction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.StateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}

object KafkaMessageGenerator {
  val outputTopic = "input_topic"
  val bootstrapServers = "kafka:9092"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setLatencyTrackingInterval(-1L)
    env.setStateBackend(new MemoryStateBackend().asInstanceOf[StateBackend])
    env.enableCheckpointing(3 * 1000L)
    buildGenerator(env)
    env.execute()
  }

  def buildGenerator(env: StreamExecutionEnvironment): Unit = {
    val simpleSourceFunction = new CheckpointedSimpleSourceFunction

    val properties = new Properties()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

    val flinkKafkaProducer011 = new FlinkKafkaProducer[String](
      outputTopic,
      new KafkaSerializationSchema[String]() {
        override def serialize(element: String, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
          new ProducerRecord(outputTopic, element.getBytes())
        }
      },
      properties,
      FlinkKafkaProducer.Semantic.EXACTLY_ONCE
    )

    env.addSource(simpleSourceFunction).setParallelism(1).name("source_a").uid("source_a").disableChaining()
      .addSink(flinkKafkaProducer011).name("sink_a").uid("sink_a")
  }
}
