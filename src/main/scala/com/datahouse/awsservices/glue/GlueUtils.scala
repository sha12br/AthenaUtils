package com.datahouse.awsservices.glue

import software.amazon.awssdk.services.glue.GlueClient
import software.amazon.awssdk.services.glue.model.{EntityNotFoundException, GetTableRequest}


object GlueUtils {

  def tableExistsInGlueCatalog(glueClient: GlueClient, dbName: String, tblName: String): Boolean = {
    var doesExist = true
    try {
      glueClient.getTable(GetTableRequest.builder()
        .databaseName(dbName)
        .name(tblName).build())
    } catch {
      case e: EntityNotFoundException => {
        doesExist = false
      }
    }
    doesExist
  }

}
