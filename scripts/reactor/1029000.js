function act() {
    if (rm.isAllReactorState(1029000, 0x04)) { // 0x04 appears to be the destroyed state
        rm.killMonster(3230300);
        rm.killMonster(3230301);
        rm.playerMessage(6, "Once the rock crumbled, Jr. Boogie was in great pain and disappeared.");
    }    
}