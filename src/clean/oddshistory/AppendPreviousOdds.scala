package clean.oddshistory

import java.text.SimpleDateFormat

import clean.entity.{PreviousOdd, SingleBet, SmallOddUpdate, OddUpdate}
import clean.util.General

/**
 * Created by david on 28.02.15.
 */
object AppendPreviousOdds extends App {

  import clean.util.Directories._
  import clean.util.General._

  start

  def start() = {
    println("Reading odds history")
    val oddUpdatedLines = getLinesFor(OddsHistory.filteredByBuli)
    val firstLine = oddUpdatedLines.next() + ";eventBegin;previousOdd;ratioToPrevious;diffToPrevious"
    val oddUpdates = oddUpdatedLines.map(line => OddUpdate(line.split(";")))
    println("Creating map")
    val oddIdMap = oddUpdates.toStream.groupBy(_.oddId)

    println("creating odds history with previous odds")
    val oddIdMapWithPreviousOdds = oddIdMap.map {
      longToUpdateStream =>
          var oldOdd = 0.0
          var oldOddUpdated = 0l
          val newStream = for (oddUpdate <- longToUpdateStream._2) yield {
            val currentOddUpdated = dateStringToLong(oddUpdate.oddUpdated)
            val result = if (oldOdd == 0.0)
              oddUpdate.copy(previousOdd = Some(PreviousOdd(oddUpdate.odd, oddUpdate.odd, 0l)))
            else
              oddUpdate.copy(previousOdd = Some(PreviousOdd(oldOdd, oddUpdate.odd, oldOddUpdated)))

            oldOdd = oddUpdate.odd
            oldOddUpdated = currentOddUpdated
            result
          }
        (longToUpdateStream._1, newStream.reverse)
    }
    println("Test stream of oddupdates")
    oddIdMapWithPreviousOdds(12760105910l) foreach println
    println("Reading single bets")
    val singleBetLines = getLinesFor(SingleBets.filteredByBuli)
    val singleBetsHeader = singleBetLines.next + ";previousOdd;ratioToPreviousOdd;diffToPreviousOdd;timeDiffToPreviousOddInSecs"
    val singleBets = singleBetLines.map(line => SingleBet(line.split(";")))
    val singleBetsWithPrevious = singleBets.map(sb => {
      val foundPrevOdd = oddIdMapWithPreviousOdds(sb.oddId).collectFirst {
        case x if (x.oddUpdated < sb.deliveryDate) => x
      }

      val newPrevOdd = foundPrevOdd match {
        case Some(oddUpdated) => {
          val previousOdd = oddUpdated.previousOdd.get
          val oldTimeDiff = previousOdd.timeDiff
          previousOdd.copy(timeDiff = dateStringToLong(sb.deliveryDate) - oldTimeDiff)
        }
        case None => PreviousOdd(sb.odd, sb.odd, -1l)
      }
      sb.copy(previousOdd = Some(newPrevOdd))
    })

    write(SingleBets.withPreviousOdds, singleBetsWithPrevious.map(_.toString), singleBetsHeader, 22)
  }
  def formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
  def dateStringToLong(s: String) = formatter.parse(s).getTime
}

/*

Write previous odds to oddshistory
 val result = oddIdMapWithPreviousOdds.valuesIterator.flatten

   // val singleBets = getLinesFor()

    write(OddsHistory.filteredByBuliWithPreviousOdds, result.map(_.toString), firstLine, 620)
 */

/*
10;121966910;standard;;12821881010;1;2.25;2014-11-11 08:20:20;2.25;1.0;0.0
10;121966910;standard;;12821881010;1;2.35;2014-11-11 08:23:23;2.25;1.0444444444444445;0.10000000000000009
10;121966910;standard;;12821881010;1;2.3;2014-11-22 18:16:05;2.35;0.978723404255319;-0.050000000000000266
10;121966910;standard;;12821881010;1;2.25;2014-11-24 18:10:18;2.3;0.9782608695652175;-0.04999999999999982
10;121966910;standard;;12821881010;1;2.35;2014-11-28 14:27:28;2.25;1.0444444444444445;0.10000000000000009
10;121966910;standard;;12821881010;1;2.4;2014-11-28 20:06:35;2.35;1.0212765957446808;0.04999999999999982
10;121966910;standard;;12821881010;1;2.3;2014-11-28 20:47:24;2.4;0.9583333333333333;-0.10000000000000009
10;121966910;standard;;12821881010;1;2.4;2014-11-28 20:56:12;2.3;1.0434782608695652;0.10000000000000009
10;121966910;standard;;12821881010;1;2.5;2014-11-28 20:56:27;2.4;1.0416666666666667;0.10000000000000009
10;121966910;standard;;12821881010;1;5.5;2014-11-28 21:02:07;2.5;2.2;3.0
10;121966910;standard;;12821881010;1;2.5;2014-11-28 21:11:52;5.5;0.45454545454545453;-3.0
10;121966910;standard;;12821881010;1;2.6;2014-11-28 21:40:08;2.5;1.04;0.10000000000000009
10;121966910;standard;;12821881010;1;7.0;2014-11-28 21:41:29;2.6;2.692307692307692;4.4
10;121966910;standard;;12821881010;1;8.0;2014-11-28 21:46:46;7.0;1.1428571428571428;1.0
10;121966910;standard;;12821881010;1;9.0;2014-11-28 21:49:36;8.0;1.125;1.0
10;121966910;standard;;12821881010;1;10.0;2014-11-28 21:50:22;9.0;1.1111111111111112;1.0
10;121966910;standard;;12821881010;1;12.0;2014-11-28 21:53:10;10.0;1.2;2.0
10;121966910;standard;;12821881010;1;30.0;2014-11-28 21:57:30;12.0;2.5;18.0
10;121966910;standard;;12821881010;1;100.0;2014-11-28 21:59:26;30.0;3.3333333333333335;70.0
10;121966910;standard;;12821881010;1;150.0;2014-11-28 22:02:50;100.0;1.5;50.0
10;121966910;standard;;12821881010;1;300.0;2014-11-28 22:04:49;150.0;2.0;150.0
10;121966910;standard;;12821881010;1;500.0;2014-11-28 22:10:28;300.0;1.6666666666666667;200.0
10;121966910;standard;;12821881010;1;700.0;2014-11-28 22:17:48;500.0;1.4;200.0
 */