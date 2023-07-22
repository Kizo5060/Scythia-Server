package com.ruseps.world.content.combat.strategy.multi_formula;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
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

public class SharkBeast implements CombatStrategy {

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
        NPC Sharkbeast = (NPC)entity;
        if(Sharkbeast.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }
        if(Locations.goodDistance(Sharkbeast.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            Sharkbeast.performAnimation(new Animation(Sharkbeast.getDefinition().getAttackAnimation()));
            Sharkbeast.getCombatBuilder().setContainer(new CombatContainer(Sharkbeast, victim, 1, 1, CombatType.MELEE, true));
        } else if(!Locations.goodDistance(Sharkbeast.getPosition().copy(), victim.getPosition().copy(), 3) && Misc.getRandom(5) == 1) {
            Sharkbeast.setChargingAttack(true);
            final Position pos = new Position(victim.getPosition().getX() - 3, victim.getPosition().getY());
            ((Player)victim).getPacketSender().sendGlobalGraphic(new Graphic(68), pos);
            Sharkbeast.performAnimation(new Animation(440));
            TaskManager.submit(new Task(2) {
                @Override
                protected void execute() {
                    Sharkbeast.performAnimation(new Animation(Sharkbeast.getDefinition().getAttackAnimation()));
                    Sharkbeast.getCombatBuilder().setContainer(new CombatContainer(Sharkbeast, victim, 1, 1, CombatType.MELEE, false));
                    Sharkbeast.setChargingAttack(false);
                    Sharkbeast.getCombatBuilder().setAttackTimer(0);
                    stop();
                }
            });
        } else {
            Sharkbeast.setChargingAttack(true);
            boolean barrage = Misc.getRandom(4) <= 2;
            Sharkbeast.performAnimation(new Animation(barrage ? 440 : 433));
            Sharkbeast.getCombatBuilder().setContainer(new CombatContainer(Sharkbeast, victim, 1, 3, CombatType.MAGIC, true));
            TaskManager.submit(new Task(1, Sharkbeast, false) {
                int tick = 0;
                @Override
                public void execute() {
                    if(tick == 0 && !barrage) {
                        new Projectile(Sharkbeast, victim, 87, 44, 3, 43, 43, 0).sendProjectile();
                    } else if(tick == 1) {
                        if(barrage && Misc.getRandom(6) <= 3) {
                            Sharkbeast.performAnimation(new Animation(433));
                            for(Player toAttack : Misc.getCombinedPlayerList((Player)victim)) {
                                if(toAttack != null && Locations.goodDistance(Sharkbeast.getPosition(), toAttack.getPosition(), 7) && toAttack.getConstitution() > 0) {
                                    new CombatHitTask(Sharkbeast.getCombatBuilder(), new CombatContainer(Sharkbeast, toAttack, 2, CombatType.MAGIC, false)).handleAttack();
                                    toAttack.performGraphic(new Graphic(502));
                                }
                            }
                        }
                        Sharkbeast.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(Sharkbeast) - 2);
                        stop();
                    }
                    tick++;
                }
            });
        }
        return true;
    }


    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }
}