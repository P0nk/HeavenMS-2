CREATE TABLE IF NOT EXISTS `mts_cart`
(
    `id`     INT(11) NOT NULL AUTO_INCREMENT,
    `cid`    INT(11) NOT NULL,
    `itemid` INT(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;

CREATE TABLE IF NOT EXISTS `mts_items`
(
    `id`           INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `tab`          INT(11)          NOT NULL DEFAULT '0',
    `type`         INT(11)          NOT NULL DEFAULT '0',
    `itemid`       INT(10) UNSIGNED NOT NULL DEFAULT '0',
    `quantity`     INT(11)          NOT NULL DEFAULT '1',
    `seller`       INT(11)          NOT NULL DEFAULT '0',
    `price`        INT(11)          NOT NULL DEFAULT '0',
    `bid_incre`    INT(11)                   DEFAULT '0',
    `buy_now`      INT(11)                   DEFAULT '0',
    `position`     INT(11)                   DEFAULT '0',
    `upgradeslots` INT(11)                   DEFAULT '0',
    `level`        INT(11)                   DEFAULT '0',
    `itemlevel`    INT(11)          NOT NULL DEFAULT '1',
    `itemexp`      INT(11) UNSIGNED NOT NULL DEFAULT '0',
    `ringid`       INT(11)          NOT NULL DEFAULT '-1',
    `str`          INT(11)                   DEFAULT '0',
    `dex`          INT(11)                   DEFAULT '0',
    `int`          INT(11)                   DEFAULT '0',
    `luk`          INT(11)                   DEFAULT '0',
    `hp`           INT(11)                   DEFAULT '0',
    `mp`           INT(11)                   DEFAULT '0',
    `watk`         INT(11)                   DEFAULT '0',
    `matk`         INT(11)                   DEFAULT '0',
    `wdef`         INT(11)                   DEFAULT '0',
    `mdef`         INT(11)                   DEFAULT '0',
    `acc`          INT(11)                   DEFAULT '0',
    `avoid`        INT(11)                   DEFAULT '0',
    `hands`        INT(11)                   DEFAULT '0',
    `speed`        INT(11)                   DEFAULT '0',
    `jump`         INT(11)                   DEFAULT '0',
    `locked`       INT(11)                   DEFAULT '0',
    `isequip`      INT(1)                    DEFAULT '0',
    `owner`        VARCHAR(16)               DEFAULT '',
    `sellername`   VARCHAR(16)      NOT NULL,
    `sell_ends`    VARCHAR(16)      NOT NULL,
    `transfer`     INT(2)                    DEFAULT '0',
    `vicious`      INT(2) UNSIGNED  NOT NULL DEFAULT '0',
    `flag`         INT(2) UNSIGNED  NOT NULL DEFAULT '0',
    `expiration`   BIGINT(20)       NOT NULL DEFAULT '-1',
    `giftFrom`     VARCHAR(26)      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;