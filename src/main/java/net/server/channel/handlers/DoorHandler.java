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
import server.maps.DoorObject;
import server.maps.MapObject;
import tools.PacketCreator;

/**
 * @author Matze
 */
public final class DoorHandler extends AbstractPacketHandler {
    @Override
    public final void handlePacket(InPacket p, Client c) {
        int ownerid = p.readInt();
        p.readByte(); // specifies if backwarp or not, 1 town to target, 0 target to town

        Character chr = c.getPlayer();
        if (chr.isChangingMaps() || chr.isBanned()) {
            c.sendPacket(PacketCreator.enableActions());
            return;
        }

        for (MapObject obj : chr.getMap().getMapObjects()) {
            if (obj instanceof DoorObject door) {
                if (door.getOwnerId() == ownerid) {
                    door.warp(chr);
                    return;
                }
            }
        }

        c.sendPacket(PacketCreator.blockedMessage(6));
        c.sendPacket(PacketCreator.enableActions());
    }
}
