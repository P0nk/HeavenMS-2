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

import client.MapleCharacter;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import tools.PacketCreator;

/**
 *
 * @author Matze
 * @author Ronan - concurrency protection
 */
public final class MesoDropHandler extends AbstractMaplePacketHandler {
        @Override
        public final void handlePacket(InPacket p, MapleClient c) {
            MapleCharacter player = c.getPlayer();
            if (!player.isAlive()) {
                c.sendPacket(PacketCreator.enableActions());
                return;
            }
            p.skip(4);
            int meso = p.readInt();

            if (c.tryacquireClient()) {     // thanks imbee for noticing players not being able to throw mesos too fast
                try {
                    if (meso <= player.getMeso() && meso > 9 && meso < 50001) {
                        player.gainMeso(-meso, false, true, false);
                    } else {
                        c.sendPacket(PacketCreator.enableActions());
                        return;
                    }
                } finally {
                    c.releaseClient();
                }
            } else {
                c.sendPacket(PacketCreator.enableActions());
                return;
            }

            if (player.attemptCatchFish(meso)) {
                player.getMap().disappearingMesoDrop(meso, player, player, player.getPosition());
            } else {
                player.getMap().spawnMesoDrop(meso, player.getPosition(), player, player, true, (byte) 2);
            }
        }
}