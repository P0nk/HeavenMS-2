function start(ms) {
    const Point = Java.type('java.awt.Point');
    var pos = new Point(171, 50);
    var mobId = 9400611;
    var mobName = "Crocell";

    var player = ms.getPlayer();
    var map = player.getMap();

    if (map.getMonsterById(mobId) != null) {
        return;
    }

    const LifeFactory = Java.type('server.life.LifeFactory');
    map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), pos);
    player.message(mobName + " has appeared!");
}