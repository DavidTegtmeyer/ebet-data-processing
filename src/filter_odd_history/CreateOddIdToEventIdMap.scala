package filter_odd_history

import java.io.{File, PrintWriter}

import util.General._

/**
 * Created by david on 11.02.15.
 */
object CreateOddIdToEventIdMap extends App {

  start()

  def start() = {
    val lines = scala.io.Source.fromFile("/Users/david/Desktop/EBET-816/helper/odds-history-filtered-by-buli.csv").getLines
    val pairs = lines.map(line => line.split(";")(1) + ";" + line.split(";")(4))

    val writer = new PrintWriter(new File("/Users/david/Desktop/EBET-816/helper/oddids-to-eventids.csv"))

    timed {
      while (pairs.hasNext) {
        writer.write(pairs.next + "\n")

      }
    }


    writer.close()
  }
}
