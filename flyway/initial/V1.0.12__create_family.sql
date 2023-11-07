CREATE TABLE IF NOT EXISTS `family_character`
(
    `cid`             INT(11)    NOT NULL,
    `familyid`        INT(11)    NOT NULL,
    `seniorid`        INT(11)    NOT NULL,
    `reputation`      INT(11)    NOT NULL DEFAULT '0',
    `todaysrep`       INT(11)    NOT NULL DEFAULT '0',
    `totalreputation` INT(11)    NOT NULL DEFAULT '0',
    `reptosenior`     INT(11)    NOT NULL DEFAULT '0',
    `precepts`        VARCHAR(200)        DEFAULT NULL,
    `lastresettime`   BIGINT(20) NOT NULL DEFAULT '0',
    PRIMARY KEY (`cid`),
    INDEX (cid, familyid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `family_entitlement`
(
    `id`            INT(11)    NOT NULL AUTO_INCREMENT,
    `charid`        INT(11)    NOT NULL,
    `entitlementid` INT(11)    NOT NULL,
    `TIMESTAMP`     BIGINT(20) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    INDEX (charid)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;