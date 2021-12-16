package Configuration

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import java.util.UUID

/**
 *  topics is a list of kafka topics to consume from
*   brokers is a list of Kafka brokers
 *   **/
trait KafkaConfig extends SparkConfig {

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("application.conf")

  // Give the list of kafkaTopicNames here
  val kafkaTopicName = config.getString("sparkStreaming.kafkaTopicName")
//  val kafkaTopicNameList = kafkaTopicName.split(",").toSet
  log.info("Creating kafka consumer config")
  // Set the kafka consumer configuration properties
  val kafkaConsumerConfig: Map[String, String] = Map(
    // Set the bootstrap server config address
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> config.getString("sparkStreaming.kafkaServer"),
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer].getName,
    // Set the group ID config
    ConsumerConfig.GROUP_ID_CONFIG -> UUID.randomUUID().toString,
    // Set the auto offset reset config value
//    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> config.getString("sparkStreaming.KafkaConsumerOffset"),
    // Set the auto commit enable ot disable value
//    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> config.getString("sparkStreaming.KafkaConsumerAutoCommit"),
    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG -> config.getString("sparkStreaming.securityProtocol"),
    "ssl.truststore.location" -> config.getString("sparkStreaming.sslTruststoreLocation")

  )

  log.info("Creating kafka Consumer Stream")
  // Pass the streamingContext from spark config to D stream along with consumer config properties and kafka topics
  val kafkaConsumerStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
    streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Set(kafkaTopicName), kafkaConsumerConfig)
  )

}
