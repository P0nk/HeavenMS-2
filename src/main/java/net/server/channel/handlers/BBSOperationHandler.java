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
import net.AbstractPacketHandler;
import net.packet.InPacket;
import net.server.guild.GuildPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class BBSOperationHandler extends AbstractPacketHandler {
    private static final Logger log = LoggerFactory.getLogger(BBSOperationHandler.class);

    private String correctLength(String in, int maxSize) {
        return in.length() > maxSize ? in.substring(0, maxSize) : in;
    }

    @Override
    public void handlePacket(InPacket p, Client c) {
        if (c.getPlayer().getGuildId() < 1) {
            return;
        }
        byte mode = p.readByte();
        int localthreadid = 0;
        switch (mode) {
            case 0:
                boolean bEdit = p.readByte() == 1;
                if (bEdit) {
                    localthreadid = p.readInt();
                }
                boolean bNotice = p.readByte() == 1;
                String title = correctLength(p.readString(), 25);
                String text = correctLength(p.readString(), 600);
                int icon = p.readInt();
                if (icon >= 0x64 && icon <= 0x6a) {
                    if (!c.getPlayer().haveItemWithId(5290000 + icon - 0x64, false)) {
                        return;
                    }
                } else if (icon < 0 || icon > 3) {
                    return;
                }
                if (!bEdit) {
                    newBBSThread(c, title, text, icon, bNotice);
                } else {
                    editBBSThread(c, title, text, icon, localthreadid);
                }
                break;
            case 1:
                localthreadid = p.readInt();
                deleteBBSThread(c, localthreadid);
                break;
            case 2:
                int start = p.readInt();
                listBBSThreads(c, start * 10);
                break;
            case 3: // list thread + reply, following by id (int)
                localthreadid = p.readInt();
                displayThread(c, localthreadid);
                break;
            case 4: // reply
                localthreadid = p.readInt();
                text = correctLength(p.readString(), 25);
                newBBSReply(c, localthreadid, text);
                break;
            case 5: // delete reply
                p.readInt(); // we don't use this
                int replyid = p.readInt();
                deleteBBSReply(c, replyid);
                break;
            default:
                //System.out.println("Unhandled BBS mode: " + slea.toString());
        }
    }

    private static void listBBSThreads(Client c, int start) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM bbs_threads WHERE guildid = ? ORDER BY localthreadid DESC",
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            ps.setInt(1, c.getPlayer().getGuildId());
            try (ResultSet rs = ps.executeQuery()) {
                c.sendPacket(GuildPackets.BBSThreadList(rs, start));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private static void newBBSReply(Client c, int localthreadid, String text) {
        if (c.getPlayer().getGuildId() <= 0) {
            return;
        }
        try (Connection con = DatabaseConnection.getConnection()) {
            final int threadid;
            try (PreparedStatement ps = con.prepareStatement("SELECT threadid FROM bbs_threads WHERE guildid = ? AND localthreadid = ?")) {
                ps.setInt(1, c.getPlayer().getGuildId());
                ps.setInt(2, localthreadid);

                try (ResultSet threadRS = ps.executeQuery()) {
                    if (!threadRS.next()) {
                        return;
                    }

                    threadid = threadRS.getInt("threadid");
                }
            }

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO bbs_replies " + "(`threadid`, `postercid`, `timestamp`, `content`) VALUES " + "(?, ?, ?, ?)")) {
                ps.setInt(1, threadid);
                ps.setInt(2, c.getPlayer().getId());
                ps.setLong(3, currentServerTime());
                ps.setString(4, text);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement("UPDATE bbs_threads SET replycount = replycount + 1 WHERE threadid = ?")) {
                ps.setInt(1, threadid);
                ps.executeUpdate();
            }

            displayThread(c, localthreadid);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private static void editBBSThread(Client client, String title, String text, int icon, int localthreadid) {
        Character chr = client.getPlayer();
        if (chr.getGuildId() < 1) {
            return;
        }
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE bbs_threads SET `name` = ?, `timestamp` = ?, " + "`icon` = ?, " + "`startpost` = ? WHERE guildid = ? AND localthreadid = ? AND (postercid = ? OR ?)")) {

            ps.setString(1, title);
            ps.setLong(2, currentServerTime());
            ps.setInt(3, icon);
            ps.setString(4, text);
            ps.setInt(5, chr.getGuildId());
            ps.setInt(6, localthreadid);
            ps.setInt(7, chr.getId());
            ps.setBoolean(8, chr.getGuildRank() < 3);
            ps.execute();

            displayThread(client, localthreadid);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private static void newBBSThread(Client client, String title, String text, int icon, boolean bNotice) {
        Character chr = client.getPlayer();
        if (chr.getGuildId() <= 0) {
            return;
        }
        int nextId = 0;
        try (Connection con = DatabaseConnection.getConnection()) {
            if (!bNotice) {
                try (PreparedStatement ps = con.prepareStatement("SELECT MAX(localthreadid) AS lastLocalId FROM bbs_threads WHERE guildid = ?")) {
                    ps.setInt(1, chr.getGuildId());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        nextId = rs.getInt("lastLocalId") + 1;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO bbs_threads (`postercid`, `name`, `timestamp`, `icon`, `startpost`, `guildid`, `localthreadid`) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, chr.getId());
                ps.setString(2, title);
                ps.setLong(3, currentServerTime());
                ps.setInt(4, icon);
                ps.setString(5, text);
                ps.setInt(6, chr.getGuildId());
                ps.setInt(7, nextId);
                ps.executeUpdate();
            }

            displayThread(client, nextId);
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    public static void deleteBBSThread(Client client, int localthreadid) {
        Character mc = client.getPlayer();
        if (mc.getGuildId() <= 0) {
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {

            final int threadid;
            try (PreparedStatement ps = con.prepareStatement("SELECT threadid, postercid FROM bbs_threads WHERE guildid = ? AND localthreadid = ?")) {
                ps.setInt(1, mc.getGuildId());
                ps.setInt(2, localthreadid);

                try (ResultSet threadRS = ps.executeQuery()) {
                    if (!threadRS.next()) {
                        return;
                    }

                    if (mc.getId() != threadRS.getInt("postercid") && mc.getGuildRank() > 2) {
                        return;
                    }

                    threadid = threadRS.getInt("threadid");
                }
            }

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM bbs_replies WHERE threadid = ?")) {
                ps.setInt(1, threadid);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM bbs_threads WHERE threadid = ?")) {
                ps.setInt(1, threadid);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void deleteBBSReply(Client client, int replyid) {
        Character mc = client.getPlayer();
        if (mc.getGuildId() <= 0) {
            return;
        }

        final int threadid;
        try (Connection con = DatabaseConnection.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement("SELECT postercid, threadid FROM bbs_replies WHERE replyid = ?")) {
                ps.setInt(1, replyid);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return;
                    }

                    if (mc.getId() != rs.getInt("postercid") && mc.getGuildRank() > 2) {
                        return;
                    }

                    threadid = rs.getInt("threadid");
                }
            }

            try (PreparedStatement ps = con.prepareStatement("DELETE FROM bbs_replies WHERE replyid = ?")) {
                ps.setInt(1, replyid);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement("UPDATE bbs_threads SET replycount = replycount - 1 WHERE threadid = ?")) {
                ps.setInt(1, threadid);
                ps.executeUpdate();
            }

            displayThread(client, threadid, false);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void displayThread(Client client, int threadid) {
        displayThread(client, threadid, true);
    }

    public static void displayThread(Client client, int threadid, boolean bIsThreadIdLocal) {
        Character mc = client.getPlayer();
        if (mc.getGuildId() <= 0) {
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            // TODO clean up this block and use try-with-resources
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM bbs_threads WHERE guildid = ? AND " + (bIsThreadIdLocal ? "local" : "") + "threadid = ?")) {
                ps.setInt(1, mc.getGuildId());
                ps.setInt(2, threadid);
                ResultSet threadRS = ps.executeQuery();
                if (!threadRS.next()) {
                    return;
                }
                ResultSet repliesRS = null;
                try (PreparedStatement ps2 = con.prepareStatement("SELECT * FROM bbs_replies WHERE threadid = ?")) {
                    if (threadRS.getInt("replycount") >= 0) {
                        ps2.setInt(1, !bIsThreadIdLocal ? threadid : threadRS.getInt("threadid"));
                        repliesRS = ps2.executeQuery();
                    }
                    client.sendPacket(GuildPackets.showThread(bIsThreadIdLocal ? threadid : threadRS.getInt("localthreadid"), threadRS, repliesRS));
                }
            }
        } catch (SQLException se) {
            log.error("Error displaying thread", se);
        } catch (RuntimeException re) {//btw we get this everytime for some reason, but replies work!
            log.error("The number of reply rows does not match the replycount in thread.", re);
        }
    }
}
