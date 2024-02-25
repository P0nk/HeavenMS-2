/* @Author SharpAceX
* 5511001.js: Summons Scarlion.
*/

function act() {
    const scarlionMobId = 9420547;
    if (rm.getReactor().getMap().getMonsterById(scarlionMobId) == null) {
        rm.summonBossDelayed(scarlionMobId, 3200, -238, 636, "Bgm09/TimeAttack", "Beware! The furious Scarlion has shown himself!");
    }
}