package Utils

import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

case class WordCount(word: String, count: Int)

// To get the total word count from the line
object WordCount {

  // To print log messages in console
  val log = Logger.getLogger(classOf[WordCount])

  def count(sc: SparkContext, lines: RDD[String]): RDD[WordCount] = {

    // Map the lines and reduce by key to get the total word count
    val wordCounts = lines.map(word => (word, 1)).reduceByKey(_ + _).map {
      case (word: String, count: Int) => WordCount(word, count)
    }

    log.info(s"Word count: wordcounts")

    // Sort the words
    val sortedWordCount = wordCounts.sortBy(_.word)

    // Return sorted word count
    sortedWordCount
  }

}