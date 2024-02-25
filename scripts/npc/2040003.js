var status = 0;
var em;
var eim;

function sendBaseText() {
    cm.sendOk("Access is restricted to the public.");
    cm.dispose();
}

function start() {
    em = cm.getEventManager("q3239");
    if (em != null)
        eim = cm.getEventInstance();

    if (em == null) { // No event handler
        sendBaseText();
        return;
    }
    else if (eim == null && !cm.isQuestStarted(3239)) { // Not in instance, quest is not in progress
        sendBaseText();
        return;
    }

    if (eim == null) { // Not in instance
        cm.sendYesNo("Are you ready to enter #b#m922000000##k?");
    }
    else { // Inside the instance
        cm.sendYesNo("Are you ready to leave this place?");
    }
}

function action(mode, type, selection) {
    if (mode < 1) {
        cm.dispose();
        return;
    }

    if (eim == null) { // Not in instance, ready to enter
        cm.removeAll(4031092); // This handling is done in the portal script and in the event end, just for legacy purposes here
        if (!em.startInstance(cm.getPlayer())) {
            cm.sendOk("Someone else is already gathering some parts for me. Please wait until the area is cleared.");
        }
    }
    else { // Inside the instance, ready to exit
        eim.removePlayer(cm.getPlayer()); // This will end the event and warp the player out
    }
    cm.dispose();
}