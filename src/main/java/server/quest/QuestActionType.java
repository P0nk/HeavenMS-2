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
package server.quest;

/**
 * @author Matze
 */
public enum QuestActionType {
    UNDEFINED(-1),
    EXP(0),
    ITEM(1),
    NEXTQUEST(2),
    MESO(3),
    QUEST(4),
    SKILL(5),
    FAME(6),
    BUFF(7),
    PETSKILL(8),
    YES(9),
    NO(10),
    NPC(11),
    MIN_LEVEL(12),
    NORMAL_AUTO_START(13),
    PETTAMENESS(14),
    PETSPEED(15),
    INFO(16),
    ZERO(16);

    final byte type;

    QuestActionType(int type) {
        this.type = (byte) type;
    }

    public static QuestActionType getByWZName(String name) {
        switch (name) {
        case "exp":
            return EXP;
        case "money":
            return MESO;
        case "item":
            return ITEM;
        case "skill":
            return SKILL;
        case "nextQuest":
            return NEXTQUEST;
        case "pop":
            return FAME;
        case "buffItemID":
            return BUFF;
        case "petskill":
            return PETSKILL;
        case "no":
            return NO;
        case "yes":
            return YES;
        case "npc":
            return NPC;
        case "lvmin":
            return MIN_LEVEL;
        case "normalAutoStart":
            return NORMAL_AUTO_START;
        case "pettameness":
            return PETTAMENESS;
        case "petspeed":
            return PETSPEED;
        case "info":
            return INFO;
        case "0":
            return ZERO;
        default:
            return UNDEFINED;
        }
    }
}
