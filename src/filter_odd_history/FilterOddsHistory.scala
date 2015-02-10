package filter_odd_history

import java.io.{File, PrintWriter}

/**
 * Created by david on 10.02.15.
 */
object FilterOddsHistory extends App {

  start()

  def start() = {

    println("Started filtering odds-history")
    val eventMap = scala.io.Source
      .fromFile("/Users/david/Desktop/EBET-816/helper/eventIds-buli.csv")
      .getLines.toSet match { case line: String => line.toDouble }

    def isInEvents(s: Double) = {
      eventMap.contains(s)
    }

    val lines = scala.io.Source.fromFile("/Users/david/Desktop/EBET-816/odds-history-2015-01-14.csv")
      .getLines.filter(line => isInEvents(line.split(";")(1).toDouble))

    val writer = new PrintWriter(new File("/Users/david/Desktop/EBET-816/helper/odds-history-filtered-by-buli.csv" ))

    writer.write("provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated\n")


    var counter = 0

    println("Initializing done ... ")
    println("counter set to zero. Here we go.")
    while (lines.hasNext)
    {
      print(",")
      if (counter % 5000 == 0)
        println("Yeah ...")
      writer.write(lines.next + "\n")
      counter = counter + 1
    }

    writer.close()
  }

}
