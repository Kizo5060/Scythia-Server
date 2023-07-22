package com.ruseps.world.content.well_of_voting.global_vote_bosses;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class VoteBossOne {

	public static int[] COMMONLOOT = {  };
	public static int[] RARELOOT = { };
	public static int[] SUPERRARELOOT = { };
	public static int[] LEGENDARYLOOT = {  };

	public static Position position = new Position(2334, 9820, 0);

	public static void sequence() {
		NPC npc = new NPC(7553, position);
		World.register(npc);
		World.sendMessage("<img=10>@blu@The First Global Vote Boss has spawned @ ::vboss1 !");
		World.sendMessage("<img=10>@blu@This Boss is a reward for filling the Well of Voting!");
	}

	public static void giveLoot(Player player, NPC npc) {
		// int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		int legendaryrare = LEGENDARYLOOT[Misc.getRandom(LEGENDARYLOOT.length - 1)];

		if (Misc.exclusiveRandom(1, 1250) <= 2) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(legendaryrare),
							new Position(position.getX() + Misc.getRandom(7), position.getY() + Misc.getRandom(4), 0),
							"", true, 200, true, 200));
			String itemName = (new Item(legendaryrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<img=5><col=FF0000>[OMG LEGENDARY!] The First Global Vote Boss dropped " + itemMessage + "!");
		}

		if (Misc.exclusiveRandom(1, 750) <= 3) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare),
							new Position(position.getX() + Misc.getRandom(2), position.getY() + Misc.getRandom(3), 0),
							"", true, 200, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<img=5><col=FF0000>[SUPER RARE] The First Global Vote Boss dropped " + itemMessage + "!");
		}
		// 1 Super rare
		if (Misc.exclusiveRandom(1, 750) <= 3) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(superrare),
							new Position(position.getX() + Misc.getRandom(5), position.getY() + Misc.getRandom(3), 0),
							"", true, 200, true, 200));
			String itemName = (new Item(superrare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<img=5><col=FF0000>[SUPER RARE] The First Global Vote Boss dropped " + itemMessage + "!");
		}
		// 2 rares
		if (Misc.exclusiveRandom(1, 10) == 1) {
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)]),
							new Position(position.getX() + Misc.getRandom(9), position.getY() + Misc.getRandom(7), 0),
							"", true, 200, true, 200));
			GroundItemManager.spawnGroundItem(player,
					new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)]),
							new Position(position.getX() + Misc.getRandom(4), position.getY() + Misc.getRandom(4), 0),
							"", true, 200, true, 200));
			String itemName = (new Item(rare).getDefinition().getName());
			String itemMessage = Misc.anOrA(itemName) + " " + itemName;
			World.sendMessage("<img=5><col=FF0000>[RARE] The First Global Vote Boss dropped " + itemMessage + "!");
		}

		// Commons
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() + Misc.getRandom(10), position.getY() + Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() - Misc.getRandom(8), position.getY() - Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() - Misc.getRandom(6), position.getY() - Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() + Misc.getRandom(12), position.getY() + Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() + Misc.getRandom(8), position.getY() + Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() - Misc.getRandom(6), position.getY() - Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() - Misc.getRandom(12), position.getY() - Misc.getRandom(5), 0), "",
						true, 200, true, 200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(position.getX() + Misc.getRandom(8), position.getY() + Misc.getRandom(5), 0), "",
						true, 200, true, 200));

	}

}
