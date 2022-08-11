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
package net.server.services.task.channel;

import config.YamlConfig;
import net.server.services.BaseScheduler;
import net.server.services.BaseService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ronan
 */
public class MobAnimationService extends BaseService {

    private final MobAnimationScheduler[] mobAnimationSchedulers = new MobAnimationScheduler[YamlConfig.config.server.CHANNEL_LOCKS];

    public MobAnimationService() {
        for (int i = 0; i < YamlConfig.config.server.CHANNEL_LOCKS; i++) {
            mobAnimationSchedulers[i] = new MobAnimationScheduler();
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < YamlConfig.config.server.CHANNEL_LOCKS; i++) {
            if (mobAnimationSchedulers[i] != null) {
                mobAnimationSchedulers[i].dispose();
                mobAnimationSchedulers[i] = null;
            }
        }
    }

    public boolean registerMobOnAnimationEffect(int mapid, int mobHash, long delay) {
        return mobAnimationSchedulers[getChannelSchedulerIndex(mapid)].registerAnimationMode(mobHash, delay);
    }

    // do nothing
    private static final Runnable r = () -> {
    };

    private class MobAnimationScheduler extends BaseScheduler {
        Set<Integer> onAnimationMobs = new HashSet<>(1000);
        private final Lock animationLock = new ReentrantLock(true);

        public MobAnimationScheduler() {
            super.addListener((toRemove, update) -> {
                animationLock.lock();
                try {
                    for (Object hashObj : toRemove) {
                        Integer mobHash = (Integer) hashObj;
                        onAnimationMobs.remove(mobHash);
                    }
                } finally {
                    animationLock.unlock();
                }
            });
        }

        public boolean registerAnimationMode(Integer mobHash, long animationTime) {
            animationLock.lock();
            try {
                if (onAnimationMobs.contains(mobHash)) {
                    return false;
                }

                registerEntry(mobHash, r, animationTime);
                onAnimationMobs.add(mobHash);
                return true;
            } finally {
                animationLock.unlock();
            }
        }

        @Override
        public void dispose() {
            super.dispose();
        }

    }

}
