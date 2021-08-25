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
package net.server.handlers.login;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import net.server.coordinator.session.SessionCoordinator;
import tools.PacketCreator;

/*
 * @author Rob
 */
public final class RegisterPinHandler extends AbstractMaplePacketHandler {
    @Override
    public final void handlePacket(InPacket p, MapleClient c) {
        byte c2 = p.readByte();
        if (c2 == 0) {
            SessionCoordinator.getInstance().closeSession(c, null);
            c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN);
        } else {
            String pin = p.readString();
            if (pin != null) {
                c.setPin(pin);
                c.sendPacket(PacketCreator.pinRegistered());
                
                SessionCoordinator.getInstance().closeSession(c, null);
                c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN);
            }
        }
    }
}
