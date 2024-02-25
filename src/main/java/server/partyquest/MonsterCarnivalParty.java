package server.partyquest;

import client.Character;
import server.maps.MapleMap;
import tools.PacketCreator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Rob
 */
public class MonsterCarnivalParty {

    private List<Character> members = new LinkedList<>();
    private final Character leader;
    private final byte team;
    private short availableCP = 0, totalCP = 0;
    private int summons = 8;
    private boolean winner = false;

    public MonsterCarnivalParty(final Character owner, final List<Character> members1, final byte team1) {
        leader = owner;
        members = members1;
        team = team1;

        for (final Character chr : members) {
            chr.setMonsterCarnivalParty(this);
            chr.setTeam(team);
        }
    }

    public final Character getLeader() {
        return leader;
    }

    public void addCP(Character player, int ammount) {
        totalCP += ammount;
        availableCP += ammount;
        player.addCP(ammount);
    }

    public int getTotalCP() {
        return totalCP;
    }

    public int getAvailableCP() {
        return availableCP;
    }

    public void useCP(Character player, int ammount) {
        availableCP -= ammount;
        player.useCP(ammount);
    }

    public List<Character> getMembers() {
        return members;
    }

    public int getTeam() {
        return team;
    }

    public void warpOut(final int map) {
        for (Character chr : members) {
            chr.changeMap(map, 0);
            chr.setMonsterCarnivalParty(null);
            chr.setMonsterCarnival(null);
        }
        members.clear();
    }

    public void warp(final MapleMap map, final int portalid) {
        for (Character chr : members) {
            chr.changeMap(map, map.getPortal(portalid));
        }
    }

    public void warpOut() {
        if (winner == true) {
            warpOut(980000003 + (leader.getMonsterCarnival().getRoom() * 100));
        } else {
            warpOut(980000004 + (leader.getMonsterCarnival().getRoom() * 100));
        }
    }

    public boolean allInMap(MapleMap map) {
        boolean status = true;
        for (Character chr : members) {
            if (chr.getMap() != map) {
                status = false;
            }
        }
        return status;
    }

    public void removeMember(Character chr) {
        members.remove(chr);
        chr.changeMap(980000010);
        chr.setMonsterCarnivalParty(null);
        chr.setMonsterCarnival(null);
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean status) {
        winner = status;
    }

    public void displayMatchResult() {
        final String effect = winner ? "quest/carnival/win" : "quest/carnival/lose";

        for (final Character chr : members) {
            chr.sendPacket(PacketCreator.showEffect(effect));
        }
    }

    public void summon() {
        this.summons--;
    }

    public boolean canSummon() {
        return this.summons > 0;
    }
}
