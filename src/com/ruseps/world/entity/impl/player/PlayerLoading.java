package com.ruseps.world.entity.impl.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruseps.engine.task.impl.FamiliarSpawnTask;
import com.ruseps.model.*;
import com.ruseps.util.InterfaceAdapter;
import com.ruseps.world.content.ClueScrolls;
import com.ruseps.model.PlayerRelations.PrivateChatStatus;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.net.login.LoginResponses;

import com.ruseps.world.content.DropLog;
import com.ruseps.world.content.DropLog.DropLogEntry;
import com.ruseps.world.content.KillsTracker;
import com.ruseps.world.content.KillsTracker.KillsEntry;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.world.content.battle_pass.BattlePassType;
import com.ruseps.world.content.collectionLogs.CollectionLogManager;
//import com.ruseps.world.content.battle_pass.BattlePassType;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.weapon.FightType;
import com.ruseps.world.content.dailytasks.DailyTaskSlot;
import com.ruseps.world.content.grandexchange.GrandExchangeOffer;
import com.ruseps.world.content.grandexchange.GrandExchangeSlot;
import com.ruseps.world.content.grandexchange.GrandExchangeSlotState;
import com.ruseps.world.content.skill.SkillManager.Skills;
import com.ruseps.world.content.skill.impl.slayer.SlayerMaster;
import com.ruseps.world.content.skill.impl.slayer.SlayerTasks;

import com.ruseps.world.content.skill.impl.construction.ConstructionSave;

public class PlayerLoading {

	public static int getResult(Player player) {
		return getResult(player, false);
	}

	public static int getResult(Player player, boolean force) {
		System.out.println("called result");
		// Create the path and file objects.
		Path path = Paths.get("./data/saves/characters/", player.getUsername() + ".json");
		File file = path.toFile();

		// If the file doesn't exist, we're logging in for the first
		// time and can skip all of this.
		if (!file.exists()) {
			System.out.println("file doesnt exist");
			return LoginResponses.NEW_ACCOUNT;
		}

		// Now read the properties from the json parser.
		try (FileReader fileReader = new FileReader(file)) {
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().registerTypeAdapter(Perk.class, new InterfaceAdapter<Perk>()).create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("reset-season-one")) {
				player.getBattlePass().setResetSeasonOne(reader.get("reset-season-one").getAsBoolean());
			}
			if (reader.has("battlePassLevel")) {
				player.getBattlePass().setLevel(reader.get("battlePassLevel").getAsInt());
			}
			if (reader.has("battlePassExp")) {
				player.getBattlePass().setExperience(reader.get("battlePassExp").getAsInt());
			}
			if (reader.has("battlePassType")) {
				player.getBattlePass().setType(BattlePassType.valueOf(reader.get("battlePassType").getAsString()));
			}
			if (reader.has("BronzeBattlepassClaimed")) {
				player.setbronzeBattlepassExpires(reader.get("BronzeBattlepassClaimed").getAsString());
			}
			if (reader.has("BronzeBattlepassExpires")) {
				player.setbronzeBattlepassExpires(reader.get("BronzeBattlepassExpires").getAsString());
			}
			if (reader.has("GoldBattlepassClaimed")) {
				player.setgoldBattlepassClaimed(reader.get("GoldBattlepassClaimed").getAsString());
			}

			if (reader.has("GoldBattlepassExpires")) {
				player.setgoldBattlepassExpires(reader.get("GoldBattlepassExpires").getAsString());
			}

			if (reader.has("starterprogressioncompletions")) {
				player.setStarterProgressionCompletions(
						builder.fromJson(reader.get("starterprogressioncompletions"), boolean[].class));
			}

			if (reader.has("total-play-time-ms")) {
				player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());
			}

			if (reader.has("last-teleport-time")) {
				player.setLastTeleportTime(reader.get("last-teleport-time").getAsLong());
			}

			if (reader.has("username")) {
				player.setUsername(reader.get("username").getAsString());
			}

