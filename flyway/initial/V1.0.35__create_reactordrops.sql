CREATE TABLE IF NOT EXISTS `reactordrops`
(
    `reactordropid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `reactorid`     INT(11)          NOT NULL,
    `itemid`        INT(11)          NOT NULL,
    `chance`        INT(11)          NOT NULL,
    `questid`       INT(5)           NOT NULL DEFAULT '-1',
    PRIMARY KEY (`reactordropid`),
    KEY `reactorid` (`reactorid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  PACK_KEYS = 1
  AUTO_INCREMENT = 841;