package server;

import client.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapleLeafLogger {
    private static final Logger log = LoggerFactory.getLogger(MapleLeafLogger.class);

    public static void log(Character player, boolean gotPrize, String operation) {
        String action = gotPrize ? " used a maple leaf to buy " + operation : " redeemed " + operation + " VP for a leaf";
        log.info("{} {}", player.getName(), action);
    }
}
