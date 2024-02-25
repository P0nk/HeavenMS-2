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

import client.BuffStat;
import client.Character;
import client.Client;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import server.life.Monster;
import server.maps.MapObject;
import tools.PacketCreator;
import tools.Pair;

import java.util.Collections;
import java.util.List;

/**
 * @author Ronan
 */
public final class PlayerMapTransitionHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        Character chr = c.getPlayer();
        chr.setMapTransitionComplete();

        int beaconid = chr.getBuffSource(BuffStat.HOMING_BEACON);
        if (beaconid != -1) {
            chr.cancelBuffStats(BuffStat.HOMING_BEACON);

            final List<Pair<BuffStat, Integer>> stat = Collections.singletonList(new Pair<>(BuffStat.HOMING_BEACON, 0));
            chr.sendPacket(PacketCreator.giveBuff(1, beaconid, stat));
        }

        if (!chr.isHidden()) {  // thanks Lame (Conrad) for noticing hidden characters controlling mobs
            for (MapObject mo : chr.getMap().getMonsters()) {    // thanks BHB, IxianMace, Jefe for noticing several issues regarding mob statuses (such as freeze)
                Monster m = (Monster) mo;
                if (m.getSpawnEffect() == 0 || m.getHp() < m.getMaxHp()) {     // avoid effect-spawning mobs
                    if (m.getController() == chr) {
                        c.sendPacket(PacketCreator.stopControllingMonster(m.getObjectId()));
                        m.sendDestroyData(c);
                        m.aggroRemoveController();
                    } else {
                        m.sendDestroyData(c);
                    }

                    m.sendSpawnData(c);
                    m.aggroSwitchController(chr, false);
                }
            }
        }
    }
}