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

import client.Character;
import client.inventory.Inventory;
import client.inventory.InventoryType;
import client.inventory.Item;
import client.inventory.manipulator.InventoryManipulator;
import client.inventory.manipulator.KarmaManipulator;
import config.YamlConfig;
import constants.game.GameConstants;
import net.server.coordinator.world.InviteCoordinator;
import net.server.coordinator.world.InviteCoordinator.InviteResult;
import net.server.coordinator.world.InviteCoordinator.InviteResultType;
import net.server.coordinator.world.InviteCoordinator.InviteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.PacketCreator;
import tools.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Matze
 * @author Ronan - concurrency safety + check available slots + trade results
 */
public class Trade {
    private static final Logger log = LoggerFactory.getLogger(Trade.class);

    public enum TradeResult {
        NO_RESPONSE(1),
        PARTNER_CANCEL(2),
        SUCCESSFUL(7),
        UNSUCCESSFUL(8),
        UNSUCCESSFUL_UNIQUE_ITEM_LIMIT(9),
        UNSUCCESSFUL_ANOTHER_MAP(12),
        UNSUCCESSFUL_DAMAGED_FILES(13);

        private final int res;

        TradeResult(int res) {
            this.res = res;
        }

        private byte getValue() {
            return (byte) res;
        }
    }

    private Trade partner = null;
    private final List<Item> items = new ArrayList<>();
    private List<Item> exchangeItems;
    private int meso = 0;
    private int exchangeMeso;
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Character chr;
    private final byte number;
    private boolean fullTrade = false;

    public Trade(byte number, Character chr) {
        this.chr = chr;
        this.number = number;
    }

    public static int getFee(long meso) {
        long fee = 0;
        if (meso >= 100000000) {
            fee = (meso * 6) / 100;
        } else if (meso >= 25000000) {
            fee = (meso * 5) / 100;
        } else if (meso >= 10000000) {
            fee = (meso * 4) / 100;
        } else if (meso >= 5000000) {
            fee = (meso * 3) / 100;
        } else if (meso >= 1000000) {
            fee = (meso * 18) / 1000;
        } else if (meso >= 100000) {
            fee = (meso * 8) / 1000;
        }
        return (int) fee;
    }

    private void lockTrade() {
        locked.set(true);
        partner.getChr().sendPacket(PacketCreator.getTradeConfirmation());
    }

    private void fetchExchangedItems() {
        exchangeItems = partner.getItems();
        exchangeMeso = partner.getMeso();
    }

    private void completeTrade() {
        byte result;
        boolean show = YamlConfig.config.server.USE_DEBUG;
        items.clear();
        meso = 0;

        for (Item item : exchangeItems) {
            KarmaManipulator.toggleKarmaFlagToUntradeable(item);
            InventoryManipulator.addFromDrop(chr.getClient(), item, show);
        }

        if (exchangeMeso > 0) {
            int fee = getFee(exchangeMeso);

            chr.gainMeso(exchangeMeso - fee, show, true, show);
            if (fee > 0) {
                chr.dropMessage(1, "Transaction completed. You received " + GameConstants.numberWithCommas(exchangeMeso - fee) + " mesos due to trade fees.");
            } else {
                chr.dropMessage(1, "Transaction completed. You received " + GameConstants.numberWithCommas(exchangeMeso) + " mesos.");
            }

            result = TradeResult.NO_RESPONSE.getValue();
        } else {
            result = TradeResult.SUCCESSFUL.getValue();
        }

        exchangeMeso = 0;
        if (exchangeItems != null) {
            exchangeItems.clear();
        }

        chr.sendPacket(PacketCreator.getTradeResult(number, result));
    }

    private void cancel(byte result) {
        boolean show = YamlConfig.config.server.USE_DEBUG;

        for (Item item : items) {
            InventoryManipulator.addFromDrop(chr.getClient(), item, show);
        }
        if (meso > 0) {
            chr.gainMeso(meso, show, true, show);
        }
        meso = 0;
        if (items != null) {
            items.clear();
        }
        exchangeMeso = 0;
        if (exchangeItems != null) {
            exchangeItems.clear();
        }

        chr.sendPacket(PacketCreator.getTradeResult(number, result));
    }

    private boolean isLocked() {
        return locked.get();
    }

    private int getMeso() {
        return meso;
    }

