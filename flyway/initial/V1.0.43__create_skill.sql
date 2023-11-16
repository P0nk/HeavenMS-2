CREATE TABLE IF NOT EXISTS `skillmacros`
(
    `id`          INT(11)    NOT NULL AUTO_INCREMENT,
    `characterid` INT(11)    NOT NULL DEFAULT '0',
    `position`    TINYINT(1) NOT NULL DEFAULT '0',
    `skill1`      INT(11)    NOT NULL DEFAULT '0',
    `skill2`      INT(11)    NOT NULL DEFAULT '0',
    `skill3`      INT(11)    NOT NULL DEFAULT '0',
    `name`        VARCHAR(13)         DEFAULT NULL,
    `shout`       TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `skills`
(
    `id`          INT(11)    NOT NULL AUTO_INCREMENT,
    `skillid`     INT(11)    NOT NULL DEFAULT '0',
    `characterid` INT(11)    NOT NULL DEFAULT '0',
    `skilllevel`  INT(11)    NOT NULL DEFAULT '0',
    `masterlevel` INT(11)    NOT NULL DEFAULT '0',
    `expiration`  BIGINT(20) NOT NULL DEFAULT '-1',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `skillpair` (`skillid`, `characterid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;