CREATE TABLE IF NOT EXISTS `bbs_replies`
(
    `replyid`   INT(10) unsigned    NOT NULL AUTO_INCREMENT,
    `threadid`  INT(10) unsigned    NOT NULL,
    `postercid` INT(10) unsigned    NOT NULL,
    `TIMESTAMP` BIGINT(20) unsigned NOT NULL,
    `content`   VARCHAR(26)         NOT NULL DEFAULT '',
    PRIMARY KEY (`replyid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `bbs_threads`
(
    `threadid`      INT(10) unsigned     NOT NULL AUTO_INCREMENT,
    `postercid`     INT(10) unsigned     NOT NULL,
    `name`          VARCHAR(26)          NOT NULL DEFAULT '',
    `TIMESTAMP`     BIGINT(20) unsigned  NOT NULL,
    `icon`          SMALLINT(5) unsigned NOT NULL,
    `replycount`    SMALLINT(5) unsigned NOT NULL DEFAULT '0',
    `startpost`     TEXT                 NOT NULL,
    `guildid`       INT(10) unsigned     NOT NULL,
    `localthreadid` INT(10) unsigned     NOT NULL,
    PRIMARY KEY (`threadid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;