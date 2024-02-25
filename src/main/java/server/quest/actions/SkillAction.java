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
package server.quest.actions;

import client.Character;
import client.Job;
import client.Skill;
import client.SkillFactory;
import provider.Data;
import provider.DataTool;
import server.quest.Quest;
import server.quest.QuestActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler (Twdtwd)
 */
public class SkillAction extends AbstractQuestAction {
    int itemEffect;
    Map<Integer, SkillData> skillData = new HashMap<>();

    public SkillAction(Quest quest, Data data) {
        super(QuestActionType.SKILL, quest);
        processData(data);
    }


    @Override
    public void processData(Data data) {
        for (Data sEntry : data) {
            byte skillLevel = 0;
            int skillid = DataTool.getInt(sEntry.getChildByPath("id"));
            Data skillLevelData = sEntry.getChildByPath("skillLevel");
            if (skillLevelData != null) {
                skillLevel = (byte) DataTool.getInt(skillLevelData);
            }
            int masterLevel = DataTool.getInt(sEntry.getChildByPath("masterLevel"));
            List<Integer> jobs = new ArrayList<>();

            Data applicableJobs = sEntry.getChildByPath("job");
            if (applicableJobs != null) {
                for (Data applicableJob : applicableJobs.getChildren()) {
                    jobs.add(DataTool.getInt(applicableJob));
                }
            }

            skillData.put(skillid, new SkillData(skillid, skillLevel, masterLevel, jobs));
        }
    }

    @Override
    public void run(Character chr, Integer extSelection) {
        for (SkillData skill : skillData.values()) {
            Skill skillObject = SkillFactory.getSkill(skill.getId());
            if (skillObject == null) {
                continue;
            }

            boolean shouldLearn = skill.jobsContains(chr.getJob()) || skillObject.isBeginnerSkill();

            byte skillLevel = (byte) Math.max(skill.getLevel(), chr.getSkillLevel(skillObject));
            int masterLevel = Math.max(skill.getMasterLevel(), chr.getMasterLevel(skillObject));
            if (shouldLearn) {
                chr.changeSkillLevel(skillObject, skillLevel, masterLevel, -1);
            }

        }
    }

    private class SkillData {
        protected int id, level, masterLevel;
        List<Integer> jobs = new ArrayList<>();

        public SkillData(int id, int level, int masterLevel, List<Integer> jobs) {
            this.id = id;
            this.level = level;
            this.masterLevel = masterLevel;
            this.jobs = jobs;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public int getMasterLevel() {
            return masterLevel;
        }

        public boolean jobsContains(Job job) {
            return jobs.contains(job.getId());
        }


    }
} 