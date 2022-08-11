/*
    This file is part of the HeavenMS MapleStory Server, commands OdinMS-based
    Copyleft (L) 2016 - 2019 RonanLana

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

/*
   @Author: Arthur L - Refactored command content into modules
*/
package client.command.commands.gm2;

import client.Character;
import client.*;
import client.command.Command;
import constants.skills.Brawler;
import constants.skills.Magician;
import constants.skills.Warrior;
import provider.Data;
import provider.DataProviderFactory;
import provider.wz.WZFiles;

public class ResetSkillCommand extends Command {
    {
        setDescription("Set all skill levels to 0.");
    }

    @Override
    public void execute(Client c, String[] params) {
        Character player = c.getPlayer();
        for (Data skill_ : DataProviderFactory.getDataProvider(WZFiles.STRING).getData("Skill.img").getChildren()) {
            try {
                Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                int curLevel = player.getSkillLevel(skill);
                // Handle HP/MP increase per level skills
                if (skill.getId() == Warrior.IMPROVED_MAXHP || skill.getId() == Brawler.IMPROVE_MAX_HP)
                {
                    if(curLevel > 0){
                        int currentIncrease = skill.getEffect(curLevel).getX() * player.getLevel();
                        player.addMaxHP(-currentIncrease);
                    }
                }
                else if(skill.getId() == Magician.IMPROVED_MAX_MP_INCREASE)
                {
                    if(curLevel > 0){
                        int currentIncrease = skill.getEffect(curLevel).getX() * player.getLevel();
                        player.addMaxMP(-currentIncrease);
                    }
                }
                
                player.changeSkillLevel(skill, (byte) 0, skill.getMaxLevel(), -1);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                break;
            } catch (NullPointerException npe) {
            }
        }

        if (player.getJob().isA(Job.ARAN1) || player.getJob().isA(Job.LEGEND)) {
            Skill skill = SkillFactory.getSkill(5001005);
            player.changeSkillLevel(skill, (byte) -1, -1, -1);
        } else {
            Skill skill = SkillFactory.getSkill(21001001);
            player.changeSkillLevel(skill, (byte) -1, -1, -1);
        }

        player.yellowMessage("Skills reseted.");
    }
}
