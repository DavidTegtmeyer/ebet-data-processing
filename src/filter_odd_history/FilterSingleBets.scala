package filter_odd_history

import java.io.{File, PrintWriter}

import util.General._

/**
 * Created by david on 11.02.15.
 */
object FilterSingleBets extends App {
  start()
  def start() = {

    println("Started filtering single-bets")
    val singleBets = scala.io.Source
      .fromFile("/Users/david/Desktop/EBET-816/www2_single-bets-2015-01-15.csv")
      .getLines

    singleBets.next

    def getOddId = getNthElemet(2)

    val oToEMap = scala.io.Source
      .fromFile("/Users/david/Desktop/EBET-816/helper/oddids-to-eventids.csv")
      .getLines.map(line => (line.split(";")(1), line.split(";")(0))).toMap

    singleBets.next

    val a = oToEMap.get("")
    val filteredBetsWithEId = singleBets.map(line => oToEMap.get(getOddId(line)) match {
      case Some(l) => Some(line + ";" + l)
      case None => None
    }).flatten



    val writer = new PrintWriter(new File("/Users/david/Desktop/EBET-816/helper/single-bets-filtered-by-buli.csv"))

    writer.write("deliveryDate;type;oddId;odd;stake;customerId;automaticCustomerRating;automaticCustomerRatingLive;eventId\n")

    while (filteredBetsWithEId.hasNext) {
      // println(filteredBetsWithEId.next)
      writer.write(filteredBetsWithEId.next + "\n")

    }


    writer.close()
  }

  def getNthElemet(n: Int) = (s: String) => s.split(";")(n)
}