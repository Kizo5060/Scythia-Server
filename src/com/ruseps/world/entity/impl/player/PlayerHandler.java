package com.ruseps.world.entity.impl.player;


import com.ruseps.world.content.skill.impl.summoning.BossPets;
import com.ruseps.GameServer;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.BonusExperienceTask;
import com.ruseps.engine.task.impl.CombatSkullEffect;
import com.ruseps.engine.task.impl.FireImmunityTask;
import com.ruseps.engine.task.impl.OverloadPotionTask;
import com.ruseps.engine.task.impl.PlayerSkillsTask;
import com.ruseps.engine.task.impl.PlayerSpecialAmountTask;
import com.ruseps.engine.task.impl.PrayerRenewalPotionTask;
import com.ruseps.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.ruseps.model.Flag;
import com.ruseps.model.Locations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.net.PlayerSession;
import com.ruseps.net.SessionState;
import com.ruseps.net.security.ConnectionHandler;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.Lottery;
import com.ruseps.world.content.PlayerLogs;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.content.PlayerPunishments;
import com.ruseps.world.content.PlayersOnlineInterface;
import com.ruseps.world.content.StaffList;
import com.ruseps.world.content.StartScreen;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.combat.effect.CombatPoisonEffect;
import com.ruseps.world.content.combat.effect.CombatTeleblockEffect;
import com.ruseps.world.content.combat.effect.CombatVenomEffect;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.pvp.BountyHunter;
import com.ruseps.world.content.combat.range.DwarfMultiCannon;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.dropchecker.NPCDropTableChecker;
import com.ruseps.world.content.grandexchange.GrandExchange;
import com.ruseps.world.content.groupironman.GroupIronmanGroup;
import com.ruseps.world.content.minigames.impl.Barrows;
import com.ruseps.world.content.skill.impl.hunter.Hunter;
import com.ruseps.world.content.skill.impl.slayer.Slayer;

public class PlayerHandler 
{
    public static Player getPlayerForName(String name) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (player.getUsername().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    public static void handleLogin(Player player) {

        System.out.println("[World] Registering player - [username, host] : [" + player.getUsername() + ", " + player
                .getHostAddress() + "]");
        player.getPlayerOwnedShopManager().hookShop();
        player.setActive(true);
        ConnectionHandler.add(player.getHostAddress());
        World.getPlayers().add(player);
        World.updatePlayersOnline();
        PlayersOnlineInterface.add(player);
        player.getCollectionLog().loadCollectionLogs(player);
        player.getSession().setState(SessionState.LOGGED_IN);

        player.getPacketSender().sendMapRegion().sendDetails();
        player.getCosmeticGear().update();
        player.getRecordedLogin().reset();
        player.getPacketSender().sendTabs();

        if (GroupIronmanGroup.getGroups().containsKey(player.getUsername())) {
            GroupIronmanGroup gim = GroupIronmanGroup.getGroups().get(player.getUsername());
            gim.setOwner(player);
            player.setGroupIronmanGroup(GroupIronmanGroup.getGroups().get(player.getUsername()));
        } else {
            
        }

        if (GroupIronmanGroup.getGroups().containsKey(player.getGroupOwnerName())) {
            player.setGroupIronmanGroup(GroupIronmanGroup.getGroups().get(player.getGroupOwnerName()));
        }
        
        for (int i = 0; i < player.getBanks().length; i++) {
            if (player.getBank(i) == null) {
                player.setBank(i, new Bank(player));
            }
        }
        player.getInventory().refreshItems();
        player.getEquipment().refreshItems();

        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        CombatSpecial.updateBar(player);
        BonusManager.update(player);
        player.getSummoning().login();
        player.getPlayerOwnedShopManager().hookShop();
        player.getFarming().load();
        Slayer.checkDuoSlayer(player, true);
        
        for (Skill skill : Skill.values()) {
            player.getSkillManager().updateSkill(skill);
        }

        if (player.getSerialNumber().toLowerCase().contains("00-ff-") || player.getSerialNumber()
                .toLowerCase()
                .contains("00-1C-") || player.getSuperSerialNumber().contains("20181227165339")) {
            World.sendStaffMessage("<img=483> [SUS ACCOUNT] " + "@red@" + player.getName() + " has logged in with a suspicous marker.");
            World.sendStaffMessage("<img=483> [SUS ACCOUNT] " + "@red@" + player.getName() + " has logged in with a suspicous marker.");
            World.sendStaffMessage("<img=483> [SUS ACCOUNT] " + "@red@" + player.getName() + " has been auto kicked on suspicion of compromised.");
            //player.setAccountCompromised(true);
            player.getPacketSender().sendLogout();
        }

        if (player.getHostAddress() != player.getLastHostAddress() && GameSettings.HIGH_SECURITY) {
            World.sendStaffMessage("<img=483> [ HIGH SECURITY ] @dre@" + player.getName() + " has logged in with a different ip address than usual.");
        }

        player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true);

        player.getPacketSender()
                .sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
                .sendTotalXp(player.getSkillManager().getTotalGainedExp())
                .sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId())
                .sendRunStatus()
                .sendRunEnergy(player.getRunEnergy())
                .sendString(8135, "" + player.getMoneyInPouch())/*.sendInteractionOption("Dice with", 2, false)*/
                .sendInteractionOption("Follow", 3, false)
                .sendInteractionOption("Trade With", 4, false)
                .sendInterfaceRemoval()
                .sendString(39161, "@or2@Server Time: @or1@" + Misc.getCurrentServerTime());

