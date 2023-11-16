CREATE TABLE IF NOT EXISTS `questactions`
(
    `questactionid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `questid`       INT(11)          NOT NULL DEFAULT '0',
    `status`        INT(11)          NOT NULL DEFAULT '0',
    `data`          BLOB             NOT NULL,
    PRIMARY KEY (`questactionid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `questprogress`
(
    `id`            INT(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `characterid`   INT(11)                                                      NOT NULL,
    `queststatusid` INT(10) UNSIGNED                                             NOT NULL DEFAULT '0',
    `progressid`    INT(11)                                                      NOT NULL DEFAULT '0',
    `progress`      VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `questrequirements`
(
    `questrequirementid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `questid`            INT(11)          NOT NULL DEFAULT '0',
    `status`             INT(11)          NOT NULL DEFAULT '0',
    `data`               BLOB             NOT NULL,
    PRIMARY KEY (`questrequirementid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `queststatus`
(
    `queststatusid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `characterid`   INT(11)          NOT NULL DEFAULT '0',
    `quest`         INT(11)          NOT NULL DEFAULT '0',
    `status`        INT(11)          NOT NULL DEFAULT '0',
    `time`          INT(11)          NOT NULL DEFAULT '0',
    `expires`       BIGINT(20)       NOT NULL DEFAULT '0',
    `forfeited`     INT(11)          NOT NULL DEFAULT '0',
    `completed`     INT(11)          NOT NULL DEFAULT '0',
    `info`          TINYINT(3)       NOT NULL DEFAULT '0',
    PRIMARY KEY (`queststatusid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;