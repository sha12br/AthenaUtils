package com.datahouse.awsservices

import com.datahouse.awsservices.athena.ReadSqlFile.readSqlFile
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.{ArgumentParser, ArgumentParserException, Namespace}

class Init(args: Array[String]) {

  private val namespace: Namespace = parseArguments (args)
  val sqlType: String = namespace.get ("sqlType")
  private val filePath: String = namespace.get ("filePath")
  val sqlQueries: Array[String] = readSqlFile (filePath)
  val awsProfile: String = namespace.get ("awsProfile")
  val awsRegion: String = namespace.get ("awsRegion")
  val defaultDatabase: String = if (namespace.get ("defaultDatabase") != null) {namespace.get ("defaultDatabase")} else {"sbx_shabeer"}
  val athenaOutputLocation: String = namespace.get ("athenaOutputLocation")
  val queryPollInMs: Long = if (namespace.get ("defaultDatabase") != null) {namespace.get ("queryPollInMs").toString.toLong} else {5000}

  private def parseArguments(args: Array[String]): Namespace = {
    val parser: ArgumentParser = ArgumentParsers.newFor("AthenaConnect").build()
    parser.addArgument("--sql-type").dest("sqlType").required(true).help("Type of sql --> DDL or SQL")
    parser.addArgument("--file-path").dest("filePath").required(true).help("Full path of DDL or SQL file")
    parser.addArgument("--aws-profile").dest("awsProfile").required(true).help("AWS_PROFILE to be used")
    parser.addArgument("--aws-region").dest("awsRegion").required(true).help("AWS_REGION to be used")
    parser.addArgument("--default-database").dest("defaultDatabase").help("Athena Default Database to be used, Default is sbx_shabeer")
    parser.addArgument("--athena-output-location").dest("athenaOutputLocation").required(true).help("S3 location to be used to stage Query results")
    parser.addArgument("--query-poll-in-ms").dest("queryPollInMs").help("Query to Poll in MS, Default is 5000 MS")

    try {
      parser.parseArgs(args)
    } catch {
      case e: ArgumentParserException =>
        print(s"Error:${e.getMessage}\n Usage:\n")
        parser.printUsage
        null
    }
  }
}