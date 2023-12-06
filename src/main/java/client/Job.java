/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package client;

public enum Job {
    BEGINNER(0),

    WARRIOR(100),
    FIGHTER(110), CRUSADER(111), HERO(112),
    PAGE(120), WHITEKNIGHT(121), PALADIN(122),
    SPEARMAN(130), DRAGONKNIGHT(131), DARKKNIGHT(132),

    MAGICIAN(200),
    FP_WIZARD(210), FP_MAGE(211), FP_ARCHMAGE(212),
    IL_WIZARD(220), IL_MAGE(221), IL_ARCHMAGE(222),
    CLERIC(230), PRIEST(231), BISHOP(232),

    BOWMAN(300),
    HUNTER(310), RANGER(311), BOWMASTER(312),
    CROSSBOWMAN(320), SNIPER(321), MARKSMAN(322),

    THIEF(400),
    ASSASSIN(410), HERMIT(411), NIGHTLORD(412),
    BANDIT(420), CHIEFBANDIT(421), SHADOWER(422),

    PIRATE(500),
    BRAWLER(510), MARAUDER(511), BUCCANEER(512),
    GUNSLINGER(520), OUTLAW(521), CORSAIR(522),

    MAPLELEAF_BRIGADIER(800),
    GM(900), SUPERGM(910),

    NOBLESSE(1000),
    DAWNWARRIOR1(1100), DAWNWARRIOR2(1110), DAWNWARRIOR3(1111), DAWNWARRIOR4(1112),
    BLAZEWIZARD1(1200), BLAZEWIZARD2(1210), BLAZEWIZARD3(1211), BLAZEWIZARD4(1212),
    WINDARCHER1(1300), WINDARCHER2(1310), WINDARCHER3(1311), WINDARCHER4(1312),
    NIGHTWALKER1(1400), NIGHTWALKER2(1410), NIGHTWALKER3(1411), NIGHTWALKER4(1412),
    THUNDERBREAKER1(1500), THUNDERBREAKER2(1510), THUNDERBREAKER3(1511), THUNDERBREAKER4(1512),

    LEGEND(2000), EVAN(2001),
    ARAN1(2100), ARAN2(2110), ARAN3(2111), ARAN4(2112),

    EVAN1(2200), EVAN2(2210), EVAN3(2211), EVAN4(2212), EVAN5(2213), EVAN6(2214),
    EVAN7(2215), EVAN8(2216), EVAN9(2217), EVAN10(2218);

    final int jobid;
    final static int maxId = 22;    // maxId = (EVAN / 100);

