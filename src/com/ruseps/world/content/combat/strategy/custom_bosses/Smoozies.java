package com.ruseps.world.content.combat.strategy.custom_bosses;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Smoozies implements CombatStrategy {


    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC Smoozies = (NPC)entity;

        if(Smoozies.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }
        if(Locations.goodDistance(Smoozies.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            Smoozies.performAnimation(new Animation(Smoozies.getDefinition().getAttackAnimation()));
            Smoozies.getCombatBuilder().setContainer(new CombatContainer(Smoozies, victim, 1, 1, CombatType.MELEE, true));
        } else if(!Locations.goodDistance(Smoozies.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
            Smoozies.setChargingAttack(true);
            final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
            ((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(1179), pos);
            Smoozies.performAnimation(new Animation(423));
            Smoozies.performGraphic(new Graphic(2009));
            TaskManager.submit(new Task(2) {
                @Override
                protected void execute() {
                    //Smoozies.moveTo(pos);
                    Smoozies.performAnimation(new Animation(Smoozies.getDefinition().getAttackAnimation()));
                    Smoozies.getCombatBuilder().setContainer(new CombatContainer(Smoozies, victim, 1, 1, CombatType.MELEE, false));
                    Smoozies.setChargingAttack(false);
                    Smoozies.getCombatBuilder().setAttackTimer(0);
                    stop();
                }
            });
        } else {
            Smoozies.setChargingAttack(true);
            boolean barrage = Misc.getRandom(4) <= 2;
            Smoozies.performAnimation(new Animation(barrage ? 401 : 10114));
            Smoozies.performGraphic(new Graphic (2003));
            Smoozies.getCombatBuilder().setContainer(new CombatContainer(Smoozies, victim, 1, 3, CombatType.MAGIC, true));
            TaskManager.submit(new Task(1, Smoozies, false) {
                int tick = 0;
                @Override
                public void execute() {
                    if(tick == 0 && !barrage) {
                        new Projectile(Smoozies, victim, 1271, 44, 3, 43, 43, 0).sendProjectile();
                    } else if(tick == 1) {
                        if(barrage && victim.isPlayer() && Misc.getRandom(10) <= 5) {
                            victim.getMovementQueue().freeze(5);
                            victim.performGraphic(new Graphic(197));
                        }
                        if(barrage && Misc.getRandom(6) <= 3) {
                            Smoozies.performAnimation(new Animation(440));
                            for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
                                if(toAttack != null && Locations.goodDistance(Smoozies.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
                                    new CombatHitTask(Smoozies.getCombatBuilder(), new CombatContainer(Smoozies, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
                                    toAttack.performGraphic(new Graphic(184));
                                }
                            }
                        }
                        Smoozies.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Smoozies) - 2);
                        stop();
                    }
                    tick++;
                }
            });
        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 401;

        return anim;
    }


    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 50;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}