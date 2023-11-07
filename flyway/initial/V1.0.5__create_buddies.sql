CREATE TABLE IF NOT EXISTS `buddies`
(
    `id`          INT(11)    NOT NULL AUTO_INCREMENT,
    `characterid` INT(11)    NOT NULL,
    `buddyid`     INT(11)    NOT NULL,
    `pending`     TINYINT(4) NOT NULL DEFAULT '0',
    `group`       VARCHAR(17)         DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;