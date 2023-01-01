package com.datahouse.awsservices

import com.datahouse.awsservices.athena.AthenaExecution

object Main {

  def main(args: Array[String]): Unit = {
    val initArgs: Init = new Init(args)
    val athenaExecution: AthenaExecution = new AthenaExecution(initArgs)
    athenaExecution.start
  }
}
