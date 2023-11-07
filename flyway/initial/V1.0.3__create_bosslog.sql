CREATE TABLE IF NOT EXISTS `bosslog_daily`
(
    `id`          INT(11)                                                   NOT NULL AUTO_INCREMENT,
    `characterid` INT(11)                                                   NOT NULL,
    `bosstype`    ENUM ('ZAKUM','HORNTAIL','PINKBEAN','SCARGA','PAPULATUS') NOT NULL,
    `attempttime` TIMESTAMP                                                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `bosslog_weekly`
(
    `id`          INT(11)                                                   NOT NULL AUTO_INCREMENT,
    `characterid` INT(11)                                                   NOT NULL,
    `bosstype`    ENUM ('ZAKUM','HORNTAIL','PINKBEAN','SCARGA','PAPULATUS') NOT NULL,
    `attempttime` TIMESTAMP                                                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;