CREATE TABLE IF NOT EXISTS `dueyitems`
(
    `id`              INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `PackageId`       INT(10) UNSIGNED NOT NULL DEFAULT '0',
    `inventoryitemid` INT(10) UNSIGNED NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `INVENTORYITEMID` (`inventoryitemid`),
    KEY `PackageId` (`PackageId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;


CREATE TABLE IF NOT EXISTS `dueypackages`
(
    `PackageId`  INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `ReceiverId` INT(10) UNSIGNED NOT NULL,
    `SenderName` VARCHAR(13)      NOT NULL,
    `Mesos`      INT(10) UNSIGNED          DEFAULT '0',
    `TIMESTAMP`  TIMESTAMP        NOT NULL DEFAULT '2015-01-01 05:00:00',
    `Message`    VARCHAR(200)     NULL,
    `Checked`    TINYINT(1) UNSIGNED       DEFAULT '1',
    `Type`       TINYINT(1) UNSIGNED       DEFAULT '0',
    PRIMARY KEY (`PackageId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;