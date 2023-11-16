ALTER TABLE `dueyitems`
    ADD CONSTRAINT `dueyitems_ibfk_1` FOREIGN KEY (`PackageId`) REFERENCES `dueypackages` (`PackageId`) ON DELETE CASCADE;

ALTER TABLE `famelog`
    ADD CONSTRAINT `famelog_ibfk_1` FOREIGN KEY (`characterid`) REFERENCES `characters` (`id`) ON DELETE CASCADE;

ALTER TABLE `family_character`
    ADD CONSTRAINT `family_character_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `characters` (`id`) ON DELETE CASCADE;

ALTER TABLE `skills`
    ADD CONSTRAINT `skills_chrid_fk` FOREIGN KEY (`characterid`) REFERENCES `characters` (`id`) ON DELETE CASCADE; # thanks Shavit
