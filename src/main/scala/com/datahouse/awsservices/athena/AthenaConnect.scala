package com.datahouse.awsservices.athena

import software.amazon.awssdk.services.athena.AthenaClient
import software.amazon.awssdk.services.athena.model._
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable

import java.util
import scala.collection.JavaConversions._

case class AthenaConnect(athenaClient: AthenaClient, sqlStatement: String, defaultDatabase: String,
                         tableName: String, athenaOutputLocation: String, queryPollInMs: Long) {
  def apply: Option[Boolean] = {
    try {
      val queryExecutionContext: QueryExecutionContext = QueryExecutionContext.builder()
        .database(defaultDatabase)
        .build()

      val resultConfiguration: ResultConfiguration = ResultConfiguration.builder()
        .outputLocation(athenaOutputLocation).build()

      val startQueryExecutionRequest: StartQueryExecutionRequest = StartQueryExecutionRequest.builder()
        .queryString(sqlStatement)
        .queryExecutionContext(queryExecutionContext)
        .resultConfiguration(resultConfiguration)
        .build()

      val startQueryExecutionResponse: StartQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest)

      val isQueryStillRunning: Boolean = pollAthenaQueryExecution(athenaClient, startQueryExecutionResponse.queryExecutionId(),
        tableName, queryPollInMs)
      Option(isQueryStillRunning)
    } catch {
      case e: AthenaException => {
        e.printStackTrace();
        None
      }
    }
  }

  private def getQueryResultsRequest(queryExecutionId: String): GetQueryResultsRequest = {
    GetQueryResultsRequest.builder()
      .queryExecutionId(queryExecutionId)
      .build()
  }

  private def getQueryExecutionRequest(queryExecutionId: String): GetQueryExecutionRequest = {
    GetQueryExecutionRequest.builder()
      .queryExecutionId(queryExecutionId)
      .build();
  }

  private def pollAthenaQueryExecution(athenaClient: AthenaClient, queryExecutionId: String, tableName: String,
                                       queryPollInMs: Long): Boolean = {

    var isQueryStillRunning = true
    try {
      while (isQueryStillRunning) {
        val getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest(queryExecutionId))
        val queryState = getQueryExecutionResponse.queryExecution.status.state.toString
        if (queryState == QueryExecutionState.FAILED.toString) throw new RuntimeException("The Amazon Athena query failed to run with error message: " + getQueryExecutionResponse.queryExecution.status.stateChangeReason)
        else if (queryState == QueryExecutionState.CANCELLED.toString) throw new RuntimeException("The Amazon Athena query was cancelled.")
        else if (queryState == QueryExecutionState.SUCCEEDED.toString) isQueryStillRunning = false
        else {
          // Sleep an amount of time before retrying again.
          Thread.sleep(queryPollInMs)
        }
        System.out.println("Table : " + tableName + " status is : " + queryState)
      }
    } catch {
      case e: AthenaException => {
        e.printStackTrace();
        System.exit(1)
      }
    }
    isQueryStillRunning
  }

  private def processResultRows(athenaClient: AthenaClient, queryExecutionId: String): Unit = {

    val getQueryResultsResults: GetQueryResultsIterable = athenaClient.getQueryResultsPaginator(getQueryResultsRequest(queryExecutionId))

    for (result: GetQueryResultsResponse <- getQueryResultsResults) {
      val columnInfoList: util.List[ColumnInfo] = result.resultSet().resultSetMetadata().columnInfo()
      val results: util.List[Row] = result.resultSet().rows()
      processRow(results, columnInfoList)
    }

  }

  private def processRow(row: util.List[Row], columnInfoList: util.List[ColumnInfo]): Unit = {
    for (myRow: Row <- row) {
      val allData: util.List[Datum] = myRow.data()
      for (data: Datum <- allData) {
        println("The value of the column is " + data.varCharValue())
      }
    }
  }
}
