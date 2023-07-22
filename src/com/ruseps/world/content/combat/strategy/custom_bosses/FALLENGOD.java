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

public class FALLENGOD implements CombatStrategy {

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
        NPC FALLENGOD = (NPC)entity;
        if(FALLENGOD.isChargingAttack() || FALLENGOD.getConstitution() <= 0) {
            FALLENGOD.getCombatBuilder().setAttackTimer(4);
            return true;
        }
        if(Locations.goodDistance(FALLENGOD.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(5) <= 3) {
            FALLENGOD.performAnimation(new Animation(FALLENGOD.getDefinition().getAttackAnimation()));
            FALLENGOD.getCombatBuilder().setContainer(new CombatContainer(FALLENGOD, victim, 1, 1, CombatType.MELEE, true));
        } else {
            FALLENGOD.setChargingAttack(true);
            FALLENGOD.performAnimation(new Animation(getAnimation(FALLENGOD.getId())));
            FALLENGOD.getCombatBuilder().setContainer(new CombatContainer(FALLENGOD, victim, 1, 3, CombatType.MELEE, true));
            TaskManager.submit(new Task(1, FALLENGOD, false) {
                int tick = 0;
                @Override
                public void execute() {
					/*if(tick == 1 && FALLENGOD.getId() == 50) {
						new Projectile(FALLENGOD, victim, 393 + Misc.getRandom(3), 44, 3, 43, 43, 0).sendProjectile();
					} else*/
                    if(tick == 1) {
                        victim.performGraphic(new Graphic(1626, GraphicHeight.MIDDLE));
                        FALLENGOD.performAnimation(new Animation(getAnimation(FALLENGOD.getId())));
                    } else if(tick == 3) {
                        victim.performGraphic(new Graphic(1627, GraphicHeight.MIDDLE));
                        FALLENGOD.setChargingAttack(false).getCombatBuilder().setAttackTimer(6);
                        stop();
                    }
                    tick++;
                }
            });

        }
        return true;
    }

    public static int getAnimation(int npc) {
        int anim = 812;
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