        Autocasting.onLogin(player);
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        BonusManager.sendCurseBonuses(player);
        Barrows.updateInterface(player);

        TaskManager.submit(new PlayerSkillsTask(player));
        TaskManager.submit(new ZulrahClouds(player));
        if (player.isPoisoned()) {
            TaskManager.submit(new CombatPoisonEffect(player));
        }
        if (player.isVenomed()) {
            TaskManager.submit(new CombatVenomEffect(player));
        }
        if (player.getPrayerRenewalPotionTimer() > 0) {
            TaskManager.submit(new PrayerRenewalPotionTask(player));
        }
        if (player.getOverloadPotionTimer() > 0) {
            TaskManager.submit(new OverloadPotionTask(player));
        }
        if (player.getTeleblockTimer() > 0) {
            TaskManager.submit(new CombatTeleblockEffect(player));
        }
        if (player.getSkullTimer() > 0) {
            player.setSkullIcon(1);
            TaskManager.submit(new CombatSkullEffect(player));
        }
        if (player.getFireImmunity() > 0) {
            FireImmunityTask.makeImmune(player, player.getFireImmunity(), player.getFireDamageModifier());
        }
        if (player.getSpecialPercentage() < 100) {
            TaskManager.submit(new PlayerSpecialAmountTask(player));
        }
        if (player.hasStaffOfLightEffect()) {
            TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
        }
        if (player.getMinutesBonusExp() >= 0) {
            TaskManager.submit(new BonusExperienceTask(player));
        }

        player.getUpdateFlag().flag(Flag.APPEARANCE);

        Lottery.onLogin(player);
        Locations.login(player);

        if (player.didReceiveStarter() == false) {
         
        	
        }
        
        player.getPacketSender()
                .sendMessage("@blu@Welcome to Scythia! @bla@ Visit our website at: www.ScythiaScape.org");


        if (player.experienceLocked()) {
            player.getPacketSender()
                    .sendMessage("@red@Warning: your experience is currently locked.");
        }
        ClanChatManager.handleLogin(player);

        if (GameSettings.BONUS_EXP) {
           
        }
        if (WellOfGoodwill.isActive()) {
            player.getPacketSender()
                    .sendMessage("<img=10> @blu@The Well of Goodwill is granting 30% bonus experience for another " + WellOfGoodwill
                            .getMinutesRemaining() + " minutes.");
        }

        PlayerPanel.refreshPanel(player);
        ClanChatManager.join(player, "help");

        //New player
        if (player.newPlayer()) {
            player.setPasswordPlayer(2);
            StartScreen.open(player);
            player.setHidePlayer(true);
            player.setPlayerLocked(true);
            World.sendMessage("<col=6600CC><img=10>[New Scythia Member]: " + player.getUsername() + " Let's Give Our New Member a Warm Welcoming! <img=10>");


        } else {
            player.setHidePlayer(false);
        }


