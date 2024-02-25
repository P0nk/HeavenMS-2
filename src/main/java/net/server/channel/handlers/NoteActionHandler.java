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

import client.Client;
import model.Note;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.NoteService;
import tools.PacketCreator;

import java.util.Optional;

public final class NoteActionHandler extends AbstractPacketHandler {
    private static final Logger log = LoggerFactory.getLogger(NoteActionHandler.class);

    private final NoteService noteService;

    public NoteActionHandler(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    public void handlePacket(InPacket p, Client c) {
        int action = p.readByte();
        if (action == 0 && c.getPlayer().getCashShop().getAvailableNotes() > 0) { // Reply to gift in cash shop
            String charname = p.readString();
            String message = p.readString();
            if (c.getPlayer().getCashShop().isOpened()) {
                c.sendPacket(PacketCreator.showCashInventory(c));
            }

            boolean sendNoteSuccess = noteService.sendWithFame(message, c.getPlayer().getName(), charname);
            if (sendNoteSuccess) {
                c.getPlayer().getCashShop().decreaseNotes();
            }
        } else if (action == 1) { // Discard notes in game
            int num = p.readByte();
            p.readByte();
            p.readByte();
            int fame = 0;
            for (int i = 0; i < num; i++) {
                int id = p.readInt();
                p.readByte(); //Fame, but we read it from the database :)

                Optional<Note> discardedNote = noteService.delete(id);
                if (discardedNote.isEmpty()) {
                    log.warn("Note with id {} not able to be discarded. Already discarded?", id);
                    continue;
                }

                fame += discardedNote.get().fame();
            }
            if (fame > 0) {
                c.getPlayer().gainFame(fame);
            }
        }
    }
}
