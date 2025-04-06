package com.ruseps.world.content.fuser;

import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;

public class FuserHandler {

	private final int maxItems = 50;

	private Player player;

	public FuserHandler(Player player) {
		this.player = player;
	}

	private static final FuserEnum[] data = FuserEnum.values();

	public void init() {
		player.setFuserType(FuserType.WEAPON);
		player.getPA().sendFrame126("Fuser Interface", 46105);
	}

	public void openInterface() {
		player.setFuserType(FuserType.WEAPON);
		selectTab(-19422);
		player.getPA().sendInterface(46100);
	}

	public boolean selectTab(int buttonId) {
		switch (buttonId) {
		case -19422:
			player.setFuserType(FuserType.WEAPON);
			loadList();
			return true;

		case -19419:
			player.setFuserType(FuserType.ARMOR);
			loadList();
			return true;

		case -19416:
			player.setFuserType(FuserType.MISC);
			loadList();
			return true;

		case -19434:
			player.getPA().closeAllWindows();
			return true;
		}
		return false;
	}

	public void clearList() {
		for (int i = 46131; i < 46131 + maxItems; i++) {
			player.getPA().sendFrame126("", i);
		}
	}

	public void loadList() {
		clearList();
		int frame = 46131;
		for (FuserEnum data : data) {
			if (data.getType() == player.getFuserType()) {
				player.getPA().sendFrame126(ItemDefinition.forId(data.getResultItem()).getName(), frame++);
				if (frame >= 46131 + maxItems) {
					System.err.println("You are placing a value greater than the max list items");
					return;
				}
			}
		}
	}

	public void clearItems() {
		for (int i = 0; i < 8; i++)
			player.getPA().sendItemOnInterface(46182+i, -1,  -1);
		player.getPA().sendItemOnInterface(46113, -1, -1);
		player.getPA().sendItemOnInterface(46128, -1, 1);
	}

	public void displayItems(int buttonId) {
		clearItems();
		for (FuserEnum data : data) {
			if (data.getType() == player.getFuserType()) {
				if (buttonId == -19405 + data.getButtonId() - 1) {
					for (int i = 0; i < data.getIngredients().length; i++)
						player.getPA().sendItemOnInterface(46182+i, data.getIngredients()[i].getId(),
								data.getIngredients()[i].getAmount());
					player.getPA().sendItemOnInterface(46113, data.getResultItem(), 1);
					player.getPA().sendItemOnInterface(46128, data.getSafeItem(), 1);
				}
			}
		}
	}

	public boolean button(int buttonId) {
		for (FuserEnum data : data) {
			if (data.getType() == player.getFuserType()) {
				if (buttonId == -19405 + data.getButtonId() - 1) {
					player.setCurrentFuse(data);
					// player.sendMessage("Selected: "+data);
					player.getPA().sendFrame126(""/*"Required Upgrade Tokens: " + data.getCurrencyAmount() + ""*/, 46112);
					displayItems(buttonId);
					return true;
				}
			}
		}
		return false;
	}

	public void upgrade() {
		if (player.getCurrentFuse() == null) {
			player.sendMessage("Please select a recipe first.");
		} else {
			for (int i = 0; i < player.getCurrentFuse().getIngredients().length; i++) {
				if (player.getInventory().getAmount(player.getCurrentFuse().getIngredients()[i]
								.getId()) < player.getCurrentFuse().getIngredients()[i].getAmount()) {
					player.sendMessage("You don't have the required ingredients to upgrade "
							+ ItemDefinition.forId(player.getCurrentFuse().getResultItem()).getName() + ".");
					return;
				}
			}
			int randomInt = Misc.random(100) + 1;
			if (randomInt <= player.getCurrentFuse().getSuccessRate()) {
				for (int k = 0; k < player.getCurrentFuse().getIngredients().length; k++) {
					player.getInventory().delete(player.getCurrentFuse().getIngredients()[k].getId(),
							player.getCurrentFuse().getIngredients()[k].getAmount());
				}
			} else {
				for (int k = 0; k < player.getCurrentFuse().getIngredients().length; k++) {
					if (player.getCurrentFuse().getIngredients()[k].getId() != player.getCurrentFuse()
							.getSafeItem())
						player.getInventory().delete(player.getCurrentFuse().getIngredients()[k].getId(),
								player.getCurrentFuse().getIngredients()[k].getAmount());
				}
			}
			player.sendMessage("Congratulations, you successfully fused "
					+ ItemDefinition.forId(player.getCurrentFuse().getResultItem()).getName() + ".");
			player.getInventory().add(player.getCurrentFuse().getResultItem(), 1);
			// }
		}
	}
}
