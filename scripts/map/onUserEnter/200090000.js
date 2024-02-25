// Author: Ronan
var mapId = 200090000;

function start(ms) {
    var map = ms.getClient().getChannelServer().getMapFactory().getMap(mapId);

    if (map.getDocked()) {
        const PacketCreator = Java.type('tools.PacketCreator');
        ms.getClient().sendPacket(PacketCreator.musicChange("Bgm04/ArabPirate"));
        ms.getClient().sendPacket(PacketCreator.crogBoatPacket(true));
    }

    return true;
}