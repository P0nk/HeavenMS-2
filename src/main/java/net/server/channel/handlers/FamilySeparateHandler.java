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
package net.server.channel.handlers;

import client.Client;
import client.Family;
import client.FamilyEntry;
import config.YamlConfig;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import tools.PacketCreator;

public class FamilySeparateHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(InPacket p, Client c) {
        if (!YamlConfig.config.server.USE_FAMILY_SYSTEM) {
            return;
        }
        Family oldFamily = c.getPlayer().getFamily();
        if (oldFamily == null) {
            return;
        }
        FamilyEntry forkOn = null;
        boolean isSenior;
        if (p.available() > 0) { //packet 0x95 doesn't send id, since there is only one senior
            forkOn = c.getPlayer().getFamily().getEntryByID(p.readInt());
            if (!c.getPlayer().getFamilyEntry().isJunior(forkOn)) {
                return; //packet editing?
            }
            isSenior = true;
        } else {
            forkOn = c.getPlayer().getFamilyEntry();
            isSenior = false;
        }
        if (forkOn == null) {
            return;
        }

        FamilyEntry senior = forkOn.getSenior();
        if (senior == null) {
            return;
        }
        int levelDiff = Math.abs(c.getPlayer().getLevel() - senior.getLevel());
        int cost = 2500 * levelDiff;
        cost += levelDiff * levelDiff;
        if (c.getPlayer().getMeso() < cost) {
            c.sendPacket(PacketCreator.sendFamilyMessage(isSenior ? 81 : 80, cost));
            return;
        }
        c.getPlayer().gainMeso(-cost);
        int repCost = separateRepCost(forkOn);
        senior.gainReputation(-repCost, false);
        if (senior.getSenior() != null) {
            senior.getSenior().gainReputation(-(repCost / 2), false);
        }
        forkOn.announceToSenior(PacketCreator.serverNotice(5, forkOn.getName() + " has left the family."), true);
        forkOn.fork();
        c.sendPacket(PacketCreator.getFamilyInfo(forkOn)); //pedigree info will be requested from the client if the window is open
        forkOn.updateSeniorFamilyInfo(true);
        c.sendPacket(PacketCreator.sendFamilyMessage(1, 0));
    }


    private static int separateRepCost(FamilyEntry junior) {
        int level = junior.getLevel();
        int ret = level / 20;
        ret += 10;
        ret *= level;
        ret *= 2;
        return ret;
    }
}
