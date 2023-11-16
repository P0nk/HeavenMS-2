CREATE TABLE IF NOT EXISTS `specialcashitems`
(
    `id`       INT(11) NOT NULL,
    `sn`       INT(11) NOT NULL,
    `modifier` INT(11) NOT NULL COMMENT '1024 is add/remove',
    `info`     INT(1)  NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;