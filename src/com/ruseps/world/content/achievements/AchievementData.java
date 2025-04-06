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
            new Item[] {new Item(915, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	KILL_RYUK(6, AchievementType.EASY, 1, "Kill Ryuk",
            new Item[] {new Item(19919, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	VISIT_POS(7, AchievementType.EASY, 1, "Access ::pos",
            new Item[] {new Item(621, 1), new Item(19992, 1)},
            new BossPointReward(10)),
	COMPLETE_5_SLAYER_TASKS(8, AchievementType.EASY, 5, "Complete 5 Slayer Tasks",
            new Item[] {new Item(621, 1)},
            new BossPointReward(10)),
	REACH_MAX_EXP_IN_A_SKILL(9, AchievementType.EASY, 1, "Reach Max Exp In A Skill",
            new Item[] {new Item(10835, 1),new Item(915, 2)},
            new BossPointReward(10)),
	COMPLETE_5_HARD_SLAYER_TASKS(24, AchievementType.MEDIUM, 5, "Complete 5 Hard Slayer Tasks",
            new Item[] {new Item(21055, 1),new Item(19992, 5)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_MELEE(10, AchievementType.EASY, 50000, "Deal 50K Melee Damage",
            new Item[] {new Item(915, 2),new Item(19992, 2)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_RANGED(11, AchievementType.EASY, 50000, "Deal 50K Ranged Damage",
            new Item[] {new Item(915, 2),new Item(19992, 2)},
            new BossPointReward(10)),
	DEAL_EASY_DAMAGE_USING_MAGIC(12, AchievementType.EASY, 50000, "Deal 50K Magic Damage",
            new Item[] {new Item(915, 2),new Item(19992, 2)},
            new BossPointReward(10)),
	DEAL_SPECIAL_ATTACK(13, AchievementType.EASY, 1, "Do a Special Attack",
            new Item[] {new Item(11653, 1),new Item(19992, 2)},
            new BossPointReward(10)),
	VOTE_ONCE(14, AchievementType.EASY, 1, "Vote Once",
            new Item[] {new Item(6199, 1),new Item(915, 1)},
            new BossPointReward(10)),
	
    //Medium
	KILL_CHARIZARD_100_TIMES(17, AchievementType.MEDIUM, 100, "Kill Charizard 100 Times",
            new Item[] {new Item(6199, 1),new Item(19992, 10), new Item(621, 2)},
            new BossPointReward(10)),
	KILL_TIKI_DEMON_250_TIMES(18, AchievementType.MEDIUM, 250, "Kill Tiki Demon 250 Times",
            new Item[] {new Item(19992, 10), new Item(621, 3)},
            new BossPointReward(10)),
	COMPLETE_POKEMON_RAIDS(21, AchievementType.MEDIUM, 1, "Complete a Pokemon Raid",
            new Item[] {new Item(5585, 5)},
            new BossPointReward(10)),
	COMPLETE_ANIME_RAIDS(22, AchievementType.MEDIUM, 1, "Complete a Anime Raid",
            new Item[] {new Item(21013, 1),new Item(621, 5)},
            new BossPointReward(10)),
	COMPLETE_20_DURADEL_SLAYER_TASKS(58, AchievementType.MEDIUM, 20, "Complete 5 Hard Slayer Tasks",
            new Item[] {new Item(915, 5),new Item(19992, 50)},
            new BossPointReward(10)),
	COMPLETE_30_HARD_SLAYER_TASKS(57, AchievementType.MEDIUM, 30, "Complete 5 Hard Slayer Tasks",
            new Item[] {new Item(13016, 3),new Item(14471, 2)},
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
	VOTE_10_TIMES(29, AchievementType.MEDIUM, 10, "Vote 10 Times",
            new Item[] {new Item(6199, 2),new Item(915, 2)},
            new BossPointReward(10)),
	
	// Hard
	KILL_BAD_BITCH_1500_TIMES(31, AchievementType.HARD, 1500, "Kill Bad Bitch 500 Times",
            new Item[] {new Item(8788, 100),new Item(19992, 25)},
            new BossPointReward(250)),
	COMPLETE_SOME_ELITE_SLAYER_TASKS(33, AchievementType.HARD, 25, "Complete 25 Elite Slayer Tasks",
            new Item[] {new Item(19992, 200),new Item(2867, 1),new Item(10942, 1)},
            new BossPointReward(250)),
	COMPLETE_POKEMON_RAIDS_50_TIMES(34, AchievementType.HARD, 50, "Complete 50 DBZ Minigames",
            new Item[] {new Item(5585, 5),new Item(10942, 1)},
            new BossPointReward(250)),
	COMPLETE_ANIME_RAIDS_50_TIMES(35, AchievementType.HARD, 50, "Complete 50 DBZ Minigames",
            new Item[] {new Item(21013, 5),new Item(10934, 1)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_MELEE(41, AchievementType.HARD, 2000000, "Deal 2M Melee Damage",
            new Item[] {new Item(5585, 10),new Item(21055, 2)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_RANGED(42, AchievementType.HARD, 2000000, "Deal 2M Ranged Damage",
            new Item[] {new Item(5585, 10),new Item(21055, 2)},
            new BossPointReward(250)),
	DEAL_HARD_DAMAGE_USING_MAGIC(43, AchievementType.HARD, 2000000, "Deal 2M Magic Damage",
            new Item[] {new Item(5585, 10),new Item(21055, 2)},
            new BossPointReward(250)),
	COMPLETE_SILVER_RAIDS_50_TIMES(53, AchievementType.HARD, 50, "Deal 2M Magic Damage",
            new Item[] {new Item(10942, 1),new Item(18057, 1)},
            new BossPointReward(250)),
	KILL_FOUR_ARMS_2500_TIMES(55, AchievementType.HARD, 2500, "Deal 2M Magic Damage",
            new Item[] {new Item(21071, 1),new Item(21055, 1),new Item(19992, 500)},
            new BossPointReward(250)),
	DEFEAT_INHERITED_BOSS(44, AchievementType.HARD, 1, "Defeat Witches Girl",
            new Item[] {new Item(5585, 10),new Item(915, 5)},
            new BossPointReward(250)),
	DEFEAT_LAVA_DEMON_BOSS(45, AchievementType.HARD, 1, "Defeat Lava Demon Global Boss",
            new Item[] {new Item(10934, 1),new Item(21055, 5)},
            new BossPointReward(250)),
	VOTE_25_TIMES(32, AchievementType.HARD, 25, "Vote 25 Times",
            new Item[] {new Item(6199, 2),new Item(21055, 1)},
            new BossPointReward(10)),
	COMPLETE_MORTAL_KOMBAT_RAIDS_150_TIMES(48, AchievementType.HARD, 150, "Complete 50 DBZ Minigames",
            new Item[] {new Item(10935, 1),new Item(2752, 25)},
            new BossPointReward(250)),
	
	//Expert
	COMPLETE_100_ELITE_SLAYER_TASKS(46, AchievementType.EXPERT, 100, "Complete 100 Elite Slayer Tasks",
            new Item[] {new Item(10934, 1),new Item(14471, 10),new Item(19992, 500)},
            new BossPointReward(250)),
	COMPLETE_ANIME_RAIDS_100_TIMES(47, AchievementType.EXPERT, 100, "Complete 50 DBZ Minigames",
            new Item[] {new Item(10934, 1),new Item(18058, 1)},
            new BossPointReward(250)),
	COMPLETE_DARK_RAIDS_350_TIMES(66, AchievementType.EXPERT, 350, "Complete 50 DBZ Minigames",
            new Item[] {new Item(21056, 3),new Item(10935, 1), new Item(6853, 1)},
            new BossPointReward(250)),
	COMPLETE_GOLD_RAIDS_200_TIMES(49, AchievementType.EXPERT, 200, "Complete 50 DBZ Minigames",
            new Item[] {new Item(2750, 30),new Item(10934, 1)},
            new BossPointReward(250)),
	COMPLETE_PLAT_RAIDS_400_TIMES(50, AchievementType.EXPERT, 400, "Complete 50 DBZ Minigames",
            new Item[] {new Item(18948, 1),new Item(18057, 1)},
            new BossPointReward(250)),
	COMPLETE_DIAMOND_RAIDS_250_TIMES(51, AchievementType.EXPERT, 250, "Complete 50 DBZ Minigames",
            new Item[] {new Item(2747, 50),new Item(10935, 1)},
            new BossPointReward(250)),
	DEFEAT_OCTANES_1000_TIMES(52, AchievementType.EXPERT, 1000, "Complete 50 DBZ Minigames",
            new Item[] {new Item(2716, 1),new Item(10942, 1), new Item(21055, 3)},
            new BossPointReward(250)),
	DEFEAT_KNIGHT_OF_TORMENT_10K_TIMES(54, AchievementType.EXPERT, 10000, "Complete 50 DBZ Minigames",
            new Item[] {new Item(10935, 1),new Item(21056, 1), new Item(18058, 1)},
            new BossPointReward(250)),
	COMPLETE_350_ELITE_SLAYER_TASKS(56, AchievementType.EXPERT, 350, "Complete 100 Elite Slayer Tasks",
            new Item[] {new Item(2858, 1),new Item(2869, 1),new Item(10935, 1),new Item(14471, 25)},
            new BossPointReward(250)),
	COMPLETE_250_RUBY_RAIDS(59, AchievementType.EXPERT, 250, "Complete 100 Elite Slayer Tasks",
            new Item[] {new Item(85, 30),new Item(10935, 1),new Item(18058, 1)},
            new BossPointReward(250)),
	COMPLETE_750_RUBY_RAIDS(60, AchievementType.EXPERT, 750, "Complete 100 Elite Slayer Tasks",
            new Item[] {new Item(85, 75),new Item(18689, 5),new Item(18059, 1),new Item(10943, 1)},
            new BossPointReward(250)),
	COMPLETE_200_BOSS_SLAYER_TASKS(61, AchievementType.EXPERT, 200, "Complete 100 Elite Slayer Tasks",
            new Item[] {new Item(17658, 1),new Item(21055, 2),new Item(14471, 10),new Item(10935, 1)},
            new BossPointReward(250)),
	COMPLETE_400_BOSS_SLAYER_TASKS(62, AchievementType.EXPERT, 400, "Complete 100 Elite Slayer Tasks",
     new Item[] {new Item(10598, 1),new Item(10934, 6),new Item(21056, 2),new Item(19992, 75000)},
     new BossPointReward(250)),
	KILL_LITCH_25K_TIMES(63, AchievementType.EXPERT, 25000, "Complete 50 DBZ Minigames",
            new Item[] {new Item(10935, 1),new Item(21056, 1), new Item(18059, 1)},
            new BossPointReward(250)),
	KILL_SPUDERMAN_40K_TIMES(64, AchievementType.EXPERT, 40000, "Complete 50 DBZ Minigames",
            new Item[] {new Item(10935, 1),new Item(21056, 2), new Item(10934, 2)},
            new BossPointReward(250)),
	VOTE_EXPERT(65, AchievementType.EXPERT, 50, "Vote 50 Times",
            new Item[] {new Item(6199, 5),new Item(21056, 2)},
            new BossPointReward(10)),
	COMPLETE_DRAGONSTONE_RAIDS_500_TIMES(67, AchievementType.EXPERT, 500, "Complete 50 DBZ Minigames",
            new Item[] {new Item(18689, 5),new Item(911, 1),new Item(6854, 1)},
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
    final public int progressAmount;
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
