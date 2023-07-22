package com.ruseps.world.content.battle_pass;

import java.util.HashMap;
import java.util.Map;
import com.ruseps.model.Item;
import lombok.Getter;

public enum BattlePassData
{
	Level1(1, 1_000_000, new Item(7124, 1), new Item(21689, 1), BattlePassPages.PAGE_ONE),
	Level2(2, 1_250_000, new Item(20052, 1), new Item(20053), BattlePassPages.PAGE_ONE),
	Level3(3, 1_500_000, new Item(15355), new Item(15356, 1), BattlePassPages.PAGE_ONE),
	Level4(4, 1_750_000, new Item(2572, 1), new Item(11533, 1), BattlePassPages.PAGE_ONE),
	Level5(5, 2_000_000, new Item(14933, 1), new Item(14933, 3), BattlePassPages.PAGE_ONE),
	Level6(6, 2_250_000, new Item(11527, 1), new Item(11529, 1), BattlePassPages.PAGE_ONE),
	Level7(7, 2_500_000, new Item(5022, 100), new Item(5022, 750), BattlePassPages.PAGE_ONE),
	Level8(8, 2_750_000, new Item(7118, 1), new Item(7118, 3), BattlePassPages.PAGE_ONE),
	Level9(9, 3_000_000, new Item(5022, 200), new Item(5022, 800), BattlePassPages.PAGE_TWO),
	Level10(10, 3_250_000, new Item(21689, 1), new Item(21689, 3), BattlePassPages.PAGE_TWO),
	Level11(11, 3_500_000, new Item(21685, 1), new Item(21685, 3), BattlePassPages.PAGE_TWO),
	Level12(12, 3_750_000, new Item(5022, 1000), new Item(5022, 10000), BattlePassPages.PAGE_TWO),
	Level13(13, 4_000_000, new Item(5175, 1), new Item(13733, 1), BattlePassPages.PAGE_TWO),
	Level14(14, 4_250_000, new Item(5176, 1), new Item(21838, 1), BattlePassPages.PAGE_TWO),
	Level15(15, 4_500_000, new Item(5022, 1500), new Item(5022, 25000), BattlePassPages.PAGE_TWO),
	Level16(16, 4_750_000, new Item(7124, 2), new Item(7124, 5), BattlePassPages.PAGE_TWO),
	Level17(17, 5_000_000, new Item(21863, 1), new Item(21863, 2), BattlePassPages.PAGE_THREE),
	Level18(18, 5_500_000, new Item(7124, 3), new Item(7124, 6), BattlePassPages.PAGE_THREE),
	Level19(19, 5_750_000, new Item(455, 1), new Item(455, 2), BattlePassPages.PAGE_THREE),
	Level20(20, 6_000_000, new Item(5083, 1), new Item(13728, 1), BattlePassPages.PAGE_THREE),
	Level21(21, 6_250_000, new Item(5084, 1), new Item(13729, 1), BattlePassPages.PAGE_THREE),
	Level22(22, 6_500_000, new Item(5085, 1), new Item(13730, 1), BattlePassPages.PAGE_THREE),
	Level23(23, 6_750_000, new Item(5086, 1), new Item(13731, 1), BattlePassPages.PAGE_THREE),
	Level24(24, 7_000_000, new Item(5087, 1), new Item(13732, 1), BattlePassPages.PAGE_THREE),
	Level25(25, 7_250_000, new Item(5088, 1), new Item(5022, 30000), BattlePassPages.PAGE_FOUR),
	Level26(26, 7_500_000, new Item(5089, 1), new Item(7124, 3), BattlePassPages.PAGE_FOUR),
	Level27(27, 7_750_000, new Item(21650, 1), new Item(21651, 1), BattlePassPages.PAGE_FOUR),
	Level28(28, 8_000_000, new Item(21864, 2), new Item(21864, 5), BattlePassPages.PAGE_FOUR),
	Level29(29, 8_250_000, new Item(3070, 1), new Item(8893, 1), BattlePassPages.PAGE_FOUR),
	Level30(30, 8_500_000, new Item(13265, 1), new Item(8894, 1), BattlePassPages.PAGE_FOUR),
	Level31(31, 8_750_000, new Item(13266, 1), new Item(8895, 1), BattlePassPages.PAGE_FOUR),
	Level32(32, 9_000_000, new Item(13268, 1), new Item(8896, 1), BattlePassPages.PAGE_FOUR),
	Level33(33, 9_250_000, new Item(13269, 1), new Item(8897, 1), BattlePassPages.PAGE_FIVE),
	Level34(34, 9_500_000, new Item(13270, 1), new Item(8898, 1), BattlePassPages.PAGE_FIVE),
	Level35(35, 9_750_000, new Item(13271, 1), new Item(8899, 1), BattlePassPages.PAGE_FIVE),
	Level36(36, 10_000_000, new Item(5022, 40000), new Item(5022, 250000), BattlePassPages.PAGE_FIVE),
	Level37(37, 10_250_000, new Item(11423, 1), new Item(11423, 2), BattlePassPages.PAGE_FIVE),
	Level38(38, 10_500_000, new Item(7124, 3), new Item(7124, 6), BattlePassPages.PAGE_FIVE),
	Level39(39, 10_750_000, new Item(455, 3), new Item(455, 6), BattlePassPages.PAGE_FIVE),
	Level40(40, 11_000_000, new Item(13733, 1), new Item(20430, 1), BattlePassPages.PAGE_FIVE),
	;
	
	@Getter
	private int level;
	@Getter
	private int exp;
	@Getter
	private Item bronzeReward, goldReward;
	@Getter
	private BattlePassPages page;
	
	BattlePassData(int level, int exp, Item bronzeReward, Item goldReward, BattlePassPages page)
	{
		this.level = (level);
		this.bronzeReward = (bronzeReward);
		this.goldReward = (goldReward);
		this.page = (page);
		this.exp = (exp);
	}
	
	static final Map<Integer, BattlePassData> byId = new HashMap<Integer, BattlePassData>();
	
	static
	{
		for (BattlePassData e : BattlePassData.values())
		{
			if (byId.put(e.getLevel(), e) != null) {
				  throw new IllegalArgumentException("duplicate id: " + e.getLevel());
			}
		}
	}
	
	public static BattlePassData getByLevel(int id) 
	{
		if(byId.get(id) == null) 
		{
			return byId.get(0);
		}
	    return byId.get(id);
	}
}