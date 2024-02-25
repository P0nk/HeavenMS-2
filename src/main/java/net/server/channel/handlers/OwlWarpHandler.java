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
import constants.game.GameConstants;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import server.maps.HiredMerchant;
import server.maps.PlayerShop;
import tools.PacketCreator;

/*
 * @author Ronan
 */
public final class OwlWarpHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        int ownerid = p.readInt();
        int mapid = p.readInt();

        if (ownerid == c.getPlayer().getId()) {
            c.sendPacket(PacketCreator.serverNotice(1, "You cannot visit your own shop."));
            return;
        }

        HiredMerchant hm = c.getWorldServer().getHiredMerchant(ownerid);   // if both hired merchant and player shop is on the same map
        PlayerShop ps;
        if (hm == null || hm.getMapId() != mapid || !hm.hasItem(c.getPlayer().getOwlSearch())) {
            ps = c.getWorldServer().getPlayerShop(ownerid);
            if (ps == null || ps.getMapId() != mapid || !ps.hasItem(c.getPlayer().getOwlSearch())) {
                if (hm == null && ps == null) {
                    c.sendPacket(PacketCreator.getOwlMessage(1));
                } else {
                    c.sendPacket(PacketCreator.getOwlMessage(3));
                }
                return;
            }

            if (ps.isOpen()) {
                if (GameConstants.isFreeMarketRoom(mapid)) {
                    if (ps.getChannel() == c.getChannel()) {
                        c.getPlayer().changeMap(mapid);

                        if (ps.isOpen()) {   //change map has a delay, must double check
                            if (!ps.visitShop(c.getPlayer())) {
                                if (!ps.isBanned(c.getPlayer().getName())) {
                                    c.sendPacket(PacketCreator.getOwlMessage(2));
                                } else {
                                    c.sendPacket(PacketCreator.getOwlMessage(17));
                                }
                            }
                        } else {
                            //c.sendPacket(PacketCreator.serverNotice(1, "That merchant has either been closed or is under maintenance."));
                            c.sendPacket(PacketCreator.getOwlMessage(18));
                        }
                    } else {
                        c.sendPacket(PacketCreator.serverNotice(1, "That shop is currently located in another channel. Current location: Channel " + hm.getChannel() + ", '" + hm.getMap().getMapName() + "'."));
                    }
                } else {
                    c.sendPacket(PacketCreator.serverNotice(1, "That shop is currently located outside of the FM area. Current location: Channel " + hm.getChannel() + ", '" + hm.getMap().getMapName() + "'."));
                }
            } else {
                //c.sendPacket(PacketCreator.serverNotice(1, "That merchant has either been closed or is under maintenance."));
                c.sendPacket(PacketCreator.getOwlMessage(18));
            }
        } else {
            if (hm.isOpen()) {
                if (GameConstants.isFreeMarketRoom(mapid)) {
                    if (hm.getChannel() == c.getChannel()) {
                        c.getPlayer().changeMap(mapid);

                        if (hm.isOpen()) {   //change map has a delay, must double check
                            if (hm.addVisitor(c.getPlayer())) {
                                c.sendPacket(PacketCreator.getHiredMerchant(c.getPlayer(), hm, false));
                                c.getPlayer().setHiredMerchant(hm);
                            } else {
                                //c.sendPacket(PacketCreator.serverNotice(1, hm.getOwner() + "'s merchant is full. Wait awhile before trying again."));
                                c.sendPacket(PacketCreator.getOwlMessage(2));
                            }
                        } else {
                            //c.sendPacket(PacketCreator.serverNotice(1, "That merchant has either been closed or is under maintenance."));
                            c.sendPacket(PacketCreator.getOwlMessage(18));
                        }
                    } else {
                        c.sendPacket(PacketCreator.serverNotice(1, "That merchant is currently located in another channel. Current location: Channel " + hm.getChannel() + ", '" + hm.getMap().getMapName() + "'."));
                    }
                } else {
                    c.sendPacket(PacketCreator.serverNotice(1, "That merchant is currently located outside of the FM area. Current location: Channel " + hm.getChannel() + ", '" + hm.getMap().getMapName() + "'."));
                }
            } else {
                //c.sendPacket(PacketCreator.serverNotice(1, "That merchant has either been closed or is under maintenance."));
                c.sendPacket(PacketCreator.getOwlMessage(18));
            }
        }
    }
}