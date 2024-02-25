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
package server.gachapon;

import client.Character;
import constants.id.NpcId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ItemInformationProvider;
import tools.Randomizer;

/**
 * @author Alan (SharpAceX)
 */
public class Gachapon {
    private static final Logger log = LoggerFactory.getLogger(Gachapon.class);
    private static final Gachapon instance = new Gachapon();

    public static Gachapon getInstance() {
        return instance;
    }

    public enum GachaponType {

        GLOBAL(-1, -1, -1, -1, new Global()),
        HENESYS(NpcId.GACHAPON_HENESYS, 90, 8, 2, new Henesys()),
        ELLINIA(NpcId.GACHAPON_ELLINIA, 90, 8, 2, new Ellinia()),
        PERION(NpcId.GACHAPON_PERION, 90, 8, 2, new Perion()),
        KERNING_CITY(NpcId.GACHAPON_KERNING, 90, 8, 2, new KerningCity()),
        SLEEPYWOOD(NpcId.GACHAPON_SLEEPYWOOD, 90, 8, 2, new Sleepywood()),
        MUSHROOM_SHRINE(NpcId.GACHAPON_MUSHROOM_SHRINE, 90, 8, 2, new MushroomShrine()),
        SHOWA_SPA_MALE(NpcId.GACHAPON_SHOWA_MALE, 90, 8, 2, new ShowaSpaMale()),
        SHOWA_SPA_FEMALE(NpcId.GACHAPON_SHOWA_FEMALE, 90, 8, 2, new ShowaSpaFemale()),
        LUDIBRIUM(NpcId.GACHAPON_LUDIBRIUM, 90, 8, 2, new Ludibrium()),
        NEW_LEAF_CITY(NpcId.GACHAPON_NLC, 90, 8, 2, new NewLeafCity()),
        EL_NATH(NpcId.GACHAPON_EL_NATH, 90, 8, 2, new ElNath()),
        NAUTILUS_HARBOR(NpcId.GACHAPON_NAUTILUS, 90, 8, 2, new NautilusHarbor());

        private static final GachaponType[] values = GachaponType.values();

        private final GachaponItems gachapon;
        private final int npcId;
        private final int common;
        private final int uncommon;
        private final int rare;

        GachaponType(int npcid, int c, int u, int r, GachaponItems g) {
            this.npcId = npcid;
            this.gachapon = g;
            this.common = c;
            this.uncommon = u;
            this.rare = r;
        }

        private int getTier() {
            int chance = Randomizer.nextInt(common + uncommon + rare) + 1;
            if (chance > common + uncommon) {
                return 2; //Rare
            } else if (chance > common) {
                return 1; //Uncommon
            } else {
                return 0; //Common
            }
        }

        public int[] getItems(int tier) {
            return gachapon.getItems(tier);
        }

        public int getItem(int tier) {
            int[] gacha = getItems(tier);
            int[] global = GLOBAL.getItems(tier);
            int chance = Randomizer.nextInt(gacha.length + global.length);
            return chance < gacha.length ? gacha[chance] : global[chance - gacha.length];
        }

        public static GachaponType getByNpcId(int npcId) {
            for (GachaponType gacha : values) {
                if (npcId == gacha.npcId) {
                    return gacha;
                }
            }
            return null;
        }

        public static String[] getLootInfo() {
            ItemInformationProvider ii = ItemInformationProvider.getInstance();

            String[] strList = new String[values.length + 1];

            String menuStr = "";
            int j = 0;
            for (GachaponType gacha : values) {
                menuStr += "#L" + j + "#" + gacha.name() + "#l\r\n";
                j++;

                String str = "";
                for (int i = 0; i < 3; i++) {
                    int[] gachaItems = gacha.getItems(i);

                    if (gachaItems.length > 0) {
                        str += ("  #rTier " + i + "#k:\r\n");
                        for (int itemid : gachaItems) {
                            String itemName = ii.getName(itemid);
                            if (itemName == null) {
                                itemName = "MISSING NAME #" + itemid;
                            }

                            str += ("    " + itemName + "\r\n");
                        }

                        str += "\r\n";
                    }
                }
                str += "\r\n";

                strList[j] = str;
            }
            strList[0] = menuStr;

            return strList;
        }
    }

    public GachaponItem process(int npcId) {
        GachaponType gacha = GachaponType.getByNpcId(npcId);
        int tier = gacha.getTier();
        int item = gacha.getItem(tier);
        return new GachaponItem(tier, item);
    }

    public static class GachaponItem {
        private final int id;
        private final int tier;

        public GachaponItem(int t, int i) {
            id = i;
            tier = t;
        }

        public int getTier() {
            return tier;
        }

        public int getId() {
            return id;
        }
    }

    public static void log(Character player, int itemId, String map) {
        String itemName = ItemInformationProvider.getInstance().getName(itemId);
        log.info("{} got a {} ({}) from the {} gachapon.", player.getName(), itemName, itemId, map);
    }
}
