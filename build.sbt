ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val echopraxiaVersion = "2.1.1"
val echopraxiaPlusScalaVersion = "1.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "scala-playground",
    //scalacOptions += "-Ymacro-debug-verbose",
    scalacOptions ++= Seq("-Xexperimental"),
    libraryDependencies += "com.tersesystems.echopraxia.plusscala" %% "logger" % echopraxiaPlusScalaVersion,
    libraryDependencies += "com.tersesystems.echopraxia.plusscala" %% "generic" % echopraxiaPlusScalaVersion,

    libraryDependencies += "com.tersesystems.echopraxia" % "logstash" % echopraxiaVersion,
  )
