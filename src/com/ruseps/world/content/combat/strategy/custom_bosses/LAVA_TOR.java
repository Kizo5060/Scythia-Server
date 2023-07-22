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

public class LAVA_TOR implements CombatStrategy {

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
        NPC LAVA_TOR = (NPC)entity;
        if(LAVA_TOR.isChargingAttack() || LAVA_TOR.getConstitution() <= 0) {
            LAVA_TOR.getCombatBuilder().setAttackTimer(4);
            return true;
        }
        if(Locations.goodDistance(LAVA_TOR.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            LAVA_TOR.performAnimation(new Animation(LAVA_TOR.getDefinition().getAttackAnimation()));
            LAVA_TOR.getCombatBuilder().setContainer(new CombatContainer(LAVA_TOR, victim, 1, 1, CombatType.MELEE, true));
        } else {
            LAVA_TOR.setChargingAttack(true);
            LAVA_TOR.performAnimation(new Animation(getAnimation(LAVA_TOR.getId())));
            LAVA_TOR.getCombatBuilder().setContainer(new CombatContainer(LAVA_TOR, victim, 1, 3, CombatType.MELEE, true));
            TaskManager.submit(new Task(1, LAVA_TOR, false) {
                int tick = 0;
                @Override
                public void execute() {
					/*if(tick == 1 && LAVA_TOR.getId() == 50) {
						new Projectile(LAVA_TOR, victim, 393 + Misc.getRandom(3), 44, 3, 43, 43, 0).sendProjectile();
					} else*/
                    if(tick == 1) {
                        victim.performGraphic(new Graphic(1626, GraphicHeight.MIDDLE));
                        LAVA_TOR.performAnimation(new Animation(getAnimation(LAVA_TOR.getId())));
                    } else if(tick == 3) {
                        victim.performGraphic(new Graphic(1627, GraphicHeight.MIDDLE));
                        LAVA_TOR.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
                        stop();
                    }
                    tick++;
                }
            });

        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 1125;
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
