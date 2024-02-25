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
package provider.wz;

import provider.DataDirectoryEntry;
import provider.DataEntity;
import provider.DataEntry;
import provider.DataFileEntry;

import java.util.*;

public class WZDirectoryEntry extends WZEntry implements DataDirectoryEntry {
    private final List<DataDirectoryEntry> subdirs = new ArrayList<>();
    private final List<DataFileEntry> files = new ArrayList<>();
    private final Map<String, DataEntry> entries = new HashMap<>();

    public WZDirectoryEntry(String name, int size, int checksum, DataEntity parent) {
        super(name, size, checksum, parent);
    }

    public WZDirectoryEntry() {
        super(null, 0, 0, null);
    }

    public void addDirectory(DataDirectoryEntry dir) {
        subdirs.add(dir);
        entries.put(dir.getName(), dir);
    }

    public void addFile(DataFileEntry fileEntry) {
        files.add(fileEntry);
        entries.put(fileEntry.getName(), fileEntry);
    }

    public List<DataDirectoryEntry> getSubdirectories() {
        return Collections.unmodifiableList(subdirs);
    }

    public List<DataFileEntry> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public DataEntry getEntry(String name) {
        return entries.get(name);
    }
}
