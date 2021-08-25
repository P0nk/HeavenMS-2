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

import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import server.maps.MapleMapObject;
import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import tools.FilePrinter;

/**
 *
 * @author Matze
 * @author Ronan
 */
public final class ItemPickupHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(final InPacket p, final MapleClient c) {
        p.readInt(); //Timestamp
        p.readByte();
        p.readPos(); //cpos
        int oid = p.readInt();
        MapleCharacter chr = c.getPlayer();
        MapleMapObject ob = chr.getMap().getMapObject(oid);
        if(ob == null) return;
        
        Point charPos = chr.getPosition();
        Point obPos = ob.getPosition();
        if (Math.abs(charPos.getX() - obPos.getX()) > 800 || Math.abs(charPos.getY() - obPos.getY()) > 600) {
            FilePrinter.printError(FilePrinter.EXPLOITS + c.getPlayer().getName() + ".txt", c.getPlayer().getName() + " tried to pick up an item too far away. Mapid: " + chr.getMapId() + " Player pos: " + charPos + " Object pos: " + obPos);
            return;
        }
        
        chr.pickupItem(ob);
    }
}
