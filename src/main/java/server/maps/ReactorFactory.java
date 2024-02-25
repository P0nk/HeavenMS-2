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

import provider.Data;
import provider.DataProvider;
import provider.DataProviderFactory;
import provider.DataTool;
import provider.wz.WZFiles;
import server.maps.ReactorStats.StateData;
import tools.Pair;
import tools.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactorFactory {
    private static final DataProvider data = DataProviderFactory.getDataProvider(WZFiles.REACTOR);
    private static final Map<Integer, ReactorStats> reactorStats = new HashMap<>();

    public static final ReactorStats getReactorS(int rid) {
        ReactorStats stats = reactorStats.get(rid);
        if (stats == null) {
            int infoId = rid;
            Data reactorData = data.getData(StringUtil.getLeftPaddedStr(infoId + ".img", '0', 11));
            Data link = reactorData.getChildByPath("info/link");
            if (link != null) {
                infoId = DataTool.getIntConvert("info/link", reactorData);
                stats = reactorStats.get(infoId);
            }
            if (stats == null) {
                stats = new ReactorStats();
                reactorData = data.getData(StringUtil.getLeftPaddedStr(infoId + ".img", '0', 11));
                if (reactorData == null) {
                    return stats;
                }
                boolean canTouch = DataTool.getInt("info/activateByTouch", reactorData, 0) > 0;
                boolean areaSet = false;
                boolean foundState = false;
                for (byte i = 0; true; i++) {
                    Data reactorD = reactorData.getChildByPath(String.valueOf(i));
                    if (reactorD == null) {
                        break;
                    }
                    Data reactorInfoData_ = reactorD.getChildByPath("event");
                    if (reactorInfoData_ != null && reactorInfoData_.getChildByPath("0") != null) {
                        Data reactorInfoData = reactorInfoData_.getChildByPath("0");
                        Pair<Integer, Integer> reactItem = null;
                        int type = DataTool.getIntConvert("type", reactorInfoData);
                        if (type == 100) { //reactor waits for item
                            reactItem = new Pair<>(DataTool.getIntConvert("0", reactorInfoData), DataTool.getIntConvert("1", reactorInfoData, 1));
                            if (!areaSet) { //only set area of effect for item-triggered reactors once
                                stats.setTL(DataTool.getPoint("lt", reactorInfoData));
                                stats.setBR(DataTool.getPoint("rb", reactorInfoData));
                                areaSet = true;
                            }
                        }
                        foundState = true;
                        stats.addState(i, type, reactItem, (byte) DataTool.getIntConvert("state", reactorInfoData), DataTool.getIntConvert("timeOut", reactorInfoData_, -1), (byte) (canTouch ? 2 : (DataTool.getIntConvert("2", reactorInfoData, 0) > 0 || reactorInfoData.getChildByPath("clickArea") != null || type == 9 ? 1 : 0)));
                    } else {
                        stats.addState(i, 999, null, (byte) (foundState ? -1 : (i + 1)), 0, (byte) 0);
                    }
                }
                reactorStats.put(infoId, stats);
                if (rid != infoId) {
                    reactorStats.put(rid, stats);
                }
            } else { // stats exist at infoId but not rid; add to map
                reactorStats.put(rid, stats);
            }
        }
        return stats;
    }

    public static ReactorStats getReactor(int rid) {
        ReactorStats stats = reactorStats.get(rid);
        if (stats == null) {
            int infoId = rid;
            Data reactorData = data.getData(StringUtil.getLeftPaddedStr(infoId + ".img", '0', 11));
            Data link = reactorData.getChildByPath("info/link");
            if (link != null) {
                infoId = DataTool.getIntConvert("info/link", reactorData);
                stats = reactorStats.get(infoId);
            }
            Data activateOnTouch = reactorData.getChildByPath("info/activateByTouch");
            boolean loadArea = false;
            if (activateOnTouch != null) {
                loadArea = DataTool.getInt("info/activateByTouch", reactorData, 0) != 0;
            }
            if (stats == null) {
                reactorData = data.getData(StringUtil.getLeftPaddedStr(infoId + ".img", '0', 11));
                Data reactorInfoData = reactorData.getChildByPath("0");
                stats = new ReactorStats();
                List<StateData> statedatas = new ArrayList<>();
                if (reactorInfoData != null) {
                    boolean areaSet = false;
                    byte i = 0;
                    while (reactorInfoData != null) {
                        Data eventData = reactorInfoData.getChildByPath("event");
                        if (eventData != null) {
                            int timeOut = -1;

                            for (Data fknexon : eventData.getChildren()) {
                                if (fknexon.getName().equalsIgnoreCase("timeOut")) {
                                    timeOut = DataTool.getInt(fknexon);
                                } else {
                                    Pair<Integer, Integer> reactItem = null;
                                    int type = DataTool.getIntConvert("type", fknexon);
                                    if (type == 100) { //reactor waits for item
                                        reactItem = new Pair<>(DataTool.getIntConvert("0", fknexon), DataTool.getIntConvert("1", fknexon));
                                        if (!areaSet || loadArea) { //only set area of effect for item-triggered reactors once
                                            stats.setTL(DataTool.getPoint("lt", fknexon));
                                            stats.setBR(DataTool.getPoint("rb", fknexon));
                                            areaSet = true;
                                        }
                                    }
                                    Data activeSkillID = fknexon.getChildByPath("activeSkillID");
                                    List<Integer> skillids = null;
                                    if (activeSkillID != null) {
                                        skillids = new ArrayList<>();
                                        for (Data skill : activeSkillID.getChildren()) {
                                            skillids.add(DataTool.getInt(skill));
                                        }
                                    }
                                    byte nextState = (byte) DataTool.getIntConvert("state", fknexon);
                                    statedatas.add(new StateData(type, reactItem, skillids, nextState));
                                }
                            }
                            stats.addState(i, statedatas, timeOut);
                        }
                        i++;
                        reactorInfoData = reactorData.getChildByPath(Byte.toString(i));
                        statedatas = new ArrayList<>();
                    }
                } else //sit there and look pretty; likely a reactor such as Zakum/Papulatus doors that shows if player can enter
                {
                    statedatas.add(new StateData(999, null, null, (byte) 0));
                    stats.addState((byte) 0, statedatas, -1);
                }
                reactorStats.put(infoId, stats);
                if (rid != infoId) {
                    reactorStats.put(rid, stats);
                }
            } else // stats exist at infoId but not rid; add to map
            {
                reactorStats.put(rid, stats);
            }
        }
        return stats;
    }
}
