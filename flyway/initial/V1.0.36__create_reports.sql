CREATE TABLE IF NOT EXISTS `reports`
(
    `id`          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `reporttime`  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `reporterid`  INT(11)          NOT NULL,
    `victimid`    INT(11)          NOT NULL,
    `reason`      TINYINT(4)       NOT NULL,
    `chatlog`     TEXT             NOT NULL,
    `description` TEXT             NOT NULL, # correct field name, thanks resinate
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;