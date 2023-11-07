CREATE TABLE IF NOT EXISTS `playernpcs`
(
    `id`           INT(11)          NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(13)      NOT NULL,
    `hair`         INT(11)          NOT NULL,
    `face`         INT(11)          NOT NULL,
    `skin`         INT(11)          NOT NULL,
    `gender`       INT(11)          NOT NULL DEFAULT '0',
    `x`            INT(11)          NOT NULL,
    `cy`           INT(11)          NOT NULL DEFAULT '0',
    `world`        INT(11)          NOT NULL DEFAULT '0',
    `map`          INT(11)          NOT NULL DEFAULT '0',
    `dir`          INT(11)          NOT NULL DEFAULT '0',
    `scriptid`     INT(10) UNSIGNED NOT NULL DEFAULT '0',
    `fh`           INT(11)          NOT NULL DEFAULT '0',
    `rx0`          INT(11)          NOT NULL DEFAULT '0',
    `rx1`          INT(11)          NOT NULL DEFAULT '0',
    `worldrank`    INT(11)          NOT NULL DEFAULT '0',
    `overallrank`  INT(11)          NOT NULL DEFAULT '0',
    `worldjobrank` INT(11)          NOT NULL DEFAULT '0',
    `job`          INT(11)          NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 2147000000;


CREATE TABLE IF NOT EXISTS `playernpcs_equip`
(
    `id`       INT(11) NOT NULL AUTO_INCREMENT,
    `npcid`    INT(11) NOT NULL DEFAULT '0',
    `equipid`  INT(11) NOT NULL,
    `type`     INT(11) NOT NULL DEFAULT '0',
    `equippos` INT(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `playernpcs_field`
(
    `id`     INT(11)     NOT NULL AUTO_INCREMENT,
    `world`  INT(11)     NOT NULL,
    `map`    INT(11)     NOT NULL,
    `step`   TINYINT(1)  NOT NULL DEFAULT '0',
    `podium` SMALLINT(8) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;