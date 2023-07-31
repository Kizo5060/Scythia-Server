package com.ruseps.net.packet.impl;

import com.ruseps.BalloonDropParty;
import com.ruseps.world.content.groupironman.impl.CreateGroupInputListener;
import com.ruseps.world.content.newdroptable.DropTableInterface;
import com.ruseps.world.content.newdroptable.SearchForDropTableNpc;
import com.ruseps.world.content.referral.RefferalButtons;
import com.ruseps.world.content.upgrading.UpgradeListener;
import com.ruseps.world.content.scratchcards.ScratchCard;
import com.ruseps.GameSettings;
import com.ruseps.model.Animation;
import com.ruseps.model.GameMode;
import com.ruseps.model.Graphic;
import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Bank.BankSearchAttributes;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.WeaponInterfaces.WeaponInterface;
import com.ruseps.model.input.impl.EnterClanChatToJoin;
import com.ruseps.model.input.impl.EnterSyntaxToBankSearchFor;
import com.ruseps.model.input.impl.InviteRaidsPlayer;
import com.ruseps.model.input.impl.PosInput;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.net.packet.interaction.PacketInteractionManager;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.BankPin;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.Consumables;
import com.ruseps.world.content.DropLog;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.achievements.AchievementInterface;
import com.ruseps.world.content.Emotes;
import com.ruseps.world.content.EnergyHandler;
import com.ruseps.world.content.ExperienceLamps;
import com.ruseps.world.content.ItemsKeptOnDeath;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.content.LoyaltyProgramme;
import com.ruseps.world.content.MoneyPouch;
import com.ruseps.world.content.PlayersOnlineInterface;
import com.ruseps.world.content.ProfileViewing;
import com.ruseps.world.content.Sounds;
import com.ruseps.world.content.Sounds.Sound;
import com.ruseps.world.content.StartScreen;
import com.ruseps.world.content.clan.ClanChat;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.collectionlog.CollectionLogButtons;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.magic.MagicSpells;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.combat.weapon.FightType;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueOptions;
import com.ruseps.world.content.dropchecker.NPCDropTableChecker;
import com.ruseps.world.content.droppreview.AVATAR;
import com.ruseps.world.content.droppreview.BARRELS;
import com.ruseps.world.content.droppreview.BORKS;
import com.ruseps.world.content.droppreview.CERB;
import com.ruseps.world.content.droppreview.CORP;
import com.ruseps.world.content.droppreview.DAGS;
import com.ruseps.world.content.droppreview.GLAC;
import com.ruseps.world.content.droppreview.GWD;
import com.ruseps.world.content.droppreview.KALPH;
import com.ruseps.world.content.droppreview.KBD;
import com.ruseps.world.content.droppreview.LIZARD;
import com.ruseps.world.content.droppreview.NEXX;
import com.ruseps.world.content.droppreview.PHEON;
import com.ruseps.world.content.droppreview.SKOT;
import com.ruseps.world.content.droppreview.SLASHBASH;
import com.ruseps.world.content.droppreview.TDS;
import com.ruseps.world.content.fuser.FuserEnum;
import com.ruseps.world.content.fuser.FuserHandler;
import com.ruseps.world.content.goodie_bag.GoodieBagManager;
import com.ruseps.world.content.grandexchange.GrandExchange;
import com.ruseps.world.content.groupironman.impl.InvitePlayerInputListener;
import com.ruseps.world.content.groupironman.impl.KickPlayerInputListener;
import com.ruseps.world.content.instance_manager.InstanceButtons;
import com.ruseps.world.content.loot_display.RewardsHandler;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.minigames.impl.Nomad;
import com.ruseps.world.content.minigames.impl.PestControl;
import com.ruseps.world.content.minigames.impl.RecipeForDisaster;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.skill.ChatboxInterfaceSkillAction;
import com.ruseps.world.content.skill.impl.construction.Construction;
import com.ruseps.world.content.skill.impl.crafting.LeatherMaking;
import com.ruseps.world.content.skill.impl.crafting.Tanning;
import com.ruseps.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ruseps.world.content.skill.impl.dungeoneering.DungeoneeringParty;
import com.ruseps.world.content.skill.impl.dungeoneering.ItemBinding;
import com.ruseps.world.content.skill.impl.fletching.Fletching;
import com.ruseps.world.content.skill.impl.herblore.IngridientsBook;
import com.ruseps.world.content.skill.impl.slayer.Slayer;
import com.ruseps.world.content.skill.impl.smithing.SmithingData;
import com.ruseps.world.content.skill.impl.summoning.PouchMaking;
import com.ruseps.world.content.skill.impl.summoning.SummoningTab;
import com.ruseps.world.content.teleport.TeleportHandlerNew;
import com.ruseps.world.content.teleportation.BossTeleports;
import com.ruseps.world.content.teleportation.MinigameTeleports;
import com.ruseps.world.content.teleportation.Teleporting;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.content.transportation.TeleportType;
import com.ruseps.world.entity.impl.player.Player;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {


    public static final boolean isBlockedGameMode(Player player) {
        GameMode[] mode = {
                GameMode.IRONMAN,
                GameMode.HARDCORE_IRONMAN
        };
        for (GameMode gameMode : mode) {
            if (player.getGameMode() == gameMode)
                return true;
        }
        return false;
    }

    @Override
    public void handleMessage(Player player, Packet packet) {

        int id = packet.readShort();
        System.out.println(id);

        long start = System.currentTimeMillis();

        if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER) {
            player.getPacketSender().sendMessage("Clicked button: " + id);
        }

        if (PacketInteractionManager.checkButtonInteraction(player, id)) {
            System.out.println("Well gg?");
            return;
        }
        
        if (player.getAchievementInterface() != null && player.getAchievementInterface().handleButton(id)) {
			return;
		}
        
        if(InstanceButtons.isInstanceButton(player, id)) {
			return;
		}

        if (new RewardsHandler(player).button(id)) {
            return;
        }

        if (checkHandlers(player, id))
            return;

        if (NPCDropTableChecker.getSingleton().handleButtonClick(player, id)) {
            return;
        }
        if (BossTeleports.handleButton(player, id)) {
            return;
        }
        if (RefferalButtons.isRefferalButton(player, id)) {
			return;
		}
        if(CollectionLogButtons.isCollectionLogButton(player, id)) {
			return;
		}
        if (MinigameTeleports.handleButton(player, id)) {
            return;
        }
        if (id >= 32623 && id <= 32722) {
            player.getPlayerOwnedShopManager().handleButton(id);
        }

        //player.getBattlePass().handleClick(id);
        
        if (id >= -19635 && id <= -19580) {
            if (player.teleports.size() >= (id + 19635) + 1) {
                TeleportHandlerNew.buildInterface((id + 19635), player);
            } else {
                player.getPacketSender().sendMessage("This teleport is empty!");
            }
        }

        if (id >= -22034 && id <= -22025) {
            player.combineIndex = (id + 22034);
            FuserHandler.openInterface((FuserEnum.values()[player.combineIndex]), player);
        }

        if (id >= 32410 && id <= 32460) {

        }
        switch (id) {
        case 19314:
            player.setNoteWithdrawal(!player.withdrawAsNote());
            System.out.println("Withdrawing as note: "+player.withdrawAsNote());
            if (player.withdrawAsNote()) {
                player.getPacketSender().sendToggle(888, 1);
            } else {
                player.getPacketSender().sendToggle(888, 0);
            }
            break;

            case 32009:
                if (player.getGroupIronmanGroup() != null) {
                    player.sendMessage("You already have a group");
                    return;
                }
                player.getPacketSender().sendEnterInputPrompt("What would you like to name your group?");
                player.setInputHandling(new CreateGroupInputListener());
                break;

            case 32010:
                player.getPacketSender().sendEnterInputPrompt("Who would you like to invite?");
                player.setInputHandling(new InvitePlayerInputListener());
                break;
                
            case 19855:
    			PlayerPanel.handleSwitch(player, 1, false);
    			player.getPacketSender().setInterfaceClicked(19850, 19855, true);
    			break;
    		case 19858:
    			PlayerPanel.handleSwitch(player, 2, false);
    			player.getPacketSender().setInterfaceClicked(19850, 19858, true);
    			break;
    		case 19861:
    			PlayerPanel.handleSwitch(player, 3, false);
    			player.getPacketSender().setInterfaceClicked(19850, 19861, true);
    			break;
    		case 19864:
    			PlayerPanel.handleSwitch(player, 4, false);
    			player.getPacketSender().setInterfaceClicked(19850, 19864, true);
    			break;
    			
    		case 19867:
    			AchievementInterface.open(player);
				break;
            case 32011:
                player.getPacketSender().sendEnterInputPrompt("Who would you like to kick?");
                player.setInputHandling(new KickPlayerInputListener());
                break;

            case 32012:
                if (player.getGroupIronmanGroup() == null) {
                    player.sendMessage("Your not in a group");
                    return;
                }
                if (player.getGroupIronmanGroup().getOwnerName().equalsIgnoreCase(player.getUsername())) {
                    player.getGroupIronmanGroup().delete();
                }
                break;

            case 29730:
                player.getPacketSender().updateInterfaceVisibility(32007, true);
                player.getPacketSender().updateInterfaceVisibility(29732, true);
                break;

            case 22222:
                player.setInputHandling(new SearchForDropTableNpc());
                player.getPacketSender()
                        .sendEnterInputPrompt("Type the NPC name that you would like to search for");
                break;

            case 19317:
                if (player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER
                        || player.getRights() == PlayerRights.MODERATOR
                        || player.getRights() == PlayerRights.ADMINISTRATOR) {
                    BalloonDropParty.spawn(player);
                }
                break;

            case 24310:
            	
                player.setViewingCosmeticTab(!player.isViewingCosmeticTab());
                
                if (player.isViewingCosmeticTab()) {
                    player.getCosmeticGear().update();
                } else {
                    player.getEquipment().refreshItems();
                }
                
                player.sendMessage("Viewing cosmetic tab: " + player.isViewingCosmeticTab());
               // player.getPacketSender().sendTabInterface(4, 27650);
                break;


            case -7520:
            case -7516:
            case -7512:
            case -7508:
            case -7504:
                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null) {
                    if (player.equals(player.getRaidsParty().getOwner())) {
                        if (player.getMinigameAttributes()
                                .getRaidsAttributes()
                                .getParty()
                                .getPlayers()
                                .size() >= ((id - 58016) / 4) + 1) {
                            Player playerToKick = player.getMinigameAttributes()
                                    .getRaidsAttributes()
                                    .getParty()
                                    .getPlayers()
                                    .get((id - 58016) / 4);
                            if (playerToKick == player) {
                                player.sendMessage("You cannot kick yourself!");
                            } else {
                                player.getMinigameAttributes()
                                        .getRaidsAttributes()
                                        .getParty()
                                        .remove(playerToKick, false,
                                                true);

                            }
                        }
                    } else {
                        player.sendMessage("Only the leader of the party can kick players!");
                    }
                }
                return;
            case -26218:
            case -26215:
            case -26209:
            case -26212:
                new ScratchCard(player).reveal(id);
                break;
            case -11438:
                player.getPlayerOwnedShopManager().openEditor();
                break;
            case -3334:
                /*** LIVE ***/
                if (!player.getClickDelay().elapsed(3000)) {
                    player.sendMessage("@red@Please wait a few secounds before trying to upgrade again.");
                    return;
                }
                new UpgradeListener(player).upgrade();
                player.getClickDelay().reset();
                break;
            case -3306:
                player.getPacketSender().sendInterfaceRemoval();
                break;

            case -19703:
                if (player.getLocation() != null && player.getLocation() == Location.WILDERNESS) {
                    player.getPacketSender().sendMessage("You cannot do this at the moment.");
                    return;
                }
                player.getPacketSender().sendInterfaceRemoval();

                Position position = player.teleports.get(player.teleportIndex).getPosition();
                TeleportHandler.teleportPlayer(player, position, TeleportType.NORMAL);
                player.getPacketSender().sendMessage("Teleporting!");
                break;

            case -21998:
                if (!FuserEnum.checkRequirements(FuserEnum.values()[player.combineIndex], player)) {
                    player.getPacketSender().sendMessage("You don't meet the requirements!");
                    return;
                }

                FuserEnum.removeRequirements(FuserEnum.values()[player.combineIndex], player);
                player.getInventory()
                        .add(new Item(FuserEnum.values()[player.combineIndex].getEndItem(), 1));
                World.sendMessage("<shad=0><col=23545>@yel@ [FUSION] @red@" + player.getUsername() + "@yel@ has created a @red@" + ItemDefinition.forId(FuserEnum.values()[player.combineIndex].getEndItem())
                        .getName() + "!");
                break;

            case -17500:
                if (!player.getClickDelay().elapsed(5000)) {
                    player.sendMessage(player.getUsername() + "@red@ Please wait a 5 secounds before trying to claim a reward again.");
                    return;
                }
                if (player.getInventory().contains(18689)) {
                    int[] common = new int[]{20106, 20108, 20110, 20112, 20114, 20116, 19919, 19920};
                    int[] uncommon = new int[]{14018, 20150, 20154, 20156, 20158, 19918};
                    int[] rare = new int[]{2572, 11005, 14460, 14462, 18896, 18898, 18900};
                    player.getChestViewer().open(6759, common, uncommon, rare);
                    player.getClickDelay().reset();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getInventory().delete(18689);
                    World.sendMessage("<img=10>@blu@" + player.getUsername() + " has just donated and opened the Donor Chest #1");
                    player.moveTo(GameSettings.DONOR_CHEST_ZONE.copy());
                    return;
                } else {
                    player.getPacketSender()
                            .sendMessage(player.getUsername() + ", Sorry you require the Key. Please contact staff to purchase this product.");
                }
                if (player.getInventory().contains(19645)) {
                    int[] common = new int[]{2572, 11005, 14460, 14462, 18896, 18898, 18900};
                    int[] uncommon = new int[]{10503, 11654, 20522, 11659, 11653, 20146, 11650};
                    int[] rare = new int[]{10682, 10683, 10684, 10685, 940, 11039, 11040, 11041, 11042, 10480, 19888};
                    player.getChestViewer().open(6759, common, uncommon, rare);
                    player.getClickDelay().reset();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getInventory().delete(19645);
                    World.sendMessage("<img=10>@blu@" + player.getUsername() + " has just donated opened the Donor Chest #2");
                    player.moveTo(GameSettings.DONOR_CHEST_ZONE.copy());
                    return;
                } else {
                    player.getPacketSender()
                            .sendMessage(player.getUsername() + ", Sorry you require the Key. Please contact staff to purchase this product.");

                }
                if (player.getInventory().contains(18665)) {
                    int[] common = new int[]{20100, 20102, 11896, 20104, 10822, 10824, 10826, 11206, 11208, 7082};
                    int[] uncommon = new int[]{11896, 10502, 20126, 20136, 20138, 20140, 20142, 20144, 20656};
                    int[] rare = new int[]{11661, 11662, 11679, 11680, 11681, 11682, 11658, 20520, 20658, 20659, 20660};
                    player.getChestViewer().open(6759, common, uncommon, rare);
                    player.getClickDelay().reset();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getInventory().delete(18665);
                    World.sendMessage("<img=10>@blu@" + player.getUsername() + " has just donated X Amount and opened the Donor Chest #3");
                    player.moveTo(GameSettings.DONOR_CHEST_ZONE.copy());
                    return;
                } else {
                    player.getPacketSender()
                            .sendMessage(player.getUsername() + ", Sorry you require the Key. Please contact staff to purchase this product.");
                }
                if (player.getInventory().contains(18647)) {
                    int[] common = new int[]{20650, 20651, 20652, 20653, 20654, 937, 20658, 20659, 20660};
                    int[] uncommon = new int[]{996, 965, 5085, 5086, 5087, 5088, 5089, 18876, 11651, 5082, 5083, 5084};
                    int[] rare = new int[]{799, 894, 895, 896, 798, 938, 8860, 8861, 8862, 8871, 8656, 8657, 8658, 8659, 8660,};
                    player.getChestViewer().open(6759, common, uncommon, rare);
                    player.getClickDelay().reset();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getInventory().delete(18647);
                    World.sendMessage("<img=10>@blu@" + player.getUsername() + " has just donated X Amount and opened the Donor Chest #4");
                    player.moveTo(GameSettings.DONOR_CHEST_ZONE.copy());
                    return;
                } else {
                    player.getPacketSender()
                            .sendMessage(player.getUsername() + ", Sorry you require the Key. Please contact staff to purchase this product.");
                }
                if (player.getInventory().contains(14471)) {
                    int[] common = new int[]{799, 894, 895, 896, 798, 938, 8860, 8861, 8862, 8871, 8656, 8657, 8658, 8659, 8660};
                    int[] uncommon = new int[]{11068, 11071, 11078, 11087, 11067, 2542, 18440, 18914, 18946, 20527, 20528, 20529};
                    int[] rare = new int[]{6830, 6832, 7960, 11949, 405, 18982, 18983, 18993, 19021, 2545, 2544, 2546, 2547, 2548, 2549};
                    player.getChestViewer().open(6759, common, uncommon, rare);
                    player.getClickDelay().reset();
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getInventory().delete(14471);
                    World.sendMessage("<img=10>@blu@" + player.getUsername() + " has just donated X Amount and opened the Donor Chest #5");
                    player.moveTo(GameSettings.DONOR_CHEST_ZONE.copy());
                    return;
                } else {
                    player.getPacketSender()
                            .sendMessage(player.getUsername() + ", Sorry you require the Key. Please contact staff to purchase this product.");
                }
                break;
            /* Mystery box */
            case -18532:
                if (player.ownerBoxx == true) {
                    player.getOwnerBox().spin();
                } else if (player.mboxx == true) {
                    player.getMysteryBox().spin();
                } else if (player.auroraBoxx == true) {
                    player.getAuroraBox().spin();
                } else if (player.petBoxx == true) {
                    player.getPetBox().spin();
                }
                break;
            case -26734:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case -28734:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case -27933:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 26746:
                player.getPacketSender().sendInterface(37600);
                break;
            case 26113:
                player.dropLogOrder = !player.dropLogOrder;
                if (player.dropLogOrder) {
                    player.getPA().sendFrame126(26113, "Oldest to Newest");
                } else {
                    player.getPA().sendFrame126(26113, "Newest to Oldest");
                }
                break;
            case -29031:
                ProfileViewing.rate(player, true);
                break;
            case -29028:
                ProfileViewing.rate(player, false);
                break;
            case -27454:
            case -27534:
            case -18533:
            case 5384:
            case 12729:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case -17631:
                KBD.closeInterface(player);
                break;

            case 10003:
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
                break;

            case -17629:
                if (player.getLocation() == Location.KING_BLACK_DRAGON) {
                    KBD.nextItem(player);
                }
                if (player.getLocation() == Location.SLASH_BASH) {
                    SLASHBASH.nextItem(player);
                }
                if (player.getLocation() == Location.TORM_DEMONS) {
                    TDS.nextItem(player);
                }
                if (player.getLocation() == Location.CORPOREAL_BEAST) {
                    CORP.nextItem(player);
                }
                if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
                    DAGS.nextItem(player);
                }
                if (player.getLocation() == Location.BANDOS_AVATAR) {
                    AVATAR.nextItem(player);
                }
                if (player.getLocation() == Location.KALPHITE_QUEEN) {
                    KALPH.nextItem(player);
                }
                if (player.getLocation() == Location.PHOENIX) {
                    PHEON.nextItem(player);
                }
                if (player.getLocation() == Location.GLACORS) {
                    GLAC.nextItem(player);
                }
                if (player.getLocation() == Location.SKOTIZO) {
                    SKOT.nextItem(player);
                }
                if (player.getLocation() == Location.CERBERUS) {
                    CERB.nextItem(player);
                }
                if (player.getLocation() == Location.NEX) {
                    NEXX.nextItem(player);
                }
                if (player.getLocation() == Location.GODWARS_DUNGEON) {
                    GWD.nextItem(player);
                }
                if (player.getLocation() == Location.BORK) {
                    BORKS.nextItem(player);
                }
                if (player.getLocation() == Location.LIZARDMAN) {
                    LIZARD.nextItem(player);
                }
                if (player.getLocation() == Location.BARRELCHESTS) {
                    BARRELS.nextItem(player);
                }
                break;

            case -17630:
                if (player.getLocation() == Location.KING_BLACK_DRAGON) {
                    KBD.previousItem(player);
                }
                if (player.getLocation() == Location.SLASH_BASH) {
                    SLASHBASH.previousItem(player);
                }
                if (player.getLocation() == Location.TORM_DEMONS) {
                    TDS.previousItem(player);
                }
                if (player.getLocation() == Location.CORPOREAL_BEAST) {
                    CORP.previousItem(player);
                }
                if (player.getLocation() == Location.DAGANNOTH_DUNGEON) {
                    DAGS.previousItem(player);
                }
                if (player.getLocation() == Location.BANDOS_AVATAR) {
                    AVATAR.previousItem(player);
                }
                if (player.getLocation() == Location.KALPHITE_QUEEN) {
                    KALPH.previousItem(player);
                }
                if (player.getLocation() == Location.PHOENIX) {
                    PHEON.previousItem(player);
                }
                if (player.getLocation() == Location.GLACORS) {
                    GLAC.previousItem(player);
                }
                if (player.getLocation() == Location.SKOTIZO) {
                    SKOT.previousItem(player);
                }
                if (player.getLocation() == Location.CERBERUS) {
                    CERB.previousItem(player);
                }
                if (player.getLocation() == Location.NEX) {
                    NEXX.previousItem(player);
                }
                if (player.getLocation() == Location.GODWARS_DUNGEON) {
                    GWD.previousItem(player);
                }
                if (player.getLocation() == Location.BORK) {
                    BORKS.previousItem(player);
                }
                if (player.getLocation() == Location.LIZARDMAN) {
                    LIZARD.previousItem(player);
                }
                if (player.getLocation() == Location.BARRELCHESTS) {
                    BARRELS.previousItem(player);
                }
                break;

            case 25253:
                DropLog.open(player);
                break;
            case 2461:
    			player.getPacketSender().sendInterfaceRemoval();
    			player.sendMessage("Clicked it ->");
    			break;
            case 32602:
                player.setInputHandling(new PosInput());
                player.getPacketSender()
                        .sendEnterInputPrompt("What/Who would you like to search for?");
                break;
            case 1036:
                EnergyHandler.rest(player);
                break;
            case -26376:
                //PlayersOnlineInterface.showInterface(player);
                break;
            case 26601:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 46343);
                //StaffList.updateInterface(player);
                break;
            case 32388:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 26600);
                break;

            case 27229:
                DungeoneeringParty.create(player);
                break;

            case -7533:
                if (player.getLocation() == Location.RAIDS_ONE_ENTRANCE || player.getLocation() == Location.RAIDS_TWO_ENTRANCE || player.getLocation() == Location.RAIDS_THREE_ENTRANCE || player.getLocation() == Location.RAIDS_FOUR_ENTRANCE || player.getLocation() == Location.RAIDS_FIVE_ENTRANCE || player.getLocation() == Location.RAIDS_SIX_ENTRANCE || player.getLocation() == Location.RAIDS_SEVEN_ENTRANCE) {
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null) {
                        if (player.getMinigameAttributes()
                                .getRaidsAttributes()
                                .getParty()
                                .getOwner() != player) {
                            player.getPacketSender()
                                    .sendMessage("Only the party leader can attemptInvite other players.");
                        } else {
                            player.setInputHandling(new InviteRaidsPlayer());
                            player.getPacketSender().sendEnterInputPrompt("Invite Player");
                        }
                    } else {
                        new RaidsParty(player).create();
                    }
                }
                return;
            case -7530:
                if (player.getLocation() == Location.RAIDS_ONE_ENTRANCE
                        || player.getLocation() == Location.RAIDS_ONE_PHASE_THREE
                        || player.getLocation() == Location.RAIDS_ONE_PHASE_TWO
                        || player.getLocation() == Location.RAIDS_ONE_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_TWO_ENTRANCE
                        || player.getLocation() == Location.RAIDS_TWO_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_THREE_ENTRANCE
                        || player.getLocation() == Location.RAIDS_THREE_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_THREE_PHASE_TWO
                        || player.getLocation() == Location.RAIDS_THREE_PHASE_THREE
                        || player.getLocation() == Location.RAIDS_FOUR_ENTRANCE
                        || player.getLocation() == Location.RAIDS_FOUR_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_FOUR_PHASE_TWO
                        || player.getLocation() == Location.RAIDS_FOUR_PHASE_THREE
                        || player.getLocation() == Location.RAIDS_FIVE_ENTRANCE
                        || player.getLocation() == Location.RAIDS_FIVE_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_FIVE_PHASE_TWO
                        || player.getLocation() == Location.RAIDS_FIVE_PHASE_THREE
                        || player.getLocation() == Location.RAIDS_FIVE_PHASE_FOUR
                        || player.getLocation() == Location.RAIDS_SIX_ENTRANCE
                        || player.getLocation() == Location.RAIDS_SIX_PHASE_ONE
                        || player.getLocation() == Location.RAIDS_SEVEN_ENTRANCE
                        || player.getLocation() == Location.RAIDS_SEVEN_PHASE_ONE

                ) {
                    RaidsParty party = player.getMinigameAttributes()
                            .getRaidsAttributes()
                            .getParty();
                    party.remove(player, false, true);
                    party.sendMessage("<img=10>@blu@" + player.getUsername() + " has left the Raiding Party");
                    if (player.getLocation() == Location.RAIDS_ONE_ENTRANCE) {
                        player.moveTo(new Position(2297, 3331, 0));
                    } else if (player.getLocation() == Location.RAIDS_TWO_ENTRANCE) {
                        player.moveTo(new Position(3038, 2848, 0));
                    } else if (player.getLocation() == Location.RAIDS_THREE_ENTRANCE) {
                        player.moveTo(new Position(1246, 1247, 0));
                    }
                    player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000);
                    player.getPacketSender().sendDungeoneeringTabIcon(false);
                    player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
                    player.sendMessage("<img=10>@blu@You have left your Raiding Party.");
                    party.sendMessage("<img=10>@blu@" + player.getUsername() + " has left the Raiding Party");
                }

                return;
            case 26226:
            case 26229:
                if (Dungeoneering.doingDungeoneering(player)) {
                    DialogueManager.start(player, 114);
                    player.setDialogueActionId(71);
                } else {
                    Dungeoneering.leave(player, false, true);
                }
                break;
            case 26244:
            case 26247:
                if (player.getMinigameAttributes()
                        .getDungeoneeringAttributes()
                        .getParty() != null) {
                    if (player.getMinigameAttributes()
                            .getDungeoneeringAttributes()
                            .getParty()
                            .getOwner()
                            .getUsername()
                            .equals(player.getUsername())) {
                        DialogueManager.start(player, id == 26247 ? 106 : 105);
                        player.setDialogueActionId(id == 26247 ? 68 : 67);
                    } else {
                        player.getPacketSender()
                                .sendMessage("Only the party owner can change this setting.");
                    }
                }
                break;
            case 28180:
                player.getPacketSender().sendInterfaceRemoval();
                String plrCannotEnter = null;
                if (player.getSummoning().getFamiliar() != null) {
                    player.getPacketSender()
                            .sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
                    player.getPacketSender()
                            .sendMessage("You must dismiss your familiar before joining the dungeon");
                    return;
                }

                player.getPacketSender().sendMessage("<shad=0>@yel@Dungeoneering Is Currenty Disabled.");
                
                break;

            case -4884:
            case 3902:
            case -31929:
            case 32606:
                player.getPacketSender().sendInterfaceRemoval();

                break;


            case 14176:
                player.setUntradeableDropItem(null);
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 14175:
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getUntradeableDropItem() != null && player.getInventory()
                        .contains(player.getUntradeableDropItem().getId())) {
                    ItemBinding.unbindItem(player, player.getUntradeableDropItem().getId());
                    player.getInventory().delete(player.getUntradeableDropItem());
                    player.getPacketSender()
                            .sendMessage("Your item vanishes as it hits the floor.");
                    Sounds.sendSound(player, Sound.DROP_ITEM);
                }
                player.setUntradeableDropItem(null);
                break;
            case 1013:
                player.getSkillManager().setTotalGainedExp(0);
                break;
            case -26373:
                if (WellOfGoodwill.isActive()) {
                    player.getPacketSender()
                            .sendMessage("<img=10> <col=008FB2>The Well of Goodwill is granting 30% bonus experience for another " + WellOfGoodwill.getMinutesRemaining() + " minutes.");
                } else {
                    player.getPacketSender()
                            .sendMessage("<img=10> <col=008FB2>The Well of Goodwill needs another " + Misc.insertCommasToNumber("" + WellOfGoodwill.getMissingAmount()) + " coins before becoming full.");
                }
                break;
            case 26745:
                KillsTracker.open(player);
                break;
            case -26345:
                DropLog.open(player);
                break;

            case -30281:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 26744:
                DropLog.open(player);
                break;


            case -10531:
                if (player.isKillsTrackerOpen()) {
                    player.setKillsTrackerOpen(false);
                    player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
                }
                break;

            case 28177:
                if (!TeleportHandler.checkReqs(player, null)) {
                    return;
                }
                if (!player.getClickDelay().elapsed(4500) || player.getMovementQueue()
                        .isLockMovement()) {
                    return;
                }
                if (player.getLocation() == Location.CONSTRUCTION) {
                    return;
                }
                Construction.newHouse(player);
                Construction.enterHouse(player, player, true, true);
                break;
            case -30282:
                KillsTracker.openBoss(player);
                break;
            case -10283:
                KillsTracker.open(player);
                break;

            case 11014:
                player.performGraphic(new Graphic(1034));
                player.moveTo(new Position(3261, 2870), true);
                break;
            case -26333:
                player.getPacketSender().sendString(1, "http://www.SolaraScape.org");
                player.getPacketSender().sendMessage("Attempting to open: SolaraScape.org");
                break;


            case 28004:
                player.getPacketSender().sendString(1, "https://solara.gamepayments.net/");
                player.getPacketSender().sendMessage("<shad=0>@blu@Attempting to open: Solara Store");
                break;
            case 28000:
                player.getPacketSender().sendString(1, "https://solara.everythingrs.com/services/vote");
                player.getPacketSender().sendMessage("<shad=0>@blu@Attempting to open: Solara Voting Site");
                break;
            case -26332:
                player.getPacketSender().sendString(1, "http://www.SolaraScape.org");
                player.getPacketSender().sendMessage("Attempting to open: Solararsps.com/rules");
                break;
            case -26331:
                player.getPacketSender()
                        .sendString(1, "https://Solarascape.gamepayments.net/category/238");
                player.getPacketSender().sendMessage("Attempting to open: Solararsps.com/store");
                break;
            case -26330:
                player.getPacketSender()
                        .sendString(1, "https://Solarascape.everythingrs.com/services/vote");
                player.getPacketSender().sendMessage("Attempting to open: Solararsps.com/vote");
                break;
            case -26329:
                player.getPacketSender().sendString(1, "http://www.SolaraScape.org");
                player.getPacketSender().sendMessage("Attempting to open: Solararsps.com/hiscores");
                break;
            case -26328:
                player.getPacketSender().sendString(1, "http://www.SolaraScape.org");
                player.getPacketSender().sendMessage("Attempting to open: Solararsps.com/report");
                break;
            case 26750:
                RecipeForDisaster.openQuestLog(player);
                break;
            case 26751:
                Nomad.openQuestLog(player);
                break;
            case 26611:
                ProfileViewing.view(player, player);
                break;
            case 350:
                player.getPacketSender()
                        .sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
                        .sendTab(GameSettings.MAGIC_TAB)
                        .sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
                break;
            case 12162:
                DialogueManager.start(player, 61);
                player.setDialogueActionId(28);
                break;
            case 29335:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                DialogueManager.start(player, 60);
                player.setDialogueActionId(27);
                break;
            case 29455:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                ClanChatManager.toggleLootShare(player);
                break;
            case 8658:
                DialogueManager.start(player, 55);
                player.setDialogueActionId(26);
                break;
            case 11001:
                player.performAnimation(new Animation(866));
                player.performGraphic(new Graphic(2009));
                player.moveTo(GameSettings.NEW_HOME.copy());
                break;
            case 8667:
                TeleportHandler.teleportPlayer(player, new Position(2742, 3443), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8672:
                TeleportHandler.teleportPlayer(player, new Position(2595, 4772), player.getSpellbook()
                        .getTeleportType());
                player.getPacketSender()
                        .sendMessage("<img=10> To get started with Runecrafting, buy a talisman and use the locate option on it.");
                break;
            case 8861:
                TeleportHandler.teleportPlayer(player, new Position(3273, 2890), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8656:
                player.setDialogueActionId(47);
                DialogueManager.start(player, 86);
                break;
            case 8659:
                TeleportHandler.teleportPlayer(player, new Position(2035, 4643), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8664:
                TeleportHandler.teleportPlayer(player, new Position(2035, 4643), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8666:
                TeleportHandler.teleportPlayer(player, new Position(3037, 2780), player.getSpellbook()
                        .getTeleportType());
                break;

            /*
             * Teleporting Called Below
             */

            case -4914:
            case -4911:
            case -4908:
            case -4905:
            case -4902:
            case -4899:
            case -4896:
            case -4893:
            case -4890:
            case -4845:
            case -4839:
            case -4842:
                Teleporting.teleport(player, id);
                break;


            case 1716:
                Teleporting.openTab(player, -4934);
                break;
            case -4934:
                Teleporting.openTab(player, -4934);
                break;
            case -4931:
                Teleporting.openTab(player, -4931);
                break;
            case -4928:
                Teleporting.openTab(player, -4928);
                break;
            case -4925:
                Teleporting.openTab(player, -4925);
                break;
            case -4922:
                Teleporting.openTab(player, -4922);
                break;
            case -4919:
                Teleporting.openTab(player, -4919);
                break;

            /*
             * End Teleporting
             */

            case 8671:
                player.setDialogueActionId(56);
                DialogueManager.start(player, 89);
                break;
            case 8670:
                TeleportHandler.teleportPlayer(player, new Position(2717, 3499), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8668:
                TeleportHandler.teleportPlayer(player, new Position(2709, 3437), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8665:
                TeleportHandler.teleportPlayer(player, new Position(3253, 2866), player.getSpellbook()
                        .getTeleportType());
                break;
            case 8662:
                TeleportHandler.teleportPlayer(player, new Position(2345, 3698), player.getSpellbook()
                        .getTeleportType());
                break;
            case 13928:
                TeleportHandler.teleportPlayer(player, new Position(3052, 3304), player.getSpellbook()
                        .getTeleportType());
                break;
            case 28179:
                TeleportHandler.teleportPlayer(player, new Position(2022, 4640), player.getSpellbook()
                        .getTeleportType());
                break;
            case 28178:
                DialogueManager.start(player, 54);
                player.setDialogueActionId(25);
                break;
            case 1159: //Bones to Bananas
            case 15877://Bones to peaches
            case 30306:
                MagicSpells.handleMagicSpells(player, id);
                break;
            case 10001:
                if (player.getInterfaceId() == -1) {
                    Consumables.handleHealAction(player);
                } else {
                    player.getPacketSender().sendMessage("You cannot heal yourself right now.");
                }
                break;
            case 18025:
                if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
                }
                break;
            case 18018:
                if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
                }
                break;
            case 10000:
            case 950:
                if (player.getInterfaceId() < 0)
                    player.getPacketSender().sendInterface(40030);
                else
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before doing this.");
                break;
            case 3546:
            case 3420:
                if (System.currentTimeMillis() - player.getTrading().lastAction <= 300)
                    return;
                player.getTrading().lastAction = System.currentTimeMillis();
                if (player.getTrading().inTrade()) {
                    player.getTrading().acceptTrade(id == 3546 ? 2 : 1);
                } else if (player.getDicing().inDice) {
                    player.getDicing().acceptDice(id == 3546 ? 2 : 1);
                } else {
                    player.getPacketSender().sendInterfaceRemoval();
                }
                break;
            case 10162:
            case -18269:
            case 11729:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 841:
                IngridientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
                break;
            case 839:
                IngridientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
                break;
            case 14922:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                break;
            case 14921:
                player.getPacketSender()
                        .sendMessage("Please visit the forums and ask for help in the support section.");
                break;
            case 5294:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
                DialogueManager.start(player, DialogueManager.getDialogues()
                        .get(player.getBankPinAttributes().hasBankPin() ? 12 : 9));
                break;
            case 27653:
                if (!player.busy() && !player.getCombatBuilder()
                        .isBeingAttacked() && !Dungeoneering.doingDungeoneering(player)) {
                    player.getSkillManager().stopSkilling();
                    player.getPriceChecker().open();
                } else {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                }
                break;
            case 2735:
            case 1511:
                if (player.getSummoning().getBeastOfBurden() != null) {
                    player.getSummoning().toInventory();
                    player.getPacketSender().sendInterfaceRemoval();
                } else {
                    player.getPacketSender()
                            .sendMessage("You do not have a familiar who can hold items.");
                }
                break;
            case -11501:
            case -11504:
            case -11498:
            case -11507:
            case 1020:
            case 1021:
            case 1019:
            case 1018:
                if (id == 1020 || id == -11504)
                    SummoningTab.renewFamiliar(player);
                else if (id == 1019 || id == -11501)
                    if (player.getLocation() == Location.BANK) {
                        player.getPacketSender().sendMessage("You can't do that in a bank");
                    } else {
                        SummoningTab.callFollower(player);

                    }

                else if (id == 1021 || id == -11498)
                    SummoningTab.handleDismiss(player, false);
                else if (id == -11507)
                    player.getSummoning().store();
                else if (id == 1018)
                    player.getSummoning().toInventory();
                break;
            case 11004:
                player.getPacketSender().sendTab(GameSettings.SKILLS_TAB);
                DialogueManager.sendStatement(player, "Simply press on the skill you want to train in the skills tab.");
                player.setDialogueActionId(-1);
                break;
		/*case 8654:
		case 8657:
		case 8655:
		case 8663:
		case 8669:
		case 8660:
		case 11008:
			Teleporting.openTab(player, -4934);	
			break;*
		case 11017:
			Teleporting.openTab(player, -4931);	
			break;
		case 11011:
			Teleporting.openTab(player, -4919);	
			break;
		case 11020:
			Teleporting.openTab(player, -4922);	
			break;*/
            case 8654:
            case 8657:
            case 8655:
            case 8663:
            case 8669:
            case 8660:
            case 11008:
                TeleportHandlerNew.openInterface(player, 1);
                break;
            case 11017:
                RewardsHandler.open(player);
                player.getPacketSender().sendInterface(17550);
                break;
            case 11011:
                TeleportHandlerNew.openInterface(player, 2);
                break;
            case 11020:
                TeleportHandlerNew.openInterface(player, 4);
                break;
            case 26716:
                if (player.musicActive() == true) {
                    player.setMusicActive(false);
                    player.getPacketSender()
                            .sendString(26716, "@or2@Music:  @gre@" + (player.musicActive() ? "On" : "Off") + "");
                } else {
                    player.setMusicActive(true);
                    player.getPacketSender()
                            .sendString(26716, "@or2@Music:  @gre@" + (player.musicActive() ? "On" : "Off") + "");
                }

                break;

            case 26717:
                if (player.soundsActive() == true) {
                    player.setSoundsActive(false);
                    player.getPacketSender()
                            .sendString(26717, "@or2@Sounds:  @gre@" + (player.soundsActive() ? "On" : "Off") + "");
                } else {
                    player.setSoundsActive(true);
                    player.getPacketSender()
                            .sendString(26717, "@or2@Sounds:  @gre@" + (player.soundsActive() ? "On" : "Off") + "");
                    player.getPacketSender()
                            .sendString(26717, "@or2@Sounds:  @gre@" + (player.soundsActive() ? "On" : "Off") + "");
                }
                break;
            case 2799:
            case 2798:
            case 1747:
            case 1748:
            case 8890:
            case 8886:
            case 8875:
            case 8871:
            case 8894:
                ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
                break;
            case 14873:
            case 14874:
            case 14875:
            case 14876:
            case 14877:
            case 14878:
            case 14879:
            case 14880:
            case 14881:
            case 14882:
                BankPin.clickedButton(player, id);
                break;
            case 27005:
            case 22012:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
                break;
            case 27023:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                if (player.getSummoning().getBeastOfBurden() == null) {
                    player.getPacketSender()
                            .sendMessage("You do not have a familiar which can hold items.");
                    return;
                }
                Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
                break;
            case 22008:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setNoteWithdrawal(!player.withdrawAsNote());
                break;
            case 21000:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setSwapMode(false);
                player.getPacketSender()
                        .sendConfig(304, 0)
                        .sendMessage("This feature is coming soon!");
                //player.setSwapMode(!player.swapMode());
                break;
            case 27009:
                MoneyPouch.toBank(player);
                break;
            case 27014:
            case 27015:
            case 27016:
            case 27017:
            case 27018:
            case 27019:
            case 27020:
            case 27021:
            case 27022:
                if (!player.isBanking())
                    return;
                if (player.getBankSearchingAttribtues().isSearchingBank())
                    BankSearchAttributes.stopSearch(player, true);
                int bankId = id - 27014;
                boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
                if (!empty || bankId == 0) {
                    player.setCurrentBankTab(bankId);
                    player.getPacketSender().sendString(5385, "scrollreset");
                    player.getPacketSender()
                            .sendString(27002, Integer.toString(player.getCurrentBankTab()));
                    player.getPacketSender().sendString(27000, "1");
                    player.getBank(bankId).open(player, false);
                } else
                    player.getPacketSender()
                            .sendMessage("To create a new tab, please drag an item here.");
                break;
            case 22004:
                if (!player.isBanking())
                    return;
                if (!player.getBankSearchingAttribtues().isSearchingBank()) {
                    player.getBankSearchingAttribtues().setSearchingBank(true);
                    player.setInputHandling(new EnterSyntaxToBankSearchFor());
                    player.getPacketSender()
                            .sendEnterInputPrompt("What would you like to search for?");
                } else {
                    BankSearchAttributes.stopSearch(player, true);
                }
                break;
            case 22845:
            case 24115:
            case 24010:
            case 24041:
            case 150:
                player.setAutoRetaliate(!player.isAutoRetaliate());
                break;
            case 29332:
                ClanChat clan = player.getCurrentClanChat();
                if (clan == null) {
                    player.getPacketSender().sendMessage("You are not in a clanchat channel.");
                    return;
                }
                ClanChatManager.leave(player, false);
                player.setClanChatName(null);
                break;
            case 29329:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.setInputHandling(new EnterClanChatToJoin());
                player.getPacketSender()
                        .sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
                break;

            case 19158:
            case 152:
                if (player.getRunEnergy() <= 1) {
                    player.getPacketSender()
                            .sendMessage("You do not have enough energy to do this.");
                    player.setRunning(false);
                } else
                    player.setRunning(!player.isRunning());
                player.getPacketSender().sendRunStatus();
                break;

            case -282:
                DropLog.openRare(player);
                break;

            case 27658:
                player.setExperienceLocked(!player.experienceLocked());
                String type = player.experienceLocked() ? "locked" : "unlocked";
                player.getPacketSender().sendMessage("Your experience is now " + type + ".");
                break;
            case 27651:
            case 21341:
                if (player.getInterfaceId() == -1) {
                    player.getSkillManager().stopSkilling();
                    BonusManager.update(player);
                    player.getPacketSender().sendInterface(21172);
                } else
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before doing this.");
                break;
            case 27654:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.getSkillManager().stopSkilling();
                ItemsKeptOnDeath.sendInterface(player);
                break;
            case 2458: //Logout
                if (player.logout()) {
                    World.getPlayers().remove(player);
                }
                break;


            case -19725:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 1);
                break;

            case -19722:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 2);
                break;

            case -19719:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 3);
                break;
            case -19716:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 4);
                break;
            case -19713:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 5);
                break;


            case -19710:
                if (player.teleports != null) {
                    if (player.teleports.size() > 0) {
                        player.teleports.clear();
                    }
                }
                TeleportHandlerNew.openInterface(player, 6);
                break;


            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
            case 7473:
            case 7562:
            case 7487:
            case 7788:
            case 8481:
            case 7612:
            case 7587:
            case 7662:
            case 7462:
            case 7548:
            case 7687:
            case 7537:
            case 12322:
            case 7637:
            case 12311:
            case -24530:
            case 10298:
                CombatSpecial.activate(player);
                break;
            case -20152:
                for (int j = 0; j < player.getInventory().getItems().length; j++) {
                    player.getTrading().tradeItem(player.getInventory().get(j).getId(),
                            player.getInventory().get(j).getAmount(), j);
                }
                break;
            case 1772: // shortbow & longbow
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_ACCURATE);
                }
                break;
            case 1771:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_RAPID);
                }
                break;
            case 1770:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_LONGRANGE);
                }
                break;
            case 2282: // dagger & sword
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_STAB);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_STAB);
                }
                break;
            case 2285:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_LUNGE);
                }
                break;
            case 2284:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_SLASH);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_SLASH);
                }
                break;
            case 2283:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_BLOCK);
                }
                break;
            case 2429: // scimitar & longsword
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_CHOP);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_CHOP);
                }
                break;
            case 2432:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_SLASH);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_SLASH);
                }
                break;
            case 2431:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_LUNGE);
                }
                break;
            case 2430:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_BLOCK);
                }
                break;
            case 3802: // mace
                player.setFightType(FightType.MACE_POUND);
                break;
            case 3805:
                player.setFightType(FightType.MACE_PUMMEL);
                break;
            case 3804:
                player.setFightType(FightType.MACE_SPIKE);
                break;
            case 3803:
                player.setFightType(FightType.MACE_BLOCK);
                break;
            case 4454: // knife, thrownaxe, dart & javelin
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_ACCURATE);
                }
                break;
            case 4453:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_RAPID);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_RAPID);
                }
                break;
            case 4452:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_LONGRANGE);
                }
                break;
            case 4685: // spear
                player.setFightType(FightType.SPEAR_LUNGE);
                break;
            case 4688:
                player.setFightType(FightType.SPEAR_SWIPE);
                break;
            case 4687:
                player.setFightType(FightType.SPEAR_POUND);
                break;
            case 4686:
                player.setFightType(FightType.SPEAR_BLOCK);
                break;
            case 4711: // 2h sword
                player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
                break;
            case 4714:
                player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
                break;
            case 4713:
                player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
                break;
            case 4712:
                player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
                break;
            case 5576: // pickaxe
                player.setFightType(FightType.PICKAXE_SPIKE);
                break;
            case 5579:
                player.setFightType(FightType.PICKAXE_IMPALE);
                break;
            case 5578:
                player.setFightType(FightType.PICKAXE_SMASH);
                break;
            case 5577:
                player.setFightType(FightType.PICKAXE_BLOCK);
                break;
            case 7768: // claws
                player.setFightType(FightType.CLAWS_CHOP);
                break;
            case 7771:
                player.setFightType(FightType.CLAWS_SLASH);
                break;
            case 7770:
                player.setFightType(FightType.CLAWS_LUNGE);
                break;
            case 7769:
                player.setFightType(FightType.CLAWS_BLOCK);
                break;
            case 8466: // halberd
                player.setFightType(FightType.HALBERD_JAB);
                break;
            case 8468:
                player.setFightType(FightType.HALBERD_SWIPE);
                break;
            case 8467:
                player.setFightType(FightType.HALBERD_FEND);
                break;
            case 5862: // unarmed
                player.setFightType(FightType.UNARMED_PUNCH);
                break;
            case 5861:
                player.setFightType(FightType.UNARMED_KICK);
                break;
            case 5860:
                player.setFightType(FightType.UNARMED_BLOCK);
                break;
            case 12298: // whip
                player.setFightType(FightType.WHIP_FLICK);
                break;
            case 12297:
                player.setFightType(FightType.WHIP_LASH);
                break;
            case 12296:
                player.setFightType(FightType.WHIP_DEFLECT);
                break;
            case 336: // staff
                player.setFightType(FightType.STAFF_BASH);
                break;
            case 335:
                player.setFightType(FightType.STAFF_POUND);
                break;
            case 334:
                player.setFightType(FightType.STAFF_FOCUS);
                break;
            case 433: // warhammer
                player.setFightType(FightType.WARHAMMER_POUND);
                break;
            case 432:
                player.setFightType(FightType.WARHAMMER_PUMMEL);
                break;
            case 431:
                player.setFightType(FightType.WARHAMMER_BLOCK);
                break;
            case 782: // scythe
                player.setFightType(FightType.SCYTHE_REAP);
                break;
            case 784:
                player.setFightType(FightType.SCYTHE_CHOP);
                break;
            case 785:
                player.setFightType(FightType.SCYTHE_JAB);
                break;
            case 783:
                player.setFightType(FightType.SCYTHE_BLOCK);
                break;
            case 1704: // battle axe
                player.setFightType(FightType.BATTLEAXE_CHOP);
                break;
            case 1707:
                player.setFightType(FightType.BATTLEAXE_HACK);
                break;
            case 1706:
                player.setFightType(FightType.BATTLEAXE_SMASH);
                break;
            case 1705:
                player.setFightType(FightType.BATTLEAXE_BLOCK);
                break;
        }

        long end = System.currentTimeMillis();
        long cycle = end - start;
        if (cycle >= 100) {
            System.err.println(cycle + "ms - button - " + id);
        }

    }

    private boolean checkHandlers(Player player, int id) {

        if (player.getCustomTasks().handleTaskClick(id)) {
            return true;
        }

        if (player.getCustomTasks().handleTabClick(id)) {
            return true;
        }

        if (Construction.handleButtonClick(id, player)) {
            return true;
        }

        if (player.getBis().handleButtonClick(id)) {
            return true;
        }

        if (DropTableInterface.getInstance().handleButton(player, id)) {
            return true;
        }

        switch (id) {
            case 2461:
            case 2494:
            case 2495:
            case 2496:
            case 2497:
            case 2498:
            case 2471:
            case 2472:
            case 2473:
            case 2462:
            case 2482:
            case 2483:
            case 2484:
            case 2485:
                DialogueOptions.handle(player, id);
                return true;
        }
        if (player.isPlayerLocked() && id != 2458 && id != -12760 && id != -12767 && id != -12780 && id != -12779 && id != -12778 && id != -29767 && id != -12763) {
            return true;
        }

        if (PrayerHandler.isButton(id)) {
            PrayerHandler.togglePrayerWithActionButton(player, id);
            return true;
        }
        
        if (CurseHandler.isButton(player, id)) {
            return true;
        }
        if (StartScreen.handleButton(player, id)) {
            return true;
        }
        if (Autocasting.handleAutocast(player, id)) {
            return true;
        }
        if (SmithingData.handleButtons(player, id)) {
            return true;
        }
        if (PouchMaking.pouchInterface(player, id)) {
            return true;
        }
        if (LoyaltyProgramme.handleButton(player, id)) {
            return true;
        }
        if (Fletching.fletchingButton(player, id)) {
            return true;
        }
        if (player.getRaidsOne().getRaidsConnector().isButton(id)) {
            return true;
        }
        if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
            return true;
        }
        if (Emotes.doEmote(player, id)) {
            return true;
        }
        if (PestControl.handleInterface(player, id)) {
            return true;
        }
        if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
            return true;
        }
        if (Slayer.handleRewardsInterface(player, id)) {
            return true;
        }
        if (ExperienceLamps.handleButton(player, id)) {
            return true;
        }
        if (PlayersOnlineInterface.handleButton(player, id)) {
            return true;
        }
        if (GrandExchange.handleButton(player, id)) {
            return true;
        }
        if (GoodieBagManager.AsaButton(player, id)) {
            return true;
        }
        return false;
    }

    public static final int OPCODE = 185;
}
