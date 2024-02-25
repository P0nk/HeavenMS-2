package net.server.channel.handlers;

import client.Character;
import client.Client;
import constants.id.NpcId;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import server.minigame.RockPaperScissor;
import tools.PacketCreator;

/**
 * @Author Arnah
 * @Website http://Vertisy.ca/
 * @since Aug 15, 2016
 */
public final class RPSActionHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        Character chr = c.getPlayer();
        RockPaperScissor rps = chr.getRPS();

        if (c.tryacquireClient()) {
            try {
                if (p.available() == 0 || !chr.getMap().containsNPC(NpcId.RPS_ADMIN)) {
                    if (rps != null) {
                        rps.dispose(c);
                    }
                    return;
                }
                final byte mode = p.readByte();
                switch (mode) {
                    case 0: // start game
                    case 5: // retry
                        if (rps != null) {
                            rps.reward(c);
                        }
                        if (chr.getMeso() >= 1000) {
                            chr.setRPS(new RockPaperScissor(c, mode));
                        } else {
                            c.sendPacket(PacketCreator.rpsMesoError(-1));
                        }
                        break;
                    case 1: // answer
                        if (rps == null || !rps.answer(c, p.readByte())) {
                            c.sendPacket(PacketCreator.rpsMode((byte) 0x0D));// 13
                        }
                        break;
                    case 2: // time over
                        if (rps == null || !rps.timeOut(c)) {
                            c.sendPacket(PacketCreator.rpsMode((byte) 0x0D));
                        }
                        break;
                    case 3: // continue
                        if (rps == null || !rps.nextRound(c)) {
                            c.sendPacket(PacketCreator.rpsMode((byte) 0x0D));
                        }
                        break;
                    case 4: // leave
                        if (rps != null) {
                            rps.dispose(c);
                        } else {
                            c.sendPacket(PacketCreator.rpsMode((byte) 0x0D));
                        }
                        break;
                }
            } finally {
                c.releaseClient();
            }
        }
    }
}