			if (reader.has("password")) {
				String password = reader.get("password").getAsString();
				if (!force) {
					if (!player.getPassword().equals(password)) {
						return LoginResponses.LOGIN_INVALID_CREDENTIALS;
					}
				}
				player.setPassword(password);
			}

			if (reader.has("referral")) {
				player.hasReferral = reader.get("referral").getAsBoolean();
			}

			if (reader.has("boss-points")) {
				player.setBossPoints(reader.get("boss-points").getAsInt());
			}

			if (reader.has("dr-boost")) {
				player.setDrBoost(reader.get("dr-boost").getAsInt());
			}

			if (reader.has("perks")) {
				List<Perk> perks = builder.fromJson(reader.get("perks"),
						new com.google.gson.reflect.TypeToken<List<Perk>>() {
						}.getType());
				player.setPerks(perks);
				player.recomputeAttributes();
			}

			if (reader.has("npctaskcompletions")) {
				boolean[] completions = builder.fromJson(reader.get("npctaskcompletions"), boolean[].class);
				if (player.getNpcTaskCompletions().length > completions.length) {
					boolean[] result = new boolean[player.getNpcTaskCompletions().length];
					System.arraycopy(completions, 0, result, 0, completions.length);
					player.setNpcTaskCompletions(result);
				} else {
					player.setNpcTaskCompletions(completions);
				}
			}

			if (reader.has("skillingtaskcompletions")) {
				boolean[] completions = builder.fromJson(reader.get("skillingtaskcompletions"), boolean[].class);
				if (player.getSkillingTaskCompletions().length > completions.length) {
					boolean[] result = new boolean[player.getSkillingTaskCompletions().length];
					System.arraycopy(completions, 0, result, 0, completions.length);
					player.setSkillingTaskCompletions(result);
				} else {
					player.setSkillingTaskCompletions(completions);
				}
			}

			if (reader.has("minigametaskcompletions")) {
				boolean[] completions = builder.fromJson(reader.get("minigametaskcompletions"), boolean[].class);
				if (player.getMinigameTaskCompletions().length > completions.length) {
					boolean[] result = new boolean[player.getMinigameTaskCompletions().length];
					System.arraycopy(completions, 0, result, 0, completions.length);
					player.setMinigameTaskCompletions(result);
				} else {
					player.setMinigameTaskCompletions(completions);
				}
			}

			if (reader.has("othertaskcompletions")) {
				boolean[] completions = builder.fromJson(reader.get("othertaskcompletions"), boolean[].class);
				if (player.getOtherTaskCompletions().length > completions.length) {
					boolean[] result = new boolean[player.getOtherTaskCompletions().length];
					System.arraycopy(completions, 0, result, 0, completions.length);
					player.setOtherTaskCompletions(result);
				} else {
					player.setOtherTaskCompletions(completions);
				}
			}

			if (reader.has("miscamountmap")) {
				Map<String, Integer> miscAmountMap = builder.fromJson(reader.get("miscamountmap"),
						new com.google.gson.reflect.TypeToken<Map<String, Integer>>() {
						}.getType());
				player.setMiscAmountMap(miscAmountMap);
			}

			if (reader.has("miscstatemap")) {
				Map<String, Boolean> miscStateMap = builder.fromJson(reader.get("miscstatemap"),
						new com.google.gson.reflect.TypeToken<Map<String, Boolean>>() {
						}.getType());
				player.setMiscStateMap(miscStateMap);
			}

			if (reader.has("skillingdata")) {
				Map<Skill, Map<Integer, Integer>> skillingData = builder.fromJson(reader.get("skillingdata"),
						new com.google.gson.reflect.TypeToken<Map<Skill, Map<Integer, Integer>>>() {
						}.getType());
				player.setSkillingData(skillingData);
			}

			if (reader.has("npc-kc-data")) {
				@SuppressWarnings("serial")
				Map<Integer, Integer> kcData = builder.fromJson(reader.get("npc-kc-data"),
						new TypeToken<Map<Integer, Integer>>() {
						}.getType());
				player.setNpcKillCount(kcData);
			}

