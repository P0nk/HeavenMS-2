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
package client;

import net.packet.Packet;
import net.server.Server;
import net.server.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.DatabaseConnection;
import tools.PacketCreator;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jay Estrella - Mr.Trash :3
 * @author Ubaware
 */
public class Family {
    private static final Logger log = LoggerFactory.getLogger(Family.class);
    private static final AtomicInteger familyIDCounter = new AtomicInteger();

    private final int id, world;
    private final Map<Integer, FamilyEntry> members = new ConcurrentHashMap<>();
    private FamilyEntry leader;
    private String name;
    private String preceptsMessage = "";
    private int totalGenerations;

    public Family(int id, int world) {
        int newId = id;
        if (id == -1) {
            // get next available family id
            while (idInUse(newId = familyIDCounter.incrementAndGet())) {
            }
        }
        this.id = newId;
        this.world = world;
    }

    private static boolean idInUse(int id) {
        for (World world : Server.getInstance().getWorlds()) {
            if (world.getFamily(id) != null) {
                return true;
            }
        }
        return false;
    }

    public int getID() {
        return id;
    }

    public int getWorld() {
        return world;
    }

    public void setLeader(FamilyEntry leader) {
        this.leader = leader;
        setName(leader.getName());
    }

    public FamilyEntry getLeader() {
        return leader;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getTotalMembers() {
        return members.size();
    }

    public int getTotalGenerations() {
        return totalGenerations;
    }

    public void setTotalGenerations(int generations) {
        this.totalGenerations = generations;
    }

    public String getName() {
        return this.name;
    }

    public void setMessage(String message, boolean save) {
        this.preceptsMessage = message;
        if (save) {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE family_character SET precepts = ? WHERE cid = ?")) {
                ps.setString(1, message);
                ps.setInt(2, getLeader().getChrId());
                ps.executeUpdate();
            } catch (SQLException e) {
                log.error("Could not save new precepts for family {}", getID(), e);
            }
        }
    }

    public String getMessage() {
        return preceptsMessage;
    }

    public void addEntry(FamilyEntry entry) {
        members.put(entry.getChrId(), entry);
    }

    public void removeEntryBranch(FamilyEntry root) {
        members.remove(root.getChrId());
        for (FamilyEntry junior : root.getJuniors()) {
            if (junior != null) {
                removeEntryBranch(junior);
            }
        }
    }

    public void addEntryTree(FamilyEntry root) {
        members.put(root.getChrId(), root);
        for (FamilyEntry junior : root.getJuniors()) {
            if (junior != null) {
                addEntryTree(junior);
            }
        }
    }

    public FamilyEntry getEntryByID(int cid) {
        return members.get(cid);
    }

    public void broadcast(Packet packet) {
        broadcast(packet, -1);
    }

    public void broadcast(Packet packet, int ignoreID) {
        for (FamilyEntry entry : members.values()) {
            Character chr = entry.getChr();
            if (chr != null) {
                if (chr.getId() == ignoreID) {
                    continue;
                }
                chr.sendPacket(packet);
            }
        }
    }

    public void broadcastFamilyInfoUpdate() {
        for (FamilyEntry entry : members.values()) {
            Character chr = entry.getChr();
            if (chr != null) {
                chr.sendPacket(PacketCreator.getFamilyInfo(entry));
            }
        }
    }

    public void resetDailyReps() {
        for (FamilyEntry entry : members.values()) {
            entry.setTodaysRep(0);
            entry.setRepsToSenior(0);
            entry.resetEntitlementUsages();
        }
    }

