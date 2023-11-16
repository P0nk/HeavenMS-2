# quickslot table, by Shavit
CREATE TABLE IF NOT EXISTS `quickslotkeymapped`
(
    `accountid` INT    NOT NULL,
    `keymap`    BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`accountid`)
);

ALTER TABLE `quickslotkeymapped`
    ADD CONSTRAINT `quickslotkeymapped_accountid_fk` FOREIGN KEY (`accountid`) REFERENCES `accounts` (`id`) ON DELETE CASCADE;