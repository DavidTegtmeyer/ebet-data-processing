package clean.util

/**
 * Created by david on 28.02.15.
 */
object Directories {

  def rootFolder = "/Users/david/Desktop/Daten/"

  def originals = rootFolder + "EBET-816/"

  def oddshistoryFolder = rootFolder + "odds-history/"

  def singlebetsFolder = rootFolder + "single-bets/"

  def eventsFolder = rootFolder + "events/"

  def helper = rootFolder + "helper/"

  object OriginalFiles {

    def oddshistory = originals + "odds-history-2015-01-14.csv"

    def singlebets = originals + "www2_single-bets-2015-01-15.csv"

    def events = originals + "events-2015-01-14.csv"
  }

  object SingleBets {
    def filteredByBuli = singlebetsFolder + "singlebets-buli.csv"
    def withPreviousOdds = singlebetsFolder + "singlebets-withpreviousodds.csv"
  }

  object Events {
    def filteredByBuli = eventsFolder + "events-buli.csv"
  }

  object Helper {

  }

  object OddsHistory {
    def filteredByBuli = oddshistoryFolder + "oddshistory-buli.csv"
    def filteredByBuliWithPreviousOdds = oddshistoryFolder + "oddshistory-buli-with-previousodd.csv"
  }
}
