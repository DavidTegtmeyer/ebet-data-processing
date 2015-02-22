package filter_odd_history

import java.io.{File, PrintWriter}

/**
 * Created by david on 10.02.15.
 */
object FilterOddsHistory extends App {

  import util.General._
  start()

  def startt() = {
    val a = 23.4567
    println(f"$a%.2f")
  }
  def start() = {

    println("Started filtering odds-history")
    val eventLines = scala.io.Source
      .fromFile("/Users/david/Desktop/EBET-816/helper/eventIds-buli.csv")
      .getLines
    eventLines.next

    val eventMap = eventLines.map(line => line.toDouble).toSet

    def isInEvents(s: Double) = {
      eventMap.contains(s)
    }

    val strings = scala.io.Source.fromFile("/Users/david/Desktop/EBET-816/odds-history-2015-01-14.csv")
      .getLines
    strings.next()
    val l = strings.filter(line => isInEvents(line.split(";")(1).toDouble))


    val writer = new PrintWriter(new File("/Users/david/Desktop/EBET-816/helper/odds-history-filtered-by-buli.csv{safe}" ))

    writer.write("provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated\n")


    var counter:Double = 0.0

    println("Initializing done ... ")

    println("counter set to zero. Here we go.")

    timed{
      while (l.hasNext)
      {
        if (counter % 10000 == 0)
          println(f" ${(counter/341463156) *100}%.2f")
        writer.write(l.next + "\n")
        counter = counter + 1
      }
    }


    writer.close()
  }
}
