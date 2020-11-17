package no.beat

import javax.xml.datatype.Duration

abstract class AudioPart {
  def startTime: Duration
  def endTime: Duration

  def duration(): Duration =
    endTime.subtract(startTime)
}

case class Audio(startTime: Duration, endTime: Duration) extends AudioPart
case class Silence(startTime: Duration, endTime: Duration) extends AudioPart
