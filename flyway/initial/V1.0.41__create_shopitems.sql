CREATE TABLE IF NOT EXISTS `shopitems`
(
    `shopitemid` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `shopid`     INT(10) UNSIGNED NOT NULL,
    `itemid`     INT(11)          NOT NULL,
    `price`      INT(11)          NOT NULL,
    `pitch`      INT(11)          NOT NULL DEFAULT '0',
    `position`   INT(11)          NOT NULL COMMENT 'sort is an arbitrary field designed to give leeway when modifying shops. The lowest number is 104 and it increments by 4 for each item to allow decent space for swapping/inserting/removing items.',
    PRIMARY KEY (`shopitemid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 20047;