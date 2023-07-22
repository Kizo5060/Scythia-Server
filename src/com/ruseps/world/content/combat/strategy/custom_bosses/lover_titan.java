package com.ruseps.world.content.combat.strategy.custom_bosses;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class lover_titan implements CombatStrategy {

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
        NPC lover_titan = (NPC)entity;
        if(lover_titan.isChargingAttack() || lover_titan.getConstitution() <= 0) {
            lover_titan.getCombatBuilder().setAttackTimer(4);
            return true;
        }
        if(Locations.goodDistance(lover_titan.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            lover_titan.performAnimation(new Animation(lover_titan.getDefinition().getAttackAnimation()));
            lover_titan.getCombatBuilder().setContainer(new CombatContainer(lover_titan, victim, 1, 1, CombatType.MELEE, true));
        } else {
            lover_titan.setChargingAttack(true);
            lover_titan.performAnimation(new Animation(getAnimation(lover_titan.getId())));
            lover_titan.getCombatBuilder().setContainer(new CombatContainer(lover_titan, victim, 1, 3, CombatType.MELEE, true));
            TaskManager.submit(new Task(1, lover_titan, false) {
                int tick = 0;
                @Override
                public void execute() {
					/*if(tick == 1 && lover_titan.getId() == 50) {
						new Projectile(lover_titan, victim, 393 + Misc.getRandom(3), 44, 3, 43, 43, 0).sendProjectile();
					} else*/
                    if(tick == 2) {
                        victim.performGraphic(new Graphic(1028));
                    } else if(tick == 3) {
                        victim.performGraphic(new Graphic(1271));
                        lover_titan.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
                        stop();
                    }
                    tick++;
                }
            });

        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 7299;
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
