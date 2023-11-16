CREATE TABLE IF NOT EXISTS `alliance`
(
    `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(13)      NOT NULL,
    `capacity` INT(10) UNSIGNED NOT NULL DEFAULT '2',
    `notice`   VARCHAR(20)      NOT NULL DEFAULT '',
    `rank1`    VARCHAR(11)      NOT NULL DEFAULT 'Master',
    `rank2`    VARCHAR(11)      NOT NULL DEFAULT 'Jr. Master',
    `rank3`    VARCHAR(11)      NOT NULL DEFAULT 'Member',
    `rank4`    VARCHAR(11)      NOT NULL DEFAULT 'Member',
    `rank5`    VARCHAR(11)      NOT NULL DEFAULT 'Member',
    PRIMARY KEY (`id`),
    INDEX (name)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `allianceguilds`
(
    `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `allianceid` INT(10)          NOT NULL DEFAULT '-1',
    `guildid`    INT(10)          NOT NULL DEFAULT '-1',
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;