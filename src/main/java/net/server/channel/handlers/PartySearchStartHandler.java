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
package net.server.channel.handlers;

import client.Character;
import client.Client;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import net.server.world.Party;
import net.server.world.World;
import tools.PacketCreator;

/**
 * @author XoticStory
 * @author BubblesDev
 * @author Ronan
 */
public class PartySearchStartHandler extends AbstractPacketHandler {
    @Override
    public void handlePacket(InPacket p, Client c) {
        int min = p.readInt();
        int max = p.readInt();

        Character chr = c.getPlayer();
        if (min > max) {
            chr.dropMessage(1, "The min. value is higher than the max!");
            c.sendPacket(PacketCreator.enableActions());
            return;
        }

        if (max - min > 30) {
            chr.dropMessage(1, "You can only search for party members within a range of 30 levels.");
            c.sendPacket(PacketCreator.enableActions());
            return;
        }

        if (chr.getLevel() < min || chr.getLevel() > max) {
            chr.dropMessage(1, "The range of level for search has to include your own level.");
            c.sendPacket(PacketCreator.enableActions());
            return;
        }

        p.readInt(); // members
        int jobs = p.readInt();

        Party party = c.getPlayer().getParty();
        if (party == null || !c.getPlayer().isPartyLeader()) {
            return;
        }

        World world = c.getWorldServer();
        world.getPartySearchCoordinator().registerPartyLeader(chr, min, max, jobs);
    }
}