    public void setMeso(int meso) {
        if (locked.get()) {
            throw new RuntimeException("Trade is locked.");
        }
        if (meso < 0) {
            log.warn("[Hack] Chr {} is trying to trade negative mesos", chr.getName());
            return;
        }
        if (chr.getMeso() >= meso) {
            chr.gainMeso(-meso, false, true, false);
            this.meso += meso;
            chr.sendPacket(PacketCreator.getTradeMesoSet((byte) 0, this.meso));
            if (partner != null) {
                partner.getChr().sendPacket(PacketCreator.getTradeMesoSet((byte) 1, this.meso));
            }
        } else {
        }
    }

    public boolean addItem(Item item) {
        synchronized (items) {
            if (items.size() > 9) {
                return false;
            }
            for (Item it : items) {
                if (it.getPosition() == item.getPosition()) {
                    return false;
                }
            }

            items.add(item);
        }

        return true;
    }

    public void chat(String message) {
        chr.sendPacket(PacketCreator.getTradeChat(chr, message, true));
        if (partner != null) {
            partner.getChr().sendPacket(PacketCreator.getTradeChat(chr, message, false));
        }
    }

    public Trade getPartner() {
        return partner;
    }

    public void setPartner(Trade partner) {
        if (locked.get()) {
            return;
        }
        this.partner = partner;
    }

    public Character getChr() {
        return chr;
    }

    public List<Item> getItems() {
        return new LinkedList<>(items);
    }

    public int getExchangeMesos() {
        return exchangeMeso;
    }

    private boolean fitsMeso() {
        return chr.canHoldMeso(exchangeMeso - getFee(exchangeMeso));
    }

    private boolean fitsInInventory() {
        List<Pair<Item, InventoryType>> tradeItems = new LinkedList<>();
        for (Item item : exchangeItems) {
            tradeItems.add(new Pair<>(item, item.getInventoryType()));
        }

        return Inventory.checkSpotsAndOwnership(chr, tradeItems);
    }

    private boolean fitsUniquesInInventory() {
        List<Integer> exchangeItemids = new LinkedList<>();
        for (Item item : exchangeItems) {
            exchangeItemids.add(item.getItemId());
        }

        return chr.canHoldUniques(exchangeItemids);
    }

    private synchronized boolean checkTradeCompleteHandshake(boolean updateSelf) {
        Trade self, other;

        if (updateSelf) {
            self = this;
            other = this.getPartner();
        } else {
            self = this.getPartner();
            other = this;
        }

        if (self.isLocked()) {
            return false;
        }

        self.lockTrade();
        return other.isLocked();
    }

    private boolean checkCompleteHandshake() {  // handshake checkout thanks to Ronan
        if (this.getChr().getId() < this.getPartner().getChr().getId()) {
            return this.checkTradeCompleteHandshake(true);
        } else {
            return this.getPartner().checkTradeCompleteHandshake(false);
        }
    }

    public static void completeTrade(Character chr) {
        Trade local = chr.getTrade();
        Trade partner = local.getPartner();
        if (local.checkCompleteHandshake()) {
            local.fetchExchangedItems();
            partner.fetchExchangedItems();

            if (!local.fitsMeso()) {
                cancelTrade(local.getChr(), TradeResult.UNSUCCESSFUL);
                chr.message("There is not enough meso inventory space to complete the trade.");
                partner.getChr().message("Partner does not have enough meso inventory space to complete the trade.");
                return;
            } else if (!partner.fitsMeso()) {
                cancelTrade(partner.getChr(), TradeResult.UNSUCCESSFUL);
                chr.message("Partner does not have enough meso inventory space to complete the trade.");
                partner.getChr().message("There is not enough meso inventory space to complete the trade.");
                return;
            }

            if (!local.fitsInInventory()) {
                if (local.fitsUniquesInInventory()) {
                    cancelTrade(local.getChr(), TradeResult.UNSUCCESSFUL);
                    chr.message("There is not enough inventory space to complete the trade.");
                    partner.getChr().message("Partner does not have enough inventory space to complete the trade.");
                } else {
                    cancelTrade(local.getChr(), TradeResult.UNSUCCESSFUL_UNIQUE_ITEM_LIMIT);
                    partner.getChr().message("Partner cannot hold more than one one-of-a-kind item at a time.");
                }
                return;
            } else if (!partner.fitsInInventory()) {
                if (partner.fitsUniquesInInventory()) {
                    cancelTrade(partner.getChr(), TradeResult.UNSUCCESSFUL);
                    chr.message("Partner does not have enough inventory space to complete the trade.");
                    partner.getChr().message("There is not enough inventory space to complete the trade.");
                } else {
                    cancelTrade(partner.getChr(), TradeResult.UNSUCCESSFUL_UNIQUE_ITEM_LIMIT);
                    chr.message("Partner cannot hold more than one one-of-a-kind item at a time.");
                }
                return;
            }

            if (local.getChr().getLevel() < 15) {
                if (local.getChr().getMesosTraded() + local.exchangeMeso > 1000000) {
                    cancelTrade(local.getChr(), TradeResult.NO_RESPONSE);
                    local.getChr().sendPacket(PacketCreator.serverNotice(1, "Characters under level 15 may not trade more than 1 million mesos per day."));
                    return;
                } else {
                    local.getChr().addMesosTraded(local.exchangeMeso);
                }
            } else if (partner.getChr().getLevel() < 15) {
                if (partner.getChr().getMesosTraded() + partner.exchangeMeso > 1000000) {
                    cancelTrade(partner.getChr(), TradeResult.NO_RESPONSE);
                    partner.getChr().sendPacket(PacketCreator.serverNotice(1, "Characters under level 15 may not trade more than 1 million mesos per day."));
                    return;
                } else {
                    partner.getChr().addMesosTraded(partner.exchangeMeso);
                }
            }

            logTrade(local, partner);
            local.completeTrade();
            partner.completeTrade();

            partner.getChr().setTrade(null);
            chr.setTrade(null);
        }
    }

