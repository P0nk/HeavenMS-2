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
package server.maps;

import client.Character;
import client.Client;
import tools.PacketCreator;


public class Dragon extends AbstractAnimatedMapObject {
    private final Character owner;

    public Dragon(Character chr) {
        super();
        this.owner = chr;
        this.setPosition(chr.getPosition());
        this.setStance(chr.getStance());
        this.sendSpawnData(chr.getClient());
    }

    @Override
    public MapObjectType getType() {
        return MapObjectType.DRAGON;
    }

    @Override
    public void sendSpawnData(Client client) {
        client.sendPacket(PacketCreator.spawnDragon(this));
    }

    @Override
    public int getObjectId() {
        return owner.getId();
    }

    @Override
    public void sendDestroyData(Client c) {
        c.sendPacket(PacketCreator.removeDragon(owner.getId()));
    }

    public Character getOwner() {
        return owner;
    }
}