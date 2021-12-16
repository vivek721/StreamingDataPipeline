package Driver

import java.io.File
import java.util.EventObject

/*
A file event class gets the instance of the file , where the event in occured.
*/

class FileEvent(val file: File) extends EventObject(file) {

  // Method to get the instance of the file
  def getFile: File = getSource.asInstanceOf[File]

}