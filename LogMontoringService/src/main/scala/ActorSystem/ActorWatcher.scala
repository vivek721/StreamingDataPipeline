package ActorSystem

import HelperUtils.CreateLogger
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import Driver.{FileAdapter, FileEvent, FileWatcher}

import java.io.File


object ActorWatcher {
  def props(extractor: ActorRef, path: String): Props = Props(new ActorWatcher(extractor, path))
}

class ActorWatcher(extractor: ActorRef, path: String) extends Actor {

  // To print log messages in console
  val log = CreateLogger(classOf[ActorWatcher])

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("Application.conf")

  // To receive the extractor
  override def receive: Receive = {
    case "watch" =>
      watch(extractor)
    case _ => log.error("Invalid input")
  }

  // To watch the file
  def watch(extractor: ActorRef): Unit = {
    //Specify the path here
    val folder = new File(path)
    val watcher = new FileWatcher(folder)
    // Add the listener to watcher
    log.info("Add Listeners to watchers")
    watcher.addListener(new FileAdapter() {
      override def onModified(event: FileEvent): Unit = {
        extractor ! event.getFile
      }
    }).watch()
  }
}

object Main extends App {
  // Get the config values
  val config: Config = ConfigFactory.load("application" + ".conf")
  val path = config.getString("config.FileName")
  val system = ActorSystem("Watchers")
  // Get the extractor from Actor Extractor
  val extractor = system.actorOf(Props[ActorExtractor], name = "extractor")
  val watcher = system.actorOf(ActorWatcher.props(extractor, path), name = "watcher")

  watcher ! "watch"
}
