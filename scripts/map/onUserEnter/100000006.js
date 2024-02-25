function start(ms) {

    if (ms.getQuestStatus(2175) == 1) {
        var mobId = 9300156;
        var player = ms.getPlayer();
        var map = player.getMap();

        if (map.getMonsterById(mobId) != null) {
            return;
        }

        const LifeFactory = Java.type('server.life.LifeFactory');
        const Point = Java.type('java.awt.Point');
        map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), new Point(-1027, 216));
    }
}