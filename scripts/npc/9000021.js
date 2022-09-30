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
/* 9000021 - Gaga
    BossRushPQ recruiter
    @author Ronan
 */

var status;
var work;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var code;
    if (mode < 0) {
        cm.dispose();
    } else {
        if (mode == 0 && type > 0) {
            cm.dispose();
            return;
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }
          
        if (status == 0) {
            cm.sendGetText("Hey, traveler! Please enter your NX code to redeem your NX!");	    
        } else if (status == 1) {
            code = cm.getText();
		work = cm.gainNX(code);
		if (work) {
	        cm.sendOk("Success! the nx was added to your account!");
	    }
	    else{
		cm.sendOk("Something went wrong, seek support on discord");	
	    }
        } else {
	cm.dispose();
	}
    }
}
