package clean.entity

import java.text.SimpleDateFormat
import java.util.Date

object SmallOddUpdate{
  def apply(split: Array[String]): SmallOddUpdate =
    SmallOddUpdate(//splitted(0),
      //splitted(1),
      // split(2),
      //splitted(3),
      split(4).toLong,
      //splitted(5),
      split(6).toDouble,
      split(7),
      0.0)
      //splitted(8),
      //split(9),
      //split(10),
      //split(11))
  

  def header = "provider;id;marketType;marketParam;id;choiceParam;odd;oddUpdated;eventBegin"
}
case class SmallOddUpdate(//provider: String,
                     //eventId: String,
                     // marketType: String,
                     //marketParam: String,
                     oddId: Long,
                     //choiceParam: String,
                     odd: Double,
                     oddUpdated: String,
                     previousOdd: Double)
                     //eventBegin: String)

object Event {
  def apply(split: Array[String]): Event = Event(split(0),
    split(1).toLong,
    split(2),
    split(3).toLong,
    split(4),
    split(5).toLong,
    split(6),
    split(7).toLong,
    split(8),
    split(9),
    split(9).split(" - "))
}
case class Event(
    sportId: String,
    rootGroupId: Long,
    rootGroupName: String,
    countryGroupId: Long,
    countryGroupName: String,
    leagueGroupId: Long,
    leagueGroupName: String,
    eventId: Long,
    eventBegin: String,
    eventName: String,
    participants: Seq[String]) {
  override def toString: String =
    sportId + ";" + rootGroupId  + ";" + rootGroupName  + ";" + countryGroupId  + ";" + countryGroupName + ";" + leagueGroupId  + ";" + leagueGroupName  + ";" +eventId  + ";" + eventBegin  + ";" + eventName
}

object SingleBet {
  def apply(split: Array[String]): SingleBet = SingleBet(split(0),
    split(1),
    split(2).toLong,
    split(3).toDouble,
    split(4).toDouble,
    split(5).toLong,
    split(6).toDouble,
    split(7).toDouble,
    None)
}
case class SingleBet(
  deliveryDate: String,
  `type`: String,
  oddId: Long,
  odd: Double,
  stake: Double,
  customerId: Long,
  automaticCustomerRating: Double,
  automaticCustomerRatingLive:Double,
  previousOdd: Option[PreviousOdd]) {
  override def toString: String = {
    val prev = previousOdd match {
      case None => ""
      case Some(po) => ";" + po.toString
    }
    deliveryDate + ";" + `type` + ";" + oddId + ";" + odd + ";" + stake  + ";" + customerId + ";" + automaticCustomerRating  + ";" + automaticCustomerRatingLive + prev
  }

}

object OddUpdate {

  def apply(split: Seq[String]): OddUpdate =
    OddUpdate(split(0).toInt,
      split(1).toLong,
      split(2),
      split(3),
      split(4).toLong,
      split(5),
      split(6).toDouble,
      split(7),
      None)

}
case class OddUpdate(
  providerPk: Int,
  eventId: Long,
  marketType: String,
  marketParam: String,
  oddId: Long,
  choiceParam: String,
  odd: Double,
  oddUpdated: String,
  previousOdd: Option[PreviousOdd]) {
  override def toString: String = {
    val prev = previousOdd match {
      case None => ""
      case Some(po) => ";" + po.toString
    }

    providerPk   + ";" + eventId   + ";" + marketType   + ";" + marketParam  + ";" + oddId   + ";" + choiceParam   + ";" + odd   + ";" + oddUpdated + prev
  }

}

object PreviousOdd {
  def apply(previousOdd: Double, currentOdd: Double, timeDiff: Long): PreviousOdd = PreviousOdd(previousOdd, currentOdd/previousOdd, currentOdd - previousOdd, timeDiff)
}
case class PreviousOdd(odd: Double, ratio: Double, diff: Double, timeDiff: Long) {
  override def toString: String = odd + ";" + ratio + ";" + diff + ";" + timeDiff
}