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
package server.maps;

import client.Client;
import config.YamlConfig;
import net.packet.Packet;
import net.server.services.task.channel.OverallService;
import net.server.services.type.ChannelServices;
import scripting.reactor.ReactorScriptManager;
import server.TimerManager;
import server.partyquest.GuardianSpawnPoint;
import tools.PacketCreator;
import tools.Pair;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lerk
 * @author Ronan
 */
public class Reactor extends AbstractMapObject {
    private final int rid;
    private final ReactorStats stats;
    private byte state;
    private byte evstate;
    private int delay;
    private MapleMap map;
    private String name;
    private boolean alive;
    private boolean shouldCollect;
    private boolean attackHit;
    private ScheduledFuture<?> timeoutTask = null;
    private Runnable delayedRespawnRun = null;
    private GuardianSpawnPoint guardian = null;
    private byte facingDirection = 0;
    private final Lock reactorLock = new ReentrantLock(true);
    private final Lock hitLock = new ReentrantLock(true);

    public Reactor(ReactorStats stats, int rid) {
        this.evstate = (byte) 0;
        this.stats = stats;
        this.rid = rid;
        this.alive = true;
    }

    public void setShouldCollect(boolean collect) {
        this.shouldCollect = collect;
    }

    public boolean getShouldCollect() {
        return shouldCollect;
    }

    public void lockReactor() {
        reactorLock.lock();
    }

    public void unlockReactor() {
        reactorLock.unlock();
    }

    public void hitLockReactor() {
        hitLock.lock();
        reactorLock.lock();
    }

    public void hitUnlockReactor() {
        reactorLock.unlock();
        hitLock.unlock();
    }

    public void setState(byte state) {
        this.state = state;
    }

    public byte getState() {
        return state;
    }

    public void setEventState(byte substate) {
        this.evstate = substate;
    }

    public byte getEventState() {
        return evstate;
    }

    public ReactorStats getStats() {
        return stats;
    }

