CREATE TABLE IF NOT EXISTS `eventstats`
(
    `characterid` INT(11) UNSIGNED NOT NULL,
    `name`        VARCHAR(11)      NOT NULL DEFAULT '0' COMMENT '0',
    `info`        INT(11)          NOT NULL,
    PRIMARY KEY (`characterid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;