			/*
			 * if (reader.has("collected-items")) { Map<Integer, Map<Integer, Integer>>
			 * collectedItems = builder.fromJson(reader.get("collected-items"), new
			 * com.google.gson.reflect.TypeToken<Map<Integer, Map<Integer, Integer>>>() {
			 * }.getType()); player.setCollectedItems(collectedItems); }
			 */

			if (reader.has("collection-log")) {
				CollectionLogManager.LogProgress[] logs = builder.fromJson(
						reader.get("collection-log").getAsJsonArray(), CollectionLogManager.LogProgress[].class);
				for (CollectionLogManager.LogProgress l : logs) {
					player.getCollectionLogManager().addLogProgress(l);
				}
			}

			if (reader.has("claimed-logs")) {
				String[] logs = builder.fromJson(reader.get("claimed-logs").getAsJsonArray(), String[].class);
				for (String l : logs) {
					player.getCollectionLogManager().getClaimedCollectionRewards().add(l);
				}
			}

			if (reader.has("password-reset")) {
				player.setPasswordPlayer(reader.get("password-reset").getAsInt());
			}

			if (reader.has("cosmetic-equipment")) {
				player.setCosmeticEquipment(builder.fromJson(reader.get("cosmetic-equipment"), int[].class));
			}

			if (reader.has("email")) {
				player.setEmailAddress(reader.get("email").getAsString());
			}

			if (reader.has("staff-rights")) {
				player.setRights(PlayerRights.valueOf(reader.get("staff-rights").getAsString()));
			}

			if (reader.has("game-mode")) {
				player.setGameMode(GameMode.valueOf(reader.get("game-mode").getAsString()));
			}

			if (reader.has("loyalty-title")) {
				player.setLoyaltyTitle(LoyaltyTitles.valueOf(reader.get("loyalty-title").getAsString()));
			}

			if (reader.has("position")) {
				player.getPosition().setAs(builder.fromJson(reader.get("position"), Position.class));
				if (player.getPosition().getZ() >= 4) {
					player.getPosition().setZ(0);
				}
			}

			if (reader.has("online-status")) {
				player.getRelations().setStatus(PrivateChatStatus.valueOf(reader.get("online-status").getAsString()),
						false);
			}

			if (reader.has("money-pouch")) {
				player.setMoneyInPouch(reader.get("money-pouch").getAsLong());
			}

			if (reader.has("given-starter")) {
				player.setReceivedStarter(reader.get("given-starter").getAsBoolean());
			}

			if (reader.has("is-instanced")) {
				player.setPlayerInstanced(reader.get("is-instanced").getAsBoolean());
			}
			if (reader.has("donated")) {
				player.incrementAmountDonated(reader.get("donated").getAsInt());
			}

			if (reader.has("minutes-bonus-exp")) {
				player.setMinutesBonusExp(reader.get("minutes-bonus-exp").getAsInt(), false);
			}

			if (reader.has("total-gained-exp")) {
				player.getSkillManager().setTotalGainedExp(reader.get("total-gained-exp").getAsInt());
			}

			if (reader.has("dung-tokens")) {
				player.getPointsHandler().setDungeoneeringTokens(reader.get("dung-tokens").getAsInt(), false);
			}

			if (reader.has("prestige-points")) {
				player.getPointsHandler().setPrestigePoints(reader.get("prestige-points").getAsInt(), false);
			}
			if (reader.has("achievements")) {
				player.getAchievementTracker().load(reader.get("achievements"));
			}
			if (reader.has("achievement-points")) {
				player.getPointsHandler().setAchievementPoints(reader.get("achievement-points").getAsInt(), false);
			}

			if (reader.has("commendations")) {
				player.getPointsHandler().setCommendations(reader.get("commendations").getAsInt(), false);
			}

			if (reader.has("loyalty-points")) {
				player.getPointsHandler().setLoyaltyPoints(reader.get("loyalty-points").getAsInt(), false);
			}

			if (reader.has("raids-one-points")) {
				player.getPointsHandler().setRaidsOnePoints(reader.get("raids-one-points").getAsInt());
			}

