CREATE TABLE IF NOT EXISTS default.inventory
(
    item_id INT,
    amount  INT
) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    STORED AS
        INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION 'hdfs://namenode:8020/user/hive/warehouse/inventory';

CREATE TABLE IF NOT EXISTS default.users
(
    id     INT,
    gender INT
)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    STORED AS
        INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION 'hdfs://namenode:8020/user/hive/warehouse/users';

CREATE TABLE IF NOT EXISTS default.cpus
(
    id    INT,
    color STRING,
    price INT
)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    STORED AS
        INPUTFORMAT 'org.apache.hadoop.mapred.TextInputFormat'
        OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION 'hdfs://namenode:8020/user/hive/warehouse/cpus';