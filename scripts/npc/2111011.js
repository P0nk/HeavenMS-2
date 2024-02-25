// NPC - Wall
// Location: Magatia - Home of the Missing Alchemist
// Used to handle quest 3311 - Clue

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && type > 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }

        if (!cm.isQuestStarted(3311)) {
            cm.dispose();
            return;
        }

        if (status == 0) {
            cm.sendYesNo("Amidst the throng of spider webs, there's a wall behind it that seems to have something written on it. Perhaps you should take a closer look at the wall?");
        }
        else if (status == 1) {
            cm.setQuestProgress(3311, 5);
            cm.sendOk("On a wall full of graffiti, there seems to be a phrase that really stands out above the rest. #bIt's in a form of a pendant...#k What does that mean?");
        }
        else {
            cm.dispose();
        }
    }
}