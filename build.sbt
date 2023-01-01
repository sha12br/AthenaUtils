ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.14"

lazy val root = (project in file("."))
  .settings(
    name := "AthenaUtils"
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.first
  case x =>         val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)     }


libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "auth" % "2.17.285",
  "software.amazon.awssdk" % "regions" % "2.17.285",
  "software.amazon.awssdk" % "athena" % "2.17.285",
  "software.amazon.awssdk" % "sts" % "2.17.285",
  "software.amazon.awssdk" % "glue" % "2.17.285",
  "net.sourceforge.argparse4j" % "argparse4j" % "0.9.0",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.9.1" % Test,
  "org.mockito" % "mockito-core" % "4.9.0" % Test


)