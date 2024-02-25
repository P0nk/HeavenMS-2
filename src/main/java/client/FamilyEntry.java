/*
    This file is part of the HeavenMS MapleStory Server
    Copyleft (L) 2016 - 2019 RonanLana

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.DatabaseConnection;
import tools.PacketCreator;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ubaware
 */

public class FamilyEntry {
    private static final Logger log = LoggerFactory.getLogger(FamilyEntry.class);

    private final int characterID;
    private volatile Family family;
    private volatile Character character;

    private volatile FamilyEntry senior;
    private final FamilyEntry[] juniors = new FamilyEntry[2];
    private final int[] entitlements = new int[11];
    private volatile int reputation, totalReputation;
    private volatile int todaysRep, repsToSenior; //both are daily values
    private volatile int totalJuniors, totalSeniors;

    private volatile int generation;

    private volatile boolean repChanged; //used to ignore saving unchanged rep values

    // cached values for offline players
    private String charName;
    private int level;
    private Job job;

    public FamilyEntry(Family family, int characterID, String charName, int level, Job job) {
        this.family = family;
        this.characterID = characterID;
        this.charName = charName;
        this.level = level;
        this.job = job;
    }

    public Character getChr() {
        return character;
    }

    public void setCharacter(Character newCharacter) {
        if (newCharacter == null) {
            cacheOffline(newCharacter);
        } else {
            newCharacter.setFamilyEntry(this);
        }
        this.character = newCharacter;
    }

    private void cacheOffline(Character chr) {
        if (chr != null) {
            charName = chr.getName();
            level = chr.getLevel();
            job = chr.getJob();
        }
    }

