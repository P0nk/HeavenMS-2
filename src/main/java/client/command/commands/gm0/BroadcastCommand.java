package client.command.commands.gm0;

import server.maps.MapleTVEffect;
import client.Character;
import client.Client;
import client.command.Command;
import java.util.Arrays;
import java.util.LinkedList;

import net.server.Server;
import java.util.List;
import tools.Pair;
import tools.Randomizer;

/*
    -bobby
    might get an error if less than 2 players on the rankings (edge case)
    so far no errors otherwise

 */
public class BroadcastCommand extends Command {
    {
        setDescription("Go somewhere, be somebody");
    }

    @Override
    public void execute(Client c, String[] params) {
        String[] tips = {
                "Maple TV",
        };
        Character player = c.getPlayer();
        if (params.length < 1) { // #goodbye 'hi'
            player.dropMessage(5, "invalid broadcast");
            return;
        }
        try{
            Character victim = null;
            List<String> messages = new LinkedList<>();

            Server server = Server.getInstance();

            List<Pair<String, Integer>> worldRanking = server.getWorldPlayerRanking(player.getWorld());

            Character firstCharacter = server.getWorld(0).getPlayerStorage().getCharacterById(server.getFirstCharacter().getId());
            if(firstCharacter == null){
                firstCharacter = server.getFirstCharacter();
            }
            //probably a little efficient to pass the whole character
            //im a C++ programmer, id pass by const reference if i knew how
            //a possibly quicker solution would be a server.getSecondCharacterId() function that only returns the Id
            Character secondCharacter = server.getWorld(0).getPlayerStorage().getCharacterById(server.getSecondCharacter().getId());
            if(secondCharacter == null){
                secondCharacter = server.getSecondCharacter();
            }

            for(int i = 0; i < 5; i++){
                if(worldRanking.size() <= i){
                    messages.add("");
                }
                else {
                    messages.add(i + 1 + " - Lv." + worldRanking.get(i).getRight() + " " + worldRanking.get(i).getLeft());
                }
            }

            MapleTVEffect.broadcastMapleTVIfNotActive(firstCharacter, secondCharacter, messages, 0);
        } catch(NumberFormatException e) {
            player.dropMessage(5, "invalid broadcast");
        }
    }
}
