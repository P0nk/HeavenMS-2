CREATE TABLE IF NOT EXISTS `storages`
(
    `storageid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `accountid` INT(11)          NOT NULL DEFAULT '0',
    `world`     INT(2)           NOT NULL,
    `slots`     INT(11)          NOT NULL DEFAULT '0',
    `meso`      INT(11)          NOT NULL DEFAULT '0',
    PRIMARY KEY (`storageid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;