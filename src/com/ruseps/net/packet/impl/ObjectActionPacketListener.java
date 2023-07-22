package com.ruseps.net.packet.impl;

import com.ruseps.BalloonDropParty;
import com.ruseps.world.content.new_raids_system.instances.*;
import com.ruseps.world.content.EvilTrees;
import com.ruseps.world.content.TrioBosses;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_seven_loot.RaidsSevenChest;
import com.ruseps.world.content.upgrading.UpgradeData;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.WalkToTask;
import com.ruseps.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Direction;
import com.ruseps.model.DwarfCannon;
import com.ruseps.model.Flag;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.Locations;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.PlayerRights;
import com.ruseps.world.content.skill.impl.construction.Construction;
import com.ruseps.model.Position;
import com.ruseps.model.Prayerbook;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.GameObjectDefinition;
import com.ruseps.model.input.impl.DonateToWell;
import com.ruseps.model.input.impl.EnterAmountOfLogsToAdd;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.clip.region.RegionClipping;
import com.ruseps.world.content.CrystalChest;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.ShootingStar;
import com.ruseps.world.content.TreasureChest;
import com.ruseps.world.content.WildernessObelisks;
import com.ruseps.world.content.afk_spots.AfkTree;
import com.ruseps.world.content.afk_spots.IceCoinRock;
import com.ruseps.world.content.battle_royale.BattleRoyale;
import com.ruseps.world.content.battle_royale.loot.MeleeBattleRoyaleChest;
import com.ruseps.world.content.battle_royale.rotators.melee.ChestRotator;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.range.DwarfMultiCannon;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.fuser.FuserEnum;
import com.ruseps.world.content.fuser.FuserHandler;
import com.ruseps.world.content.grandexchange.GrandExchange;
import com.ruseps.world.content.minigames.impl.Barrows;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.minigames.impl.Dueling.DuelRule;
import com.ruseps.world.content.minigames.impl.FightCave;
import com.ruseps.world.content.minigames.impl.FightPit;
import com.ruseps.world.content.minigames.impl.Nomad;
import com.ruseps.world.content.minigames.impl.PestControl;
import com.ruseps.world.content.minigames.impl.RecipeForDisaster;
import com.ruseps.world.content.minigames.impl.WarriorsGuild;
import com.ruseps.world.content.new_raids_system.instances.PokemonRaids;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_five_loot.RaidsFiveChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_four_loot.RaidsFourChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.RaidsOneChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_one.R1P1NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_two_loot.RaidsTwoChest;
import com.ruseps.world.content.new_raids_system.raids_loot.aoe_zone_chest.AoeChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_three_loot.RaidsThreeChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_six_loot.RaidsSixChest;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_seven_loot.RaidsSevenChest;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.skill.impl.agility.Agility;
import com.ruseps.world.content.skill.impl.construction.Construction;
import com.ruseps.world.content.skill.impl.crafting.Flax;
import com.ruseps.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ruseps.world.content.skill.impl.fishing.Fishing;
import com.ruseps.world.content.skill.impl.fishing.Fishing.Spot;
import com.ruseps.world.content.skill.impl.hunter.Hunter;
import com.ruseps.world.content.skill.impl.hunter.PuroPuro;
import com.ruseps.world.content.skill.impl.mining.Mining;
import com.ruseps.world.content.skill.impl.mining.MiningData;
import com.ruseps.world.content.skill.impl.mining.Prospecting;
import com.ruseps.world.content.skill.impl.runecrafting.Runecrafting;
import com.ruseps.world.content.skill.impl.runecrafting.RunecraftingData;
import com.ruseps.world.content.skill.impl.smithing.EquipmentMaking;
import com.ruseps.world.content.skill.impl.smithing.Smelting;
import com.ruseps.world.content.skill.impl.thieving.Stalls;
import com.ruseps.world.content.skill.impl.woodcutting.Woodcutting;
import com.ruseps.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.ruseps.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.content.transportation.TeleportType;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player clicked
 * on a game object.
 * 
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

	private static void firstClick(final Player player, Packet packet) {
		final int x = packet.readLEShortA();
		final int id = packet.readUnsignedShort();
		final int y = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(player.getLocation() != Location.CONSTRUCTION) {
			if(id > 0 && id != 6 && !RegionClipping.objectExists(gameObject) && id != 9294) {
				player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX() : GameObjectDefinition.forId(id).getSizeY();
		if (size <= 0)
			size = 1;
		gameObject.setSize(size);
		if(player.getMovementQueue().isLockMovement())
			return;
		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MANAGER)
			player.getPacketSender().sendMessage("First click object id; [id, position] : [" + id + ", " + position.toString() + "]");
		player.setInteractingObject(gameObject).setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			@SuppressWarnings("static-access")
			@Override
			public void execute() {
				player.setPositionToFace(gameObject.getPosition());
				if(player.getRegionInstance() != null) {
					Construction.handleFifthObjectClick(x, y, id, player);
				}
				if(WoodcuttingData.Trees.forId(id) != null) {
					Woodcutting.cutWood(player, gameObject, false);
					return;
				}
				if(MiningData.forRock(gameObject.getId()) != null) {
					Mining.startMining(player, gameObject);
					return;
				}
				if (player.getFarming().click(player, x, y, 1))
					return;
				if(Runecrafting.runecraftingAltar(player, gameObject.getId())) {
					RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
					if(rune == null)
						return;
					Runecrafting.craftRunes(player, rune);
					return;
				}
				if(Agility.handleObject(player, gameObject)) {
					return;
				}
				if(Barrows.handleObject(player, gameObject)) {
					return;
				}

				if(player.getLocation() == Location.WILDERNESS && WildernessObelisks.handleObelisk(gameObject.getId())) {
					return;
				}

				if (BalloonDropParty.pop(player, gameObject)) {
					return;
				}

				switch(id) {
				case 47236:
					player.setDialogueActionId(522);
					DialogueManager.start(player, 522);
					break;
				case 15_000:
					IceCoinRock.mineIceCoins(player);
					break;
				/** RAIDS 1 OBJECTS **/
				case 2009:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Pokemon Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    PokemonRaids.startRaid(player);
                    break;
				case 3000:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Plat Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    PlatRaids.startRaidsSix(player);
					break;
					case 3001:
						if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
							player.sendMessage("You need to be in a party to do Diamond Raids");
							return;
						}
						if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
							player.sendMessage("Only the party leader can initiate the Raid");
							return;
						}
						DiamondRaids.startRaidsSeven(player);
						break;
				case 3010:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Anime Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    AnimeRaids.startRaidsTwo(player);
                    break;
				case 3012:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Silver Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    SilverRaids.startRaidsThree(player);
					break;
				case 3009:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Gold Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    GoldRaids.startRaidsFive(player);
					break;
				case 3013:
				    if (player.getMinigameAttributes().getRaidsAttributes().getParty() == null) {
                        player.sendMessage("You need to be in a party to do Mortal Kombat Raids");
                        return;
                    }
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty().getOwner() != player) {
                        player.sendMessage("Only the party leader can initiate the Raid");
                        return;
                    }
                    MortalKombatRaids.startRaidsFour(player);
					break;
				case 3011:
					if (!player.getClickDelay().elapsed(3000))
					{
						player.sendMessage("@red@Please slow your clicking down.");
						return;
					}
					if(player.getInventory().contains(9284))
					{
						RaidsTwoChest.openChest(player);
						player.getInventory().delete(9284, 1);
					} else {
						player.sendMessage("@red@You require a Pokemon Raids Key for this chest " + player.getUsername());
					}
					break;
				case 1992:
					RaidsSevenChest.openChest(player);
					break;
					case 1993:
						BalloonDropParty.open(player);
						break;
				case 2014:
					RaidsOneChest.openChest(player);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_POKEMON_RAIDS, 1);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_POKEMON_RAIDS_50_TIMES, 1);
					break;
				case 4002:
					FuserHandler.openInterface(FuserEnum.TEST,player);
					break;
				case 4001:
					RaidsSixChest.openChest(player);
					break;
				case 2006:
					AoeChest.openChest(player);
					break;
				case 2043:
					RaidsTwoChest.openChest(player);
					player.getAchievementTracker().progress(com.ruseps.world.content.achievements.AchievementData.COMPLETE_ANIME_RAIDS, 1);
					break;
				case 2044: 
					RaidsThreeChest.openChest(player);
					break;
				case 2008:
					RaidsFourChest.openChest(player);
					break;
				case 2007:
					RaidsFiveChest.openChest(player);
					break;
				case 21585:
					player.performGraphic(new Graphic(1176));
					player.moveTo(new Position(2467, 9673), true);
					break;
				case 3008:
            		player.getPacketSender().sendInterface(62200);
        				for (int i = 0; i < UpgradeData.itemList.length; i++)
        			player.getPacketSender().sendItemOnInterface(62209, UpgradeData.itemList[i], i, 1);
        			player.getPacketSender().sendMessage("@blu@ This Upgrade Machine accepts Diamond Coins! Currently, you will lose the item on Failure");
            		break;
				case 16604:
					AfkTree.fillBloodEssence(player);
					break;
					
				case 5273:
					if(player.getInventory().contains(18689)) {
						int[] common = new int[] {20106, 20108, 20110, 20112, 20114, 20116, 19919, 19920};
						int[] uncommon = new int[] {14018, 20150, 20154, 20156, 20158, 19918};
						int[] rare = new int[] {2572, 11005, 14460, 14462, 18896, 18898, 18900};
							player.getChestViewer().display(6759, " 1", common, uncommon, rare);
					} else {
							player.getPacketSender().sendMessage("@blu@You need chest key #1 to open this chest");
					}
					break;
				case 5274:
					if(player.getInventory().contains(19645)) {
						int[] common = new int[] {2572, 11005, 14460, 14462, 18896, 18898, 18900 };
						int[] uncommon = new int[] { 10503, 11654, 20522, 11659, 11653, 20146, 11650 };
						int[] rare = new int[] { 10682, 10683, 10684, 10685, 940, 11039, 11040, 11041, 11042, 10480, 19888 };
							player.getChestViewer().display(6759, " 1", common, uncommon, rare);
					} else {
							player.getPacketSender().sendMessage("@blu@You need chest key #2 to open this chest");
					}
					break;
				case 5275:
					if(player.getInventory().contains(18665)) {
						int[] common = new int[] {20100, 20102, 11896, 20104, 10822, 10824, 10826, 11206, 11208, 7082};
						int[] uncommon = new int[] {11896, 10502, 20126, 20136, 20138, 20140, 20142, 20144, 20656 };
						int[] rare = new int[] { 11661, 11662, 11679, 11680, 11681, 11682, 11658, 20520, 20658, 20659, 20660 };
						
							player.getChestViewer().display(6759, " 1", common, uncommon, rare);
					} else {
							player.getPacketSender().sendMessage("@blu@You need chest key #3 to open this chest");
					}
					break;
				case 5278:
					if(player.getInventory().contains(18647)) {
						int[] common = new int[] { 20650, 20651, 20652, 20653, 20654, 937, 20658, 20659, 20660};
						int[] uncommon = new int[] { 996, 965, 5085, 5086, 5087, 5088, 5089, 18876, 11651, 5082, 5083, 5084 };
						int[] rare = new int[] { 799, 894, 895, 896, 798, 938, 8860, 8861, 8862, 8871, 8656, 8657, 8658, 8659, 8660 };
					
							player.getChestViewer().display(6759, " 1", common, uncommon, rare);
					} else {
							player.getPacketSender().sendMessage("@blu@You need chest key #4 to open this chest");
					}
					break;
				case 5279:
					if(player.getInventory().contains(14471)) {
						int[] common = new int[] {799, 894, 895, 896, 798, 938, 8860, 8861, 8862, 8871, 8656, 8657, 8658, 8659, 8660};
						int[] uncommon = new int[] { 11068, 11071, 11078, 11087, 11067, 2542, 18440, 18914, 18946, 20527, 20528, 20529 };
						int[] rare = new int[] { 6830, 6832, 7960, 11949, 405, 18982, 18983, 18993, 19021, 2545, 2544, 2545, 2546, 2547, 2548, 2549};
						
							player.getChestViewer().display(6759, " 1", common, uncommon, rare);
					} else {
							player.getPacketSender().sendMessage("@blu@You need chest key #5 to open this chest");
					}
					break;
					
				case 38660:
					if(ShootingStar.CRASHED_STAR != null) {

					}
					break;
				case 11434:
					if (EvilTrees.SPAWNED_TREE != null) {

					}
					break;
					
				case 2079:
					TrioBosses.openChest(player);
					break;
					
                case 10621:
                case 18804:
                case 24204:
                case 29577:
                    TreasureChest.handleChest(player, gameObject);
                    break;
				case 4470:
					player.moveTo(new Position (player.getPosition().getX() -1 , player.getPosition().getY() +1 ));
					break;
				case 10820:
					if(player.getRights() == PlayerRights.GOLD_MEMBER
					|| player.getRights() == PlayerRights.RUBY_MEMBER
					|| player.getRights() == PlayerRights.PLATINUM_MEMBER
					|| player.getRights() == PlayerRights.DIAMOND_MEMBER) {
						player.moveTo(new Position (player.getPosition().getX(), player.getPosition().getY() -1 ));
					} else {
						player.sendMessage("@red You must be at least a Gold Member to enter this area!");
					}
					break;
					
				case 13405:
					if(!TeleportHandler.checkReqs(player, null)) {
						return;
					}
					if(!player.getClickDelay().elapsed(4500) || player.getMovementQueue().isLockMovement()) {
						return;
					}
					Position tele = GameSettings.DEFAULT_POSITION;
					if(player.getLocation() == Location.CONSTRUCTION) {
						player.moveTo(tele);
						return;
					}
					Construction.newHouse(player);
					Construction.enterHouse(player, player, true, true);
					player.getPacketSender().sendMessage("@red@If your construction area map bugs out, teleport home and back in!");
					break;
				case 8799:
			        GrandExchange.open(player);
			        break;
				case 38700:
					player.moveTo(new Position(3092, 3502));
					break;
				case 2465:
					if(player.getLocation() == Location.EDGE) {
						player.getPacketSender().sendMessage("<img=10> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
						player.moveTo(new Position(2815, 5511));
					} else {
						player.getPacketSender().sendMessage("The portal does not seem to be functioning properly.");
					}
					break;
				case 45803:
				case 1767:
					DialogueManager.start(player, 114);
					player.setDialogueActionId(72);
					break;
				case 7352:
					if(Dungeoneering.doingDungeoneering(player) && player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getGatestonePosition() != null) {
						player.moveTo(player.getMinigameAttributes().getDungeoneeringAttributes().getParty().getGatestonePosition());
						player.setEntityInteraction(null);
						player.getPacketSender().sendMessage("You are teleported to your party's gatestone.");
						player.performGraphic(new Graphic(1310));
					} else
						player.getPacketSender().sendMessage("Your party must drop a Gatestone somewhere in the dungeon to use this portal.");
					break;
				case 7353:
					player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
					break;
				case 7321:
					player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
					break;
				case 7322:
					player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
					break;
				case 7315:
					player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
					break;
				case 7316:
					player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
					break;
				case 7318:
					player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
					break;
				case 7319:
					player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
					break;
				case 7324:
					player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
					break;
				case 47180:
					
					player.getPacketSender().sendMessage("You activate the device..");
					player.moveTo(new Position(2586, 3912));
					break;
                case 26414:
                    player.getInventory().add(915, 1);
                    break;
				case 10091:
				case 8702:
					
					Fishing.startFishing(player, Spot.ROCKTAIL);
					break;
				case 9319:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61){
						player.getPacketSender().sendMessage("You need an Agility level of at least 61 or higher to climb this");
						return;
					}
					if(player.getPosition().getZ() == 0)
						player.moveTo(new Position(3422, 3549, 1));
					else if(player.getPosition().getZ() == 1) {
						if(gameObject.getPosition().getX() == 3447) 
							player.moveTo(new Position(3447, 3575, 2));
						else
							player.moveTo(new Position(3447, 3575, 0));
					}
					break;

				case 9320:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 61 or higher to climb this");
						return;
					}
					if(player.getPosition().getZ() == 1)
						player.moveTo(new Position(3422, 3549, 0));
					else if(player.getPosition().getZ() == 0)
						player.moveTo(new Position(3447, 3575, 1));
					else if(player.getPosition().getZ() == 2)
						player.moveTo(new Position(3447, 3575, 1));
					player.performAnimation(new Animation(828));
					break;
				case 2274:
					if(gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
						player.moveTo(new Position(2914, 5300, 1));
					} else if(gameObject.getPosition().getX() == 2914 && gameObject.getPosition().getY() == 5300) {
						player.moveTo(new Position(2912, 5300, 2));
					} else if(gameObject.getPosition().getX() == 2919 && gameObject.getPosition().getY() == 5276) {
						player.moveTo(new Position(2918, 5274));
					} else if(gameObject.getPosition().getX() == 2918 && gameObject.getPosition().getY() == 5274) {
						player.moveTo(new Position(2919, 5276, 1));
					} else if(gameObject.getPosition().getX() == 3001 && gameObject.getPosition().getY() == 3931 || gameObject.getPosition().getX() == 3652 && gameObject.getPosition().getY() == 3488) {
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
						player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
					}
					break;
				case 7836:
				case 7808:
					int amt = player.getInventory().getAmount(6055);
					if(amt > 0) {
						player.getInventory().delete(6055, amt);
						player.getPacketSender().sendMessage("You put the weed in the compost bin.");
						player.getSkillManager().addExperience(Skill.FARMING, 20*amt);
					} else {
						player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
					}
					break;
				case 5960: //Levers
				case 5959:
					player.setDirection(Direction.WEST);
					TeleportHandler.teleportPlayer(player, new Position(3090, 3475), TeleportType.LEVER);
					break;
				case 5096:
					if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
						player.moveTo(new Position(2649, 9591));
					break;

				case 5094:
					if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
						player.moveTo(new Position(2643, 9594, 2));
					break;

				case 5098:
					if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
						player.moveTo(new Position(2637, 9517));
					break;

				case 5097:
					if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
						player.moveTo(new Position(2636, 9510, 2));
					break;
				case 26428:
				case 26426:
				case 26425:
				case 26427:
					String bossRoom = "Armadyl";
					boolean leaveRoom = player.getPosition().getY() > 5295;
					int index = 0;
					Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
					if(id == 26425) {
						bossRoom = "Bandos";
						leaveRoom = player.getPosition().getX() > 2863;
						index = 1;
						movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
					} else if(id == 26427) {
						bossRoom = "Saradomin";
						leaveRoom = player.getPosition().getX() < 2908;
						index = 2;
						movePos = new Position(leaveRoom ? 2908 : 2907, 5265);
					} else if(id == 26428) {
						bossRoom = "Zamorak";
						leaveRoom = player.getPosition().getY() <= 5331;
						index = 3;
						movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
					}
					if(!leaveRoom && player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] < 20) {
						if (player.getAmountDonated() > 49) {
							
						} else {
							player.getPacketSender().sendMessage("You need "+Misc.anOrA(bossRoom)+" "+bossRoom+" killcount of at least 20 to enter this room.");
							return;
						}
						
					}
					player.moveTo(movePos);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(leaveRoom ? false : true);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
					player.getPacketSender().sendString(16216+index, "0");
					break;
				case 26289:
				case 26286:
				case 26288:
				case 26287:
					if(System.currentTimeMillis() - player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
						player.getPacketSender().sendMessage("");
						player.getPacketSender().sendMessage("You can only pray at a God's altar once every 10 minutes.");
						player.getPacketSender().sendMessage("You must wait another "+(int)((600 - (System.currentTimeMillis() - player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))+" seconds before being able to do this again.");
						return;
					}
					int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false) : id == 26286 ? Equipment.getItemCount(player, "Zamorak", false) : id == 26288 ? Equipment.getItemCount(player, "Armadyl", false) : id == 26287 ? Equipment.getItemCount(player, "Saradomin", false) : 0;
					int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
						player.getPacketSender().sendMessage("You do not need to recharge your Prayer points at the moment.");
						return;
					}
					player.performAnimation(new Animation(645));
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
					player.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(System.currentTimeMillis());
					break;
				case 23093:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 70 to go through this portal.");
						return;
					}
					if(!player.getClickDelay().elapsed(2000)) 
						return;
					int plrHeight = player.getPosition().getZ();
					if(plrHeight == 2)
						player.moveTo(new Position(2914, 5300, 1));
					else if(plrHeight == 1) {
						int x = gameObject.getPosition().getX();
						int y = gameObject.getPosition().getY();
						if(x == 2914 && y == 5300)
							player.moveTo(new Position(2912, 5299, 2));
						else if(x == 2920 && y == 5276)
							player.moveTo(new Position(2920, 5274, 0));
					} else if(plrHeight == 0)
						player.moveTo(new Position(2920, 5276, 1));
					player.getClickDelay().reset();
					break;
				case 26439:
					if(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
						player.getPacketSender().sendMessage("You need a Constitution level of at least 70 to swim across.");
						return;
					}
					if(!player.getClickDelay().elapsed(1000))
						return;
					if(player.isCrossingObstacle())
						return;
					final String startMessage = "You jump into the icy cold water..";
					final String endMessage = "You climb out of the water safely.";
					final int jumpGFX = 68;
					final int jumpAnimation = 772;
					player.setSkillAnimation(773);
					player.setCrossingObstacle(true);
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					player.performAnimation(new Animation(3067));
					final boolean goBack2 = player.getPosition().getY() >= 5344;
					player.getPacketSender().sendMessage(startMessage);  
					player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
					player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
					player.performGraphic(new Graphic (jumpGFX));
					player.performAnimation(new Animation(jumpAnimation));
					TaskManager.submit(new Task(1, player, false) {
						int ticks = 0;
						@Override
						public void execute() {
							ticks++;
							player.getMovementQueue().walkStep(0, goBack2 ? -1 : 1);
							if(ticks >= 10)
								stop();
						}
						@Override
						public void stop() {
							player.setSkillAnimation(-1);
							player.setCrossingObstacle(false);
							player.getUpdateFlag().flag(Flag.APPEARANCE);
							player.getPacketSender().sendMessage(endMessage);
							player.moveTo(new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
							setEventRunning(false);
						}
					});
					player.getClickDelay().reset((System.currentTimeMillis() + 9000));
					break;
				case 26384:
					if(player.isCrossingObstacle())
						return;
					if(!player.getInventory().contains(2347)) {
						player.getPacketSender().sendMessage("You need to have a hammer to bang on the door with.");
						return;
					}
					player.setCrossingObstacle(true);
					final boolean goBack = player.getPosition().getX() <= 2850;
					player.performAnimation(new Animation(377));
					TaskManager.submit(new Task(2, player, false) {
						@Override
						public void execute() {
							player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
							player.setCrossingObstacle(false);
							stop();
						}
					});
					break;
				case 26303:
					if(!player.getClickDelay().elapsed(1200))
						return;
					if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
						player.getPacketSender().sendMessage("You need a Ranged level of at least 70 to swing across here.");
					else if (!player.getInventory().contains(9418)) {
						player.getPacketSender().sendMessage("You need a Mithril grapple to swing across here. Explorer Jack might have one.");
						return;
					} else {
						player.performAnimation(new Animation(789));
						TaskManager.submit(new Task(2, player, false) {
							@Override
							public void execute() {
								player.getPacketSender().sendMessage("You throw your Mithril grapple over the pillar and move across.");
								player.moveTo(new Position(2871, player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
								stop();
							}
						});
						player.getClickDelay().reset();
					}
					break;
				case 4493:
					if(player.getPosition().getX() >= 3432) {
						player.moveTo(new Position(3433, 3538, 1));
					}
					break;
				case 4494:
					player.moveTo(new Position(3438, 3538, 0));
					break;
				case 4495:
					player.moveTo(new Position(3417, 3541, 2));
					break;
				case 4496:
					player.moveTo(new Position(3412, 3541, 1));
					break;
				case 2491:
					player.setDialogueActionId(48);
					DialogueManager.start(player, 87);
					break;
				case 25339:
				case 25340:
					player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
					break;
				case 10229:
				case 10230:
					boolean up = id == 10229;
					player.performAnimation(new Animation(up ? 828 : 827));
					player.getPacketSender().sendMessage("You climb "+(up ? "up" : "down")+" the ladder..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
							stop();
						}
					});
					break;
				case 1568:
					player.moveTo(new Position(3097, 9868));
					break;
				case 5103: //Brimhaven vines
				case 5104:
				case 5105:
				case 5106:
				case 5107:
					if(!player.getClickDelay().elapsed(4000))
						return;
					if(player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
						player.getPacketSender().sendMessage("You need a Woodcutting level of at least 30 to do this.");
						return;
					}
					if(WoodcuttingData.getHatchet(player) < 0) {
						player.getPacketSender().sendMessage("You do not have a hatchet which you have the required Woodcutting level to use.");
						return;
					}
					final Hatchet axe = Hatchet.forId(WoodcuttingData.getHatchet(player));
					player.performAnimation(new Animation(axe.getAnim()));
					gameObject.setFace(-1);
					TaskManager.submit(new Task(3 + RandomUtility.getRandom(4), player, false) {
						@Override
						protected void execute() {
							if(player.getMovementQueue().isMoving()) {
								stop();
								return;
							}
							int x = 0;
							int y = 0;
							if(player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2691 && player.getPosition().getY() == 9564) {
								x = -2;
								y = 0;
							} else if(player.getPosition().getX() == 2683 && player.getPosition().getY() == 9568) {
								x = 0;
								y = 2;
							} else if(player.getPosition().getX() == 2683 && player.getPosition().getY() == 9570) {
								x = 0;
								y = -2;
							} else if(player.getPosition().getX() == 2674 && player.getPosition().getY() == 9479) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2676 && player.getPosition().getY() == 9479) {
								x = -2;
								y = 0;
							} else if(player.getPosition().getX() == 2693 && player.getPosition().getY() == 9482) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2672 && player.getPosition().getY() == 9499) {
								x = 2;
								y = 0;
							} else if(player.getPosition().getX() == 2674 && player.getPosition().getY() == 9499) {
								x = -2;
								y = 0;
							}
							CustomObjects.objectRespawnTask(player, new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
							player.getPacketSender().sendMessage("You chop down the vines..");
							player.getSkillManager().addExperience(Skill.WOODCUTTING, 45);
							player.performAnimation(new Animation(65535));
							player.getMovementQueue().walkStep(x, y);
							stop();
						}
					});
					player.getClickDelay().reset();
					break;
				case 29942:
					if(player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager().getMaxLevel(Skill.SUMMONING)) {
						player.getPacketSender().sendMessage("You do not need to recharge your Summoning points right now.");
						return;
					}
					player.performGraphic(new Graphic(1517));
					player.getSkillManager().setCurrentLevel(Skill.SUMMONING, player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
					player.getPacketSender().sendString(18045, " "+player.getSkillManager().getCurrentLevel(Skill.SUMMONING)+"/"+player.getSkillManager().getMaxLevel(Skill.SUMMONING));
					player.getPacketSender().sendMessage("You recharge your Summoning points.");
					break;
				case 57225:
					if(!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
						player.setDialogueActionId(44);
						DialogueManager.start(player, 79);
					} else {
						player.moveTo(new Position(2906, 5204));
						player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
					}
					break;
				case 2654:
					player.setDialogueActionId(41);
					DialogueManager.start(player, 75);
					break;
				case 9294:
					if(player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
						player.getPacketSender().sendMessage("You need an Agility level of at least 80 to use this shortcut.");
						return;
					}
					player.performAnimation(new Animation(769));
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));	
							stop();
						}
					});
					break;
				case 9293:
					boolean back = player.getPosition().getX() > 2888;
					player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
					break;
				case 2320:
					back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
					player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
					break;
				case 1755:
					player.performAnimation(new Animation(828));
					player.getPacketSender().sendMessage("You climb the stairs..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							if(gameObject.getPosition().getX() == 2547 && gameObject.getPosition().getY() == 9951) {
								player.moveTo(new Position(2548, 3551));
							} else if(gameObject.getPosition().getX() == 3005 && gameObject.getPosition().getY() == 10363) { 
								player.moveTo(new Position(3005, 3962));
							} else if(gameObject.getPosition().getX() == 3084 && gameObject.getPosition().getY() == 9672) {
								player.moveTo(new Position(3117, 3244));
							} else if(gameObject.getPosition().getX() == 3097 && gameObject.getPosition().getY() == 9867) {
								player.moveTo(new Position(3096, 3468));
							}
							stop();
						}
					});
					break;
				case 5110:
					player.moveTo(new Position(2647, 9557));
					player.getPacketSender().sendMessage("You pass the stones..");
					break;
				case 5111:
					player.moveTo(new Position(2649, 9562));
					player.getPacketSender().sendMessage("You pass the stones..");
					break;
				case 6434:
					player.performAnimation(new Animation(827));
					player.getPacketSender().sendMessage("You enter the trapdoor..");
					TaskManager.submit(new Task(1, player, false) {
						@Override
						protected void execute() {
							player.moveTo(new Position(3085, 9672));
							stop();
						}
					});
					break;
				case 19187:
				case 19175:
					Hunter.dismantle(player, gameObject);
					break;
				case 25029:
					PuroPuro.goThroughWheat(player, gameObject);
					break;
				case 47976:
					Nomad.endFight(player, false);
					break;
				case 2182:
					if(!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
						player.getPacketSender().sendMessage("You have no business with this chest. Talk to the Gypsy first!");
						return;
					}
					RecipeForDisaster.openRFDShop(player);
					break;
				case 12356:
					if(!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
						player.getPacketSender().sendMessage("You have no business with this portal. Talk to the Gypsy first!");
						return;
					}
					if(player.getPosition().getZ() > 0) {
						RecipeForDisaster.leave(player);
					} else {
						player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1, true);
						RecipeForDisaster.enter(player);
					}
					break;
				case 9369:
					if (player.getPosition().getY() > 5175) {
						FightPit.addPlayer(player);
					} else {
						FightPit.removePlayer(player, "leave room");
					}
					break;
				case 9368:
					if (player.getPosition().getY() < 5169) {
						FightPit.removePlayer(player, "leave game");
					}
					break;
				case 357:
					
					break;
					
				case 170:
					/*if(!player.getClickDelay().elapsed(3500))
						return;
					int[] rewards = 	{7937, 7937, 7937, 214, 214, 12163, 12160, 454, 454, 454, 2360, 25, 15335, 1620, 1748,  1748, 15271, 15271, 1359, 309, 1275, 1632, 1618, 1516,  1514, 2996, 314, 537, 224, 995, 2364, 52, 15517,  11256  };
					int[] rewardsAmount = {20, 50, 100, 2, 4, 3, 3, 5, 10, 15, 3, 7, 1, 3, 2,                      4,                     2,  4, 1, 1, 1, 1, 2, 20, 10, 5, 20, 3, 5,  10000, 1, 100, 1,                            1   };
					int rewardPos = Misc.getRandom(rewards.length-1);
		
					player.performAnimation(new Animation(827));
					player.getClickDelay().reset();
					player.getSkillManager().addExperience(Skill.THIEVING, 6500);

						player.getInventory().add(rewards[rewardPos], (int)((rewardsAmount[rewardPos])));
					*/
					player.forceChat("Not so fast buddy");
					break;
				case 1:
					
					break;
				case 9357:
					FightCave.leaveCave(player, false);
					break;
				case 9356:
					FightCave.enterCave(player);
					break;
				case 6704:
					player.moveTo(new Position(3577, 3282, 0));
					break;
				case 6706:
					player.moveTo(new Position(3554, 3283, 0));
					break;
				case 6705:
					player.moveTo(new Position(3566, 3275, 0));
					break;
				case 6702:
					player.moveTo(new Position(3564, 3289, 0));
					break;
				case 6703:
					player.moveTo(new Position(3574, 3298, 0));
					break;
				case 6707:
					player.moveTo(new Position(3556, 3298, 0));
					break;
				case 3203:
					if(player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
						if(Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
							player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");			
							return;
						}
						player.getCombatBuilder().reset(true);
						if(player.getDueling().duelingWith > -1) {
							Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
							if(duelEnemy == null)
								return;
							duelEnemy.getCombatBuilder().reset(true);
							duelEnemy.getMovementQueue().reset();
							duelEnemy.getDueling().duelVictory();
						}
						player.moveTo(new Position(3368 + RandomUtility.getRandom(5), 3267+ RandomUtility.getRandom(3), 0));
						player.getDueling().reset();
						player.getCombatBuilder().reset(true);
						player.restart();
					}
					break;
				case 14315:
					PestControl.boardBoat(player);
					break;
				case 14314:
					if(player.getLocation() == Location.PEST_CONTROL_BOAT) {
						player.getLocation().leave(player);
					}
					break;
				case 1738:
					if(player.getLocation() == Location.LUMBRIDGE && player.getPosition().getZ() == 0) {
						player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
					} else {
						player.moveTo(new Position(2840, 3539, 2));
					}
					break;
				case 15638:
					player.moveTo(new Position(2840, 3539, 0));
					break;
				case 15644:
				case 15641:
					switch(player.getPosition().getZ()) {
					case 0:
						player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
						break;
					case 2:
						if(player.getPosition().getX() == 2846) {
							if(player.getInventory().getAmount(8851) < 70) {
								player.getPacketSender().sendMessage("You need at least 70 tokens to enter this area.");
								return;
							}
							DialogueManager.start(player, WarriorsGuild.warriorsGuildDialogue(player));
							player.moveTo(new Position(2847, player.getPosition().getY(), 2));
							WarriorsGuild.handleTokenRemoval(player);
						} else if(player.getPosition().getX() == 2847) {
							WarriorsGuild.resetCyclopsCombat(player);
							player.moveTo(new Position(2846, player.getPosition().getY(), 2));
							player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
						}
						break;
					}
					break;
				case 28714:
					player.performAnimation(new Animation(828));
					player.delayedMoveTo(new Position(3089, 3492), 2);
					break;
				case 1746:
					player.performAnimation(new Animation(827));
					player.delayedMoveTo(new Position(2209, 5348), 2);
					break;
				case 19191:
				case 19189:
				case 19180:
				case 19184:
				case 19182:
				case 19178:
					Hunter.lootTrap(player, gameObject);
					break;
				case 13493:
					
					double c = Math.random()*100;
					int reward = c >= 70 ? 13003 : c >= 45 ? 4131 : c >= 35 ? 1113 : c >= 25 ? 1147 : c >= 18 ? 1163 : c >= 12 ? 1079 : c >= 5 ? 1201 : 1127;
					Stalls.stealFromStall(player, 95, 24800, reward, "You stole some rune equipment.");
					break;
				case 3192:
					player.setDialogueActionId(11);
					DialogueManager.start(player, 20);
					break;
				case 28716:
					if(!player.busy()) {
						player.getSkillManager().updateSkill(Skill.SUMMONING);
						player.getPacketSender().sendInterface(63471);
					} else
						player.getPacketSender().sendMessage("Please finish what you're doing before opening this.");
					break;
				case 6:
					DwarfCannon cannon = player.getCannon();
					if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
						player.getPacketSender().sendMessage("This is not your cannon!");
					} else {
						DwarfMultiCannon.startFiringCannon(player, cannon);
					}
					break;
				case 2:
					player.moveTo(new Position(player.getPosition().getX() > 2690 ? 2687 : 2694, 3714));
					player.getPacketSender().sendMessage("You walk through the entrance..");
					break;
				case 2026:
				case 2028:
				case 2029:
				case 2030:
				case 2031:
					player.setEntityInteraction(gameObject);
					Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
					return;
				case 12692:
				case 2783:
				case 4306:
					player.setInteractingObject(gameObject);
					EquipmentMaking.handleAnvil(player);
					//player.getPacketSender().sendMessage("Temporarily Disabled until fixed.");

					break;
				case 2732:
					EnterAmountOfLogsToAdd.openInterface(player);
					break;
				case 409:
				case 27661:
				case 2640:
				case 36972:
					player.performAnimation(new Animation(645));
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
						player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
						player.getPacketSender().sendMessage("You recharge your Prayer points.");
					}
					break;
				case 8749:			
					player.setSpecialPercentage(100);
					CombatSpecial.updateBar(player);
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
					player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, player.getSkillManager().getMaxLevel(Skill.CONSTITUTION), true);
					player.getPacketSender().sendMessage("Your special attack, prayer, and health has been restored.");	
					player.performGraphic(new Graphic(1302));
					break;
				case 4859:
					player.performAnimation(new Animation(645));
					if(player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager().getMaxLevel(Skill.PRAYER)) {
						player.getSkillManager().setCurrentLevel(Skill.PRAYER, player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
						player.getPacketSender().sendMessage("You recharge your Prayer points.");
					}
					break;
				case 61:
					if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
						player.getPacketSender().sendMessage("You need a Defence level of at least 30 to use this altar.");
						return;
					}
					player.performAnimation(new Animation(645));
					if(player.getPrayerbook() == Prayerbook.NORMAL) {
						player.getPacketSender().sendMessage("You sense a surge of power flow through your body!");
						player.setPrayerbook(Prayerbook.CURSES);
					} else {
						player.getPacketSender().sendMessage("You sense a surge of purity flow through your body!");
						player.setPrayerbook(Prayerbook.NORMAL);
					}
					player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
					PrayerHandler.deactivateAll(player);
					CurseHandler.deactivateAll(player);
					break;
				case 6552:
					player.performAnimation(new Animation(645));
					player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL : MagicSpellbook.ANCIENT);
					player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");
					Autocasting.resetAutocast(player, true);
					break;
				case 13179:
					if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
						player.getPacketSender().sendMessage("You need a Defence level of at least 40 to use this altar.");
						return;
					}
					player.performAnimation(new Animation(645));
					player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL : MagicSpellbook.LUNAR);
					player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId()).sendMessage("Your magic spellbook is changed..");;
					Autocasting.resetAutocast(player, true);
					break;
				case 11098:
					MeleeBattleRoyaleChest.handleChest(player, gameObject);
					ChestRotator.openChest(player);
					ChestRotator.despawn(true);
					CustomObjects.deleteGlobalObject(gameObject);
					break;
				case 11099:
					MeleeBattleRoyaleChest.handleChest(player, gameObject);
					ChestRotator.openChest(player);
					ChestRotator.despawn(true);
					CustomObjects.deleteGlobalObject(gameObject);
					break;
				case 172:
					CrystalChest.handleChest(player, gameObject);
					break;
				case 6910:
				case 4483:
				case 3193:
				case 2213:
				case 4005:
				case 11758:
				case 14367:
				case 42192:
				case 75:
					player.setTempBankTabs(null);
					player.getBank(player.getCurrentBankTab()).open();
					break;
				case 3194:
					player.setTempBankTabs(null);
					player.getBank(player.getCurrentBankTab()).open();
					break;
				}
			}
		}));
	}

	private static void secondClick(final Player player, Packet packet) {
		final int id = packet.readLEShortA();
		final int y = packet.readLEShort();
		final int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(id > 0 && id != 6 && !RegionClipping.objectExists(gameObject)) {
			//player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
			return;
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		player.setInteractingObject(gameObject).setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			public void execute() {
				if(MiningData.forRock(gameObject.getId()) != null) {
					Prospecting.prospectOre(player, id);
					return;
				}
				if (player.getFarming().click(player, x, y, 1))
					return;
				switch(gameObject.getId()) {
				case 10_000:
					player.getPlayerOwnedShopManager().open();
					break;
				case 2654:
					player.setDialogueActionId(41);
					player.setInputHandling(new DonateToWell());
					player.getPacketSender().sendInterfaceRemoval().sendEnterAmountPrompt("How much money would you like to contribute with?");
					break;
					case 3192:
						player.getGroupIronman().openLeaderboard();
						break;
				case 2646:
				case 312:
					if(!player.getClickDelay().elapsed(1200))
						return;
					if(player.getInventory().isFull()) {
						player.getPacketSender().sendMessage("You don't have enough free inventory space.");
						return;
					}
					String type = gameObject.getId() == 312 ? "Potato" : "Flax";
					player.performAnimation(new Animation(827));
					if(Location.inResource(player)) {
						player.getInventory().add(gameObject.getId() == 312 ? 1943 : 1780, 1);

					} else {
					player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);

					}
					player.getPacketSender().sendMessage("You pick some "+type+"..");
					gameObject.setPickAmount(gameObject.getPickAmount() + 1);
					if(RandomUtility.getRandom(3) == 1 && gameObject.getPickAmount() >= 1 || gameObject.getPickAmount() >= 6) {
						player.getPacketSender().sendClientRightClickRemoval();
						gameObject.setPickAmount(0);
						CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()), gameObject, 10);
					}
					player.getClickDelay().reset();
					break;
				case 2644:
					Flax.showSpinInterface(player);
					break;
				case 6:
					DwarfCannon cannon = player.getCannon();
					if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
						player.getPacketSender().sendMessage("This is not your cannon!");
					} else {
						DwarfMultiCannon.pickupCannon(player, cannon, false);
					}
					break;
				case 4875:
					Stalls.stealFromStall(player, 1, 5100, 19994, "You steal a ice coin.");
					break;
				case 4874:
					Stalls.stealFromStall(player, 30, 6130, 19994, "You steal a ice coin.");
					break;
				case 4876:
					Stalls.stealFromStall(player, 60, 7370, 19994, "You steal a ice coin.");
					break;
				case 4877:
					Stalls.stealFromStall(player, 65, 7990, 19994, "You steal a ice coin.");
					break;
				case 4878:
					Stalls.stealFromStall(player, 80, 9230, 19994, "You steal a ice coin.");
					break;
				case 6189:
				
				case 26814:
				case 11666:
					Smelting.openInterface(player);
					break;
				case 2152:
					player.performAnimation(new Animation(8502));
					player.performGraphic(new Graphic(1308));
					player.getSkillManager().setCurrentLevel(Skill.SUMMONING, player.getSkillManager().getMaxLevel(Skill.SUMMONING));
					player.getPacketSender().sendMessage("You renew your Summoning points.");
					break;
				}
			}
		}));
	}

	private static void thirdClick(Player player, Packet packet) {}

	private static void fourthClick(Player player, Packet packet) {}

	private static void fifthClick(final Player player, Packet packet) {
		final int id = packet.readUnsignedShortA();
		final int y = packet.readUnsignedShortA();
		final int x = packet.readShort();
		final Position position = new Position(x, y, player.getPosition().getZ());
		final GameObject gameObject = new GameObject(id, position);
		if(!Construction.buildingHouse(player)) {
			if(id > 0 && !RegionClipping.objectExists(gameObject)) {
				//player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
				return;
			}
		}
		player.setPositionToFace(gameObject.getPosition());
		int distanceX = (player.getPosition().getX() - position.getX());
		int distanceY = (player.getPosition().getY() - position.getY());
		if (distanceX < 0)
			distanceX = -(distanceX);
		if (distanceY < 0)
			distanceY = -(distanceY);
		int size = distanceX > distanceY ? distanceX : distanceY;
		gameObject.setSize(size);
		player.setInteractingObject(gameObject);
		player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
			@Override
			public void execute() {
				switch(id) {
				}
				Construction.handleFifthObjectClick(x, y, id, player);
			}
		}));
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.isTeleporting() || player.isPlayerLocked() || player.getMovementQueue().isLockMovement())
			return;
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			firstClick(player, packet);
			break;
		case SECOND_CLICK:
			secondClick(player, packet);
			break;
		case THIRD_CLICK:
			//thirdClick(player, packet);
			break;
		case FOURTH_CLICK:
			//fourthClick(player, packet);
			break;
		case FIFTH_CLICK:
			fifthClick(player, packet);
			break;
		}
	}

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234, FIFTH_CLICK = 228;
}
