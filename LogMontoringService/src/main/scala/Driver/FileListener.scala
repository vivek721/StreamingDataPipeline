package Driver

import java.util.EventListener

// Method implemented by an observer in order to be notified for file events
trait FileListener extends EventListener {

  // To notify on creation
//  def onCreated(event: FileEvent): Unit
  // To notify on modification
  def onModified(event: FileEvent): Unit
  // To notify on deletion
//  def onDeleted(event: FileEvent): Unit

}