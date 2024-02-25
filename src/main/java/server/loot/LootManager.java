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
package server.loot;

import client.Character;
import server.life.MonsterDropEntry;
import server.life.MonsterInformationProvider;
import server.quest.Quest;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ronan
 */
public class LootManager {

    private static boolean isRelevantDrop(MonsterDropEntry dropEntry, List<Character> players, List<LootInventory> playersInv) {
        int qStartAmount = 0, qCompleteAmount = 0;
        Quest quest = Quest.getInstance(dropEntry.questid);
        if (quest != null) {
            qStartAmount = quest.getStartItemAmountNeeded(dropEntry.itemId);
            qCompleteAmount = quest.getCompleteItemAmountNeeded(dropEntry.itemId);
        }

        //boolean restricted = ItemInformationProvider.getInstance().isPickupRestricted(dropEntry.itemId);
        for (int i = 0; i < players.size(); i++) {
            LootInventory chrInv = playersInv.get(i);

            if (dropEntry.questid > 0) {
                int qItemAmount, chrQuestStatus = players.get(i).getQuestStatus(dropEntry.questid);
                if (chrQuestStatus == 0) {
                    qItemAmount = qStartAmount;
                } else if (chrQuestStatus != 1) {
                    continue;
                } else {
                    qItemAmount = qCompleteAmount;
                }

                // thanks kvmba for noticing quest items with no required amount failing to be detected as such

                int qItemStatus = chrInv.hasItem(dropEntry.itemId, qItemAmount);
                if (qItemStatus == 2) {
                    continue;
                } /*else if (restricted && qItemStatus == 1) {  // one-of-a-kind loots should be available everytime, thanks onechord for noticing
                    continue;
                }*/
            } /*else if (restricted && chrInv.hasItem(dropEntry.itemId, 1) > 0) {   // thanks Conrad, Legalize for noticing eligible loots not being available to drop for non-killer parties
                continue;
            }*/

            return true;
        }

        return false;
    }

    public static List<MonsterDropEntry> retrieveRelevantDrops(int monsterId, List<Character> players) {
        List<MonsterDropEntry> loots = MonsterInformationProvider.getInstance().retrieveEffectiveDrop(monsterId);
        if (loots.isEmpty()) {
            return loots;
        }

        List<LootInventory> playersInv = new LinkedList<>();
        for (Character chr : players) {
            LootInventory lootInv = new LootInventory(chr);
            playersInv.add(lootInv);
        }

        List<MonsterDropEntry> effectiveLoot = new LinkedList<>();
        for (MonsterDropEntry mde : loots) {
            if (isRelevantDrop(mde, players, playersInv)) {
                effectiveLoot.add(mde);
            }
        }

        return effectiveLoot;
    }

}
