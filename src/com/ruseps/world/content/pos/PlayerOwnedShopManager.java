package com.ruseps.world.content.pos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruseps.GameSettings;
import com.ruseps.model.GameMode;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.input.Input;
import com.ruseps.net.packet.PacketBuilder;
import com.ruseps.net.packet.ValueType;
import com.ruseps.util.Misc;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.pos.PlayerOwnedShop.Item;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A management class for all player owned shops and information
 * related to a player owned shop on player instance basis.
 * 
 * 
 *
 */
public class PlayerOwnedShopManager {
	
	/**
	 * A collection of all player owned shops ever created.
	 */
	public static final List<PlayerOwnedShop> SHOPS = new ArrayList<>();
	
	/**
	 * The directory for the player owned shops
	 */
	public static final File DIRECTORY = new File("./data/shops/");
	
	/**
	 * A reference to the player instance.
	 */
	private Player player;

	/**
	 * The current player owned shop being visited by the player.
	 */
	private PlayerOwnedShop current;
	
	/**
	 * The player owned shop owned by the player relative to the
	 * current {@link PlayerOwnedShopManager} instance.
	 */
	private PlayerOwnedShop myShop;
	
	/**
	 * A collection of the shops filtered for this player's instance.
	 */
	private List<PlayerOwnedShop> filtered = new ArrayList<>();
	
	/**
	 * The string we are using to filter through all of the player
	 * owned shops.
	 */
	private String filterString = "";
	
	/**
	 * The available earnings the player has made from his shop.
	 */
	private long earnings = 0;
	
	/**
	 * Construct a new {@code PlayerOwnedShopManager} {@code Object}.
	 * @param player The reference to the player owning this instance.
	 */
	public PlayerOwnedShopManager(Player player) {
		this.player = player;
	}

	/**
	 * Open the player owned shop management interface.
	 */
	public void open() {
		if(player.getGameMode() == GameMode.HARDCORE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN || player.getGameMode() == GameMode.IRONMAN) {
			player.sendMessage("@red@<img=10>Iron Men are not allowed to use the POS " + player.getUsername());
			return;
		}
      	player.getPacketSender().sendString(32610, "Player Owned Shops");
		
		int i = 0;
		filtered.clear();
		
		for(PlayerOwnedShop shop : SHOPS) {
			
			if(shop != null && shop.size() > 0) {
				player.getPacketSender().sendString(32623 + (i++), shop.ownerName());
				filtered.add(shop);
			}
			
		}
		
		for(; i < 100; i++){
			player.getPacketSender().sendString(32623 + i, "");
		}
		
		PlayerOwnedShop.resetItems(player);
		player.getPacketSender().sendInterface(32600);
		
	}
	
