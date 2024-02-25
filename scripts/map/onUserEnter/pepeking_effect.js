function start(ms) {
    var mobId = 3300000 + (Math.floor(Math.random() * 3) + 5);
    var player = ms.getPlayer();
    var map = player.getMap();

    const LifeFactory = Java.type('server.life.LifeFactory');
    const Point = Java.type('java.awt.Point');
    map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), new Point(-28, -67));
}