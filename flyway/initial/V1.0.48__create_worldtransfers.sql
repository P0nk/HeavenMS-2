CREATE TABLE IF NOT EXISTS `worldtransfers`
(
    `id`             INT(11)    NOT NULL AUTO_INCREMENT,
    `characterid`    INT(11)    NOT NULL,
    `from`           TINYINT(3) NOT NULL,
    `to`             TINYINT(3) NOT NULL,
    `requestTime`    TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `completionTime` TIMESTAMP  NULL,
    PRIMARY KEY (`id`),
    INDEX (characterid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;