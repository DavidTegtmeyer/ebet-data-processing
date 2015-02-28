package clean.oddshistory

import clean.util.General
import clean.util.General._

/**
 * Created by david on 28.02.15.
 */
object CountOddId extends App {
  import General._
  import clean.util.Directories._

  start

  def start() = {
    println("Filtering oh")

    println("Reading oddupdates")
    val oddUpdates = getLinesFor(OriginalFiles.oddshistory)
    var counter = 0
    for (line <- oddUpdates)
      if (line.contains("54409901"))
        counter = counter + 1

    println(counter)
  }
}
