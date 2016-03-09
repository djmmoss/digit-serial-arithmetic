val chiselVersion = System.getProperty("chiselVersion", "latest.release")

lazy val dsaSettings = Seq(
    organization := "usyd.edu.au",

    version := "0.1",

    name := "chisel-digit-serial-arithmetic",

    scalaVersion := "2.11.7",

    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-language:reflectiveCalls"),
    resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    parallelExecution in Test := false,
    libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.1",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.7"
)

lazy val chisel = RootProject(uri("git://github.com/djmmoss/chisel.git"))

lazy val root = (project in file(".")).settings(dsaSettings:_*).dependsOn(chisel)