			if (reader.has("raids-two-points")) {
				player.getPointsHandler().setRaidsTwoPoints(reader.get("raids-two-points").getAsInt());
			}

			if (reader.has("voting-points")) {
				player.getPointsHandler().setVotingPoints(reader.get("voting-points").getAsInt(), false);
			}

			if (reader.has("slayer-points")) {
				player.getPointsHandler().setSlayerPoints(reader.get("slayer-points").getAsInt(), false);
			}

			if (reader.has("hide-player")) {
				player.setHidePlayer(reader.get("hide-player").getAsBoolean());
			}

			if (reader.has("pk-points")) {
				player.getPointsHandler().setPkPoints(reader.get("pk-points").getAsInt(), false);
			}
			if (reader.has("BattleRoyal-Points")) {
				player.getPointsHandler().setPkPoints(reader.get("BattleRoyal-points").getAsInt(), false);
			}
			if (reader.has("donation-points")) {
				player.getPointsHandler().setDonationPoints(reader.get("donation-points").getAsInt(), false);
			}
			if (reader.has("trivia-points")) {
				player.getPointsHandler().setTriviaPoints(reader.get("trivia-points").getAsInt(), false);
			}

			if (reader.has("cluescomplted")) {
				ClueScrolls.setCluesCompleted(reader.get("cluescompleted").getAsInt(), false);
			}

			if (reader.has("player-kills")) {
				player.getPlayerKillingAttributes().setPlayerKills(reader.get("player-kills").getAsInt());
			}

			if (reader.has("player-killstreak")) {
				player.getPlayerKillingAttributes().setPlayerKillStreak(reader.get("player-killstreak").getAsInt());
			}

			if (reader.has("player-deaths")) {
				player.getPlayerKillingAttributes().setPlayerDeaths(reader.get("player-deaths").getAsInt());
			}

			if (reader.has("target-percentage")) {
				player.getPlayerKillingAttributes().setTargetPercentage(reader.get("target-percentage").getAsInt());
			}

			if (reader.has("bh-rank")) {
				player.getAppearance().setBountyHunterSkull(reader.get("bh-rank").getAsInt());
			}

			if (reader.has("gender")) {
				player.getAppearance().setGender(Gender.valueOf(reader.get("gender").getAsString()));
			}

			if (reader.has("spell-book")) {
				player.setSpellbook(MagicSpellbook.valueOf(reader.get("spell-book").getAsString()));
			}

			if (reader.has("prayer-book")) {
				player.setPrayerbook(Prayerbook.valueOf(reader.get("prayer-book").getAsString()));
			}
			if (reader.has("running")) {
				player.setRunning(reader.get("running").getAsBoolean());
			}
			if (reader.has("run-energy")) {
				player.setRunEnergy(reader.get("run-energy").getAsInt());
			}
			if (reader.has("music")) {
				player.setMusicActive(reader.get("music").getAsBoolean());
			}
			if (reader.has("sounds")) {
				player.setSoundsActive(reader.get("sounds").getAsBoolean());
			}
			if (reader.has("auto-retaliate")) {
				player.setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
			}
			if (reader.has("xp-locked")) {
				player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
			}
			if (reader.has("veng-cast")) {
				player.setHasVengeance(reader.get("veng-cast").getAsBoolean());
			}
			if (reader.has("last-veng")) {
				player.getLastVengeance().reset(reader.get("last-veng").getAsLong());
			}
			if (reader.has("fight-type")) {
				player.setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
			}
			if (reader.has("sol-effect")) {
				player.setStaffOfLightEffect(Integer.valueOf(reader.get("sol-effect").getAsInt()));
			}
			if (reader.has("skull-timer")) {
				player.setSkullTimer(reader.get("skull-timer").getAsInt());
			}
			if (reader.has("accept-aid")) {
				player.setAcceptAid(reader.get("accept-aid").getAsBoolean());
			}
			if (reader.has("poison-damage")) {
				player.setPoisonDamage(reader.get("poison-damage").getAsInt());
			}
			if (reader.has("poison-immunity")) {
				player.setPoisonImmunity(reader.get("poison-immunity").getAsInt());
			}
			if (reader.has("venom-damage")) {
				player.setVenomDamage(reader.get("venom-damage").getAsInt());
			}
			if (reader.has("venom-immunity")) {
				player.setVenomImmunity(reader.get("venom-immunity").getAsInt());
			}
			if (reader.has("overload-timer")) {
				player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
			}
			if (reader.has("fire-immunity")) {
				player.setFireImmunity(reader.get("fire-immunity").getAsInt());
			}
			if (reader.has("fire-damage-mod")) {
				player.setFireDamageModifier(reader.get("fire-damage-mod").getAsInt());
			}
			if (reader.has("overload-timer")) {
				player.setOverloadPotionTimer(reader.get("overload-timer").getAsInt());
			}
			if (reader.has("prayer-renewal-timer")) {
				player.setPrayerRenewalPotionTimer(reader.get("prayer-renewal-timer").getAsInt());
			}
			if (reader.has("teleblock-timer")) {
				player.setTeleblockTimer(reader.get("teleblock-timer").getAsInt());
			}
			if (reader.has("special-amount")) {
				player.setSpecialPercentage(reader.get("special-amount").getAsInt());
			}

