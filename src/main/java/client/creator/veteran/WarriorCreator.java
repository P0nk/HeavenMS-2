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
import client.Skill;
import client.SkillFactory;
import client.creator.CharacterFactory;
import client.creator.CharacterFactoryRecipe;
import client.inventory.InventoryType;
import client.inventory.Item;
import constants.id.ItemId;
import constants.id.MapId;
import constants.skills.Warrior;
import server.ItemInformationProvider;

/**
 * @author RonanLana
 */
public class WarriorCreator extends CharacterFactory {
    private static final int[] equips = {ItemId.RED_HWARANG_SHIRT, 0, ItemId.BLACK_MARTIAL_ARTS_PANTS, 0, ItemId.MITHRIL_BATTLE_GRIEVES};
    private static final int[] weapons = {ItemId.GLADIUS, ItemId.MITHRIL_POLE_ARM, ItemId.MITHRIL_MAUL, ItemId.FIREMANS_AXE};
    private static final int[] startingHpMp = {905, 208};
    private static final int[] hpGain = {0, 72, 144, 212, 280, 348, 412, 476, 540, 600, 660};

    private static CharacterFactoryRecipe createRecipe(Job job, int level, int map, int top, int bottom, int shoes, int weapon, int gender, int improveSp) {
        CharacterFactoryRecipe recipe = new CharacterFactoryRecipe(job, level, map, top, bottom, shoes, weapon);
        ItemInformationProvider ii = ItemInformationProvider.getInstance();

        recipe.setStr(35);
        recipe.setRemainingAp(123);
        recipe.setRemainingSp(61);

        recipe.setMaxHp(startingHpMp[0] + hpGain[improveSp]);
        recipe.setMaxMp(startingHpMp[1]);

        recipe.setMeso(100000);

        if (gender == 1) {
            giveEquipment(recipe, ii, ItemId.DARK_ENGRIT);
        }

        for (int i = 1; i < weapons.length; i++) {
            giveEquipment(recipe, ii, weapons[i]);
        }

        if (improveSp > 0) {
            improveSp += 5;
            recipe.setRemainingSp(recipe.getRemainingSp() - improveSp);

            int toUseSp = 5;
            Skill improveHpRec = SkillFactory.getSkill(Warrior.IMPROVED_HPREC);
            recipe.addStartingSkillLevel(improveHpRec, toUseSp);
            improveSp -= toUseSp;

            if (improveSp > 0) {
                Skill improveMaxHp = SkillFactory.getSkill(Warrior.IMPROVED_MAXHP);
                recipe.addStartingSkillLevel(improveMaxHp, improveSp);
            }
        }

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
        return createNewCharacter(c, name, face, hair, skin, gender, createRecipe(Job.WARRIOR, 30, MapId.PERION, equips[gender], equips[2 + gender], equips[4], weapons[0], gender, improveSp));
    }
}
