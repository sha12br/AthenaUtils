package com.datahouse.awsservices.athena

import com.datahouse.awsservices.Init
import com.datahouse.awsservices.athena.ReadSqlFile.getTableNameFromSql
import com.datahouse.awsservices.glue.GlueClientFactory
import com.datahouse.awsservices.glue.GlueUtils.tableExistsInGlueCatalog
import software.amazon.awssdk.services.athena.AthenaClient
import software.amazon.awssdk.services.glue.GlueClient


class AthenaExecution(initArgs: Init) {

  private val athenaClient: AthenaClient = AthenaClientFactory(initArgs.awsRegion, initArgs.awsProfile).athenaClient
  private val glueClient: GlueClient = GlueClientFactory(initArgs.awsRegion, initArgs.awsProfile).glueClient

  def start = {
    for (sql <- initArgs.sqlQueries) {
      val tableName: String = getTableNameFromSql(sql)
      val tableExistsInCatalog: Boolean = {
        val resTblName: Array[String] = tableName.split("\\.")
        tableExistsInGlueCatalog(glueClient, dbName = resTblName(0), tblName = resTblName(1))
      }

      if (!tableExistsInCatalog) {
        val isQueryStillRunning: Option[Boolean] = AthenaConnect(athenaClient, sql, initArgs.defaultDatabase, tableName: String,
          initArgs.athenaOutputLocation: String, initArgs.queryPollInMs).apply
        if (!isQueryStillRunning.get) {
          println("Table : " + tableName + " successfully created")
        } else {
          println("Something went wrong while creating the Table -> " + tableName)
        }
      } else if (sql.contains("-- CREATE TABLE")) {
        println("Table : " + tableName + " Commented out, Hence Skipping the Table")
      } else {
        println("Table : " + tableName + " Exists in the AWS Glue Catalog")
      }
      println("-----------------------------------------------------------------")


      athenaClient.close()
    }
  }
}
