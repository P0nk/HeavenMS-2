CREATE TABLE IF NOT EXISTS `playerdiseases`
(
    `id`         INT(11) NOT NULL AUTO_INCREMENT,
    `charid`     INT(11) NOT NULL,
    `disease`    INT(11) NOT NULL,
    `mobskillid` INT(11) NOT NULL,
    `mobskilllv` INT(11) NOT NULL,
    `length`     INT(11) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;