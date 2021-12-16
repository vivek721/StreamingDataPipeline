package Driver

import HelperUtils.CreateLogger
import com.typesafe.config.ConfigFactory
import Driver.FileWatcher.log

import java.io.{File, IOException}
import java.nio.file.StandardWatchEventKinds._
import java.nio.file._
import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

// The class maintains the list of observers and notifies them about changes by calling the methods given .
object FileWatcher {

  // To print log messages in console
  val log = CreateLogger(classOf[FileWatcher.type])

  // Get the config values from application.conf in resources
  val config = ConfigFactory.load("application.conf")

  // Watch service api to register the file to be monitored .
  protected val watchServices = new util.ArrayList[WatchService]
  // To get the list of watch services
  def getWatchServices: util.List[WatchService] = Collections.unmodifiableList(watchServices)
  log.info("Get the list of watch services")

}

class FileWatcher(val folder: File) extends Runnable {
  protected var listeners = new util.ArrayList[FileListener]

  // Check whether the folder exists and create a thread rto monitor
  def watch(): Unit = {
    log.info("Check whether folder exists")
    if (folder.exists) {
      val thread = new Thread(this)
      thread.setDaemon(true)
      thread.start()
      log.info("Thread Started")
    }
  }

  log.info("Run method started to start the poll event")
  // run method to start the poll event
  override def run(): Unit = {
    try {
      val watchService = FileSystems.getDefault.newWatchService
      try {
        // get the file location
        val path = Paths.get(folder.getAbsolutePath)
        // Register the the file with watch service with the create, delete and modify notification enabled .
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)
        // Add the created watchservice to the list of watchservices to be monitored .
        FileWatcher.watchServices.add(watchService)
        var poll = true
        while(poll) {
          // Poll events method monitors the file changes and notify the listener using notify listener method
          log.info("Notify the listener about the file changes")
          poll = pollEvents(watchService)
        }
      } catch {
            // Interrupt the thread incase of a exception in the poll event .
        case e@(_: IOException | _: InterruptedException | _: ClosedWatchServiceException) =>
          log.error("Thread Interrupted")
          Thread.currentThread.interrupt()
      }
        // Close the watch service once the event ends
      finally if (watchService != null) watchService.close()
    }
    catch {
      case e@(_: IOException | _: InterruptedException | _: ClosedWatchServiceException) =>
        Thread.currentThread.interrupt()
    }
  }

  @throws[InterruptedException]
  // Poll events method monitors the file changes and notify the listener using notify listener method
  protected def pollEvents(watchService: WatchService): Boolean = {
    val key = watchService.take
    val path = key.watchable.asInstanceOf[Path]
    // Monitor the events and notify the listeners
    log.info("Events monitor")
    for (event <- key.pollEvents.asScala) {
      notifyListeners(event.kind, path.resolve(event.context.asInstanceOf[Path]).toFile)
    }
    key.reset
  }

  // Notify listeners method which checks for entry
  protected def notifyListeners(kind: WatchEvent.Kind[_], file: File): Unit = {
    val event = new FileEvent(file)
    /* When a directory is registered for this event then the Watch key
is queued when it is observed that an entry is created in the directory */
//    if (kind eq ENTRY_CREATE) {
//      for (listener <- listeners.asScala) {
//        listener.onCreated(event)
//      }
//      if (file.isDirectory) new FileWatcher(file).setListeners(listeners).watch()
//    }
    /* When a directory is registered for this event then the Watch key
is queued when it is observed that an entry is modified */
    log.info("Notify the listener")
    if (kind eq ENTRY_MODIFY) {
      for (listener <- listeners.asScala) {
        listener.onModified(event)
      }
    }

   /* When a directory is registered for this event then the Watch key
   is queued when it is observed that an entry is deleted or renamed out of
    the directory. */
//    else if (kind eq ENTRY_DELETE) {
//      for (listener <- listeners.asScala) {
//        listener.onDeleted(event)
//      }
//    }
  }

  // Method to add listeners
  def addListener(listener: FileListener): FileWatcher = {
    log.info("Add Listener")
    listeners.add(listener)
    this
  }

  // Method to remove listeners
  def removeListener(listener: FileListener): FileWatcher = {
    log.info("Remove Listener")
    listeners.remove(listener)
    this
  }

  // Method to get Listeners
  def getListeners: util.List[FileListener] = listeners

  //Method to set listeners
  def setListeners(listeners: util.ArrayList[FileListener]): FileWatcher = {
    log.info("Set Listener")
    this.listeners = listeners
    this
  }
}


