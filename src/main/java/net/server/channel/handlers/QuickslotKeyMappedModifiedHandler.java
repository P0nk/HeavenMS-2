package net.server.channel.handlers;

import client.MapleClient;
import client.keybind.MapleQuickslotBinding;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;

/**
 *
 * @author Shavit
 */
public class QuickslotKeyMappedModifiedHandler extends AbstractMaplePacketHandler
{
    @Override
    public void handlePacket(InPacket p, MapleClient c)
    {
        // Invalid size for the packet.
        if(p.available() != MapleQuickslotBinding.QUICKSLOT_SIZE * Integer.BYTES ||
        // not logged in-game
            c.getPlayer() == null)
        {
            return;
        }

        byte[] aQuickslotKeyMapped = new byte[MapleQuickslotBinding.QUICKSLOT_SIZE];

        for(int i = 0; i < MapleQuickslotBinding.QUICKSLOT_SIZE; i++)
        {
            aQuickslotKeyMapped[i] = (byte) p.readInt();
        }

        c.getPlayer().changeQuickslotKeybinding(aQuickslotKeyMapped);
    }
}
