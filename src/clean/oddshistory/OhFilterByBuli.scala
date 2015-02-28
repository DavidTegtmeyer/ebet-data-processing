package clean.oddshistory

import clean.entity.{ OddUpdate, Event}
import clean.util.General
import clean.util.General._

/**
 * Created by david on 28.02.15.
 */
object OhFilterByBuli extends App {
  import General._
  import clean.util.Directories._

  start

  def start() = {
    println("Filtering oh")

    println("Reading oddupdates")
    val oddUpdates = getLinesFor(OriginalFiles.oddshistory)
    val firstLine = oddUpdates.next() + ";eventBegin"

    println("Reading buliEvents")
    val buliEventLines = getLinesFor(Events.filteredByBuli)
    val firstEventLine = buliEventLines.next
    val buliEvents = buliEventLines.map(line => Event(line.split(";")))

    println("creating id sequence")
    val buliEventMap = buliEvents.map(event => (event.eventId, event.eventBegin)).toMap

    println("Filtering.")
    val oddUpdatesBuli = oddUpdates filter(oddUpdateLine => buliEventMap.contains(getNthElementInLine(1)(oddUpdateLine).toLong))


    val oddUpdatesWithEventBegin = oddUpdatesBuli map(oddUpdate => oddUpdate + ";" + buliEventMap(getNthElementInLine(1)(oddUpdate).toLong))

    println("Writing!")
    write(OddsHistory.filteredByBuli + "safe", oddUpdatesWithEventBegin, firstLine, 333333333)
  }
}
