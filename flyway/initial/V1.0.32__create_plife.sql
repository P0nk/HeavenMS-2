CREATE TABLE IF NOT EXISTS `plife`
(
    `id`      INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `world`   INT(11)          NOT NULL DEFAULT '-1',
    `map`     INT(11)          NOT NULL DEFAULT '0',
    `life`    INT(11)          NOT NULL DEFAULT '0',
    `type`    VARCHAR(1)       NOT NULL DEFAULT 'n',
    `cy`      INT(11)          NOT NULL DEFAULT '0',
    `f`       INT(11)          NOT NULL DEFAULT '0',
    `fh`      INT(11)          NOT NULL DEFAULT '0',
    `rx0`     INT(11)          NOT NULL DEFAULT '0',
    `rx1`     INT(11)          NOT NULL DEFAULT '0',
    `x`       INT(11)          NOT NULL DEFAULT '0',
    `y`       INT(11)          NOT NULL DEFAULT '0',
    `hide`    INT(11)          NOT NULL DEFAULT '0',
    `mobtime` INT(11)          NOT NULL DEFAULT '0',
    `team`    INT(11)          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;