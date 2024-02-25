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
package client.inventory.manipulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author RonanLana
 */
public class CashIdGenerator {
    private static final Logger log = LoggerFactory.getLogger(CashIdGenerator.class);
    private final static Set<Integer> existentCashids = new HashSet<>(10000);
    private static Integer runningCashid = 0;

    private static void loadExistentCashIdsFromQuery(Connection con, String query) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                if (!rs.wasNull()) {
                    existentCashids.add(id);
                }
            }
        }
    }

    public static synchronized void loadExistentCashIdsFromDb(Connection con) throws SQLException {
        loadExistentCashIdsFromQuery(con, "SELECT id FROM rings");
        loadExistentCashIdsFromQuery(con, "SELECT petid FROM pets");

        runningCashid = 0;
        do {
            runningCashid++;    // hopefully the id will never surpass the allotted amount for pets/rings?
        } while (existentCashids.contains(runningCashid));
    }

    private static void getNextAvailableCashId() {
        runningCashid++;
        if (runningCashid >= 777000000) {
            existentCashids.clear();
            try (Connection con = DatabaseConnection.getConnection()) {
                loadExistentCashIdsFromDb(con);
            } catch (SQLException e) {
                log.warn("Failed to reset overflowing cash ids", e);
            }
        }
    }

    public static synchronized int generateCashId() {
        while (true) {
            if (!existentCashids.contains(runningCashid)) {
                int ret = runningCashid;
                getNextAvailableCashId();

                // existentCashids.add(ret)... no need to do this since the wrap over already refetches already used cashids from the DB
                return ret;
            }

            getNextAvailableCashId();
        }
    }

    public static synchronized void freeCashId(int cashId) {
        existentCashids.remove(cashId);
    }

}