    public synchronized void join(FamilyEntry senior) {
        if (senior == null || getSenior() != null) {
            return;
        }
        Family oldFamily = getFamily();
        Family newFamily = senior.getFamily();
        setSenior(senior, false);
        addSeniorCount(newFamily.getTotalGenerations(), newFamily); //count will be overwritten by doFullCount()
        newFamily.getLeader().doFullCount(); //easier than keeping track of numbers
        oldFamily.setMessage(null, true);
        newFamily.addEntryTree(this);
        Server.getInstance().getWorld(oldFamily.getWorld()).removeFamily(oldFamily.getID());

        //db
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            boolean success = updateDBChangeFamily(con, getChrId(), newFamily.getID(), senior.getChrId());
            for (FamilyEntry junior : juniors) { // better to duplicate this than the SQL code
                if (junior != null) {
                    success = junior.updateNewFamilyDB(con); // recursively updates juniors in db
                    if (!success) {
                        break;
                    }
                }
            }
            if (!success) {
                con.rollback();
                log.error("Could not absorb {}'s family into {}'s family. (SQL ERROR)", oldFamily.getName(), newFamily.getName());
            }
            con.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Could not get connection to DB when joining families", e);
        }
    }

    public synchronized void fork() {
        Family oldFamily = getFamily();
        FamilyEntry oldSenior = getSenior();
        family = new Family(-1, oldFamily.getWorld());
        Server.getInstance().getWorld(family.getWorld()).addFamily(family.getID(), family);
        setSenior(null, false);
        family.setLeader(this);
        addSeniorCount(-getTotalSeniors(), family);
        setTotalSeniors(0);
        if (oldSenior != null) {
            oldSenior.addJuniorCount(-getTotalJuniors());
            oldSenior.removeJunior(this);
            oldFamily.getLeader().doFullCount();
        }
        oldFamily.removeEntryBranch(this);
        family.addEntryTree(this);
        this.repsToSenior = 0;
        this.repChanged = true;
        family.setMessage("", true);
        doFullCount(); //to make sure all counts are correct
        // update db
        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            boolean success = updateDBChangeFamily(con, getChrId(), getFamily().getID(), 0);

            for (FamilyEntry junior : juniors) { // better to duplicate this than the SQL code
                if (junior != null) {
                    success = junior.updateNewFamilyDB(con); // recursively updates juniors in db
                    if (!success) {
                        break;
                    }
                }
            }
            if (!success) {
                con.rollback();
                log.error("Could not fork family with new leader {}. (Old senior: {}, leader: {})", getName(), oldSenior.getName(), oldFamily.getLeader().getName());
            }
            con.setAutoCommit(true);

        } catch (SQLException e) {
            log.error("Could not get connection to DB when forking families", e);
        }
    }

    private synchronized boolean updateNewFamilyDB(Connection con) {
        if (!updateFamilyEntryDB(con, getChrId(), getFamily().getID())) {
            return false;
        }
        if (!updateCharacterFamilyDB(con, getChrId(), getFamily().getID(), true)) {
            return false;
        }

        for (FamilyEntry junior : juniors) {
            if (junior != null) {
                if (!junior.updateNewFamilyDB(con)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean updateFamilyEntryDB(Connection con, int cid, int familyid) {
        try (PreparedStatement ps = con.prepareStatement("UPDATE family_character SET familyid = ? WHERE cid = ?")) {
            ps.setInt(1, familyid);
            ps.setInt(2, cid);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update family id in 'family_character' for chrId {}. (fork)", cid, e);
            return false;
        }
        return true;
    }

    private synchronized void addSeniorCount(int seniorCount, Family newFamily) { // traverses tree and subtracts seniors and updates family
        if (newFamily != null) {
            this.family = newFamily;
        }
        setTotalSeniors(getTotalSeniors() + seniorCount);
        this.generation += seniorCount;
        for (FamilyEntry junior : juniors) {
            if (junior != null) {
                junior.addSeniorCount(seniorCount, newFamily);
            }
        }
    }

    private synchronized void addJuniorCount(int juniorCount) { // climbs tree and adds junior count
        setTotalJuniors(getTotalJuniors() + juniorCount);
        FamilyEntry senior = getSenior();
        if (senior != null) {
            senior.addJuniorCount(juniorCount);
        }
    }

    public Family getFamily() {
        return family;
    }

    public int getChrId() {
        return characterID;
    }

    public String getName() {
        Character chr = character;
        if (chr != null) {
            return chr.getName();
        } else {
            return charName;
        }
    }

    public int getLevel() {
        Character chr = character;
        if (chr != null) {
            return chr.getLevel();
        } else {
            return level;
        }
    }

    public Job getJob() {
        Character chr = character;
        if (chr != null) {
            return chr.getJob();
        } else {
            return job;
        }
    }

    public int getReputation() {
        return reputation;
    }

    public int getTodaysRep() {
        return todaysRep;
    }

    public void setReputation(int reputation) {
        if (reputation != this.reputation) {
            this.repChanged = true;
        }
        this.reputation = reputation;
    }

    public void setTodaysRep(int today) {
        if (today != todaysRep) {
            this.repChanged = true;
        }
        this.todaysRep = today;
    }

    public int getRepsToSenior() {
        return repsToSenior;
    }

    public void setRepsToSenior(int reputation) {
        if (reputation != this.repsToSenior) {
            this.repChanged = true;
        }
        this.repsToSenior = reputation;
    }

    public void gainReputation(int gain, boolean countTowardsTotal) {
        gainReputation(gain, countTowardsTotal, this);
    }

    private void gainReputation(int gain, boolean countTowardsTotal, FamilyEntry from) {
        if (gain != 0) {
            repChanged = true;
        }
        this.reputation += gain;
        this.todaysRep += gain;
        if (gain > 0 && countTowardsTotal) {
            this.totalReputation += gain;
        }
        Character chr = getChr();
        if (chr != null) {
            chr.sendPacket(PacketCreator.sendGainRep(gain, from != null ? from.getName() : ""));
        }
    }

    public void giveReputationToSenior(int gain, boolean includeSuperSenior) {
        int actualGain = gain;
        FamilyEntry senior = getSenior();
        if (senior != null && senior.getLevel() < getLevel() && gain > 0) {
            actualGain /= 2; //don't halve negative values
        }
        if (senior != null) {
            senior.gainReputation(actualGain, true, this);
            if (actualGain > 0) {
                this.repsToSenior += actualGain;
                this.repChanged = true;
            }
            if (includeSuperSenior) {
                senior = senior.getSenior();
                if (senior != null) {
                    senior.gainReputation(actualGain, true, this);
                }
            }
        }
    }

    public int getTotalReputation() {
        return totalReputation;
    }

    public void setTotalReputation(int totalReputation) {
        if (totalReputation != this.totalReputation) {
            this.repChanged = true;
        }
        this.totalReputation = totalReputation;
    }

    public FamilyEntry getSenior() {
        return senior;
    }

    public synchronized boolean setSenior(FamilyEntry senior, boolean save) {
        if (this.senior == senior) {
            return false;
        }
        FamilyEntry oldSenior = this.senior;
        this.senior = senior;
        if (senior != null) {
            if (senior.addJunior(this)) {
                if (save) {
                    updateDBChangeFamily(getChrId(), senior.getFamily().getID(), senior.getChrId());
                }
                if (this.repsToSenior != 0) {
                    this.repChanged = true;
                }
                this.repsToSenior = 0;
                this.addSeniorCount(1, null);
                this.setTotalSeniors(senior.getTotalSeniors() + 1);
                return true;
            }
        } else {
            if (oldSenior != null) {
                oldSenior.removeJunior(this);
            }
        }
        return false;
    }

    private static boolean updateDBChangeFamily(int cid, int familyid, int seniorid) {
        try (Connection con = DatabaseConnection.getConnection()) {
            return updateDBChangeFamily(con, cid, familyid, seniorid);
        } catch (SQLException e) {
            log.error("Could not get connection to DB while changing family", e);
            return false;
        }
    }

    private static boolean updateDBChangeFamily(Connection con, int cid, int familyid, int seniorid) {
        try (PreparedStatement ps = con.prepareStatement("UPDATE family_character SET familyid = ?, seniorid = ?, reptosenior = 0 WHERE cid = ?")) {
            ps.setInt(1, familyid);
            ps.setInt(2, seniorid);
            ps.setInt(3, cid);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update seniorId in 'family_character' for chrId {}", cid, e);
            return false;
        }
        return updateCharacterFamilyDB(con, cid, familyid, false);
    }

    private static boolean updateCharacterFamilyDB(Connection con, int charid, int familyid, boolean fork) {
        try (PreparedStatement ps = con.prepareStatement("UPDATE characters SET familyid = ? WHERE id = ?")) {
            ps.setInt(1, familyid);
            ps.setInt(2, charid);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not update familyId in 'characters' for chrId {} when changing family. {}", charid, fork ? "(fork)" : "", e);
            return false;
        }
        return true;
    }

    public List<FamilyEntry> getJuniors() {
        return Collections.unmodifiableList(Arrays.asList(juniors));
    }

    public FamilyEntry getOtherJunior(FamilyEntry junior) {
        if (juniors[0] == junior) {
            return juniors[1];
        } else if (juniors[1] == junior) {
            return juniors[0];
        }
        return null;
    }

    public int getJuniorCount() { //close enough to be relatively consistent to multiple threads (and the result is not vital)
        int juniorCount = 0;
        if (juniors[0] != null) {
            juniorCount++;
        }
        if (juniors[1] != null) {
            juniorCount++;
        }
        return juniorCount;
    }

    public synchronized boolean addJunior(FamilyEntry newJunior) {
        for (int i = 0; i < juniors.length; i++) {
            if (juniors[i] == null) { // successfully add new junior to family
                juniors[i] = newJunior;
                addJuniorCount(1);
                getFamily().addEntry(newJunior);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isJunior(FamilyEntry entry) { //require locking since result accuracy is vital
        if (juniors[0] == entry) {
            return true;
        } else {
            return juniors[1] == entry;
        }
    }

    public synchronized boolean removeJunior(FamilyEntry junior) {
        for (int i = 0; i < juniors.length; i++) {
            if (juniors[i] == junior) {
                juniors[i] = null;
                return true;
            }
        }
        return false;
    }

    public int getTotalSeniors() {
        return totalSeniors;
    }

    public void setTotalSeniors(int totalSeniors) {
        this.totalSeniors = totalSeniors;
    }

    public int getTotalJuniors() {
        return totalJuniors;
    }

    public void setTotalJuniors(int totalJuniors) {
        this.totalJuniors = totalJuniors;
    }

    public void announceToSenior(Packet packet, boolean includeSuperSenior) {
        FamilyEntry senior = getSenior();
        if (senior != null) {
            Character seniorChr = senior.getChr();
            if (seniorChr != null) {
                seniorChr.sendPacket(packet);
            }
            senior = senior.getSenior();
            if (includeSuperSenior && senior != null) {
                seniorChr = senior.getChr();
                if (seniorChr != null) {
                    seniorChr.sendPacket(packet);
                }
            }
        }
    }

    public void updateSeniorFamilyInfo(boolean includeSuperSenior) {
        FamilyEntry senior = getSenior();
        if (senior != null) {
            Character seniorChr = senior.getChr();
            if (seniorChr != null) {
                seniorChr.sendPacket(PacketCreator.getFamilyInfo(senior));
            }
            senior = senior.getSenior();
            if (includeSuperSenior && senior != null) {
                seniorChr = senior.getChr();
                if (seniorChr != null) {
                    seniorChr.sendPacket(PacketCreator.getFamilyInfo(senior));
                }
            }
        }
    }

    /**
     * Traverses entire family tree to update senior/junior counts. Call on leader.
     */
    public synchronized void doFullCount() {
        Pair<Integer, Integer> counts = this.traverseAndUpdateCounts(0);
        getFamily().setTotalGenerations(counts.getLeft() + 1);
    }

    private Pair<Integer, Integer> traverseAndUpdateCounts(int seniors) { // recursion probably limits family size, but it should handle a depth of a few thousand
        setTotalSeniors(seniors);
        this.generation = seniors;
        int juniorCount = 0;
        int highestGeneration = this.generation;
        for (FamilyEntry entry : juniors) {
            if (entry != null) {
                Pair<Integer, Integer> counts = entry.traverseAndUpdateCounts(seniors + 1);
                juniorCount += counts.getRight(); //total juniors
                if (counts.getLeft() > highestGeneration) {
                    highestGeneration = counts.getLeft();
                }
            }
        }
        setTotalJuniors(juniorCount);
        return new Pair<>(highestGeneration, juniorCount); //creating new objects to return is a bit inefficient, but cleaner than packing into a long
    }

    public boolean useEntitlement(FamilyEntitlement entitlement) {
        int id = entitlement.ordinal();
        if (entitlements[id] >= 1) {
            return false;
        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO family_entitlement (entitlementid, charid, timestamp) VALUES (?, ?, ?)")) {
            ps.setInt(1, id);
            ps.setInt(2, getChrId());
            ps.setLong(3, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not insert new row in 'family_entitlement' for chr {}", getName(), e);
        }
        entitlements[id]++;
        return true;
    }

    public boolean refundEntitlement(FamilyEntitlement entitlement) {
        int id = entitlement.ordinal();
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM family_entitlement WHERE entitlementid = ? AND charid = ?")) {
            ps.setInt(1, id);
            ps.setInt(2, getChrId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Could not refund family entitlement \"{}\" for chr {}", entitlement.getName(), getName(), e);
        }
        entitlements[id] = 0;
        return true;
    }

    public boolean isEntitlementUsed(FamilyEntitlement entitlement) {
        return entitlements[entitlement.ordinal()] >= 1;
    }

    public int getEntitlementUsageCount(FamilyEntitlement entitlement) {
        return entitlements[entitlement.ordinal()];
    }

    public void setEntitlementUsed(int id) {
        entitlements[id]++;
    }

    public void resetEntitlementUsages() {
        for (FamilyEntitlement entitlement : FamilyEntitlement.values()) {
            entitlements[entitlement.ordinal()] = 0;
        }
    }

    public boolean saveReputation() {
        if (!repChanged) {
            return true;
        }
        try (Connection con = DatabaseConnection.getConnection()) {
            return saveReputation(con);
        } catch (SQLException e) {
            log.error("Could not get connection to DB while saving reputation", e);
            return false;
        }
    }

    public boolean saveReputation(Connection con) {
        if (!repChanged) {
            return true;
        }
        try (PreparedStatement ps = con.prepareStatement("UPDATE family_character SET reputation = ?, todaysrep = ?, totalreputation = ?, reptosenior = ? WHERE cid = ?")) {
            ps.setInt(1, getReputation());
            ps.setInt(2, getTodaysRep());
            ps.setInt(3, getTotalReputation());
            ps.setInt(4, getRepsToSenior());
            ps.setInt(5, getChrId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to autosave rep to 'family_character' for chrId {}", getChrId(), e);
            return false;
        }
        return true;
    }

    public void savedSuccessfully() {
        this.repChanged = false;
    }
}
