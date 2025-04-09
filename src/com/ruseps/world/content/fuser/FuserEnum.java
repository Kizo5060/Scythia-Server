package com.ruseps.world.content.fuser;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.world.entity.impl.player.Player;

public enum FuserEnum {
	// WEP
	THANOS(1, FuserType.WEAPON, 13080, 100, 15, 10000, -1,
			new Item[] { new Item(20656, 3), new Item(8788, 15), new Item(19992, 2500), new Item(19994, 1500),}),
	OBBY(2, FuserType.WEAPON, 13081, 100, 15, 10000, -1,
			new Item[] { new Item(21071, 2), new Item(8788, 15), new Item(19992, 2500), new Item(19994, 1500)}),
	RANGEMASTERBOW(3, FuserType.WEAPON, 2680, 100, 15, 10000, -1,
			new Item[] { new Item(4081, 1), new Item(2690, 3), new Item(2787, 2), new Item(1013, 1),
					new Item(19994, 2500000) }),
	ZOMBIEKILLER(4, FuserType.WEAPON, 13023, 100, 15, 10000, -1,
			new Item[] { new Item(13022, 1), new Item(3510, 1), new Item(2700, 1), new Item(2786, 1),
					new Item(14639, 100), new Item(19992, 50000), new Item(19994, 2500000) }),

	// ARMOR
	MELEESHIELD(1, FuserType.ARMOR, 13742, 100, 15, 10000, -1,
			new Item[] { new Item(2831, 2), new Item(17668, 2), new Item(17666, 2), new Item(17656, 2),
					new Item(17658, 2), new Item(17652, 2), new Item(19994, 750000), new Item(19992, 25000) }),

	BFGOFFHAND(2, FuserType.ARMOR, 4081, 100, 15, 10000, -1,
			new Item[] { new Item(2690, 2), new Item(2831, 2), new Item(3526, 1), new Item(3495, 1),
					new Item(19992, 50000), new Item(19994, 2500000)}),
	RAYGUNOFF(3, FuserType.ARMOR, 13022, 100, 15, 10000, -1,
			new Item[] { new Item(2831, 2), new Item(9077, 2), new Item(17654, 2), new Item(17660, 2),
					new Item(17664, 2), new Item(17662, 2), new Item(19994, 750000), new Item(19992, 25000) }),
	GODOFSLAYERCAPE(4, FuserType.ARMOR, 10598, 100, 15, 10000, -1,
			new Item[] { new Item(13029, 1), new Item(2505, 150), new Item(2507, 150), new Item(18653, 50),
					new Item(14639, 50), new Item(1808, 150), new Item(19994, 1000000) }),
	OWNEROFSLAYER(5, FuserType.ARMOR, 2728, 100, 15, 10000, -1,
			new Item[] { new Item(10598, 1), new Item(1413, 1), new Item(2788, 1), new Item(18653, 100),
					new Item(14639, 100), new Item(2951, 50), new Item(19994, 2500000) }),

	// MISC
	BOOSTER(1, FuserType.MISC, 18941, 100, 15, 10000, -1,
			new Item[] { new Item(18943, 2), new Item(18947, 2), new Item(18948, 2), new Item(8788, 50), }),
	BURNINGAMMY(2, FuserType.MISC, 1002, 100, 15, 10000, -1,
			new Item[] { new Item(20658, 1), new Item(20659, 1), new Item(20660, 1), new Item(20657, 2),
					new Item(8788, 1), new Item(19888, 1), }),
	GODAMMY(3, FuserType.MISC, 20657, 100, 15, 10000, -1,
			new Item[] { new Item(20658, 1), new Item(20659, 1), new Item(20660, 1), }),
	GODROW(4, FuserType.MISC, 2709, 100, 15, 10000, -1,
			new Item[] { new Item(2572, 1), new Item(10480, 1), new Item(11005, 1), new Item(8788, 10), }),
	EXTRAHANDS(5, FuserType.MISC, 12437, 100, 15, 10000, -1,
			new Item[] { new Item(6570, 25), new Item(19992, 25000), new Item(19994, 150000), new Item(8788, 50), }),
	LAMPQ(6, FuserType.MISC, 18782, 50, 15, 10000, -1,
			new Item[] {  new Item(19994, 15000), }),

	;

	private final FuserType type;

	public final int buttonId;

	private int resultItem;

	private float successRate;

	private int otherCurrency;

	private int currencyAmount; // LEVEL REQUIRED

	private Item[] ingredients;

	private int safeItem;

	FuserEnum(int buttonId, FuserType type, int resultItem, float successRate, int otherCurrency, int currencyAmount,
			int safeItem, Item[] ingredients) {
		this.buttonId = buttonId;
		this.type = type;
		this.resultItem = resultItem;
		this.successRate = successRate;
		this.otherCurrency = otherCurrency;
		this.currencyAmount = currencyAmount; // LEVEL REQUIRED
		this.ingredients = ingredients;
		this.safeItem = safeItem;
	}

	public int getButtonId() {
		return buttonId;
	}

	public FuserType getType() {
		return type;
	}

	public int getResultItem() {
		return resultItem;
	}

	public float getSuccessRate() {
		return successRate;
	}

	public int getOtherCurrency() {
		return otherCurrency;
	}

	public int getCurrencyAmount() {
		return currencyAmount;
	}

	public Item[] getIngredients() {
		return ingredients;
	}

	public int getSafeItem() {
		return safeItem;
	}
}
