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
package server.quest.requirements;

import client.Character;
import client.inventory.InventoryType;
import client.inventory.Item;
import constants.inventory.ItemConstants;
import provider.Data;
import provider.DataTool;
import server.ItemInformationProvider;
import server.quest.Quest;
import server.quest.QuestRequirementType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyler (Twdtwd)
 */
public class ItemRequirement extends AbstractQuestRequirement {
    Map<Integer, Integer> items = new HashMap<>();


    public ItemRequirement(Quest quest, Data data) {
        super(QuestRequirementType.ITEM);
        processData(data);
    }

    @Override
    public void processData(Data data) {
        for (Data itemEntry : data.getChildren()) {
            int itemId = DataTool.getInt(itemEntry.getChildByPath("id"));
            int count = DataTool.getInt(itemEntry.getChildByPath("count"), 0);

            items.put(itemId, count);
        }
    }


    @Override
    public boolean check(Character chr, Integer npcid) {
        ItemInformationProvider ii = ItemInformationProvider.getInstance();
        for (Integer itemId : items.keySet()) {
            int countNeeded = items.get(itemId);
            int count = 0;

            InventoryType iType = ItemConstants.getInventoryType(itemId);

            if (iType.equals(InventoryType.UNDEFINED)) {
                return false;
            }
            for (Item item : chr.getInventory(iType).listById(itemId)) {
                count += item.getQuantity();
            }
            //Weird stuff, nexon made some quests only available when wearing gm clothes. This enables us to accept it ><
            if (iType.equals(InventoryType.EQUIP) && !ItemConstants.isMedal(itemId)) {
                if (chr.isGM()) {
                    for (Item item : chr.getInventory(InventoryType.EQUIPPED).listById(itemId)) {
                        count += item.getQuantity();
                    }
                } else {
                    if (count < countNeeded) {
                        if (chr.getInventory(InventoryType.EQUIPPED).countById(itemId) + count >= countNeeded) {
                            chr.dropMessage(5, "Unequip the required " + ii.getName(itemId) + " before trying this quest operation.");
                            return false;
                        }
                    }
                }
            }

            if (count < countNeeded || countNeeded <= 0 && count > 0) {
                return false;
            }
        }
        return true;
    }

    public int getItemAmountNeeded(int itemid, boolean complete) {
        Integer amount = items.get(itemid);
        if (amount != null) {
            return amount;
        } else {
            return complete ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
    }
}
