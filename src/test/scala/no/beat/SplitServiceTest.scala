package no.beat

import javax.xml.datatype.DatatypeFactory
import no.beat.Reader.toDuration
import org.scalatest.flatspec.AnyFlatSpec

class SplitServiceTest extends AnyFlatSpec {
  val durationFactory = DatatypeFactory.newInstance()

  val input = List(
    Audio(toDuration("PT0S"), toDuration("PT30S")),
    Silence(toDuration("PT30S"), toDuration("PT33S")),

    Audio(toDuration("PT33S"), toDuration("PT50S")),
    Silence(toDuration("PT50S"), toDuration("PT52S")),
    Audio(toDuration("PT52S"), toDuration("PT1M20S")),
    Silence(toDuration("PT1M20S"), toDuration("PT1M25S")),

    Audio(toDuration("PT1M25S"), toDuration("PT3M55S")),
  )

  "splitAudioByDuration" should "return parts of input divided by duration" in {
      val result = SplitService
        .splitAudioByDuration(input, durationFactory.newDuration(3000L))

      assert(result.size == 3)
      assert(result.head.size == 1)
      assert(result(1).size == 3)
      assert(result(2).size == 1)
  }


  "splitInputInToChapters" should "return parts of input divided by duration" in {
    val result = SplitService
      .splitInputInToChapters(
        input,
        durationFactory.newDuration(3000L),
        durationFactory.newDuration(2000L),
        durationFactory.newDuration(6000L)
      )

    assert(result.size == 3)

    assert(result.head.size == 1)
    assert(result(1).size == 2)
    assert(result(2).size == 1)
  }

}
