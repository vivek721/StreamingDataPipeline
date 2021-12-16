package Configuration

import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

trait SparkConfig {

  // To print log messages in console
  val log = Logger.getLogger(classOf[SparkConfig])

  // create a spark conf without spark session
//  val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")

  log.info("Creating Spark session")
//  val spark: SparkSession = SparkSession
//    .builder()
//    .appName("SparkStreaming") // appName parameter is a name for the application to show on the cluster UI
//    .master("local[*]") // master can be Spark Mesos or YARN cluster URL or local for local mode . * represents no. of threads
//    .getOrCreate() // get if available already or create a new one
  val conf = new SparkConf().setAppName("sparkstream").setMaster("local[*]")

  log.info("Creating Streaming Context with 10 seconds interval")
  //  Only one SparkContext should be active per JVM. Stop the active SparkContext before creating a new one.
  val sparkContext: SparkContext = new SparkContext(conf)
  // Create a StreamingContext with working thread and batch interval of 10 seconds.
  val streamingContext: StreamingContext = new StreamingContext(sparkContext, Seconds(1))
  // Set Log level to error
  streamingContext.sparkContext.setLogLevel("ERROR")
}
