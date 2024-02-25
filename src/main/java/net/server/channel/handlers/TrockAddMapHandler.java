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
import server.maps.FieldLimit;
import tools.PacketCreator;

/**
 * @author kevintjuh93
 */
public final class TrockAddMapHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        Character chr = c.getPlayer();
        byte type = p.readByte();
        boolean vip = p.readByte() == 1;
        if (type == 0x00) {
            int mapId = p.readInt();
            if (vip) {
                chr.deleteFromVipTrocks(mapId);
            } else {
                chr.deleteFromTrocks(mapId);
            }
            c.sendPacket(PacketCreator.trockRefreshMapList(chr, true, vip));
        } else if (type == 0x01) {
            if (!FieldLimit.CANNOTVIPROCK.check(chr.getMap().getFieldLimit())) {
                if (vip) {
                    chr.addVipTrockMap();
                } else {
                    chr.addTrockMap();
                }

                c.sendPacket(PacketCreator.trockRefreshMapList(chr, false, vip));
            } else {
                chr.message("You may not save this map.");
            }
        }
    }
}
