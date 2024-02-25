/*
	This file is part of the MapleSolaxia Maple Story Server

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
import client.QuestStatus;
import provider.Data;
import provider.DataTool;
import server.quest.Quest;
import server.quest.QuestRequirementType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyler (Twdtwd)
 */
public class QuestRequirement extends AbstractQuestRequirement {
    Map<Integer, Integer> quests = new HashMap<>();

    public QuestRequirement(Quest quest, Data data) {
        super(QuestRequirementType.QUEST);
        processData(data);
    }

    /**
     * @param data
     */
    @Override
    public void processData(Data data) {
        for (Data questEntry : data.getChildren()) {
            int questID = DataTool.getInt(questEntry.getChildByPath("id"));
            int stateReq = DataTool.getInt(questEntry.getChildByPath("state"));
            quests.put(questID, stateReq);
        }
    }


    @Override
    public boolean check(Character chr, Integer npcid) {
        for (Integer questID : quests.keySet()) {
            int stateReq = quests.get(questID);
            QuestStatus qs = chr.getQuest(Quest.getInstance(questID));

            if (qs == null && QuestStatus.Status.getById(stateReq).equals(QuestStatus.Status.NOT_STARTED)) {
                continue;
            }

            if (qs == null || !qs.getStatus().equals(QuestStatus.Status.getById(stateReq))) {
                return false;
            }

        }
        return true;
    }
}
