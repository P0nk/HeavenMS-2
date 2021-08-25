package net.server.channel.handlers;

import client.MapleClient;
import client.MapleFamily;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import tools.PacketCreator;

public class FamilyPreceptsHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(InPacket p, MapleClient c) {
        MapleFamily family = c.getPlayer().getFamily();
        if(family == null) return;
        if(family.getLeader().getChr() != c.getPlayer()) return; //only the leader can set the precepts
        String newPrecepts = p.readString();
        if(newPrecepts.length() > 200) return;
        family.setMessage(newPrecepts, true);
        //family.broadcastFamilyInfoUpdate(); //probably don't need to broadcast for this?
        c.sendPacket(PacketCreator.getFamilyInfo(c.getPlayer().getFamilyEntry()));
    }

}
