package no.beat

import javax.xml.datatype.{DatatypeFactory, Duration}

import scala.xml.{Elem, XML}

object Reader {
  def readFileAsAudioParts(pathToFile: String): List[AudioPart] = {
    val xml: Elem = XML.loadFile(pathToFile)

    transformToAudioParts(transformXm(xml).toList)
  }

  def transformXm(xml: Elem): Seq[Input] = {
    (xml \\ "silence")
      .map(
        node =>
          Input(
            node.attribute("from").get.toString(),
            node.attribute("until").get.toString()
          )
      )
  }

  def transformToAudioParts(silences: List[Input]): List[AudioPart] = {
    val firstChapter: Duration = toDuration("PT0S")

    def run(silences: List[Input], acc: List[AudioPart]): List[AudioPart] =
      silences match {
        case head :: next :: tail =>
          run(
            next :: tail,
            acc ::: List(
              Silence(toDuration(head.from), toDuration(head.until)),
              Audio(toDuration(head.until), toDuration(next.from))
            )
          )
        case head :: Nil =>
          acc :+ Silence(toDuration(head.from), toDuration(head.until))
        case Nil => acc
      }

    val result = run(silences, List())

    result.headOption match {
      case Some(value) if value.startTime.isLongerThan(firstChapter) =>
        Audio(firstChapter, value.startTime) :: result
      case None => result
    }
  }

  def toDuration(duration: String): Duration =
    DatatypeFactory.newInstance.newDuration(duration)
}