			if (reader.has("entered-gwd-room")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setHasEnteredRoom(reader.get("entered-gwd-room").getAsBoolean());
			}

			if (reader.has("gwd-altar-delay")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setAltarDelay(reader.get("gwd-altar-delay").getAsLong());
			}

			if (reader.has("gwd-killcount")) {
				player.getMinigameAttributes().getGodwarsDungeonAttributes()
						.setKillcount(builder.fromJson(reader.get("gwd-killcount"), int[].class));
			}

			if (reader.has("effigy")) {
				player.setEffigy(reader.get("effigy").getAsInt());
			}

			if (reader.has("summon-npc")) {
				int npc = reader.get("summon-npc").getAsInt();
				if (npc > 0)
					player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player)).setFamiliarId(npc);
			}
			if (reader.has("summon-death")) {
				int death = reader.get("summon-death").getAsInt();
				if (death > 0 && player.getSummoning().getSpawnTask() != null)
					player.getSummoning().getSpawnTask().setDeathTimer(death);
			}
			if (reader.has("process-farming")) {
				player.setProcessFarming(reader.get("process-farming").getAsBoolean());
			}

			if (reader.has("clanchat")) {
				String clan = reader.get("clanchat").getAsString();
				if (!clan.equals("null"))
					player.setClanChatName(clan);
			}
			if (reader.has("autocast")) {
				player.setAutocast(reader.get("autocast").getAsBoolean());
			}
			if (reader.has("autocast-spell")) {
				int spell = reader.get("autocast-spell").getAsInt();
				if (spell != -1)
					player.setAutocastSpell(CombatSpells.getSpell(spell));
			}

			if (reader.has("dfs-charges")) {
				player.incrementDfsCharges(reader.get("dfs-charges").getAsInt());
			}

			if (reader.has("kills")) {
				Deque<KillsEntry> filter = new ArrayDeque<>();
				Stream<KillsEntry> asStream = Arrays
						.stream(builder.fromJson(reader.get("kills").getAsJsonArray(), KillsEntry[].class));
				asStream.filter(Objects::nonNull).forEach(object -> {
					if (object.getId() > 0)
						filter.add(object);
				});
				KillsTracker.submit(player, filter.toArray(new KillsEntry[filter.size()]));
			}

			if (reader.has("drops")) {
				DropLog.submit(player, builder.fromJson(reader.get("drops").getAsJsonArray(), DropLogEntry[].class));
			}

			if (reader.has("shop-updated")) {
				player.setShopUpdated(reader.get("shop-updated").getAsBoolean());
			}

			if (reader.has("shop-earnings")) {
				player.getPlayerOwnedShopManager().setEarnings(reader.get("shop-earnings").getAsLong());
			}

			if (reader.has("slayer-master")) {
				player.getSlayer().setSlayerMaster(SlayerMaster.valueOf(reader.get("slayer-master").getAsString()));
			}

			if (reader.has("slayer-task")) {
				try {
					player.getSlayer().setSlayerTask(SlayerTasks.valueOf(reader.get("slayer-task").getAsString()));
				} catch (IllegalArgumentException e) {
					player.getSlayer().setSlayerTask(null);
				}
			}

			if (reader.has("prev-slayer-task")) {
				player.getSlayer().setLastTask(SlayerTasks.valueOf(reader.get("prev-slayer-task").getAsString()));
			}

			if (reader.has("task-amount")) {
				player.getSlayer().setAmountToSlay(reader.get("task-amount").getAsInt());
			}

			if (reader.has("task-streak")) {
				player.getSlayer().setTaskStreak(reader.get("task-streak").getAsInt());
			}

			if (reader.has("duo-partner")) {
				String partner = reader.get("duo-partner").getAsString();
				player.getSlayer().setDuoPartner(partner.equals("null") ? null : partner);
			}

			if (reader.has("double-slay-xp")) {
				player.getSlayer().doubleSlayerXP = reader.get("double-slay-xp").getAsBoolean();
			}

			if (reader.has("recoil-deg")) {
				player.setRecoilCharges(reader.get("recoil-deg").getAsInt());
			}

			if (reader.has("brawler-deg")) {
				player.setBrawlerCharges(builder.fromJson(reader.get("brawler-deg").getAsJsonArray(), int[].class));
			}

			if (reader.has("killed-players")) {
				List<String> list = new ArrayList<String>();
				String[] killed_players = builder.fromJson(reader.get("killed-players").getAsJsonArray(),
						String[].class);
				for (String s : killed_players)
					list.add(s);
				player.getPlayerKillingAttributes().setKilledPlayers(list);
			}

			if (reader.has("barrows-brother")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsData(
						builder.fromJson(reader.get("barrows-brother").getAsJsonArray(), int[][].class));
			}

			if (reader.has("random-coffin")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes()
						.setRandomCoffin((reader.get("random-coffin").getAsInt()));
			}

			if (reader.has("barrows-killcount")) {
				player.getMinigameAttributes().getBarrowsMinigameAttributes()
						.setKillcount((reader.get("barrows-killcount").getAsInt()));
			}

			if (reader.has("nomad")) {
				player.getMinigameAttributes().getNomadAttributes()
						.setQuestParts(builder.fromJson(reader.get("nomad").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("valentines")) {
				player.getMinigameAttributes().getValentinesAttributes()
						.setQuestParts(builder.fromJson(reader.get("valentines").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("recipe-for-disaster")) {
				player.getMinigameAttributes().getRecipeForDisasterAttributes().setQuestParts(
						builder.fromJson(reader.get("recipe-for-disaster").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("recipe-for-disaster-wave")) {
				player.getMinigameAttributes().getRecipeForDisasterAttributes()
						.setWavesCompleted((reader.get("recipe-for-disaster-wave").getAsInt()));
			}

			if (reader.has("dung-items-bound")) {
				player.getMinigameAttributes().getDungeoneeringAttributes()
						.setBoundItems(builder.fromJson(reader.get("dung-items-bound").getAsJsonArray(), int[].class));
			}

			if (reader.has("rune-ess")) {
				player.setStoredRuneEssence((reader.get("rune-ess").getAsInt()));
			}

			if (reader.has("pure-ess")) {
				player.setStoredPureEssence((reader.get("pure-ess").getAsInt()));
			}

			if (reader.has("bank-pin")) {
				player.getBankPinAttributes()
						.setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
			}

			if (reader.has("has-bank-pin")) {
				player.getBankPinAttributes().setHasBankPin(reader.get("has-bank-pin").getAsBoolean());
			}
			if (reader.has("last-pin-attempt")) {
				player.getBankPinAttributes().setLastAttempt(reader.get("last-pin-attempt").getAsLong());
			}
			if (reader.has("invalid-pin-attempts")) {
				player.getBankPinAttributes().setInvalidAttempts(reader.get("invalid-pin-attempts").getAsInt());
			}

			if (reader.has("bank-pin")) {
				player.getBankPinAttributes()
						.setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));
			}

			if (reader.has("appearance")) {
				player.getAppearance().set(builder.fromJson(reader.get("appearance").getAsJsonArray(), int[].class));
			}

			if (reader.has("agility-obj")) {
				player.setCrossedObstacles(
						builder.fromJson(reader.get("agility-obj").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("skills")) {
				player.getSkillManager().setSkills(builder.fromJson(reader.get("skills"), Skills.class));
			}
			if (reader.has("inventory")) {
				player.getInventory()
						.setItems(builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class));
			}
			if (reader.has("equipment")) {
				player.getEquipment()
						.setItems(builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class));
			}

			/** BANK **/
			for (int i = 0; i < 9; i++) {
				if (reader.has("bank-" + i + ""))
					player.setBank(i, new Bank(player)).getBank(i).addItems(
							builder.fromJson(reader.get("bank-" + i + "").getAsJsonArray(), Item[].class), false);
			}

			if (reader.has("bank-0")) {
				player.setBank(0, new Bank(player)).getBank(0)
						.addItems(builder.fromJson(reader.get("bank-0").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-1")) {
				player.setBank(1, new Bank(player)).getBank(1)
						.addItems(builder.fromJson(reader.get("bank-1").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-2")) {
				player.setBank(2, new Bank(player)).getBank(2)
						.addItems(builder.fromJson(reader.get("bank-2").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-3")) {
				player.setBank(3, new Bank(player)).getBank(3)
						.addItems(builder.fromJson(reader.get("bank-3").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-4")) {
				player.setBank(4, new Bank(player)).getBank(4)
						.addItems(builder.fromJson(reader.get("bank-4").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-5")) {
				player.setBank(5, new Bank(player)).getBank(5)
						.addItems(builder.fromJson(reader.get("bank-5").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-6")) {
				player.setBank(6, new Bank(player)).getBank(6)
						.addItems(builder.fromJson(reader.get("bank-6").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-7")) {
				player.setBank(7, new Bank(player)).getBank(7)
						.addItems(builder.fromJson(reader.get("bank-7").getAsJsonArray(), Item[].class), false);
			}
			if (reader.has("bank-8")) {
				player.setBank(8, new Bank(player)).getBank(8)
						.addItems(builder.fromJson(reader.get("bank-8").getAsJsonArray(), Item[].class), false);
			}

			if (reader.has("ge-slots")) {
				GrandExchangeSlot[] slots = builder.fromJson(reader.get("ge-slots").getAsJsonArray(),
						GrandExchangeSlot[].class);
				player.setGrandExchangeSlots(slots);
			}

			if (reader.has("store")) {
				Item[] validStoredItems = builder.fromJson(reader.get("store").getAsJsonArray(), Item[].class);
				if (player.getSummoning().getSpawnTask() != null)
					player.getSummoning().getSpawnTask().setValidItems(validStoredItems);
			}

			if (reader.has("charm-imp")) {
				int[] charmImpConfig = builder.fromJson(reader.get("charm-imp").getAsJsonArray(), int[].class);
				player.getSummoning().setCharmimpConfig(charmImpConfig);
			}

			if (reader.has("blowpipe-charge-item") || reader.has("blowpipe-charge-amount")) {
				player.getBlowpipeLoading().getContents().setCount(reader.get("blowpipe-charge-item").getAsInt(),
						reader.get("blowpipe-charge-amount").getAsInt());
			}

			if (reader.has("friends")) {
				long[] friends = builder.fromJson(reader.get("friends").getAsJsonArray(), long[].class);

				for (long l : friends) {
					player.getRelations().getFriendList().add(l);
				}
			}
			if (reader.has("ignores")) {
				long[] ignores = builder.fromJson(reader.get("ignores").getAsJsonArray(), long[].class);

				for (long l : ignores) {
					player.getRelations().getIgnoreList().add(l);
				}
			}

			if (reader.has("loyalty-titles")) {
				player.setUnlockedLoyaltyTitles(
						builder.fromJson(reader.get("loyalty-titles").getAsJsonArray(), boolean[].class));
			}

			if (reader.has("max-cape-colors")) {
				int[] colors = builder.fromJson(reader.get("max-cape-colors").getAsJsonArray(), int[].class);
				player.setMaxCapeColors(colors);
			}

			if (reader.has("comp-cape-colors")) {
				int[] colors = builder.fromJson(reader.get("comp-cape-colors").getAsJsonArray(), int[].class);
				player.setCompCapeColors(colors);
			}

			if (reader.has("player-title")) {
				player.setTitle(reader.get("player-title").getAsString());
			}

			if (reader.has("group-owner-name")) {
				player.setGroupOwnerName(reader.get("group-owner-name").getAsString());
			}

			if (reader.has("is-gim")) {
				player.setGim(reader.get("is-gim").getAsBoolean());
			}
			if (reader.has("AnimeRaidCompletion")) {
				player.setAnimeRaidsOpened(reader.get("AnimeRaidCompletion").getAsInt());
			}
			if (reader.has("PokemonRaidCompletion")) {
				player.setPokemonRaidsOpened(reader.get("PokemonRaidCompletion").getAsInt());
			}
			if (reader.has("MKRaidCompletion")) {
				player.setMkRaidsOpened(reader.get("MKRaidCompletion").getAsInt());
			}
			if (reader.has("SilverRaidCompletion")) {
				player.setSilverRaidsOpened(reader.get("SilverRaidCompletion").getAsInt());
			}
			if (reader.has("GoldRaidCompletion")) {
				player.setGoldRaidsOpened(reader.get("GoldRaidCompletion").getAsInt());
			}
			if (reader.has("PlatRaidCompletion")) {
				player.setPlatRaidsOpened(reader.get("PlatRaidCompletion").getAsInt());
			}
			if (reader.has("DiamondRaidCompletion")) {
				player.setDiamondRaidsOpened(reader.get("DiamondRaidCompletion").getAsInt());
			}
			if (reader.has("RubyRaidCompletion")) {
				player.setRubyRaidsOpened(reader.get("RubyRaidCompletion").getAsInt());
			}
			if (reader.has("darkDementionRaidCompletion")) {
				player.setDarkDementionRaidsOpened(reader.get("darkDementionRaidCompletion").getAsInt());
			}
			if (reader.has("dsRaidCompletion")) {
				player.setDragonstoneRaidsOpened(reader.get("dsRaidCompletion").getAsInt());
			}

			if (reader.has("daily-task-slots")) {
				player.getDailyTaskManager().setTaskSlots(
						builder.fromJson(reader.get("daily-task-slots").getAsJsonArray(), DailyTaskSlot[][].class));
			}

			if (reader.has("daily-tasks-completed")) {
				player.setDailyTasksCompleted(reader.get("daily-tasks-completed").getAsInt());
			}

			if (reader.has("daily-date")) {
                player.getDailyTaskManager().setDailyDate(reader.get("daily-date").getAsLong());
            }

		} catch (Exception e) {
			e.printStackTrace();
			return LoginResponses.LOGIN_SUCCESSFUL;
		}
		try {

			Path path2 = Paths.get("./data/saves/construction/", player.getUsername() + ".obj");
			File file2 = path2.toFile();

			if (file2.exists()) {
				FileInputStream fileIn = new FileInputStream(file2);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ConstructionSave save = (ConstructionSave) in.readObject();
				player.setHouseRooms(save.getHouseRooms());
				player.setHouseFurtinture(save.getHouseFurniture());
				player.setHousePortals(save.getHousePortals());
				in.close();
				fileIn.close();
			}

		} catch (Throwable t) {
			// t.printStackTrace();
			return LoginResponses.LOGIN_COULD_NOT_COMPLETE;
		}
		return LoginResponses.LOGIN_SUCCESSFUL;
	}

}