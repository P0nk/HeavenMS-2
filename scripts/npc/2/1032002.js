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
/* Francois
	Victoria Island: Ellinia (101000000)
	
	Refining NPC: (magicians)
	* Gloves
	* Glove Upgrades
	* Hats
	* Wand
	* Staff
*/

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;

function start() {
    cm.getPlayer().setCS(true);
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
    }
    if (status == 0 && mode == 1) {
        var selStr = "Welcome to my eco-safe refining operation! What would you like today?#b"
        var options = ["Make a glove", "Upgrade a glove", "Upgrade a hat", "Make a wand", "Make a staff"];
        for (var i = 0; i < options.length; i++) {
            selStr += "\r\n#L" + i + "# " + options[i] + "#l";
        }

        cm.sendSimple(selStr);
    } else if (status == 1 && mode == 1) {
        selectedType = selection;
        if (selectedType == 0) { //glove refine
            var selStr = "So, what kind of glove would you like me to make?#b";
            var items = ["Lemona#k - Magician Lv. 15#b", "Blue Morrican#k - Magician Lv. 20#b", "Ocean Mesana#k - Magician Lv. 25#b", "Red Lutia#k - Magician Lv. 30#b", "Red Noel#k - Magician Lv. 35#b", "Red Arten#k - Magician Lv. 40#b",
                "Red Pennance#k - Magician Lv. 50#b", "Steel Manute#k - Magician Lv. 60#b"];
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 1) { //glove upgrade
            var selStr = "So, what kind of glove are you looking to upgrade to?#b";
            var items = ["Green Morrican#k - Magician Lv. 20#b", "Purple Morrican#k - Magician Lv. 20#b", "Blood Mesana#k - Magician Lv. 25#b", "Dark Mesana#k - Magician Lv. 25#b", "Blue Lutia#k - Magician Lv. 30#b", "Black Lutia#k - Magician Lv. 30#b",
                "Blue Noel#k - Magician Lv. 35#b", "Dark Noel#k - Magician Lv. 35#b", "Blue Arten#k - Magician Lv. 40#b", "Dark Arten#k - Magician Lv. 40#b", "Blue Pennance#k - Magician Lv. 50#b", "Dark Pennance#k - Magician Lv. 50#b",
                "Gold Manute#k - Magician Lv. 60#b", "Dark Manute#k - Magician Lv. 60#b"];
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 2) { //hat upgrade
            var selStr = "A hat? Which one were you thinking of?#b";
            var items = ["Steel Pride#k - Magician Lv. 30#b", "Golden Pride#k - Magician Lv. 30#b"];
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 3) { //wand refine
            var selStr = "A wand, huh? Prefer the smaller weapon that fits in your pocket? Which type are you seeking?#b";
            var items = ["Wooden Wand#k - Common Lv. 8#b", "Hardwood Wand#k - Common Lv. 13#b", "Metal Wand#k - Common Lv. 18#b", "Ice Wand#k - Magician Lv. 23#b", "Mithril Wand#k - Magician Lv. 28#b",
                "Wizard Wand#k - Magician Lv. 33#b", "Fairy Wand#k - Magician Lv. 38#b", "Cromi#k - Magician Lv. 48#b"];
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        } else if (selectedType == 4) { //staff refine
            var selStr = "Ah, a staff, a great symbol of one's power! Which are you looking to make?#b";
            var items = ["Wooden Staff#k - Magician Lv. 10#b", "Sapphire Staff#k - Magician Lv. 15#b", "Emerald Staff#k - Magician Lv. 15#b", "Old Wooden Staff#k - Magician Lv. 20#b", "Wizard Staff#k - Magician Lv. 25#b",
                "Arc Staff#k - Magician Lv. 45#b"];
            for (var i = 0; i < items.length; i++) {
                selStr += "\r\n#L" + i + "# " + items[i] + "#l";
            }
            cm.sendSimple(selStr);
        }
    } else if (status == 2 && mode == 1) {
        selectedItem = selection;

        if (selectedType == 0) { //glove refine
            var itemSet = [1082019, 1082020, 1082026, 1082051, 1082054, 1082062, 1082081, 1082086];
            var matSet = [4000021, [4000021, 4011001], [4000021, 4011006], [4000021, 4021006, 4021000], [4000021, 4011006, 4011001, 4021000],
                [4000021, 4021000, 4021006, 4003000], [4021000, 4011006, 4000030, 4003000], [4011007, 4011001, 4021007, 4000030, 4003000]];
            var matQtySet = [15, [30, 1], [50, 2], [60, 1, 2], [70, 1, 3, 2], [80, 3, 3, 30], [3, 2, 35, 40], [1, 8, 1, 50, 50]];
            var costSet = [7000, 15000, 20000, 25000, 30000, 40000, 50000, 70000];
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 1) { //glove upgrade
            var itemSet = [1082021, 1082022, 1082027, 1082028, 1082052, 1082053, 1082055, 1082056, 1082063, 1082064, 1082082, 1082080, 1082087, 1082088];
            var matSet = [[1082020, 4011001], [1082020, 4021001], [1082026, 4021000], [1082026, 4021008], [1082051, 4021005],
                [1082051, 4021008], [1082054, 4021005], [1082054, 4021008], [1082062, 4021002], [1082062, 4021008],
                [1082081, 4021002], [1082081, 4021008], [1082086, 4011004, 4011006], [1082086, 4021008, 4011006]];
            var matQtySet = [[1, 1], [1, 2], [1, 3], [1, 1], [1, 3], [1, 1], [1, 3], [1, 1], [1, 4],
                [1, 2], [1, 5], [1, 3], [1, 3, 5], [1, 2, 3]];
            var costSet = [20000, 25000, 30000, 40000, 35000, 40000, 40000, 45000, 45000, 50000, 55000, 60000, 70000, 80000];
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 2) { //hat upgrade
            var itemSet = [1002065, 1002013];
            var matSet = [[1002064, 4011001], [1002064, 4011006]];
            var matQtySet = [[1, 3], [1, 3]];
            var costSet = [40000, 50000];
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 3) { //wand refine
            var itemSet = [1372005, 1372006, 1372002, 1372004, 1372003, 1372001, 1372000, 1372007];
            var matSet = [4003001, [4003001, 4000001], [4011001, 4000009, 4003000], [4011002, 4003002, 4003000], [4011002, 4021002, 4003000],
                [4021006, 4011002, 4011001, 4003000], [4021006, 4021005, 4021007, 4003003, 4003000], [4011006, 4021003, 4021007, 4021002, 4003002, 4003000]];
            var matQtySet = [5, [10, 50], [1, 30, 5], [2, 1, 10], [3, 1, 10], [5, 3, 1, 15], [5, 5, 1, 1, 20], [4, 3, 2, 1, 1, 30]];
            var costSet = [1000, 3000, 5000, 12000, 30000, 60000, 120000, 200000];
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 4) { //staff refine
            var itemSet = [1382000, 1382003, 1382005, 1382004, 1382002, 1382001];
            var matSet = [4003001, [4021005, 4011001, 4003000], [4021003, 4011001, 4003000], [4003001, 4011001, 4003000],
                [4021006, 4021001, 4011001, 4003000], [4011001, 4021006, 4021001, 4021005, 4003000, 4000010, 4003003]];
            var matQtySet = [5, [1, 1, 5], [1, 1, 5], [50, 1, 10], [2, 1, 1, 15], [8, 5, 5, 5, 30, 50, 1]];
            var costSet = [2000, 2000, 2000, 5000, 12000, 180000];
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        }

        var prompt = "You want me to make a #t" + item + "#? In that case, I'm going to need specific items from you in order to make it. Make sure you have room in your inventory, though!#b";

        if (mats instanceof Array) {
            for (var i = 0; i < mats.length; i++) {
                prompt += "\r\n#i" + mats[i] + "# " + matQty[i] + " #t" + mats[i] + "#";
            }
        } else {
            prompt += "\r\n#i" + mats + "# " + matQty + " #t" + mats + "#";
        }

        if (cost > 0) {
            prompt += "\r\n#i4031138# " + cost + " meso";
        }

        cm.sendYesNo(prompt);
    } else if (status == 3 && mode == 1) {
        var complete = true;

        if (!cm.canHold(item, 1)) {
            cm.sendOk("Check your inventory for a free slot first.");
            cm.dispose();
            return;
        } else if (cm.getMeso() < cost) {
            cm.sendOk("Sorry, but all of us need money to live. Come back when you can pay my fees, yes?")
            cm.dispose();
            return;
        } else {
            if (mats instanceof Array) {
                for (var i = 0; complete && i < mats.length; i++) {
                    if (!cm.haveItem(mats[i], matQty[i])) {
                        complete = false;
                    }
                }
            } else if (!cm.haveItem(mats, matQty)) {
                complete = false;
            }
        }

        if (!complete) {
            cm.sendOk("Uhm... I don't keep extra material on me. Sorry. ");
        } else {
            if (mats instanceof Array) {
                for (var i = 0; i < mats.length; i++) {
                    cm.gainItem(mats[i], -matQty[i]);
                }
            } else {
                cm.gainItem(mats, -matQty);
            }

            if (cost > 0) {
                cm.gainMeso(-cost);
            }

            cm.gainItem(item, 1);
            cm.sendOk("It's a success! Oh, I've never felt so alive! Please come back again!");
        }
        cm.dispose();
    }
}