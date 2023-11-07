CREATE TABLE IF NOT EXISTS `pets`
(
    `petid`     INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(13)               DEFAULT NULL,
    `level`     INT(10) UNSIGNED NOT NULL,
    `closeness` INT(10) UNSIGNED NOT NULL,
    `fullness`  INT(10) UNSIGNED NOT NULL,
    `summoned`  TINYINT(1)       NOT NULL DEFAULT '0',
    `flag`      INT(10) UNSIGNED NOT NULL DEFAULT '0',
    PRIMARY KEY (`petid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `petignores`
(
    `id`     INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `petid`  INT(11) UNSIGNED NOT NULL,
    `itemid` INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_petignorepetid` FOREIGN KEY (`petid`) REFERENCES `pets` (`petid`) ON DELETE CASCADE # thanks Optimist for noticing queries over petid taking too long, shavit for pointing out an improvement using foreign key
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;