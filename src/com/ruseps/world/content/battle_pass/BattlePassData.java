package com.ruseps.world.content.battle_pass;

import java.util.HashMap;
import java.util.Map;
import com.ruseps.model.Item;
import lombok.Getter;

public enum BattlePassData
{
	Level1(1, 10_000, new Item(5585, 5), new Item(5585, 10), BattlePassPages.PAGE_ONE), // SECOND NUMBER IS THE EXP REQUIRED
	Level2(2, 10_000, new Item(915, 3), new Item(915, 5), BattlePassPages.PAGE_ONE),
	Level3(3, 10_000, new Item(18057, 1), new Item(18058, 1), BattlePassPages.PAGE_ONE),
	Level4(4, 10_000, new Item(6855, 1), new Item(6856, 1), BattlePassPages.PAGE_ONE),
	Level5(5, 10_000, new Item(10835, 2), new Item(10835, 4), BattlePassPages.PAGE_ONE),
	Level6(6, 10_000, new Item(2572, 1), new Item(2507, 10), BattlePassPages.PAGE_ONE),
	Level7(7, 10_000, new Item(5585, 5), new Item(21013, 10), BattlePassPages.PAGE_ONE),
	Level8(8, 10_000, new Item(13016, 1), new Item(13016, 3), BattlePassPages.PAGE_ONE),
	Level9(9, 10_000, new Item(2505, 5), new Item(2505, 10), BattlePassPages.PAGE_TWO),
	Level10(10, 25_000, new Item(621, 5), new Item(10942, 1), BattlePassPages.PAGE_TWO),
	Level11(11, 10_000, new Item(6855, 1), new Item(6856, 1), BattlePassPages.PAGE_TWO),
	Level12(12, 10_000, new Item(19990, 10), new Item(19990, 20), BattlePassPages.PAGE_TWO),
	Level13(13, 10_000, new Item(915, 5), new Item(915, 8), BattlePassPages.PAGE_TWO),
	Level14(14, 10_000, new Item(19992, 10), new Item(19992, 20), BattlePassPages.PAGE_TWO),
	Level15(15, 10_000, new Item(2752, 10), new Item(4015, 25), BattlePassPages.PAGE_TWO),
	Level16(16, 10_000, new Item(6199, 2), new Item(6199, 4), BattlePassPages.PAGE_TWO),
	Level17(17, 10_000, new Item(14471, 7), new Item(14471, 15), BattlePassPages.PAGE_THREE),
	Level18(18, 10_000, new Item(20657, 1), new Item(1808, 25), BattlePassPages.PAGE_THREE),
	Level19(19, 10_000, new Item(8788, 25), new Item(18653, 20), BattlePassPages.PAGE_THREE),
	Level20(20, 25_000, new Item(10942, 1), new Item(10934, 1), BattlePassPages.PAGE_THREE),
	Level21(21, 10_000, new Item(13016, 2), new Item(13016, 4), BattlePassPages.PAGE_THREE),
	Level22(22, 10_000, new Item(6856, 1), new Item(6853, 1), BattlePassPages.PAGE_THREE),
	Level23(23, 10_000, new Item(14471, 5), new Item(14471, 10), BattlePassPages.PAGE_THREE),
	Level24(24, 10_000, new Item(2752, 10), new Item(2747, 25), BattlePassPages.PAGE_THREE),
	Level25(25, 10_000, new Item(21055, 1), new Item(21055, 3), BattlePassPages.PAGE_FOUR),
	Level26(26, 10_000, new Item(8788, 75), new Item(14639, 30), BattlePassPages.PAGE_FOUR),
	Level27(27, 10_000, new Item(10942, 1), new Item(10934, 1), BattlePassPages.PAGE_FOUR),
	Level28(28, 10_000, new Item(19990, 10), new Item(19990, 20), BattlePassPages.PAGE_FOUR),
	Level29(29, 10_000, new Item(18058, 1), new Item(10942, 1), BattlePassPages.PAGE_FOUR),
	Level30(30, 25_000, new Item(6856, 1), new Item(85, 10), BattlePassPages.PAGE_FOUR),
	Level31(31, 10_000, new Item(19992, 10), new Item(19992, 20), BattlePassPages.PAGE_FOUR),
	Level32(32, 10_000, new Item(6199, 2), new Item(6199, 4), BattlePassPages.PAGE_FOUR),
	Level33(33, 25_000, new Item(9077, 2), new Item(6853, 1), BattlePassPages.PAGE_FIVE),
	Level34(34, 10_000, new Item(2752, 10), new Item(85, 10), BattlePassPages.PAGE_FIVE),
	Level35(35, 25_000, new Item(10942, 1), new Item(10935, 1), BattlePassPages.PAGE_FIVE),
	Level36(36, 10_000, new Item(18689, 5), new Item(18689, 10), BattlePassPages.PAGE_FIVE),
	Level37(37, 10_000, new Item(6853, 1), new Item(6854, 1), BattlePassPages.PAGE_FIVE),
	Level38(38, 10_000, new Item(13234, 10), new Item(275, 10), BattlePassPages.PAGE_FIVE),
	Level39(39, 10_000, new Item(18059, 1), new Item(18060, 1), BattlePassPages.PAGE_FIVE),
	Level40(40, 25_000, new Item(21056, 3), new Item(21056, 8), BattlePassPages.PAGE_FIVE),
	;
	static final Map<Integer, BattlePassData> byId = new HashMap<Integer, BattlePassData>();
	
	private int level;
    private int exp;
    private Item bronzeReward;
    private Item goldReward;
    private BattlePassPages page;

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public Item getBronzeReward() {
        return bronzeReward;
    }

    public Item getGoldReward() {
        return goldReward;
    }

    public BattlePassPages getPage() {
        return page;
    }
	
	BattlePassData(int level, int exp, Item bronzeReward, Item goldReward, BattlePassPages page)
	{
		this.level = (level);
		this.bronzeReward = (bronzeReward);
		this.goldReward = (goldReward);
		this.page = (page);
		this.exp = (exp);
	}
	
	static
	{
		for (BattlePassData e : BattlePassData.values())
		{
			if (byId.put(e.getLevel(), e) != null) {
				System.out.println("Warning: Duplicate ID found for BattlePassData with level " + e.getLevel());
				byId.remove(e.getLevel());
			}
		}
	}

    public static BattlePassData getByLevel(int id) {
        return byId.get(id);
    }

    public boolean isExperienceEnough(int playerExperience) {
        return playerExperience >= this.exp;
    }
}