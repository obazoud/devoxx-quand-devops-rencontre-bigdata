name := "spark-logs"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.3.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.3.0" % "provided"

libraryDependencies += "net.sf.uadetector" % "uadetector-resources" % "2014.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

fork := true
