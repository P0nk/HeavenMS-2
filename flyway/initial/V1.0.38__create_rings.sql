CREATE TABLE IF NOT EXISTS `rings`
(
    `id`            INT(11)      NOT NULL AUTO_INCREMENT,
    `partnerRingId` INT(11)      NOT NULL DEFAULT '0',
    `partnerChrId`  INT(11)      NOT NULL DEFAULT '0',
    `itemid`        INT(11)      NOT NULL DEFAULT '0',
    `partnername`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;