    public static void loadAllFamilies(Connection con) {
        List<Pair<Pair<Integer, Integer>, FamilyEntry>> unmatchedJuniors = new ArrayList<>(200); // <<world, seniorid> familyEntry>
        try (PreparedStatement psEntries = con.prepareStatement("SELECT * FROM family_character")) {
            ResultSet rsEntries = psEntries.executeQuery();
            while (rsEntries.next()) { // can be optimized
                int cid = rsEntries.getInt("cid");
                String name = null;
                int level = -1;
                int jobID = -1;
                int world = -1;
                try (PreparedStatement ps = con.prepareStatement("SELECT world, name, level, job FROM characters WHERE id = ?")) {
                    ps.setInt(1, cid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        world = rs.getInt("world");
                        name = rs.getString("name");
                        level = rs.getInt("level");
                        jobID = rs.getInt("job");
                    } else {
                        log.error("Could not load character information of chrId {} in loadAllFamilies(). (RECORD DOES NOT EXIST)", cid);
                        continue;
                    }
                } catch (SQLException e) {
                    log.error("Could not load character information of chrId {} in loadAllFamilies(). (SQL ERROR)", cid, e);
                    continue;
                }
                int familyid = rsEntries.getInt("familyid");
                int seniorid = rsEntries.getInt("seniorid");
                int reputation = rsEntries.getInt("reputation");
                int todaysRep = rsEntries.getInt("todaysrep");
                int totalRep = rsEntries.getInt("totalreputation");
                int repsToSenior = rsEntries.getInt("reptosenior");
                String precepts = rsEntries.getString("precepts");
                //Timestamp lastResetTime = rsEntries.getTimestamp("lastresettime"); //taken care of by FamilyDailyResetTask
                World wserv = Server.getInstance().getWorld(world);
                if (wserv == null) {
                    continue;
                }
                Family family = wserv.getFamily(familyid);
                if (family == null) {
                    family = new Family(familyid, world);
                    Server.getInstance().getWorld(world).addFamily(familyid, family);
                }
                FamilyEntry familyEntry = new FamilyEntry(family, cid, name, level, Job.getById(jobID));
                family.addEntry(familyEntry);
                if (seniorid <= 0) {
                    family.setLeader(familyEntry);
                    family.setMessage(precepts, false);
                }
                FamilyEntry senior = family.getEntryByID(seniorid);
                if (senior != null) {
                    familyEntry.setSenior(family.getEntryByID(seniorid), false);
                } else {
                    if (seniorid > 0) {
                        unmatchedJuniors.add(new Pair<>(new Pair<>(world, seniorid), familyEntry));
                    }
                }
                familyEntry.setReputation(reputation);
                familyEntry.setTodaysRep(todaysRep);
                familyEntry.setTotalReputation(totalRep);
                familyEntry.setRepsToSenior(repsToSenior);
                //load used entitlements
                try (PreparedStatement ps = con.prepareStatement("SELECT entitlementid FROM family_entitlement WHERE charid = ?")) {
                    ps.setInt(1, familyEntry.getChrId());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        familyEntry.setEntitlementUsed(rs.getInt("entitlementid"));
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Could not get family_character entries", e);
        }
        // link missing ones (out of order)
        for (Pair<Pair<Integer, Integer>, FamilyEntry> unmatchedJunior : unmatchedJuniors) {
            int world = unmatchedJunior.getLeft().getLeft();
            int seniorid = unmatchedJunior.getLeft().getRight();
            FamilyEntry junior = unmatchedJunior.getRight();
            FamilyEntry senior = Server.getInstance().getWorld(world).getFamily(junior.getFamily().getID()).getEntryByID(seniorid);
            if (senior != null) {
                junior.setSenior(senior, false);
            } else {
                log.error("Missing senior for chr {} in world {}", junior.getName(), world);
            }
        }

        for (World world : Server.getInstance().getWorlds()) {
            for (Family family : world.getFamilies()) {
                family.getLeader().doFullCount();
            }
        }
    }

    public void saveAllMembersRep() { //was used for autosave task, but character autosave should be enough
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            boolean success = true;
            for (FamilyEntry entry : members.values()) {
                success = entry.saveReputation(con);
                if (!success) {
                    break;
                }
            }
            if (!success) {
                con.rollback();
                log.error("Family rep autosave failed for family {}", getID());
            }
            con.setAutoCommit(true);
            //reset repChanged after successful save
            for (FamilyEntry entry : members.values()) {
                entry.savedSuccessfully();
            }
        } catch (SQLException e) {
            log.error("Could not get connection to DB while saving all members rep", e);
        }
    }
}
