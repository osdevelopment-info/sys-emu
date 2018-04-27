name := "x86-emulator"

organization := "info.osdevelopment"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.5"

libraryDependencies += "commons-cli" % "commons-cli" % "1.4"
libraryDependencies += "org.specs2" %% "specs2-core" % "4.1.0" % "test"

coverageEnabled := true

scalacOptions += "-feature"
