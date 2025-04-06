package com.ruseps.world.entity.impl.player;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import com.google.common.collect.Multiset.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ruseps.GameServer;
import com.ruseps.util.Misc;
import com.ruseps.world.content.ClueScrolls;
import com.ruseps.world.content.skill.impl.construction.ConstructionSave;


public class PlayerSaving {

	public static void save(Player player) {
		if (player.newPlayer())
			return;
		// Create the path and file objects.
		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		}
		try (FileWriter writer = new FileWriter(file)) {

			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.addProperty("total-play-time-ms", player.getTotalPlayTime());
			object.addProperty("username", player.getUsername().trim());
			object.addProperty("password", player.getPassword().trim());
			object.addProperty("email", player.getEmailAddress() == null ? "null" : player.getEmailAddress().trim());
			object.addProperty("staff-rights", player.getRights().name());
			object.addProperty("game-mode", player.getGameMode().name());
			object.addProperty("boss-points", player.getBossPoints());
			object.addProperty("dr-boost", player.getDrBoost());
			object.addProperty("loyalty-title", player.getLoyaltyTitle().name());
			object.add("position", builder.toJsonTree(player.getPosition()));
			object.addProperty("online-status", player.getRelations().getStatus().name());
			object.addProperty("given-starter", new Boolean(player.didReceiveStarter()));
			object.addProperty("is-instanced", new Boolean(player.isPlayerInstanced()));
			object.addProperty("money-pouch", new Long(player.getMoneyInPouch()));
			object.addProperty("donated", new Long(player.getAmountDonated()));
			object.addProperty("password-reset", player.getPasswordPlayer());
			object.addProperty("minutes-bonus-exp", new Integer(player.getMinutesBonusExp()));
			object.addProperty("total-gained-exp", new Long(player.getSkillManager().getTotalGainedExp()));
			object.addProperty("last-teleport-time", new Long(player.getLastTeleportTime()));
			object.addProperty("prestige-points", new Integer(player.getPointsHandler().getPrestigePoints()));
			object.addProperty("achievement-points", new Integer(player.getPointsHandler().getAchievementPoints()));
			object.add("achievements", player.getAchievementTracker().jsonSave());
			object.addProperty("dung-tokens", new Integer(player.getPointsHandler().getDungeoneeringTokens()));
			object.addProperty("commendations", new Integer(player.getPointsHandler().getCommendations()));
			object.addProperty("loyalty-points", new Integer(player.getPointsHandler().getLoyaltyPoints()));
			object.addProperty("raids-one-points", new Integer(player.getPointsHandler().getRaidsOnePoints()));
			object.addProperty("raids-two-points", new Integer(player.getPointsHandler().getRaidsTwoPoints()));
			object.addProperty("current-starter", new Integer(player.getCurrentStarterProgression()));
			object.addProperty("voting-points", new Integer(player.getPointsHandler().getVotingPoints()));
			object.addProperty("slayer-points", new Integer(player.getPointsHandler().getSlayerPoints()));
			object.addProperty("hide-player", new Boolean(player.isHidePlayer()));
			object.add("npc-kc-data", builder.toJsonTree(player.getNpcKillCount()));
			object.addProperty("pk-points", new Integer(player.getPointsHandler().getPkPoints()));
			object.addProperty("BattleRoyal-points", new Integer(player.getPointsHandler().getBattleRoyalPoints()));
			object.addProperty("donation-points", new Integer(player.getPointsHandler().getDonationPoints()));
			object.addProperty("trivia-points", new Integer(player.getPointsHandler().getTriviaPoints()));
			object.addProperty("cluescompleted", new Integer(ClueScrolls.getCluesCompleted()));
			object.addProperty("referral", new Boolean(player.hasReferral));
			object.addProperty("player-kills", new Integer(player.getPlayerKillingAttributes().getPlayerKills()));
			object.addProperty("player-killstreak",
					new Integer(player.getPlayerKillingAttributes().getPlayerKillStreak()));
			object.addProperty("player-deaths", new Integer(player.getPlayerKillingAttributes().getPlayerDeaths()));
			object.addProperty("target-percentage",
					new Integer(player.getPlayerKillingAttributes().getTargetPercentage()));
			object.addProperty("bh-rank", new Integer(player.getAppearance().getBountyHunterSkull()));
			object.addProperty("gender", player.getAppearance().getGender().name());
			object.addProperty("spell-book", player.getSpellbook().name());
			object.addProperty("shop-updated", new Boolean(player.isShopUpdated()));
			object.addProperty("shop-earnings", new Long(player.getPlayerOwnedShopManager().getEarnings()));
			object.addProperty("prayer-book", player.getPrayerbook().name());
			object.addProperty("running", new Boolean(player.isRunning()));
			object.addProperty("run-energy", new Integer(player.getRunEnergy()));
			object.addProperty("music", new Boolean(player.musicActive()));
			object.addProperty("sounds", new Boolean(player.soundsActive()));
			object.addProperty("auto-retaliate", new Boolean(player.isAutoRetaliate()));
			object.addProperty("xp-locked", new Boolean(player.experienceLocked()));
			object.addProperty("veng-cast", new Boolean(player.hasVengeance()));
			object.addProperty("last-veng", new Long(player.getLastVengeance().elapsed()));
			object.addProperty("fight-type", player.getFightType().name());
			object.addProperty("sol-effect", new Integer(player.getStaffOfLightEffect()));
			object.addProperty("skull-timer", new Integer(player.getSkullTimer()));
			object.addProperty("accept-aid", new Boolean(player.isAcceptAid()));
			object.addProperty("venom-damage", new Integer(player.getVenomDamage()));
			object.addProperty("venom-immunity", new Integer(player.getVenomImmunity()));
			object.addProperty("poison-damage", new Integer(player.getPoisonDamage()));
			object.addProperty("poison-immunity", new Integer(player.getPoisonImmunity()));
			object.addProperty("overload-timer", new Integer(player.getOverloadPotionTimer()));
			object.addProperty("fire-immunity", new Integer(player.getFireImmunity()));
			object.addProperty("fire-damage-mod", new Integer(player.getFireDamageModifier()));
			object.addProperty("prayer-renewal-timer", new Integer(player.getPrayerRenewalPotionTimer()));
			object.addProperty("teleblock-timer", new Integer(player.getTeleblockTimer()));
			object.addProperty("special-amount", new Integer(player.getSpecialPercentage()));
			object.addProperty("entered-gwd-room",
					new Boolean(player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()));
			object.addProperty("gwd-altar-delay",
					new Long(player.getMinigameAttributes().getGodwarsDungeonAttributes().getAltarDelay()));
			object.add("gwd-killcount",
					builder.toJsonTree(player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()));
			object.addProperty("effigy", new Integer(player.getEffigy()));
			object.addProperty("summon-npc", new Integer(player.getSummoning().getFamiliar() != null
					? player.getSummoning().getFamiliar().getSummonNpc().getId() : -1));
			object.addProperty("summon-death", new Integer(player.getSummoning().getFamiliar() != null
					? player.getSummoning().getFamiliar().getDeathTimer() : -1));
			object.addProperty("process-farming", new Boolean(player.shouldProcessFarming()));
			object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
			object.addProperty("autocast", new Boolean(player.isAutocast()));
			object.addProperty("autocast-spell",
					player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);
			object.addProperty("dfs-charges", player.getDfsCharges());
			object.addProperty("slayer-master", player.getSlayer().getSlayerMaster().name());
			object.addProperty("slayer-task", player.getSlayer().getSlayerTask().name());
			object.addProperty("prev-slayer-task", player.getSlayer().getLastTask().name());
			object.addProperty("task-amount", player.getSlayer().getAmountToSlay());
			object.addProperty("task-streak", player.getSlayer().getTaskStreak());
			object.addProperty("duo-partner",
					player.getSlayer().getDuoPartner() == null ? "null" : player.getSlayer().getDuoPartner());
			object.addProperty("double-slay-xp", player.getSlayer().doubleSlayerXP);
			object.addProperty("recoil-deg", new Integer(player.getRecoilCharges()));
			object.add("brawler-deg", builder.toJsonTree(player.getBrawlerChargers()));
			object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers()));
			object.add("barrows-brother",
					builder.toJsonTree(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()));
			object.addProperty("random-coffin",
					new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()));
			object.addProperty("barrows-killcount",
					new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount()));
			object.add("nomad",
					builder.toJsonTree(player.getMinigameAttributes().getNomadAttributes().getQuestParts()));
			object.add("valentines",
					builder.toJsonTree(player.getMinigameAttributes().getValentinesAttributes().getQuestParts()));
			object.add("recipe-for-disaster", builder
					.toJsonTree(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()));
			object.addProperty("recipe-for-disaster-wave",
					new Integer(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted()));
			object.add("dung-items-bound",
					builder.toJsonTree(player.getMinigameAttributes().getDungeoneeringAttributes().getBoundItems()));
			object.addProperty("rune-ess", new Integer(player.getStoredRuneEssence()));
			object.addProperty("pure-ess", new Integer(player.getStoredPureEssence()));
			object.addProperty("has-bank-pin", new Boolean(player.getBankPinAttributes().hasBankPin()));
			object.addProperty("last-pin-attempt", new Long(player.getBankPinAttributes().getLastAttempt()));
			object.addProperty("invalid-pin-attempts", new Integer(player.getBankPinAttributes().getInvalidAttempts()));
			object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin()));
			object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
			object.add("agility-obj", builder.toJsonTree(player.getCrossedObstacles()));
			object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
			object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
			object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
			object.add("bank-0", builder.toJsonTree(player.getBank(0).getValidItems()));
			object.add("bank-1", builder.toJsonTree(player.getBank(1).getValidItems()));
			object.add("bank-2", builder.toJsonTree(player.getBank(2).getValidItems()));
			object.add("bank-3", builder.toJsonTree(player.getBank(3).getValidItems()));
			object.add("bank-4", builder.toJsonTree(player.getBank(4).getValidItems()));
			object.add("bank-5", builder.toJsonTree(player.getBank(5).getValidItems()));
			object.add("bank-6", builder.toJsonTree(player.getBank(6).getValidItems()));
			object.add("bank-7", builder.toJsonTree(player.getBank(7).getValidItems()));
			object.add("bank-8", builder.toJsonTree(player.getBank(8).getValidItems()));
			object.add("ge-slots", builder.toJsonTree(player.getGrandExchangeSlots()));
			object.add("starterprogressioncompletions", builder.toJsonTree(player.getStarterProgressionCompletions()));
			/** STORE SUMMON **/
			if (player.getSummoning().getBeastOfBurden() != null) {
				object.add("store", builder.toJsonTree(player.getSummoning().getBeastOfBurden().getValidItems()));
			}
			object.add("charm-imp", builder.toJsonTree(player.getSummoning().getCharmImpConfigs()));

			for (Entry<Integer> dartItem : player.getBlowpipeLoading().getContents().entrySet()) {
				object.addProperty("blowpipe-charge-item", new Integer(dartItem.getElement()));
				object.addProperty("blowpipe-charge-amount",
						new Integer(player.getBlowpipeLoading().getContents().count(dartItem.getElement())));
			}

			object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
			object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));
			object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles()));
			object.add("kills", builder.toJsonTree(player.getKillsTracker().toArray()));
			object.add("drops", builder.toJsonTree(player.getDropLog().toArray()));
			object.add("max-cape-colors", builder.toJsonTree(player.getMaxCapeColors()));
			object.add("comp-cape-colors", builder.toJsonTree(player.getCompCapeColors()));
			object.addProperty("player-title", new String(player.getTitle()));
			object.addProperty("ovl", player.getTimeLeft());
			object.addProperty("dmg", player.getDmgTimeLeft());
			object.add("cosmetic-equipment", builder.toJsonTree(player.getCosmeticEquipment()));
			//object.add("collected-items", builder.toJsonTree(player.getCollectedItems()));
			object.add("perks", builder.toJsonTree(player.getPerks()));
			object.add("npctaskcompletions", builder.toJsonTree(player.getNpcTaskCompletions()));
			object.add("skillingtaskcompletions", builder.toJsonTree(player.getSkillingTaskCompletions()));
			object.add("minigametaskcompletions", builder.toJsonTree(player.getMinigameTaskCompletions()));
			object.add("othertaskcompletions",builder.toJsonTree(player.getOtherTaskCompletions()));
			object.add("skillingdata", builder.toJsonTree(player.getSkillingData()));
			object.add("miscamountmap", builder.toJsonTree(player.getMiscAmountMap()));
			object.add("miscstatemap", builder.toJsonTree(player.getMiscStateMap()));
			object.addProperty("group-owner-name", player.getGroupOwnerName());
            object.addProperty("is-gim",player.isGim());
            object.addProperty("reset-season-one", player.getBattlePass().isResetSeasonOne());
			object.addProperty("battlePassLevel", player.getBattlePass().getLevel());//battlePassExp
			object.addProperty("battlePassExp", player.getBattlePass().getExperience());//battlePassExp
			object.addProperty("battlePassType", player.getBattlePass().getType().name());//battlePassExp
            object.addProperty("BronzeBattlepassClaimed", player.bronzeBattlepassClaimed);//bronze pass claimed date
			object.addProperty("BronzeBattlepassExpires", player.bronzeBattlepassExpires);//bronze pass expires date
			object.addProperty("GoldBattlepassClaimed", player.goldBattlepassClaimed);//gold pass claimed date
			object.addProperty("GoldBattlepassExpires", player.goldBattlepassExpires);// gold pass expired date
			
			object.addProperty("GoldBattlepassClaimed", player.goldBattlepassClaimed);//gold pass claimed date
			object.addProperty("GoldBattlepassExpires", player.goldBattlepassExpires);// gold pass expired date
			
			object.addProperty("AnimeRaidCompletion", player.getAnimeRaidsOpened());// Anime Raids done
			object.addProperty("MKRaidCompletion", player.getMkRaidsOpened());// MK Raids done
			object.addProperty("RubyRaidCompletion", player.getRubyRaidsOpened());// Ruby Raids done
			object.addProperty("GoldRaidCompletion", player.getGoldRaidsOpened());// Gold Raids done
			object.addProperty("SilverRaidCompletion", player.getSilverRaidsOpened());// silver Raids done
			object.addProperty("PlatRaidCompletion", player.getPlatRaidsOpened());// Plat Raids done
			object.addProperty("DiamondRaidCompletion", player.getDiamondRaidsOpened());// Diamond Raids done
			object.addProperty("PokemonRaidCompletion", player.getPokemonRaidsOpened());// Pokemon Raids done
			object.addProperty("darkDementionRaidCompletion", player.getDarkDementionRaidsOpened());// Pokemon Raids done dsRaidCompletion
			object.addProperty("dsRaidCompletion", player.getDragonstoneRaidsOpened());
			
			object.add("collection-log", builder.toJsonTree(player.getCollectionLogManager().getLogProgress()));
			object.add("claimed-logs", builder.toJsonTree(player.getCollectionLogManager().getClaimedCollectionRewards()));

			object.add("daily-task-slots", builder.toJsonTree(player.getDailyTaskManager().getTaskSlots()));
			object.addProperty("daily-tasks-completed", player.getDailyTasksCompleted());
			object.addProperty("daily-date", player.getDailyTaskManager().getDailyDate());

			
			writer.write(builder.toJson(object));
			writer.close();
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a character file!", e);
		}
			try {
			
			File file2 = new File("./data/saves/construction/"+player.getUsername() + ".obj");
			
			if(!file2.exists()) {
				file2.createNewFile();
			}
			
			FileOutputStream fileOut = new FileOutputStream(file2);
			ConstructionSave save = new ConstructionSave();
			save.supply(player);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(save);
			out.close();
			fileOut.close();
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
		//PlayerOwnedShops.saveShop(player);
	}

	public static boolean playerExists(String p) {
		p = Misc.formatPlayerName(p.toLowerCase());
		return new File("./data/saves/characters/" + p + ".json").exists();
	}
}
