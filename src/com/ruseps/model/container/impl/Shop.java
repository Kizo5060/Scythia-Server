package com.ruseps.model.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.ShopRestockTask;
import com.ruseps.model.GameMode;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.model.container.ItemContainer;
import com.ruseps.model.container.StackType;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.input.impl.EnterAmountToBuyFromShop;
import com.ruseps.model.input.impl.EnterAmountToSellToShop;
import com.ruseps.util.JsonLoader;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.minigames.impl.RecipeForDisaster;
import com.ruseps.world.entity.impl.player.Player;

public class Shop extends ItemContainer {

	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 42)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	private final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public Item getCurrency() {
		return currency;
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	private boolean restockingItems;

	public boolean isRestockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	/**
	 * Opens a shop for a player
	 * 
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		if (player.getGameMode() == GameMode.GROUP_IRONMAN && player.isGim () && id != POTIONS && id != SLAYER_STORE && id != 40 ) {
			player.getPacketSender().sendMessage("Group Ironman players are not allowed to use Shops.");
			return this;
		}
		setPlayer(player);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
		}
	}

	/**
	 * Checks a value of an item in a shop
	 * 
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 995)
			return;
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}
		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
			if (id == PLATEMINI || id == DIAMONDMINI || id == AFK_TREE_STORE || id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == STARDUST_STORE|| id == AGILITY_TICKET_STORE || id == BATTLE_ROYAL_STORE
					|| id == GRAVEYARD_STORE) {
				Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + (int) finalValue + " " + s + "" + shopPriceEx((int) finalValue) + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}
		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if (amountToSell == 0)
			return;
		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "+s+
		 * " at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (getCurrency().getId() > 0) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}
		if (itemValue <= 0)
			return;
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}
		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
					} else {
						// Return points here
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					} else {
						// Return points here
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
		}
		if (customShop) {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
				player.getPacketSender()
						.sendMessage("@red@<img=10>You may only sell items to this Store.");
				return this;
		}
		if (!shopSellsItem(item))
			return this;

		if (getItems()[slot].getAmount() <= 5001 && id != GENERAL_STORE) {

			player.getPacketSender()
					.sendMessage("The shop can't be below 5,000 items and needs to regenerate some items first..");

		}

		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();

		if (getItems()[slot].getAmount() < amountBuying) {
			amountBuying = getItems()[slot].getAmount() - 101;

		}
		if (amountBuying == 0)
			return this;

		if (amountBuying > 100000) {
			player.getPacketSender().sendMessage(
					"You can only buy 100000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			return this;
		}
		
		PlayerLogs.log(player.getUsername(), "Player bought "+item.getId()+ " x " +item.getAmount()+" from shop");

		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		int playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
			if (currency.getId() == 19994) {
				if (player.getMoneyInPouch() >= value) {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					if (!(player.getInventory().getFreeSlots() == 0
							&& player.getInventory().getAmount(currency.getId()) == value)) {
						usePouch = true;
					}
				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == PLATEMINI || id == DIAMONDMINI || id == AFK_TREE_STORE || id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == STARDUST_STORE || id == AGILITY_TICKET_STORE|| id == BATTLE_ROYAL_STORE
						|| id == GRAVEYARD_STORE) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == PKING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getPkPoints();
			} else if (id == VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
			} else if (id == DUNGEONEERING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
			} else if (id == DONATOR_STORE_1) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == TRIVIA_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getTriviaPoints();
			} else if (id == BOSS_POINT_STORE) {
				playerCurrencyAmount = player.getBossPoints();
			} else if (id == RAIDS1) {
				playerCurrencyAmount = player.getPointsHandler().getRaidsOnePoints();
			} else if (id == DONATOR_STORE_2) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			}	else if (id == DONATOR_STORE_3) {
					playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == PRESTIGE_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getPrestigePoints();
			} else if (id == SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			}
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender()
					.sendMessage("You do not have enough "
							+ ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s")))
							+ " to purchase this item.");
			return this;
		}
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		} else if (id == GAMBLING_STORE) {
			if (item.getId() == 15084 || item.getId() == 299) {
				if (player.getRights() == PlayerRights.PLAYER) {
					player.getPacketSender().sendMessage("You need to be a member to use these items.");
					return this;
				}
			}
		}

		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() < amountBuying) {
				amountBuying = getItems()[slot].getAmount() - 101;

			}

			if (getItems()[slot].getAmount() <= 5001 && id != GENERAL_STORE) {

				player.getPacketSender()
						.sendMessage("The shop can't be below 5,000 items and needs to regenerate some items first...");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(currency.getId(), value, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - value);
						} else if (id == RAIDS1) {
							player.getPointsHandler().setRaidsOnePoints(player.getPointsHandler().getRaidsOnePoints() - value);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value, true);
						} else if (id == DONATOR_STORE_2) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == DONATOR_STORE_3) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						}
					}

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					int canBeBought = playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(currency.getId(), value * canBeBought, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value * canBeBought, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value * canBeBought, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - (value * canBeBought));
						} else if (id == RAIDS1) {
							player.getPointsHandler().setRaidsOnePoints(player.getPointsHandler().getRaidsOnePoints() - (value * canBeBought));
						} else if (id == DONATOR_STORE_2) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_3) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value * canBeBought, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						}
					}
					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					playerCurrencyAmount -= value;
					break;
				} else {
					break;
				}
			}
			amountBuying--;
		}
		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
																							// the
																							// money
																							// pouch
			}
		} else {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 * 
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
			publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 42;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

    @Override
    public ItemContainer delete(Item item, int slot, boolean refresh, ItemContainer toContainer) {
        if (item == null || slot < 0)
            return this;

        boolean leavePlaceHolder = this instanceof Shop && id != GENERAL_STORE;
        if (item.getAmount() > getAmount(item.getId()))
            item.setAmount(getAmount(item.getId()));
        if (item.getDefinition().isStackable() || stackType() == StackType.STACKS) {
            if (toContainer != null && !item.getDefinition().isStackable()
                    && item.getAmount() > toContainer.getFreeSlots() && !(this instanceof Shop))
                item.setAmount(toContainer.getFreeSlots());

            getItems()[slot].setAmount(getItems()[slot].getAmount() - item.getAmount());

            if (getItems()[slot].getAmount() < 1) {
                getItems()[slot].setAmount(0);
                if (!leavePlaceHolder) {
                    getItems()[slot].setId(-1);
                }
            }
        } else {
            int amount = item.getAmount();
            while (amount > 0) {
                if (slot == -1 || (toContainer != null && toContainer.isFull()))
                    break;
                if (!leavePlaceHolder) {
                    getItems()[slot].setId(-1);
                }
                getItems()[slot].setAmount(0);
                slot = getSlot(item.getId());
                amount--;
            }
        }
        if (refresh)
            refreshItems();
        return this;
    }

	@Override
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	public String shopPriceEx(int shopPrice) {
		String ShopAdd = "";
		if (shopPrice >= 1000 && shopPrice < 1000000) {
			ShopAdd = " (" + (shopPrice / 1000) + "K)";
		} else if (shopPrice >= 1000000) {
			ShopAdd = " (" + (shopPrice / 1000000) + " million)";
		}
		return ShopAdd;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public void restockShop() {
		for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
			int currentStockAmount = getItems()[shopItemIndex].getAmount();
			add(getItems()[shopItemIndex].getId(), getOriginalStock()[shopItemIndex].getAmount() - currentStockAmount);
//			publicRefresh();
			refreshItems();
		}

	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE)
			return true;
		if (shopId == DUNGEONEERING_STORE || shopId == BOSS_POINT_STORE || shopId == TRIVIA_STORE || shopId == RAIDS1 || shopId == PLATEMINI
				|| shopId == DIAMONDMINI
				|| shopId == DONATOR_STORE_1 || shopId == DONATOR_STORE_2 || shopId == DONATOR_STORE_3|| shopId == PKING_REWARDS_STORE
				|| shopId == VOTING_REWARDS_STORE || shopId == RECIPE_FOR_DISASTER_STORE|| shopId == BATTLE_ROYAL_STORE
				|| shopId == ENERGY_FRAGMENT_STORE || shopId == AGILITY_TICKET_STORE || shopId == GRAVEYARD_STORE
				|| shopId == TOKKUL_EXCHANGE_STORE || shopId == AFK_TREE_STORE || shopId == PRESTIGE_STORE || shopId == STARDUST_STORE || shopId == SLAYER_STORE)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Map<Integer, Shop> getShops() {
			return shops;
		}

		public static JsonLoader parseShops() {
			return new JsonLoader() {
				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					shops.put(id, new Shop(null, id, name, currency, items));
				}

				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}
			};
		}

		public static Object[] getCustomShopData(int shop, int item) {
			if (shop == VOTING_REWARDS_STORE) {
				switch (item) {
				case 6183:
					return new Object[] { 10, "Voting Points" };
				case 18338: 
					return new Object[] { 15, "Voting Points" };
				case 915:
					return new Object[] { 50, "Voting Points" };
				case 21055:
					return new Object[] { 100, "Voting Points" };
				case 21056: 
					return new Object[] { 400, "Voting Points" };
				case 10835:
					return new Object[] { 50, "Voting Points" };
				case 19888:
					return new Object[] { 100, "Voting Points" };
				case 6500:
					return new Object[] { 125, "Voting points" };
					
				case 10996:
				case 10997:
				case 10998:
				case 10999:
					
					return new Object[] { 150, "Voting points" };
		
				case 928:
				case 926:
				case 929:
				case 927:
				case 925:
				case 930:
					return new Object[] { 250, "Voting points" };
					
				case 935:
				case 932:
				case 936:
				case 934:
				case 931:
				case 933:
					return new Object[] { 300, "Voting Points" };
					
				case 923:
				case 920:
				case 922:
				case 921:
				case 924:
				case 919:
					return new Object[] {350, "Voting Points" };
					
				case 13663:
					return new Object[] { 6, "Voting points" };
				}
			} else if (shop == GENERAL_STORE) {
				switch (item) {
				case 21071:
					return new Object[] { 45, "NexArch Coins" };
				}
			} else if (shop == PKING_REWARDS_STORE) {
				switch (item) {
				case 6918:
				case 6914:
				case 6889:
				case 3535:
				case 13896:
					return new Object[] { 5, "Pk points" };
				case 13887:
				case 13893:
					return new Object[] { 30, "Pk points" };
					
				case 10550:
					return new Object[] { 12, "Pk points" };
				case 11283:
					return new Object[] { 25, "Pk points" };
				case 15019:
					return new Object[] { 20, "Pk points" };

				case 20000:
				case 20001:
				case 20002:
			
					return new Object[] { 25, "Pk points" };
				case 13884:
				case 13890:
					return new Object[] { 25, "Pk points" };
				case 4706:
					return new Object[] { 45, "Pk points" };
				case 20555:
					return new Object[] { 55, "Pk points" };
				case 6916:
					return new Object[] { 8, "Pk points" };
				case 6924:
					return new Object[] { 6, "Pk points" };
				case 6920:
				case 6922:
					return new Object[] { 4, "Pk points" };
				case 2581:
					return new Object[] { 5, "Pk points" };
				case 11730:
					return new Object[] { 25, "Pk points" };
				case 12601:
					return new Object[] { 25, "Pk points" };
				case 2577:
					return new Object[] {5, "Pk points" };
				case 15486:
					return new Object[] { 20, "Pk points" };
				case 19111:
					return new Object[] { 30, "Pk points" };
				case 13879:
				case 13883:
				case 15243:
				case 15332:
					return new Object[] { 1, "Pk points" };
				case 15241:
				case 17273:
					return new Object[] { 25, "Pk points" };
				case 10548:
				case 10547:
				case 10551:
					return new Object[] { 12, "Pk points" };
				case 6570:

				case 11235:
				case 4151:
				case 13262:
				case 20072:
					return new Object[] { 8, "Pk points" };

				case 11696:
				case 11698:
				case 11700:
					return new Object[] { 35, "Pk points" };
				case 11728:
				case 15018:
				case 15020:
				case 15220:
					return new Object[] { 20, "Pk points" };
				case 11694:
				case 19780:
				case 14484:
					return new Object[] { 65, "Pk points" };
				}
			} else if (shop == BATTLE_ROYAL_STORE) {
				switch (item) {
				case 19039:
					return new Object[] { 500, "Battle Royal Points" };
				case 19040:
				case 19041:
				case 19042:
				case 19043:
				case 19044:
					return new Object[] { 450, "Battle Royal Points" };
				case 19045:
				case 19046:
				case 19047:
					return new Object[] { 600, "Battle Royal Points" };
				}
			} else if (shop == STARDUST_STORE) {
				switch (item) {
				case 18831:
					return new Object[] { 5, "stardust" };
				
				case 6924:
				case 6916:
				case 6918:
					return new Object[] { 100, "stardust" };

				case 6585:
				case 2581:
				case 2577:
					return new Object[] { 200, "stardust" };

				case 15259:	
				case 13661:
					return new Object[] { 350, "stardust" };
				case 2417:
				case 2415:
				case 2416:
					return new Object[] { 500, "stardust" };
				
				case 11704:
				case 11706:
				case 11708:
				
					return new Object[] { 650, "stardust" };
				case 11710:
				case 11712:
				case 11714:
					return new Object[] { 120, "stardust"};
				case 15606:
				case 15608:
				case 15610:
				case 15612:
				case 15614:
				case 15616:
				case 15618:
				case 15620:
				case 15622:
					return new Object[] { 500, "stardust" };
				}
			} else if (shop == ENERGY_FRAGMENT_STORE) {
				switch (item) {
				case 5509:
					return new Object[] { 400, "energy fragments" };
				case 5510:
					return new Object[] { 750, "energy fragments" };
				case 5512:
					return new Object[] { 1100, "energy fragments" };
				case 13625:
				case 13626:
				case 13627:
				case 13628:
					return new Object[] { 2500, "energy fragments" };
						
					
				}
			} else if (shop == RAIDS1) {
				switch (item) {
				case 2774:
					return new Object[] { 750, "Raid Points" }; 
				case 2778:
					return new Object[] { 750, "Raid Points" };
				case 2780:
					return new Object[] { 750, "Raid Points" };
				case 2776:
					return new Object[] { 750, "Raid Points" };
				case 2782:
					return new Object[] { 1000, "RaidPoints" };
				case 21056:
					return new Object[] { 5000, "Raid Points" };
				case 21055:	
					return new Object[] { 3500, "Raid Points" };
				case 6855:
					return new Object[] { 2500, "Raid Points" };
				case 6856:
					return new Object[] { 3000, "Raid Points" };
				case 11724:
					return new Object[] { 5500, "Raid Points" };
				case 11726:
					return new Object[] { 5500, "Raid Points" };
				case 11718:
					return new Object[] { 5500, "Raid Points" };
				case 11720:
					return new Object[] { 5500, "Raid Points" };
				case 11722:
					return new Object[] { 5500, "Raid Points" };
				case 3486:
					return new Object[] { 5500, "Raid Points" };
				case 3481:
					return new Object[] { 5500, "Raid Points" };
				case 3483:
					return new Object[] { 5500, "Raid Points" };
				case 3485:
					return new Object[] { 5500, "Raid Points" };
				case 12002:
					return new Object[] { 10500, "Raid Points" };
				case 12502:
					return new Object[] { 10500, "Raid Points" };
					
				}
			} else if (shop == BOSS_POINT_STORE) {
				switch (item) {
				case 669:
					return new Object[] { 15000, "Boss Points"};
				case 952:
					return new Object[] { 10, "Boss Points"};
				case 18902:
					return new Object[] { 40000, "Boss Points"};
				case 18904:
					return new Object[] { 40000, "Boss Points"};
				case 14010:
					return new Object[] { 40000, "Boss Points"};
				case 19111:
					return new Object[] { 40000, "Boss Points"};
				case 19990:
					return new Object[] { 300, "Boss Points"};
				case 20002:
					return new Object[] { 40000, "Boss Points"};
				case 7028:
					return new Object[] { 40000, "Boss Points"};
				case 12502:
					return new Object[] { 35000, "Boss Points"};
				case 18337:
					return new Object[] { 2500, "Boss Points"};
				case 1503:
					return new Object[] { 150000, "Boss Points"};
				case 21055:
					return new Object[] { 35000, "Boss Points"};
				case 21056:
					return new Object[] { 75000, "Boss Points"};	
				case 1002:
					return new Object[] { 55000, "Boss Points"};		
				case 19888:
					return new Object[] { 700, "Boss Points" };
				case 11948:
					return new Object[] { 5500, "Boss Points" };
				case 11949:
					return new Object[] { 15000, "Boss Points" };
				case 19868:
					return new Object[] { 4000, "Boss Points" };
				case 1685:
					return new Object[] { 2000, "Boss Points" };
				case 11991:
					return new Object[] { 3500, "Boss Points" };
				case 11896:
					return new Object[] { 500, "Boss Points" };
				case 18984:
					return new Object[] { 7500, "Boss Points" };
				case 18985:
					return new Object[] { 2500, "Boss Points" };
				case 18986:
					return new Object[] { 750, "Boss Points" };
				case 18989:
					return new Object[] { 2800, "Boss Points" };
				case 18987:
					return new Object[] { 2800, "Boss Points" };
				}
				return new Object[] { 1500, "Boss Points" };
			} else if (shop == DONATOR_STORE_1) {
				switch (item) {
				case 15332:
					return new Object[] { 2, "Donation Points" };	
				case 11142:
					return new Object[] { 180, "Donation Points" };
				case 11143:
					return new Object[] { 180, "Donation Points" };
				//
				case 11144:
					return new Object[] { 180, "Donation Points" };
				case 11145:
					return new Object[] { 180, "Donation Points" };
				case 11146:
					return new Object[] { 180, "Donation Points" };
				case 11978:
					return new Object[] { 500, "Donation Points" };	
				case 9850:
					return new Object[] { 600, "Donation Points" };	
				case 19972:
					return new Object[] { 140, "Donation Points" };
				case 19976:
					return new Object[] { 140, "Donation Points" };
				//
				case 19974:
					return new Object[] { 140, "Donation Points" };
				case 19970:
					return new Object[] { 140, "Donation Points" };
				case 19978:
					return new Object[] { 140, "Donation Points" };
				case 1002:
					return new Object[] { 250, "Donation Points" };
				case 996:
					return new Object[] { 500, "Donation Points" }; 
				}

			} else if (shop == DONATOR_STORE_2) {
				switch (item) {
				case 621:
					return new Object[] { 5, "Donation Points" };
				case 10942:
					return new Object[] { 30, "Donation Points" };
				case 10934:
					return new Object[] { 60, "Donation Points" };
				case 10935:
					return new Object[] { 150, "Donation Points" };
				case 10943:
					return new Object[] { 300, "Donation Points" };
					//
				case 18984:
					return new Object[] { 400, "Donation Points" };
				case 18985:
					return new Object[] { 120, "Donation Points" };
				case 18989:
					return new Object[] { 200, "Donation Points" };
				case 18987:
					return new Object[] { 200, "Donaton Points" };
					//
				case 1685:
					return new Object[] { 80, "Donation Points" };
					//
				case 18986:
					return new Object[] { 120, "Donation Points" };
				case 11949:
					return new Object[] { 400, "Donation Points" };
				case 11896:
					return new Object[] { 40, "Donation Points" };
				case 18338: 
					return new Object[] { 20, "Donation Points" };
				case 20656:
					return new Object[] { 60, "Donation Points" };
				case 2545: 
					return new Object[] { 60, "Donation Points" };
				case 666: 
					return new Object[] { 500, "Donation Points" };	
				case 1215: 
					return new Object[] { 1, "Donation Points" };		
				case 938:
					return new Object[] { 150, "Donation Points" };
				}
			} else if (shop == DONATOR_STORE_3) {
				switch (item) {
				case 6830:
					return new Object[] { 650, "Donation Points" };
				case 6828:
					return new Object[] { 750, "Donation Points" };
				case 6832:
					return new Object[] { 800, "Donation Points" };
				case 6833:
					return new Object[] { 2000, "Donation Points" };
				case 7960:
					return new Object[] { 1000, "Donation Points" };
				case 8610:
					return new Object[] { 350, "Donation Points" };
				case 9850:
					return new Object[] { 700, "Donation Points" };
				case 15246:
					return new Object[] { 600, "Donation Points" };
				case 19671:
					return new Object[] { 3000, "Donation Points" };
				case 6829:
					return new Object[] { 750, "Donation Points" };
				case 1000:
					return new Object[] { 100, "Donation Points" };
				case 19990:
					return new Object[] { 100, "Donation Points" };
				case 1005:
					return new Object[] { 250, "Donation Points" };
				case 965:
					return new Object[] { 500, "Donation Points" };
				case 1002:
					return new Object[] { 300, "Donation Points" };
				case 1007:
					return new Object[] { 500, "Donation Points" };
				case 10482:
					return new Object[] { 300, "Donation Points" };
				case 1413:
					return new Object[] { 4500, "Donation Points" };
				case 669:
					return new Object[] { 500, "Donation Points" };	
				case 2572:
					return new Object[] { 30, "Donation Points" };
				}
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
				case 14936:
				case 14938:
					return new Object[] { 60, "agility tickets" };
				case 10941:
				case 10939:
				case 10940:
				case 10933:
					return new Object[] { 20, "agility tickets" };
				case 13661:
					return new Object[] { 100, "agility tickets" };
				}
			} else if (shop == GRAVEYARD_STORE) {
				switch (item) {
				case 18337:
					return new Object[] { 350, "zombie fragments" };
				case 10551:
					return new Object[] { 500, "zombie fragments" };
				case 10548:
				case 10549:
				case 10550:
					return new Object[] { 200, "zombie fragments" };
				case 7592:
				case 7593:
				case 7594:
				case 7595:
				case 7596:
					return new Object[] { 25, "zombie fragments" };
				case 15241:
					return new Object[] { 1500, "zombie fragments" };
				case 15243:
					return new Object[] { 2, "zombie fragments" };
				}
			} else if (shop == TOKKUL_EXCHANGE_STORE) {
				switch (item) {
				case 10480:
					return new Object[] { 100000, "tokkul" };
				case 15332: 
					return new Object[] { 500, "tokkul" };
				case 1035:
					return new Object[] { 550000, "tokkul" };
				case 1031:
					return new Object[] { 550000, "tokkul" };
				case 1033:
					return new Object[] { 550000, "tokkul" };
				case 10504:
					return new Object[] { 1000000, "tokkul" };
				case 964:
					return new Object[] { 2000000, "tokkul" };
				case 8666:
					return new Object[] { 200000, "tokkul" };
				
				}
			} else if (shop == PLATEMINI) {
				switch (item) {
				case 7236:
					return new Object[] { 1000000, "Plat Mini Game Token"};
				case 10835:
					return new Object[] { 50000, "Plat Mini Game Token" };
				case 15332: 
					return new Object[] { 200, "Plat Mini Game Token" };
				case 2765:
					return new Object[] { 550000, "Plat Mini Game Token" };
				case 2766:
					return new Object[] { 550000, "Plat Mini Game Token" };
				case 2767:
					return new Object[] { 550000, "Plat Mini Game Token" };
				case 3490:
					return new Object[] { 300000, "Plat Mini Game Token" };
				case 3491:
					return new Object[] { 300000, "Plat Mini Game Token" };
				case 3492:
					return new Object[] { 300000, "Plat Mini Game Token" };
				case 3493:
					return new Object[] { 300000, "Plat Mini Game Token" };
				case 3494:
					return new Object[] { 300000, "Plat Mini Game Token" };
				case 8666:
					return new Object[] { 200000, "Plat Mini Game Token" };
				
				}
			} else if (shop == DIAMONDMINI) {
				switch (item) {
					case 7236:
						return new Object[] { 1000000, "Diamond Mini Game Token"};
					case 10835:
						return new Object[] { 50000, "Diamond Mini Game Token" };
					case 15332:
						return new Object[] { 200, "Diamond Mini Game Token" };
					case 13017:
						return new Object[] { 550000, "Diamond Mini Game Token" };
					case 13018:
						return new Object[] { 550000, "Diamond Mini Game Token" };
					case 13019:
						return new Object[] { 550000, "Diamond Mini Game Token" };
					case 13010:
						return new Object[] { 300000, "Diamond Mini Game Token" };
					case 13011:
						return new Object[] { 300000, "Diamond Mini Game Token" };
					case 13012:
						return new Object[] { 300000, "Diamond Mini Game Token" };
					case 13013:
						return new Object[] { 300000, "Diamond Mini Game Token" };
					case 13014:
						return new Object[] { 300000, "Diamond Mini Game Token" };

				}
			} else if (shop == AFK_TREE_STORE) {
				switch (item) {
				case 21055:
					return new Object[] {125000, "AFK Ticket"};
				case 18782:
					return new Object[] {5000, "AFK Ticket"};
				case 19888:
					return new Object[] {10000, "AFK Ticket"};
				case 11981:
					return new Object[] { 17000, "AFK Ticket" };
				case 11982:
					return new Object[] { 17000, "AFK Ticket" };
				case 11983:
					return new Object[] { 17000, "AFK Ticket" };
				case 11984:
					return new Object[] { 18000, "AFK Ticket" };
				case 11987:
					return new Object[] { 30000, "AFK Ticket" };
				case 11988:
					return new Object[] { 32000, "AFK Ticket" };
				case 11989:
					return new Object[] { 35000, "AFK Ticket" };
				case 11990:
					return new Object[] { 37500, "AFK Ticket" };
				case 11993:
					return new Object[] { 50000, "AFK Ticket" };
				case 11994:
					return new Object[] { 55000, "AFK Ticket" };
				case 19341:
					return new Object[] { 50000, "AFK Ticket" };
				case 19342:
					return new Object[] { 50000, "AFK Ticket" };
				case 19343:
					return new Object[] { 50000, "AFK Ticket" };
				case 19345:
					return new Object[] { 50000, "AFK Ticket" };
				case 13462:
					return new Object[] { 50000, "AFK Ticket" };
				case 19314:
					return new Object[] { 35000, "AFK Ticket" };
				case 19317:
					return new Object[] { 50000, "AFK Ticket" };
				case 19320:
					return new Object[] { 50000, "AFK Ticket" };
				case 19308:
					return new Object[] { 50000, "AFK Ticket" };
				case 19311:
					return new Object[] { 50000, "AFK Ticket" };
				case 10350:
					return new Object[] { 50000, "AFK Ticket" };
				case 10348:
					return new Object[] { 50000, "AFK Ticket" };
				case 10346:
					return new Object[] { 50000, "AFK Ticket" };
				case 13555:
					return new Object[] { 50000, "AFK Ticket" };
				}
			} else if (shop == DUNGEONEERING_STORE) {
				switch (item) {
				case 18351:
				case 18349:
				case 18353:
				case 18357:
				case 18355:
				case 18359:
				case 18361:
				case 18363:
					return new Object[] { 200000, "Dungeoneering tokens" };
				case 19669:
					return new Object[] { 250000, "Dungeoneering tokens" };

				case 18346:
	
					return new Object[] { 150000, "Dungeoneering tokens" };
				case 18782:
					return new Object[] { 50000, "Dungeoneering tokens" };
				case 18337:
					return new Object[] { 50000, "Dungeoneering tokens" };
			
				case 16955:
				case 16425:
				case 16909:
					return new Object[] { 1500000, "Dungeoneering tokens" };
				case 18335:
				case 6500:
					return new Object[] { 75000, "Dungeoneering tokens" };
				}
			} else if (shop == TRIVIA_STORE) {
				switch (item) {
				case 18782:
					return new Object[] { 10, "Trivia Points" };
				case 10025:
					return new Object[] { 20, "Trivia Points" };
				case 21055:
					return new Object[] { 1000, "Trivia Points" };
				case 915:
					return new Object[] { 500, "Trivia Points" };
				case 18338:
					return new Object[] { 200, "Trivia Points" };
				case 1044:		
				case 1038: 	
				case 1040:				
				case 1042:				
				case 1046:
				case 1048:
					return new Object[] { 50, "Trivia Points" };
				case 14045:
				case 14048:
				case 14047:
				case 14046:
				case 14044:
					return new Object[] { 150, "Trivia Points" };
				}
			} else if (shop == PRESTIGE_STORE) {
				switch (item) {
				case 15501:
					return new Object[] { 20, "Prestige Points" };
				case 18338:
					return new Object[] { 100, "Prestige Points" };
				case 915:
					return new Object[] { 250, "Prestige Points" };
				case 21055:
					return new Object[] { 500, "Prestige Points" };
				case 2846:
				case 2700:
				case 2690:
				case 2831:
					return new Object[] { 150, "Prestige Points" };
				case 938:
					return new Object[] { 400, "Prestige Points" };
			
				}
			} else if (shop == SLAYER_STORE) {
				switch (item) {
				case 15501: 
					return new Object[] { 30, "Slayer Points" };
				case 10835:
					return new Object[] { 25000, "Slayer Points" };
				case 17654:
					return new Object[] { 25000, "Slayer points" };	
				case 915:
					return new Object[] { 6000, "Slayer points" };
				case 19992:
					return new Object[] { 50, "Slayer points" };
				case 7236:
					return new Object[] { 75000, "Slayer points" };
				case 19647:
					return new Object[] { 20000, "Slayer points" };
				case 10876:
					return new Object[] { 20000, "Slayer points" };	
				case 18337:
					return new Object[] { 2500, "Slayer points" };	
				case 19990:
					return new Object[] { 100, "Slayer points" };	
				case 2709:
					return new Object[] { 10000, "Slayer points" };		
				case 1002:
					return new Object[] { 75000, "Slayer points" };		
				case 19888:
					return new Object[] { 500, "Slayer points" };	
				case 2572:
					return new Object[] { 300, "Slayer points" };
				case 19087:
				case 19088:
				case 19089:
				case 19090:
				case 19091:
					return new Object[] { 400, "Slayer points" };
				case 19092:
					return new Object[] { 750, "Slayer points" };
				}
			}
			return null;
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/*
	 * Declared shops
	 */

	public static final int DONATOR_STORE_1 = 48;
	public static final int DONATOR_STORE_2 = 49;
	public static final int DONATOR_STORE_3 = 57;
	public static final int TRIVIA_STORE = 50;

	public static final int GENERAL_STORE = 12;
	public static final int RECIPE_FOR_DISASTER_STORE = 36;
	public static final int POTIONS = 6;

	private static final int VOTING_REWARDS_STORE = 27;
	private static final int PKING_REWARDS_STORE = 26;
	private static final int BATTLE_ROYAL_STORE = 81;
	private static final int ENERGY_FRAGMENT_STORE = 33;
	private static final int AGILITY_TICKET_STORE = 39;
	private static final int GRAVEYARD_STORE = 42;
	private static final int TOKKUL_EXCHANGE_STORE = 43;
	private static final int AFK_TREE_STORE = 80;
	private static final int SKILLCAPE_STORE_1 = 8;
	private static final int SKILLCAPE_STORE_2 = 9;
	private static final int SKILLCAPE_STORE_3 = 10;
	private static final int GAMBLING_STORE = 41;
	private static final int DUNGEONEERING_STORE = 44;
	private static final int PRESTIGE_STORE = 46;
	public static final int BOSS_POINT_STORE = 92;
	private static final int SLAYER_STORE = 47;
	public static final int STARDUST_STORE = 55;
	public static final int RAIDS1 = 100;
	public static final int PLATEMINI = 101;
	public static final int DIAMONDMINI =102;
}
