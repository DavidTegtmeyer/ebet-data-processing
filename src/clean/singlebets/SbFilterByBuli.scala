package clean.singlebets

import clean.entity.SingleBet

/**
 * Created by david on 28.02.15.
 */
object SbFilterByBuli extends App{

  import clean.util.Directories._
  import clean.util.General._

  start

  def start() = {
    val oddUpdateLines = getLinesFor(OddsHistory.filteredByBuli)
    oddUpdateLines.next()
    val oddIdToEventIdMap = oddUpdateLines.map(line => OddIdToEventId(line.split(";"))).toSeq.distinct.map(oe => (oe.oddId, oe.eventId)).toMap

    val oddIdsBuli = oddIdToEventIdMap.keySet

    val singleBetLines = getLinesFor(OriginalFiles.singlebets)
    val sbHeader = singleBetLines.next()

    val singleBets = singleBetLines.map(line => SingleBet(line.split(";")))
    val singleBetsFiltered = singleBets.filter(sb => oddIdsBuli.contains(sb.oddId))

    write(SingleBets.filteredByBuli, singleBetsFiltered.map(_.toString), sbHeader, 234)
  }

}

case class OddIdToEventId(
                         oddId: Long,
                         eventId: Long
                           )

// header: provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated;eventBegin
object OddIdToEventId {
  def apply(split: Array[String]): OddIdToEventId =
    this(split(4).toLong, split(1).toLong)
}
