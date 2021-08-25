/*
    This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
               Matthias Butz <matze@odinms.de>
               Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.server.channel.handlers;

import client.MapleCharacter;
import client.MapleClient;
import config.YamlConfig;
import net.packet.InPacket;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.PacketCreator;
import tools.Pair;
import tools.Randomizer;
import tools.exceptions.EmptyMovementException;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Danny (Leifde)
 * @author ExtremeDevilz
 * @author Ronan (HeavenMS)
 */
public final class MoveLifeHandler extends AbstractMovementPacketHandler {
        
	@Override
	public final void handlePacket(InPacket p, MapleClient c) {
                MapleCharacter player = c.getPlayer();
                MapleMap map = player.getMap();
                
                if (player.isChangingMaps()) {  // thanks Lame for noticing mob movement shuffle (mob OID on different maps) happening on map transitions
                    return;
                }
                
		int objectid = p.readInt();
		short moveid = p.readShort();
		MapleMapObject mmo = map.getMapObject(objectid);
		if (mmo == null || mmo.getType() != MapleMapObjectType.MONSTER) {
			return;
		}
                
		MapleMonster monster = (MapleMonster) mmo;
                List<MapleCharacter> banishPlayers = null;
                
                byte pNibbles = p.readByte();
		byte rawActivity = p.readByte();
		int skillId = p.readByte() & 0xff;
		int skillLv = p.readByte() & 0xff;
		short pOption = p.readShort();
                p.skip(8);
                
                if (rawActivity >= 0) {
			rawActivity = (byte) (rawActivity & 0xFF >> 1);
		}

		boolean isAttack = inRangeInclusive(rawActivity, 24, 41);
		boolean isSkill = inRangeInclusive(rawActivity, 42, 59);
                
		MobSkill toUse = null;
                int useSkillId = 0, useSkillLevel = 0;
                
                MobSkill nextUse = null;
                int nextSkillId = 0, nextSkillLevel = 0;
                
                boolean nextMovementCouldBeSkill = !(isSkill || (pNibbles != 0));
                
                int castPos;
                if (isSkill) {
                        useSkillId = skillId;
                        useSkillLevel = skillLv;
                        
                        castPos = monster.getSkillPos(useSkillId, useSkillLevel);
                        if (castPos != -1) {
                                toUse = MobSkillFactory.getMobSkill(useSkillId, useSkillLevel);
                                
                                if (monster.canUseSkill(toUse, true)) {
                                        int animationTime = MapleMonsterInformationProvider.getInstance().getMobSkillAnimationTime(toUse);
                                        if(animationTime > 0 && toUse.getSkillId() != 129) {
                                                toUse.applyDelayedEffect(player, monster, true, animationTime);
                                        } else {
                                                banishPlayers = new LinkedList<>();
                                                toUse.applyEffect(player, monster, true, banishPlayers);
                                        }
                                }
                        }
                } else {
                        castPos = (rawActivity - 24) / 2;
                        
                        int atkStatus = monster.canUseAttack(castPos, isSkill);
                        if (atkStatus < 1) {
                                rawActivity = -1;
                                pOption = 0;
                        }
                }
                
                int mobMp = monster.getMp();
                if (nextMovementCouldBeSkill) {
                        int noSkills = monster.getNoSkills();
                        if (noSkills > 0) {
                                int rndSkill = Randomizer.nextInt(noSkills);
                                
                                Pair<Integer, Integer> skillToUse = monster.getSkills().get(rndSkill);
                                nextSkillId = skillToUse.getLeft();
                                nextSkillLevel = skillToUse.getRight();
                                nextUse = MobSkillFactory.getMobSkill(nextSkillId, nextSkillLevel);
                                
                                if (!(nextUse != null && monster.canUseSkill(nextUse, false) && nextUse.getHP() >= (int) (((float) monster.getHp() / monster.getMaxHp()) * 100) && mobMp >= nextUse.getMpCon())) {
                                        // thanks OishiiKawaiiDesu for noticing mobs trying to cast skills they are not supposed to be able
                                        
                                        nextSkillId = 0;
                                        nextSkillLevel = 0;
                                        nextUse = null;
                                }
                        }
                }
                
		p.readByte();
		p.readInt(); // whatever
		short start_x = p.readShort(); // hmm.. startpos?
		short start_y = p.readShort(); // hmm...
		Point startPos = new Point(start_x, start_y - 2);
		Point serverStartPos = new Point(monster.getPosition());
                
                Boolean aggro = monster.aggroMoveLifeUpdate(player);
                if (aggro == null) return;

                if (nextUse != null) {
                        c.sendPacket(PacketCreator.moveMonsterResponse(objectid, moveid, mobMp, aggro, nextSkillId, nextSkillLevel));
		} else {
			c.sendPacket(PacketCreator.moveMonsterResponse(objectid, moveid, mobMp, aggro));
		}
                
                
                try {
                        int movementDataStart = p.getPosition();
                        updatePosition(p, monster, -2);  // Thanks Doodle & ZERO傑洛 for noticing sponge-based bosses moving out of stage in case of no-offset applied
                        long movementDataLength = p.getPosition() - movementDataStart; //how many bytes were read by updatePosition
                        p.seek(movementDataStart);
                        
                        if (YamlConfig.config.server.USE_DEBUG_SHOW_RCVD_MVLIFE) {
                                System.out.println((isSkill ? "SKILL " : (isAttack ? "ATTCK " : " ")) + "castPos: " + castPos + " rawAct: " + rawActivity + " opt: " + pOption + " skillID: " + useSkillId + " skillLV: " + useSkillLevel + " " + "allowSkill: " + nextMovementCouldBeSkill + " mobMp: " + mobMp);
                        }
                        
                        map.broadcastMessage(player, PacketCreator.moveMonster(objectid, nextMovementCouldBeSkill, rawActivity, useSkillId, useSkillLevel, pOption, startPos, p, movementDataLength), serverStartPos);
                        //updatePosition(res, monster, -2); //does this need to be done after the packet is broadcast?
                        map.moveMonster(monster, monster.getPosition());
                } catch (EmptyMovementException e) {}
                
                if (banishPlayers != null) {
                        for (MapleCharacter chr : banishPlayers) {
                               chr.changeMapBanish(monster.getBanish().getMap(), monster.getBanish().getPortal(), monster.getBanish().getMsg());
                        }
                }
	}

	private static boolean inRangeInclusive(Byte pVal, Integer pMin, Integer pMax) {
		return !(pVal < pMin) || (pVal > pMax);
	}
}