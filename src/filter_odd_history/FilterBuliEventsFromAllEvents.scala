package filter_odd_history

import java.io.{PrintWriter, File}

/**
 * Created by david on 10.02.15.
 */
object FilterBuliEventsFromAllEvents extends App {

  start()

  def start() = {

    println(new File("/Users/david/Desktop/EBET-816").getAbsolutePath)
    val lines = scala.io.Source.fromFile("/Users/david/Desktop/EBET-816/events-2015-01-14.csv")
      .getLines().filter(line => line.contains(";42301;"))

    val writer = new PrintWriter(new File("/Users/david/Desktop/EBET-816/events-filtered-by-buli.csv" ))

    writer.write("sportId;rootGroupId;rootGroupName;countryGroupId;countryGroupName;leagueGroupId;leagueGroupName;eventId;eventBegin;eventName\n")


    while (!lines.isEmpty)
    {
      writer.write(lines.next + "\n")
    }
    writer.close()
  }
}