    public int getId() {
        return rid;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public MapObjectType getType() {
        return MapObjectType.REACTOR;
    }

    public int getReactorType() {
        return stats.getType(state);
    }

    public boolean isRecentHitFromAttack() {
        return attackHit;
    }

    public void setMap(MapleMap map) {
        this.map = map;
    }

    public MapleMap getMap() {
        return map;
    }

    public Pair<Integer, Integer> getReactItem(byte index) {
        return stats.getReactItem(state, index);
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isActive() {
        return alive && stats.getType(state) != -1;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void sendDestroyData(Client client) {
        client.sendPacket(makeDestroyData());
    }

    public final Packet makeDestroyData() {
        return PacketCreator.destroyReactor(this);
    }

    @Override
    public void sendSpawnData(Client client) {
        if (this.isAlive()) {
            client.sendPacket(makeSpawnData());
        }
    }

    public final Packet makeSpawnData() {
        return PacketCreator.spawnReactor(this);
    }

    public void resetReactorActions(int newState) {
        setState((byte) newState);
        cancelReactorTimeout();
        setShouldCollect(true);
        refreshReactorTimeout();

        if (map != null) {
            map.searchItemReactors(this);
        }
    }

    public void forceHitReactor(final byte newState) {
        this.lockReactor();
        try {
            this.resetReactorActions(newState);
            map.broadcastMessage(PacketCreator.triggerReactor(this, (short) 0));
        } finally {
            this.unlockReactor();
        }
    }

    private void tryForceHitReactor(final byte newState) {  // weak hit state signal, if already changed reactor state before timeout then drop this
        if (!reactorLock.tryLock()) {
            return;
        }

        try {
            this.resetReactorActions(newState);
            map.broadcastMessage(PacketCreator.triggerReactor(this, (short) 0));
        } finally {
            reactorLock.unlock();
        }
    }

    public void cancelReactorTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
            timeoutTask = null;
        }
    }

    private void refreshReactorTimeout() {
        int timeOut = stats.getTimeout(state);
        if (timeOut > -1) {
            final byte nextState = stats.getTimeoutState(state);

            timeoutTask = TimerManager.getInstance().schedule(() -> {
                timeoutTask = null;
                tryForceHitReactor(nextState);
            }, timeOut);
        }
    }

    public void delayedHitReactor(final Client c, long delay) {
        TimerManager.getInstance().schedule(() -> hitReactor(c), delay);
    }

    public void hitReactor(Client c) {
        hitReactor(false, 0, (short) 0, 0, c);
    }

    public void hitReactor(boolean wHit, int charPos, short stance, int skillid, Client c) {
        try {
            if (!this.isActive()) {
                return;
            }

            if (hitLock.tryLock()) {
                this.lockReactor();
                try {
                    cancelReactorTimeout();
                    attackHit = wHit;

                    if (YamlConfig.config.server.USE_DEBUG) {
                        c.getPlayer().dropMessage(5, "Hitted REACTOR " + this.getId() + " with POS " + charPos + " , STANCE " + stance + " , SkillID " + skillid + " , STATE " + state + " STATESIZE " + stats.getStateSize(state));
                    }
                    ReactorScriptManager.getInstance().onHit(c, this);

                    int reactorType = stats.getType(state);
                    if (reactorType < 999 && reactorType != -1) {//type 2 = only hit from right (kerning swamp plants), 00 is air left 02 is ground left
                        if (!(reactorType == 2 && (stance == 0 || stance == 2))) { //get next state
                            for (byte b = 0; b < stats.getStateSize(state); b++) {//YAY?
                                List<Integer> activeSkills = stats.getActiveSkills(state, b);
                                if (activeSkills != null) {
                                    if (!activeSkills.contains(skillid)) {
                                        continue;
                                    }
                                }

                                this.state = stats.getNextState(state, b);
                                byte nextState = stats.getNextState(state, b);
                                boolean isInEndState = nextState < this.state;
                                if (isInEndState) {//end of reactor
                                    if (reactorType < 100) {//reactor broken
                                        if (delay > 0) {
                                            map.destroyReactor(getObjectId());
                                        } else {//trigger as normal
                                            map.broadcastMessage(PacketCreator.triggerReactor(this, stance));
                                        }
                                    } else {//item-triggered on final step
                                        map.broadcastMessage(PacketCreator.triggerReactor(this, stance));
                                    }

                                    ReactorScriptManager.getInstance().act(c, this);
                                } else { //reactor not broken yet
                                    map.broadcastMessage(PacketCreator.triggerReactor(this, stance));
                                    if (state == stats.getNextState(state, b)) {//current state = next state, looping reactor
                                        ReactorScriptManager.getInstance().act(c, this);
                                    }

                                    setShouldCollect(true);     // refresh collectability on item drop-based reactors
                                    refreshReactorTimeout();
                                    if (stats.getType(state) == 100) {
                                        map.searchItemReactors(this);
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        state++;
                        map.broadcastMessage(PacketCreator.triggerReactor(this, stance));
                        if (this.getId() != 9980000 && this.getId() != 9980001) {
                            ReactorScriptManager.getInstance().act(c, this);
                        }

                        setShouldCollect(true);
                        refreshReactorTimeout();
                        if (stats.getType(state) == 100) {
                            map.searchItemReactors(this);
                        }
                    }
                } finally {
                    this.unlockReactor();
                    hitLock.unlock();   // non-encapsulated unlock found thanks to MiLin
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean destroy() {
        if (reactorLock.tryLock()) {
            try {
                boolean alive = this.isAlive();
                // reactor neither alive nor in delayed respawn, remove map object allowed
                if (alive) {
                    this.setAlive(false);
                    this.cancelReactorTimeout();

                    if (this.getDelay() > 0) {
                        this.delayedRespawn();
                    }
                } else {
                    return !this.inDelayedRespawn();
                }
            } finally {
                reactorLock.unlock();
            }
        }

        map.broadcastMessage(PacketCreator.destroyReactor(this));
        return false;
    }

    private void respawn() {
        this.lockReactor();
        try {
            this.resetReactorActions(0);
            this.setAlive(true);
        } finally {
            this.unlockReactor();
        }

        map.broadcastMessage(this.makeSpawnData());
    }

    public void delayedRespawn() {
        Runnable r = () -> {
            delayedRespawnRun = null;
            respawn();
        };

        delayedRespawnRun = r;

        OverallService service = (OverallService) map.getChannelServer().getServiceAccess(ChannelServices.OVERALL);
        service.registerOverallAction(map.getId(), r, this.getDelay());
    }

    public boolean forceDelayedRespawn() {
        Runnable r = delayedRespawnRun;

        if (r != null) {
            OverallService service = (OverallService) map.getChannelServer().getServiceAccess(ChannelServices.OVERALL);
            service.forceRunOverallAction(map.getId(), r);
            return true;
        } else {
            return false;
        }
    }

    public boolean inDelayedRespawn() {
        return delayedRespawnRun != null;
    }

    public Rectangle getArea() {
        return new Rectangle(getPosition().x + stats.getTL().x, getPosition().y + stats.getTL().y, stats.getBR().x - stats.getTL().x, stats.getBR().y - stats.getTL().y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuardianSpawnPoint getGuardian() {
        return guardian;
    }

    public void setGuardian(GuardianSpawnPoint guardian) {
        this.guardian = guardian;
    }

    public final void setFacingDirection(final byte facingDirection) {
        this.facingDirection = facingDirection;
    }

    public final byte getFacingDirection() {
        return facingDirection;
    }
}
