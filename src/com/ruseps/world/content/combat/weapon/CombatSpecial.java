package com.ruseps.world.content.combat.weapon;

import java.util.Arrays;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PlayerSpecialAmountTask;
import com.ruseps.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.Projectile;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.WeaponInterfaces.WeaponInterface;
import com.ruseps.util.Misc;
import com.ruseps.world.content.Consumables;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.minigames.impl.Dueling.DuelRule;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;

/**
 * Holds constants that hold data for all of the special attacks that can be
 * used.
 *
 * @author lare96
 */
public enum CombatSpecial {

    DRAGON_DAGGER(new int[]{1215, 1231, 5680, 5698}, 25, 1.22, 1.35, CombatType.MELEE, WeaponInterface.DAGGER) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1062));
                    player.performGraphic(new Graphic(252, GraphicHeight.HIGH));

                    return new CombatContainer(player, target, 2, CombatType.MELEE,
                            true);
                }
            },
    ABYSSAL_DAGGER(new int[]{13047}, 25, 1.25, 1.25, CombatType.MELEE, WeaponInterface.DAGGER) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1062));
                    player.performGraphic(new Graphic(252, GraphicHeight.HIGH));

                    return new CombatContainer(player, target, 2, CombatType.MELEE,
                            true);
                }
            },
    infernal_mg(new int[]{20074}, 25, 1.25, 1.25, CombatType.RANGED, WeaponInterface.LONGBOW) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    return new CombatContainer(player, target, 4, CombatType.MELEE,
                            true);
                }
            },
    ABYSSAL_BLUDGEON(new int[] { 13045 }, 50, 1.25, 1.30, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        		@Override
        		public CombatContainer container(Player player, Character target) {
        			player.performAnimation(new Animation(2661));
        			target.performGraphic(new Graphic(2233, GraphicHeight.MIDDLE));
        			player.poisonVictim(target, CombatType.MELEE);
        			return new CombatContainer(player, target, 1, CombatType.MELEE,
        					true);
        		}
        	},
    KORASIS_SWORD(new int[]{19780}, 60, 1.58, 10, CombatType.MELEE, WeaponInterface.SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(14788));
                    player.performGraphic(new Graphic(1729));

                    return new CombatContainer(player, target, 1, 1, CombatType.MAGIC, true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            target.performGraphic(new Graphic(1730));
                        }
                    };
                }
            },
    MORRIGANS_JAVELIN(new int[]{13879}, 50, 1.50, 1.30, CombatType.RANGED, WeaponInterface.JAVELIN) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(10501));
                    player.performGraphic(new Graphic(1836));

                    return new CombatContainer(player, target, 1, CombatType.RANGED, true);
                }
            },
    MORRIGANS_THROWNAXE(new int[]{13883}, 50, 1.48, 1.30, CombatType.RANGED, WeaponInterface.THROWNAXE) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(10504));
                    player.performGraphic(new Graphic(1838));

                    return new CombatContainer(player, target, 1, CombatType.RANGED, true);
                }
            },
    GRANITE_MAUL(new int[]{4153, 20084}, 50, 1.35, 1.15, CombatType.MELEE, WeaponInterface.WARHAMMER) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1667));
                    player.performGraphic(new Graphic(337, GraphicHeight.HIGH));
                    player.getCombatBuilder().setAttackTimer(1);
                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true);
                }
            },
    ABYSSAL_WHIP(new int[]{4151, 15441, 15442, 15443, 15444, 20061}, 50, 1, 1, CombatType.MELEE, WeaponInterface.WHIP) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1658));
                    target.performGraphic(new Graphic(341, GraphicHeight.HIGH));
                    if (target.isPlayer()) {
                        Player p = (Player) target;
                        int totalRunEnergy = p.getRunEnergy() - 25;
                        if (totalRunEnergy < 0) {
                            totalRunEnergy = 0;
                        }
                        p.setRunEnergy(totalRunEnergy);
                    }
                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            false);
                }
            },
    ABYSSAL_TENT(new int[]{11554}, 50, 1.3, 2, CombatType.MELEE, WeaponInterface.WHIP) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1658));
                    target.performGraphic(new Graphic(341, GraphicHeight.HIGH));
                    
                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            false);
                }
            },
			  VINE_WHIP(new int[]{4705}, 60, 1.4, 2.4, CombatType.MELEE, WeaponInterface.WHIP) {
                @Override
                public CombatContainer container(Player player, Character target) {
                	int amountToDrain = Misc.getRandom(500);
                    player.performAnimation(new Animation(1658));
                    target.performGraphic(new Graphic(436, GraphicHeight.HIGH));
                    if (target.isPlayer()) {
                        Player p = (Player) target;
                        p.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
                        p.getPacketSender().sendMessage("Your prayer has been drained " +amountToDrain+" prayer points from the vine whip spec!");

                    }
                    player.getPacketSender().sendMessage("You drain your opponents prayer by a random amount of " +amountToDrain+" prayer points");

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            false);
                }
            },
			  ZARYTE(new int[]{4706}, 50, 1.30, 2.90, CombatType.RANGED, WeaponInterface.SHORTBOW) {
                @Override
                public CombatContainer container(Player player, Character target) {

                	player.performAnimation(new Animation(426));
                    player.setEntityInteraction(target);
                    TaskManager.submit(new Task(1, player, false) {
                        int tick = 0;

                        @Override
                        public void execute() {
                            if (tick == 0) {

                               // new Projectile(player, target, 311, 44, 3, 32, 31, 0).sendProjectile();
                                new Projectile(player, target, 311, 60, 3, 32, 31, 0).sendProjectile();
                            } else if (tick >= 1) {
                                target.performGraphic(new Graphic(2345, GraphicHeight.LOW));
                                  this.stop();
                            }
                            tick++;
                        }
                    });

                    return new CombatContainer(player, target, 2, CombatType.RANGED, true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                        	if(target instanceof Player) {
                        		int prayerDrain = (int) (damage * 0.40);
                           	 Player p = (Player) target;
                                p.getSkillManager().setCurrentLevel(Skill.PRAYER, p.getSkillManager().getCurrentLevel(Skill.PRAYER) - prayerDrain);
                        	} else if(target instanceof NPC) {
                        		
                        	}
                        	
                       
                        }
                    };
                }
            },
    DRAGON_LONGSWORD(new int[]{20134}, 25, 1.15, 1.20, CombatType.MELEE, WeaponInterface.LONGSWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1058));
                    player.performGraphic(new Graphic(248, GraphicHeight.HIGH));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true);
                }
            },
    BARRELSCHEST_ANCHOR(new int[]{10887}, 50, 1.40, 1.35, CombatType.MELEE, WeaponInterface.WARHAMMER) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(5870));
                    player.performGraphic(new Graphic(1027, GraphicHeight.MIDDLE));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true);
                }
            },
    SARADOMIN_SWORD(new int[]{11730}, 100, 1.35, 1.2, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(11993));
                    player.setEntityInteraction(target);

                    return new CombatContainer(player, target, 2, CombatType.MAGIC, true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            target.performGraphic(new Graphic(1194));
                        }
                    };
                }
            },
    VESTAS_LONGSWORD(new int[]{13899}, 25, 1.45, 2.25, CombatType.MELEE, WeaponInterface.LONGSWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(10502));

                    return new CombatContainer(player, target, 1, CombatType.MELEE, true);
                }
            },
    DRAGON_WARHAMMER(new int[] { 20555 }, 30, 1.40, 1.9, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        		@Override
        		public CombatContainer container(Player player, Character target) {
        			player.performAnimation(new Animation(10505));
        			//player.performGraphic(new Graphic(1840));
        			return new CombatContainer(player, target, 1, CombatType.MELEE, true);
        		}
        	},
    TOXIC_BLOWPIPE(new int[] { 12926 }, 50, 1.35, 2.20, CombatType.RANGED, WeaponInterface.DART) {
        		@Override
        		public CombatContainer container(Player player, Character target) {
        			player.performAnimation(new Animation(5061));
        			player.performGraphic(new Graphic(1123));
        			return new CombatContainer(player, target, 2, CombatType.RANGED, true);
        		}
        	},
    STATIUS_WARHAMMER(new int[]{13902}, 35, 1.3, 1.45, CombatType.MELEE, WeaponInterface.WARHAMMER) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(10505));
                    player.performGraphic(new Graphic(1840));
                    return new CombatContainer(player, target, 1, CombatType.MELEE, true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            if (target.isPlayer() && accurate) {
                                Player t = (Player) target;
                                int currentDef = t.getSkillManager().getCurrentLevel(Skill.DEFENCE);
                                int defDecrease = (int) (currentDef * 0.11);
                                if ((currentDef - defDecrease) <= 0 || currentDef <= 0) {
                                    return;
                                }
                                t.getSkillManager().setCurrentLevel(Skill.DEFENCE, defDecrease);
                                t.getPacketSender().sendMessage("Your opponent has reduced your Defence level.");
                                player.getPacketSender().sendMessage("Your hammer forces some of your opponent's defences to break.");
                            }
                        }
                    };
                }
            },
    MAGIC_SHORTBOW(new int[]{861}, 55, 1.35, 1.35, CombatType.RANGED, WeaponInterface.SHORTBOW) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(1074));
                    player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
                    new Projectile(player, target, 249, 44, 3, 43, 31, 0).sendProjectile();

                    TaskManager.submit(new Task(1, player, false) {
                        @Override
                        public void execute() {

                            new Projectile(player, target, 249, 44, 3, 43, 31, 0).sendProjectile();
                            this.stop();
                        }
                    });

                    return new CombatContainer(player, target, 2, CombatType.RANGED,
                            true);
                }
            },
    MAGIC_LONGBOW(new int[]{859}, 35, 1.20, 5, CombatType.RANGED, WeaponInterface.LONGBOW) {
                @Override
                public CombatContainer container(Player player, Character target) {

                    player.performAnimation(new Animation(426));
                    player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
                    new Projectile(player, target, 249, 44, 3, 43, 31, 0).sendProjectile();

                    return new CombatContainer(player, target, 1, CombatType.RANGED,
                            true);
                }
            },
    DARK_BOW(new int[]{11235}, 55, 1.45, 1.65, CombatType.RANGED, WeaponInterface.LONGBOW) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(426));

                    TaskManager.submit(new Task(1, player, false) {
                        int tick = 0;

                        @Override
                        public void execute() {
                            if (tick == 0) {
                                new Projectile(player, target, 1099, 44, 3, 43, 31, 0).sendProjectile();
                                new Projectile(player, target, 1099, 60, 3, 43, 31, 0).sendProjectile();
                            } else if (tick >= 1) {
                                target.performGraphic(new Graphic(1100, GraphicHeight.HIGH));
                                this.stop();
                            }
                            tick++;
                        }
                    });

                    return new CombatContainer(player, target, 2, CombatType.RANGED,
                            true);
                }
            },
	HAND_CANNON(new int[] { 15241 }, 45, 1.30, 2, CombatType.RANGED, WeaponInterface.SHORTBOW) {
        		
        		@Override
        		public CombatContainer container(Player player, Character target) {	
        			player.performAnimation(new Animation(12175));
        			player.getCombatBuilder().setAttackTimer(8);
        			
        			TaskManager.submit(new Task(1, player, false) {
        				@Override
        				public void execute() {
        					player.performGraphic(new Graphic(2141));
        					new Projectile(player, target, 2143, 44, 3, 43, 31, 0).sendProjectile();
        					new CombatHitTask(player.getCombatBuilder(), new CombatContainer(player, target, 2, CombatType.RANGED, true)).handleAttack();
        					player.getCombatBuilder().setAttackTimer(3);
        					stop();
        				}
        			});
        			return new CombatContainer(player, target, 1, 1, CombatType.RANGED,
        					true);
        		}
        	},
    DRAGON_BATTLEAXE(new int[]{1377}, 100, 1, 1, CombatType.MELEE, WeaponInterface.BATTLEAXE) {
                @Override
                public void onActivation(Player player, Character target) {
                    player.performGraphic(new Graphic(246, GraphicHeight.LOW));
                    player.performAnimation(new Animation(1056));
                    player.forceChat("Raarrrrrgggggghhhhhhh!");
                    CombatSpecial.drain(player, DRAGON_BATTLEAXE.drainAmount);
                    Consumables.drinkStatPotion(player, -1, -1, -1, Skill.STRENGTH.ordinal(), true);
                    player.getSkillManager().setCurrentLevel(Skill.ATTACK, player.getSkillManager().getCurrentLevel(Skill.ATTACK) - 7);
                    player.getCombatBuilder().cooldown(true);
                }

                @Override
                public CombatContainer container(Player player, Character target) {
                    throw new UnsupportedOperationException(
                            "Dragon battleaxe does not have a special attack!");
                }
            },
    STAFF_OF_LIGHT(new int[]{14004, 14005, 14006, 14007, 15486}, 100, 1, 1, CombatType.MELEE, WeaponInterface.LONGSWORD) {
                @Override
                public void onActivation(Player player, Character target) {
                    player.performGraphic(new Graphic(1958));
                    player.performAnimation(new Animation(10516));
                    CombatSpecial.drain(player, STAFF_OF_LIGHT.drainAmount);
                    player.setStaffOfLightEffect(200);
                    TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
                    player.getPacketSender().sendMessage("You are shielded by the spirits of the Staff of light!");
                    player.getCombatBuilder().cooldown(true);
                }

                @Override
                public CombatContainer container(Player player, Character target) {
                    throw new UnsupportedOperationException(
                            "Dragon battleaxe does not have a special attack!");
                }
            },
    DRAGON_SPEAR(new int[]{1249, 1263, 5716, 5730}, 25, 1, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
                @Override
                public CombatContainer container(Player player, Character target) {
              //      target.getMovementQueue().freeze(0);

                    player.performAnimation(new Animation(1064));
                    player.performGraphic(new Graphic(253));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {

                            if (target.isPlayer()) {
                                int moveX = target.getPosition().getX() - player.getPosition().getX();
                                int moveY = target.getPosition().getY() - player.getPosition().getY();
                                if (moveX > 0) {
                                    moveX = 1;
                                } else if (moveX < 0) {
                                    moveX = -1;
                                }
                                if (moveY > 0) {
                                    moveY = 1;
                                } else if (moveY < 0) {
                                    moveY = -1;
                                }
                                if (target.getMovementQueue().canWalk(moveX, moveY)) {
                                    target.setEntityInteraction(player);
                                    target.getMovementQueue().reset();
                                    target.getMovementQueue().walkStep(moveX, moveY);
                                }
                            }
                            target.performGraphic(new Graphic(254, GraphicHeight.HIGH));
                            TaskManager.submit(new Task(1, false) {
                                @Override
                                public void execute() {
                                    target.getMovementQueue().freeze(6);
                                    stop();
                                }
                            });
                        }
                    };
                }
            },
    DRAGON_MACE(new int[]{19020}, 25, 1.45, 1.25, CombatType.MELEE, WeaponInterface.MACE) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1060));
                    player.performGraphic(new Graphic(251, GraphicHeight.HIGH));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true);
                }
            },
    DRAGON_SCIMITAR(new int[]{4587}, 55, 1.1, 1.1, CombatType.MELEE, WeaponInterface.SCIMITAR) {
                @Override
                public CombatContainer container(Player player, Character target) {
                	
                    player.performAnimation(new Animation(1872));
                    player.performGraphic(new Graphic(347, GraphicHeight.HIGH));
                    
                    if(target instanceof Player) {
                    	
                    	Player victim = (Player) target;
                    
	                    if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MELEE) || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 2716 || CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MAGIC) || CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MISSILES) || CurseHandler.isActivated(victim, CurseHandler.DEFLECT_SUMMONING)) {
	                    	CurseHandler.deactivateCurse(victim, CurseHandler.DEFLECT_MAGIC);
	                    	CurseHandler.deactivateCurse(victim, CurseHandler.DEFLECT_MELEE);
	                    	CurseHandler.deactivateCurse(victim, CurseHandler.DEFLECT_SUMMONING);
	                    	CurseHandler.deactivateCurse(victim, CurseHandler.DEFLECT_MISSILES);
	                    	victim.getPacketSender().sendMessage("You have been injured!");
	                    	victim.setDragonScimInjury(System.currentTimeMillis());
	                    }
	                    
	                    if (PrayerHandler.isActivated(victim, PrayerHandler.PROTECT_FROM_MAGIC) || PrayerHandler.isActivated(victim, PrayerHandler.PROTECT_FROM_MELEE) || PrayerHandler.isActivated(victim, PrayerHandler.PROTECT_FROM_MISSILES)) {
	                    	PrayerHandler.deactivatePrayer(victim, PrayerHandler.PROTECT_FROM_MAGIC);
	                    	PrayerHandler.deactivatePrayer(victim, PrayerHandler.PROTECT_FROM_MELEE);
	                    	PrayerHandler.deactivatePrayer(victim, PrayerHandler.PROTECT_FROM_MISSILES);
	                    	victim.getPacketSender().sendMessage("You have been injured!");
	                    	victim.setDragonScimInjury(System.currentTimeMillis());
	                    }
                    
                    }
                    
                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            true);
                }
            },
    DRAGON_2H_SWORD(new int[]{7158}, 60, 1, 1, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(3157));
                    player.performGraphic(new Graphic(559));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            false) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            /*if (Location.inMulti(player)) {
                             List<GameCharacter> localEntities;

                             if (target.isPlayer()) {
                             localEntities = Optional.of(player.getLocalPlayers());
                             } else if (target.isNpc()) {
                             localEntities = Optional.of(player.getLocalNpcs());
                             }

                             for (GameCharacter e : localEntities.get()) {
                             if (e == null) {
                             continue;
                             }

                             if (e.getPosition().isWithinDistance(
                             target.getPosition(), 1) && !e.equals(target) && !e.equals(player) && e.getConstitution() > 0 && !e.isDead()) {
                             Hit hit = CombatFactory.getHit(player, target,
                             CombatType.MELEE);
                             e.dealDamage(hit);
                             e.getCombatBuilder().addDamage(player,
                             hit.getDamage());
                             }
                             }
                             }*/
                        }
                    };
                }
            },
    DRAGON_HALBERD(new int[]{3204}, 30, 1.07, 1.08, CombatType.MELEE, WeaponInterface.HALBERD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(1203));
                    player.performGraphic(new Graphic(282, GraphicHeight.HIGH));

                    return new CombatContainer(player, target, 2, CombatType.MELEE,
                            true);
                }
            },
    ARMADYL_GODSWORD(new int[]{11694}, 50, 1.55, 2.9, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(11989));
                    player.performGraphic(new Graphic(2113));

                    return new CombatContainer(player, target, 1, CombatType.MELEE, true);
                }
            },
    REBEL_GODSWORD(new int[]{7084}, 50, 1.55, 2.9, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(11989));
                    player.performGraphic(new Graphic(2113));

                    return new CombatContainer(player, target, 1, CombatType.MELEE, true);
                }
            },
    ZAMORAK_GODSWORD(new int[]{11700}, 50, 1.25, 2.6, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(7070));

                    return new CombatContainer(player, target, 1, CombatType.MELEE, true) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            if (accurate) {
                                target.performGraphic(new Graphic(1221));
                                if (!target.isFrozen()) {
                                    if (target.getSize() == 1) {
                                        target.getMovementQueue().freeze(15);
                                    }
                                }
                            }
                        }
                    };
                }
            },
    BANDOS_GODSWORD(new int[]{11696}, 100, 1.25, 2.4, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(11991));
                    player.performGraphic(new Graphic(2114));

                    return new CombatContainer(player, target, 1, CombatType.MELEE,
                            false) {
                        @Override
                        public void onHit(int damage, boolean accurate) {
                            if (target != null && target.isPlayer() && accurate) {
                                int skillDrain = 1;
                                int damageDrain = (int) (damage * 0.1);
                                if (damageDrain < 0) {
                                    return;
                                }
                                ((Player) target).getSkillManager().setCurrentLevel(Skill.forId(skillDrain), player.getSkillManager().getCurrentLevel(Skill.forId(skillDrain)) - damageDrain);
                                if (((Player) target).getSkillManager().getCurrentLevel(Skill.forId(skillDrain)) < 1) {
                                    ((Player) target).getSkillManager().setCurrentLevel(Skill.forId(skillDrain), 1);
                                }
                                player.getPacketSender().sendMessage("You've drained " + ((Player) target).getUsername() + "'s " + Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase()) + " level by " + damageDrain + ".");
                                ((Player) target).getPacketSender().sendMessage("Your " + Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase()) + " level has been drained.");
                            }
                        }
                    };
                }
            },
    SARADOMIN_GODSWORD(new int[]{11698}, 50, 1.25, 2.7, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(7071));
                    player.performGraphic(new Graphic(1220));

                    return new CombatContainer(player, target, 1, CombatType.MELEE, false) {
                        @Override
                        public void onHit(int dmg, boolean accurate) {
                            if (accurate) {
                                int damageHeal = (int) (dmg * 0.5);
                                int damagePrayerHeal = (int) (dmg * 0.25);
                                if (player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) < player.getSkillManager().getMaxLevel(Skill.CONSTITUTION)) {
                                    int level = player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + damageHeal > player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) ? player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) : player.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + damageHeal;
                                    player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, level);
                                }
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
                                    int level = player.getSkillManager().getCurrentLevel(Skill.PRAYER) + damagePrayerHeal > player.getSkillManager().getMaxLevel(Skill.PRAYER) ? player.getSkillManager().getMaxLevel(Skill.PRAYER) : player.getSkillManager().getCurrentLevel(Skill.PRAYER) + damagePrayerHeal;
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER, level);
                                }
                            }
                        }
                    };
                }
            },
    DRAGON_CLAWS(new int[]{14484}, 50, 2, 2.5, CombatType.MELEE, WeaponInterface.CLAWS) {
                @Override
                public CombatContainer container(Player player, Character target) {
                    player.performAnimation(new Animation(10961));
                    player.performGraphic(new Graphic(1950));

                    return new CombatContainer(player, target, 4, CombatType.MELEE, true);
                }
            };

    /**
     * The weapon ID's that perform this special when activated.
     */
    private int[] identifiers;

    /**
     * The amount of special energy this attack will drain.
     */
    private int drainAmount;

    /**
     * The strength bonus when performing this special attack.
     */
    private double strengthBonus;

    /**
     * The accuracy bonus when performing this special attack.
     */
    private double accuracyBonus;

    /**
     * The combat type used when performing this special attack.
     */
    private CombatType combatType;

    /**
     * The weapon interface used by the identifiers.
     */
    private WeaponInterface weaponType;

    /**
     * Create a new {@link CombatSpecial}.
     *
     * @param identifers the weapon ID's that perform this special when
     * activated.
     * @param drainAmount the amount of special energy this attack will drain.
     * @param strengthBonus the strength bonus when performing this special
     * attack.
     * @param accuracyBonus the accuracy bonus when performing this special
     * attack.
     * @param combatType the combat type used when performing this special
     * attack.
     * @param weaponType the weapon interface used by the identifiers.
     */
    private CombatSpecial(int[] identifiers, int drainAmount,
            double strengthBonus, double accuracyBonus, CombatType combatType,
            WeaponInterface weaponType) {
        this.identifiers = identifiers;
        this.drainAmount = drainAmount;
        this.strengthBonus = strengthBonus;
        this.accuracyBonus = accuracyBonus;
        this.combatType = combatType;
        this.weaponType = weaponType;
    }

    /**
     * Fired when the argued {@link Player} activates the special attack bar.
     *
     * @param player the player activating the special attack bar.
     * @param target the target when activating the special attack bar, will be
     * <code>null</code> if the player is not in combat while activating the
     * special bar.
     */
    public void onActivation(Player player, Character target) {

    }

    /**
     * Fired when the argued {@link Player} is about to attack the argued
     * target.
     *
     * @param player the player about to attack the target.
     * @param target the entity being attacked by the player.
     * @return the combat container for this combat hook.
     */
    public abstract CombatContainer container(Player player, Character target);

    /**
     * Drains the special bar for the argued {@link Player}.
     *
     * @param player the player who's special bar will be drained.
     * @param amount the amount of energy to drain from the special bar.
     */
    public static void drain(Player player, int amount) {
      
        if (player.getEquipment().get(Equipment.RING_SLOT).getId() == 19669) {
        	amount *= .90;
        }
        player.decrementSpecialPercentage(amount);
        player.getPacketSender().sendSpriteChange(41006, 945);
        player.setSpecialActivated(false);
        CombatSpecial.updateBar(player);
        if (!player.isRecoveringSpecialAttack()) {
            TaskManager.submit(new PlayerSpecialAmountTask());
        }
        player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.DEAL_SPECIAL_ATTACK, 1);    }

    /**
     * Restores the special bar for the argued {@link Player}.
     *
     * @param player the player who's special bar will be restored.
     * @param amount the amount of energy to restore to the special bar.
     */
    public static void restore(Player player, int amount) {
        player.incrementSpecialPercentage(amount);
        CombatSpecial.updateBar(player);
    }

    /**
     * Updates the special bar with the amount of special energy the argued
     * {@link Player} has.
     *
     * @param player the player who's special bar will be updated.
     */
    public static void updateBar(Player player) {
        if (player.getWeapon().getSpecialBar() == -1 || player.getWeapon().getSpecialMeter() == -1) {
            return;
        }
        int specialCheck = 10;
        int specialBar = player.getWeapon().getSpecialMeter();
        int specialAmount = player.getSpecialPercentage() / 10;

        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendInterfaceComponentMoval(specialAmount >= specialCheck ? 500 : 0, 0, --specialBar);
            specialCheck--;
        }
        player.getPacketSender().updateSpecialAttackOrb().sendString(player.getWeapon().getSpecialMeter(), player.isSpecialActivated() ? ("@yel@ Special Attack (" + player.getSpecialPercentage() + "%)") : ("@bla@ Special Attack (" + player.getSpecialPercentage() + "%"));

    }

    /**
     * Assigns special bars to the attack style interface if needed.
     *
     * @param player the player to assign the special bar for.
     */
    public static void assign(Player player) {
        if (player.getWeapon().getSpecialBar() == -1) {
            //if(!player.isPerformingSpecialAttack()) {
            player.getPacketSender().sendSpriteChange(41006, 945);
            player.setSpecialActivated(false);
            player.setCombatSpecial(null);
            CombatSpecial.updateBar(player);
            //}

            return;
        }

        for (CombatSpecial c : CombatSpecial.values()) {
            if (player.getWeapon() == c.getWeaponType()) {
                if (Arrays.stream(c.getIdentifiers()).anyMatch(
                        id -> player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == id)) {
                    player.getPacketSender().sendInterfaceDisplayState(player.getWeapon().getSpecialBar(), false);
                    player.setCombatSpecial(c);
                    return;
                }
            }
        }

        player.getPacketSender().sendInterfaceDisplayState(player.getWeapon().getSpecialBar(), true);
        player.setCombatSpecial(null);
    }

    public static void activate(Player player) {
        if (Dueling.checkRule(player, DuelRule.NO_SPECIAL_ATTACKS)) {
            player.getPacketSender().sendMessage("Special Attacks have been turned off in this duel.");
            return;
        }
        if (player.getCombatSpecial() == null) {
            return;
        }
        if (player.isSpecialActivated()) {
            player.getPacketSender().sendSpriteChange(41006, 945);
            player.setSpecialActivated(false);
            CombatSpecial.updateBar(player);
        } else {
            if (player.getSpecialPercentage() < player.getCombatSpecial().getDrainAmount()) {
                player.getPacketSender().sendMessage(
                        "You do not have enough special attack energy left!");
                return;
            }
            final CombatSpecial spec = player.getCombatSpecial();
            boolean instantSpecial = spec == CombatSpecial.GRANITE_MAUL || spec == CombatSpecial.DRAGON_BATTLEAXE || spec == CombatSpecial.STAFF_OF_LIGHT;
            if (spec != CombatSpecial.STAFF_OF_LIGHT && player.isAutocast()) {
                Autocasting.resetAutocast(player, true);
            } else if (spec == CombatSpecial.STAFF_OF_LIGHT && player.hasStaffOfLightEffect()) {
                player.getPacketSender().sendMessage("You are already being protected by the Staff of Light!");
                return;
            }
            player.getPacketSender().sendSpriteChange(41006, 946);
            player.setSpecialActivated(true);
            if (instantSpecial) {
                spec.onActivation(player, player.getCombatBuilder().getVictim());
                if (spec == CombatSpecial.GRANITE_MAUL && player.getCombatBuilder().isAttacking() && !player.getCombatBuilder().isCooldown()) {
                    player.getCombatBuilder().setAttackTimer(0);
                    player.getCombatBuilder().attack(player.getCombatBuilder().getVictim());
                    player.getCombatBuilder().instant();
                } else {
                    CombatSpecial.updateBar(player);
                }
            } else {
                CombatSpecial.updateBar(player);
                TaskManager.submit(new Task(1, false) {
                    @Override
                    public void execute() {
                        if (!player.isSpecialActivated()) {
                            this.stop();
                            return;
                        }
                        spec.onActivation(player, player.getCombatBuilder().getVictim());
                        this.stop();
                    }
                }.bind(player));
            }
        }
    }

    /**
     * Gets the weapon ID's that perform this special when activated.
     *
     * @return the weapon ID's that perform this special when activated.
     */
    public int[] getIdentifiers() {
        return identifiers;
    }

    /**
     * Gets the amount of special energy this attack will drain.
     *
     * @return the amount of special energy this attack will drain.
     */
    public int getDrainAmount() {
        return drainAmount;
    }

    /**
     * Gets the strength bonus when performing this special attack.
     *
     * @return the strength bonus when performing this special attack.
     */
    public double getStrengthBonus() {
        return strengthBonus;
    }

    /**
     * Gets the accuracy bonus when performing this special attack.
     *
     * @return the accuracy bonus when performing this special attack.
     */
    public double getAccuracyBonus() {
        return accuracyBonus;
    }

    /**
     * Gets the combat type used when performing this special attack.
     *
     * @return the combat type used when performing this special attack.
     */
    public CombatType getCombatType() {
        return combatType;
    }

    /**
     * Gets the weapon interface used by the identifiers.
     *
     * @return the weapon interface used by the identifiers.
     */
    public WeaponInterface getWeaponType() {
        return weaponType;
    }
}