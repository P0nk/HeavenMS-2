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
package server.quest.actions;

import client.Character;
import provider.Data;
import provider.DataTool;
import server.ItemInformationProvider;
import server.quest.Quest;
import server.quest.QuestActionType;

/**
 * @author Tyler (Twdtwd)
 */
public class BuffAction extends AbstractQuestAction {
    int itemEffect;

    public BuffAction(Quest quest, Data data) {
        super(QuestActionType.BUFF, quest);
        processData(data);
    }

    @Override
    public boolean check(Character chr, Integer extSelection) {
        return true;
    }

    @Override
    public void processData(Data data) {
        itemEffect = DataTool.getInt(data);
    }

    @Override
    public void run(Character chr, Integer extSelection) {
        ItemInformationProvider.getInstance().getItemEffect(itemEffect).applyTo(chr);
    }
} 
