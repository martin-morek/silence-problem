name := "silence-problem"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "2.0.0-M1",
  "net.liftweb" %% "lift-json" % "3.4.2",
  "org.scalatest" %% "scalatest-flatspec" % "3.2.0" % "test"
)

mainClass := Some("no.beat.Processor")