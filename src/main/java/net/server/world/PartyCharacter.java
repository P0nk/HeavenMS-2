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
package net.server.world;

import client.Character;
import client.Job;

public class PartyCharacter {
    private final String name;
    private int id;
    private int level;
    private int channel, world;
    private int jobid;
    private int mapid;
    private boolean online;
    private Job job;
    private Character character;

    public PartyCharacter(Character maplechar) {
        this.character = maplechar;
        this.name = maplechar.getName();
        this.level = maplechar.getLevel();
        this.channel = maplechar.getClient().getChannel();
        this.world = maplechar.getWorld();
        this.id = maplechar.getId();
        this.jobid = maplechar.getJob().getId();
        this.mapid = maplechar.getMapId();
        this.online = true;
        this.job = maplechar.getJob();
    }

    public PartyCharacter() {
        this.name = "";
    }

    public Character getPlayer() {
        return character;
    }

    public Job getJob() {
        return job;
    }

    public int getLevel() {
        return level;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public boolean isLeader() {
        return getPlayer().isPartyLeader();
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
        if (!online) {
            this.character = null;  // thanks Feras for noticing offline party members retaining whole character object unnecessarily
        }
    }

    public int getMapId() {
        return mapid;
    }

    public void setMapId(int mapid) {
        this.mapid = mapid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getJobId() {
        return jobid;
    }

    public int getGuildId() {
        return character.getGuildId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartyCharacter other = (PartyCharacter) obj;
        if (name == null) {
            return other.name == null;
        } else {
            return name.equals(other.name);
        }
    }

    public int getWorld() {
        return world;
    }

}
