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
package server.quest.actions;

import client.Character;
import client.Client;
import client.inventory.Pet;
import provider.Data;
import server.quest.Quest;
import server.quest.QuestActionType;

/**
 * @author Ronan
 */
public class PetSpeedAction extends AbstractQuestAction {

    public PetSpeedAction(Quest quest, Data data) {
        super(QuestActionType.PETTAMENESS, quest);
        questID = quest.getId();
    }


    @Override
    public void processData(Data data) {}

    @Override
    public void run(Character chr, Integer extSelection) {
        Client c = chr.getClient();

        Pet pet = chr.getPet(0);   // assuming here only the pet leader will gain owner speed
        if (pet == null) {
            return;
        }

        c.lockClient();
        try {
            pet.addPetAttribute(c.getPlayer(), Pet.PetAttribute.OWNER_SPEED);
        } finally {
            c.unlockClient();
        }

    }
} 
