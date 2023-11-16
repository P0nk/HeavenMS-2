CREATE TABLE IF NOT EXISTS `notes`
(
    `id`        INT(11)             NOT NULL AUTO_INCREMENT,
    `to`        VARCHAR(13)         NOT NULL DEFAULT '',
    `from`      VARCHAR(13)         NOT NULL DEFAULT '',
    `message`   TEXT                NOT NULL,
    `TIMESTAMP` BIGINT(20) UNSIGNED NOT NULL,
    `fame`      INT(11)             NOT NULL DEFAULT '0',
    `deleted`   INT(2)              NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;