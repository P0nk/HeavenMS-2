/*
    This file is part of the HeavenMS MapleStory Server
    Copyleft (L) 2016 - 2019 RonanLana

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
package client.creator.veteran;

import client.Client;
import client.Job;
import client.creator.CharacterFactory;
import client.creator.CharacterFactoryRecipe;
import client.inventory.InventoryType;
import client.inventory.Item;
import constants.id.ItemId;
import constants.id.MapId;
import server.ItemInformationProvider;

/**
 * @author RonanLana
 */
public class PirateCreator extends CharacterFactory {
    private static final int[] equips = {0, 0, 0, 0, ItemId.BROWN_PAULIE_BOOTS};
    private static final int[] weapons = {ItemId.PRIME_HANDS, ItemId.COLD_MIND};
    private static final int[] startingHpMp = {846, 503};

    private static CharacterFactoryRecipe createRecipe(Job job, int level, int map, int top, int bottom, int shoes, int weapon) {
        CharacterFactoryRecipe recipe = new CharacterFactoryRecipe(job, level, map, top, bottom, shoes, weapon);
        ItemInformationProvider ii = ItemInformationProvider.getInstance();

        recipe.setDex(20);
        recipe.setRemainingAp(138);
        recipe.setRemainingSp(61);

        recipe.setMaxHp(startingHpMp[0]);
        recipe.setMaxMp(startingHpMp[1]);

        recipe.setMeso(100000);

        giveEquipment(recipe, ii, ItemId.BROWN_POLLARD);

        for (int i = 1; i < weapons.length; i++) {
            giveEquipment(recipe, ii, weapons[i]);
        }

        giveItem(recipe, ItemId.BULLET, 800, InventoryType.USE);

        giveItem(recipe, ItemId.WHITE_POTION, 100, InventoryType.USE);
        giveItem(recipe, ItemId.BLUE_POTION, 100, InventoryType.USE);
        giveItem(recipe, ItemId.RELAXER, 1, InventoryType.SETUP);

        return recipe;
    }

    private static void giveEquipment(CharacterFactoryRecipe recipe, ItemInformationProvider ii, int equipid) {
        Item nEquip = ii.getEquipById(equipid);
        recipe.addStartingEquipment(nEquip);
    }

    private static void giveItem(CharacterFactoryRecipe recipe, int itemid, int quantity, InventoryType itemType) {
        recipe.addStartingItem(itemid, quantity, itemType);
    }

    public static int createCharacter(Client c, String name, int face, int hair, int skin, int gender, int improveSp) {
        return createNewCharacter(c, name, face, hair, skin, gender, createRecipe(Job.PIRATE, 30, MapId.NAUTILUS_HARBOR, equips[gender], equips[2 + gender], equips[4], weapons[0]));
    }
}
