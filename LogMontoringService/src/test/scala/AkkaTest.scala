import akka.actor.{ActorKilledException, ActorSystem, Kill, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActors, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import akka.testkit.TestProbe
import akka.actor.Props
import akka.pattern.ask
import sun.tools.jconsole.Worker

import scala.concurrent.duration._
import scala.language.postfixOps

class AkkaTest extends TestKit(ActorSystem("AkkaTest"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  val worker = TestProbe("worker")
  val aggregator = TestProbe("aggregator")

  // Check whether actor sends message unchanged
  "An Actor" must {

    "send messages unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "Check message"
      expectMsg("Check message")
    }

  }

  // Check whether worker reference starts with worker name
  worker.ref.path.name should startWith("worker")

  // // Check whether aggregator reference starts with aggregator name
  aggregator.ref.path.name should startWith("aggregator")
  

}
