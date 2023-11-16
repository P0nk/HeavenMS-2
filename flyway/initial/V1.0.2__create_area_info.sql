CREATE TABLE IF NOT EXISTS `area_info`
(
    `id`     INT(11)      NOT NULL AUTO_INCREMENT,
    `charid` INT(11)      NOT NULL,
    `area`   INT(11)      NOT NULL,
    `info`   VARCHAR(200) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;