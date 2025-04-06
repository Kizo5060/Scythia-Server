
package com.ruseps.net.packet.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.common.collect.Multiset.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ruseps.BalloonDropParty;
import com.ruseps.model.container.impl.Shop;
import com.ruseps.util.Misc;
import com.ruseps.util.NameUtils;
import com.ruseps.util.RandomUtility;
import com.ruseps.util.TreasureIslandLootDumper;
import com.ruseps.world.World;
import com.ruseps.world.content.*;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.net.security.ConnectionHandler;
import com.ruseps.world.content.newdroptable.DropTableInterface;
import com.ruseps.world.content.upgrading.UpgradeData;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.content.bossevents.GameEventManager;
import com.ruseps.GameLoader;
import com.ruseps.GameServer;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PlayerDeathTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Inventory;
import com.ruseps.model.container.impl.Shop.ShopManager;
import com.ruseps.model.definitions.DropUtils;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.input.impl.ConfirmEmpty;
import com.ruseps.model.input.impl.SetTitle;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ruseps.world.content.skill.impl.herblore.Decanting;
import com.ruseps.world.content.teleport.TeleportHandlerNew;
import com.ruseps.world.content.PlayerPunishments.Jail;
import com.ruseps.world.content.achievements.AchievementInterface;
import com.ruseps.world.content.battle_pass.BattlePassType;
import com.ruseps.world.content.battle_royale.BattleRoyale;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.DesolaceFormulas;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.droppreview.SLASHBASH;
import com.ruseps.world.content.fuser.FuserEnum;
import com.ruseps.world.content.fuser.FuserHandler;
import com.ruseps.world.content.grandexchange.GrandExchangeOffers;
import com.ruseps.world.content.instances.CashZone;
import com.ruseps.world.content.instances.Raichu;
import com.ruseps.world.content.loot_display.RewardsHandler;
import com.ruseps.world.content.minigames.impl.RaichuInstance;
import com.ruseps.world.content.new_raids_system.instances.RubyRaid;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.content.transportation.TeleportType;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerHandler;

import com.ruseps.world.content.bossevents.GameEventManager;
import com.teamgames.endpoints.store.Transaction;
import com.teamgames.endpoints.vote.VoteEndpoint;
import com.teamgames.endpoints.vote.obj.ClaimReward;

import static com.ruseps.world.World.sendMessage;

public class CommandPacketListener implements PacketListener {

	public static int config;
	private static int VOTES;
	private static final String TEAMGAMES_API_KEY = "0r9sUMZuKE1XveTp9yjGlq0gdQFm4Qyj7Zgl8PnXQnpZT6HGiOE7xrwzXq2AuYPTettthbNA";

	public static String getFormatedTime(GameEvent event) {

		long timeLeft = event.getLastEventInstant() + event.getDelayBetweenEvents() - System.currentTimeMillis();
		int hoursLeft = (int) TimeUnit.MILLISECONDS.toHours(timeLeft);
		int minutesLeft = (int) TimeUnit.MILLISECONDS.toMinutes(timeLeft) - (hoursLeft * 60);
		int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft) - (hoursLeft * 60 * 60) - (minutesLeft * 60);

		if (hoursLeft < 0 || minutesLeft < 0 || secondsLeft < 0) {
			return "Soon";
		}

		return (hoursLeft < 10 ? "0" : "") + hoursLeft + ":" + (minutesLeft < 10 ? "0" : "") + minutesLeft + ":"
				+ (secondsLeft < 10 ? "0" : "") + secondsLeft;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		long start = System.currentTimeMillis();
		String command = Misc.readString(packet.getBuffer());
		if (player.isHidePlayer()) {
			return;
		}

