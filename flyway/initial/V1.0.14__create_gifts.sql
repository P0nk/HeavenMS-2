CREATE TABLE IF NOT EXISTS `gifts`
(
    `id`      INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `to`      INT(11)          NOT NULL,
    `from`    VARCHAR(13)      NOT NULL,
    `message` TINYTEXT         NOT NULL,
    `sn`      INT(10) UNSIGNED NOT NULL,
    `ringid`  INT(10)          NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;