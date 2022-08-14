/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/* Dr. Rhomes
	Orbis Random/VIP Eye Color Change.
*/
var status = 0;
var beauty = 0;
var colors = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 1) {  // disposing issue with stylishs found thanks to Vcoc
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            cm.sendSimple("Hello, I'm Dr. Rhomes, head of the cosmetic lens department here at the Orbis Plastic Surgery Shop.\r\nMy goal here is to add personality to everyone's eyes through the wonders of cosmetic lenses, and with #b#t5152011##k or #b#t5152014##k, I can do the same for you, too! Now, what would you like to use?\r\n#L1#Cosmetic Lenses: #i5152011##t5152011##l\r\n#L2#Cosmetic Lenses: #i5152014##t5152014##l\r\n#L3#One-time Cosmetic Lenses: #i5152104# (any color)#l");
        } else if (status == 1) {
            if (selection == 1) {
                beauty = 1;
                selectedRegularCoupon()
            } else if (selection == 2) {
                beauty = 2;
                selectedVipCoupon()
            } else if (selection == 3) {
                beauty = 3;
                selectedOneTimeCoupon()
            }
        } else if (status == 2) {
            cm.dispose();
            if (beauty == 1) {
                acceptedRegularCoupon()
            } else if (beauty == 2) {
                selectedVipStyle(selection)
            } else if (beauty == 3) {
                selectedOneTimeStyle(selection)
            }
        }
    }
}

function selectedRegularCoupon() {
    if (cm.getPlayer().getGender() == 0) {
        var current = cm.getPlayer().getFace() % 100 + 20000;
    }
    if (cm.getPlayer().getGender() == 1) {
        var current = cm.getPlayer().getFace() % 100 + 21000;
    }
    colors = Array();
    pushIfItemsExists(colors, [current + 100, current + 300, current + 400, current + 700]);
    cm.sendYesNo("If you use the regular coupon, you'll be awarded a random pair of cosmetic lenses. Are you going to use a #b#t5152011##k and really make the change to your eyes?");
}

function selectedVipCoupon() {
    if (cm.getPlayer().isMale()) {
        var current = cm.getPlayer().getFace() % 100 + 20000;
    } else {
        var current = cm.getPlayer().getFace() % 100 + 21000;
    }

    colors = Array();
    pushIfItemsExists(colors, [current + 100, current + 300, current + 400, current + 700]);
    cm.sendStyle("With our new computer program, you can see yourself after the treatment in advance. What kind of lens would you like to wear? Please choose the style of your liking.", colors);
}

function pushIfItemsExists(array, itemidList) {
    for (var i = 0; i < itemidList.length; i++) {
        var itemid = itemidList[i];

        if ((itemid = cm.getCosmeticItem(itemid)) != -1 && !cm.isCosmeticEquipped(itemid)) {
            array.push(itemid);
        }
    }
}

function selectedOneTimeCoupon() {
    if (cm.getPlayer().isMale()) {
        var current = cm.getPlayer().getFace() % 100 + 20000;
    } else {
        var current = cm.getPlayer().getFace() % 100 + 21000;
    }

    colors = Array();
    for (var i = 0; i < 8; i++) {
        const oneTimeCouponId = 5152100 + i
        if (cm.haveItem(oneTimeCouponId)) {
            pushIfItemExists(colors, current + 100 * i);
        }
    }

    if (colors.length == 0) {
        cm.sendOk("You don't have any One-Time Cosmetic Lens to use.");
        cm.dispose();
        return;
    }

    cm.sendStyle("What kind of lens would you like to wear? Please choose the style of your liking.", colors);
}

function pushIfItemExists(array, itemid) {
    if ((itemid = cm.getCosmeticItem(itemid)) != -1 && !cm.isCosmeticEquipped(itemid)) {
        array.push(itemid);
    }
}

function acceptedRegularCoupon() {
    const regularCouponItemId = 5152011
    if (cm.haveItem(regularCouponItemId)) {
        cm.gainItem(regularCouponItemId, -1);
        cm.setFace(colors[Math.floor(Math.random() * colors.length)]);
        cm.sendOk("Enjoy your new and improved cosmetic lenses!");
    } else {
        sendLackingCoupon()
    }
}

function selectedVipStyle(selection) {
    const vipCouponItemId = 5152014
    if (cm.haveItem(vipCouponItemId)) {
        cm.gainItem(vipCouponItemId, -1);
        const selectedFace = colors[selection]
        cm.setFace(selectedFace);
        cm.sendOk("Enjoy your new and improved cosmetic lenses!");
    } else {
        sendLackingCoupon()
    }
}

function selectedOneTimeStyle(selection) {
    const selectedFace = colors[selection]
    const color = Math.floor(selectedFace / 100) % 10;

    const oneTimeCouponItemId = 5152100 + color
    if (cm.haveItem(oneTimeCouponItemId)) {
        cm.gainItem(oneTimeCouponItemId, -1);
        cm.setFace(selectedFace);
        cm.sendOk("Enjoy your new and improved cosmetic lenses!");
    } else {
        sendLackingCoupon()
    }
}

function sendLackingCoupon() {
    cm.sendOk("I'm sorry, but I don't think you have our cosmetic lens coupon with you right now. Without the coupon, I'm afraid I can't do it for you..");
}
