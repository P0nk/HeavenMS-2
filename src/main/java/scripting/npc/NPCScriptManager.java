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
package scripting.npc;

import client.Character;
import client.Client;
import net.server.world.PartyCharacter;
import scripting.AbstractScriptManager;
import server.ItemInformationProvider.ScriptedItem;
import tools.FilePrinter;
import tools.PacketCreator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matze
 */
public class NPCScriptManager extends AbstractScriptManager {
    private static final NPCScriptManager instance = new NPCScriptManager();

    private final Map<Client, NPCConversationManager> cms = new HashMap<>();
    private final Map<Client, Invocable> scripts = new HashMap<>();

    public static NPCScriptManager getInstance() {
        return instance;
    }

    public boolean isNpcScriptAvailable(Client c, String fileName) {
        ScriptEngine engine = null;
        if (fileName != null) {
            engine = getInvocableScriptEngine("npc/" + c.getPlayer().getLanguage() +  "/" + fileName + ".js", c);
        }

        return engine != null;
    }

    public boolean start(Client c, int npc, Character chr) {
        return start(c, npc, -1, chr);
    }

    public boolean start(Client c, int npc, int oid, Character chr) {
        return start(c, npc, oid, null, chr);
    }

    public boolean start(Client c, int npc, String fileName, Character chr) {
        return start(c, npc, -1, fileName, chr);
    }

    public boolean start(Client c, int npc, int oid, String fileName, Character chr) {
        return start(c, npc, oid, fileName, chr, false, "cm");
    }

    public boolean start(Client c, ScriptedItem scriptItem, Character chr) {
        return start(c, scriptItem.getNpc(), -1, scriptItem.getScript(), chr, true, "im");
    }

    public void start(String filename, Client c, int npc, List<PartyCharacter> chrs) {
        try {
            final NPCConversationManager cm = new NPCConversationManager(c, npc, chrs, true);
            cm.dispose();
            if (cms.containsKey(c)) {
                return;
            }
            cms.put(c, cm);
            ScriptEngine engine = getInvocableScriptEngine("npc/" + c.getPlayer().getLanguage() +  "/" + filename + ".js", c);

            if (engine == null) {
                c.getPlayer().dropMessage(1, "NPC " + npc + " is uncoded.");
                cm.dispose();
                return;
            }
            engine.put("cm", cm);

            Invocable invocable = (Invocable) engine;
            scripts.put(c, invocable);
            try {
                invocable.invokeFunction("start", chrs);
            } catch (final NoSuchMethodException nsme) {
                nsme.printStackTrace();
            }

        } catch (final Exception e) {
            FilePrinter.printError(FilePrinter.NPC + npc + ".txt", e);
            dispose(c);
        }
    }

    private boolean start(Client c, int npc, int oid, String fileName, Character chr, boolean itemScript, String engineName) {
        try {
            final NPCConversationManager cm = new NPCConversationManager(c, npc, oid, fileName, itemScript);
            if (cms.containsKey(c)) {
                dispose(c);
            }
            if (c.canClickNPC()) {
                cms.put(c, cm);
                ScriptEngine engine = null;
                if (!itemScript) {
                    if (fileName != null) {
                        engine = getInvocableScriptEngine("npc/" + c.getPlayer().getLanguage() +  "/" + fileName + ".js", c);
                    }
                } else {
                    if (fileName != null) {     // thanks MiLin for drafting NPC-based item scripts
                        engine = getInvocableScriptEngine("item/" + c.getPlayer().getLanguage() +  "/" + fileName + ".js", c);
                    }
                }
                if (engine == null) {
                    engine = getInvocableScriptEngine("npc/" + c.getPlayer().getLanguage() +  "/" + npc + ".js", c);
                    cm.resetItemScript();
                }
                if (engine == null) {
                    dispose(c);
                    return false;
                }
                engine.put(engineName, cm);

                Invocable iv = (Invocable) engine;
                scripts.put(c, iv);
                c.setClickedNPC();
                try {
                    iv.invokeFunction("start");
                } catch (final NoSuchMethodException nsme) {
                    try {
                        iv.invokeFunction("start", chr);
                    } catch (final NoSuchMethodException nsma) {
                        nsma.printStackTrace();
                    }
                }
            } else {
                c.sendPacket(PacketCreator.enableActions());
            }
            return true;
        } catch (final Exception ute) {
            FilePrinter.printError(FilePrinter.NPC + npc + ".txt", ute);
            dispose(c);

            return false;
        }
    }

    public void action(Client c, byte mode, byte type, int selection) {
        Invocable iv = scripts.get(c);
        if (iv != null) {
            try {
                c.setClickedNPC();
                iv.invokeFunction("action", mode, type, selection);
            } catch (ScriptException | NoSuchMethodException t) {
                if (getCM(c) != null) {
                    FilePrinter.printError(FilePrinter.NPC + getCM(c).getNpc() + ".txt", t);
                }
                dispose(c);
            }
        }
    }

    public void dispose(NPCConversationManager cm) {
        Client c = cm.getClient();
        c.getPlayer().setCS(false);
        c.getPlayer().setNpcCooldown(System.currentTimeMillis());
        cms.remove(c);
        scripts.remove(c);

        String scriptFolder = (cm.isItemScript() ? "item" : "npc");
        if (cm.getScriptName() != null) {
            resetContext(scriptFolder + "/" + c.getPlayer().getLanguage() +  "/" + cm.getScriptName() + ".js", c);
        } else {
            resetContext(scriptFolder + "/" + c.getPlayer().getLanguage() +  "/" + cm.getNpc() + ".js", c);
        }

        c.getPlayer().flushDelayedUpdateQuests();
    }

    public void dispose(Client c) {
        NPCConversationManager cm = cms.get(c);
        if (cm != null) {
            dispose(cm);
        }
    }

    public NPCConversationManager getCM(Client c) {
        return cms.get(c);
    }

}
