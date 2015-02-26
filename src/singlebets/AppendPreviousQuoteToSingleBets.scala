package singlebets

import util.General
import util.General._


object OddUpdate{
  def apply(line: String): OddUpdate = {
    val splitted = line.split(";")
    OddUpdate(//splitted(0),
      //splitted(1),
      //splitted(2),
      //splitted(3),
      splitted(4),
      //splitted(5),
      //splitted(6),
      splitted(7),
      //splitted(8),
      splitted(9),
      splitted(10),
      splitted(11))
  }
}
case class OddUpdate(//provider: String,
                     //eventId: String,
                     //marketType: String,
                     //marketParam: String,
                     oddId: String,
                     //choiceParam: String,
                     //odd: String,
                     oddUpdated: String,
                     //eventBegin: String,
                     previousQuote: String,
                     diffRatio: String,
                     diff: String)

object SingleBet {
  def apply(line: String): SingleBet = {
    val splitted = line.split(";")

    SingleBet(splitted(0), splitted(1), splitted(2), splitted(3), splitted(4), splitted(5),
      splitted(6), splitted(7), splitted(8), splitted(9), "")

  }
}

case class SingleBet(deliveryDate: String,
                     _type: String,
                     oddId: String,
                     odd: String,
                     stake: String,
                     customerId: String,
                     automaticCustomerRating: String,
                     automaticCustomerRatingLive: String,
                     eventId: String,
                     eventBegin :String,
                      previousQuote: String)
object AppendPreviousQuoteToSingleBets extends App {

  start

  def start() = {
    import General._

    println("Reading input.")
    val lines = getLinesFor(rootFolder  + "/odds-history/oh-with-eventbegin-previousQuote-diff.csv")
    val ohHeader = lines.next()
    println(ohHeader)
    println("Grouping and sorting Elements")
    val ohCaseClassed = lines.map(OddUpdate(_))
    val groupedByOddId = ohCaseClassed.toStream.groupBy(_.oddId)
    val groupedByOddIdSorted = groupedByOddId.map(mapElement => (mapElement._1 -> mapElement._2.sortBy(_.oddUpdated).reverse))

    val singleBetLines = getLinesFor(rootFolder + "/single-bets/single-bets-filtered-by-buli-with-eventbegin.csv")
    val singleBetsHeader = singleBetLines.next()
    val singleBets = singleBetLines.map(SingleBet(_))
    println("single-bets retrieved...")
    println("starting to append previous quotes..")


    def appendPreviousOddToSingleBetLine(singleBet: SingleBet): String = {
      val currentOddId = singleBet.oddId
      val currentDeliveryDate = singleBet.deliveryDate
      val eventBegin = singleBet.eventBegin
      val currentQuote = singleBet.odd

      val firstSmaller = groupedByOddIdSorted(currentOddId).collectFirst {
        case ohEntry if (ohEntry.oddUpdated < currentDeliveryDate) => ohEntry
      }
      val result = firstSmaller match {
        case Some(ohEntry) => singleBet.copy(previousQuote = ohEntry.previousQuote)
        case None =>
          println("FEHLER!! ")
          println("Odds and delivery date")
          groupedByOddIdSorted(currentOddId).foreach(println)
          println(currentDeliveryDate)
          singleBet.copy(previousQuote = currentQuote)
      }

      singleBetToString(result)
    }

    println("printing result")
    println(singleBetsHeader + ": String,previousOdd")

    write(rootFolder + "/single-bets/single-bets-filtered-by-buli-withPrevious.csv", singleBets.map(appendPreviousOddToSingleBetLine), singleBetsHeader + ";previousOdd", 13399002)
  }

  def singleBetToString(singleBet: SingleBet): String = {
    singleBet.deliveryDate   + ";" +  singleBet._type   + ";" +  singleBet.oddId   + ";" +  singleBet.odd   + ";" +  singleBet.stake  + ";" +
      singleBet.customerId   + ";" +  singleBet.automaticCustomerRating   + ";" +  singleBet.automaticCustomerRatingLive   + ";" +
      singleBet.eventId   + ";" +  singleBet.eventBegin   + ";" +  singleBet.previousQuote
  }
}