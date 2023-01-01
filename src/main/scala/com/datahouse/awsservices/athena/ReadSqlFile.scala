package com.datahouse.awsservices.athena

import scala.io.BufferedSource
import scala.io.Codec.default
import scala.util.matching.Regex

object ReadSqlFile {

  def readSqlFile(file: String): Array[String] = {
    val source: BufferedSource = scala.io.Source.fromFile(file)
    val lines: String = try source.mkString finally source.close()
    val sqlStatements: Array[String] = lines.split(";")
    sqlStatements.filter(_.length > 10)
  }

  def getTableNameFromSql(sqlStr: String): String = {
    val keyValPattern: Regex = "`(.*?)`".r
    val tableName = keyValPattern.findFirstMatchIn(sqlStr).map(_.group(1)).getOrElse(default)
    if (tableName.toString != "UTF-8") {
      tableName.toString
    } else {
      ""
    }
  }

}
