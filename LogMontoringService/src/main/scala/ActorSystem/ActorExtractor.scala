package ActorSystem

import HelperUtils.CreateLogger
import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.io.File
import java.nio.file.Files
import java.util.Properties
import scala.io.Source

class ActorExtractor extends Actor {

  // To print log messages in console
  val log = CreateLogger(classOf[ActorExtractor])

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("application.conf")
  // To read the last line to be omitted
  var lastReadLines: scala.collection.mutable.Map[String, Int] = scala.collection.mutable.Map[String, Int]()
  var lineCounter = 0
  log.info("Add property values for kafka producers")
  // Create a poperty to be sent to kafka producer
  val props: Properties = new Properties()
  log.info("set kafka server")
  // Set boostrap servers taken from AWS MSK cluster as bootsreap server
  props.put("bootstrap.servers", config.getString("config.KafkaServer"))
  // Set key and value serializer
  props.put("key.serializer", config.getString("config.keySerializer"))
  props.put("value.serializer", config.getString("config.valueSerializer"))
  // Set acks as all
  props.put("acks", config.getString("config.acks"))
  // Add the SSL security protocol and truststore file location
  props.put("security.protocol", config.getString("config.securityProtocol"))
  props.put("ssl.truststore.location", config.getString("config.sslTruststoreLocation"))
  val producer = new KafkaProducer[String, String](props)

  //   Give the list of kafkaTopicNames here


  // To receive the file
  override def receive: Receive = {
    case file: File =>
      // Check whether last read line contains the file name
      if (!lastReadLines.contains(file.getName)) lastReadLines += (file.getName -> 0)
      // Get the buffered source from the file
      val BufferedSource = Source.fromFile(file)
      // Read the line from the file
      val data = Files.lines(file.toPath)
      data.skip(lastReadLines(file.getName)).forEach(SendTokafka(_))
      lastReadLines(file.getName) = BufferedSource.getLines.size
  }

  // To send data from producer to consumer
  def SendTokafka(data: String): Unit = {
    // Split the data to check whether the message contains ERROR message
    val message = data.split("\\s+")
    val kafkaTopicName = config.getString("config.topic")
    // Send the data to consumer in Spark if the log contains error message .
    if (message(2) == config.getString("config.regEx")) {
      producer.send(new ProducerRecord[String, String](kafkaTopicName, "kafkaProducer", data))
    }
  }
}
