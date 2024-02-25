function start(ms) {
    var player = ms.getPlayer();
    var map = player.getMap();

    if (ms.isQuestStarted(21747) && ms.getQuestProgressInt(21747, 9300351) == 0) {
        const LifeFactory = Java.type('server.life.LifeFactory');
        const Point = Java.type('java.awt.Point');
        map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(9300351), new Point(897, 51));
    }
}