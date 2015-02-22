package util

import java.io.{File, PrintWriter}

/**
 * Typical workflows provided by this object.
 */
object General {

  def printAndGetInfoBoutNoOfLines(s: String): Int = {
    val numberOfLines = getLinesFor(s).length
    println(s"About to process $numberOfLines lines.")
    numberOfLines
  }
  /**
   * transform an iterator of Strings into another iterator of strings
   *
   * @param it
   * @param f_transformation
   * @return
   */
  def transform[A](it: Iterator[String], f_transformation: String => A): Iterator[A] = it.map(f_transformation)

  /**
   * transform an seq of Strings into another iterator of strings
   *
   * @param it
   * @param f_transformation
   * @return
   */
  def transform[A](it: Seq[String], f_transformation: String => A): Seq[A] = it.map(f_transformation)
  /**
   * Given a file name returns the iterator over lines of this file
   * @param file
   * @return
   */
  def getLinesFor(file: String) = scala.io.Source.fromFile(file).getLines

  /**
   * given a filename writes an iterator of strings into this file.
   * @param outputFile
   * @param it
   */
  def write(outputFile: String, it: Iterator[String], firstLine: String, fileLength: Int) = {
    val outfile = new File(outputFile)
    val p = new PrintWriter(outfile)
    var counter: Double = 0

    p.write(firstLine + "\n")

    watchedIteration(it, (s:String) => p.write(s + "\n"), fileLength)

    p.close()
  }

  def watchedIteration[A](it: Iterator[A], repeat: A => Unit, size: Double) = {
    var counter = 0

    for (element <- it) {
      if (counter % (size/100) == 0)
        println((counter/size)*100 + " Prozent verarbeitet.")
      repeat(element)
      counter += 1
    }
  }

  def processWatched[A](block: => A, size: Int) = {
    val t0 = System.currentTimeMillis
    val result = block
    println("Block took %d milliseconds".format((System.currentTimeMillis - t0)))
    result
  }

  /**
   * returns a function, that can collect the nth element from a given string.
   * @param n
   * @return
   */
  def getNthElementInLine(n: Int) = (line: String) => splitCsvLine(line)(n)

  /**
   * Create Array of an string, splitted by ";"
   * @param s
   * @return
   */
  def splitCsvLine(s: String) = s.split(";")

  def createMapForLookUp(idPos: Int, valuePos: Int, it: Iterator[String]) = {
    def getIdElement = getNthElementInLine(idPos)
    def getValueElement = getNthElementInLine(valuePos)

    it.map(s => (getIdElement(s), getValueElement(s))).toMap
  }

  /**
   * Given a block to be executed prints the time this block took to be executed.
   * @param block
   * @param blockName
   *                   a meaningful name of the block.
   * @tparam A
   * @return
   */
  def timed[A](block: => A, blockName: String) = {
    val t0 = System.currentTimeMillis
    val result = block
    println("Block " + blockName+ " took %d milliseconds".format((System.currentTimeMillis - t0)))
    result
  }


  def dropColumnsBut(keepIndices: List[Int]) = (s: String) => toCsvString(keepIndices.map(index => s.split(";")(index)))

  def dropFirstN(n: Int) = (s: String) => toCsvString(s.split(";").drop(n))

  def toCsvString(seq: Seq[String]) = seq.foldLeft("")((a, b) => a + ";" + b ).drop(1)
}
