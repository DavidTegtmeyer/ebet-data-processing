package clean.util

import clean.util.General._

/**
 * This class contains functions for the creation of maps from one attribute to another and aggregation functionality over maps that map attributes to Lists of values
 */
object MapCreation {


  def sumUp[A](a: Double, b: (A, Double)) = a + b._2

  def count[A](a: Double, b: (A, Double)) = a + 1

  def sumAndCount(b: (Double, Double), a: (Long, Double)): (Double, Double) = (b._1 + a._2, b._2 + 1)
  def sumAndCountDouble(b: (Double, Double), a: (Double, Double)): (Double, Double) = (b._1 + a._2, b._2 + 1)
  /*
  def mapFromIndexedSeq[A,B](it: Iterator[(A,B)], f: (B, (A, B)) => B, startB: B): Map[A, B] = {
    it.toIndexedSeq.groupBy(_._1).map { case (k, v) => (k,v.foldLeft(startB)(f))}.toMap
  }
*/
  def mapFromIndexedSeq2[ID,V,FR](it: Iterator[(ID,V)], f: (FR, (ID, V)) => FR, startFR: FR): Map[ID, FR] = {
    it.toIndexedSeq.groupBy(_._1).map { case (id, indexedSeqIdToValue) => (id,indexedSeqIdToValue.foldLeft(startFR)(f))}.toMap
  }

  /**
   * takes an iterator of (Long, Double) and applies the function f in a fold left manner to List[Double] that contains all Double values for one Long value.
   * @param it
   * @param f
   * @param startB
   * @return
   */
  def typedMapping(it: Iterator[(Long, Double)], f: ((Double, Double), ((Long, Double))) => (Double, Double), startB: (Double, Double)): Map[Long, (Double, Double)] = {
    it.toIndexedSeq.groupBy(_._1).map { case (k, v) => (k,v.foldLeft(startB)(f))}.toMap
  }

  def createMap[ID, V, FR](it: Seq[String], f: String => (ID, V)) = (foldFct:(FR, (ID, V)) => FR, foldStart: FR) => {
    val transformedIt = transform(it, f).toIterator
    mapFromIndexedSeq2(transformedIt, foldFct, foldStart)
  }
}
