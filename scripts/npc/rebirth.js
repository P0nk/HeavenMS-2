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
/* Rebirth NPC
    @author Ronan
    @author wejrox
*/
var status;
var jobId = 0;

function start() {
    status = -1;
    const YamlConfig = Java.type('config.YamlConfig');
    if (!YamlConfig.config.server.USE_REBIRTH_SYSTEM) {
        cm.sendOk("Rebirths aren't enabled on this server, how did you get here?");
        cm.dispose();
        return;
    }
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status === 0) {
        cm.sendNext("Come to me when you want to be reborn again. You currently have a total of #r" + cm.getChar().getReborns() + " #krebirths.");
    } else if (status === 1) {
        cm.sendSimple("What do you want me to do today: \r\n \r\n #L0##bI want to be reborn!#l \r\n #L1##bNothing for now...#k#l");
    } else if (status === 2) {
        if (selection === 0) {
            if (cm.getChar().getLevel() === cm.getChar().getMaxClassLevel()) {
                cm.sendSimple("I see... and which path would you like to take? \r\n\r\n #L0##bExplorer (Beginner)#l \r\n #L1##bCygnus Knight (Noblesse)#l \r\n #L2##bAran (Legend)#l");
            } else {
                cm.sendOk("It looks like your journey has not yet ended... come back when you're level " + cm.getChar().getMaxClassLevel());
                cm.dispose();
            }
        } else if (selection === 1) {
            cm.sendOk("See you soon!")
            cm.dispose();
        }
    } else if (status === 3) {
        // 0 => beginner, 1000 => noblesse, 2000 => legend
        // makes this very easy :-)
        jobId = selection * 1000;

        var job = "";
        if (selection === 0) job = "Beginner";
        else if (selection === 1) job = "Noblesse";
        else if (selection === 2) job = "Legend";
        cm.sendYesNo("Are you sure you want to be reborn as a " + job + "?");
    }
    else if (status === 4 && type === 1) {
        cm.getChar().executeRebornAsId(jobId);
        cm.sendOk("You have now been reborn. That's a total of #r" + cm.getChar().getReborns() + "#k rebirths");
        cm.dispose();
    }
}