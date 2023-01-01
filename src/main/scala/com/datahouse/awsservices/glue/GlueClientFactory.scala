package com.datahouse.awsservices.glue

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.glue.GlueClient

case class GlueClientFactory(awsRegion: String, awsProfile: String) {

  final val glueClient: GlueClient = GlueClient.builder()
    .region(Region.of(awsRegion))
    .credentialsProvider(DefaultCredentialsProvider.builder().profileName(awsProfile).build())
    .build()
}
