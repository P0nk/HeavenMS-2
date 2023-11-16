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
package server;

import client.inventory.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author Traitor
 */
public class MTSItemInfo {
    private final int price;
    private final Item item;
    private final String seller;
    private final int id;
    private final int year;
    private final int month;
    private int day = 1;

    public MTSItemInfo(Item item, int price, int id, int cid, String seller, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate sellEnd = LocalDate.parse(date, formatter);

        this.item = item;
        this.price = price;
        this.seller = seller;
        this.id = id;
        this.year = sellEnd.getYear();
        this.month = sellEnd.getMonthValue();
        this.day = sellEnd.getDayOfMonth();
    }

    public Item getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }

    public int getTaxes() {
        return 100 + price / 10;
    }

    public int getID() {
        return id;
    }

    public long getEndingDate() {
        Calendar now = Calendar.getInstance();
        now.set(year, month - 1, day);
        return now.getTimeInMillis();
    }

    public String getSeller() {
        return seller;
    }
}
