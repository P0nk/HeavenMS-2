package net.server.channel.handlers;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;
import net.AbstractMaplePacketHandler;
import net.packet.InPacket;
import server.MapleItemInformationProvider;
import server.MapleItemInformationProvider.QuestConsItem;
import server.quest.MapleQuest;
import tools.PacketCreator;

import java.util.Map;

/**
 *
 * @author Xari
 * @author Ronan - added concurrency protection and quest progress limit
 */
public class RaiseIncExpHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(InPacket p, MapleClient c) {
        byte inventorytype = p.readByte();//nItemIT
        short slot = p.readShort();//nSlotPosition
        int itemid = p.readInt();//nItemID
        
        if (c.tryacquireClient()) {
            try {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                QuestConsItem consItem = ii.getQuestConsumablesInfo(itemid);
                if (consItem == null) {
                    return;
                }

                int infoNumber = consItem.questid;
                Map<Integer, Integer> consumables = consItem.items;
                
                MapleCharacter chr = c.getPlayer();
                MapleQuest quest = MapleQuest.getInstanceFromInfoNumber(infoNumber);
                if (!chr.getQuest(quest).getStatus().equals(MapleQuestStatus.Status.STARTED)) {
                    c.sendPacket(PacketCreator.enableActions());
                    return;
                }
                
                int consId;
                MapleInventory inv = chr.getInventory(MapleInventoryType.getByType(inventorytype));
                inv.lockInventory();
                try {
                    consId = inv.getItem(slot).getItemId();
                    if (!consumables.containsKey(consId) || !chr.haveItem(consId)) {
                        return;
                    }

                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.getByType(inventorytype), slot, (short) 1, false, true);
                } finally {
                    inv.unlockInventory();
                }
                
                int questid = quest.getId();
                int nextValue = Math.min(consumables.get(consId) + c.getAbstractPlayerInteraction().getQuestProgressInt(questid, infoNumber), consItem.exp * consItem.grade);
                c.getAbstractPlayerInteraction().setQuestProgress(questid, infoNumber, nextValue);
                
                c.sendPacket(PacketCreator.enableActions());
            } finally {
                c.releaseClient();
            }
        }
    }
}
