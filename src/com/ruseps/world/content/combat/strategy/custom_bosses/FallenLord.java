package com.ruseps.world.content.combat.strategy.custom_bosses;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.*;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class FallenLord implements CombatStrategy {

    private boolean[] specialAttacks = new boolean[3];
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
        NPC panther = (NPC)entity;
        if(panther.isChargingAttack() || victim.getConstitution() <= 0) {
            return true;
        }

        regularAttack(victim, panther);
        return true;
    }




    public void regularAttack(Character victim, NPC FallenLord) {
        //FallenLord.setChargingAttack(false).getCombatBuilder().setAttackTimer(attackDelay(FallenLord) - 2);
        FallenLord.performAnimation(new Animation(FallenLord.getDefinition().getAttackAnimation()));
        for(Player p : Misc.getCombinedPlayerList((Player)victim)) {
            TaskManager.submit(new Task(2, FallenLord, false) {
                @Override
                public void execute() {
                    boolean mage = Misc.getRandom(10) <= 7;
                        new Projectile(FallenLord, p, mage ? 1278 : 1607, 44, 2, 43, 43, 0).sendProjectile();
                        p.dealDamage(new Hit(Misc.random(300, 800), Hitmask.DARK_GREEN, mage ? CombatIcon.RANGED : CombatIcon.RANGED));
                    /*FallenLord.getCombatBuilder().setContainer(new CombatContainer(FallenLord, p, 1, mage ? 3 : 2,
                        mage ? CombatType.MAGIC : CombatType.RANGED, true));*/
                    stop();
                }
            });
        }
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
