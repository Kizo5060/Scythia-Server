package com.ruseps.world.content.achievements;

import com.ruseps.model.Item;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
//  63; keep track of the latest string used (update if you add new achievements)
public enum AchievementData {
	
	KILL_A_MONSTER_USING_MELEE(0, AchievementType.EASY, 1, "Kill a Monster Using Melee",
            new Item[] {new Item(621, 1),new Item(19992, 1)},
            new BossPointReward(10)),
	KILL_A_MONSTER_USING_RANGED(1, AchievementType.EASY, 1, "Kill a Monster Using Ranged",
            new Item[] {new Item(621, 1),new Item(19992, 1)},
            new BossPointReward(10)),
	KILL_A_MONSTER_USING_MAGIC(2, AchievementType.EASY, 1, "Kill a Monster Using Magic",
            new Item[] {new Item(621, 1),new Item(19992, 1)},
            new BossPointReward(10)),
	KILL_SNOWMAN(5, AchievementType.EASY, 1, "Kill Abominable Snowman",
            new Item[] {new Item(7080, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	KILL_RYUK(6, AchievementType.EASY, 1, "Kill Ryuk",
            new Item[] {new Item(19919, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	VISIT_POS(7, AchievementType.EASY, 1, "Access ::pos",
            new Item[] {new Item(621, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	COMPLETE_A_SLAYER_TASK(8, AchievementType.EASY, 1, "Complete A Slayer Task",
            new Item[] {new Item(10835, 1),new Item(2572, 1)},
            new BossPointReward(10)),
	REACH_MAX_EXP_IN_A_SKILL(9, AchievementType.EASY, 1, "Reach Max Exp In A Skill",
            new Item[] {new Item(10835, 500),new Item(989, 1)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_MELEE(10, AchievementType.EASY, 50000, "Deal 50K Melee Damage",
            new Item[] {new Item(14018, 1),new Item(19992, 2)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_RANGED(11, AchievementType.EASY, 50000, "Deal 50K Ranged Damage",
            new Item[] {new Item(20523, 1),new Item(19992, 2)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_MAGIC(12, AchievementType.EASY, 50000, "Deal 50K Magic Damage",
            new Item[] {new Item(11653, 1),new Item(19992, 2)},
            new BossPointReward(10)),
	PERFORM_A_SPECIAL_ATTACK(13, AchievementType.EASY, 1, "Perform a Special Attack",
            new Item[] {new Item(19992, 1)},
            new BossPointReward(10)),
    //Medium
	KILL_CHARIZARD_100_TIMES(17, AchievementType.MEDIUM, 100, "Kill Charizard 100 Times",
            new Item[] {new Item(15501, 1),new Item(19992, 10), new Item(621, 1)},
            new BossPointReward(10)),
	KILL_TIKI_DEMON_250_TIMES(18, AchievementType.MEDIUM, 250, "Kill Tiki Demon 250 Times",
            new Item[] {new Item(15501, 1),new Item(19992, 10), new Item(621, 1)},
            new BossPointReward(10)),
	COMPLETE_POKEMON_RAIDS(21, AchievementType.MEDIUM, 1, "Complete a Pokemon Raid",
            new Item[] {new Item(5585, 1)},
            new BossPointReward(10)),
	COMPLETE_ANIME_RAIDS(22, AchievementType.MEDIUM, 1, "Complete a Anime Raid",
            new Item[] {new Item(21013, 1)},
            new BossPointReward(10)),
	COMPLETE_A_HARD_SLAYER_TASK(24, AchievementType.MEDIUM, 1, "Complete A Hard Slayer Task",
            new Item[] {new Item(18338, 5),new Item(19992, 5)},
            new BossPointReward(10)),
	DEAL_MEDIUM_DAMAGE_USING_MELEE(26, AchievementType.MEDIUM, 150000, "Deal 150K Melee Damage",
            new Item[] {new Item(11659, 1),new Item(19992, 10)},
            new BossPointReward(10)),
	DEAL_MEDIUM_DAMAGE_USING_RANGED(27, AchievementType.MEDIUM, 150000, "Deal 150K Ranged Damage",
            new Item[] {new Item(10502, 1),new Item(19992, 10)},
            new BossPointReward(10)),
	DEAL_MEDIUM_DAMAGE_USING_MAGIC(28, AchievementType.MEDIUM, 150000, "Deal 150K Magic Damage",
            new Item[] {new Item(18968, 1),new Item(19992, 10)},
            new BossPointReward(10)),
	
	KILL_BAD_BITCH_500_TIMES(31, AchievementType.HARD, 500, "Kill Naruto 500 Times",
            new Item[] {new Item(15501, 5),new Item(19992, 25)},
            new BossPointReward(250)),
	COMPLETE_AN_ELITE_SLAYER_TASK(33, AchievementType.HARD, 100, "Claim 100 Bonds",
            new Item[] {new Item(19888, 1),new Item(19092, 1)},
            new BossPointReward(250)),
	COMPLETE_POKEMON_RAIDS_50_TIMES(34, AchievementType.HARD, 50, "Complete 50 DBZ Minigames",
            new Item[] {new Item(5585, 5)},
            new BossPointReward(250)),
	COMPLETE_ANIME_RAIDS_50_TIMES(35, AchievementType.HARD, 50, "Complete 50 DBZ Minigames",
            new Item[] {new Item(21013, 5)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_MELEE(41, AchievementType.HARD, 2000000, "Deal 2M Melee Damage",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_RANGED(42, AchievementType.HARD, 2000000, "Deal 2M Ranged Damage",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_MAGIC(43, AchievementType.HARD, 2000000, "Deal 2M Magic Damage",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
	DEFEAT_WR3CKED_YOUS_BOSS(44, AchievementType.HARD, 1, "Defeat Wr3ckeds Girl",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
	DEFEAT_LAVA_DEMON_BOSS(45, AchievementType.HARD, 1, "Defeat Lava Demon Global Boss",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
//daily
	
    ;

    public static final AchievementData[] values = AchievementData.values();
    public static final AchievementData[][] arraysByType = new AchievementData[AchievementType.values().length][];

    public static void checkDuplicateIds() {
        Set<Integer> ids = new HashSet<>();
        for (AchievementData achievement : values) {
            if (ids.contains(achievement.id)) {
                System.err.println("AchievementData sharing the same id!!! Shutting Down. Each achievement must have a unique id.");
                for (AchievementData data : values) {
                    if (data.id == achievement.id) {
                        System.out.println(data.name() + " id: " + data.id);
                    }
                }
                System.exit(0);
            }
            ids.add(achievement.id);
        }
    }

    final int id;
    final AchievementType type;
    final String description;
    final int progressAmount;
    final Item[] itemRewards;
    final NonItemReward[] nonItemRewards;

    AchievementData(int id, AchievementType type, int progressAmount, String description, Item[] itemRewards, NonItemReward... nonItemRewards) {
        this.id = id;
        this.type = type;
        this.progressAmount = progressAmount;
        this.description = description;
        this.itemRewards = itemRewards;
        this.nonItemRewards = nonItemRewards;
    }

    @Override
    public String toString() {
        return WordUtils.capitalize(this.name().toLowerCase().replaceAll("_", " "));
    }

    public static AchievementData[] getAchievementsOfType(AchievementType type){
        int index = type.ordinal();
        if (arraysByType[index] != null) {
            return arraysByType[index];
        }
        arraysByType[index] = Arrays.stream(AchievementData.values).filter(a -> a.type.equals(type)).toArray(AchievementData[]::new);
        return arraysByType[index];
    }
}