    private static void cancelTradeInternal(Character chr, byte selfResult, byte partnerResult) {
        Trade trade = chr.getTrade();
        if (trade == null) {
            return;
        }

        trade.cancel(selfResult);
        if (trade.getPartner() != null) {
            trade.getPartner().cancel(partnerResult);
            trade.getPartner().getChr().setTrade(null);

            InviteCoordinator.answerInvite(InviteType.TRADE, trade.getChr().getId(), trade.getPartner().getChr().getId(), false);
            InviteCoordinator.answerInvite(InviteType.TRADE, trade.getPartner().getChr().getId(), trade.getChr().getId(), false);
        }
        chr.setTrade(null);
    }

    private static byte[] tradeResultsPair(byte result) {
        byte selfResult, partnerResult;

        if (result == TradeResult.PARTNER_CANCEL.getValue()) {
            partnerResult = result;
            selfResult = TradeResult.NO_RESPONSE.getValue();
        } else if (result == TradeResult.UNSUCCESSFUL_UNIQUE_ITEM_LIMIT.getValue()) {
            partnerResult = TradeResult.UNSUCCESSFUL.getValue();
            selfResult = result;
        } else {
            partnerResult = result;
            selfResult = result;
        }

        return new byte[]{selfResult, partnerResult};
    }

    private synchronized void tradeCancelHandshake(boolean updateSelf, byte result) {
        byte selfResult, partnerResult;
        Trade self;

        byte[] pairedResult = tradeResultsPair(result);
        selfResult = pairedResult[0];
        partnerResult = pairedResult[1];

        if (updateSelf) {
            self = this;
        } else {
            self = this.getPartner();
        }

        cancelTradeInternal(self.getChr(), selfResult, partnerResult);
    }

    private void cancelHandshake(byte result) {  // handshake checkout thanks to Ronan
        Trade partner = this.getPartner();
        if (partner == null || this.getChr().getId() < partner.getChr().getId()) {
            this.tradeCancelHandshake(true, result);
        } else {
            partner.tradeCancelHandshake(false, result);
        }
    }

    public static void cancelTrade(Character chr, TradeResult result) {
        Trade trade = chr.getTrade();
        if (trade == null) {
            return;
        }

        trade.cancelHandshake(result.getValue());
    }

    public static void startTrade(Character chr) {
        if (chr.getTrade() == null) {
            chr.setTrade(new Trade((byte) 0, chr));
        }
    }

    private static boolean hasTradeInviteBack(Character c1, Character c2) {
        Trade other = c2.getTrade();
        if (other != null) {
            Trade otherPartner = other.getPartner();
            if (otherPartner != null) {
                return otherPartner.getChr().getId() == c1.getId();
            }
        }

        return false;
    }

