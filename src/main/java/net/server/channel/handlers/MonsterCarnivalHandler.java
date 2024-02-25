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

import client.Character;
import client.Client;
import client.Disease;
import net.AbstractPacketHandler;
import net.packet.InPacket;
import net.server.world.Party;
import net.server.world.PartyCharacter;
import server.life.LifeFactory;
import server.life.MobSkillType;
import server.life.Monster;
import server.partyquest.CarnivalFactory;
import server.partyquest.CarnivalFactory.MCSkill;
import server.partyquest.MonsterCarnival;
import tools.PacketCreator;
import tools.Pair;

import java.awt.*;
import java.util.List;


/**
 * @author Drago (Dragohe4rt)
 */

public final class MonsterCarnivalHandler extends AbstractPacketHandler {

    @Override
    public void handlePacket(InPacket p, Client c) {
        if (c.tryacquireClient()) {
            try {
                try {
                    int tab = p.readByte();
                    int num = p.readByte();
                    int neededCP = 0;
                    if (tab == 0) {
                        final List<Pair<Integer, Integer>> mobs = c.getPlayer().getMap().getMobsToSpawn();
                        if (num >= mobs.size() || c.getPlayer().getCP() < mobs.get(num).right) {
                            c.sendPacket(PacketCreator.CPQMessage((byte) 1));
                            c.sendPacket(PacketCreator.enableActions());
                            return;
                        }

                        final Monster mob = LifeFactory.getMonster(mobs.get(num).left);
                        MonsterCarnival mcpq = c.getPlayer().getMonsterCarnival();
                        if (mcpq != null) {
                            if (!mcpq.canSummonR() && c.getPlayer().getTeam() == 0 || !mcpq.canSummonB() && c.getPlayer().getTeam() == 1) {
                                c.sendPacket(PacketCreator.CPQMessage((byte) 2));
                                c.sendPacket(PacketCreator.enableActions());
                                return;
                            }

                            if (c.getPlayer().getTeam() == 0) {
                                mcpq.summonR();
                            } else {
                                mcpq.summonB();
                            }

                            Point spawnPos = c.getPlayer().getMap().getRandomSP(c.getPlayer().getTeam());
                            mob.setPosition(spawnPos);

                            c.getPlayer().getMap().addMonsterSpawn(mob, 1, c.getPlayer().getTeam());
                            c.getPlayer().getMap().addAllMonsterSpawn(mob, 1, c.getPlayer().getTeam());
                            c.sendPacket(PacketCreator.enableActions());
                        }

                        neededCP = mobs.get(num).right;
                    } else if (tab == 1) { //debuffs
                        final List<Integer> skillid = c.getPlayer().getMap().getSkillIds();
                        if (num >= skillid.size()) {
                            c.getPlayer().dropMessage(5, "An unexpected error has occurred.");
                            c.sendPacket(PacketCreator.enableActions());
                            return;
                        }
                        final MCSkill skill = CarnivalFactory.getInstance().getSkill(skillid.get(num)); //ugh wtf
                        if (skill == null || c.getPlayer().getCP() < skill.cpLoss()) {
                            c.sendPacket(PacketCreator.CPQMessage((byte) 1));
                            c.sendPacket(PacketCreator.enableActions());
                            return;
                        }
                        final Disease dis = skill.getDisease();
                        Party enemies = c.getPlayer().getParty().getEnemy();
                        if (skill.targetsAll()) {
                            int hitChance = rollHitChance(dis.getMobSkillType());
                            if (hitChance <= 80) {
                                for (PartyCharacter mpc : enemies.getPartyMembers()) {
                                    Character mc = mpc.getPlayer();
                                    if (mc != null) {
                                        if (dis == null) {
                                            mc.dispel();
                                        } else {
                                            mc.giveDebuff(dis, skill.getSkill());
                                        }
                                    }
                                }
                            }
                        } else {
                            int amount = enemies.getMembers().size() - 1;
                            int randd = (int) Math.floor(Math.random() * amount);
                            Character chrApp = c.getPlayer().getMap().getCharacterById(enemies.getMemberByPos(randd).getId());
                            if (chrApp != null && chrApp.getMap().isCPQMap()) {
                                if (dis == null) {
                                    chrApp.dispel();
                                } else {
                                    chrApp.giveDebuff(dis, skill.getSkill());
                                }
                            }
                        }
                        neededCP = skill.cpLoss();
                        c.sendPacket(PacketCreator.enableActions());
                    } else if (tab == 2) { //protectors
                        final MCSkill skill = CarnivalFactory.getInstance().getGuardian(num);
                        if (skill == null || c.getPlayer().getCP() < skill.cpLoss()) {
                            c.sendPacket(PacketCreator.CPQMessage((byte) 1));
                            c.sendPacket(PacketCreator.enableActions());
                            return;
                        }

                        MonsterCarnival mcpq = c.getPlayer().getMonsterCarnival();
                        if (mcpq != null) {
                            if (!mcpq.canGuardianR() && c.getPlayer().getTeam() == 0 || !mcpq.canGuardianB() && c.getPlayer().getTeam() == 1) {
                                c.sendPacket(PacketCreator.CPQMessage((byte) 2));
                                c.sendPacket(PacketCreator.enableActions());
                                return;
                            }

                            int success = c.getPlayer().getMap().spawnGuardian(c.getPlayer().getTeam(), num);
                            if (success != 1) {
                                switch (success) {
                                    case -1:
                                        c.sendPacket(PacketCreator.CPQMessage((byte) 3));
                                        break;

                                    case 0:
                                        c.sendPacket(PacketCreator.CPQMessage((byte) 4));
                                        break;

                                    default:
                                        c.sendPacket(PacketCreator.CPQMessage((byte) 3));
                                }
                                c.sendPacket(PacketCreator.enableActions());
                                return;
                            } else {
                                neededCP = skill.cpLoss();
                            }
                        }
                    }
                    c.getPlayer().gainCP(-neededCP);
                    c.getPlayer().getMap().broadcastMessage(PacketCreator.playerSummoned(c.getPlayer().getName(), tab, num));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                c.releaseClient();
            }
        }
    }

    private int rollHitChance(MobSkillType type) {
        return switch (type) {
            case DARKNESS, WEAKNESS, POISON, SLOW -> (int) (Math.random() * 100);
            default -> 0;
        };
    }
}
