CREATE TABLE IF NOT EXISTS `responses`
(
    `chat`     TEXT,
    `response` TEXT,
    `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;