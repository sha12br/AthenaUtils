
--CREATE EXTERNAL TABLE `db_name.table_name_A`(
--  `colA` string,
--  `colB` string,
--  `colC` string,
--  `colD` string,
--  `colE` string)
--ROW FORMAT SERDE
--  'org.openx.data.jsonserde.JsonSerDe'
--WITH SERDEPROPERTIES (
--  'ignore.malformed.json'='true')
--STORED AS INPUTFORMAT
--  'org.apache.hadoop.mapred.TextInputFormat'
--OUTPUTFORMAT
--  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
--LOCATION
--  's3://<bucket_name>/stage/table_name_A';

CREATE EXTERNAL TABLE `db_name.table_name_B`(
  `colA` string,
  `colB` string,
  `colC` string,
  `colD` string,
  `colE` string)
ROW FORMAT SERDE
  'org.openx.data.jsonserde.JsonSerDe'
WITH SERDEPROPERTIES (
  'ignore.malformed.json'='true')
STORED AS INPUTFORMAT
  'org.apache.hadoop.mapred.TextInputFormat'
OUTPUTFORMAT
  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION
  's3://<bucket_name>/stage/table_name_B';
