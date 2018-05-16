name := "sys-emu"

organization := "info.osdevelopment"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe" % "config" % "1.3.3"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.1"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12"
libraryDependencies += "commons-cli" % "commons-cli" % "1.4"
libraryDependencies += "org.slf4j" % "slf4j-jdk14" % "1.7.25"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % "test"
libraryDependencies += "org.specs2" %% "specs2-core" % "4.2.0" % "test"
libraryDependencies += "org.specs2" %% "specs2-mock" % "4.2.0" % "test"

coverageEnabled := true

scalacOptions += "-feature"

mappings in makeSite ++= Seq(
  file("LICENSE") -> "LICENSE",
)

includeFilter in makeSite := ((includeFilter in makeSite).value || "*.md")

enablePlugins(SiteScaladocPlugin)
