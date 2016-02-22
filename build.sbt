organization := "usyd.edu.au"

version := "0.1"

name := "chisel-digit-serial-arithmetic"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-language:reflectiveCalls")

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.1"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.6"
libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
