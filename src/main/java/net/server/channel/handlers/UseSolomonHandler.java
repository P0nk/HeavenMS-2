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
import client.inventory.Inventory;
import client.inventory.InventoryType;
import client.inventory.Item;
import client.inventory.manipulator.InventoryManipulator;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import server.ItemInformationProvider;
import tools.PacketCreator;

/**
 * @author XoticStory
 * <p>
 * Modified by -- kevintjuh93, Ronan
 */
public final class UseSolomonHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        p.readInt();
        short slot = p.readShort();
        int itemId = p.readInt();
        ItemInformationProvider ii = ItemInformationProvider.getInstance();

        if (c.tryacquireClient()) {
            try {
                Character chr = c.getPlayer();
                Inventory inv = chr.getInventory(InventoryType.USE);
                inv.lockInventory();
                try {
                    Item slotItem = inv.getItem(slot);
                    if (slotItem == null) {
                        return;
                    }

                    long gachaexp = ii.getExpById(itemId);
                    if (slotItem.getItemId() != itemId || slotItem.getQuantity() <= 0 || chr.getLevel() > ii.getMaxLevelById(itemId)) {
                        return;
                    }
                    if (gachaexp + chr.getGachaExp() > Integer.MAX_VALUE) {
                        return;
                    }
                    chr.addGachaExp((int) gachaexp);
                    InventoryManipulator.removeFromSlot(c, InventoryType.USE, slot, (short) 1, false);
                } finally {
                    inv.unlockInventory();
                }
            } finally {
                c.releaseClient();
            }
        }

        c.sendPacket(PacketCreator.enableActions());
    }
}
