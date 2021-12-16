package Driver

//Default implementation of the FileListener interface so that we can process only few of the events

abstract class FileAdapter extends FileListener {

//  def onCreated(event: FileEvent): Unit

  def onModified(event: FileEvent): Unit

//  def onDeleted(event: FileEvent): Unit

}

