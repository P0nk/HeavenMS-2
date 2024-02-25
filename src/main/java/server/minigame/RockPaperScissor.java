package server.minigame;

import client.Client;
import client.inventory.Item;
import client.inventory.manipulator.InventoryManipulator;
import constants.id.ItemId;
import tools.PacketCreator;
import tools.Randomizer;

/**
 * @Author Arnah
 * @Website http://Vertisy.ca/
 * @since Aug 15, 2016
 */
public class RockPaperScissor {
    private int round = 0;
    private boolean ableAnswer = true;
    private boolean win = false;

    public RockPaperScissor(final Client c, final byte mode) {
        c.sendPacket(PacketCreator.rpsMode((byte) (9 + mode)));
        if (mode == 0) {
            c.getPlayer().gainMeso(-1000, true, true, true);
        }
    }

    public final boolean answer(final Client c, final int answer) {
        if (ableAnswer && !win && answer >= 0 && answer <= 2) {
            final int response = Randomizer.nextInt(3);
            if (response == answer) {
                c.sendPacket(PacketCreator.rpsSelection((byte) response, (byte) round));
                // dont do anything. they can still answer once a draw
            } else if ((answer == 0 && response == 2) || (answer == 1 && response == 0) || (answer == 2 && response == 1)) { // they win
                c.sendPacket(PacketCreator.rpsSelection((byte) response, (byte) (round + 1)));
                ableAnswer = false;
                win = true;
            } else { // they lose
                c.sendPacket(PacketCreator.rpsSelection((byte) response, (byte) -1));
                ableAnswer = false;
            }
            return true;
        }
        reward(c);
        return false;
    }

    public final boolean timeOut(final Client c) {
        if (ableAnswer && !win) {
            ableAnswer = false;
            c.sendPacket(PacketCreator.rpsMode((byte) 0x0A));
            return true;
        }
        reward(c);
        return false;
    }

    public final boolean nextRound(final Client c) {
        if (win) {
            round++;
            if (round < 10) {
                win = false;
                ableAnswer = true;
                c.sendPacket(PacketCreator.rpsMode((byte) 0x0C));
                return true;
            } else {
                round = 10;
            }
        }
        reward(c);
        return false;
    }

    public final void reward(final Client c) {
        if (win) {
            InventoryManipulator.addFromDrop(c, new Item(ItemId.RPS_CERTIFICATE_BASE + round, (short) 0, (short) 1), true);
        }
        c.getPlayer().setRPS(null);
    }

    public final void dispose(final Client c) {
        reward(c);
        c.sendPacket(PacketCreator.rpsMode((byte) 0x0D));
    }
}
