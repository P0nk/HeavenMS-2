/* @Author SharpAceX
* 5511000.js: Summons Targa.
*/

function act() {
    const targaMobId = 9420542;
    if (rm.getReactor().getMap().getMonsterById(targaMobId) == null) {
        rm.summonBossDelayed(targaMobId, 3200, -527, 637, "Bgm09/TimeAttack", "Beware! The furious Targa has shown himself!");
    }
}