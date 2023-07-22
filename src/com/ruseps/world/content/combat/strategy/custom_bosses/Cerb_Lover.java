package com.ruseps.world.content.combat.strategy.custom_bosses;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Position;
import com.ruseps.model.Projectile;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.HitQueue.CombatHit;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Cerb_Lover implements CombatStrategy {

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
        NPC Cerb_Lover = (NPC)entity;
        if(victim.getConstitution() <= 0) {
            return true;
        }
        if(Cerb_Lover.isChargingAttack()) {
            return true;
        }

        Cerb_Lover.setChargingAttack(true);
        Cerb_Lover.performAnimation(new Animation((4492)));
        final CombatType attkType = Misc.getRandom(5) <= 2 ? CombatType.RANGED : CombatType.MAGIC;
        Cerb_Lover.getCombatBuilder().setContainer(new CombatContainer(Cerb_Lover, victim, 1, 4, attkType, Misc.getRandom(5) <= 1 ? false : true));
        TaskManager.submit(new Task(1, Cerb_Lover, false) {
            int tick = 0;
            @Override
            public void execute() {
                if(tick == 2) {
                    new Projectile(Cerb_Lover, victim, (attkType == CombatType.RANGED ? 450 : 439), 44, 3, 43, 43, 0).sendProjectile();
                } else if(tick == 3) {
                    new CombatHit(Cerb_Lover.getCombatBuilder(), new CombatContainer(Cerb_Lover, victim, 1, CombatType.MAGIC, true)).handleAttack();
                    if(Misc.getRandom(10) <= 2) {
                        Player p = (Player)victim;
                        int lvl = p.getSkillManager().getCurrentLevel(Skill.PRAYER);
                        lvl *= 0.9;
                        p.getSkillManager().setCurrentLevel(Skill.PRAYER, p.getSkillManager().getCurrentLevel(Skill.PRAYER) - lvl <= 0 ?  1 : lvl);
                        p.getPacketSender().sendMessage("Cerb_Lover has reduced your Prayer level.");
                    }
                    Cerb_Lover.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 20;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