		String[] parts = command.toLowerCase().split(" ");
		if (command.contains("\r") || command.contains("\n")) {
			return;
		}
		PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " used command ::" + command
				+ " | Player rights = " + player.getRights() + "");

		try {
			switch (player.getRights()) {
			case PLAYER:
				playerCommands(player, parts, command);
				break;
			case MODERATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				platCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case ADMINISTRATOR:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				goldCommands(player, parts, command);
				platCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case OWNER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				goldCommands(player, parts, command);
				platCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case DEVELOPER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				ownerCommands(player, parts, command);
				developerCommands(player, parts, command);
				goldCommands(player, parts, command);
				platCommands(player, parts, command);
				DCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case MANAGER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				moderatorCommands(player, parts, command);
				administratorCommands(player, parts, command);
				goldCommands(player, parts, command);
				platCommands(player, parts, command);
				DCommands(player, parts, command);
				rubyCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case SUPPORT:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				helperCommands(player, parts, command);
				break;
			case YOUTUBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				platCommands(player, parts, command);
				DCommands(player, parts, command);
				rubyCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case BRONZE_MEMBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				bronzeCommands(player, parts, command);
				break;
			case SILVER_MEMBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				sCommands(player, parts, command);
				break;

			case RUBY_MEMBER:
				platCommands(player, parts, command);
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				bronzeCommands(player, parts, command);
				sCommands(player, parts, command);
				DCommands(player, parts, command);
				rubyCommands(player, parts, command);
				break;

			case DIAMOND_MEMBER:
				platCommands(player, parts, command);
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				bronzeCommands(player, parts, command);
				sCommands(player, parts, command);
				DCommands(player, parts, command);
				break;
			case DRAGONSTONE_MEMBER:
				platCommands(player, parts, command);
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				bronzeCommands(player, parts, command);
				sCommands(player, parts, command);
				DCommands(player, parts, command);
				rubyCommands(player, parts, command);
				dragonCommands(player, parts, command);
				break;
			case PLATINUM_MEMBER:
				platCommands(player, parts, command);
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				bronzeCommands(player, parts, command);
				sCommands(player, parts, command);
				break;
			case GOLD_MEMBER:
				playerCommands(player, parts, command);
				memberCommands(player, parts, command);
				goldCommands(player, parts, command);
				sCommands(player, parts, command);
				break;
			default:
				break;
			}
		} catch (Exception exception) {

			if (player.getRights() == PlayerRights.DEVELOPER) {
				player.getPacketSender().sendConsoleMessage("Error executing that command.");
			} else {
				player.getPacketSender().sendMessage("Error executing that command.");
			}

		}
		long end = System.currentTimeMillis();
		long cycle = end - start;
		if (cycle >= 500) {
			System.err.println(cycle + "ms - command packet- " + command + " - " + player.getRights().name());
		}
	}

	private static void playerCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("accept")) {
			if (player.getGroupIronmanGroupInvitation() == null) {
				player.sendMessage("You have no pending group ironman invitations");
				return;
			}
			player.getGroupIronmanGroupInvitation().handleInvitation(player, true);
		}

		if (command[0].equalsIgnoreCase("kh") || command[0].equalsIgnoreCase("collectionlog")) {
			player.getCollectionLogManager().openInterface();

		}

		if (command[0].equalsIgnoreCase("leave")) {
			player.getInstanceManager().endInstance(player, false);
			CashZone.exitCashZone(player, false);
		}

		boolean inJail = (player.getLocation() == Location.IN_JAIL) ? true : false;

		if (command[0].equals("blvl")) {
			player.sendMessage("[MY BP LEVEL]: " + player.getBattlePass().getLevel());
		}

		if (command[0].equals("global") || command[0].equals("globals")) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(433));
			player.moveTo(new Position(2337, 3229), true);
		}

		if (command[0].equals("forcebplvl")) {
			player.getBattlePass().checkLevelUp();
			player.sendMessage("@red@Your BP level is now: " + player.getBattlePass().getLevel());
		}

		if (command[0].equals("bxp")) {
			player.sendMessage("[MY BP EXP]: " + player.getBattlePass().getExperience());
		}

		if (command[0].equals("esc")) {
			player.getPA().closeAllWindows();
		}

		if (command[0].equals("teleportation")) {
			if (player.getInterfaceId() == 45800) {
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			if (player.teleports != null) {
				if (player.teleports.size() > 0) {
					player.teleports.clear();
				}
			}
			TeleportHandlerNew.openInterface(player, 1);
		}

		if (command[0].equalsIgnoreCase("gim")) {
			if (!player.isGim()) {
				return;
			}

			player.getGroupIronman().open();
		}
		if (command[0].equalsIgnoreCase("daily") || command[0].equalsIgnoreCase("dailytasks")) {
			player.getDailyTaskManager().open();
		}

		if (command[0].equalsIgnoreCase("bp")) {
			player.getPacketSender().sendInterface(13782);
			player.getBattlePass().displayPage();
		}
		if (command[0].equalsIgnoreCase("best")) {
			player.getPacketSender().sendInterface(27245); // Use your correct interface ID for Best In Slot
		}

		if (command[0].equalsIgnoreCase("votetest")) {
			for (int i = 0; i < 20; i++) {
				VOTES += 20;
			}
		}

		if (command[0].equalsIgnoreCase("task")) {
			player.getSlayer().handleSlayerCommand();
		}

		if (command[0].equalsIgnoreCase("gimbank")) {
			if (player.getGroupIronmanGroup() == null) { // this check is enough because a non-group ironman can't have
															// a group anyway
				player.sendMessage("You are not in a ironman group");
				return;
			}
			player.getGroupIronmanGroup().openBank(player);
		}

		if (command[0].equalsIgnoreCase("test1234")) {
			AchievementInterface.open(player);
		}

		if (command[0].equalsIgnoreCase("gimleaderboard")) {
			player.getGroupIronman().openLeaderboard();
		}

		if (command[0].equalsIgnoreCase("timeroff")) {
			player.getPacketSender().sendWalkableInterface(48300, false);
			return;
		}
		if (command[0].equalsIgnoreCase("bis")) {
			player.getBis().open();
		}
		if (command[0].equalsIgnoreCase("loots")) {
			RewardsHandler.open(player);
			return;
		}
		if (command[0].equalsIgnoreCase("Kckills")) {
			player.getPacketSender().sendInterface(55250);
			return;
		}
		if (command[0].equalsIgnoreCase("Totalkills")) {
			player.forceChat(" I have " + KillsTracker.getTotalKills(player) + " kills!");
			player.sendMessage("@bla@<shad=1>" + player.getUsername() + " @mag@you have @bla@"
					+ KillsTracker.getTotalKills(player) + "@mag@ kills ");
		}

		if (command[0].equalsIgnoreCase("kcoff")) {
			player.kcMessage = false;
			player.kcMessage = false;
			return;
		}

		if (command[0].equalsIgnoreCase("kcon")) {
			player.kcMessage = true;
			return;
		}
		if (command[0].equalsIgnoreCase("taskon")) {
			player.getPacketSender().sendWalkableInterface(42001, true);
		}
		if (command[0].equalsIgnoreCase("voteboss")) {
			player.moveTo(new Position(2335, 3229, 0));
		}

		if (command[0].equalsIgnoreCase("players")) {
			PlayersOnlineInterface.showInterface(player);
			PlayersOnlineInterface.updateInterface(player, PlayersOnlineInterface.getPlayer(player));
		}

		if (command[0].equalsIgnoreCase("taskoff")) {
			player.getPacketSender().sendWalkableInterface(42001, false);
		}

		if (command[0].equalsIgnoreCase("collon")) {
			player.collectorMessage = true;
		}

		if (command[0].equalsIgnoreCase("collection")) {
			player.getCollectionLogManager().openInterface();
		}

		if (command[0].equalsIgnoreCase("colloff")) {
			player.collectorMessage = false;
		}

		if (command[0].equalsIgnoreCase("events")) {
			String title = "@whi@NexArch World Boss Events (" + GameEventManager.getEvents().size() + ")";
			List<String> events = new ArrayList<>();
			int index = 0;

			for (GameEvent event : GameEventManager.getEvents().values()) {
				player.getPacketSender().sendString((index > 50 ? 12174 : 8145) + index++, event.name());
				if (index == 1) {
					index++;
				}
				events.add("@whi@" + event.name());
				if (event.isActive()) {
					events.add("The event is currently @gre@[ Active ]");
				} else {
					events.add("The event will start in: " + getFormatedTime(event));
				}
			}
			player.getPacketSender().resetItemsOnInterface(58800, 200);
			Misc.sendItemInfoInterface(player, title, new ArrayList<>(), events);
		}

		if (command[0].equalsIgnoreCase("bunny") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1176));
			player.moveTo(new Position(2317, 3891), true);
		}

		if (command[0].equalsIgnoreCase("raidsdeath")) {
			player.forceChat("I have died a total of " + player.getMinigameAttributes().getRaidsAttributes().getDeaths()
					+ " times during Pokemon Raids");
		}
		if (command[0].equalsIgnoreCase("raidsKc")) {
			player.sendMessage("<shad=1>@whi@Pokemon Raids Completed: @gre@" + player.getPokemonRaidsOpened());
			player.sendMessage("<shad=1>@whi@Anime Raids Completed: @gre@" + player.getAnimeRaidsOpened());
			player.sendMessage("<shad=1>@whi@Mk Raids Completed: @gre@" + player.getMkRaidsOpened());
			player.sendMessage("<shad=1>@whi@Silver Raids Completed: @gre@" + player.getSilverRaidsOpened());
			player.sendMessage("<shad=1>@whi@Gold Raids Completed: @gre@" + player.getGoldRaidsOpened());
			player.sendMessage("<shad=1>@whi@Diamond Raids Completed: @gre@" + player.getDiamondRaidsOpened());
			player.sendMessage("<shad=1>@whi@Plat Raids Completed: @gre@" + player.getPlatRaidsOpened());
			player.sendMessage("<shad=1>@whi@Ruby Raids Completed: @gre@" + player.getRubyRaidsOpened());
			player.sendMessage("<shad=1>@whi@Dark Dimension Completed: @gre@" + player.getDarkDementionRaidsOpened());

		}

		if (command[0].equalsIgnoreCase("completedraids")) {
			player.forceChat("I have completed a WHOPPING total of "
					+ player.getMinigameAttributes().getRaidsAttributes().getCompleted() + " Pokemon Raids");
		}

		if (command[0].equalsIgnoreCase("vader") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2337, 3229), true);
		}
		if (command[0].equalsIgnoreCase("witch") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1552));
			player.moveTo(new Position(3357, 3034), true);
		}
		if (command[0].equalsIgnoreCase("reaper") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2337, 3229), true);
		}
		if (command[0].equalsIgnoreCase("lava") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2337, 3229), true);
		}
		if (command[0].equalsIgnoreCase("nx") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2955, 3212), true);
		}
		if (command[0].equalsIgnoreCase("santa") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2526, 3920), true);
		}
		if (command[0].equalsIgnoreCase("owner") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(2853, 3919), true);
		}
		if (command[0].equalsIgnoreCase("Telos") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(433));
			player.moveTo(new Position(2337, 3229), true);
		}
		if (command[0].equalsIgnoreCase("spartan") && !inJail) {
			player.performGraphic(new Graphic(342));
			player.moveTo(new Position(2337, 3229), true);
		}

		if (command[0].equalsIgnoreCase("dragon") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performGraphic(new Graphic(2009));
			player.moveTo(new Position(2559, 4951), true);
		}

		if (command[0].equalsIgnoreCase("well")) {
			int time = WellOfGoodwill.getMinutesRemaining();
			if (time <= 0) {
				player.getPA().sendMessage("The well is not currently active!");
			} else {
				player.getPA().sendMessage("There are currently: " + time + " minutes remaining.");

			}
		}
		if (command[0].equalsIgnoreCase("combine")) {
			player.getFuserHandler().openInterface();
		}

		if (command[0].startsWith("voted")) {
//			if (command.length == 1) {
//				player.getPacketSender()
						sendMessage("Please use claimvote.");
//				return;
//			}
			final String playerName = player.getUsername();
//			final String id = command[1];
//			final String amount = command.length == 3 ? command[2] : "1";
			final String id = "1";
			final String amount = "all";

			new java.lang.Thread() {
				public void run() {
					try {
						VoteEndpoint voteEndpoint = new VoteEndpoint();
						voteEndpoint.setApiKey(TEAMGAMES_API_KEY)
								.setPlayerName(playerName)
								.setRewardId(id)
								.setAmount(amount);
						ClaimReward[] rewards = voteEndpoint.getReward();
						boolean claimed = false;
						for (int i = 0; i < rewards.length; i++) {
							if (rewards[i].getMessage() != null) {
								player.sendMessage(rewards[i].getMessage());
								return;
							}
							claimed = true;
							player.getInventory().add(new Item(rewards[i].getRewardId(), rewards[i].getGiveAmount()));
						}
						if (claimed) {
							player.sendMessage("Thank you for voting!");
						}
					} catch (Exception e) {
						System.err.println("Error claiming reward: " + e.getMessage());
					}
				}
			}.start();


		}
		if (command[0].equalsIgnoreCase("claim")) {
		    new java.lang.Thread() {
		        public void run() {
					Transaction transaction = new Transaction()
							.setApiKey(TEAMGAMES_API_KEY).setPlayerName(player.getUsername());

					try {
						Transaction[] transactions = transaction.getTransactions();
						if (transactions.length > 0) {
							for (Transaction trans : transactions) {
								if (trans.message != null) {
									player.sendMessage(trans.message);
									return;
								}
//								System.out.println("Player: " + trans.player_name + ", Product: " + trans.product_name + ", Amount: " + trans.product_amount);
								player.getInventory().add(new Item(trans.product_id, trans.product_amount));
							}
						} else {
							player.sendMessage("No transactions found for the player.");
						}
					} catch (Exception e) {
						System.err.println("Error fetching transactions: " + e.getMessage());
					}

		        }
		    }.start();
		}
		if (command[0].equalsIgnoreCase("ffaleave")) {
			if (player.getLocation() != Location.GODS_RAID) {
				player.getPA().sendMessage("You can only use this in the ffa arenas");
				return;
			}

			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.IN_JAIL) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (player.getLocation() == Location.DUEL_ARENA) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			player.getPA().sendInterfaceRemoval();
			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 0) {

					} else if (tick >= 3) {
						BattleRoyale.leaveArena(player);
						this.stop();
					}
					tick++;
				}
			});
		}

		if (command[0].equalsIgnoreCase("battle")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterMeleeLobby(player);
			} else {
				player.getPacketSender().sendMessage("@red@There is not currently a Melee Battle Royale Event.");
			}
		}

		if (command[0].equalsIgnoreCase("nightmare")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterRangeLobby(player);
			} else {
				player.getPacketSender().sendMessage("@red@There is not currently a Nightmare Royale Event.");
			}
		}

		if (command[0].equalsIgnoreCase("rbattle")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterMeleeLobby(player);
			} else {
				player.getPacketSender().sendMessage("@red@There is not currently a Melee Battle Royale Event.");
			}
		}

		if (command[0].equalsIgnoreCase("kraken")) {
			player.getInstanceManager().endInstance(player, false);
			player.getPacketSender().sendMessage("Teleporting you to kraken.");
			player.getKraken().enter(player, true);
		}

		if (command[0].equalsIgnoreCase("grabregion")) {
			int regionX = player.getPosition().getX() >> 3;
			int regionY = player.getPosition().getY() >> 3;
			int regionId = ((regionX / 8) << 8) + (regionY / 8);
			player.getPacketSender().sendMessage("Region id: " + regionId);
		}

		if (command[0].equalsIgnoreCase("mole")) {
			player.getInstanceManager().endInstance(player, false);
			TeleportHandler.teleportPlayer(player, new Position(1761, 5186), player.getSpellbook().getTeleportType());

		}

		if (command[0].equalsIgnoreCase("bzone") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			if (player.getAmountDonated() < 50) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(1296, 1247),
						player.getSpellbook().getTeleportType());
			}
		}

		if (command[0].equalsIgnoreCase("gzone") && !inJail) {
			if (player.getAmountDonated() < 300) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2909, 2516),
						player.getSpellbook().getTeleportType());
			}
		}

		if (command[0].equalsIgnoreCase("pzone") && !inJail) {
			if (player.getAmountDonated() < 500) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(3115, 3938),
						player.getSpellbook().getTeleportType());
			}
		}

		if (command[0].equalsIgnoreCase("dzone") && !inJail) {
			if (player.getAmountDonated() < 850) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(1818, 3099, 2),
						player.getSpellbook().getTeleportType());
			}
		}

		if (command[0].equalsIgnoreCase("szone") && !inJail) {
			if (player.getAmountDonated() < 100) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(1245, 1246),
						player.getSpellbook().getTeleportType());
			}
		}
		if (command[0].equalsIgnoreCase("junk")) {
			ShopManager.getShops().get(12).open(player);
		}
		if (command[0].equals("tree")) {
			player.getPacketSender().sendMessage("<img=4> <shad=1><col=FF9933> The Evil Tree has sprouted at: "
					+ EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + "");
		}

		if (command[0].equals("star")) {
			player.getPacketSender().sendMessage("<img=4> <shad=1><col=FF9933> The Shooting star has spawned at: "
					+ ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame + "");
		}

		if (command[0].equals("decant")) {
			Decanting.startDecanting(player);
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.getPacketSender().sendMessage("You are already skulled");
				return;
			} else {
				player.getPacketSender().sendMessage("got here somehow");
				CombatFactory.skullPlayer(player);
			}
		}

		if (command[0].equalsIgnoreCase("raidparty")) {
			player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
					.sendTab(GameSettings.ACHIEVEMENT_TAB);
		}

		if (command[0].equalsIgnoreCase("answer")) {
			String answer = wholeCommand.substring(7);
			TriviaBot.answer(player, answer);
		}
		if (command[0].equalsIgnoreCase("drop")) {
			DropTableInterface.getInstance().open(player);

		}

		if (command[0].equalsIgnoreCase("drops")) {
			player.getPacketSender().sendInterface(37600);

		}

		if (command[0].equalsIgnoreCase("gamble")) {
			TeleportHandler.teleportPlayer(player, new Position(2915, 2616), player.getSpellbook().getTeleportType());
			player.getPacketSender().sendMessage("@red@Please gamble safely. It is reccomended to record any gambles.");
			player.getPacketSender()
					.sendMessage("@red@YOU MUST HAVE VIDEO EVIDENCE OF GETTING SCAMMED TO FILE A REPORT");
		}

		// if (!player.getClickDelay().elapsed(2000))
		// return;

		/*
		 * Sql commands start
		 */

		/*
		 * End of sql commands
		 */

		if (command[0].equalsIgnoreCase("train") && !inJail) {
			TeleportHandler.teleportPlayer(player, new Position(2519, 4842), player.getSpellbook().getTeleportType());

		}
		
		if (command[0].equalsIgnoreCase("sm2") && !inJail) {
			TeleportHandler.teleportPlayer(player, new Position(2519, 4842, 8), player.getSpellbook().getTeleportType());
			
		}

		if (command[0].equalsIgnoreCase("db")) {
			TeleportHandler.teleportPlayer(player, new Position(3425, 3998, 4),
					player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("commands")) {
			player.getPacketSender().sendMessage("::help - contacts staff for help");
			player.getPacketSender().sendMessage("::events - shows event timer");
			player.getPacketSender().sendMessage("::home - teleports you to home area");
			player.getPacketSender().sendMessage("::vote - opens vote page");
			player.getPacketSender().sendMessage("::donate - opens donate page");
			player.getPacketSender().sendMessage("::auth (code) - claims voting auth");
			player.getPacketSender().sendMessage("::save - saves your progress");
			player.getPacketSender().sendMessage("::dr - shows your DR");
			player.getPacketSender().sendMessage("::claim - claims a donation");
			player.getPacketSender().sendMessage("::train - teleports you to rock crabs");
			player.getPacketSender().sendMessage("::empty - empties your entire inventory");
			player.getPacketSender().sendMessage("::answer (answer) - answers the trivia");
			player.getPacketSender().sendMessage("::drops - opens drop list of npc");
			player.getPacketSender().sendMessage("::guides - a guide to help u begin your adventure");
			player.getPacketSender().sendMessage("::taskon - turns on task messages on ");
			player.getPacketSender().sendMessage("::taskoff - turns off task messages off ");
			player.getPacketSender().sendMessage("::kcoff - turns off KillCount messages on ");
			player.getPacketSender().sendMessage("::kcon - turns on KillCount messages on ");
			player.getPacketSender().sendMessage("::loots - shows mboxes loot ");

		}

		if (command[0].equalsIgnoreCase("setemail")) {
			String email = wholeCommand.substring(9);
			player.setEmailAddress(email);
			player.getPacketSender().sendMessage("You set your account's email adress to: [" + email + "] ");
			PlayerPanel.refreshPanel(player);
		}

		if (command[0].equalsIgnoreCase("changepassword")) {
			String syntax = wholeCommand.substring(15);
			if (syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
				player.getPacketSender().sendMessage("That password is invalid. Please try another password.");
				return;
			}
			if (syntax.contains("_")) {
				player.getPacketSender().sendMessage("Your password can not contain underscores.");
				return;
			}
			if (player.getPasswordPlayer() == 0) {
				player.setPasswordPlayer(2);
				player.setPlayerLocked(false);
			}
			player.setPassword(syntax);
			player.getPacketSender().sendMessage("Your new password is: [" + syntax + "] Write it down!");

		}
		if (command[0].equalsIgnoreCase("remove")) {
			player.getPA().sendWalkableInterface(42001, false);
			player.getPA().sendWalkableInterface(41700, false);

		}

		if (command[0].equalsIgnoreCase("shad")) {
			String syntax = wholeCommand.substring(5);
			player.getPacketSender().sendMessage("<col=" + syntax + "><shad=" + syntax + ">Test");

		} // 1aa3ff <col=1aa3ff><shad=1aa3ff>

		if (command[0].equalsIgnoreCase("dropparty")) {
			TeleportHandler.teleportPlayer(player, new Position(3367, 3093), player.getSpellbook().getTeleportType());
		}

		if (command[0].equalsIgnoreCase("home") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performAnimation(new Animation(866));
			player.performGraphic(new Graphic(2009));
			player.moveTo(GameSettings.DEFAULT_POSITION.copy());
		}

		if (command[0].equalsIgnoreCase("im")) {
			player.getInstanceManager().openInterface(player);
		}

		if (command[0].equalsIgnoreCase("rzone") && !inJail) {
			if (player.getAmountDonated() < 1700) {
				player.getPacketSender().sendMessage("You have not donated enough for this!");
				return;
			} else {
				TeleportHandler.teleportPlayer(player, new Position(3285, 4000),
						player.getSpellbook().getTeleportType());
			}
		}

		if (command[0].equalsIgnoreCase("testr")) {
			RubyRaid.startRaidsEight(player);
		}

		if (command[0].equalsIgnoreCase("Shops") && !inJail) {
			player.getInstanceManager().endInstance(player, false);
			player.performAnimation(new Animation(866));
			player.performGraphic(new Graphic(1044));
			player.moveTo(GameSettings.NEW_SHOPS.copy());
		}

		if (command[0].equalsIgnoreCase("afk") && !inJail) {
			TeleportHandler.teleportPlayer(player, new Position(2016, 4627), player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("dr")) {
			player.getPA().sendMessage("Your Drop Rate Is Now! @red@" + DropUtils.drBonus(player));

		}

		if (command[0].equalsIgnoreCase("refer")) {
			if (player.hasReferral != true) {
				if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
					player.sendMessage("@red@You are too hardcore for this event.");
					return;
				}
				player.getPacketSender()
						.sendEnterInputPrompt("Hi :D Where did you find this server from type Nothing if no-one");
				player.setInputHandling(new EnterReferral());
			} else {
				player.sendMessage("@red@You have already used this before!");
			}

		}

		if (command[0].equalsIgnoreCase("removetitle")) {
			player.setTitle("");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (wholeCommand.toLowerCase().startsWith("yell")) { // ironman img=697
			if (player.getLocation() == Location.GAMBLING_ZONE) {
				return;
			}
			if (PlayerPunishments.isMuted(player.getUsername())
					|| PlayerPunishments.isIpMuted(player.getHostAddress())) {
				player.getPacketSender().sendMessage("You are muted and cannot yell.");
				return;
			}
			int delay = player.getRights().getYellDelay();
			if (!player.getLastYell().elapsed((delay * 1000))) {
				player.getPacketSender().sendMessage(
						"You must wait at least " + delay + " seconds between every yell-message you send.");
				return;
			}
			String yellMessage = wholeCommand.substring(4, wholeCommand.length());

			if (player.getAmountDonated() < 19 && player.getRights().isStaff() == false) {
				player.getPacketSender().sendMessage("You are not a donator!");
				DialogueManager.start(player, 291);
				player.setDialogueActionId(111);
				player.setYellMsg(yellMessage);

			}
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				if (player.getAmountDonated() > 19) {
					sendMessage("<col=787878>$ [HC Iron Man] @bla@" + player.getUsername() + ":" + yellMessage);
					return;
				} else {
					player.getPacketSender().sendMessage("You are not a donator!");

				}

			}
			if (player.getRights() == PlayerRights.DEVELOPER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><shad=1>@red@ [DEVELOPER]@bla@ <shad=1>"
						+ player.getUsername() + ":@red@" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.OWNER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + ">@red@<shad=1> [OWNER] @bla@" + player.getUsername() + ":"
						+ yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.MANAGER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "<col=006699><shad=1> [Manager] <col=d5096e><shad=1>"
						+ player.getUsername() + "@bla@:<col=006699><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.SUPPORT) {

				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + ">@blu@<shad=1> [SUPPORT] @bla@" + player.getUsername()
						+ ":@red@" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.YOUTUBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + ">@red@<shad=1> [YOUTUBER] @whi@" + player.getUsername()
						+ ":@red@" + yellMessage);
				return;
			}

			if (player.getRights() == PlayerRights.MODERATOR) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=6600CC> [MOD]</col> <col=0b3876><shad=1>"
						+ player.getUsername() + ":" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.ADMINISTRATOR) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + ">@yel@ <shad=1>[ADMIN] <col=81061a><shad=1>"
						+ player.getUsername() + ":@yel@" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.DRAGONSTONE_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=771414><shad=1> [DRAGONSTONE] @bla@"
						+ player.getUsername() + ":<col=771414><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.RUBY_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=E0115F><shad=1>[RUBY] @bla@" + player.getUsername()
						+ ":<col=E0115F><shad=1>" + yellMessage);
				return;
			}
			if (player.getRights() == PlayerRights.DIAMOND_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + ">@cya@<shad=1> [DIAMOND]</shad></col> @bla@"
						+ player.getUsername() + ":@cya@<shad=1>" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.PLATINUM_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=E5E4E2><shad=1> [PLATINUM]</shad></col> @bla@"
						+ player.getUsername() + ":<col=E5E4E2><shad=1>" + yellMessage);

				return;
			}
			if (player.getRights() == PlayerRights.GOLD_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=FFD700><shad=1> [GOLD]</shad></col> @bla@"
						+ player.getUsername() + ":<col=FFD700><shad=1>" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			if (player.getRights() == PlayerRights.SILVER_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=C0C0C0><shad=1> [SILVER]</shad></col> @bla@"
						+ player.getUsername() + ":<col=C0C0C0><shad=1>" + yellMessage);
				player.getLastYell().reset();

				return;
			}

			if (player.getRights() == PlayerRights.BRONZE_MEMBER) {
				sendMessage("" + player.getRights().getYellPrefix()
						+ (player.getGameMode() == GameMode.IRONMAN ? "<img=697> " : "") + "<img="
						+ player.getRights().ordinal() + "><col=CD7F32><shad=1> [BRONZE]</shad></col> @bla@"
						+ player.getUsername() + ":<col=CD7F32><shad=1>" + yellMessage);
				player.getLastYell().reset();

				return;
			}
			/*
			 * if (player.getRights() == PlayerRights.YOUTUBER) { World.sendMessage("" +
			 * player.getRights().getYellPrefix() + "<img=" + player.getRights() .ordinal()
			 * + ">@red@ [YOUTUBER] @bla@" + player.getUsername() + ":" + yellMessage);
			 * player.getLastYell().reset();
			 * 
			 * return; }
			 * 
			 * if (player.getRights() == PlayerRights.MODERATOR) { World.sendMessage("" +
			 * player.getRights().getYellPrefix() + "<img=" + player.getRights() .ordinal()
			 * + ">@blu@<shad=1> [MOD] @bla@" + player.getUsername() + ":" + yellMessage);
			 * player.getLastYell().reset();
			 * 
			 * return; }
			 */
		}















		if (command[0].equalsIgnoreCase("maxhit")) {
			int attack = DesolaceFormulas.getMeleeAttack(player) * 10;
			int range = DesolaceFormulas.getRangedAttack(player) * 10;
			int magic = DesolaceFormulas.getMagicAttack(player) * 10;
			player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack + "@bla@Ranged attack: @or2@"
					+ range + "@bla@Magic attack: @or2@" + magic);
		}

		if (command[0].equals("save")) {
			player.save();
			player.getPacketSender().sendMessage("Your progress has been saved.");
		}

		if (command[0].equals("help")) {
			if (player.getLastYell().elapsed(30000)) {
				World.sendStaffMessage("<col=FF0066><img=10> [TICKET SYSTEM]<col=6600FF> " + player.getUsername()
						+ " has requested help. Please help them!");
				player.getLastYell().reset();
				player.getPacketSender()
						.sendMessage("<col=663300>Your help request has been received. Please be patient.");
			} else {
				player.getPacketSender().sendMessage("")
						.sendMessage("<col=663300>You need to wait 30 seconds before using this again.").sendMessage(
								"<col=663300>If it's an emergency, please private message a staff member directly instead.");
			}
		}
		if (command[0].equals("empty")) {
			player.setInputHandling(new ConfirmEmpty());
			player.getPacketSender()
					.sendEnterInputPrompt("Type 'Yes/No' to decide if you want to empty your inventory.");
		}

		if (command[0].equalsIgnoreCase("[cn]")) {
			if (player.getInterfaceId() == 40172) {
				ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(command[1])));
			}
		}
	}

	private static void memberCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("title")) {
			player.setInputHandling(new SetTitle());
			player.getPacketSender().sendEnterInputPrompt("Enter the title you would like to set");
		}
		if (command[0].equalsIgnoreCase("invisible989")) {
			player.setHidePlayer(true);
			player.getPA().sendMessage("You are now completely invisible to other players. Relog to come visible");
		}
		if (command[0].equalsIgnoreCase("promotele")) {
			String playerToTele = wholeCommand.substring(10);
			Player player2 = World.getPlayerByName(playerToTele);

			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}
		if (command[0].equals("pos")) {
			if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.GOLD_MEMBER
					|| player.getRights() == PlayerRights.YOUTUBER || player.getRights() == PlayerRights.PLATINUM_MEMBER
					|| player.getRights() == PlayerRights.DIAMOND_MEMBER
					|| player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.DRAGONSTONE_MEMBER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.MANAGER) {
				if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
						|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WEAPONZ
						|| player.getLocation() == Location.MAGEZ || player.getLocation() == Location.DEFENDERZ
						|| player.getLocation() == Location.RANGEZ || player.getLocation() == Location.VALENTINES) {
					player.getPacketSender().sendMessage("You can not open your POS here!");
					return;
				}
				player.setTempBankTabs(null);
				player.getPlayerOwnedShopManager().open();
			} else {
				player.getPacketSender().sendMessage("You must be Silver+ to do this!");
			}
		}
		if (command[0].equals("upgrade")) {
			if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.GOLD_MEMBER
					|| player.getRights() == PlayerRights.YOUTUBER || player.getRights() == PlayerRights.PLATINUM_MEMBER
					|| player.getRights() == PlayerRights.DIAMOND_MEMBER
					|| player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.DRAGONSTONE_MEMBER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.MANAGER) {
				if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
						|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WEAPONZ
						|| player.getLocation() == Location.MAGEZ || player.getLocation() == Location.DEFENDERZ
						|| player.getLocation() == Location.RANGEZ || player.getLocation() == Location.VALENTINES) {
					player.getPacketSender().sendMessage("You can not open your Upgrade here!");
					return;
				}
				player.getPacketSender().sendInterface(62200);
				for (int i = 0; i < UpgradeData.itemList.length; i++)
					player.getPacketSender().sendItemOnInterface(62209, UpgradeData.itemList[i], i, 1);
				player.getPacketSender().sendMessage(
						"@red@Be careful, " + player.getUsername() + "Currently, you will lose the item on Failure");

			} else {
				player.getPacketSender().sendMessage("You must be Silver+ to do this!");
			}
		}

		if (command[0].equals("fuser")) {
			if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.GOLD_MEMBER
					|| player.getRights() == PlayerRights.YOUTUBER || player.getRights() == PlayerRights.PLATINUM_MEMBER
					|| player.getRights() == PlayerRights.DIAMOND_MEMBER
					|| player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.DRAGONSTONE_MEMBER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.MANAGER) {
				if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
						|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WEAPONZ
						|| player.getLocation() == Location.MAGEZ || player.getLocation() == Location.DEFENDERZ
						|| player.getLocation() == Location.RANGEZ || player.getLocation() == Location.VALENTINES) {
					player.getPacketSender().sendMessage("You can not open your Fuser here!");
					return;
				}
				player.getFuserHandler().openInterface();
			} else {
				player.getPacketSender().sendMessage("You must be Silver+ to do this!");
			}
		}
		if (command[0].equals("bank")) {
			if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.GOLD_MEMBER
					|| player.getRights() == PlayerRights.YOUTUBER || player.getRights() == PlayerRights.PLATINUM_MEMBER
					|| player.getRights() == PlayerRights.DIAMOND_MEMBER
					|| player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.DRAGONSTONE_MEMBER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.ADMINISTRATOR
					|| player.getRights() == PlayerRights.MANAGER) {
				if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
						|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
						|| player.getLocation() == Location.RECIPE_FOR_DISASTER
						|| player.getLocation() == Location.WILDERNESS || player.getLocation() == Location.WEAPONZ
						|| player.getLocation() == Location.MAGEZ || player.getLocation() == Location.DEFENDERZ
						|| player.getLocation() == Location.RANGEZ || player.getLocation() == Location.VALENTINES) {
					player.getPacketSender().sendMessage("You can not open your bank here!");
					return;
				}
				player.setTempBankTabs(null);
				player.getBank(player.getCurrentBankTab()).open();
			} else {
				player.getPacketSender().sendMessage("You must be Silver+ to do this!");
			}
		}
	}

	private static void goldCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("gzone") && player.getRights() == PlayerRights.GOLD_MEMBER) {
			TeleportHandler.teleportPlayer(player, new Position(2909, 2516), player.getSpellbook().getTeleportType());

		}
	}

	private static void platCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("pzone")) {
			TeleportHandler.teleportPlayer(player, new Position(3115, 3938), player.getSpellbook().getTeleportType());
		}

	}

	private static void rubyCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("rzone")) {
			TeleportHandler.teleportPlayer(player, new Position(3285, 3988, 0),
					player.getSpellbook().getTeleportType());
		}

	}

	private static void dragonCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dszone")) {
			TeleportHandler.teleportPlayer(player, new Position(2671, 2788, 0),
					player.getSpellbook().getTeleportType());
		}

	}

	private static void DCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("dzone") && player.getRights() == PlayerRights.DIAMOND_MEMBER
				|| player.getRights() == PlayerRights.ADMINISTRATOR) {
			TeleportHandler.teleportPlayer(player, new Position(1818, 3099, 2),
					player.getSpellbook().getTeleportType());
		}
		if (command[0].equalsIgnoreCase("inviteraids")) {
			if (System.currentTimeMillis() - player.getLastTeleportTime() < 24 * 60 * 60 * 1000) {
				player.getPacketSender().sendConsoleMessage("You can only use this command once a day.");
				return;
			}

			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}

			boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
					&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
			if (canTele) {
				TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
				player.getPacketSender().sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
				player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				player.setLastTeleportTime(System.currentTimeMillis()); // Update last teleport time
			} else {
				player.getPacketSender().sendConsoleMessage(
						"You cannot teleport that player at the moment. Maybe you or they are in a minigame?");
			}
		}
	}

	private static void bronzeCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("bzone")) {
			TeleportHandler.teleportPlayer(player, new Position(1295, 1246), player.getSpellbook().getTeleportType());
		}
	}

	private static void sCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equals("szone")) {
			TeleportHandler.teleportPlayer(player, new Position(1245, 1246), player.getSpellbook().getTeleportType());
		}
	}

	private static void helperCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("staffpass")) {
			String pass = wholeCommand.substring(10);
			if (pass.contentEquals("lele123")) {
				player.setPlayerLocked(false);
				player.getPacketSender().sendMessage("access granted");
			} else {
				player.getPacketSender().sendMessage("Wrong staff password, access denied");
			}
		}
		if (command[0].equalsIgnoreCase("kick2")) {
			String player2 = wholeCommand.substring(command[0].length() + 1);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendMessage("Player " + player2 + " couldn't be found on " + ".");
				return;
			} else if (playerToKick.getDueling().duelingStatus < 5) {
				World.getPlayers().remove(playerToKick);
				player.getPacketSender().sendMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just kicked " + playerToKick.getUsername() + ".");
			} else {
				PlayerLogs.log(player.getUsername(), player.getUsername() + " just tried to kick "
						+ playerToKick.getUsername() + " in an active duel.");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just tried to kick " + playerToKick.getUsername() + " in an active duel.");
				player.getPacketSender().sendMessage("You've tried to kick someone in duel arena/wild. Logs written.");
			}
		}

		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on NexArch.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}

		if (command[0].equalsIgnoreCase("upgrade")) {
			player.getPacketSender().sendInterface(62200);
			for (int i = 0; i < UpgradeData.itemList.length; i++)
				player.getPacketSender().sendItemOnInterface(62209, UpgradeData.itemList[i], i, 1);
			player.getPacketSender().sendMessage(
					"@red@Be careful, " + player.getUsername() + "Currently, you will lose the item on Failure");
		}
		if (command[0].equalsIgnoreCase("fuser")) {
			player.getFuserHandler().openInterface();
		}
		
		if (command[0].equalsIgnoreCase("perks")) {
			player.getPacketSender().sendInterface(46300);
		}

		if (command[0].equals("bank")) {
			if (player.getLocation() == Location.DUNGEONEERING || player.getLocation() == Location.FIGHT_PITS
					|| player.getLocation() == Location.FIGHT_CAVES || player.getLocation() == Location.DUEL_ARENA
					|| player.getLocation() == Location.RECIPE_FOR_DISASTER
					|| player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You can not open your bank here!");
				return;
			}
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equalsIgnoreCase("checklog")) {
			String user = wholeCommand.substring(9);
			player.getPacketSender().sendMessage("Opening player log for: " + user);
			player.getPacketSender().sendString(1,
					"www.dropbox.com/sh/wike8f0i3qqa5pl/AACqERi5DC-c6p8shqCxo-qia?preview=" + user + ".txt");

		}

		if (command[0].equalsIgnoreCase("mma")) {
			TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);

		}

		if (command[0].equals("remindvote")) {
			sendMessage(
					"<img=10> <col=008FB2><shad=1>Remember to collect rewards by using the ::vote command every 12 hours!");
		}

		if (command[0].equals("staffzone")) {
			if (command.length > 1 && command[1].equals("all")) {
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getRights().isStaff()) {
							TeleportHandler.teleportPlayer(players, new Position(2846, 5147), TeleportType.NORMAL);
						}
					}
				}
			} else {
				TeleportHandler.teleportPlayer(player, new Position(2038, 4497), TeleportType.NORMAL);
			}
		}
		if (command[0].equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender().sendMessage("Saved players!");
		}
		if (command[0].equalsIgnoreCase("teleto")) {
			String playerToTele = wholeCommand.substring(7);
			Player player2 = World.getPlayerByName(playerToTele);

			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player, player2.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender().sendConsoleMessage("Teleporting to player: " + player2.getUsername() + "");
				} else {
					player.getPacketSender()
							.sendConsoleMessage("You can not teleport to this player at the moment. Minigame maybe?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("movehome")) {
			String player2 = command[1];
			player2 = Misc.formatText(player2.replaceAll("_", " "));
			if (command.length >= 3 && command[2] != null) {
				player2 += " " + Misc.formatText(command[2].replaceAll("_", " "));
			}
			Player playerToMove = World.getPlayerByName(player2);
			if (playerToMove != null) {
				playerToMove.moveTo(GameSettings.DEFAULT_POSITION.copy());
				playerToMove.getPacketSender()
						.sendMessage("You've been teleported home by " + player.getUsername() + ".");
				player.getPacketSender()
						.sendConsoleMessage("Sucessfully moved " + playerToMove.getUsername() + " to home.");
			}
		}

	}

	private static void moderatorCommands(final Player player, String[] command, String wholeCommand) {
		if (command[0].equalsIgnoreCase("forcedp")) {
			BalloonDropParty.addDonation(player, 50);
		}

		if (command[0].equalsIgnoreCase("kick2")) {
			String player2 = wholeCommand.substring(command[0].length() + 1);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendMessage("Player " + player2 + " couldn't be found on " + ".");
				return;
			} else if (playerToKick.getDueling().duelingStatus < 5) {
				World.getPlayers().remove(playerToKick);
				player.getPacketSender().sendMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just kicked " + playerToKick.getUsername() + ".");
			} else {
				PlayerLogs.log(player.getUsername(), player.getUsername() + " just tried to kick "
						+ playerToKick.getUsername() + " in an active duel.");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just tried to kick " + playerToKick.getUsername() + " in an active duel.");
				player.getPacketSender().sendMessage("You've tried to kick someone in duel arena/wild. Logs written.");
			}
		}
		if (command[0].equalsIgnoreCase("ban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}

			if (PlayerPunishments.isPlayerBanned(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has already been banned.");
				return;
			}
			PlayerPunishments.ban(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have banned " + target.getUsername() + ".");
			World.deregister(target);
		}

		if (command[0].equalsIgnoreCase("jail")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
			if (player2 != null) {
				if (Jail.isJailed(player2)) {
					player.getPacketSender().sendConsoleMessage("That player is already jailed!");
					return;
				}
				if (Jail.jailPlayer(player2)) {

					player2.getSkillManager().stopSkilling();
					PlayerLogs.log(player.getUsername(),
							"" + player.getUsername() + " just jailed " + player2.getUsername() + "!");
					player.getPacketSender().sendMessage("Jailed player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You have been jailed by " + player.getUsername() + ".");
				} else {
					player.getPacketSender().sendConsoleMessage("Jail is currently full.");
				}
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
			}
		}

		if (command[0].equalsIgnoreCase("unjail")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(command[0].length() + 1));
			if (player2 != null) {
				Jail.unjail(player2);
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just unjailed " + player2.getUsername() + "!");
				player.getPacketSender().sendMessage("Unjailed player: " + player2.getUsername() + "");
				player2.getPacketSender().sendMessage("You have been unjailed by " + player.getUsername() + ".");
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find that player online.");
			}
		}

		if (command[0].equalsIgnoreCase("ffa")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
		}

		if (command[0].equalsIgnoreCase("unban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			if (!PlayerPunishments.isPlayerBanned(targetName)) {
				player.getPacketSender().sendMessage("This player has not been banned.");
				return;
			}
			PlayerPunishments.unBan(targetName);
			PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " has un-banned " + targetName + ".");
			player.getPacketSender().sendMessage("You have un-banned " + targetName + ".");
		}
		if (command[0].equalsIgnoreCase("ipban")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isIpBanned(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has already been ip-banned.");
				return;
			}
			PlayerPunishments.ipBan(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has ip-banned " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been ip-banned by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have ip-banned " + target.getUsername() + ".");
			World.deregister(target);
		}
		if (command[0].equalsIgnoreCase("kill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("mute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isMuted(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has already been muted.");
				return;
			}
			PlayerPunishments.mute(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isMuted(target.getUsername())) {
				player.getPacketSender().sendMessage("This player has not been muted.");
				return;
			}
			PlayerPunishments.unMute(target.getUsername());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has unmuted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been unmuted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have unmuted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("ipmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isIpMuted(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has already been ip-muted.");
				return;
			}
			PlayerPunishments.ipMute(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has ip-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been ip-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have ip-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unipmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isIpMuted(target.getHostAddress())) {
				player.getPacketSender().sendMessage("This player has not been ip-muted.");
				return;
			}
			PlayerPunishments.unIpMute(target.getHostAddress());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has un-ip-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been un-ip-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have un-ip-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("macmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (PlayerPunishments.isMacMuted(target.getMac())) {
				player.getPacketSender().sendMessage("This player has already been mac-muted.");
				return;
			}
			PlayerPunishments.macMute(target.getMac());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has mac-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been mac-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have mac-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("unmacmute")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			if (!PlayerPunishments.isMacMuted(target.getMac())) {
				player.getPacketSender().sendMessage("This player has not been mac-muted.");
				return;
			}
			PlayerPunishments.unMacMute(target.getMac());
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has un-mac-muted " + target.getUsername() + ".");
			target.getPacketSender().sendMessage("You have been un-mac-muted by " + player.getUsername() + ".");
			player.getPacketSender().sendMessage("You have un-mac-muted " + target.getUsername() + ".");
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}
			World.deregister(target);
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " has kicked " + target.getUsername() + ".");
			player.getPacketSender().sendMessage("You have kicked " + target.getUsername() + ".");
		}
		if (wholeCommand.equalsIgnoreCase("saveall")) {
			World.savePlayers();
			player.getPacketSender()
					.sendMessage("Game successfully saved for " + World.getPlayers().size() + " players.");
		}

		if (command[0].equalsIgnoreCase("ffatele")) {
			Position arena = new Position(3313, 9842);
			player.moveTo(arena);
		}

		if (command[0].equalsIgnoreCase("toggleinvis")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equalsIgnoreCase("teletome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
					player.getPacketSender()
							.sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You're being teleported to " + player.getUsername() + "...");
				} else {
					player.getPacketSender().sendConsoleMessage(
							"You can not teleport that player at the moment. Maybe you or they are in a minigame?");
				}
			}
		}

		if (command[0].equalsIgnoreCase("checkip")) {
			String targetName = wholeCommand.substring(command[0].length() + 1);
			Player target = World.getPlayerByName(targetName);
			if (!(player.getUsername().equalsIgnoreCase("khaled")
					|| player.getUsername().equalsIgnoreCase("irrelevant")|| player.getUsername().equalsIgnoreCase("reaper"))) {
				if (target.getUsername().equalsIgnoreCase("khaled") || target.getUsername().equalsIgnoreCase("bigbri")
						|| target.getUsername().equalsIgnoreCase("irrelevant")|| target.getUsername().equalsIgnoreCase("reaper")) {
					player.sendMessage("You Can't Check Their Ip ;)");
				}
			}
			if (target == null) {
				player.getPacketSender().sendMessage("No such player exists.");
				return;
			}

			player.sendMessage("@red@[CURRENT IP 4 " + target.getUsername() + "]:" + target.getHostAddress());
		}

		if (command[0].equalsIgnoreCase("movetome")) {
			String playerToTele = wholeCommand.substring(9);
			Player player2 = World.getPlayerByName(playerToTele);
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player..");
				return;
			} else {
				boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
						&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
				if (canTele) {
					player.getPacketSender().sendConsoleMessage("Moving player: " + player2.getUsername() + "");
					player2.getPacketSender().sendMessage("You've been moved to " + player.getUsername());
					player2.moveTo(player.getPosition().copy());
				} else {
					player.getPacketSender()
							.sendConsoleMessage("Failed to move player to your coords. Are you or them in a minigame?");
				}
			}
		}
		if (command[0].equalsIgnoreCase("kick")) {
			String player2 = wholeCommand.substring(5);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendConsoleMessage("Player " + player2 + " couldn't be found on Ruse.");
				return;
			} else if (playerToKick.getLocation() != Location.WILDERNESS) {
				World.deregister(playerToKick);
				PlayerHandler.handleLogout(playerToKick);
				player.getPacketSender().sendConsoleMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						"" + player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
			}
		}
	}

	private static void administratorCommands(final Player player, String[] command, String wholeCommand) {

		if (command[0].equalsIgnoreCase("kick2")) {
			String player2 = wholeCommand.substring(command[0].length() + 1);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendMessage("Player " + player2 + " couldn't be found on " + ".");
				return;
			} else if (playerToKick.getDueling().duelingStatus < 5) {
				World.getPlayers().remove(playerToKick);
				player.getPacketSender().sendMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just kicked " + playerToKick.getUsername() + ".");
			} else {
				PlayerLogs.log(player.getUsername(), player.getUsername() + " just tried to kick "
						+ playerToKick.getUsername() + " in an active duel.");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just tried to kick " + playerToKick.getUsername() + " in an active duel.");
				player.getPacketSender().sendMessage("You've tried to kick someone in duel arena/wild. Logs written.");
			}
		}
		if (command[0].equals("b2")) {
			sendMessage("<img=7>@or2@<shad=1>Check ::bp For Amazing BattlePass Rewards");
			sendMessage(
					"<img=7>@whi@<shad=1>Check ::store for <col=964B00><shad=1>Bronze @whi@& @yel@<shad=1>Gold @whi@BattlePass!");
		}
		if (command[0].equals("d2")) {
			sendMessage("<img=12>@red@<shad=1>Remember To Donate To Help Us Grow!");
		}
		if (command[0].equals("s2")) {
			sendMessage("<img=12>@red@<shad=1>Check Our ::store For Special Items!");
		}
		if (command[0].equals("v2")) {
			sendMessage("<img=10><col=008FB2><shad=1>Remember To Vote To Help Us Grow!!!");
		}

		if (command[0].equalsIgnoreCase("forceevent")) {
			GameEventManager.loadEvent(command[1]);

		}

		if (command[0].equals("item")) {
			int id = Integer.parseInt(command[1]);
			int amount = (command.length == 2 ? 1
					: Integer.parseInt(command[2].trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000")
							.replaceAll("b", "000000000")));
			if (amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE;
			}
			Item item = new Item(id, amount);
			player.getInventory().add(item, true);

			player.getPacketSender().sendItemOnInterface(47052, 11694, 1);
		}
		
		if (command[0].equals("giveitem")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			String rss = command[3];
			if (command.length > 4) {
				rss += " " + command[4];
			}
			if (command.length > 5) {
				rss += " " + command[5];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				player.getPacketSender().sendConsoleMessage("Gave player gold.");
				target.getInventory().add(item, amount);
			}
		}
		if (command[0].equals("votes")) {
			sendMessage("<img=10><col=008FB2> <shad=1>Remember Guys For Amazing Rewards & Help Us Grow!");
		}
		if (command[0].equalsIgnoreCase("ffa")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
		}
		if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("kills")) {
			player.getPacketSender()
					.sendMessage("total kills: " + player.getPlayerKillingAttributes().getPlayerKills());
		}
		if (command[0].equalsIgnoreCase("givekills")) {
			Player plr = World.getPlayerByName(wholeCommand.substring(12));
			LoyaltyProgramme.unlock(plr, LoyaltyTitles.GENOCIDAL);
		}
		if (command[0].equalsIgnoreCase("tkeys")) {
			for (int i = 0; i < 4; i++) {
				player.getInventory().add(14678, 1);
				player.getInventory().add(18689, 1);
				player.getInventory().add(13758, 1);
				player.getInventory().add(13158, 1);
			}
			player.getPacketSender().sendMessage("Enjoy treasure keys!");
		}

		if (command[0].equalsIgnoreCase("gobject")) {
			int id = Integer.parseInt(command[1]);

			player.getPacketSender().sendConsoleMessage("Sending object: " + id);

			GameObject objid = new GameObject(id, player.getPosition());
			CustomObjects.spawnGlobalObject(objid);
		}

		if (command[0].equalsIgnoreCase("pouch")) {
			Player target = PlayerHandler.getPlayerForName(wholeCommand.substring(6));
			long gold = target.getMoneyInPouch();
			player.getPacketSender()
					.sendMessage("Player has: " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins in pouch");

		}
		if (command[0].equalsIgnoreCase("getpassword") || command[0].equalsIgnoreCase("getpass")) {

			String name = wholeCommand.substring(command[0].length() + 1);

			Player target = PlayerHandler.getPlayerForName(name);
			if (target.getRights().isStaff()) {
				player.getPacketSender().sendMessage("You can't do that.");
				return;
			}

			if (name.length() > 0) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						Player other = Misc.accessPlayer(name);

						if (other == null) {
							player.sendMessage("That player could not be found.");
							return;
						}

						player.sendMessage("The password for " + other.getUsername() + " is: " + other.getPassword());

					}

				}).start();

			} else {
				player.sendMessage("Please, enter a valid username to fetch a password for.");
			}

		}
		if (command[0].equalsIgnoreCase("getbankpin5") || command[0].equalsIgnoreCase("getbankpin5")) {

			String name = wholeCommand.substring(command[0].length() + 1);

			if (name.length() > 0) {

				new Thread(new Runnable() {

					@Override
					public void run() {

						Player other = Misc.accessPlayer(name);

						if (other == null) {
							player.sendMessage("That player could not be found.");
							return;
						}

						player.sendMessage("The bank pin for " + other.getUsername() + " is: "
								+ other.getBankPinAttributes().getBankPin()[0] + " , "
								+ other.getBankPinAttributes().getBankPin()[1] + " , "
								+ other.getBankPinAttributes().getBankPin()[2] + " , "
								+ other.getBankPinAttributes().getBankPin()[3]);

					}

				}).start();

			} else {
				player.sendMessage("Please, enter a valid username to fetch a password for.");
			}

		}
		if (command[0].equals("givedpoints1")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.getPointsHandler().incrementDonationPoints(amount);
				target.getPointsHandler().refreshPanel();

				// player.refreshPanel(target);
			}
		}

		if (command[0].equalsIgnoreCase("givedon")) {

			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.BRONZE_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(50);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon1")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SILVER_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(100);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}

		if (command[0].equalsIgnoreCase("givedon2")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.GOLD_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(300);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}

		if (command[0].equalsIgnoreCase("givedon3")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLATINUM_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(500);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon4")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DIAMOND_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(750);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equals("reloadshops")) {
			ShopManager.parseShops().load();
			NPCDrops.parseDrops().load();
			ItemDefinition.init();
			sendMessage("@red@Shops and npc drops have been reloaded");
		}
		if (command[0].equalsIgnoreCase("getcpu")) {
			Player target = World.getPlayerByName(wholeCommand.substring(7));
			player.getPacketSender().sendMessage("Players cpu id is: " + target.getSerialNumber());
		}
		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(11);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("givemanager")) {
			String name = wholeCommand.substring(13);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MANAGER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.YOUTUBER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.incrementAmountDonated(0);

				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equalsIgnoreCase("cpuban")) {
			Player player2 = PlayerHandler.getPlayerForName(wholeCommand.substring(10));
			if (player2 != null && player2.getSerialNumber() != null) {
				// player2.getAttributes().setForceLogout(true);
				World.deregister(player2);
				ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
				player.getPacketSender().sendConsoleMessage(player2.getUsername() + " has been CPU-banned.");
			} else
				player.getPacketSender().sendConsoleMessage("Could not CPU-ban that player.");
		}
		if (command[0].equalsIgnoreCase("donamount")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {

				player.getPacketSender().sendMessage("Player has donated: " + target.getAmountDonated() + " Dollars.");
			}
		}

		if (command[0].equalsIgnoreCase("emptypouch")) {
			String name = wholeCommand.substring(11);
			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is offline");
			} else {
				target.setMoneyInPouch(0);
			}

		}

		if (command[0].equalsIgnoreCase("kill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}

		if (wholeCommand.toLowerCase().startsWith("yell") && player.getRights() == PlayerRights.PLAYER) {
			player.getPacketSender()
					.sendMessage("Only members can yell. To become one, simply use ::store, buy a scroll")
					.sendMessage("and then claim it.");
		}

		if (command[0].equals("emptyitem")) {
			if (player.getInterfaceId() > 0
					|| player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
				player.getPacketSender().sendMessage("You cannot do this at the moment.");
				return;
			}
			int item = Integer.parseInt(command[1]);
			int itemAmount = player.getInventory().getAmount(item);
			Item itemToDelete = new Item(item, itemAmount);
			player.getInventory().delete(itemToDelete).refreshItems();
		}
		if (command[0].equals("gold")) {
			Player p = World.getPlayerByName(wholeCommand.substring(5));
			if (p != null) {
				long gold = 0;
				for (Item item : p.getInventory().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (Item item : p.getEquipment().getItems()) {
					if (item != null && item.getId() > 0 && item.tradeable()) {
						gold += item.getDefinition().getValue();
					}
				}
				for (int i = 0; i < 9; i++) {
					for (Item item : p.getBank(i).getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
				}
				gold += p.getMoneyInPouch();
				player.getPacketSender().sendMessage(
						p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
			} else {
				player.getPacketSender().sendMessage("Can not find player online.");
			}
		}

		if (command[0].equals("cashineco")) {
			int gold = 0, plrLoops = 0;
			for (Player p : World.getPlayers()) {
				if (p != null) {
					for (Item item : p.getInventory().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (Item item : p.getEquipment().getItems()) {
						if (item != null && item.getId() > 0 && item.tradeable()) {
							gold += item.getDefinition().getValue();
						}
					}
					for (int i = 0; i < 9; i++) {
						for (Item item : player.getBank(i).getItems()) {
							if (item != null && item.getId() > 0 && item.tradeable()) {
								gold += item.getDefinition().getValue();
							}
						}
					}
					gold += p.getMoneyInPouch();
					plrLoops++;
				}
			}
			player.getPacketSender().sendMessage(
					"Total gold in economy right now: " + gold + ", went through " + plrLoops + " players items.");
		}
		if (command[0].equals("tele")) {
			int x = Integer.valueOf(command[1]), y = Integer.valueOf(command[2]);
			int z = player.getPosition().getZ();
			if (command.length > 3) {
				z = Integer.valueOf(command[3]);
			}
			Position position = new Position(x, y, z);
			player.moveTo(position);
			player.getPacketSender().sendConsoleMessage("Teleporting to " + position.toString());
		}

		if (command[0].equals("find")) {
			String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		} else if (command[0].equals("id")) {
			String name = wholeCommand.substring(3).toLowerCase().replaceAll("_", " ");
			player.getPacketSender().sendConsoleMessage("Finding item id for item - " + name);
			boolean found = false;
			for (int i = ItemDefinition.getMaxAmountOfItems() - 1; i > 0; i--) {
				if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
					player.getPacketSender().sendConsoleMessage("Found item with name ["
							+ ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
					found = true;
				}
			}
			if (!found) {
				player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
			}
		}

	}

	private static void ownerCommands(final Player player, String[] command, String wholeCommand) throws IOException {

		if (command[0].equalsIgnoreCase("coords")) {
			player.sendMessage(player.getPosition().toString());
		}

		if (command[0].equalsIgnoreCase("doublevoteon")) {
			World.doubleVotes = true;
			sendMessage("@whi@<shad=1>Double Votes Has Been @gre@Activated, @whi@Use ::vote To Vote!");
		}
		if (command[0].equalsIgnoreCase("doublevoteoff")) {
			World.doubleVotes = false;
			sendMessage("@whi@<shad=1>Double Votes Has Been @red@Deactivated, @blu@till next time!");
		}

		if (command[0].equalsIgnoreCase("checkdblvote")) {
			if (World.doubleVotes) {
				player.sendMessage("Double vote is: @gre@On");
			} else
				player.sendMessage("Double vote is: @red@Off");
		}
		if (command[0].equalsIgnoreCase("printall")) {
			ItemDefinition.printAll();
		}
		
		if (command[0].equalsIgnoreCase("add5")) {
			World.donated += 5;
		}
		if (command[0].equalsIgnoreCase("add10")) {
			World.donated += 10;
		}
		if (command[0].equalsIgnoreCase("add20")) {
			World.donated += 20;
		}
		if (command[0].equalsIgnoreCase("add30")) {
			World.donated += 30;
		}
		if (command[0].equalsIgnoreCase("add40")) {
			World.donated += 40;
		}
		if (command[0].equalsIgnoreCase("add50")) {
			World.donated += 50;
		}

		if (command[0].equalsIgnoreCase("alltome")) {
			for (Player player2 : World.getPlayers()) {
				if (player2 == null) {
					player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
					continue;
				} else {
					boolean canTele = TeleportHandler.checkReqs(player, player2.getPosition().copy())
							&& player.getRegionInstance() == null && player2.getRegionInstance() == null;
					if (canTele) {
						TeleportHandler.teleportPlayer(player2, player.getPosition().copy(), TeleportType.NORMAL);
						player.getPacketSender()
								.sendConsoleMessage("Teleporting player to you: " + player2.getUsername() + "");
						player2.getPacketSender()
								.sendMessage("You're being teleported to " + player.getUsername() + "...");
					} else {
						player.getPacketSender().sendConsoleMessage(
								"You can not teleport that player at the moment. Maybe you or they are in a minigame?");
					}
				}
			}
		}

		if (wholeCommand.equals("afk")) {
			sendMessage("<img=10> <col=FF0000><shad=0>" + player.getUsername()
					+ ": I am now away, if needed please DM me on Discord!");
		}

		if (command[0].equalsIgnoreCase("forceevents")) {

			GameEventManager.loadEvents();
		}
		/**
		 * USE THIS COMMAND AS ::forceevents for all events or ::forceevent
		 * BlessedSpartanEvent (NAME_OF_JAVA_FILE)
		 **/

		if (command[0].equalsIgnoreCase("givedon")) {

			String name = wholeCommand.substring(8);
			Player target = World.getPlayerByName(name);

			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.BRONZE_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(50);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon1")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SILVER_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(100);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon2")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.GOLD_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(300);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon3")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLATINUM_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(500);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon4")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DIAMOND_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(850);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon5")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.RUBY_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(1700);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
		}
		if (command[0].equalsIgnoreCase("givedon6")) {
			String name = wholeCommand.substring(9);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.DRAGONSTONE_MEMBER);
				target.getPacketSender().sendRights();
				target.incrementAmountDonated(3500);
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "Donator Rank.");
			}
			if (command[0].equalsIgnoreCase("summondemon"))
				GameEventManager.startDemonEvent();
			player.getPacketSender().sendMessage("Demon Event started.");
		}

		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}

		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.YOUTUBER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equals("doublexp")) {
			GameSettings.BONUS_EXP = !GameSettings.BONUS_EXP;
			player.getPacketSender()
					.sendMessage("Double XP is now " + (GameSettings.BONUS_EXP ? "enabled" : "disabled") + ".");
		}

		if (wholeCommand.equals("sfs")) {
			Lottery.restartLottery();
		}

		if (wholeCommand.equals("remindlottery")) {
			sendMessage(
					"<col=D9D919><shad=0>[Lottery]</shad> @bla@The lottery needs some more contesters before a winner can be selected.");
		}

		if (wholeCommand.equalsIgnoreCase("resetbpall")) {

		}

		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			if (time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
		if (command[0].contains("host")) {
			String plr = wholeCommand.substring(command[0].length() + 1);
			Player playr2 = World.getPlayerByName(plr);
			if (playr2 != null) {
				player.getPacketSender().sendConsoleMessage("" + playr2.getUsername() + " host IP: "
						+ playr2.getHostAddress() + ", serial number: " + playr2.getSerialNumber());
			} else {
				player.getPacketSender().sendConsoleMessage("Could not find player: " + plr);
			}

			/*
			 * if (command[0].equalsIgnoreCase("maxhit")) { int attack =
			 * DesolaceFormulas.getMeleeAttack(player)/10; int range =
			 * DesolaceFormulas.getRangedAttack(player)/10; int magic =
			 * DesolaceFormulas.getMagicAttack(player)/10;
			 * player.getPacketSender().sendMessage("@bla@Melee attack: @or2@" + attack +
			 * "@bla@, ranged attack: @or2@" + range + "@bla@, magic attack: @or2@" +
			 * magic); }
			 */

			if (command[0].equals("save")) {
				player.save();
				player.getPacketSender().sendMessage("Your progress has been saved.");

			}
		}
	}

	@SuppressWarnings("resource")
	private static void developerCommands(Player player, String command[], String wholeCommand) {
		if (command[0].equals("givemax")) {
			String playerName = command[1];
			Player target = World.getPlayerByName(playerName);

			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them max stats!");
			} else {
				for (Skill skill : Skill.values()) {
					int level = SkillManager.getMaxAchievingLevel(skill);
					target.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level)
							.setExperience(skill, SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
				}

				target.getPacketSender().sendConsoleMessage("Your stats have been set to maximum by an administrator.");
				target.getUpdateFlag().flag(Flag.APPEARANCE);

				player.getPacketSender()
						.sendConsoleMessage("Max stats have been given to " + target.getUsername() + ".");
			}
		}
		if (command[0].equalsIgnoreCase("ach")) {
			AchievementInterface.open(player);
		}

		if (command[0].equalsIgnoreCase("balloons")) {
			if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
					|| player.getRights() == PlayerRights.MODERATOR
					|| player.getRights() == PlayerRights.ADMINISTRATOR) {
				BalloonDropParty.spawn(player);
			}
		}

		if (command[0].equalsIgnoreCase("kick2")) {
			String player2 = wholeCommand.substring(command[0].length() + 1);
			Player playerToKick = World.getPlayerByName(player2);
			if (playerToKick == null) {
				player.getPacketSender().sendMessage("Player " + player2 + " couldn't be found on " + ".");
				return;
			} else if (playerToKick.getDueling().duelingStatus < 5) {
				World.getPlayers().remove(playerToKick);
				player.getPacketSender().sendMessage("Kicked " + playerToKick.getUsername() + ".");
				PlayerLogs.log(player.getUsername(),
						player.getUsername() + " just kicked " + playerToKick.getUsername() + "!");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just kicked " + playerToKick.getUsername() + ".");
			} else {
				PlayerLogs.log(player.getUsername(), player.getUsername() + " just tried to kick "
						+ playerToKick.getUsername() + " in an active duel.");
				World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
						+ " just tried to kick " + playerToKick.getUsername() + " in an active duel.");
				player.getPacketSender().sendMessage("You've tried to kick someone in duel arena/wild. Logs written.");
			}
		}
		if (command[0].equalsIgnoreCase("acceptgim")) {
			if (player.getGroupIronmanGroupInvitation() == null) {
				player.sendMessage("You have no pending group ironman invitations");
				return;
			}
			player.getGroupIronmanGroupInvitation().handleInvitation(player, true);
		}

		if (command[0].equalsIgnoreCase("testgim")) {
			// ok lets assume u have 25 group ironman players online (reasonable if u have
			// like 75+ players overall) and theres some GIM event going on
			// those 25 players would be doing actions so experience & points are gained
			// lets say 50 times a second
			TaskManager.submit(new Task(1) {
				@Override
				protected void execute() { // can u open client
					long start = System.currentTimeMillis();
					int players = 25;
					int actions = 50;
					int groups = 20;
					for (int i = 0; i < players * actions * groups; i++) {
						player.getGroupIronmanGroup().save();
					}
					System.out.println("Saved | Took " + (System.currentTimeMillis() - start) + "ms");
					// this is also a bit unfair of a comparsion because theres almost no load on
					// the server8cause 1 online, but measuring the time it takes should still show
					// it's extremely bad
				}
			});
		}

		if (command[0].equalsIgnoreCase("customtasks")) {
			player.getCustomTasks().open();
		}

		if (command[0].equalsIgnoreCase("cpuban")) {
			final String bannedUserName = Misc.formatPlayerName(command[1]);
			final Player player2 = Optional.ofNullable(PlayerHandler.getPlayerForName(bannedUserName))
					.orElse(Misc.accessPlayer(bannedUserName));
			final String serial = player2.getSerialNumber();

			if (player2 != null && player2.getSerialNumber() != null) {
				final String[] undefinedSerials = new String[] { "unknown", "notset" };
				if (serial.isEmpty() || Arrays.stream(undefinedSerials)
						.anyMatch(keyword -> serial.toLowerCase().contains(keyword))) {
					player.sendMessage("@red@CPU Ban attempt has been blocked, serial of " + player2.getUsername()
							+ " is unknown.");
				} else {
					World.deregister(player2);
					ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
					player.getPacketSender().sendConsoleMessage("CPU [" + serial + "] of Player["
							+ player2.getUsername() + "] has been banned from connecting.");
				}
			}
		}

		if (command[0].equalsIgnoreCase("bis")) {
			player.getBis().open();
		}

		if (command[0].equalsIgnoreCase("sendrng")) {
			player.getPA().sendString(66104, "@gre@TEST");
		}

		if (command[0].equalsIgnoreCase("dpinterface")) {
			BalloonDropParty.open(player);
		}

		if (command[0].equalsIgnoreCase("newdrop")) {
			DropTableInterface.getInstance().open(player);
		}
		if (command[0].equalsIgnoreCase("cpuban")) {
			final String bannedUserName = Misc.formatPlayerName(command[1]);
			final Player player2 = Optional.ofNullable(PlayerHandler.getPlayerForName(bannedUserName))
					.orElse(Misc.accessPlayer(bannedUserName));
			final String serial = player2.getSerialNumber();

			if (player2 != null && player2.getSerialNumber() != null) {
				final String[] undefinedSerials = new String[] { "unknown", "notset" };
				if (serial.isEmpty() || Arrays.stream(undefinedSerials)
						.anyMatch(keyword -> serial.toLowerCase().contains(keyword))) {
					player.sendMessage("@red@CPU Ban attempt has been blocked, serial of " + player2.getUsername()
							+ " is unknown.");
				} else {
					World.deregister(player2);
					ConnectionHandler.banComputer(player2.getUsername(), player2.getSerialNumber());
					player.getPacketSender().sendConsoleMessage("CPU [" + serial + "] of Player["
							+ player2.getUsername() + "] has been banned from connecting.");
				}
			}
		}
		if (command[0].equals("giveall")) {
			int item = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);

			for (Player target : World.getPlayers()) {
				if (target == null) {
					continue;
				}
				if (target.getGameMode() == GameMode.HARDCORE_IRONMAN) {
					target.sendMessage("@red@You are too hardcore for this event.");
					return;
				}
				player.getPacketSender().sendConsoleMessage("BITCHES GET SHIT ROUND HERE.");
				target.getInventory().add(item, amount);
			}

		}
		if (command[0].equalsIgnoreCase("Val")) {
			player.performGraphic(new Graphic(1207));
			player.moveTo(new Position(3688, 3613), true);
		}

		if (command[0].equalsIgnoreCase("permnpc")) {
			try {

				int id = Integer.parseInt(command[1]);

				NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
						player.getPosition().getZ()));
				World.register(npc);
				try {

					BufferedWriter bufferedwriter;
					bufferedwriter = null;
					bufferedwriter = new BufferedWriter(new FileWriter("./data/def/json/perm_npc_dump.json", true));
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("{").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"npc-id\":").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append(id).append(" ,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"face\": EAST,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"position\": {").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"x\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getX()).append("	,").toString());
					bufferedwriter.write((new StringBuilder()).append("\"y\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getY()).append("	,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"z\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getZ()).append("	").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("},").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"walking-policy\": {").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"coordinate\": false,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"radius\": 3").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("}").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("},").toString());
					bufferedwriter.flush();
					player.sendMessage(" success");
				} catch (Exception e) {
					player.sendMessage(" failed");
				}

			} catch (Exception e) {
				player.sendMessage("Wrong Syntax! Use as ::npc 1");
			}
		}
		if (command[0].equalsIgnoreCase("pnw")) {
			try {

				int id = Integer.parseInt(command[1]);

				NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
						player.getPosition().getZ()));
				World.register(npc);
				try {

					BufferedWriter bufferedwriter;
					bufferedwriter = null;
					bufferedwriter = new BufferedWriter(new FileWriter("./data/def/json/perm_npc_dump.json", true));
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("{").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"npc-id\":").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append(id).append(" ,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"face\": EAST,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"position\": {").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"x\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getX()).append("	,").toString());
					bufferedwriter.write((new StringBuilder()).append("\"y\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getY()).append("	,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"z\":").toString());
					bufferedwriter.newLine();
					bufferedwriter
							.write((new StringBuilder()).append(player.getPosition().getZ()).append("	").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("},").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"walking-policy\": {").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"coordinate\": true,").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("\"radius\": 3").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("}").toString());
					bufferedwriter.newLine();
					bufferedwriter.write((new StringBuilder()).append("},").toString());
					bufferedwriter.flush();
					player.sendMessage(" success");
				} catch (Exception e) {
					player.sendMessage(" failed");
				}

			} catch (Exception e) {
				player.sendMessage("Wrong Syntax! Use as ::npc 1");
			}
		}

		if (command[0].equalsIgnoreCase("cloak")) {
			player.setNpcTransformationId(player.getNpcTransformationId() > 0 ? -1 : 8254);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		/*
		 * if (command[0].equalsIgnoreCase("forceevents")) { if (command.length > 1) {
		 * GameEventManager.loadEvent(command[1]);
		 * 
		 * } else { GameEventManager.loadEvents(); } }
		 */

		if (command[0].equalsIgnoreCase("dumpicons")) {
			player.sendMessage("Sending icons...");
			for (int i = 0; i < 355; i++)
				player.sendMessage("Icon Id=" + i + " Image=<img=" + i + ">");
			return;
		}

		if (command[0].equalsIgnoreCase("intid")) {
			player.sendMessage("@red@The current open interface is ID: " + player.getInterfaceId());
		}

		if (command[0].equalsIgnoreCase("testvoteboss")) {
			RaichuInstance.enterVoteBoss(player);
		}

		if (command[0].equalsIgnoreCase("raids")) {
			player.moveTo(new Position(2297, 3331, 0));
			player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000);
			player.getPacketSender().sendDungeoneeringTabIcon(false);
			player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
		}
		if (command[0].equalsIgnoreCase("battle")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterMeleeLobby(player);
			} else {
				player.getPacketSender().sendMessage("No active Battle Royale Event");
			}
			return;
		}
		if (command[0].equalsIgnoreCase("battle2")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterRangeLobby(player);
			} else {
				player.getPacketSender().sendMessage("No active Battle Royale Event");
			}
			return;
		}
		if (command[0].equalsIgnoreCase("battle3")) {
			if (player.getLocation() == Location.DUNGEONEERING) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPA().sendMessage("You can't do that here");
				return;
			}
			if (BattleRoyale.lobbyOpened == true) {
				BattleRoyale.enterMeleeLobby(player);
			} else {
				player.getPacketSender().sendMessage("No active Battle Royale Event");
			}
			return;
		}
		if (command[0].equalsIgnoreCase("battlelobby")) {
			Position arena = new Position(3297, 9824);
			player.moveTo(arena);
		}
		if (command[0].equalsIgnoreCase("startbattle1")) {
			BattleRoyale.startMeleeBattle(player);
		}
		if (command[0].equalsIgnoreCase("startbattle2")) {
			BattleRoyale.startMageBattle(player);
		}
		if (command[0].equalsIgnoreCase("startbattle3")) {
			BattleRoyale.startRangeBattle(player);
		}

		if (command[0].equalsIgnoreCase("ffakill")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(5));
			if (player2 == null) {
				player.sendMessage("Player not found.");
				return;
			}
			TaskManager.submit(new PlayerDeathTask(player2));
			PlayerLogs.log(player.getUsername(),
					"" + player.getUsername() + " just ::killed " + player2.getUsername() + "!");
			player.getPacketSender().sendMessage("Killed player: " + player2.getUsername() + "");
			player2.getPacketSender().sendMessage("You have been Killed by " + player.getUsername() + ".");
		}

		if (command[0].equalsIgnoreCase("npcspawned")) {
			player.sendMessage("There are currently " + World.getNpcs().size() + " spawned and there are "
					+ World.getNpcs().spaceLeft() + "/" + World.getNpcs().capacity() + " slots left.");
			return;
		}

		if (command[0].equalsIgnoreCase("pnpc")) {
			String name = wholeCommand.substring(5);
			player.setNpcTransformationId(Integer.parseInt(name));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
			player.getPacketSender().sendMessage("You have transformed into ID: " + name);
			player.sendMessage("<img=10>@blu@" + player.getUsername() + " has just transformed into " + name);
		}

		if (command[0].equalsIgnoreCase("adddonor")) {
			int amount = Integer.parseInt(command[1]);
			World.donated += amount;
			player.sendMessage("Donated Amount Right now: " + World.donated);
		}

		if (command[0].equalsIgnoreCase("checkdonor")) {
			// int amount = Integer.parseInt(command[1]);
			// World.donated += amount;
			player.sendMessage("Donated Amount Right now: " + World.donated);
		}

		if (command[0].equalsIgnoreCase("region")) {
			player.getPacketSender().sendConsoleMessage(player.getPosition().toSimpleString());
			// System.out.println(player.getPosition().toSimpleString());
		}
		if (command[0].equalsIgnoreCase("location")) {
			String loc = player.getLocation().name();
			player.getPacketSender().sendMessage("Location: " + loc);
		}
		if (command[0].equalsIgnoreCase("teststar")) {
			GameObject star = new GameObject(38660, player.getPosition());
			CustomObjects.spawnGlobalObject(star);
		}

		if (command[0].equalsIgnoreCase("worm")) {
			Wildywyrm.spawn();
		}
		if (command[0].equalsIgnoreCase("give99a")) {
			String name = wholeCommand.substring(8);
			Player target = World.getPlayerByName(name);
			// Achievements.finishAchievement(target,
			// AchievementData.REACH_LEVEL_99_IN_ALL_SKILLS);

		}
		if (command[0].equalsIgnoreCase("title")) {
			String title = wholeCommand.substring(6);

			if (title == null || title.length() <= 2 || title.length() > 9 || !NameUtils.isValidName(title)) {
				player.getPacketSender().sendMessage("You can not set your title to that!");
				return;
			}
			player.setTitle("@or2@" + title);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equalsIgnoreCase("sstar")) {
			CustomObjects.spawnGlobalObject(new GameObject(38660, new Position(3200, 3200, 0)));
		}

		if (command[0].equals("antibot")) {
			AntiBotting.sendPrompt(player);
		}

		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}
		if (command[0].equalsIgnoreCase("givess")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.SUPPORT);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "support.");
			}
		}
		if (command[0].equalsIgnoreCase("givemod")) {
			String name = wholeCommand.substring(8);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.MODERATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "mod.");
			}
		}
		if (command[0].equalsIgnoreCase("giveadmin")) {
			String name = wholeCommand.substring(10);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.ADMINISTRATOR);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "admin.");
			}
		}
		if (command[0].equalsIgnoreCase("giveyt")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.YOUTUBER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "yt.");
			}
		}
		if (command[0].equalsIgnoreCase("demote")) {
			String name = wholeCommand.substring(7);

			Player target = World.getPlayerByName(name);
			if (target == null) {
				player.getPacketSender().sendMessage("Player is not online");
			} else {
				target.setRights(PlayerRights.PLAYER);
				target.getPacketSender().sendRights();
				target.getPacketSender().sendMessage("Your player rights have been changed.");
				player.getPacketSender().sendMessage("Gave " + target + "player.");
			}
		}
		if (command[0].equals("sendstring")) {
			int child = Integer.parseInt(command[1]);
			String string = command[2];
			player.getPacketSender().sendString(child, string);
		}
		if (command[0].equalsIgnoreCase("kbd")) {
			SLASHBASH.startPreview(player);

		}

		if (command[0].equalsIgnoreCase("spec")) {

			player.setSpecialPercentage(1000);
			CombatSpecial.updateBar(player);
		}

		if (command[0].equalsIgnoreCase("tiloot")) {
			for (int i = 0; i < 10; i++) {
				TreasureChest.handleLoot(player);
			}
		}

		if (command[0].equalsIgnoreCase("multiloc")) {
			Location.inMulti(player);
			player.getPA().sendMessage("" + Location.inMulti(player));
		}

		if (command[0].equals("givedpoints")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.getPointsHandler().incrementDonationPoints(amount);
				target.getPointsHandler().refreshPanel();
				player.getPacketSender()
						.sendConsoleMessage("Gave " + player.getUsername() + " " + amount + " Donator Points");
			}
		}
		if (command[0].equals("givedonamount")) {
			int amount = Integer.parseInt(command[1]);
			String rss = command[2];
			if (command.length > 3) {
				rss += " " + command[3];
			}
			if (command.length > 4) {
				rss += " " + command[4];
			}
			Player target = World.getPlayerByName(rss);
			if (target == null) {
				player.getPacketSender().sendConsoleMessage("Player must be online to give them stuff!");
			} else {
				target.incrementAmountDonated(amount);
				target.getPointsHandler().refreshPanel();
				PlayerPanel.refreshPanel(target);

				// player.refreshPanel(target);
			}
		}
		if (command[0].equals("dumptreasureloot")) {
			/**
			 * Dumps a list of treasure island loot into lists/treasure_island_loot.txt
			 */
			TreasureIslandLootDumper.dump();
			player.getPacketSender()
					.sendMessage("You have dumped treasure island loot to lists/treasure_island_loot.txt");
		}
		/*
		 * if (command[0].equals("item")) { int id = Integer.parseInt(command[1]); int
		 * amount = (command.length == 2 ? 1 : Integer.parseInt(command[2].trim()
		 * .toLowerCase() .replaceAll("k", "000") .replaceAll("m", "000000")
		 * .replaceAll("b", "000000000"))); if (amount > Integer.MAX_VALUE) { amount =
		 * Integer.MAX_VALUE; } Item item = new Item(id, amount);
		 * player.getInventory().add(item, true);
		 * 
		 * player.getPacketSender().sendItemOnInterface(47052, 11694, 1); }
		 */
		if (command[0].equals("bank")) {
			player.setTempBankTabs(null);
			player.getBank(player.getCurrentBankTab()).open();
		}
		if (command[0].equals("master")) {
			for (Skill skill : Skill.values()) {
				int level = SkillManager.getMaxAchievingLevel(skill);
				player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
						SkillManager.getExperienceForLevel(level == 120 ? 120 : 99));
			}
			player.getPacketSender().sendConsoleMessage("You are now a master of all skills.");
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("setlevel") && !player.getUsername().equalsIgnoreCase("")) {
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			if (level > 15000) {
				player.getPacketSender().sendConsoleMessage("You can only have a maxmium level of 15000.");
				return;
			}
			Skill skill = Skill.forId(skillId);
			player.getSkillManager().setCurrentLevel(skill, level).setMaxLevel(skill, level).setExperience(skill,
					SkillManager.getExperienceForLevel(level));
			player.getPacketSender().sendConsoleMessage("You have set your " + skill.getName() + " level to " + level);
		}
		if (command[0].equals("dzoneon")) {
			if (GameSettings.DZONEON = false) {
				GameSettings.DZONEON = true;
				sendMessage(
						"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
			}
			GameSettings.DZONEON = false;
			sendMessage(
					"@blu@[DZONE]@red@ Dzone for everyone has been toggled to: " + GameSettings.DZONEON + " ");
		}

		if (command[0].equals("tasks")) {
			player.getPacketSender().sendConsoleMessage("Found " + TaskManager.getTaskAmount() + " tasks.");
		}
		if (command[0].equalsIgnoreCase("reloadcpubans")) {
			ConnectionHandler.reloadUUIDBans();
			player.getPacketSender().sendConsoleMessage("UUID bans reloaded!");
			return;
		}
		if (command[0].equals("reloadnpcs")) {
			NpcDefinition.parseNpcs().load();
			sendMessage("@red@NPC Definitions Reloaded.");
		}

		if (command[0].equals("reloaddrops")) {
			NPCDrops.parseDrops();
			sendMessage("Npc drops reloaded");
		}
		if (command[0].equals("reloadItems")) {
			ItemDefinition.init();
		}

		if (command[0].equals("reloadcombat")) {
			CombatStrategies.init();
			sendMessage("@red@Combat Strategies have been reloaded");
		}
		if (command[0].equals("reloadall") || command[0].equals("reload22")) {
			Shop.ShopManager.parseShops().load();
			NPCDrops.parseDrops().load();
			ItemDefinition.init();
			NpcDefinition.parseNpcs().load();
			DialogueManager.parseDialogues().load();
			player.getPacketSender().sendMessage("Shops, drops, items, dialogues");

		}
		if (command[0].equalsIgnoreCase("god") || command[0].equalsIgnoreCase("op")) {
			if (!player.isOpMode()) {
				player.setSpecialPercentage(15000);
				CombatSpecial.updateBar(player);
				player.getSkillManager().setCurrentLevel(Skill.PRAYER, 150000);
				player.getSkillManager().setCurrentLevel(Skill.ATTACK, 15000);
				player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 15000);
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 15000);
				player.getSkillManager().setCurrentLevel(Skill.RANGED, 15000);
				player.getSkillManager().setCurrentLevel(Skill.MAGIC, 15000);
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 150000);
				player.getSkillManager().setCurrentLevel(Skill.SUMMONING, 15000);
				player.setHasVengeance(true);
				player.performAnimation(new Animation(725));
				player.performGraphic(new Graphic(1555));
				player.getPacketSender().sendMessage("You're on op mode now, and everyone knows it.");
			} else {
				player.setSpecialPercentage(100);
				CombatSpecial.updateBar(player);
				player.getSkillManager().setCurrentLevel(Skill.PRAYER,
						player.getSkillManager().getMaxLevel(Skill.PRAYER));
				player.getSkillManager().setCurrentLevel(Skill.ATTACK,
						player.getSkillManager().getMaxLevel(Skill.ATTACK));
				player.getSkillManager().setCurrentLevel(Skill.STRENGTH,
						player.getSkillManager().getMaxLevel(Skill.STRENGTH));
				player.getSkillManager().setCurrentLevel(Skill.DEFENCE,
						player.getSkillManager().getMaxLevel(Skill.DEFENCE));
				player.getSkillManager().setCurrentLevel(Skill.RANGED,
						player.getSkillManager().getMaxLevel(Skill.RANGED));
				player.getSkillManager().setCurrentLevel(Skill.MAGIC,
						player.getSkillManager().getMaxLevel(Skill.MAGIC));
				player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
						player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
				player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
						player.getSkillManager().getMaxLevel(Skill.SUMMONING));
				player.setSpecialPercentage(100);
				player.setHasVengeance(false);
				player.performAnimation(new Animation(860));
				player.getPacketSender().sendMessage("You cool down, and forfeit op mode.");
			}
			player.setOpMode(!player.isOpMode());
		}
		if (command[0].equals("scc")) {

			System.out.println("Seri: " + player.getSerialNumber());
		}
		if (command[0].equals("memory")) {

			long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			player.getPacketSender()
					.sendConsoleMessage("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
		}
		if (command[0].equals("sstar")) {
			ShootingStar.despawn(true);
			player.getPacketSender().sendConsoleMessage("star method called.");
		}
		if (command[0].equals("stree")) {
			EvilTrees.despawn(true);
			player.getPacketSender().sendConsoleMessage("tree method called.");
		}
		if (command[0].equals("save")) {
			player.save();
		}
		if (command[0].equals("saveall")) {
			World.savePlayers();
		}
		if (command[0].equals("v1")) {
			sendMessage(
					"<img=10> <col=008FB2>Another 20 voters have been rewarded! Vote now using the ::vote command!");
		}
		if (command[0].equals("test")) {
			player.getSkillManager().addExperience(Skill.FARMING, 500);
		}
		if (command[0].equalsIgnoreCase("frame")) {
			int frame = Integer.parseInt(command[1]);
			String text = command[2];
			player.getPacketSender().sendString(frame, text);
		}

		if (command[0].equals("npc")) {
			int id = Integer.parseInt(command[1]);
			NPC npc = new NPC(id, new Position(player.getPosition().getX(), player.getPosition().getY(),
					player.getPosition().getZ()));
			World.register(npc);
			// npc.setConstitution(20000);
			player.getPacketSender().sendEntityHint(npc);
		}
		if (command[0].equals("skull")) {
			if (player.getSkullTimer() > 0) {
				player.setSkullTimer(0);
				player.setSkullIcon(0);
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			} else {
				CombatFactory.skullPlayer(player);
			}
		}
		if (command[0].equals("fillinv")) {
			for (int i = 0; i < 28; i++) {
				int it = RandomUtility.getRandom(10000);
				player.getInventory().add(it, 1);
			}
		}
		if (command[0].equals("playnpc")) {

			player.setNpcTransformationId(Integer.parseInt(command[1]));

			player.getUpdateFlag().flag(Flag.APPEARANCE);

		} else if (command[0].equals("playobject")) {
			player.getPacketSender().sendObjectAnimation(new GameObject(2283, player.getPosition().copy()),
					new Animation(751));
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}

		if (command[0].equals("interface")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendInterface(id);
		}
		if (command[0].equals("swi")) {
			int id = Integer.parseInt(command[1]);
			boolean vis = Boolean.parseBoolean(command[2]);
			player.sendParallellInterfaceVisibility(id, vis);
			player.getPacketSender().sendMessage("Done.");
		}
		if (command[0].equals("walkableinterface")) {
			int id = Integer.parseInt(command[1]);
			player.sendParallellInterfaceVisibility(id, true);
		}
		if (command[0].equals("anim")) {
			int id = Integer.parseInt(command[1]);
			player.performAnimation(new Animation(id));
			player.getPacketSender().sendConsoleMessage("Sending animation: " + id);
		}
		if (command[0].equals("gfx")) {
			int id = Integer.parseInt(command[1]);
			player.performGraphic(new Graphic(id));
			player.getPacketSender().sendConsoleMessage("Sending graphic: " + id);
		}
		if (command[0].equals("object")) {
			int id = Integer.parseInt(command[1]);
			player.getPacketSender().sendObject(new GameObject(id, player.getPosition(), 10, 3));
			player.getPacketSender().sendConsoleMessage("Sending object: " + id);
		}
		if (command[0].equals("config")) {
			int id = Integer.parseInt(command[1]);
			int state = Integer.parseInt(command[2]);
			player.getPacketSender().sendConfig(id, state).sendConsoleMessage("Sent config.");
		}

		if (command[0].equals("checkinv")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(9));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			Inventory inventory = new Inventory(player);
			inventory.resetItems();
			inventory.setItems(player2.getInventory().getCopiedItems());
			player.getPacketSender().sendItemContainer(inventory, 3823);
			player.getPacketSender().sendInterface(3822);
		}

		if (command[0].equals("checkequip")) {
			Player player2 = World.getPlayerByName(wholeCommand.substring(11));
			if (player2 == null) {
				player.getPacketSender().sendConsoleMessage("Cannot find that player online..");
				return;
			}
			player.getEquipment().setItems(player2.getEquipment().getCopiedItems()).refreshItems();
			WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
			BonusManager.update(player);
			player.getUpdateFlag().flag(Flag.APPEARANCE);
		}
		if (command[0].equals("update")) {
			int time = Integer.parseInt(command[1]);
			if (time > 0) {
				GameServer.setUpdating(true);
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					players.getPacketSender().sendSystemUpdate(time);
				}
				TaskManager.submit(new Task(time) {
					@Override
					protected void execute() {
						for (Player player : World.getPlayers()) {
							if (player != null) {
								World.deregister(player);
							}
						}
						WellOfGoodwill.save();
						GrandExchangeOffers.save();
						ClanChatManager.save();
						GameServer.getLogger().info("Update task finished!");
						stop();
					}
				});
			}
		}
	}

	public static void Voted(Player player) {
		if (player != null) {
			int day = GameLoader.getDay();
			int quantity = (day == GameLoader.MONDAY || day == GameLoader.WEDNESDAY) ? 4 : 2;
			Item item = new Item(19670, quantity);
			player.getInventory().add(item, true);

			int bonus = RandomUtility.RANDOM.nextInt(2000);
			player.sendMessage("Thank you for voting and supporting our server!");

			if (day == GameLoader.MONDAY || day == GameLoader.WEDNESDAY) {
				sendMessage("@red@[VOTING]@bla@ " + player.getUsername() + " Has just voted for " + quantity
						+ "X Vote scrolls");
			} else {
				sendMessage("@red@[VOTING]@bla@ " + player.getUsername() + " Has just voted for" + quantity
						+ "X Vote scrolls");
			}

			player.getInventory().add(19994, Misc.getRandom(bonus));
			player.getPacketSender().sendMessage("You received a bonus of " + bonus + " coins");

			if (Misc.getRandom(15) == 7) {
				player.getInventory().add(915, 1);
				player.getPacketSender().sendMessage("You received a bonus mystery box!");
			}

		}
	}

}