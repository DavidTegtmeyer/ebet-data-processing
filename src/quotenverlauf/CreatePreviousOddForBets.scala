package quotenverlauf

import util.General
import util.General._

/**
 * Jeder single-bet soll die zugrunde liegende Quotenänderung zugewiesen werden.
 *
 * Dazu muss zu jeder Wettabgabe in der odds-history für die jeweilige oddId der Eintrag gesucht werden, der am nächsten
 * dran ist (und kleiner).
 *
 * Soo..
 *
 * Schwierig..
 *
 * Map anlegen aus der odds-history (armer Speicher) Map(Long, Seq(String))
 *
 * Long ist die OddId, Seq(String) ist eine rückwärts sortierte Liste an Daten (also Datums), die man dann durchgehen kann
 * bis Wetteinsatzdelivery kleiner ist, als das gefundene Datum.
 *
 * Eingabedatei header sieht so aus:
 * ------------------------------------------------------------------------------
 * provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated;previousQuote
 * ------------------------------------------------------------------------------
 */
object CreatePreviousOddForBets extends App {

  start

  def start() = {
    import General._
    // erstmal legen wir die oben beschriebene Map an...

    println("Wish me luck ... ")
    val linesOddsHistory = getLinesFor(rootFolder + "/odds-history/oh-sorted-by-oddupdated-with-eventbegin-with-previousquote.csv")

    println("odds-history retrieved...")
    val oddsHistoryGroupedByOddId = linesOddsHistory.toSeq.groupBy(getOddId)

    println("... grouped.")
    val oddsHistoryGroupedByOddISortedByOddUpdated = oddsHistoryGroupedByOddId.map(x => {
      val sorted = x._2.sortBy(getOddUpdated).reverse.map(line => {
        val result = (getOddUpdated(line), getPreviousOdd(line))
          println(result)
        (getOddUpdated(line), getPreviousOdd(line))
      })

      (x._1, sorted)
    })

    println("... sorted.")

    val singleBets = getLinesFor(rootFolder + "/single-bets/single-bets-filtered-by-buli-with-event-begin.csv")
    println("single-bets retrieved...")
    println("starting to append previous quotes..")
    val singleBetsHeader = singleBets.next()

    def appendPreviousOddToSingleBetLine(line: String): String = {
      val currentOddId = getOddIdInBets(line)
      val currentDeliveryDate = getDeliveryDate(line)
      val eventBegin = getNthElementInLine(4)(line)
      val firstSmaller = oddsHistoryGroupedByOddISortedByOddUpdated(currentOddId).collectFirst({case element if (element._1 < currentDeliveryDate) => element._2})
      firstSmaller match {
        case Some(l) => line + ";" + l
        case None =>
          println("FEHLER!! " )
          line + ";" + "-1.0"
      }
    }

    println("printing result")
    println(singleBetsHeader + ";previousOdd")

    write(rootFolder + "/single-bets/single-bets-filtered-by-buli-withPrevious.csv", singleBets.map(appendPreviousOddToSingleBetLine), singleBetsHeader + ";previousOdd", 13399002 )
  }


  def getOddIdInBets(line: String) = getThirdElement(line)
  def getThirdElement = getNthElementInLine(2)

  def getPreviousOdd(line: String) = getNinthElementInLine(line)
  def getNinthElementInLine = getNthElementInLine(9)

  def getOddId(line: String) = getFifthElementInLine(line)
  def getFifthElementInLine = getNthElementInLine(4)

  def getQuote(line: String) = getSeventhElementInLine(line)
  def getSeventhElementInLine = getNthElementInLine(6)

  def getOddUpdated(line: String) = getEigthElementInLine(line)
  def getEigthElementInLine = getNthElementInLine(7)

  def getDeliveryDate(line: String) = getFirstElement(line)
  def getFirstElement = getNthElementInLine(0)
}
