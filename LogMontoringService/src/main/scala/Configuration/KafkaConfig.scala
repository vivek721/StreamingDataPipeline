package Configuration

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.StringSerializer

trait KafkaConfig {

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("Application.conf")

  // Set the kafka producer configuration properties
  val kafkaProducerConfig: Map[String, String] = Map(
    // Set the bootstrap server config address
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> config.getString("KafkaServer"),
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer].getName,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer].getName,
    ProducerConfig.ACKS_CONFIG -> config.getString("acksConfig")
  )

    // Give the list of kafkaTopicNames here
    val kafkaTopicName = config.getString("kafkaTopicName")



}
