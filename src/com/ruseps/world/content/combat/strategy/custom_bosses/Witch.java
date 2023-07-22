package com.ruseps.world.content.combat.strategy.custom_bosses;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.Locations;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class Witch implements CombatStrategy {

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
        NPC lover_keeper = (NPC)entity;
        if(lover_keeper.isChargingAttack() || lover_keeper.getConstitution() <= 0) {
            lover_keeper.getCombatBuilder().setAttackTimer(4);
            return true;
        }
        if(Locations.goodDistance(lover_keeper.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            lover_keeper.performAnimation(new Animation(lover_keeper.getDefinition().getAttackAnimation()));
            lover_keeper.getCombatBuilder().setContainer(new CombatContainer(lover_keeper, victim, 1, 1, CombatType.MELEE, true));
        } else {
            lover_keeper.setChargingAttack(true);
            lover_keeper.performAnimation(new Animation(getAnimation(lover_keeper.getId())));
            lover_keeper.getCombatBuilder().setContainer(new CombatContainer(lover_keeper, victim, 1, 3, CombatType.MELEE, true));
            TaskManager.submit(new Task(1, lover_keeper, false) {
                int tick = 0;
                @Override
                public void execute() {
					/*if(tick == 1 && lover_keeper.getId() == 50) {
						new Projectile(lover_keeper, victim, 393 + Misc.getRandom(3), 44, 3, 43, 43, 0).sendProjectile();
					} else*/
                    if(tick == 2) {
                        victim.performGraphic(new Graphic(1626, GraphicHeight.MIDDLE));
                    } else if(tick == 3) {
                        victim.performGraphic(new Graphic(1627, GraphicHeight.MIDDLE));
                        lover_keeper.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
                        stop();
                    }
                    tick++;
                }
            });

        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 9047;
        return anim;
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
        return CombatType.MIXED;
    }
}
