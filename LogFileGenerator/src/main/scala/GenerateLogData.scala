/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

import Generation.{LogMsgSimulator, RandomStringGenerator}
import HelperUtils.{CreateLogger, ObtainConfigReference, Parameters}

import collection.JavaConverters.*
import scala.concurrent.{Await, Future, duration}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import java.io.File
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.regions.Regions
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.model.BucketVersioningConfiguration
import com.amazonaws.services.s3.model.{AmazonS3Exception, BucketVersioningConfiguration, SetBucketVersioningConfigurationRequest}

object GenerateLogData:

  //  logger to log the values for the class
  val logger = CreateLogger(classOf[GenerateLogData.type])


@main def runLogGenerator =
  import Generation.RSGStateMachine.*
  import Generation.*
  import HelperUtils.Parameters.*
  import GenerateLogData.*

  logger.info("Log data generator started...")
  val INITSTRING = "Starting the string generation"
  val init = unit(INITSTRING)

  val logFuture = Future {
    LogMsgSimulator(init(RandomStringGenerator((Parameters.minStringLength, Parameters.maxStringLength), Parameters.randomSeed)), Parameters.maxCount)
  }

  /*// get the bucket name from config file
  val bucketName: String = config.getString("randomLogGenerator.aws_s3.bucketName")
  // get the s3 file name from config file
  val s3fileName: String = config.getString("randomLogGenerator.aws_s3.s3fileName") + java.time.LocalDate.now.toString + ".log"
  // get the local file name from config file
  val localFileName: String = config.getString("randomLogGenerator.aws_s3.localFileName") + java.time.LocalDate.now.toString + ".log"

  val s3: AmazonS3 = AmazonS3ClientBuilder.standard
    .withRegion(Regions.US_EAST_1) // The first region to try the request against
    .withForceGlobalBucketAccessEnabled(true) // If a bucket is in a different region, try again in the correct region
    .build

  try {
    // Enable versioning on the bucket.
    val configuration: BucketVersioningConfiguration = BucketVersioningConfiguration().withStatus("Enabled");

    // Set bucket versioning with configuration
    val setBucketVersioningConfigurationRequest = new SetBucketVersioningConfigurationRequest(bucketName, configuration)

    s3.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest)

    // To verify the config status
    val conf = s3.getBucketVersioningConfiguration(bucketName)
    logger.info("bucket versioning configuration status:    " + conf.getStatus)

  } catch {
    case e: AmazonS3Exception => logger.error(e)
  }

  // Try to put the object in the s3 bucket
  try s3.putObject(bucketName, s3fileName, new File(localFileName))
  catch {
    case e: AmazonServiceException =>
      logger.error(e)
  }*/

  Try(Await.result(logFuture, Parameters.runDurationInMinutes)) match {
    case Success(value) => logger.info(s"Log data generation has completed after generating ${Parameters.maxCount} records.")
    case Failure(exception) => logger.info(s"Log data generation has completed within the allocated time, ${Parameters.runDurationInMinutes}")
  }

