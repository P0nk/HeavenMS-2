CREATE TABLE IF NOT EXISTS `savedlocations`
(
    `id`           INT(11) NOT NULL AUTO_INCREMENT,
    `characterid`  INT(11) NOT NULL,
    `locationtype` ENUM (
        'FREE_MARKET',
        'WORLDTOUR',
        'FLORINA',
        'INTRO',
        'SUNDAY_MARKET',
        'MIRROR',
        'EVENT',
        'BOSSPQ',
        'HAPPYVILLE',
        'DEVELOPER',
        'MONSTER_CARNIVAL',
        'JAIL')            NOT NULL,
    `map`          INT(11) NOT NULL,
    `portal`       INT(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 1;