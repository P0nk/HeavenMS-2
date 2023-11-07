CREATE TABLE IF NOT EXISTS `trocklocations`
(
    `trockid`     INT(11) NOT NULL AUTO_INCREMENT,
    `characterid` INT(11) NOT NULL,
    `mapid`       INT(11) NOT NULL,
    `vip`         INT(2)  NOT NULL,
    PRIMARY KEY (`trockid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;