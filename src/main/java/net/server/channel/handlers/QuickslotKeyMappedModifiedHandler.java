package net.server.channel.handlers;

import client.Client;
import client.keybind.QuickslotBinding;
import net.AbstractPacketHandler;
import net.packet.InPacket;

/**
 * @author Shavit
 */
public class QuickslotKeyMappedModifiedHandler extends AbstractPacketHandler {
    @Override
    public void handlePacket(InPacket p, Client c) {
        // Invalid size for the packet.
        if (p.available() != QuickslotBinding.QUICKSLOT_SIZE * Integer.BYTES ||
                // not logged in-game
                c.getPlayer() == null) {
            return;
        }

        byte[] aQuickslotKeyMapped = new byte[QuickslotBinding.QUICKSLOT_SIZE];

        for (int i = 0; i < QuickslotBinding.QUICKSLOT_SIZE; i++) {
            aQuickslotKeyMapped[i] = (byte) p.readInt();
        }

        c.getPlayer().changeQuickslotKeybinding(aQuickslotKeyMapped);
    }
}
