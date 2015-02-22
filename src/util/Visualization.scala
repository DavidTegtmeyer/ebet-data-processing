package util

import breeze.linalg._
import breeze.plot._

/**
 * Created by david on 02.02.15.
 */
object Visualization {


  def showHistogram[A](map: Map[Long, A], filter: A => Boolean, computedOutput: A => Double) =
    showHist(_: String, _: String, _: String, _: String, map.valuesIterator.filter(filter).toIndexedSeq.map(computedOutput))


  def showHist(figureName: String, xLabel: String, yLabel: String, saveAs: String, values: IndexedSeq[Double]) = {
    val f = Figure(figureName)
    val p = f.subplot(0)
    val x = linspace(0.0,1.0)
    //    p += hist(stakeBins, 10000)

    val bins = values.length/10
    println("no of bins: " + bins)
    p += hist(values, bins)
    p.xlabel = xLabel
    p.ylabel = yLabel
    f.saveas(saveAs)
  }

}
