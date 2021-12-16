import com.typesafe.config.ConfigFactory
import org.scalatest.matchers.should.Matchers
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.flatspec.AnyFlatSpec

class ActorTest extends AnyFlatSpec with Matchers  {
  val config = ConfigFactory.load("application" + ".conf")
  it should "RegEx should length > 0" in {
    val fileName = config.getString("config.FileName")
    assert(fileName.length > 0)
  }

}
