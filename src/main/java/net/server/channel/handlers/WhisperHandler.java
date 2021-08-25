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

import client.MapleCharacter;
import client.MapleClient;
import client.autoban.AutobanFactory;
import config.YamlConfig;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import tools.FilePrinter;
import tools.LogHelper;
import tools.PacketCreator;
import tools.PacketCreator.WhisperFlag;

/**
 * @author Chronos
 */
public final class WhisperHandler extends AbstractMaplePacketHandler {

    // result types, not sure if there are proper names for these
    public static final byte RT_ITC = 0x00;
    public static final byte RT_SAME_CHANNEL = 0x01;
    public static final byte RT_CASH_SHOP = 0x02;
    public static final byte RT_DIFFERENT_CHANNEL = 0x03;

    @Override
    public void handlePacket(InPacket p, MapleClient c) {
        byte request = p.readByte();
        String name = p.readString();
        MapleCharacter target = c.getWorldServer().getPlayerStorage().getCharacterByName(name);

        if (target == null) {
            c.sendPacket(PacketCreator.getWhisperResult(name, false));
            return;
        }

        switch (request) {
            case WhisperFlag.LOCATION | WhisperFlag.REQUEST:
                handleFind(c.getPlayer(), target, WhisperFlag.LOCATION);
                break;
            case WhisperFlag.WHISPER | WhisperFlag.REQUEST:
                String message = p.readString();
                handleWhisper(message, c.getPlayer(), target);
                break;
            case WhisperFlag.LOCATION_FRIEND | WhisperFlag.REQUEST:
                handleFind(c.getPlayer(), target, WhisperFlag.LOCATION_FRIEND);
                break;
            default:
                FilePrinter.printError(FilePrinter.PACKET_HANDLER + c.getPlayer().getName() + ".txt", "Unknown request " + request + " triggered by " + c.getPlayer().getName());
                break;
        }
    }

    private void handleFind(MapleCharacter user, MapleCharacter target, byte flag) {
        if (user.gmLevel() >= target.gmLevel()) {
            if (target.getCashShop().isOpened()) {
                user.sendPacket(PacketCreator.getFindResult(target, RT_CASH_SHOP, -1, flag));
            } else if (target.getClient().getChannel() == user.getClient().getChannel()) {
                user.sendPacket(PacketCreator.getFindResult(target, RT_SAME_CHANNEL, target.getMapId(), flag));
            } else {
                user.sendPacket(PacketCreator.getFindResult(target, RT_DIFFERENT_CHANNEL, target.getClient().getChannel() - 1, flag));
            }
        } else {
            // not found for whisper is the same message
            user.sendPacket(PacketCreator.getWhisperResult(target.getName(), false));
        }
    }

    private void handleWhisper(String message, MapleCharacter user, MapleCharacter target) {
        if (user.getAutobanManager().getLastSpam(7) + 200 > currentServerTime()) {
            return;
        }
        user.getAutobanManager().spam(7);

        if (message.length() > Byte.MAX_VALUE) {
            AutobanFactory.PACKET_EDIT.alert(user, user.getName() + " tried to packet edit with whispers.");
            FilePrinter.printError(FilePrinter.EXPLOITS + user.getName() + ".txt", user.getName() + " tried to send text with length of " + message.length());
            user.getClient().disconnect(true, false);
            return;
        }

        if (YamlConfig.config.server.USE_ENABLE_CHAT_LOG) {
            LogHelper.logChat(user.getClient(), "Whisper To " + target.getName(), message);
        }

        target.sendPacket(PacketCreator.getWhisperReceive(user.getName(), user.getClient().getChannel() - 1, user.isGM(), message));

        boolean hidden = target.isHidden() && target.gmLevel() > user.gmLevel();
        user.sendPacket(PacketCreator.getWhisperResult(target.getName(), !hidden));
    }
}
