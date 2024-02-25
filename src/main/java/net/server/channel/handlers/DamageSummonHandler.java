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

import client.BuffStat;
import client.Character;
import client.Client;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import server.maps.MapObject;
import server.maps.Summon;
import tools.PacketCreator;

public final class DamageSummonHandler extends AbstractPacketHandler {
    @Override
    public final void handlePacket(InPacket p, Client c) {
        int oid = p.readInt();
        p.skip(1);   // -1
        int damage = p.readInt();
        int monsterIdFrom = p.readInt();

        Character player = c.getPlayer();
        MapObject mmo = player.getMap().getMapObject(oid);

        if (mmo != null && mmo instanceof Summon) {
            Summon summon = (Summon) mmo;

            summon.addHP(-damage);
            if (summon.getHP() <= 0) {
                player.cancelEffectFromBuffStat(BuffStat.PUPPET);
            }
            player.getMap().broadcastMessage(player, PacketCreator.damageSummon(player.getId(), oid, damage, monsterIdFrom), summon.getPosition());
        }
    }
}