    public static String getJobAsString(Job job){
        return switch (job) {
            case BEGINNER -> "Beginner";
            case WARRIOR -> "Warrior";
                case PAGE -> "Page";
                case WHITEKNIGHT -> "White Knight";
                case PALADIN -> "Paladin";

                case SPEARMAN -> "Spearman";
                case DRAGONKNIGHT -> "Dragon Knight";
                case DARKKNIGHT -> "Dark Knight";

                case FIGHTER -> "Fighter";
                case CRUSADER -> "Crusader";
                case HERO -> "Hero";
            case BOWMAN -> "Archer";
                case HUNTER -> "Hunter";
                case RANGER -> "Ranger";
                case BOWMASTER -> "Bowmaster";

                case CROSSBOWMAN -> "Crossbowman";
                case SNIPER -> "Sniper";
                case MARKSMAN -> "Marksman";
            case MAGICIAN -> "Magician";
                case FP_MAGE -> "Fire Poison Mage";
                case FP_WIZARD -> "Fire Poison Wizzy";
                case FP_ARCHMAGE -> "Fire Poison Arch Mage";

                case IL_MAGE -> "Ice Lightning Mage";
                case IL_WIZARD -> "Ice Lightning Wizzy";
                case IL_ARCHMAGE -> "Ice LIghtning Arch Mage";

                case CLERIC -> "Cleric";
                case PRIEST -> "Priest";
                case BISHOP -> "Bishop";
            case PIRATE -> "Pirate";
                case BRAWLER -> "Brawler";
                case MARAUDER -> "Marauder";
                case BUCCANEER -> "Buccaneer";

                case GUNSLINGER -> "Gunslinger";
                case OUTLAW -> "Outlaw";
                case CORSAIR -> "Corsair";
            case THIEF -> "Thief";
                case BANDIT -> "Bandit";
                case CHIEFBANDIT -> "Chief Bandit";
                case SHADOWER -> "Shadower";

                case ASSASSIN -> "Assassin";
                case HERMIT -> "Hermit";
                case NIGHTLORD -> "Night Lord";
            case NOBLESSE -> "Noblesse";

            case DAWNWARRIOR1 -> "Dawn Warrior (1st)";
            case DAWNWARRIOR2 -> "Dawn Warrior (2nd)";
            case DAWNWARRIOR3 -> "Dawn Warrior (3rd)";
            case DAWNWARRIOR4 -> "Dawn Warrior (4th)";

            case BLAZEWIZARD1 -> "Blaze Wizard (1st)";
            case BLAZEWIZARD2 -> "Blaze Wizard (2nd)";
            case BLAZEWIZARD3 -> "Blaze Wizard (3rd)";
            case BLAZEWIZARD4 -> "Blaze Wizard (4th)";

            case THUNDERBREAKER1 -> "Thunder Breaker (1st)";
            case THUNDERBREAKER2 -> "Thunder Breaker (2nd)";
            case THUNDERBREAKER3 -> "Thunder Breaker (3rd)";
            case THUNDERBREAKER4 -> "Thunder Breaker (4th)";

            case WINDARCHER1 -> "Wind Archer(1st)";
            case WINDARCHER2 -> "Wind Archer(1st)";
            case WINDARCHER3 -> "Wind Archer(1st)";
            case WINDARCHER4 -> "Wind Archer(4th)";

            case NIGHTWALKER1 -> "Night Walker(1st)";
            case NIGHTWALKER2 -> "Night Walker(2nd)";
            case NIGHTWALKER3 -> "Night Walker(3rd)";
            case NIGHTWALKER4 -> "Night Walker(4th)";

            case LEGEND -> "Legend";
            case ARAN1, ARAN2, ARAN3, ARAN4 -> "Aran";
            case EVAN1, EVAN2, EVAN3, EVAN4, EVAN5, EVAN6, EVAN7, EVAN8, EVAN9, EVAN10 -> "Evan";
            case GM -> "Game Master";
            case SUPERGM -> "Game Overlord";
            default -> "default?";
        };
    }

    Job(int id) {
        jobid = id;
    }

    public static int getMax() {
        return maxId;
    }

    public int getId() {
        return jobid;
    }

    public static Job getById(int id) {
        for (Job l : Job.values()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public static Job getBy5ByteEncoding(int encoded) {
        switch (encoded) {
            case 2:
                return WARRIOR;
            case 4:
                return MAGICIAN;
            case 8:
                return BOWMAN;
            case 16:
                return THIEF;
            case 32:
                return PIRATE;
            case 1024:
                return NOBLESSE;
            case 2048:
                return DAWNWARRIOR1;
            case 4096:
                return BLAZEWIZARD1;
            case 8192:
                return WINDARCHER1;
            case 16384:
                return NIGHTWALKER1;
            case 32768:
                return THUNDERBREAKER1;
            default:
                return BEGINNER;
        }
    }


    public boolean isA(Job basejob) {  // thanks Steve (kaito1410) for pointing out an improvement here
        int basebranch = basejob.getId() / 10;
        return (getId() / 10 == basebranch && getId() >= basejob.getId()) || (basebranch % 10 == 0 && getId() / 100 == basejob.getId() / 100);
    }

    public int getJobNiche() {
        return (jobid / 100) % 10;
        
        /*
        case 0: BEGINNER;
        case 1: WARRIOR;
        case 2: MAGICIAN;
        case 3: BOWMAN;  
        case 4: THIEF;
        case 5: PIRATE;
        */
    }
}
