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

import client.Client;
import client.inventory.InventoryType;
import client.inventory.Item;
import constants.id.ItemId;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import tools.PacketCreator;

public final class UseItemEffectHandler extends AbstractPacketHandler {
    @Override
    public final void handlePacket(InPacket p, Client c) {
        Item toUse;
        int itemId = p.readInt();
        if (itemId == ItemId.BUMMER_EFFECT || itemId == ItemId.GOLDEN_CHICKEN_EFFECT) {
            toUse = c.getPlayer().getInventory(InventoryType.ETC).findById(itemId);
        } else {
            toUse = c.getPlayer().getInventory(InventoryType.CASH).findById(itemId);
        }
        if (toUse == null || toUse.getQuantity() < 1) {
            if (itemId != 0) {
                return;
            }
        }
        c.getPlayer().setItemEffect(itemId);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PacketCreator.itemEffect(c.getPlayer().getId(), itemId), false);
    }
}
