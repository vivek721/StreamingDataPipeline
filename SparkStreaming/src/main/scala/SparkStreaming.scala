import Configuration.{KafkaConfig, SparkConfig}
import Utils.AwsEmailService.{emailService, log}
import org.apache.spark.streaming.kafka010._

// Consumes messages from kafka in the form of topics and does spark operations
object SparkStreaming extends App with SparkConfig with KafkaConfig {

  def init(): Unit = {
    // subscribe to topic

    // Get the lines from the kafka and split them into words
    val output = kafkaConsumerStream.map(_.value)

    log.info("Doing Spark Operations")
    // Put the output in loop on RDD operation and count the number of errors
    output.foreachRDD {
      rdd =>
//        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//        log.info(s"Ranges for batch: ${offsetRanges.mkString}")
        // Check if rdd count is greated than zero
        log.info("Reading spark stream data from kafka")
        if (rdd.count() > 0) {
          val body: String = config.getString("sparkStreaming.subjectBody") + rdd.collect().mkString(" ")
          log.info("body"+ body)
         // Send email with the customized body
          emailService(body)
        }
        }

//        var result: Array[String] = null
//        kafkaConsumerStream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)

  }

  override def main(args: Array[String]): Unit = {
    log.info("Initializing Spark Streaming")
    // initialize the kafka operation
    init()
    log.info("Starting streaming Context")
    // Start the streaming context
    streamingContext.start()
    // Await for termination
    streamingContext.awaitTermination()
    log.info("Streaming Context Terminated")
  }

}
