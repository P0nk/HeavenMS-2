CREATE TABLE IF NOT EXISTS `makercreatedata`
(
    `id`              TINYINT(3) UNSIGNED NOT NULL,
    `itemid`          INT(11)             NOT NULL,
    `req_level`       TINYINT(3) UNSIGNED NOT NULL,
    `req_maker_level` TINYINT(3) UNSIGNED NOT NULL,
    `req_meso`        INT(11)             NOT NULL,
    `req_item`        INT(11)             NOT NULL,
    `req_equip`       INT(11)             NOT NULL,
    `catalyst`        INT(11)             NOT NULL,
    `quantity`        SMALLINT(6)         NOT NULL,
    `tuc`             TINYINT(3)          NOT NULL,
    PRIMARY KEY (`id`, `itemid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `makerrecipedata`
(
    `itemid`   INT(11)     NOT NULL,
    `req_item` INT(11)     NOT NULL,
    `count`    SMALLINT(6) NOT NULL,
    PRIMARY KEY (`itemid`, `req_item`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `makerrewarddata`
(
    `itemid`   INT(11)             NOT NULL,
    `rewardid` INT(11)             NOT NULL,
    `quantity` SMALLINT(6)         NOT NULL,
    `prob`     TINYINT(3) UNSIGNED NOT NULL DEFAULT '100',
    PRIMARY KEY (`itemid`, `rewardid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;


# generated with the MapleSkillMakerReagentIndexer feature
CREATE TABLE IF NOT EXISTS `makerreagentdata`
(
    `itemid` INT(11)     NOT NULL,
    `stat`   VARCHAR(20) NOT NULL,
    `value`  SMALLINT(6) NOT NULL,
    PRIMARY KEY (`itemid`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4;