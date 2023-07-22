package com.ruseps.world.content.dicing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.net.packet.PacketBuilder;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.BankPin;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.entity.impl.player.Player;

public final class Dicing {

	private final Player player;
	private long lastDiceSent;
	private boolean diceRequested;
	private int diceWith = -1;
	private int inDiceWith = -1;
	public boolean inDice;
	private boolean canOffer;
	private int diceStatus;
	private boolean acceptedDice;
	private boolean diceConfirmed;
	private boolean diceConfirmed2;
	private boolean owner;
	public List<Item> offeredItems = new ArrayList<Item>();

	public Dicing(Player player) {
		this.player = player;
	}

	public void requestDice(Player other) {

		if (player == null || other == null || player.getConstitution() <= 0 || other.getConstitution() <= 0
				|| player.isTeleporting() || other.isTeleporting())

			return;
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			BankPin.init(player, false);
			return;
		}
		if (System.currentTimeMillis() - lastDiceSent < 5000 && !inDice) {
			player.getPacketSender().sendMessage("You're sending dicing requests too frequently. Please slow down.");
			return;
		}
		if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
			player.getPacketSender().sendMessage("You are far too busy to gamble at the moment!");
			return;
		}
		if (inDice) {
			declineDice(true);
			return;
		}
		if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.getPacketSender().sendMessage("Administrators cannot gamble.");
			return;
		}
		if (player.getLocation() == Location.GODWARS_DUNGEON
				&& player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()
				&& !other.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
			player.getPacketSender().sendMessage("You cannot reach that.");
			return;
		}
		if (player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if (player.busy()) {
			return;
		}
		if (other.busy() || other.getInterfaceId() > 0 || other.getDicing().inDice || other.isBanking()
				|| other.isShopping()/*
										 * || other.getDueling().inDuelScreen ||
										 * FightPit.inFightPits(other)
										 */) {
			player.getPacketSender().sendMessage("The other player is currently busy.");
			return;
		}
		if (player.getInterfaceId() > 0 || inDice || player.isBanking()
				|| player.isShopping()/*
										 * || player.getDueling().inDuelScreen
										 * || FightPit.inFightPits(player)
										 */) {
			player.getPacketSender().sendMessage("You are currently unable to dice with another player.");
			if (player.getInterfaceId() > 0)
				player.getPacketSender()
						.sendMessage("Please close all open interfaces before requesting to open another one.");
			return;
		}
		diceWith = other.getIndex();
		if (diceWith == player.getIndex())
			return;
		if (!Locations.goodDistance(player.getPosition().getX(), player.getPosition().getY(),
				other.getPosition().getX(), other.getPosition().getY(), 2)) {
			player.getPacketSender().sendMessage("Please get closer to gamble.");
			return;
		}
		if (!inDice && other.getDicing().diceRequested && other.getDicing().diceWith == player.getIndex() ) {
			owner = true;
			openDice();
			other.getDicing().openDice();
		
			
		} else if (!inDice) {
			/*if (other.getRights() != PlayerRights.OWNER) {
				player.getPacketSender().sendMessage(player.getUsername() + " needs to be a dicer!");
				return;
			}*/
			diceRequested = true;
			player.getPacketSender().sendMessage("You've sent a dicing request to " + other.getUsername() + ".");
			other.getPacketSender().sendMessage(player.getUsername() + ":dicereq:");
		}
		lastDiceSent = System.currentTimeMillis();
	}

	public void openDice() {
		player.getPacketSender().sendClientRightClickRemoval();
		Player player2 = World.getPlayers().get(diceWith);
		if (player == null || player2 == null || diceWith == player.getIndex() || player.isBanking())
			return;
		inDice = true;
		diceRequested = false;
		canOffer = true;
		diceStatus = 1;
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player2.getPacketSender().sendInterfaceItems(3415, player2.getDicing().offeredItems);
		sendText(player2);
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player.getPacketSender().sendInterfaceItems(3416, player2.getDicing().offeredItems);
		player.getMovementQueue().reset();
		inDiceWith = player2.getIndex();
	}

	public void declineDice(boolean tellOther) {
		Player player2 = diceWith >= 0 && !(diceWith > World.getPlayers().capacity()) ? World.getPlayers().get(diceWith)
				: null;
		for (Item item : offeredItems) {
			if (item.getAmount() < 1)
				continue;
			player.getInventory().add(item);
		}
		offeredItems.clear();
		if (tellOther && diceWith > -1) {
			if (player2 == null)
				return;
			player2.getDicing().declineDice(false);
			player2.getPacketSender().sendMessage("Other player declined the gamble.");
		}
		resetDice();
	}

	public void sendText(Player player2) {
		if (player2 == null)
			return;
		player2.getPacketSender().sendString(3451, "" + Misc.formatPlayerName(player.getUsername()) + "");
		player2.getPacketSender().sendString(3417, "Dicing with: " + Misc.formatPlayerName(player.getUsername()) + "");
		player.getPacketSender().sendString(3451, "" + Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(3417, "Dicing with: " + Misc.formatPlayerName(player2.getUsername()) + "");
		player.getPacketSender().sendString(3431, "");
		player.getPacketSender().sendString(3535, "Are you sure you want to gamble?");
		player.getPacketSender().sendInterfaceSet(3323, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
	}

	public void diceItem(int itemId, int amount, int slot) {
		if (slot < 0)
			return;
		if (!canOffer)
			return;
		Player player2 = World.getPlayers().get(diceWith);
		if (player2 == null || player == null)
			return;
		if (player.getRights() == PlayerRights.ADMINISTRATOR) {
			player.getPacketSender().sendMessage("Administrators cannot gamble.");
			return;
		}
		if (player.getRights() != PlayerRights.DEVELOPER && player2.getRights() != PlayerRights.DEVELOPER
				&& !(itemId == 1419 && player.getRights().isStaff())) {
			if (!new Item(itemId).tradeable()) {
				player.getPacketSender().sendMessage("You cannot gamble this item.");
				return;
			}
		}
		falseDiceConfirm();
		player.getPacketSender().sendClientRightClickRemoval();
		if (!inDice || !canOffer) {
			declineDice(true);
			return;
		}
		if (!player.getInventory().contains(itemId))
			return;
		if (slot >= player.getInventory().capacity() || player.getInventory().getItems()[slot].getId() != itemId
				|| player.getInventory().getItems()[slot].getAmount() <= 0)
			return;
		Item itemToGamble = player.getInventory().getItems()[slot];
		if (itemToGamble.getId() != itemId)
			return;
		if (player.getInventory().getAmount(itemId) < amount) {
			amount = player.getInventory().getAmount(itemId);
			if (amount == 0 || player.getInventory().getAmount(itemId) < amount) {
				return;
			}
		}
		if (!itemToGamble.getDefinition().isStackable()) {
			for (int a = 0; a < amount && a < 28; a++) {
				if (player.getInventory().getAmount(itemId) >= 1) {
					offeredItems.add(new Item(itemId, 1));
					player.getInventory().delete(itemId, 1);
				}
			}
		} else if (itemToGamble.getDefinition().isStackable()) {
			boolean itemInDice = false;
			for (Item item : offeredItems) {
				if (item.getId() == itemId) {
					itemInDice = true;
					item.setAmount(item.getAmount() + amount);
					player.getInventory().delete(itemId, amount);
					break;
				}
			}
			if (!itemInDice) {
				offeredItems.add(new Item(itemId, amount));
				player.getInventory().delete(itemId, amount);
			}
		}
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3416, player2.getDicing().offeredItems);
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player.getPacketSender().sendString(3431, "");
		acceptedDice = false;
		diceConfirmed = false;
		diceConfirmed2 = false;
		player2.getPacketSender().sendInterfaceItems(3416, offeredItems);
		player2.getPacketSender().sendString(3431, "");
		player2.getDicing().acceptedDice = false;
		player2.getDicing().diceConfirmed = false;
		player2.getDicing().diceConfirmed2 = false;
		sendText(player2);
	}

	public void removeDiceItem(int itemId, int amount) {
		if (!canOffer)
			return;
		Player player2 = World.getPlayers().get(diceWith);
		if (player2 == null)
			return;
		if (!inDice || !canOffer) {
			declineDice(false);
			return;
		}
		falseDiceConfirm();
		ItemDefinition def = ItemDefinition.forId(itemId);
		if (!def.isStackable()) {
			if (amount > 28)
				amount = 28;
			for (int a = 0; a < amount; a++) {
				Iterator<Item> $it = offeredItems.iterator();
				while ($it.hasNext()) {
					Item item = $it.next();
					if (item.getId() == itemId) {
						if (!item.getDefinition().isStackable()) {
							$it.remove();
							player.getInventory().add(itemId, 1);
						}
						break;
					}
				}
			}
		} else {
			Iterator<Item> $it = offeredItems.iterator();
			while ($it.hasNext()) {
				Item item = $it.next();
				if (item.getId() == itemId) {
					if (item.getDefinition().isStackable()) {
						if (item.getAmount() > amount) {
							item.setAmount(item.getAmount() - amount);
							player.getInventory().add(itemId, amount);
						} else {
							amount = item.getAmount();
							$it.remove();
							player.getInventory().add(itemId, amount);
						}
					}
					break;
				}
			}
		}
		falseDiceConfirm();
		player.getInventory().refreshItems();
		player.getPacketSender().sendInterfaceItems(3416, player2.getDicing().offeredItems);
		player.getPacketSender().sendInterfaceItems(3415, offeredItems);
		player2.getPacketSender().sendInterfaceItems(3416, offeredItems);
		sendText(player2);
		player.getPacketSender().sendString(3431, "");
		player2.getPacketSender().sendString(3431, "");
		player.getPacketSender().sendClientRightClickRemoval();
	}

	public void acceptDice(int stage) {
		if (!player.getClickDelay().elapsed(1000))
			return;
		if (diceWith < 0) {
			declineDice(false);
			return;
		}
		Player player2 = World.getPlayers().get(diceWith);

		if (player == null || player2 == null) {
			declineDice(false);
			return;
		}
		if (!twoDicers(player, player2)) {
			player.getPacketSender().sendMessage("An error has occured. Please try re-dicing the player.");
			return;
		}
		if (offeredItems.isEmpty() || player2.getDicing().offeredItems.isEmpty()) {
			player.getPacketSender().sendMessage("You both need to offer at least one item!");
			return;
		}
		if (stage == 2) {
			if (!inDice || !player2.getDicing().inDice || !player2.getDicing().diceConfirmed) {
				declineDice(true);
				return;
			}
			if (!diceConfirmed)
				return;
			acceptedDice = true;
			diceConfirmed2 = true;
			player2.getPacketSender().sendString(3535, "Other player has accepted.");
			player.getPacketSender().sendString(3535, "Waiting for other player...");
			if (inDice && player2.getDicing().diceConfirmed2) {
				player.getMovementQueue().setLockMovement(true);
				player2.getMovementQueue().setLockMovement(true);

				player.getSession().queueMessage(new PacketBuilder(219));					player.getSession().queueMessage(new PacketBuilder(219));				
				player2.getSession().queueMessage(new PacketBuilder(219));				
			
				Player host = owner ? player : player2;
				Player other = !owner ? player : player2;
				
				host.getPacketSender().sendMessage("You roll the dice... good luck!");
				other.getPacketSender().sendMessage("The host is rolling the dice... good luck!");

				host.performAnimation(new Animation(11900));
				host.performGraphic(new Graphic(2075));
				TaskManager.submit(new Task(5, false) {
					@Override
					protected void execute() {
						int roll = Misc.inclusiveRandom(1, 100);

						if (roll <= 55) {
							finish(host, other, roll);
						} else {
							finish(other, host, roll);
						}
						player.getMovementQueue().setLockMovement(false);
						player2.getMovementQueue().setLockMovement(false);
						this.stop();
					}
				});

			}
		} else if (stage == 1) {
			player2.getDicing().goodDice = true;
			player2.getPacketSender().sendString(3431, "Other player has accepted.");
			goodDice = true;
			player.getPacketSender().sendString(3431, "Waiting for other player...");
			diceConfirmed = true;
			if (inDice && player2.getDicing().diceConfirmed && player2.getDicing().goodDice && goodDice) {
				confirmScreen();
				player2.getDicing().confirmScreen();
			}
		}
		player.getClickDelay().reset();
	}

	private static void finish(Player winner, Player loser, int roll) {
		boolean toBank = false;
		for (Item item : winner.getDicing().offeredItems) {
			if (item.getAmount() <= 0 || item.getId() <= 0) {
				continue;
			}
			if (winner.getInventory().isFull()) {
				winner.getBank(0).add(item);
				toBank = true;
			} else {
				winner.getInventory().add(item);
			}
		}
		for (Item item : loser.getDicing().offeredItems) {
			if (item.getAmount() <= 0 || item.getId() <= 0) {
				continue;
			}
			if (winner.getInventory().isFull()) {
				winner.getBank(0).add(item);
				toBank = true;
			} else {
				winner.getInventory().add(item);
			}
		}
		winner.getPacketSender().sendMessage("<col=008000>The roll was " + roll + ", you have won the gamble!");
		loser.getPacketSender().sendMessage("<col=fa0000>The roll was " + roll + ", you have lost the gamble...");
		if (toBank) {
			winner.getPacketSender().sendMessage("Some of the items were deposited to your bank.");
		}

		winner.getDicing().resetDice();
		loser.getDicing().resetDice();
	}

	private boolean goodDice;
	public long lastAction;

	public void confirmScreen() {
		Player player2 = World.getPlayers().get(diceWith);
		if (player2 == null)
			return;
		canOffer = false;
		player.getInventory().refreshItems();
		String sendDice = "Absolutely nothing!";
		String sendAmount;
		int count = 0;
		for (Item item : offeredItems) {
			if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
				sendAmount = "@cya@" + (item.getAmount() / 1000) + "K @whi@(" + Misc.format(item.getAmount()) + ")";
			} else if (item.getAmount() >= 1000000) {
				sendAmount = "@gre@" + (item.getAmount() / 1000000) + " million @whi@(" + Misc.format(item.getAmount())
						+ ")";
			} else {
				sendAmount = "" + Misc.format(item.getAmount());
			}
			if (count == 0) {
				sendDice = item.getDefinition().getName().replaceAll("_", " ");
			} else
				sendDice = sendDice + "\\n" + item.getDefinition().getName().replaceAll("_", " ");
			if (item.getDefinition().isStackable())
				sendDice = sendDice + " x " + sendAmount;
			count++;
		}

		player.getPacketSender().sendString(3557, sendDice);
		sendDice = "Absolutely nothing!";
		sendAmount = "";
		count = 0;
		for (Item item : player2.getDicing().offeredItems) {
			if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
				sendAmount = "@cya@" + (item.getAmount() / 1000) + "K @whi@(" + Misc.format(item.getAmount()) + ")";
			} else if (item.getAmount() >= 1000000) {
				sendAmount = "@gre@" + (item.getAmount() / 1000000) + " million @whi@(" + Misc.format(item.getAmount())
						+ ")";
			} else {
				sendAmount = "" + Misc.format(item.getAmount());
			}
			if (count == 0) {
				sendDice = item.getDefinition().getName().replaceAll("_", " ");
			} else
				sendDice = sendDice + "\\n" + item.getDefinition().getName().replaceAll("_", " ");
			if (item.getDefinition().isStackable())
				sendDice = sendDice + " x " + sendAmount;
			count++;
		}
		player.getPacketSender().sendString(3558, sendDice);
		player.getPacketSender().sendInterfaceSet(3443, 3321);
		player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
		/*
		 * Remove all tabs!
		 */
		// player.getPacketSender().sendInterfaceSet(3443,
		// Inventory.INTERFACE_ID);
		// player.getPacketSender().sendItemContainer(player.getInventory(),
		// Inventory.INTERFACE_ID);
	}

	public void resetDice() {
		inDiceWith = -1;
		offeredItems.clear();
		canOffer = true;
		inDice = false;
		diceWith = -1;
		diceStatus = 0;
		lastDiceSent = 0;
		acceptedDice = false;
		diceConfirmed = false;
		diceConfirmed2 = false;
		diceRequested = false;
		goodDice = false;
		owner = false;
		player.getPacketSender().sendString(3535, "Are you sure you want to gamble?");
		player.getPacketSender().sendInterfaceRemoval();
		player.getPacketSender().sendInterfaceRemoval();
	}

	private boolean falseDiceConfirm() {
		Player player2 = World.getPlayers().get(diceWith);
		return diceConfirmed = player2.getDicing().diceConfirmed = false;
	}

	public static boolean twoDicers(Player p1, Player p2) {
		int count = 0;
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getDicing().inDiceWith == p1.getIndex() || player.getDicing().inDiceWith == p2.getIndex()) {
				count++;
			}
		}
		return count == 2;
	}
}
