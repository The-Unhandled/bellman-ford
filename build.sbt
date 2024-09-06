ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

val circeVersion = "0.14.7"
val scalaTestVersion = "3.2.18"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

lazy val root = (project in file("."))
  .settings(
    name := "swissborg-challenge"
  )
