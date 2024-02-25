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
import net.packet.InPacket;
import server.maps.Summon;
import tools.PacketCreator;
import tools.exceptions.EmptyMovementException;

import java.awt.*;
import java.util.Collection;

public final class MoveSummonHandler extends AbstractMovementPacketHandler {
    @Override
    public final void handlePacket(InPacket p, Client c) {
        int oid = p.readInt();
        Point startPos = new Point(p.readShort(), p.readShort());
        Character player = c.getPlayer();
        Collection<Summon> summons = player.getSummonsValues();
        Summon summon = null;
        for (Summon sum : summons) {
            if (sum.getObjectId() == oid) {
                summon = sum;
                break;
            }
        }
        if (summon != null) {
            try {
                int movementDataStart = p.getPosition();
                updatePosition(p, summon, 0);
                long movementDataLength = p.getPosition() - movementDataStart; //how many bytes were read by updatePosition
                p.seek(movementDataStart);

                player.getMap().broadcastMessage(player, PacketCreator.moveSummon(player.getId(), oid, startPos, p, movementDataLength), summon.getPosition());
            } catch (EmptyMovementException e) {
            }
        }
    }
}
