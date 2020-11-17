package no.beat

import javax.xml.datatype.Duration

object SplitService {

  def splitInputInToChapters(
    audioParts: List[AudioPart],
    chapterSplitDur: Duration,
    chapterPartSplitDur: Duration,
    maxSegmentDuration: Duration): List[List[Audio]] = {

    val splitByChapters: List[List[AudioPart]] = splitAudioByDuration(audioParts, chapterSplitDur)

    splitByChapters.map{ chapter =>
      if(chapter.last.endTime.subtract(chapter.head.startTime).isLongerThan(maxSegmentDuration)) {
        splitAudioByDuration(chapter, chapterPartSplitDur)
          .map(e => Audio(e.head.startTime, e.last.endTime)
          )
      } else List(Audio(chapter.head.startTime, chapter.last.endTime))
    }
  }

  def splitAudioByDuration(input: List[AudioPart], splitDuration: Duration): List[List[AudioPart]] = {
    def run(audioParts: List[AudioPart], acc: List[AudioPart], result: List[List[AudioPart]]): List[List[AudioPart]] =
      audioParts match {
        case (audio: Audio) :: Nil => result.appended(acc.appended(audio))
        case (audio: Audio) :: tail => run(tail, acc.appended(audio), result)
        case (silence: Silence) :: tail =>
          if (!silence.duration().isShorterThan(splitDuration)) {
            run(tail, List(), result.appended(acc))
          }
          else run(tail, acc.appended(silence), result)
        case _ => result.appended(acc)
      }

    run(input, List(), List())
  }

}