        if (player.getPasswordPlayer() == 0) {

            player.setPlayerLocked(true);
            PlayerLogs.log(player.getUsername(), "First login since database leak, serial id:" + player
                    .getSerialNumber() + " ");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");
            player.getPacketSender()
                    .sendMessage("@red@YOUR ACCOUNT IS LOCKED UNTIL YOU CHANGE YOUR PASS - ::CHANGEPASSWORD");

        }
        player.getPacketSender()
                .updateSpecialAttackOrb()
                .sendIronmanMode(player.getGameMode().ordinal());

        if (player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR || player
                .getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.MANAGER || player
                .getRights() == PlayerRights.DEVELOPER) {
            if (player.getUsername().equalsIgnoreCase("Aus")) {
                return;
            }
            //World.sendMessage("<img=10><col=6600CC> Owner "+ player.getUsername() + " has just logged in. Only message him if absolutely needed.");
        }
        if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR ||  player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER) {
            if (!StaffList.staff.contains(player.getUsername())) {
                StaffList.login(player);
            }
        }
        
        if (player.getRights() == PlayerRights.DEVELOPER) {
            if (player.getUsername().equalsIgnoreCase("ntsn") 
            		|| player.getUsername().equalsIgnoreCase("Hipster")) {
                player.getPacketSender().sendMessage("@red@Accepted Developer Login.");
            } else {
                PlayerPunishments.ban(player.getUsername());
                World.deregister(player);
            }
        }

        GrandExchange.onLogin(player);
        player.getStarterProgression().open();

        if (player.getPlayerOwnedShopManager().getEarnings() > 0) {
            player.sendMessage("<col=FF0000>You have unclaimed earnings in your player owned shop!");
        }

       if (player.getRights() == PlayerRights.ADMINISTRATOR || player.getRights() == PlayerRights.DEVELOPER) {

        }

        NPCDropTableChecker.getSingleton().refreshDropTableChilds(player);
        player.getOvl().handleLogin();
        player.getDmg().handleLogin();
        PlayerLogs.log(player.getUsername(), "Login from host " + player.getHostAddress() + ", serial number: " + player
                .getSerialNumber());

    }

    public static boolean handleLogout(Player player) {
        try {

            PlayerSession session = player.getSession();

            if (session.getChannel().isOpen()) {
                session.getChannel().close();
            }

            if (!player.isRegistered()) {
                return true;
            }

            boolean exception = GameServer.isUpdating() || World.getLogoutQueue()
                    .contains(player) && player.getLogoutTimer().elapsed(90000);
            if (player.logout() || exception) {
                System.out.println("[World] Deregistering player - [username, host] : [" + player.getUsername() + ", " + player
                        .getHostAddress() + "]");
                player.getSession().setState(SessionState.LOGGING_OUT);
                ConnectionHandler.remove(player.getHostAddress());
                player.setTotalPlayTime(player.getTotalPlayTime() + player.getRecordedLogin()
                        .elapsed());
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getCannon() != null) {
                    DwarfMultiCannon.pickupCannon(player, player.getCannon(), true);
                }
                if (exception && player.getResetPosition() != null) {
                    player.moveTo(player.getResetPosition());
                    player.setResetPosition(null);
                }
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
                
                if (player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR || player
                        .getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.DEVELOPER || player
                        .getRights() == PlayerRights.OWNER) {
                    StaffList.logout(player);
                }
         
                if (player != null) {
                    BossPets.onLogout(player);
                }
                
                Hunter.handleLogout(player);
                Locations.logout(player);
                player.getSummoning().unsummon(false, false);
                player.getFarming().save();
                player.getCollectionLog().saveCollectionLogs(player);
                player.getPlayerOwnedShopManager().unhookShop();
                BountyHunter.handleLogout(player);
                ClanChatManager.leave(player, false);
                player.getRelations().updateLists(false);
                PlayersOnlineInterface.remove(player);
                TaskManager.cancelTasks(player.getCombatBuilder());
                TaskManager.cancelTasks(player);
                player.save();
                World.getPlayers().remove(player);
                session.setState(SessionState.LOGGED_OUT);
                World.updatePlayersOnline();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
