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
package net.server.handlers.login;

import client.MapleClient;
import client.creator.novice.BeginnerCreator;
import client.creator.novice.LegendCreator;
import client.creator.novice.NoblesseCreator;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import tools.FilePrinter;
import tools.PacketCreator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CreateCharHandler extends AbstractMaplePacketHandler {
        
        private final static Set<Integer> IDs = new HashSet<>(Arrays.asList(new Integer[]{
		1302000, 1312004, 1322005, 1442079,// weapons
		1040002, 1040006, 1040010, 1041002, 1041006, 1041010, 1041011, 1042167,// bottom
		1060002, 1060006, 1061002, 1061008, 1062115, // top
		1072001, 1072005, 1072037, 1072038, 1072383,// shoes
		30000, 30010,30020, 30030, 31000, 31040, 31050,// hair  
		20000, 20001, 20002, 21000, 21001, 21002, 21201, 20401, 20402, 21700, 20100  //face
		//#NeverTrustStevenCode
	}));

	private static boolean isLegal(Integer toCompare) {
                return IDs.contains(toCompare);
	}         	


	@Override
	public final void handlePacket(InPacket p, MapleClient c) {
                String name = p.readString();
                int job = p.readInt();
		int face = p.readInt();

		int hair = p.readInt();
		int haircolor = p.readInt();
		int skincolor = p.readInt();

		int top = p.readInt();
		int bottom = p.readInt();
		int shoes = p.readInt();
		int weapon = p.readInt();
                int gender = p.readByte();
                
                int [] items = new int [] {weapon, top, bottom, shoes, hair, face};
		for (int item : items) {
			if (!isLegal(item)) {
				FilePrinter.printError(FilePrinter.EXPLOITS + name + ".txt", "Owner from account '" + c.getAccountName() + "' tried to packet edit in char creation.");
				c.disconnect(true, false);
				return;
			}
		}
                
                int status;
                if (job == 0) { // Knights of Cygnus
			status = NoblesseCreator.createCharacter(c, name, face, hair + haircolor, skincolor, top, bottom, shoes, weapon, gender);
		} else if (job == 1) { // Adventurer
			status = BeginnerCreator.createCharacter(c, name, face, hair + haircolor, skincolor, top, bottom, shoes, weapon, gender);
		} else if (job == 2) { // Aran
			status = LegendCreator.createCharacter(c, name, face, hair + haircolor, skincolor, top, bottom, shoes, weapon, gender);
		} else {
			c.sendPacket(PacketCreator.deleteCharResponse(0, 9));
			return;
		}
                
                if (status == -2) {
                        c.sendPacket(PacketCreator.deleteCharResponse(0, 9));
                }
	}
}