/*
	NPC: Corner of the Magic Library
	MAP: Hidden Street - Magic Library (910110000)
	QUEST: Maybe it's Grendel! (20718)
*/

var status;
var mobId = 2220100; //Blue Mushroom

function start() {
    if (!cm.isQuestStarted(20718)) {    // thanks Stray, Ari
        cm.dispose();
        return;
    }

    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || (mode == 0 && status == 0)) {
        cm.dispose();
        return;
    } else if (mode == 0) {
        status--;
    } else {
        status++;
    }


    if (status == 0) {
        cm.sendOk("A mysterious black figure appeared and summoned a lot of angry monsters!");
    } else if (status == 1) {
        var player = cm.getPlayer();
        var map = player.getMap();

        const LifeFactory = Java.type('server.life.LifeFactory');
        const Point = Java.type('java.awt.Point');
        for (var i = 0; i < 10; i++) {
            map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), new Point(117, 183));
        }
        for (var i = 0; i < 10; i++) {
            map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), new Point(4, 183));
        }
        for (var i = 0; i < 10; i++) {
            map.spawnMonsterOnGroundBelow(LifeFactory.getMonster(mobId), new Point(-109, 183));
        }

        cm.completeQuest(20718, 1103003);
        cm.gainExp(4000 * cm.getPlayer().getExpRate());

        cm.dispose();

    }
}