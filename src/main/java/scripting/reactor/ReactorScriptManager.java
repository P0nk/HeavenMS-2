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
package scripting.reactor;

import client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scripting.AbstractScriptManager;
import server.maps.Reactor;
import server.maps.ReactorDropEntry;
import tools.DatabaseConnection;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Lerk
 */
public class ReactorScriptManager extends AbstractScriptManager {
    private static final Logger log = LoggerFactory.getLogger(ReactorScriptManager.class);
    private static final ReactorScriptManager instance = new ReactorScriptManager();

    private final Map<Integer, List<ReactorDropEntry>> drops = new HashMap<>();

    public static ReactorScriptManager getInstance() {
        return instance;
    }

    public void onHit(Client c, Reactor reactor) {
        try {
            Invocable iv = initializeInvocable(c, reactor);
            if (iv == null) {
                return;
            }

            iv.invokeFunction("hit");
        } catch (final NoSuchMethodException e) {
            //do nothing, hit is OPTIONAL
        } catch (final ScriptException | NullPointerException e) {
            log.error("Error during onHit script for reactor: {}", reactor.getId(), e);
        }
    }

    public void act(Client c, Reactor reactor) {
        try {
            Invocable iv = initializeInvocable(c, reactor);
            if (iv == null) {
                return;
            }

            iv.invokeFunction("act");
        } catch (final ScriptException | NoSuchMethodException | NullPointerException e) {
            log.error("Error during act script for reactor: {}", reactor.getId(), e);
        }
    }

    public List<ReactorDropEntry> getDrops(int reactorId) {
        List<ReactorDropEntry> ret = drops.get(reactorId);
        if (ret == null) {
            ret = new LinkedList<>();
            try (Connection con = DatabaseConnection.getConnection()) {
                try (PreparedStatement ps = con.prepareStatement("SELECT itemid, chance, questid FROM reactordrops WHERE reactorid = ? AND chance >= 0")) {
                    ps.setInt(1, reactorId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            ret.add(new ReactorDropEntry(rs.getInt("itemid"), rs.getInt("chance"), rs.getInt("questid")));
                        }
                    }
                }
            } catch (Throwable e) {
                log.error("Error getting drops for reactor: {}", reactorId);
            }
            drops.put(reactorId, ret);
        }
        return ret;
    }

    public void clearDrops() {
        drops.clear();
    }

    public void touch(Client c, Reactor reactor) {
        touching(c, reactor, true);
    }

    public void untouch(Client c, Reactor reactor) {
        touching(c, reactor, false);
    }

    private void touching(Client c, Reactor reactor, boolean touching) {
        final String functionName = touching ? "touch" : "untouch";
        try {
            Invocable iv = initializeInvocable(c, reactor);
            if (iv == null) {
                return;
            }

            iv.invokeFunction(functionName);
        } catch (final ScriptException | NoSuchMethodException | NullPointerException e) {
            log.error("Error during {} script for reactor: {}", functionName, reactor.getId(), e);
        }
    }

    private Invocable initializeInvocable(Client c, Reactor reactor) {
        ScriptEngine engine = getInvocableScriptEngine("reactor/" + reactor.getId() + ".js", c);
        if (engine == null) {
            return null;
        }

        Invocable iv = (Invocable) engine;
        ReactorActionManager rm = new ReactorActionManager(c, reactor, iv);
        engine.put("rm", rm);

        return iv;
    }
}