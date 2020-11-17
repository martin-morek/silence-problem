package no.beat

import org.scalatest.flatspec.AnyFlatSpec

class ReaderTest extends AnyFlatSpec {
  val inputs = List(
    Input("PT3M43.306S", "PT3M46.225S"),
    Input("PT7M16.779S", "PT7M18.512S"),
    Input("PT10M57.769S", "PT10M58.38S"),
    Input("PT13M46.388S", "PT13M47.704S")
  )


  "transformToAudioParts" should "start with audio part if silence is not at the beginning" in {
    val result = Reader.transformToAudioParts(inputs)

    assert(result.head.isInstanceOf[Audio])
    assert(result.head.startTime == Reader.toDuration("PT0S"))
  }

  "transformToAudioParts" should "transform input into audio parts" in {
    val result = Reader.transformToAudioParts(inputs)

    assert(result.size == 8)
    assert(result.count(p => p.isInstanceOf[Audio]) == 4)
    assert(result.count(p => p.isInstanceOf[Silence]) == 4)
  }

  "transformToAudioParts" should "return empty list of audio parts if input is empty" in {
    val result = Reader.transformToAudioParts(List())
    assert(result.isEmpty)
  }
}
