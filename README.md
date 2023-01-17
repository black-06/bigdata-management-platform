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

1. Start MySQL and create database

```shell
docker run -itd --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:5.7
docker exec mysql mysql -u root -proot -e "CREATE DATABASE bmp CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';"
```

2. Build jar

```shell
mvn clean package
```

## Start

```shell
java -jar ./bmp-catalog/target/bmp-catalog-1.0-SNAPSHOT.jar
```

Or debug entry point
is [Application.java](./bmp-catalog/src/main/java/com/bmp/catalog/Application.java)

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