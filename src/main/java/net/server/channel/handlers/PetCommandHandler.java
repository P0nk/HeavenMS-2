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
package net.server.channel.handlers;

import client.Character;
import client.Client;
import client.inventory.Pet;
import client.inventory.PetCommand;
import client.inventory.PetDataFactory;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import tools.PacketCreator;
import tools.Randomizer;

public final class PetCommandHandler extends AbstractPacketHandler {

    @Override
    public final void handlePacket(InPacket p, Client c) {
        Character chr = c.getPlayer();
        int petId = p.readInt();
        byte petIndex = chr.getPetIndex(petId);
        Pet pet;
        if (petIndex == -1) {
            return;
        } else {
            pet = chr.getPet(petIndex);
        }
        p.readInt();
        p.readByte();
        byte command = p.readByte();
        PetCommand petCommand = PetDataFactory.getPetCommand(pet.getItemId(), command);
        if (petCommand == null) {
            return;
        }

        if (Randomizer.nextInt(100) < petCommand.getProbability()) {
            pet.gainTamenessFullness(chr, petCommand.getIncrease(), 0, command);
            chr.getMap().broadcastMessage(PacketCreator.commandResponse(chr.getId(), petIndex, false, command, false));
        } else {
            chr.getMap().broadcastMessage(PacketCreator.commandResponse(chr.getId(), petIndex, true, command, false));
        }
    }
}
