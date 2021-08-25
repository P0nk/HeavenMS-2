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

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import tools.DatabaseConnection;
import tools.PacketCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class NoteActionHandler extends AbstractMaplePacketHandler {
    @Override
    public final void handlePacket(InPacket p, MapleClient c) {
        int action = p.readByte();
        if (action == 0 && c.getPlayer().getCashShop().getAvailableNotes() > 0) {
            String charname = p.readString();
            String message = p.readString();
            try {
                if (c.getPlayer().getCashShop().isOpened()) {
                    c.sendPacket(PacketCreator.showCashInventory(c));
                }

                c.getPlayer().sendNote(charname, message, (byte) 1);
                c.getPlayer().getCashShop().decreaseNotes();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (action == 1) {
            int num = p.readByte();
            p.readByte();
            p.readByte();
            int fame = 0;
            for (int i = 0; i < num; i++) {
                int id = p.readInt();
                p.readByte(); //Fame, but we read it from the database :)

                try (Connection con = DatabaseConnection.getConnection()) {
                    try (PreparedStatement ps = con.prepareStatement("SELECT `fame` FROM notes WHERE id=? AND deleted=0")) {
                        ps.setInt(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                fame += rs.getInt("fame");
                            }

                        }
                    }

                    try (PreparedStatement ps = con.prepareStatement("UPDATE notes SET `deleted` = 1 WHERE id = ?")) {
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (fame > 0) {
                c.getPlayer().gainFame(fame);
            }
        }
    }
}
