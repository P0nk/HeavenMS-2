package client.command.commands.gm3;

import client.Character;
import client.Client;
import client.command.Command;

public class GiveRpCommand extends Command {
    {
        setDescription("Give reward points to a player.");
    }

    @Override
    public void execute(Client client, String[] params) {
        Character player = client.getPlayer();
        if (params.length < 2) {
            player.yellowMessage("Syntax: !giverp <playername> <gainrewardpoint>");
            return;
        }

        Character victim = client.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
        if (victim != null) {
            victim.setRewardPoints(victim.getRewardPoints() + Integer.parseInt(params[1]));
            player.message("RP given. Player " + params[0] + " now has " + victim.getRewardPoints()
                    + " reward points.");
        } else {
            player.message("Player '" + params[0] + "' could not be found.");
        }
    }
}
