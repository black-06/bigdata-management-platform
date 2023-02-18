[![codecov](https://codecov.io/gh/black-06/bigdata-management-platform/branch/master/graph/badge.svg?token=YPNB7YENRA)](https://codecov.io/gh/black-06/bigdata-management-platform)

# Quick Start

## Environment: jdk 8 and maven 3.6+

```shell
apt install openjdk-8-jdk
apt install maven
```

## Test

```shell
mvn clean test
```

## Installation

1. Download/Install Docker

```shell
curl -fsSL https://get.docker.com | bash -s docker
```

2. Start MySQL and create database

```shell
docker run -itd --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0
docker exec mysql mysql -u root -proot -e "CREATE DATABASE bmp CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';"
```

3. Start Redis

```shell
docker run -itd --name redis -p 6379:6379 redis:7.0
```

4. Build jar

```shell
mvn clean package
```

## Start

### Only Start catalog Server

```shell
java -jar ./bmp-catalog/target/bmp-catalog-1.0-SNAPSHOT-exec.jar
```

Or debug entry point
is [CatalogApplication.java](./bmp-catalog/src/main/java/com/bmp/catalog/CatalogApplication.java)

### Start Standalone Server

```shell
java -jar ./bmp-standalone-server/target/bmp-standalone-server-1.0-SNAPSHOT-exec.jar
```

## Demo

1. start a HIVE datasource

```shell
docker-compose -f ./demo/hive_datasource/docker-compose.yml -p data up -d
```

2. create HIVE table

```shell
docker exec data-hive-server-1 hive -f /tables.sql
```

3. Request [demo.http](./demo/demo.http)
   with [VS Code REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
   or [JetBrains HTTP Client Editor](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html)

4. stop HIVE datasource

```shell
docker-compose -f ./demo/hive_datasource/docker-compose.yml -p data down
```

## How to check docker container

```shell
docker exec -it mysql mysql -u root -proot
mysql> show tables from bmp;
+------------------------------+
| Tables_in_bmp                |
+------------------------------+
| catalog_asset                |
| catalog_collection           |
| catalog_column               |
| catalog_datasource           |
| catalog_relation_tag_subject |
| catalog_tag                  |
+------------------------------+
```

```shell
docker exec -it data-hive-server-1 hive
hive> show tables from default;
OK
cpus
inventory
users
Time taken: 0.524 seconds, Fetched: 3 row(s)
```