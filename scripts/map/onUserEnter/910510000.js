function start(ms) {
    var player = ms.getPlayer();
    var map = player.getMap();

    const LifeFactory = Java.type('server.life.LifeFactory');
    const Point = Java.type('java.awt.Point');
    if (player.isCygnus()) {
        if (ms.isQuestStarted(20730) && ms.getQuestProgressInt(20730, 9300285) == 0) {
            map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(9300285), new Point(680, 258));
        }
    } else {
        if (ms.isQuestStarted(21731) && ms.getQuestProgressInt(21731, 9300344) == 0) {
            map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(9300344), new Point(680, 258));
        }
    }
}