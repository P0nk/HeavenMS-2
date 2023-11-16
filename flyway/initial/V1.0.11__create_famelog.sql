CREATE TABLE IF NOT EXISTS `famelog`
(
    `famelogid`      INT(11)   NOT NULL AUTO_INCREMENT,
    `characterid`    INT(11)   NOT NULL DEFAULT '0',
    `characterid_to` INT(11)   NOT NULL DEFAULT '0',
    `when`           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`famelogid`),
    KEY `characterid` (`characterid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;