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
package net.server.coordinator.login;

import config.YamlConfig;
import net.server.Server;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Ronan
 */
public class LoginStorage {
    private final ConcurrentHashMap<Integer, List<Instant>> loginHistory = new ConcurrentHashMap<>(); // Key: accountId

    public boolean registerLogin(int accountId) {
        List<Instant> attempts = loginHistory.computeIfAbsent(accountId, k -> new ArrayList<>());

        synchronized (attempts) {
            final Instant attemptExpiry = Instant.ofEpochMilli(Server.getInstance().getCurrentTime() + YamlConfig.config.server.LOGIN_ATTEMPT_DURATION);

            if (attempts.size() > YamlConfig.config.server.MAX_ACCOUNT_LOGIN_ATTEMPT) {
                Collections.fill(attempts, attemptExpiry);
                return false;
            }

            attempts.add(attemptExpiry);
            return true;
        }
    }

    public void clearExpiredAttempts() {
        final Instant now = Instant.ofEpochMilli(Server.getInstance().getCurrentTime());
        List<Integer> accountIdsToClear = new ArrayList<>();

        for (Entry<Integer, List<Instant>> loginEntries : loginHistory.entrySet()) {
            final List<Instant> attempts = loginEntries.getValue();
            synchronized (attempts) {
                List<Instant> attemptsToRemove = attempts.stream()
                        .filter(attempt -> attempt.isBefore(now))
                        .collect(Collectors.toList());

                for (Instant attemptToRemove : attemptsToRemove) {
                    attempts.remove(attemptToRemove);
                }

                if (attempts.isEmpty()) {
                    accountIdsToClear.add(loginEntries.getKey());
                }
            }
        }

        for (Integer accountId : accountIdsToClear) {
            loginHistory.remove(accountId);
        }
    }
}
