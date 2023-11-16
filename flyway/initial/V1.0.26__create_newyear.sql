CREATE TABLE IF NOT EXISTS `newyear`
(
    `id`              INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
    `senderid`        INT(10)             NOT NULL DEFAULT '-1',
    `sendername`      VARCHAR(13)                  DEFAULT '',
    `receiverid`      INT(10)             NOT NULL DEFAULT '-1',
    `receivername`    VARCHAR(13)                  DEFAULT '',
    `message`         VARCHAR(120)                 DEFAULT '',
    `senderdiscard`   TINYINT(1)          NOT NULL DEFAULT '0',
    `receiverdiscard` TINYINT(1)          NOT NULL DEFAULT '0',
    `received`        TINYINT(1)          NOT NULL DEFAULT '0',
    `timesent`        BIGINT(20) UNSIGNED NOT NULL,
    `timereceived`    BIGINT(20) UNSIGNED NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;