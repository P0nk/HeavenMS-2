package net.server.channel.handlers;

import client.MapleClient;
import config.YamlConfig;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import net.packet.Packet;
import tools.LogHelper;
import tools.PacketCreator;

/**
 *
 * @author kevintjuh93
 */
public class AdminChatHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(InPacket p, MapleClient c) {
        if (!c.getPlayer().isGM()) {//if ( (signed int)CWvsContext::GetAdminLevel((void *)v294) > 2 )
            return;
        }
        byte mode = p.readByte();
        //not saving slides...
        String message = p.readString();
        Packet packet = PacketCreator.serverNotice(p.readByte(), message);//maybe I should make a check for the slea.readByte()... but I just hope gm's don't fuck things up :)
        switch (mode) {
            case 0:// /alertall, /noticeall, /slideall
                c.getWorldServer().broadcastPacket(packet);
                if (YamlConfig.config.server.USE_ENABLE_CHAT_LOG) {
                    LogHelper.logChat(c, "Alert All", message);
                }
                break;
            case 1:// /alertch, /noticech, /slidech
                c.getChannelServer().broadcastPacket(packet);
                if (YamlConfig.config.server.USE_ENABLE_CHAT_LOG) {
                    LogHelper.logChat(c, "Alert Ch", message);
                }
                break;
            case 2:// /alertm /alertmap, /noticem /noticemap, /slidem /slidemap
                c.getPlayer().getMap().broadcastMessage(packet);
                if (YamlConfig.config.server.USE_ENABLE_CHAT_LOG) {
                    LogHelper.logChat(c, "Alert Map", message);
                }
                break;

        }
    }
}
