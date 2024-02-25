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
package server.partyquest;

import client.Character;
import constants.game.GameConstants;
import constants.id.ItemId;
import constants.id.MapId;
import server.TimerManager;
import server.expeditions.Expedition;
import server.expeditions.ExpeditionType;
import server.maps.MapleMap;
import tools.PacketCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Ronan
 */
public class AriantColiseum {

    private Expedition exped;
    private MapleMap map;

    private final Map<Character, Integer> score;
    private final Map<Character, Integer> rewardTier;
    private boolean scoreDirty = false;

    private ScheduledFuture<?> ariantUpdate;
    private ScheduledFuture<?> ariantFinish;
    private ScheduledFuture<?> ariantScoreboard;

    private int lostShards = 0;

    private boolean eventClear = false;

    public AriantColiseum(MapleMap eventMap, Expedition expedition) {
        exped = expedition;
        exped.finishRegistration();

        map = eventMap;
        map.resetFully();

        long pqTimer = MINUTES.toMillis(10);
        long pqTimerBoard = MINUTES.toMillis(9) + SECONDS.toMillis(50);

        List<Character> players = exped.getActiveMembers();
        score = new HashMap<>();
        rewardTier = new HashMap<>();
        for (Character mc : players) {
            mc.changeMap(map, 0);
            mc.setAriantColiseum(this);
            mc.updateAriantScore();
            rewardTier.put(mc, 0);
        }

        for (Character mc : players) {
            mc.sendPacket(PacketCreator.updateAriantPQRanking(score));
        }

        setAriantScoreBoard(TimerManager.getInstance().schedule(() -> showArenaResults(), pqTimerBoard));

        setArenaFinish(TimerManager.getInstance().schedule(() -> enterKingsRoom(), pqTimer));

        setArenaUpdate(TimerManager.getInstance().register(() -> broadcastAriantScoreUpdate(), 500, 500));
    }

    private void setArenaUpdate(ScheduledFuture<?> ariantUpdate) {
        this.ariantUpdate = ariantUpdate;
    }

    private void setArenaFinish(ScheduledFuture<?> arenaFinish) {
        this.ariantFinish = arenaFinish;
    }

    private void setAriantScoreBoard(ScheduledFuture<?> ariantScore) {
        this.ariantScoreboard = ariantScore;
    }

    private void cancelArenaUpdate() {
        if (ariantUpdate != null) {
            ariantUpdate.cancel(true);
            ariantUpdate = null;
        }
    }

    private void cancelArenaFinish() {
        if (ariantFinish != null) {
            ariantFinish.cancel(true);
            ariantFinish = null;
        }
    }

    private void cancelAriantScoreBoard() {
        if (ariantScoreboard != null) {
            ariantScoreboard.cancel(true);
            ariantScoreboard = null;
        }
    }

    private void cancelAriantSchedules() {
        cancelArenaUpdate();
        cancelArenaFinish();
        cancelAriantScoreBoard();
    }

    public int getAriantScore(Character chr) {
        Integer chrScore = score.get(chr);
        return chrScore != null ? chrScore : 0;
    }

    public void clearAriantScore(Character chr) {
        score.remove(chr);
    }

    public void updateAriantScore(Character chr, int points) {
        if (map != null) {
            score.put(chr, points);
            scoreDirty = true;
        }
    }

    private void broadcastAriantScoreUpdate() {
        if (scoreDirty) {
            for (Character chr : score.keySet()) {
                chr.sendPacket(PacketCreator.updateAriantPQRanking(score));
            }
            scoreDirty = false;
        }
    }

    public int getAriantRewardTier(Character chr) {
        Integer reward = rewardTier.get(chr);
        return reward != null ? reward : 0;
    }

    public void clearAriantRewardTier(Character chr) {
        rewardTier.remove(chr);
    }

    public void addLostShards(int quantity) {
        lostShards += quantity;
    }

    public void leaveArena(Character chr) {
        if (!(eventClear && GameConstants.isAriantColiseumArena(chr.getMapId()))) {
            leaveArenaInternal(chr);
        }
    }

    private synchronized void leaveArenaInternal(Character chr) {
        if (exped != null) {
            if (exped.removeMember(chr)) {
                int minSize = eventClear ? 1 : 2;
                if (exped.getActiveMembers().size() < minSize) {
                    dispose();
                }
                chr.setAriantColiseum(null);

                int shards = chr.countItem(ItemId.ARPQ_SPIRIT_JEWEL);
                chr.getAbstractPlayerInteraction().removeAll(ItemId.ARPQ_SPIRIT_JEWEL);
                chr.updateAriantScore(shards);
            }
        }
    }

    public void playerDisconnected(Character chr) {
        leaveArenaInternal(chr);
    }

    private void showArenaResults() {
        eventClear = true;

        if (map != null) {
            map.broadcastMessage(PacketCreator.showAriantScoreBoard());
            map.killAllMonsters();

            distributeAriantPoints();
        }
    }

    private static boolean isUnfairMatch(Integer winnerScore, Integer secondScore, Integer lostShardsScore, List<Integer> runnerupsScore) {
        if (winnerScore <= 0) {
            return false;
        }

        double runnerupsScoreCount = 0;
        for (Integer i : runnerupsScore) {
            runnerupsScoreCount += i;
        }

        runnerupsScoreCount += lostShardsScore;
        secondScore += lostShardsScore;

        double matchRes = runnerupsScoreCount / winnerScore;
        double runnerupRes = ((double) secondScore) / winnerScore;

        return matchRes < 0.81770726891980117713114871015349 && (runnerupsScoreCount < 7 || runnerupRes < 0.5929);
    }

    public void distributeAriantPoints() {
        int firstTop = -1, secondTop = -1;
        Character winner = null;
        List<Integer> runnerups = new ArrayList<>();

        for (Entry<Character, Integer> e : score.entrySet()) {
            Integer s = e.getValue();
            if (s > firstTop) {
                secondTop = firstTop;
                firstTop = s;
                winner = e.getKey();
            } else if (s > secondTop) {
                secondTop = s;
            }

            runnerups.add(s);
            rewardTier.put(e.getKey(), (int) Math.floor(s / 10));
        }

        runnerups.remove(firstTop);
        if (isUnfairMatch(firstTop, secondTop, map.getDroppedItemsCountById(ItemId.ARPQ_SPIRIT_JEWEL) + lostShards, runnerups)) {
            rewardTier.put(winner, 1);
        }
    }

    private ExpeditionType getExpeditionType() {
        switch (map.getId()) {
        case MapId.ARPQ_ARENA_1:
            return ExpeditionType.ARIANT;
        case MapId.ARPQ_ARENA_2:
            return ExpeditionType.ARIANT1;
        default:
            return ExpeditionType.ARIANT2;
        }
    }

    private void enterKingsRoom() {
        exped.removeChannelExpedition(map.getChannelServer());
        cancelAriantSchedules();

        for (Character chr : map.getAllPlayers()) {
            chr.changeMap(MapId.ARPQ_KINGS_ROOM, 0);
        }
    }

    private synchronized void dispose() {
        if (exped != null) {
            exped.dispose(false);

            for (Character chr : exped.getActiveMembers()) {
                chr.setAriantColiseum(null);
                chr.changeMap(MapId.ARPQ_LOBBY, 0);
            }

            map.getWorldServer().registerTimedMapObject(() -> {
                score.clear();
                exped = null;
                map = null;
            }, MINUTES.toMillis(5));
        }
    }
}
