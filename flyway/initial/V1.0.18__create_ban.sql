CREATE TABLE IF NOT EXISTS `ipbans`
(
    `ipbanid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `ip`      VARCHAR(40)      NOT NULL DEFAULT '',
    `aid`     VARCHAR(40)               DEFAULT NULL,
    PRIMARY KEY (`ipbanid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `macbans`
(
    `macbanid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `mac`      VARCHAR(30)      NOT NULL,
    `aid`      VARCHAR(40) DEFAULT NULL,
    PRIMARY KEY (`macbanid`),
    UNIQUE KEY `mac_2` (`mac`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `macfilters`
(
    `macfilterid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `filter`      VARCHAR(30)      NOT NULL,
    PRIMARY KEY (`macfilterid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;