	public void options() {
		
		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public DialogueExpression animation() {
				return null;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Visit somebody elses shop", "Manage my own shop", "Claim earnings", "Cancel" };
			}
			
			@Override
			public void specialAction() {
				player.setDialogueActionId(101);
			}
			
		});
		
	}
	
	/**
	 * Open the interface to edit your own shop.
	 */
	public void openEditor() {
		
		if(myShop == null) {
			newShop(myShop = new PlayerOwnedShop());
		}
		
		(current = myShop).refresh(player, true);
		refreshInventory();
		
	}
	
	/**
	 * Create a new player owned shop and assign the current
	 * player as owner of this shop.
	 * @param shop A reference to the player owned shop.
	 */
	public void newShop(PlayerOwnedShop shop) {
		shop.setUsername(player.getUsername());
		SHOPS.add(shop);
	}
	
	/**
	 * Refresh the inventory of the player owning this player
	 * owned shop management instance.
	 */
	public void refreshInventory() {
		
		for(int i = 0; i < player.getInventory().getItems().length; i++) {
			
			int id = player.getInventory().getItems()[i].getId(),
				amount = player.getInventory().getItems()[i].getAmount();
			
			if(id <= 0 && amount <= 0) {
				player.getPacketSender().sendItemOnInterface(37003 + i, -1, 0, 0);
			} else {
				player.getPacketSender().sendItemOnInterface(37003 + i, id, 0, amount);
			}
			
		}
		
		player.getPacketSender().sendItemContainer(player.getInventory(), 37054);
		
		if(player.getSession().getChannel().isOpen() && player != null) {
			PacketBuilder out = new PacketBuilder(248);
			out.putShort(33600, ValueType.A);
			out.putShort(37053);
			player.getSession().queueMessage(out);
		}
		
	}
	
	/**
	 * Handle a button on the management interface for this player.
	 * @param buttonId The button component id.
	 */
	public void handleButton(int buttonId) {
		
		buttonId -= 32623;
		
		boolean f = filtered.size() > 0;
		
		if(buttonId >= (f ? filtered : SHOPS).size()) {
			return;
		}
		
		PlayerOwnedShop shop = (f ? filtered : SHOPS).get(buttonId);
		
		if(shop != null) {
			(current = shop).open(player);
		} else {
			PlayerOwnedShop.resetItems(player);
		}
		
	}
	
	/**
	 * Handle the action to buy an item from a player owned shop for
	 * the player this management instance is relative to.
	 * @param slot The item slot.
	 * @param id The item id.
	 * @param amount The amount he/she would like to buy of this item.
	 */
	public void handleBuy(int slot, int id, int amount) {
		
		if(current == null) {
			return;
		}
		
		Item item = current.getItem(slot);
		
		if(item == null) {
			return;
		}
		
		ItemDefinition definiton = item.getDefinition();
		
		if(amount == -1) {
			if(definiton != null) {
				String formatPrice = Misc.sendCashToString(item.getPrice());
				player.sendMessage("<col=FF0000>"+definiton.getName()+"</col> costs "+formatPrice+" Upgrade Tokens each <col=FF0000>"+current.ownerName()+"</col>'s shop.");
			}
			return;
		}
		
		if(current == myShop) {
			player.sendMessage("You cannot buy items from your own shop.");
			return;
		}
		
		if(Arrays.stream(GameSettings.UNSELLABLE_ITEMS).anyMatch(i -> i == item.getId())) {
			player.sendMessage("You cannot buy "+definiton.getName()+" from this shop.");
			return;
		}
		
		/*if (player.getAchievements().isAchievementItem(id)) {
			player.sendMessage("You cannot buy an achievement reward item!");
			return;
		}*/
		
		for (int i : GameSettings.UNTRADEABLE_ITEMS) {
			if (i == id) {
				player.sendMessage("You can't trade this item.");
				return;
			}
		}
		
		if(item.getDefinition().getId() == 13280 || item.getDefinition().getId() == 13329 || item.getDefinition().getId() == 13337) {
			return;
		}
		
		/*if(ClueDifficulty.isClue(id)) {
			player.sendMessage("You cannot purchase clue scrolls!");
			return;
		}*/
		
		long coins = player.getInventory().getAmount(19992);
		
		if(((long) amount * item.getPrice()) > coins) {
			
			amount = (int) Math.round((double) (((double) coins / item.getPrice()) - 0.3));
			
			//System.out.println("new amount: "+amount);
			
			if(amount < 0) {
				amount = 0;
			}
			
		}
		
		if(amount == 0) {
			player.sendMessage("You do not have enough coins in your inventory.");
		} else {
			
			if(amount >= item.getAmount()) {
				amount = item.getAmount();
			}
			
			if(!item.getDefinition().isStackable() || (item.getDefinition().isStackable() && !player.getInventory().contains(item.getId()))) {
				if(player.getInventory().getFreeSlots() == 0) {
					player.sendMessage("Not enough inventory space.");
					return;
				} else if(player.getInventory().getFreeSlots() < amount) {
					amount = item.getDefinition().isStackable() ? amount : player.getInventory().getFreeSlots();
				}
			} else {
				
				int inventoryAmount = player.getInventory().getAmount(id);
				
				if((long) (amount + inventoryAmount) > Integer.MAX_VALUE) {
					amount = (int) (Integer.MAX_VALUE - inventoryAmount);
					PlayerLogs.log(player.getUsername(), "Purchasing item from POS: "+ItemDefinition.forId(id)+", amount: "+item.getAmount());
				}
				
			}
			
			coins = player.getInventory().getAmount(19992);
			long total = (long) item.getPrice() * amount;
			
			if(coins >= total) {
			
				int removed = current.remove(slot, amount);
				int inventory = player.getInventory().getAmount(19992);
				
				if(inventory > total) {
					inventory = (int) total;
				}
				
				player.getInventory().delete(19992, inventory);
				
				total -= inventory;
				
				if(total > 0) {
					player.getPacketSender().sendString(8135, "");
				}
				
				player.getInventory().add(item.getId(), removed);
				current.addMoney(item.getPrice() * removed, item);
			
			}
			
		}
		
	}
	
	/**
	 * Handle the withdraw action for this player's own shop. 
	 * @param slot The item slot.
	 * @param id The item id.
	 * @param amount The amount the player would like to withdraw.
	 */
	public void handleWithdraw(int slot, int id, int amount) {
		
		if(current != myShop) {
			return;
		}
		
		Item item = current.getItem(slot);
		ItemDefinition definiton = item.getDefinition();
		
		if(amount == -1) {
			if(definiton != null) {
				String formatPrice = Misc.sendCashToString(item.getPrice());
				player.sendMessage("<col=FF0000>"+definiton.getName()+"</col> is set to cost "+formatPrice+" Upgrade Tokens in your shop.");
			}
			return;
		}
		
		if(amount >= item.getAmount()) {
			amount = item.getAmount();
		}
		
		if(!item.getDefinition().isStackable() || (item.getDefinition().isStackable() && !player.getInventory().contains(item.getId()))) {
			if(player.getInventory().getFreeSlots() == 0) {
				player.sendMessage("Not enough inventory space.");
				return;
			} else if(player.getInventory().getFreeSlots() < amount) {
				amount = item.getDefinition().isStackable() ? amount : player.getInventory().getFreeSlots();
			}
		} else {
			
			int inventoryAmount = player.getInventory().getAmount(id);
			
			if((long) (amount + inventoryAmount) > Integer.MAX_VALUE) {
				amount = (int) (Integer.MAX_VALUE - inventoryAmount);
			}
			
		}
		
		if(amount == 0) {
			player.sendMessage("Not enough inventory space.");
			return;
		}
		
		int removed = current.remove(slot, amount);
		
		player.getInventory().add(item.getId(), removed);
		myShop.refresh(player, true);
		refreshInventory();
		
	}
	
	public void handleStore(int slot, int id, int amount) {
		handleStore(slot, id, amount, -1);
	}
	
	public void handleStore(int slot, int id, int amount, int price) {
		
		if(player.getInventory().get(slot) == null) {
			return;
		}
		
		int itemId = player.getInventory().get(slot).getId();
		int itemAmount = player.getInventory().getAmount(itemId);
		
		if(itemId == id) {
			
			if(id == 995) {
				player.sendMessage("You cannot store money in your shop.");
				return;
			}
			
			ItemDefinition definition = ItemDefinition.forId(itemId);
			
			if(Arrays.stream(GameSettings.UNSELLABLE_ITEMS).anyMatch(i -> i == itemId)) {
				if(definition != null) {
					player.sendMessage("You cannot sell "+definition.getName()+" in your shop.");
				}
				return;
			}
			
			for(int i : GameSettings.UNTRADEABLE_ITEMS) {
				if(i == id) {
					player.sendMessage("You can't trade this item.");
					return;
				}
			}
			
			/*if (Pet.get(id) != null) {
				player.sendMessage("You cannot sell a pet item!");
				return;
			}
			
			if (player.getAchievements().isAchievementItem(id)) {
				player.sendMessage("You cannot trade an achievement reward item!");
				return;
			}
			
			if (ClueDifficulty.isClue(id)) {
				player.sendMessage("You cannot trade clue scrolls!");
				return;
			}*/
			
			if(amount >= itemAmount) {
				amount = itemAmount;
			}
			
			int currentAmount = myShop.getAmount(id);
			
			if(currentAmount == 0 && price == -1) {
				
				final int amount2 = amount;

				player.setInputHandling(new Input() {
					
					@Override
					public void handleAmount(Player player, int value) {
						handleStore(slot, id, amount2, value);
					}
					
				});
				player.getPacketSender().sendEnterAmountPrompt("Enter the price for this item:");
				
				return;
				
			}
			
			if(myShop.size() >= 32) {
				player.sendMessage("Your shop cannot contain any more items.");
				return;
			}
			
			if(currentAmount == Integer.MAX_VALUE) {
				player.sendMessage("You cannot store any more of this item in your shop.");
				return;
			}
			
			long total = ((long) currentAmount + (long) amount);
			
			if(total > Integer.MAX_VALUE) {
				amount = (int) (Integer.MAX_VALUE - currentAmount);
			}
			
			if(price == -1) {
				myShop.add(id, amount);
			} else {
				myShop.add(id, amount, price);
			}
			
			if(amount != 0) {
				if(amount == 1) {
					player.getInventory().delete(id, amount, slot);
				} else {
					player.getInventory().delete(id, amount);
				}
			}
			
			refreshInventory();
			
		}
		
	}
	
	public void setCustomPrice(int slot, int id, int price) {
		
		if(current != myShop) {
			return;
		}
		
		Item item = current.getItem(slot);
		
		if(item == null) {
			return;
		}
		
		ItemDefinition definiton = item.getDefinition();
		
		if(price > 0 && price <= Integer.MAX_VALUE) {
			item.setPrice(price);
			String formatPrice = Misc.sendCashToString(price);
			player.sendMessage("You have set <col=FF0000>"+definiton.getName().toLowerCase()+"</col> to cost <col=FF0000>"+formatPrice+"</col> Upgrade Tokens in your shop.");
			myShop.save();
		}
		
	}
	
	public void hookShop() {
		for(PlayerOwnedShop shop : SHOPS) {
			if(shop == null) {
				continue;
			}
			if(shop.getUsername().equalsIgnoreCase(player.getUsername())) {
				myShop = shop;
				shop.setOwner(player);
				break;
			}
		}
	}
	
	public void unhookShop() {
		
		if(myShop == null) {
			hookShop();
		}
		
		if(myShop != null) {
			myShop.setOwner(null);
		}
		
	}
	
	public void updateFilter(String string) {
		
		filtered.clear();
		
		player.getPacketSender().sendString(32610, string.length() == 0 ? "Player Owned Shops" : "Player Owned Shops - Searching: "+(setFilterString(string)));
		boolean l = string.length() == 0;
		int i = 0;
		
		for(PlayerOwnedShop shop : SHOPS) {
			
			if(shop != null && shop.size() > 0 && (l || shop.contains(string) || shop.getUsername().toLowerCase().contains(string.toLowerCase()))) {
				player.getPacketSender().sendString(32623 + (i++), shop.ownerName());
				filtered.add(shop);
			}
			
		}
		
		for(; i < 100; i++){
			player.getPacketSender().sendString(32623 + i, "");
		}
		
	}
	
	private void statement(Player player, String... messages) {
		
		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return null;
			}

			@Override
			public String[] dialogue() {
				return messages;
			}
			
		});
		
	}
	
	public void claimEarnings() {
		
		if(earnings <= 0) {
			statement(player, "You do not currently have any available earnings.");
			return;
		}
		
		int coins = player.getInventory().getAmount(19992);
		long available = earnings;
		
		if((long) (earnings + coins) > Integer.MAX_VALUE) {
			available = (int) (Integer.MAX_VALUE - coins);
		}
		
		if(available <= 0 || coins == 0 && player.getInventory().getFreeSlots() == 0) {
			statement(player, "There is no available inventory space.");
			return;
		}
		
		earnings -= available;
		
		String formatPrice1 = Misc.sendCashToString(available);
		String formatPrice2 = Misc.sendCashToString(earnings);
		
		player.getInventory().add(19992, (int) available);
		statement(player, "You have claimed "+formatPrice1+" coins. This leaves "+formatPrice2+" coins", "left to claim from your shop earnings depository.");
		PlayerLogs.log(player.getUsername(), "Claiming POS Coins, Amount: "+formatPrice1+" Amount left: "+formatPrice2+" coins");
	}
	
	public static void loadShops() {
		
		File[] files = DIRECTORY.listFiles();
		
		for(File file : files) {
			
			Path path = Paths.get(DIRECTORY + File.separator, file.getName());
			PlayerOwnedShop shop = new PlayerOwnedShop();
			
			shop.setUsername(file.getName().replaceAll(".txt", ""));
			
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				
				String line;
				int offset = 0;
				
				while((line = reader.readLine()) != null) {
					
					String[] split = line.split(" - ");
					
					if(split.length == 3) {
						
						int id = Integer.parseInt(split[0]);
						int amount = Integer.parseInt(split[1]);
						int price = Integer.parseInt(split[2]);
						
						shop.getItems()[offset++] = new Item(id, amount, price);
						
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			SHOPS.add(shop);
			
		}
		
	}
	
	public static void saveShops() {
		for(PlayerOwnedShop shop : SHOPS) {
			shop.save();
		}
	}
	
	public PlayerOwnedShop getCurrent() {
		return current;
	}

	public void setCurrent(PlayerOwnedShop current) {
		this.current = current;
	}
	
	public PlayerOwnedShop getMyShop() {
		return myShop;
	}

	public void setMyShop(PlayerOwnedShop myShop) {
		this.myShop = myShop;
	}

	public List<PlayerOwnedShop> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<PlayerOwnedShop> filtered) {
		this.filtered = filtered;
	}

	public String getFilterString() {
		return filterString;
	}

	public String setFilterString(String filterString) {
		this.filterString = filterString;
		return filterString;
	}

	public long getEarnings() {
		return earnings;
	}

	public void setEarnings(long earnings) {
		this.earnings = earnings;
	}
	
	public void addEarnings(int amount) {
		earnings += amount;
	}

	
}