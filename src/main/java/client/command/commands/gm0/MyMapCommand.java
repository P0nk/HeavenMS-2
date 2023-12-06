package client.command.commands.gm0;

import client.Character;
import client.Client;
import client.command.Command;
import net.server.Server;
import server.maps.*;
import tools.Randomizer;

public class MyMapCommand extends Command {
    {
        setDescription("check current map id");
    }

    @Override
    public void execute(Client c, String[] params) {
        String[] tips = {
                "checking my map",
        };
        Character player = c.getPlayer();

        player.dropMessage("my map is : " + player.getMapId() + " - " + MapFactory.loadPlaceName(player.getMapId()));
    }
}