    public static void inviteTrade(Character c1, Character c2) {

        if ((c1.isGM() && !c2.isGM()) && c1.gmLevel() < YamlConfig.config.server.MINIMUM_GM_LEVEL_TO_TRADE) {
            c1.message("You cannot trade with non-GM characters.");
            log.info(String.format("GM %s blocked from trading with %s due to GM level.", c1.getName(), c2.getName()));
            cancelTrade(c1, TradeResult.NO_RESPONSE);
            return;
        }

        if ((!c1.isGM() && c2.isGM()) && c2.gmLevel() < YamlConfig.config.server.MINIMUM_GM_LEVEL_TO_TRADE) {
            c1.message("You cannot trade with this GM character.");
            cancelTrade(c1, TradeResult.NO_RESPONSE);
            return;
        }

        if (InviteCoordinator.hasInvite(InviteType.TRADE, c1.getId())) {
            if (hasTradeInviteBack(c1, c2)) {
                c1.message("You are already managing this player's trade invitation.");
            } else {
                c1.message("You are already managing someone's trade invitation.");
            }

            return;
        } else if (c1.getTrade().isFullTrade()) {
            c1.message("You are already in a trade.");
            return;
        }

        if (InviteCoordinator.createInvite(InviteType.TRADE, c1, c1.getId(), c2.getId())) {
            if (c2.getTrade() == null) {
                c2.setTrade(new Trade((byte) 1, c2));
                c2.getTrade().setPartner(c1.getTrade());
                c1.getTrade().setPartner(c2.getTrade());

                c1.sendPacket(PacketCreator.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 0));
                c2.sendPacket(PacketCreator.tradeInvite(c1));
            } else {
                c1.message("The other player is already trading with someone else.");
                cancelTrade(c1, TradeResult.NO_RESPONSE);
                InviteCoordinator.answerInvite(InviteType.TRADE, c2.getId(), c1.getId(), false);
            }
        } else {
            c1.message("The other player is already managing someone else's trade invitation.");
            cancelTrade(c1, TradeResult.NO_RESPONSE);
        }
    }

    public static void visitTrade(Character c1, Character c2) {
        InviteResult inviteRes = InviteCoordinator.answerInvite(InviteType.TRADE, c1.getId(), c2.getId(), true);

        InviteResultType res = inviteRes.result;
        if (res == InviteResultType.ACCEPTED) {
            if (c1.getTrade() != null && c1.getTrade().getPartner() == c2.getTrade() && c2.getTrade() != null && c2.getTrade().getPartner() == c1.getTrade()) {
                c2.sendPacket(PacketCreator.getTradePartnerAdd(c1));
                c1.sendPacket(PacketCreator.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 1));
                c1.getTrade().setFullTrade(true);
                c2.getTrade().setFullTrade(true);
            } else {
                c1.message("The other player has already closed the trade.");
            }
        } else {
            c1.message("This trade invitation already rescinded.");
            cancelTrade(c1, TradeResult.NO_RESPONSE);
        }
    }

    public static void declineTrade(Character chr) {
        Trade trade = chr.getTrade();
        if (trade != null) {
            if (trade.getPartner() != null) {
                Character other = trade.getPartner().getChr();
                if (InviteCoordinator.answerInvite(InviteType.TRADE, chr.getId(), other.getId(), false).result == InviteResultType.DENIED) {
                    other.message(chr.getName() + " has declined your trade request.");
                }

                other.getTrade().cancel(TradeResult.PARTNER_CANCEL.getValue());
                other.setTrade(null);

            }
            trade.cancel(TradeResult.NO_RESPONSE.getValue());
            chr.setTrade(null);
        }
    }

    public boolean isFullTrade() {
        return fullTrade;
    }

    public void setFullTrade(boolean fullTrade) {
        this.fullTrade = fullTrade;
    }

    private static void logTrade(Trade trade1, Trade trade2) {
        String name1 = trade1.getChr().getName();
        String name2 = trade2.getChr().getName();
        StringBuilder message = new StringBuilder();
        message.append(String.format("Committing trade between %s and %s%n", name1, name2));
        //Trade 1 to trade 2
        message.append(String.format("Trading %s -> %s: %d mesos, items: %s%n", name1, name2,
                trade1.getExchangeMesos(), getFormattedItemLogMessage(trade1.getItems())));

        //Trade 2 to trade 1
        message.append(String.format("Trading %s -> %s: %d mesos, items: %s%n", name2, name1,
                trade2.getExchangeMesos(), getFormattedItemLogMessage(trade2.getItems())));

        log.info(message.toString());
    }

    private static String getFormattedItemLogMessage(List<Item> items) {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        ItemInformationProvider ii = ItemInformationProvider.getInstance();
        for (Item item : items) {
            String itemName = ii.getName(item.getItemId());
            sj.add(String.format("%dx %s (%d)", item.getQuantity(), itemName, item.getItemId()));
        }
        return sj.toString();
    }
}