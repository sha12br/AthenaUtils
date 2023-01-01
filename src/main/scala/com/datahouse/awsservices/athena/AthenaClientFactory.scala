package com.datahouse.awsservices.athena

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.athena.AthenaClient

private case class AthenaClientFactory(awsRegion: String, awsProfile: String) {

  val athenaClient: AthenaClient = AthenaClient.builder()
      .region(Region.of(awsRegion))
      .credentialsProvider(DefaultCredentialsProvider.builder().profileName(awsProfile).build())
      .build()
}
