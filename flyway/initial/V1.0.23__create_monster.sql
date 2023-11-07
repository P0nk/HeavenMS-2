CREATE TABLE IF NOT EXISTS `monsterbook`
(
    `charid` INT(11) NOT NULL,
    `cardid` INT(11) NOT NULL,
    `level`  INT(1)  NOT NULL DEFAULT '1',
    PRIMARY KEY (`charid`, `cardid`),
    CONSTRAINT `FK_monsterbook_characters` FOREIGN KEY (`charid`) REFERENCES `characters` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `monstercarddata`
(
    `id`     INT(11) NOT NULL AUTO_INCREMENT,
    `cardid` INT(11) NOT NULL DEFAULT '0',
    `mobid`  INT(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `id` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;
