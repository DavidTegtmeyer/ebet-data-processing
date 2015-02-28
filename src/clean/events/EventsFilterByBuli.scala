package clean.events

import clean.entity.Event
import clean.util.General

/**
 * Created by david on 28.02.15.
 */
object EventsFilterByBuli extends App {

  import General._
  import clean.util.Directories._

  start

  def start() = {
    println("Filtering events")
    val eventLines = getLinesFor(OriginalFiles.events)
    val firstLine = eventLines.next()
    val events = eventLines.map(line => Event(line.split(";")))

    val buliEvents = events filter(_.leagueGroupId == 42301) map(_.toString)

    write(Events.filteredByBuli, buliEvents, firstLine, 620)
  }
}
