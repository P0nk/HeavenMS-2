CREATE TABLE IF NOT EXISTS `guilds`
(
    `guildid`     INT(10) UNSIGNED     NOT NULL AUTO_INCREMENT,
    `leader`      INT(10) UNSIGNED     NOT NULL DEFAULT '0',
    `GP`          INT(10) UNSIGNED     NOT NULL DEFAULT '0',
    `logo`        INT(10) UNSIGNED              DEFAULT NULL,
    `logoColor`   SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
    `name`        VARCHAR(45)          NOT NULL,
    `rank1title`  VARCHAR(45)          NOT NULL DEFAULT 'Master',
    `rank2title`  VARCHAR(45)          NOT NULL DEFAULT 'Jr. Master',
    `rank3title`  VARCHAR(45)          NOT NULL DEFAULT 'Member',
    `rank4title`  VARCHAR(45)          NOT NULL DEFAULT 'Member',
    `rank5title`  VARCHAR(45)          NOT NULL DEFAULT 'Member',
    `capacity`    INT(10) UNSIGNED     NOT NULL DEFAULT '10',
    `logoBG`      INT(10) UNSIGNED              DEFAULT NULL,
    `logoBGColor` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
    `notice`      VARCHAR(101)                  DEFAULT NULL,
    `signature`   INT(11)              NOT NULL DEFAULT '0',
    `allianceId`  INT(11) UNSIGNED     NOT NULL DEFAULT '0',
    PRIMARY KEY (`guildid`),
    INDEX (guildid, name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;