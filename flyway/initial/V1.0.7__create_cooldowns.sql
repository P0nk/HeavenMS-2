CREATE TABLE IF NOT EXISTS `cooldowns`
(
    `id`        INT(11)             NOT NULL AUTO_INCREMENT,
    `charid`    INT(11)             NOT NULL,
    `SkillID`   INT(11)             NOT NULL,
    `length`    BIGINT(20) UNSIGNED NOT NULL,
    `StartTime` BIGINT(20) UNSIGNED NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;