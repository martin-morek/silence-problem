package no.beat

import javax.xml.datatype.DatatypeFactory
import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization.write
object Processor {
  val usage = """
    Four arguments are required:
     - path to an XML file with silence intervals
     - silence duration in seconds which reliably indicates a chapter transition
     - maximum duration of a segment in seconds, after which the chapter will be broken up into multiple segments
     - silence duration in seconds which can be used to split a long chapter
  """

  def main(args: Array[String]): Unit = {
    if (args.length != 4) println(usage)
    else {
      val pathToFile = args(0)
      val chapterSilenceDuration = toDuration(args(1))
      val maxSegmentDuration = toDuration(args(2))
      val partSegmentDuration  = toDuration(args(3))

      val audioParts = Reader.readFileAsAudioParts(pathToFile)
      val splitResult = SplitService.splitInputInToChapters(
        audioParts,
        chapterSilenceDuration,
        partSegmentDuration,
        maxSegmentDuration)

      printAsJson(splitResult)
    }
  }

  def printAsJson(splitResult: List[List[Audio]]): Unit = {
    val result: PrintResult = PrintResult(transformToSegments(splitResult))

    implicit val formats = DefaultFormats
    val jsonString = write(result)
    println(jsonString)
  }

  def transformToSegments(chunks: List[List[Audio]]): List[Segment] =
  chunks.zip(LazyList from 1).flatMap { case (chapterParts, index) =>
    if (chapterParts.size == 1)
      List(Segment(s"Chapter $index", chapterParts.head.startTime.toString))
    else {
      chapterParts.zip(LazyList from 1).map { case (part, partIndex) =>
        Segment(s"Chapter $index, part $partIndex", part.startTime.toString)
      }
    }
  }

  private def toDuration(duration: String)= {
    val durationFactory = DatatypeFactory.newInstance()
    val durationAsInt = duration.toInt * 1000

    durationFactory.newDuration(durationAsInt)
  }

}
