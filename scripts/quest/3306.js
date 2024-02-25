/*
	QUEST: Re-acquiring Alcadno Cape
	NPC: Maed
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 0 && type > 0) {
            qm.dispose();
            return;
        }

        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        if (status == 0) {
            qm.sendNext("So you have lost the #bAlcadno cape#k. I can make another one for you but I'll need some materials");
        } else if (status == 1) {
            qm.sendAcceptDecline("To make the new cape, I need you to bring me #b5 #t4021006##k, #b10 #t4000021##k and #b10000 mesos#k");
        } else if (status == 2) {
            qm.sendOk("Come back to me when you have all of the items");
            qm.forceStartQuest();
        } else if (status == 3) {
            qm.dispose();
        }
    }
}
