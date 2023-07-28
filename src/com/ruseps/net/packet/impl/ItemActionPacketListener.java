package com.ruseps.net.packet.impl;

import com.ruseps.world.content.minigames.impl.BunnyInstance;
import com.ruseps.world.content.minigames.impl.ClownInstance;
import java.util.Random;

import com.ruseps.world.content.scratchcards.ScratchCard;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Locations.Location;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.content.CharmBox;
import com.ruseps.world.content.ChristmasPresent;
import com.ruseps.world.content.ClueScrolls;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.content.Consumables;
import com.ruseps.world.content.Digging;
import com.ruseps.world.content.DonationBox;
import com.ruseps.world.content.Effigies;
import com.ruseps.world.content.ExperienceLamps;
import com.ruseps.world.content.Gambling;
import com.ruseps.world.content.TrioBosses;
import com.ruseps.world.content.MemberScrolls;
import com.ruseps.world.content.MoneyPouch;
import com.ruseps.world.content.OpenBirdsNests;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.combat.range.DwarfMultiCannon;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.goodie_bag.GoodieBagManager;
import com.ruseps.world.content.instances.CashZone;
import com.ruseps.world.content.skill.impl.construction.Construction;
import com.ruseps.world.content.skill.impl.dungeoneering.ItemBinding;
import com.ruseps.world.content.skill.impl.herblore.Herblore;
import com.ruseps.world.content.skill.impl.herblore.IngridientsBook;
import com.ruseps.world.content.skill.impl.hunter.BoxTrap;
import com.ruseps.world.content.skill.impl.hunter.Hunter;
import com.ruseps.world.content.skill.impl.hunter.JarData;
import com.ruseps.world.content.skill.impl.hunter.PuroPuro;
import com.ruseps.world.content.skill.impl.hunter.SnareTrap;
import com.ruseps.world.content.skill.impl.hunter.Trap.TrapState;
import com.ruseps.world.content.skill.impl.prayer.Prayer;
import com.ruseps.world.content.skill.impl.runecrafting.Runecrafting;
import com.ruseps.world.content.skill.impl.runecrafting.RunecraftingPouches;
import com.ruseps.world.content.skill.impl.runecrafting.RunecraftingPouches.RunecraftingPouch;
import com.ruseps.world.content.skill.impl.slayer.SlayerDialogues;
import com.ruseps.world.content.skill.impl.slayer.SlayerTasks;
import com.ruseps.world.content.skill.impl.summoning.CharmingImp;
import com.ruseps.world.content.skill.impl.summoning.SummoningData;
import com.ruseps.world.content.skill.impl.woodcutting.BirdNests;
import com.ruseps.world.content.transportation.JewelryTeleporting;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.content.transportation.TeleportType;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class ItemActionPacketListener implements PacketListener
{	
	public static void cancelCurrentActions(Player player) 
	{
		player.getPacketSender().sendInterfaceRemoval();
		player.setTeleporting(false);
		player.setWalkToTask(null);
		player.setInputHandling(null);
		player.getSkillManager().stopSkilling();
		player.setEntityInteraction(null);
		player.getMovementQueue().setFollowCharacter(null);
		player.getCombatBuilder().cooldown(false);
		player.setResting(false);
	}
	
	public static boolean checkReqs(Player player, Location targetLocation) {
		if(player.getConstitution() <= 0)
			return false;
		if(player.getTeleblockTimer() > 0) {
			player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
			return false;
		}
		if(player.getLocation() != null && !player.getLocation().canTeleport(player))
			return false;
		if(player.isPlayerLocked() || player.isCrossingObstacle()) {
			player.getPacketSender().sendMessage("You cannot teleport right now.");
			return false;
		}
		return true;
	}

	private static void firstAction(final Player player,  Packet packet) {
		int interfaceId = packet.readUnsignedShort();
		int slot = packet.readShort();
		int itemId = packet.readShort();
		
		Location targetLocation = player.getLocation();
		
		if(interfaceId == 38274) {
			Construction.handleItemClick(itemId, player);
			return;
		}	
		if(itemId == 21879) {
        	//player.getBattlePass().unlockBronzePass();
        }	
		if(itemId == 21880) {
        	//player.getBattlePass().unlockGoldPass();
        }
		if(itemId == 19990) {
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.getInventory().delete(19990,1);
			player.getInventory().add(995,1000000000);
			}
		
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		player.setInteractingItem(player.getInventory().getItems()[slot]);
		if (Prayer.isBone(itemId)) {
			Prayer.buryBone(player, itemId);
			return;
		}
		
		if (Consumables.isFood(player, itemId, slot))
			return;
		if(Consumables.isPotion(itemId)) {
			Consumables.handlePotion(player, itemId, slot);
			return;
		}
		
		if(BirdNests.isNest(itemId)) {
			OpenBirdsNests.openNest(player, itemId);
			return;
		}
		
		if (Herblore.cleanHerb(player, itemId))
			return;
	
		
		if(Effigies.isEffigy(itemId)) {
			Effigies.handleEffigy(player, itemId);
			return;
		}
		if(ExperienceLamps.handleLamp(player, itemId)) {
			return;
		}
		
		switch(itemId)
		{
		case 600:
			player.getDmg().init();
			break;
		case 18057:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(10);
			player.getInventory().delete(18057, 1);
			break;
		case 18058:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(15);
			player.getInventory().delete(18058, 1);
			break;
		case 18059:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(25);
			player.getInventory().delete(18059, 1);
			break;
		case 18060:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(35);
			player.getInventory().delete(18060, 1);
			break;
		case 19994:
			int amount1 = player.getInventory().getAmount(19994);
			 amount1 = 0;
			for(int i = 0; i < player.getInventory().getAmount(19994); i++) {
				amount1++;
			}
			player.getInventory().delete(19994,amount1);
			player.setMoneyInPouch(player.getMoneyInPouch()+(long)1000000*amount1);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			break;
		case 18061:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(45);
			player.getInventory().delete(18061, 1);
			break;
		case 18062:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(55);
			player.getInventory().delete(18062, 1);
			break;
		case 18063:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(65);
			player.getInventory().delete(18063, 1);
			break;
		case 18064:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(75);
			player.getInventory().delete(18064, 1);
			break;
		case 18065:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(85);
			player.getInventory().delete(18065, 1);
			break;
		case 18066:
			if (!player.getClickDelay().elapsed(3000)) {
				player.getPacketSender().sendMessage("@blu@<img=10>" + player.getUsername() + " please only claim this every 2 seconds.");
				return;
			}
			player.addDrBoost(100);
			player.getInventory().delete(18066, 1);
			break;
		case 10835:
			CashZone.enterCashZone(player);
			break;
		case 12852:
			BunnyInstance.startFight(player);
			break;
		case 11180:
			ClownInstance.startFight(player);
			break;
		case 19083:
			if (player.getTransform() == 4390) {
					player.setTimer(player.getTimer() + (100 * 30));
					player.getInventory().delete(19083, 1);
			} else {
				if (player.getTransform() < 1) {
					player.setNpcTransformationId(4390);
					player.setTransform(4390);
					player.setTimer(100 * 30);
					player.getInventory().delete(19083, 1);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.performGraphic(new Graphic(1309));
					NPC npc = new NPC(4390, new Position(0, 0));
					player.setConstitution(npc.getConstitution());
					player.sendMessage("<col=ff0000>You transformed into the B00NY Digimon for 30 mins " + player.getUsername());
				} else {
					player.sendMessage("<col=ff0000>Please wait till your timer is over!");
				}
			}
			break;
		case 455:
			new ScratchCard(player).display();
			break;

		case 18689:
		case 19645:
		case 18665:
		case 18647:
		case 14471:
			DialogueManager.start(player, 500);
			player.setDialogueActionId(500);
			break;
		case 12632:
			if(player.getClickDelay().elapsed(1000)) {
			player.getInventory().delete(12632,1);
			player.setMoneyInPouch(player.getMoneyInPouch() + 100000000);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			player.getClickDelay().reset();
			player.getPA().sendMessage("@blu@100m Coins has been added to your money pouch");
			} else {
				player.getPA().sendMessage("Wait a few seconds between claiming");
			}
			break;
		case 10942:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(1);
			player.getPointsHandler().incrementDonationPoints(10);
			break;
		case 10934:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(2);
			player.getPointsHandler().incrementDonationPoints(20);
			break;
		case 621:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(9);
			player.getPointsHandler().incrementDonationPoints(1);
			break;
		case 10935:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(3);
			player.getPointsHandler().incrementDonationPoints(50);
			break;
		case 10943:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(4);
			player.getPointsHandler().incrementDonationPoints(100);
			break;
		case 21035:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(5);
			player.getPointsHandler().incrementDonationPoints(250);
			break;
		case 21036:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(6);
			player.getPointsHandler().incrementDonationPoints(500);
			break;
		case 21037:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(7);
			player.getPointsHandler().incrementDonationPoints(1000);
			break;
		case 21038:
			MemberScrolls.giveWarning(player);
			player.setScrollAmount(8);
			player.getPointsHandler().incrementDonationPoints(2000);
			break;
		case 13663:
			if(player.getInterfaceId() > 0) {
				player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
				return;
			}
			player.setUsableObject(new Object[2]).setUsableObject(0, "reset");
			player.getPacketSender().sendString(38006, "Choose stat to reset!").sendMessage("@red@Please select a skill you wish to reset and then click on the 'Confim' button.").sendString(38090, "Which skill would you like to reset?");
			player.getPacketSender().sendInterface(38000);
			break;
		case 19670:
			if(player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(70);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 8007: //Varrock
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
			
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8007, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(3210, 3424, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			 break;
		case 8008: //Lumbridge
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8008, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(3222, 3218, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			
			 break;
		case 8009: //Falador
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8009, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2964, 3378, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
	
			
			 break;
		case 9850: //Goku Package
			player.getInventory().delete(9850, 1);
			player.getInventory().add(19970, 1);
			player.getInventory().add(19972, 1);
			player.getInventory().add(19974, 1);
			player.getInventory().add(19976, 1);
			player.getInventory().add(19978, 1);
			 break;
		case 6831: //mermaid
			player.getInventory().delete(6831, 1);
			player.getInventory().add(902, 1);
			player.getInventory().add(903, 1);
			player.getInventory().add(904, 1);
			player.getInventory().add(905, 1);
			player.getInventory().add(906, 1);
			 break;
		case 8610: //vegeta
			player.getInventory().delete(8610, 1);
			player.getInventory().add(19960, 1);
			player.getInventory().add(19962, 1);
			player.getInventory().add(19964, 1);
			player.getInventory().add(19958, 1);
			player.getInventory().add(19956, 1);
			 break;
		case 6830: //vader Package
			player.getInventory().delete(6830, 1);
			player.getInventory().add(18440, 1);
			player.getInventory().add(18946, 1);
			player.getInventory().add(18914, 1);
			player.getInventory().add(20527, 1);
			player.getInventory().add(20528, 1);
			player.getInventory().add(20529, 1);
			 break;
		case 6832: //storm Package
			player.getInventory().delete(6832, 1);
			player.getInventory().add(11067, 1);
			player.getInventory().add(11068, 1);
			player.getInventory().add(11071, 1);
			player.getInventory().add(11078, 1);
			player.getInventory().add(11087, 1);
			 break;
		case 15246: //Bikini Package
			player.getInventory().delete(15246, 1);
			player.getInventory().add(897, 1);
			player.getInventory().add(898, 1);
			player.getInventory().add(899, 1);
			player.getInventory().add(900, 1);
			player.getInventory().add(901, 1);
			 break;
		case 7960: //Red Storm trooper Package
			player.getInventory().delete(7960, 1);
			player.getInventory().add(2544, 1);
			player.getInventory().add(2547, 1);
			player.getInventory().add(2548, 1);
			player.getInventory().add(2549, 1);
			player.getInventory().add(11078, 1);
			 break;
		case 6828: //Scaley teir 4
			player.getInventory().delete(6828, 1);
			player.getInventory().add(18977, 1);
			player.getInventory().add(18978, 1);
			player.getInventory().add(18979, 1);
			player.getInventory().add(19014, 1);
			player.getInventory().add(19015, 1);
			break;
		case 6829: //Shadow Lord Magic
			player.getInventory().delete(6829, 1);
			player.getInventory().add(1019, 1);
			player.getInventory().add(1021, 1);
			player.getInventory().add(1023, 1);
			player.getInventory().add(1025, 1);
			player.getInventory().add(1027, 1);
			player.getInventory().add(1029, 1);
			break;
		case 6833: //Op Package
			player.getInventory().delete(6833, 1);
			player.getInventory().add(965, 1);
			player.getInventory().add(996, 1);
			player.getInventory().add(1007, 1);
			player.getInventory().add(11978, 1);
			break;
		case 2733: //Valentines package
			player.getInventory().delete(2733, 1);
			player.getInventory().add(2725, 1);
			player.getInventory().add(2724, 1);
			player.getInventory().add(2726, 1);
			player.getInventory().add(2727, 1);
			player.getInventory().add(2729, 1);
			player.getInventory().add(2730, 1);
			break;
		case 19671: //Crazy Package
			player.getInventory().delete(19671, 1);
			player.getInventory().add(1667, 1);
			player.getInventory().add(1666, 1);
			player.getInventory().add(1013, 1);
			player.getInventory().add(1015, 1);
			player.getInventory().add(1686, 1);
			player.getInventory().add(1009, 1);
			break;
		case 8010: //Camelot
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
			
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8010, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2757, 3477, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
			
			 break;
		case 8011: //Ardy
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8011, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2662, 3305, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
			 break;
		case 8012: //Watchtower Tele
			
			if(!player.getClickDelay().elapsed(1200) || player.getMovementQueue().isLockMovement()) {
				return;
			}
			if(player.getLocation() == Location.CONSTRUCTION) {
				player.getPacketSender().sendMessage("Please use the portal to exit your house");
				return;
			}
		
			
			if(!checkReqs(player, targetLocation)) {
				return;
			}
			player.setTeleporting(true).getMovementQueue().setLockMovement(true).reset();
			cancelCurrentActions(player);
			player.performAnimation(new Animation(4731));
			player.performGraphic(new Graphic(678));
			player.getInventory().delete(8012, 1);
			player.getClickDelay().reset();
		
			TaskManager.submit(new Task(2, player, false) {
				@Override
				public void execute() {		
					Position position = new Position(2728, 3349, 0);
					player.moveTo(position);
					player.getMovementQueue().setLockMovement(false).reset();
					this.stop();
				}
			});
		
	
			 break;
		case 8013: //Home Tele
			 TeleportHandler.teleportPlayer(player, new Position(3087, 3491), TeleportType.NORMAL);
			 break;
		case 13598: //Runecrafting Tele
			 TeleportHandler.teleportPlayer(player, new Position(2595, 4772), TeleportType.NORMAL);
			 break;
		case 13599: //Air Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2845, 4832), TeleportType.NORMAL);
			 break;
		case 13600: //Mind Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2796, 4818), TeleportType.NORMAL);
			 break;
		case 13601: //Water Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2713, 4836), TeleportType.NORMAL);
			 break;
		case 13602: //Earth Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2660, 4839), TeleportType.NORMAL);
			 break;
		case 13603: //Fire Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2584, 4836), TeleportType.NORMAL);
			 break;
		
		case 13604: //Body Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2527, 4833), TeleportType.NORMAL);
			 break;
		case 13605: //Cosmic Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2162, 4833), TeleportType.NORMAL);
			 break;
		case 13606: //Chaos Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2269,4843), TeleportType.NORMAL);
			 break;
		case 13607: //Nature Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2398, 4841), TeleportType.NORMAL);
			 break;
		case 13608: //Law Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2464, 4834), TeleportType.NORMAL);
			 break;
		case 13609: //Death Altar Tele
			 TeleportHandler.teleportPlayer(player, new Position(2207, 4836), TeleportType.NORMAL);
			 break;
		case 18809: //Rimmington Tele
			 TeleportHandler.teleportPlayer(player, new Position(2957, 3214), TeleportType.NORMAL);
			 break;
		case 18811: //Pollnivneach Tele
			 TeleportHandler.teleportPlayer(player, new Position(3359,2910), TeleportType.NORMAL);
			 break;
		case 18812: //Rellekka Tele
			 TeleportHandler.teleportPlayer(player, new Position(2659, 3661), TeleportType.NORMAL);
			 break;
		case 18814: //Yanielle Tele
			 TeleportHandler.teleportPlayer(player, new Position(2606, 3093), TeleportType.NORMAL);
			 break;
		case 6542:
			ChristmasPresent.openBox(player);
			break;
			
			
		case 10025:
			CharmBox.open(player);
			break;
			
        case 18338:
            GoodieBagManager.open(player);
            break;
			
			
		case 1959:
			TrioBosses.eatPumpkin(player);
			break;
		case 2677:
		case 2678:
		case 2679:
		case 2680:
		case 2681:
		case 2682:
		case 2683:
		case 2684:
		case 2685:
		case 2686:
		case 2687:
		case 2688:
		case 2689:
		case 2690:
			ClueScrolls.giveHint(player, itemId);
			break;
		case 7956:
			if (player.getRights() == PlayerRights.BRONZE_MEMBER) {
				if (Misc.getRandom(15) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.SILVER_MEMBER || player.getRights() == PlayerRights.SUPPORT) {
				if (Misc.getRandom(12) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.GOLD_MEMBER || player.getRights() == PlayerRights.MODERATOR) {
				if (Misc.getRandom(9) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.PLATINUM_MEMBER  || player.getRights() == PlayerRights.ADMINISTRATOR) {
				if (Misc.getRandom(6) == 5) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.DIAMOND_MEMBER || player.getRights() == PlayerRights.RUBY_MEMBER|| player.getRights() == PlayerRights.DEVELOPER) {
				if (Misc.getRandom(3) == 2) {
					player.getPacketSender().sendMessage("Casket has been saved as a donator benefit");
				} else {
					player.getInventory().delete(7956, 1);
				}
			}
			if (player.getRights() == PlayerRights.PLAYER || player.getRights() == PlayerRights.VETERAN) {
				player.getInventory().delete(7956, 1);
			}
			
			int[] rewards = 		{200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618, 1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556, 558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143, 9144, 9341, 9244, 866, 867, 868,  2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149};
			int[] rewardsAmount = 	{200, 200, 200, 120, 50,  100,  70,  60,  90,  40,  30,   15,   10,  230,  140,  70,  20,   10,  400,  200,  400,  250, 100, 100,1000, 800,  500, 200, 100,    50, 150, 100,  50,     5,1500,1500,1500,1500,1500,1500,1000,1000, 500,  500, 500, 500, 500, 500,3000,2500, 800,   300, 3500, 3500,  500,  150,   80, 3000,1500,400,500,     1,     1,    1,    1,     1,    1,    1,    1};
			int rewardPos = Misc.getRandom(rewards.length-1);
			player.getInventory().add(rewards[rewardPos], (int)((rewardsAmount[rewardPos]*0.5) + (Misc.getRandom(rewardsAmount[rewardPos]))));
			break;
			
			//Donation Box
		case 6183:
			DonationBox.open(player);
			break;
			//Clue Scroll
		case 2714:
			ClueScrolls.addClueReward(player);
			break;
			
		case 15387:
			player.getInventory().delete(15387, 1);
			rewards = new int[] {1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442, 347, 247};
			player.getInventory().add(rewards[RandomUtility.getRandom(rewards.length-1)], 1);
			break;
		case 407:
			player.getInventory().delete(407, 1);
			if (RandomUtility.getRandom(3) < 3) {
				player.getInventory().add(409, 1);
			} else if(RandomUtility.getRandom(4) < 4) {
				player.getInventory().add(411, 1);
			} else 
				player.getInventory().add(413, 1);
			break;
		case 406:
			player.getInventory().delete(406, 1);
			if (RandomUtility.getRandom(1) < 1) {
				int coins = RandomUtility.getRandom(30000);
				player.getInventory().add(995, coins);
				player.getPacketSender().sendMessage("The casket contained "+coins+" coins!");
			} else
				player.getPacketSender().sendMessage("The casket was empty.");
			break;
		case 15084:
			Gambling.rollDice(player);
			break;
		case 299:
			Gambling.plantSeed(player);
			break;
		case 4155:
			if(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("Your Enchanted gem will only work if you have a Slayer task.");
				return;
			}
			DialogueManager.start(player, SlayerDialogues.dialogue(player));
			break;
		case 11858:
		case 11860:
		case 11862:
		case 11848:
		case 11856:
		case 11850:
		case 11854:
		case 11852:
		case 11846:
			if(!player.getClickDelay().elapsed(2000) || !player.getInventory().contains(itemId))
				return;
			if(player.busy()) {
				player.getPacketSender().sendMessage("You cannot open this right now.");
				return;
			}

			int[] items = itemId == 11858 ? new int[] {10350, 10348, 10346, 10352} : 
				itemId == 11860 ? new int[]{10334, 10330, 10332, 10336} : 
					itemId == 11862 ? new int[]{10342, 10338, 10340, 10344} : 
						itemId == 11848 ? new int[]{4716, 4720, 4722, 4718} : 
							itemId == 11856 ? new int[]{4753, 4757, 4759, 4755} : 
								itemId == 11850 ? new int[]{4724, 4728, 4730, 4726} : 
									itemId == 11854 ? new int[]{4745, 4749, 4751, 4747} : 
										itemId == 11852 ? new int[]{4732, 4734, 4736, 4738} : 
											itemId == 11846 ? new int[]{4708, 4712, 4714, 4710} :
												new int[]{itemId};

											if(player.getInventory().getFreeSlots() < items.length) {
												player.getPacketSender().sendMessage("You do not have enough space in your inventory.");
												return;
											}
											player.getInventory().delete(itemId, 1);
											for(int i : items) {
												player.getInventory().add(i, 1);
											}
											player.getPacketSender().sendMessage("You open the set and find items inside.");
											player.getClickDelay().reset();
											break;
		case 952:
			Digging.dig(player);
			break;
		case 10006:
			// Hunter.getInstance().laySnare(client);
			Hunter.layTrap(player, new SnareTrap(new GameObject(19175, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 10008:			
			Hunter.layTrap(player, new BoxTrap(new GameObject(19187, new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ())), TrapState.SET, 200, player));
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.fill(player, RunecraftingPouch.forId(itemId));
			break;
		case 292:
			IngridientsBook.readBook(player, 0, false);
			break;
		case 915:
			player.getMysteryBox().openInterface();
			break;
		case 21056:
			player.getOwnerBox().openInterface();
			break;
		case 21055:
			player.getAuroraBox().openInterface();
			break;
		case 13016:
			player.getPetBox().openInterface();
			break;
		
		case 6199: // Old MBOX
			int rewards2[][] = {
					{15501}, //Common, 0
					{15501}, //Uncommon, 1
					{15501}  //Rare, 2
			};
			
			double numGen = Math.random();	
			int rewardGrade = numGen >= 0.5 ? 0 : numGen >= 0.20 ? 1 : 2;
			rewardPos = RandomUtility.getRandom(rewards2[rewardGrade].length-1);
			player.getInventory().delete(6199, 1);
			Random random = new Random();
			int chance = random.nextInt(2);

			if (chance == 0) 
			{
			    player.getInventory().add(rewards2[rewardGrade][rewardPos], 1).refreshItems();
			    player.sendMessage("You got lucky and upgraded to a LBOX!");
			} else {
			    player.getInventory().add(995, 20_000 + Misc.random(30_000));
			}
			break;
		case 15501:
			int superiorRewards[][] = {
					{941, 944, 18852, 18854, 18856, 18858, 18860, 18864, 18868, 20106, 20110, 20112, 20114, 20116}, //Uncommon, 0
					{20134, 20136, 20138, 20140, 20142, 10502, 14018, 20150, 20154, 20156, 20158, 20160}, //Rare, 1
					{18900, 18896, 18898, 14460, 14462, 10822, 11208, 10824, 11206, 10826}, //Epic, 2
					{11005, 11000, 20657} //Legendary, 3
			};
			double superiorNumGen = Math.random();
			/** Chances
			 *  40% chance of Uncommon Items - various high-end coin-bought gear
			 *  30% chance of Rare Items - Highest-end coin-bought gear, Some poor voting-point/pk-point equipment
			 *  20% chance of Epic Items -Better voting-point/pk-point equipment
			 *  10% chance of Legendary Items - Only top-notch voting-point/pk-point equipment
			 */
			int superiorRewardGrade = superiorNumGen >= 0.60 ? 0 : superiorNumGen >= 0.30 ? 1 : superiorNumGen >= 0.10 ? 2 : 3;
			int superiorRewardPos = RandomUtility.getRandom(superiorRewards[superiorRewardGrade].length-1);
			player.getInventory().delete(15501, 1);
			player.getInventory().add(superiorRewards[superiorRewardGrade][superiorRewardPos], 1).refreshItems();
			break;
		case 11882:
			player.getInventory().delete(11882, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(3473, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11884:
			player.getInventory().delete(11884, 1);
			player.getInventory().add(2595, 1).refreshItems();
			player.getInventory().add(2591, 1).refreshItems();
			player.getInventory().add(2593, 1).refreshItems();
			player.getInventory().add(2597, 1).refreshItems();
			break;
		case 11906:
			player.getInventory().delete(11906, 1);
			player.getInventory().add(7394, 1).refreshItems();
			player.getInventory().add(7390, 1).refreshItems();
			player.getInventory().add(7386, 1).refreshItems();
			break;
		case 15262:
			if(!player.getClickDelay().elapsed(1000))
				return;
			player.getInventory().delete(15262, 1);
			player.getInventory().add(18016, 10000).refreshItems();
			player.getClickDelay().reset();
			break;
		case 6:
			DwarfMultiCannon.setupCannon(player);
			break;
		}
	}

	public static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readLEShortA();
		int slot = packet.readLEShort();
		int itemId = packet.readShortA();
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if (SummoningData.isPouch(player, itemId, 2))
			return;
		switch(itemId) {
		case 19085:
            int charges = player.getUltBowCharges();
            if (!player.getInventory().contains(19085)) {
                return;
            }
            if (charges <= 0) {
                player.getPacketSender().sendMessage("You have no charges!");
                return;
            }
            if (player.getInventory().contains(12934) || !player.getInventory().isFull()) {
                player.getInventory().add(12934, charges);
                player.setUltBowCharges(0);
                player.getPacketSender().sendMessage(
                        "You uncharge your Ultimate Bow and recieve " + Misc.format(charges) + " bow tokens.");
            } else {
                player.getPacketSender().sendMessage("You need an inventory space.");
            }
            break;
		case 621:
			player.getInventory().delete(621, 1);
			player.getPointsHandler().incrementDonationPoints(2);
			player.getPacketSender().sendMessage("Your account has gained funds worth $1. Your total is now at $"+player.getAmountDonated()+".");
			player.sendMessage("<img=10>@blu@ " + player.getUsername() + " has just donated $1, Now they have Donated $" + player.getAmountDonated()+".");
			PlayerPanel.refreshPanel(player);
			break;
		case 6500:
			if(player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
				player.getPacketSender().sendMessage("You cannot configure this right now.");
				return;
			}
			player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 101);
			player.setDialogueActionId(60);
			break;
		case 12926:
			player.getBlowpipeLoading().handleUnloadBlowpipe();
			break;
		case 1712:
		case 1710:
		case 1708:
		case 1706:
		case 11118:
		case 11120:
		case 11122:
		case 11124:
			JewelryTeleporting.rub(player, itemId);
			break;
		case 19992:
			int amount = player.getInventory().getAmount(19992);
			 amount = 0;
			for(int i = 0; i < player.getInventory().getAmount(19992); i++) {
				amount++;
			}
			player.getInventory().delete(19992,amount);
			player.setMoneyInPouch(player.getMoneyInPouch()+(long)1000000*amount);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			break;
		case 19994:
			int amount1 = player.getInventory().getAmount(19994);
			 amount1 = 0;
			for(int i = 0; i < player.getInventory().getAmount(19994); i++) {
				amount1++;
			}
			player.getInventory().delete(19994,amount1);
			player.setMoneyInPouch(player.getMoneyInPouch()+(long)1000000*amount1);
			player.getPacketSender().sendString(8135, ""+player.getMoneyInPouch());
			break;
			
		case 1704:
			player.getPacketSender().sendMessage("Your amulet has run out of charges.");
			break;
		case 11126:
			player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getSlayer().handleSlayerRingTP(itemId);
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.check(player, RunecraftingPouch.forId(itemId));
			break;
		case 995:
			MoneyPouch.depositMoney(player, player.getInventory().getAmount(995));
			break;
		case 1438:
		case 1448:
		case 1440:
		case 1442:
		case 1444:
		case 1446:
		case 1454:
		case 1452:
		case 1462:
		case 1458:
		case 1456:
		case 1450:
			Runecrafting.handleTalisman(player, itemId);
			break;
		}
	}

	public void thirdClickAction(Player player, Packet packet) {
		int itemId = packet.readShortA();
		int slot = packet.readLEShortA();
		int interfaceId = packet.readLEShortA();
		if(slot < 0 || slot > player.getInventory().capacity())
			return;
		if(player.getInventory().getItems()[slot].getId() != itemId)
			return;
		if(JarData.forJar(itemId) != null) {
			PuroPuro.lootJar(player, new Item(itemId, 1), JarData.forJar(itemId));
			return;
		}
		if (SummoningData.isPouch(player, itemId, 3)) {
			return;
		}
		if(ItemBinding.isBindable(itemId)) {
			ItemBinding.bindItem(player, itemId);
			return;
		}
		switch(itemId) {
		case 14019:
		case 14022:
			player.setCurrentCape(itemId);
			int[] colors = itemId == 14019 ? player.getMaxCapeColors() : player.getCompCapeColors();
			String[] join = new String[colors.length];
			for(int i = 0; i < colors.length; i++) {
				join[i] = Integer.toString(colors[i]);
			}
			player.getPacketSender().sendString(60000, "[CUSTOMIZATION]"+itemId+","+String.join(",", join));
			player.getPacketSender().sendInterface(60000);
			break;
		case 12926:
			player.getBlowpipeLoading().handleCheckBlowpipe();
			break;
		case 19670:
			if(player.busy()) {
				player.getPacketSender().sendMessage("You can not do this right now.");
				return;
			}
			player.setDialogueActionId(71);
			DialogueManager.start(player, player.getGameMode() == GameMode.NORMAL ? 108 : 109);
			break;
		case 6500:
			CharmingImp.sendConfig(player);
			break;
		case 4155:
			player.sendMessage("@red@Duo Slayer is disabled at this time.");
			/*player.getPacketSender().sendInterfaceRemoval();
			DialogueManager.start(player, 103);
			player.setDialogueActionId(65);*/
			break;
		case 13281:
		case 13282:
		case 13283:
		case 13284:
		case 13285:
		case 13286:
		case 13287:
		case 13288:
			player.getPacketSender().sendInterfaceRemoval();
			player.getPacketSender().sendMessage(player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK ? ("You do not have a Slayer task.") : ("Your current task is to kill another "+(player.getSlayer().getAmountToSlay())+" "+Misc.formatText(player.getSlayer().getSlayerTask().toString().toLowerCase().replaceAll("_", " "))+"s."));
			break;
		case 6570:
			if(player.getInventory().contains(6570) && player.getInventory().getAmount(6529) >= 50000) {
				player.getInventory().delete(6570, 1).delete(6529, 50000).add(19111, 1);
				player.getPacketSender().sendMessage("You have upgraded your Fire cape into a TokHaar-Kal cape!");
			} else {
				player.getPacketSender().sendMessage("You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
			}
			break;
		case 15262:
			if(!player.getClickDelay().elapsed(1300))
				return;
			int amt = player.getInventory().getAmount(15262);
			if(amt > 0)
				player.getInventory().delete(15262, amt).add(18016, 10000 * amt);
			player.getClickDelay().reset();
			break;
		case 5509:
		case 5510:
		case 5512:
			RunecraftingPouches.empty(player, RunecraftingPouch.forId(itemId));
			break;
		case 11283: //DFS
			player.getPacketSender().sendMessage("Your Dragonfire shield has "+player.getDfsCharges()+"/20 dragon-fire charges.");
			break;
		case 11613: //dkite
			player.getPacketSender().sendMessage("Your Dragonfire shield has "+player.getDfsCharges()+"/20 dragon-fire charges.");
			break;
		}
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
		case SECOND_ITEM_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case FIRST_ITEM_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			thirdClickAction(player, packet);
			break;
		}
	}

	public static final int SECOND_ITEM_ACTION_OPCODE = 75;

	public static final int FIRST_ITEM_ACTION_OPCODE = 122;

	public static final int THIRD_ITEM_ACTION_OPCODE = 16;

}