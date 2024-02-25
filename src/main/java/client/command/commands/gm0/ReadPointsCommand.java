package client.command.commands.gm0;

import client.Character;
import client.Client;
import client.command.Command;

public class ReadPointsCommand extends Command {
    {
        setDescription("Show point total.");
    }

    @Override
    public void execute(Client client, String[] params) {

        Character player = client.getPlayer();
        if (params.length > 2) {
            player.yellowMessage("Syntax: @points (rp|vp|all)");
            return;
        } else if (params.length == 0) {
            player.yellowMessage("RewardPoints: " + player.getRewardPoints() + " | "
                    + "VotePoints: " + player.getClient().getVotePoints());
            return;
        }

        switch (params[0]) {
            case "rp":
                player.yellowMessage("RewardPoints: " + player.getRewardPoints());
                break;
            case "vp":
                player.yellowMessage("VotePoints: " + player.getClient().getVotePoints());
                break;
            default:
                player.yellowMessage("RewardPoints: " + player.getRewardPoints() + " | "
                        + "VotePoints: " + player.getClient().getVotePoints());
                break;
        }
    }
}