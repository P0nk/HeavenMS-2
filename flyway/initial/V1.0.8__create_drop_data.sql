CREATE TABLE IF NOT EXISTS `drop_data`
(
    `id`               BIGINT(20) NOT NULL AUTO_INCREMENT,
    `dropperid`        INT(11)    NOT NULL,
    `itemid`           INT(11)    NOT NULL DEFAULT '0',
    `minimum_quantity` INT(11)    NOT NULL DEFAULT '1',
    `maximum_quantity` INT(11)    NOT NULL DEFAULT '1',
    `questid`          INT(11)    NOT NULL DEFAULT '0',
    `chance`           INT(11)    NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`dropperid`, `itemid`),
    KEY `mobid` (`dropperid`),
    INDEX (dropperid, itemid)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `drop_data_global`
(
    `id`               BIGINT(20) NOT NULL AUTO_INCREMENT,
    `continent`        TINYINT(1) NOT NULL DEFAULT '-1',
    `itemid`           INT(11)    NOT NULL DEFAULT '0',
    `minimum_quantity` INT(11)    NOT NULL DEFAULT '1',
    `maximum_quantity` INT(11)    NOT NULL DEFAULT '1',
    `questid`          INT(11)    NOT NULL DEFAULT '0',
    `chance`           INT(11)    NOT NULL DEFAULT '0',
    `comments`         VARCHAR(45)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `mobid` (`continent`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC
  AUTO_INCREMENT = 5;