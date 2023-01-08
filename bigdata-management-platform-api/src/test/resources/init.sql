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