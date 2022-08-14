/*
    This file is part of the HeavenMS MapleStory Server
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
    1101004 - Oz, Ellinia
    |- Repurposed to serve as Magician Skill helper
 */

var status;
var options = ["Explain my skills.", "Reset my skills.", "I'm good, thanks."];
var defaultText = "I'm Oz, the Magician Skill Expert. Sorry, but I can only help Magicians!";
var mageText = "Hiya, friend! I'm Chief Knight Oz. I know all sorts of magic spells, so feel free to come to me if you have any questions!";
var resetSkillCostText = "Resetting all of your SP will incur a cost. I will reset all of your SP for ";
var resetSkillCostFreeText = "Resetting all of your SP will incur a cost. However, since you are under level 31, I will do this for free.\r\n";
var resetSkillsExplanationText = "Also understand that you must assign a minimum number of SP to your earlier skills before you can apply SP to higher rank skills.\r\n";
var resetSkillsConfirmationText = "#rDo you really want to reset ALL of your assigned skill points?#k";
var resetSPCost = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
		if (mode == 1) {
			status++;
		} else {
			if (status <= 1) {
				cm.dispose();
				return;
			}
			status--;
		}

        if (status == 0) {
            if (cm.isJobType(2)) {
					cm.sendNext(mageText);
                }
             else {
                cm.sendOk(defaultText);
                cm.dispose();
            }
		}
		else if (status == 1){
			cm.sendSimple("I can give you an explanation of your skills, or, if you've made a mistake in alloting your skill points, I can reset them for you. What would you like help with?\r\n" + generateSelectionMenu(options));
		}
		else if(status == 2){
				if(selection == 0){
					cm.sendOk("I cannot explain skills at this time.");
					cm.dispose();
				}
				else if(selection == 1){
					if(cm.getLevel() < 31){					
						cm.sendYesNo(resetSkillCostFreeText + resetSkillsConfirmationText);
					}
					else{
						resetSPCost = cm.getLevel() * 10000;
						cm.sendYesNo(resetSkillCostText + "#b" + resetSPCost + "#k mesos.\r\n" + resetSkillsExplanationText + resetSkillsConfirmationText);
					}
				}
				else if(selection == 2){
					cm.sendOk("I'll be here when you need me.");
					cm.dispose();
				}
				else{
					cm.dispose();
				}
		}
		else if(status == 3){
			if(cm.getMeso() < resetSPCost){
				cm.sendOk("You don't appear to have enough mesos to reset your skills. Sadly, I cannot waive my fee. Please come back later.");
				cm.dispose();
			}
			else{
				if(resetSPCost > 0)
					cm.gainMeso(-resetSPCost);
				cm.resetSkills();
				cm.sendOk("It is done! Please assign your SP wisely. Return if you wish to reset your skills once more.")
				cm.dispose();
			}
		}
      }
	  
function generateSelectionMenu(array) {     // nice tool for generating a string for the sendSimple functionality
    var menu = "";
    for (var i = 0; i < array.length; i++) {
        menu += "#b#L" + i + "#" + array[i] + "#l#k\r\n";
    }
    return menu;
}