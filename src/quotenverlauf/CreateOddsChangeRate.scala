package quotenverlauf

import java.io.File

import util.General

/**
 * Zu jedem Eintrag, also zu jedem Quotenupdate soll das Verhältnis, also die prozentuale Änderung und die absolute Änderung
 * hinzugefügt werden.
 *
 * Header der Quelldatei:
 * ----------------------------------------------------------------
 * provider;eventid;marketType;marketParam;oddid;choiceParam;odd;oddUpdated
 * ----------------------------------------------------------------
 *
 * WICHTIG!!!
 * Eingabedatei muss nach Datum sortiert sein!
 *
 * Ein Großteil der Änderungen lassen sich aber mit Rapidminer gut umsetzen. Dieses Snippet speichert lediglich die letzte
 * Quote für jede OddId und trägt sie in die nächste Zeile mit der selben OddId als previousQuote ein.
 *
 * Ziel ist csv-Datei mit folgendem Header:
 * --------------------------------------------------------------------------------------------------------------
 * provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated;previousQuote
 * --------------------------------------------------------------------------------------------------------------
 *
 */
object CreateOddsChangeRate extends App {

  import General._


  start()

  def start() = {

    println("Reading input.")
    val lines = getLinesFor(rootFolder + "/odds-history/oh-sorted-by-oddupdated-with-eventbegin.csv")
    val header = lines.next() + ";previousQuote"

    println("Grouping and sorting Elements")
    val groupedByOddId = lines.toSeq.groupBy(line => getNthElementInLine(4)(line))
    val groupedByOddIdSorted = groupedByOddId.map(mapElement => mapElement._2.sortBy(getNthElementInLine(7)))
    val previousQuoteAppended = groupedByOddIdSorted.map(appendPreviousQuote).flatten.toIterator

    println("Writing to file.")
    write(rootFolder + "/odds-history/oh-sorted-by-oddupdated-with-eventbegin-previousQuote.csv",
      previousQuoteAppended, header, 13300000)

    println("Finished")
  }

  def appendPreviousQuote(seqLines: Seq[String]) = {
    var previousQuote: String = ""

    seqLines.map {
      line => {
        if (previousQuote == "")
          previousQuote = getQuote(line)
        val res = line + ";" + previousQuote
        previousQuote = getQuote(line)
        res
      }

    }
  }
  
  def reducedLine(line: String) = {
    getOddId(line) + ";" + getOddUpdated(line) + ";" + getQuote(line)
  }
  
  def getOddUpdated(line: String) = getSixthElementInLine(line)
  def getSixthElementInLine = getNthElementInLine(5)

  def getOddId(line: String) = getFifthElementInLine(line)
  def getFifthElementInLine = getNthElementInLine(4)

  def getQuote(line: String) = getSeventhElementInLine(line)
  def getSeventhElementInLine = getNthElementInLine(6)


}
