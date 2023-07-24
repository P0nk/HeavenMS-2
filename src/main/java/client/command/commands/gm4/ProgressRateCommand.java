package client.command.commands.gm4;

import client.Character;
import client.Client;
import client.command.Command;
import tools.PacketCreator;

public class ProgressRateCommand extends Command {
    {
        setDescription("Set world quest progress rate.");
    }

    @Override
    public void execute(Client c, String[] params) {
        Character player = c.getPlayer();
        if (params.length < 1) {
            player.yellowMessage("Syntax: !progressrate <newrate>");
            return;
        }

        int rate = Math.max(Integer.parseInt(params[0]), 1);
        c.getWorldServer().setProgressRate(rate);
        c.getWorldServer().broadcastPacket(PacketCreator.serverNotice(6, "[Rate] Quest progress rate has been changed to " + rate + "x."));
    }
}