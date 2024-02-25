function enter(pi) {
    var mapobj = pi.getWarpMap(104000004);
    if (pi.isQuestActive(21733) && pi.getQuestProgressInt(21733, 9300345) == 0 && mapobj.countMonsters() == 0) {
        const LifeFactory = Java.type('server.life.LifeFactory');
        const Point = Java.type('java.awt.Point');
        mapobj.spawnMonsterOnGroundBelow(LifeFactory.getMonster(9300345), new Point(0, 0));
        pi.setQuestProgress(21733, 21762, 2);
    }

    pi.playPortalSound();
    pi.warp(104000004, 1);
    return true;
}