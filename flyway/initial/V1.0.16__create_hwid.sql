CREATE TABLE IF NOT EXISTS `hwidaccounts`
(
    `accountid` INT(11)     NOT NULL DEFAULT '0',
    `hwid`      VARCHAR(40) NOT NULL DEFAULT '',
    `relevance` TINYINT(2)  NOT NULL DEFAULT '0',
    `expiresat` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`accountid`, `hwid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `hwidbans`
(
    `hwidbanid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `hwid`      VARCHAR(30)      NOT NULL,
    PRIMARY KEY (`hwidbanid`),
    UNIQUE KEY `hwid_2` (`hwid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;