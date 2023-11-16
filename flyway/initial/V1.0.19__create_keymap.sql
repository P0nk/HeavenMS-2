CREATE TABLE IF NOT EXISTS `keymap`
(
    `id`          INT(11) NOT NULL AUTO_INCREMENT,
    `characterid` INT(11) NOT NULL DEFAULT '0',
    `key`         INT(11) NOT NULL DEFAULT '0',
    `type`        INT(11) NOT NULL DEFAULT '0',
    `action`      INT(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;