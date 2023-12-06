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
package client.command.commands.gm0;

import client.Character;
import client.Client;
import client.Job;
import client.command.Command;
import net.server.Server;
import client.command.CommandsExecutor;
import client.inventory.InventoryType;
import client.inventory.Item;
import client.inventory.Equip;
import constants.game.GameConstants;
import constants.id.NpcId;
import org.slf4j.LoggerFactory;
import server.ItemInformationProvider;
import server.maps.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
    -bobby
    havent done any error checking yet, seems fine tho
*/

public class InspectCommand extends Command {

    {
        setDescription("Inspect a player.");

    }
    public static String EQUIPS_INFO = "";

    private static void sortGotoEntries(List<Entry<String, Integer>> listEntries) {
        listEntries.sort((e1, e2) -> e1.getValue().compareTo(e2.getValue()));
    }

    @Override
    public void execute(Client c, String[] params) {
        Character myGuy = c.getPlayer();
        Character player = null;
        if(params.length < 1){
            player = c.getPlayer();
        }
        else{
            player = Server.getInstance().getWorld(0).getPlayerStorage().getCharacterByName(params[0]);

        }
        if(player == null){
            myGuy.dropMessage(5, "Player not found. Could be offline.");
            return;
        }

        String sendStr = player.getName();
        sendStr += "\r\n" + Job.getJobAsString(player.getJob()) + "\r\n\r\n";
        sendStr += "Lv.\t\t" + player.getLevel() +
                "\r\nSTR:\t" + player.getTotalStr() +
                "\r\nDEX:\t" + player.getTotalDex() +
                "\r\nINT:\t\t" + player.getTotalInt() +
                "\r\nLUK:\t" + player.getTotalLuk() +
                "\r\nWeapon Att:" + player.getTotalWatk() + " - Magic Att:" + player.getTotalMagic() +
                "\r\n";

        //        //#fUI/UIWindow.img/QuestIcon/4/0#
        Integer totalLevel = player.getSkillLevel(20000012) + player.getSkillLevel(10000012) + player.getSkillLevel(12);
        sendStr += "#fSkill/1000.img/skill/10000012/icon#\r\n" +
                "Blessing of the Fairy - Lv. " + totalLevel + "\r\n\r\n";

        sendStr += player.getEquipInspectString((short) -11);   //weapon
        sendStr += player.getEquipInspectString((short) -10);   //shield
        sendStr += player.getEquipInspectString((short) -1);    //shoes?
        sendStr += player.getEquipInspectString((short) -2);    //idk
        sendStr += player.getEquipInspectString((short) -3);    //idk
        sendStr += player.getEquipInspectString((short) -4);    //idk
        sendStr += player.getEquipInspectString((short) -5);    //helmet
        sendStr += player.getEquipInspectString((short) -6);    //body
        sendStr += player.getEquipInspectString((short) -7);    //legs
        sendStr += player.getEquipInspectString((short) -8);    //gloves
        sendStr += player.getEquipInspectString((short) -9);    //idk
        sendStr += player.getEquipInspectString((short) -49);   //amulet


        myGuy.getAbstractPlayerInteraction().npcTalk(NpcId.SPINEL, sendStr);
        return;

    }
}
