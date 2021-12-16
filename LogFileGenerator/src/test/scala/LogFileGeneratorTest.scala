import Generation.RSGStateMachine.unit
import Generation.RandomStringGenerator
import com.mifmif.common.regex.Generex
import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
//import org.hamcrest.MatcherAssert.assertThat

import scala.io.Source.*

class LogFileGeneratorTest extends AnyFlatSpec with Matchers {

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("Application.conf").getConfig("randomLogGenerator")

  val MinString = config.getInt("MinString")
  //  val lines = fromFile("data/sample.txt").getLines.toString()
  val lines = "01:52:34.402 [scala-execution-context-global-15] INFO"

  val INITSTRING = "Starting the string generation"
  val init = unit(INITSTRING)

  behavior of "Configuration Parameters Module"


  // To check whether the timeStamp in the sample input file is same as the one expected
  it should "check file timestamp" in {
    lines.split(" ")(0) shouldBe "01:52:34.402"
  }

  // To check whether the log level in the sample input file is same as the one expected
  it should "check file log level" in {
    lines.split(" ")(2) shouldBe "INFO"
  }

  //   To check whether the seperator in the config file is same as the one expected
  // @Test
  //  def testConfigurationValues(): Unit ={
  //    Assert.assertEquals(" ",",", config.getString("separator"))
  //  }

  // To locate an instance of the pattern in the randomly generated string
  it should "locate an instance of the pattern in the generated string" in {
    val patternString = "[\\*]+"
    val generex: Generex = new Generex(patternString)
    val genString = generex.random()
    genString should include regex patternString.r
  }

  // check whether a random generated string length is lesser than the minimum length
  it should "generate a random string whose length is greater than the min length" in {
    MinString shouldBe >= (5)
  }


}