# AthenaUtils


#### Steps to Package a JAR

###### - Clone the repo.
###### - From the root of the project, run the cmd --> "sbt clean assembly". This will create a JAR(Dependencies bundled) under target/scala-2.12/


#### Running the jar

###### $ java -cp <path_to_jar> com.datahouse.awsservices.Main <args>
###### <args>: Usage --sql-type SQLTYPE --file-path FILEPATH
                     --aws-profile AWSPROFILE --aws-region AWSREGION
                     [--default-database DEFAULTDATABASE; default is sbx_shabeer(Should be present in AWS GlueCatalog)]
                     --athena-output-location ATHENAOUTPUTLOCATION
                     [--query-poll-in-ms QUERYPOLLINMS; default is 5000]
