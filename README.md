[![codecov](https://codecov.io/gh/black-06/bigdata-management-platform/branch/master/graph/badge.svg?token=YPNB7YENRA)](https://codecov.io/gh/black-06/bigdata-management-platform)

# Environment: jdk 8 and maven 3.6+

```shell
apt install openjdk-8-jdk
apt install maven
```

# Test

```shell
mvn clean test
```

# Installation

1. Start MySQL and create database

```shell
docker run -name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:5.6
docker exec mysql mysql -u root -proot CREATE DATABASE `bmp` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
```

2. Build jar

```shell
mvn clean package
```

3. Start server

```shell
java -jar ./bigdata-management-platform-catalog/target/bigdata-management-platform-catalog-1.0-SNAPSHOT.jar
```