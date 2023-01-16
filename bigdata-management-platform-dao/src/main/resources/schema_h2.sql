-- ----------------------------
-- models for catalog
-- ----------------------------

DROP TABLE IF EXISTS `catalog_collection`;
CREATE TABLE `catalog_collection`
(
    `id`          INT(20)      NOT NULL AUTO_INCREMENT,
    `create_time` DATETIME      DEFAULT NULL,
    `update_time` DATETIME      DEFAULT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `catalog_datasource`;
CREATE TABLE `catalog_datasource`
(
    `id`                INT(20)      NOT NULL AUTO_INCREMENT,
    `create_time`       DATETIME      DEFAULT NULL,
    `update_time`       DATETIME      DEFAULT NULL,
    `name`              VARCHAR(255) NOT NULL,
    `description`       VARCHAR(2048) DEFAULT NULL,
    `status`            TINYINT(4)   NOT NULL COMMENT '1 active, 2 hibernating, see com.bmp.dao.Status',
    `type`              TINYINT(4)   NOT NULL COMMENT '1 mysql, see com.bmp.dao.Datasource.Type',
    `connection_info`   TEXT         NOT NULL,
    `collection_id`     INT(20)      NOT NULL,

    `sync_paths`        TEXT          DEFAULT NULL,
    `sync_execute_time` DATETIME      DEFAULT NULL,
    `sync_interval`     BIGINT(20)    DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `catalog_asset`;
CREATE TABLE `catalog_asset`
(
    `id`            INT(20)      NOT NULL AUTO_INCREMENT,
    `create_time`   DATETIME      DEFAULT NULL,
    `update_time`   DATETIME      DEFAULT NULL,
    `name`          VARCHAR(255) NOT NULL,
    `description`   VARCHAR(2048) DEFAULT NULL,
    `parent_id`     INT(20)      NOT NULL,
    `datasource_id` INT(20)      NOT NULL,
    `type`          TINYINT(4)   NOT NULL COMMENT '1 database, 2 table, 3 fileset, see com.bmp.commons.enums.AssetType',
    `path`          VARCHAR(500) NOT NULL,
    `asset_path`    TEXT         NOT NULL,
    `file_type`     TINYINT(4)   NOT NULL COMMENT '0 no, 1 csv, 2 json, 3 parquet, 4 orc, see com.bmp.commons.enums.FileType',
    `comment`       VARCHAR(2048) DEFAULT NULL,
    `details`       TEXT          DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `catalog_column`;
CREATE TABLE `catalog_column`
(
    `id`            INT(20)       NOT NULL AUTO_INCREMENT,
    `create_time`   DATETIME      DEFAULT NULL,
    `update_time`   DATETIME      DEFAULT NULL,
    `name`          VARCHAR(255)  NOT NULL,
    `description`   VARCHAR(2048) DEFAULT NULL,
    `asset_id`      INT(20)       NOT NULL,
    `type`          VARCHAR(2048) NOT NULL,
    `comment`       VARCHAR(2048) DEFAULT NULL,
    `default_value` VARCHAR(2048) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

-- ----------------------------
-- models for label
-- ----------------------------

DROP TABLE IF EXISTS `catalog_tag`;
CREATE TABLE `catalog_tag`
(
    `id`          INT(20)      NOT NULL AUTO_INCREMENT,
    `create_time` DATETIME      DEFAULT NULL,
    `update_time` DATETIME      DEFAULT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `description` VARCHAR(2048) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `catalog_relation_tag_subject`;
CREATE TABLE `catalog_relation_tag_subject`
(
    `id`           INT(20)    NOT NULL AUTO_INCREMENT,
    `create_time`  DATETIME DEFAULT NULL,
    `update_time`  DATETIME DEFAULT NULL,
    `tag_id`       INT(20)    NOT NULL,
    `subject_id`   INT(20)    NOT NULL,
    `subject_type` TINYINT(4) NOT NULL COMMENT '1 collection, 2 datasource, 3 asset, 4 column, see com.bmp.commons.enums.SubjectType',
    PRIMARY KEY (`id`)
);
