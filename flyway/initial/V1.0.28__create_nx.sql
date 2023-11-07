CREATE TABLE IF NOT EXISTS `nxcode`
(
    `id`         INT(11)             NOT NULL AUTO_INCREMENT,
    `code`       VARCHAR(17)         NOT NULL UNIQUE,
    `retriever`  VARCHAR(13)                  DEFAULT NULL,
    `expiration` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `nxcode_items`
(
    `id`       INT(11) NOT NULL AUTO_INCREMENT,
    `codeid`   INT(11) NOT NULL,
    `type`     INT(11) NOT NULL DEFAULT '5',
    `item`     INT(11) NOT NULL DEFAULT '4000000',
    `quantity` INT(11) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `nxcoupons`
(
    `id`        INT(11) NOT NULL AUTO_INCREMENT,
    `couponid`  INT(11) NOT NULL DEFAULT '0',
    `rate`      INT(11) NOT NULL DEFAULT '0',
    `activeday` INT(11) NOT NULL DEFAULT '0',
    `starthour` INT(11) NOT NULL DEFAULT '0',
    `endhour`   INT(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 41;