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

import client.Client;
import client.Family;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import net.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.DatabaseConnection;
import tools.PacketCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DeleteCharHandler extends AbstractPacketHandler {
    private static final Logger log = LoggerFactory.getLogger(DeleteCharHandler.class);

    @Override
    public void handlePacket(InPacket p, Client c) {
        String pic = p.readString();
        int cid = p.readInt();
        if (c.checkPic(pic)) {
            //check for family, guild leader, pending marriage, world transfer
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT `world`, `guildid`, `guildrank`, `familyId` FROM characters WHERE id = ?");
                 PreparedStatement ps2 = con.prepareStatement("SELECT COUNT(*) as rowcount FROM worldtransfers WHERE `characterid` = ? AND completionTime IS NULL")) {
                ps.setInt(1, cid);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Character record does not exist.");
                    }
                    int world = rs.getInt("world");
                    int guildId = rs.getInt("guildid");
                    int guildRank = rs.getInt("guildrank");
                    int familyId = rs.getInt("familyId");
                    if (guildId != 0 && guildRank <= 1) {
                        c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x16));
                        return;
                    } else if (familyId != -1) {
                        Family family = Server.getInstance().getWorld(world).getFamily(familyId);
                        if (family != null && family.getTotalMembers() > 1) {
                            c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x1D));
                            return;
                        }
                    }
                }

                ps2.setInt(1, cid);
                try (ResultSet rs = ps2.executeQuery()) {
                    rs.next();
                    if (rs.getInt("rowcount") > 0) {
                        c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x1A));
                        return;
                    }
                }
            } catch (SQLException e) {
                log.error("Failed to delete chrId {}", cid, e);
                c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x09));
                return;
            }
            if (c.deleteCharacter(cid, c.getAccID())) {
                log.info("Account {} deleted chrId {}", c.getAccountName(), cid);
                c.sendPacket(PacketCreator.deleteCharResponse(cid, 0));
            } else {
                c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x09));
            }
        } else {
            c.sendPacket(PacketCreator.deleteCharResponse(cid, 0x14));
        }
    }
}
