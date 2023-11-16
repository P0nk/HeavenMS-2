CREATE TABLE IF NOT EXISTS `medalmaps`
(
    `id`            INT(11)          NOT NULL AUTO_INCREMENT,
    `characterid`   INT(11)          NOT NULL,
    `queststatusid` INT(11) UNSIGNED NOT NULL,
    `mapid`         INT(11)          NOT NULL,
    PRIMARY KEY (`id`),
    KEY `queststatusid` (`queststatusid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;