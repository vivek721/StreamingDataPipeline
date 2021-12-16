package Utils

import Configuration.SparkConfig
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model._
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger

import java.io.IOException
import scala.collection.JavaConverters._


object AwsEmailService {

  // To print log messages in console
  val log = Logger.getLogger(classOf[SparkConfig])

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("application.conf")

  log.info("Set the required parameters for AWS email service")
  // The email address verified in AWS account
  val DefaultSourceEmailAddress:String = config.getString("sparkStreaming.sourceAddress")
  // To email address
  val targetAddressList: List[String] = List(config.getString("sparkStreaming.targetAddressList"))
  // The subject line for the email.
  val subject = config.getString("sparkStreaming.subject")
  log.info("Subject body added to Body")
  // destination email address
  val destination:Destination = new Destination(targetAddressList.asJava)
  log.info("Target address list added to destination")

  /**
   * A handle on SES with credentials fetched from the environment variables
   *
   *     AWS_ACCESS_KEY_ID
   *     AWS_SECRET_KEY
   */
//  protected lazy val simpleEmailService:AmazonSimpleEmailServiceClient =
//    new AmazonSimpleEmailServiceClient(new EnvironmentVariableCredentialsProvider());



  @throws[IOException]
  def emailService(body: String): Unit = {
    try {
      // The body for the email.
      val messageBody: Body = new Body(new Content(body))
      val message:Message = {
        new Message(new Content(subject), messageBody)
      }
      // Build a client with amazon simple email service client builder
      val client = AmazonSimpleEmailServiceClientBuilder.standard.withRegion(Regions.US_EAST_1).build()
      // Create a send email request
      val request = new SendEmailRequest(DefaultSourceEmailAddress, destination, message)
      log.info("Send email")
      // send email with client
      client.sendEmail(request)
      // Shutdown the client
      client.shutdown()
      log.info("Sending Email to the client")
    } catch {
      case ex: Exception =>
        log.error("Sending Email failed")
    }
  }
}