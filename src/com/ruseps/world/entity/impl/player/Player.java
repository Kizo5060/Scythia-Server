package com.ruseps.world.entity.impl.player;

import com.ruseps.world.World;
import com.ruseps.world.content.*;
import com.ruseps.world.content.bis.BestInSlotInterface;
import com.ruseps.world.content.instance_manager.InstanceManager;
import com.ruseps.world.content.instance_manager.InstanceNpcs;
import com.ruseps.world.content.customtasks.CustomTasks;
import com.ruseps.world.content.customtasks.tasktypes.MinigameTask;
import com.ruseps.world.content.customtasks.tasktypes.NPCTask;
import com.ruseps.world.content.customtasks.tasktypes.OtherTask;
import com.ruseps.world.content.customtasks.tasktypes.SkillingTask;
import com.ruseps.world.content.dailytasks.DailyTaskManager;
import com.ruseps.world.content.mboxes.PetBox;
import com.ruseps.world.content.mboxes.RubyBox;
import com.ruseps.world.content.skill.impl.prayer.*;
//import com.ruseps.world.content.battle_pass.BattlePass;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ruseps.world.content.teleport.TeleportEnum;
import com.ruseps.GameSettings;
import com.ruseps.world.content.achievements.AchievementInterface;
import com.ruseps.world.content.achievements.AchievementTracker;
import com.ruseps.world.content.battle_pass.BattlePass;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.PlayerDeathTask;
import com.ruseps.engine.task.impl.WalkToTask;
import com.ruseps.model.Animation;
import com.ruseps.model.Appearance;
import com.ruseps.model.CharacterAnimations;
import com.ruseps.model.ChatMessage;
import com.ruseps.model.DwarfCannon;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GameObject;
import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.MagicSpellbook;
import com.ruseps.model.PlayerInteractingOption;
import com.ruseps.model.PlayerRelations;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Prayerbook;
import com.ruseps.model.Skill;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.model.container.impl.Bank.BankSearchAttributes;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.GambleOfferItemContainer;
import com.ruseps.model.container.impl.Inventory;
import com.ruseps.model.container.impl.PriceChecker;
import com.ruseps.model.container.impl.Shop;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.definitions.WeaponInterfaces.WeaponInterface;
import com.ruseps.model.input.Input;
import com.ruseps.net.PlayerSession;
import com.ruseps.net.SessionState;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketSender;
import com.ruseps.util.FrameUpdater;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.content.BankPin.BankPinAttributes;
import com.ruseps.world.content.DropLog.DropLogEntry;
import com.ruseps.world.content.KillsTracker.KillsEntry;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.world.content.StartScreen.GameModes;
import com.ruseps.world.content.achievements.AchievementInterface;
import com.ruseps.world.content.achievements.AchievementTracker;
import com.ruseps.world.content.chestsystem.ChestViewer;
import com.ruseps.world.content.clan.ClanChat;
import com.ruseps.world.content.collectionlog.CollectionLogInterfaceHandler;
import com.ruseps.world.content.collectionLogs.CollectionLogManager;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ruseps.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.ruseps.world.content.combat.magic.CombatSpell;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.magic.CustomMagicStaff;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.pvp.PlayerKillingAttributes;
import com.ruseps.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.combat.weapon.FightType;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dicing.Dicing;
import com.ruseps.world.content.fuser.FuserEnum;
import com.ruseps.world.content.fuser.FuserHandler;
import com.ruseps.world.content.fuser.FuserType;
import com.ruseps.world.content.gambling.GamblingManager;
import com.ruseps.world.content.grandexchange.GrandExchangeSlot;
import com.ruseps.world.content.groupironman.GroupIronman;
import com.ruseps.world.content.groupironman.GroupIronmanGroup;
import com.ruseps.world.content.interface_timers.OverloadTimer;
import com.ruseps.world.content.interface_timers.x2DamageTimer;
import com.ruseps.world.content.mboxes.ScythiaBox;
import com.ruseps.world.content.mboxes.SilverBox;
import com.ruseps.world.content.mboxes.BronzeBox;
import com.ruseps.world.content.mboxes.DiamondBox;
import com.ruseps.world.content.mboxes.GoldBox;
import com.ruseps.world.content.mboxes.MysteryBox;
import com.ruseps.world.content.mboxes.OwnerBox;
import com.ruseps.world.content.minigames.Minigame;
import com.ruseps.world.content.minigames.MinigameAttributes;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.new_raids_system.RaidsConnector;
import com.ruseps.world.content.new_raids_system.RaidsPartyConnector;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.new_raids_system.saving.RaidsSaving;
import com.ruseps.world.content.pos.PlayerOwnedShopManager;
import com.ruseps.world.content.referral.RefferalHandler;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.ruseps.world.content.skill.impl.construction.ConstructionData.HouseTheme;
import com.ruseps.world.content.skill.impl.construction.HouseFurniture;
import com.ruseps.world.content.skill.impl.construction.Portal;
import com.ruseps.world.content.skill.impl.construction.Room;
import com.ruseps.world.content.skill.impl.farming.Farming;
import com.ruseps.world.content.skill.impl.slayer.Slayer;
import com.ruseps.world.content.skill.impl.summoning.Pouch;
import com.ruseps.world.content.skill.impl.summoning.Summoning;
import com.ruseps.world.content.starter_progression.StarterProgression;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

import lombok.Getter;

public class Player extends Character {
	private long serverPerksContributions;


	public long getServerPerksContributions() {
		return serverPerksContributions;
	}

	public void setServerPerksContributions(long serverPerksContributions) {
		this.serverPerksContributions = serverPerksContributions;
	}
	
	public FuserEnum currentFuse;

	public FuserType fuserType;

	private FuserHandler fuserHandler = new FuserHandler(this);

	public FuserEnum getCurrentFuse() {
		return currentFuse;
	}

	public void setCurrentFuse(FuserEnum currentFuse) {
		this.currentFuse = currentFuse;
	}

	public FuserType getFuserType() {
		return fuserType;
	}

	public void setFuserType(FuserType fuserType) {
		this.fuserType = fuserType;
	}

	public FuserHandler getFuserHandler() {
		return fuserHandler;
	}

	public void setFuserHandler(FuserHandler fuserHandler) {
		this.fuserHandler = fuserHandler;
	}

	private int dailyTasksCompleted;

	private final DailyTaskManager dailyTaskManager = new DailyTaskManager(this);

	public int getDailyTasksCompleted() {
		return dailyTasksCompleted;
	}

	public void setDailyTasksCompleted(int dailyTasksCompleted) {
		this.dailyTasksCompleted = dailyTasksCompleted;
	}

	public DailyTaskManager getDailyTaskManager() {
		return dailyTaskManager;
	}

	private CollectionLogManager collectionLog = new CollectionLogManager(this);

	public CollectionLogManager getCollectionLogManager() {
		return collectionLog;
	}

	public void setCollectionLogManager(CollectionLogManager log) {
		collectionLog = log;
	}

	private boolean placeholders = true;

	public boolean isPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(boolean flag) {
		placeholders = flag;
	}

	private long lastTeleportTime;

	public void depositItemBank(Item item) {
		if (ItemDefinition.forId(item.getId()).isNoted()) {
			item.setId(Item.getUnNoted(item.getId()));
		}
		getBank(Bank.getTabForItem(this, item.getId())).add(item);
	}

	public void depositItemBank(Item item, boolean refresh) {
		if (ItemDefinition.forId(item.getId()).isNoted()) {
			item.setId(Item.getUnNoted(item.getId()));
		}
		getBank(Bank.getTabForItem(this, item.getId())).add(item, refresh);
	}

	public void depositItemBank(int tab, Item item, boolean refresh) {
		if (ItemDefinition.forId(item.getId()).isNoted()) {
			item.setId(Item.getUnNoted(item.getId()));
		}
		getBank(tab).add(item, refresh);
	}

	public long getLastTeleportTime() {
		return lastTeleportTime;
	}

	public void setLastTeleportTime(long lastTeleportTime) {
		this.lastTeleportTime = lastTeleportTime;
	}

	private Map<String, Integer> miscAmountMap = new HashMap<>();

	public Map<String, Integer> getMiscAmountMap() {
		return miscAmountMap;
	}

	public void setMiscAmountMap(Map<String, Integer> miscAmountMap) {
		this.miscAmountMap = miscAmountMap;
	}

	private AchievementInterface achievementInterface;

	public void setAchievementInterface(AchievementInterface achievementInterface) {
		this.achievementInterface = achievementInterface;
	}

	public AchievementInterface getAchievementInterface() {
		return this.achievementInterface;
	}

	private AchievementTracker achievementTracker = new AchievementTracker(this);

	public AchievementTracker getAchievementTracker() {
		return this.achievementTracker;
	}

	public void addMiscAmount(String key) {
		miscAmountMap.merge(key, 1, Integer::sum);
	}

	public void addMiscAmount(String key, int amount) {
		miscAmountMap.merge(key, amount, Integer::sum);
	}

	public int getMiscAmount(String key) {
		return miscAmountMap.getOrDefault(key, 0);
	}

	private Map<String, Boolean> miscStateMap = new HashMap<>();

	public Map<String, Boolean> getMiscStateMap() {
		return miscStateMap;
	}

	public String referaledby = "";

	private RefferalHandler refHandler = new RefferalHandler();

	public RefferalHandler getRefferalHandler() {
		return refHandler;
	}

	public void setMiscStateMap(Map<String, Boolean> miscStateMap) {
		this.miscStateMap = miscStateMap;
	}

	public void setMiscState(String key, boolean state) {
		miscStateMap.put(key, state);
	}

	public boolean getMiscState(String key) {
		return miscStateMap.getOrDefault(key, false);
	}

	private boolean[] npcTaskCompletions = new boolean[NPCTask.values().length];

	public boolean[] getNpcTaskCompletions() {
		return npcTaskCompletions;
	}

	public void setNpcTaskCompletions(boolean[] npcTaskCompletions) {
		this.npcTaskCompletions = npcTaskCompletions;
	}

	private boolean[] skillingTaskCompletions = new boolean[SkillingTask.values().length];

	public boolean[] getSkillingTaskCompletions() {
		return skillingTaskCompletions;
	}

	public void setSkillingTaskCompletions(boolean[] skillingTaskCompletions) {
		this.skillingTaskCompletions = skillingTaskCompletions;
	}

	public void setSkillingTaskCompleted(int index) {
		skillingTaskCompletions[index] = true;
	}

	private boolean[] minigameTaskCompletions = new boolean[MinigameTask.VALUES.length];

	public boolean[] getMinigameTaskCompletions() {
		return minigameTaskCompletions;
	}

	public void setMinigameTaskCompletions(boolean[] minigameTaskCompletions) {
		this.minigameTaskCompletions = minigameTaskCompletions;
	}

	private boolean miniPlayer;

	public boolean isMiniPlayer() {
		return miniPlayer;
	}

	public void setMinigameTaskCompleted(int index) {
		minigameTaskCompletions[index] = true;
	}

	private boolean[] otherTaskCompletions = new boolean[OtherTask.VALUES.length];

	public boolean[] getOtherTaskCompletions() {
		return otherTaskCompletions;
	}

	public void setOtherTaskCompletions(boolean[] otherTaskCompletions) {
		this.otherTaskCompletions = otherTaskCompletions;
	}

	public void setOtherTaskCompleted(int index) {
		otherTaskCompletions[index] = true;
	}

	public void setNpcTaskCompleted(int index) {
		this.npcTaskCompletions[index] = true;
	}

	private Map<Skill, Map<Integer, Integer>> skillingData = new HashMap<>();

	public Map<Skill, Map<Integer, Integer>> getSkillingData() {
		return skillingData;
	}

	public void setSkillingData(Map<Skill, Map<Integer, Integer>> skillingData) {
		this.skillingData = skillingData;
	}

	public Map<Integer, Integer> getSkillData(Skill skill) {
		return skillingData.get(skill);
	}

	private List<Perk> perks = new ArrayList<>();

	public List<Perk> getPerks() {
		return perks;
	}

	public void setPerks(List<Perk> perks) {
		this.perks = perks;
	}

	private Attributes attributes = new Attributes();

	public void recomputeAttributes() {
		attributes = new Attributes();
		perks.forEach(perk -> perk.apply(attributes));
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	private CustomTasks customTasks = new CustomTasks(this);

	public CustomTasks getCustomTasks() {
		return customTasks;
	}

	/** COLLECTION LOG **/
	public void handleCollectedItem(int npcId, Item item) {
		int id = item.getId();
		int amount = item.getAmount();
		if (collectedItems.get(npcId) == null) {
			Map<Integer, Integer> itemData = new HashMap<>();
			itemData.put(id, amount);
			collectedItems.put(npcId, itemData);
		} else {
			collectedItems.get(npcId).merge(id, amount, Integer::sum);
		}
		for (Map.Entry<Integer, Map<Integer, Integer>> collectedItemsForMonster : collectedItems.entrySet()) {
			// System.out.println("Collection log for monster " +
			// collectedItemsForMonster.getKey());
			for (Map.Entry<Integer, Integer> collectedItem : collectedItemsForMonster.getValue().entrySet()) {
				// System.out.println("\t Item " + collectedItem.getKey() + " x " +
				// collectedItem.getValue());
			}
		}
	}

	public Map<Integer, Map<Integer, Integer>> collectedItems = new HashMap<>();

	public Map<Integer, Map<Integer, Integer>> getCollectedItems() {
		return collectedItems;
	}

	public void setCollectedItems(Map<Integer, Map<Integer, Integer>> collectedItems) {
		collectedItems = collectedItems;
	}

	private RaidsConnector raids3 = new RaidsConnector(this);
	private RaidsPartyConnector raidsPartyConnector = new RaidsPartyConnector(this);

	public String getName() {
		return getUsername();
	}

	/* Mystery box */
	private MysteryBox mysteryBox = new MysteryBox(this);

	public MysteryBox getMysteryBox() {
		return mysteryBox;
	}

	private OwnerBox ownerBox = new OwnerBox(this);

	public OwnerBox getOwnerBox() {
		return ownerBox;
	}

	private ScythiaBox scythiaBox = new ScythiaBox(this);

	public ScythiaBox getAuroraBox() {
		return scythiaBox;
	}

	private PetBox petBox = new PetBox(this);

	public PetBox getPetBox() {
		return petBox;
	}

	private RubyBox rubyBox = new RubyBox(this);
	private DiamondBox diamondBox = new DiamondBox(this);
	private GoldBox goldBox = new GoldBox(this);
	private SilverBox silverBox = new SilverBox(this);
	private BronzeBox bronzeBox = new BronzeBox(this);

	public RubyBox getRubyBox() {
		return rubyBox;
	}

	public DiamondBox getDiamondBox() {
		return diamondBox;
	}

	public GoldBox getGoldBox() {
		return goldBox;
	}

	public SilverBox getSilverBox() {
		return silverBox;
	}

	public BronzeBox getBronzeBox() {
		return bronzeBox;
	}

	public boolean crossBridge = false;
	public boolean crossBridge1 = false;
	public boolean crossBridge2 = false;
	public boolean crossBridge4 = false;
	public boolean crossBridge3 = false;

	public boolean ownerBoxx = false;
	public boolean mboxx = false;
	public boolean auroraBoxx = false;
	public boolean petBoxx = false;
	public boolean rubyBoxx = false;
	public boolean diamondBoxx = false;
	public boolean goldBoxx = false;
	public boolean silverBoxx = false;
	public boolean bronzeBoxx = false;

	public boolean collectorMessage = true;

	private byte[] cachedUpdateBlock;
	private String mac;

	public int combineIndex;
	public ArrayList<FuserEnum> combineEnum;

	public ArrayList<TeleportEnum> teleports;
	public int tier;
	public int teleportIndex;

	private long dragonScimInjury;

	private boolean hidePlayer = true;

	private int[] maxCapeColors = { 65214, 65200, 65186, 62995 };

	private int[] compCapeColors = { 65214, 65200, 65186, 62995 };

	private int currentCape;

	private final Dicing dicing = new Dicing(this);

	public Dicing getDicing() {
		return dicing;
	}

	private final PlayerOwnedShopManager playerOwnedShopManager = new PlayerOwnedShopManager(this);

	private boolean active;

	private boolean shopUpdated;

	public PlayerOwnedShopManager getPlayerOwnedShopManager() {
		return playerOwnedShopManager;
	}

	public Map<Integer, String> interacts = new HashMap<>();

	public boolean hasInteract(int slot, String name) {
		return Objects.equals(interacts.get(slot), name);
	}

	public int[] getMaxCapeColors() {
		return maxCapeColors;
	}

	public Position lastTeleportPosition;

	private int venomImmunity;

	public int getVenomImmunity() {
		return poisonImmunity;
	}

	public void incrementVenomImmunity(int amount) {
		venomImmunity += amount;
	}

	public void decrementVenomImmunity(int amount) {
		venomImmunity -= amount;
	}

	public long npcDropTableDelay = 0;

	public void setMaxCapeColors(int[] maxCapeColors) {
		this.maxCapeColors = maxCapeColors;
	}

	private String title = "";

	public String yellmsg = "";

	private int transform;

	private Kraken kraken = new Kraken();

	/**
	 * Grabs the Kraken boss instance
	 *
	 * @return
	 */
	public Kraken getKraken() {
		return this.kraken;
	}

	/**
	 * Resets the Kraken instance
	 */
	public void resetKraken() {
		this.getKraken().reset();
		this.kraken = new Kraken();
	}

	public Player(PlayerSession playerIO) {
		super(GameSettings.DEFAULT_POSITION.copy());
		this.session = playerIO;
	}

	private Map<String, Object> oldAttributes = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) oldAttributes.get(key);
	}

	private Minigame minigame = null;

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, T fail) {
		Object object = oldAttributes.get(key);
		return object == null ? fail : (T) object;
	}

	public boolean hasAttribute(String key) {
		return oldAttributes.containsKey(key);
	}

	public void removeAttribute(String key) {
		oldAttributes.remove(key);
	}

	private int hardwareNumber;

	public int getHardwareNumber() {
		return hardwareNumber;
	}

	public Player setHardwareNumber(int hardwareNumber) {
		this.hardwareNumber = hardwareNumber;
		return this;
	}

	public void setAttribute(String key, Object value) {
		oldAttributes.put(key, value);
	}

	@Override
	public void appendDeath() {
		if (!isDying) {
			isDying = true;
			TaskManager.submit(new PlayerDeathTask(this));
		}
	}

	private int bossPoints;
	private int drBoost;
	private int dmgBoost;
	private int asCash;
	private int jailKills;

	private int passwordPlayer = 0;

	public int getPasswordPlayer() {
		return passwordPlayer;
	}

	public void setPasswordPlayer(int passwordPlayer) {
		this.passwordPlayer = passwordPlayer;
	}

	public void addBossPoints(int amount) {
		setBossPoints(getBossPoints() + amount);
		sendMessage("<img=0> You now have @red@" + getBossPoints() + " Boss Points!");
	}

	public void addDrBoost(int amount) {
		setDrBoost(getDrBoost() + amount);
		sendMessage("<img=0> You now have @red@" + getDrBoost() + " droprate!");
	}

	public void addDmgBoost(int amount) {
		setDmgBoost(getDmgBoost() + amount);
		sendMessage("<img=0> You now have now added @red@" + getDmgBoost() + " damage boost!");
	}

	public int pokemonRaidsOpened = 0;
	public int MkRaidsOpened = 0;
	public int animeRaidsOpened = 0;
	public int silverRaidsOpened = 0;
	public int goldRaidsOpened = 0;
	public int rubyRaidsOpened = 0;
	public int platRaidsOpened = 0;
	public int diamondRaidsOpened = 0;
	public int darkDementionRaidsOpened = 0;
	public int dragonstoneRaidsOpened = 0;

	public int getDragonstoneRaidsOpened() {
		return dragonstoneRaidsOpened;
	}

	public void setDragonstoneRaidsOpened(int amount) {
		dragonstoneRaidsOpened = amount;
	}

	public int getDarkDementionRaidsOpened() {
		return darkDementionRaidsOpened;
	}

	public void setDarkDementionRaidsOpened(int amount) {
		darkDementionRaidsOpened = amount;
	}

	public int getSilverRaidsOpened() {
		return silverRaidsOpened;
	}

	public void setSilverRaidsOpened(int amount) {
		silverRaidsOpened = amount;
	}

	public int getRubyRaidsOpened() {
		return rubyRaidsOpened;
	}

	public void setRubyRaidsOpened(int amount) {
		rubyRaidsOpened = amount;
	}

	public int getDiamondRaidsOpened() {
		return diamondRaidsOpened;
	}

	public void setDiamondRaidsOpened(int amount) {
		diamondRaidsOpened = amount;
	}

	public int getPlatRaidsOpened() {
		return platRaidsOpened;
	}

	public void setPlatRaidsOpened(int amount) {
		platRaidsOpened = amount;
	}

	public int getGoldRaidsOpened() {
		return goldRaidsOpened;
	}

	public void setGoldRaidsOpened(int amount) {
		goldRaidsOpened = amount;
	}

	public int getAnimeRaidsOpened() {
		return animeRaidsOpened;
	}

	public void setAnimeRaidsOpened(int amount) {
		animeRaidsOpened = amount;
	}

	public int getPokemonRaidsOpened() {
		return pokemonRaidsOpened;
	}

	public void setPokemonRaidsOpened(int amount) {
		pokemonRaidsOpened = amount;
	}

	public int getMkRaidsOpened() {
		return MkRaidsOpened;
	}

	public void setMkRaidsOpened(int amount) {
		MkRaidsOpened = amount;
	}

	public int getBossPoints() {
		return bossPoints;
	}

	public int getDrBoost() {
		return drBoost;
	}

	public int getDmgBoost() {
		return dmgBoost;
	}

	public int getascash() {
		return asCash;
	}

	public void setBossPoints(int bossPoints) {
		this.bossPoints = bossPoints;
	}

	public void setDrBoost(int drBoost) {
		this.drBoost = drBoost;
	}

	public void setDmgBoost(int dmgBoost) {
		this.dmgBoost = dmgBoost;
	}

	public void setJailAmount(int jailKills) {
		this.jailKills = jailKills;
	}

	public int getAmountJailed() {
		return jailKills;
	}

	public void setTotalAmount(int jailKills) {
		this.totalJailKills = jailKills;
	}

	public int getTotalAmountJailed() {
		return totalJailKills;
	}

	/*
	 * Variables for DropTable & Player Profiling
	 * 
	 * @author Levi Patton
	 * 
	 * @www.rune-server.org/members/auguryps
	 */
	public Player dropLogPlayer;
	public boolean dropLogOrder;
	private PlayerDropLog playerDropLog = new PlayerDropLog();
	private ProfileViewing profile = new ProfileViewing();

	/*
	 * Variables for the DropLog
	 * 
	 * @author Levi Patton
	 */
	public PacketSender getPA() {
		return getPacketSender();
	}

	public PlayerDropLog getPlayerDropLog() {
		return playerDropLog;
	}

	public ProfileViewing getProfile() {
		return profile;
	}

	public void setProfile(ProfileViewing profile) {
		this.profile = profile;
	}

	public void setPlayerDropLog(PlayerDropLog playerDropLog) {
		this.playerDropLog = playerDropLog;
	}

	@Override
	public long getConstitution() {
		return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
	}

	@Override
	public Character setConstitution(long constitution) {
		if (isDying) {
			return this;
		}
		skillManager.setCurrentLevel(Skill.CONSTITUTION, (int) constitution);
		packetSender.sendSkill(Skill.CONSTITUTION);
		if (getConstitution() <= 0 && !isDying) {
			appendDeath();
		}
		return this;
	}

	@Override
	public void heal(long amount) {
		int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
		if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
			setConstitution(level);
		} else {
			setConstitution((int) (skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount));
		}
	}

	private CosmeticGear cosmeticGear = new CosmeticGear(this);

	public CosmeticGear getCosmeticGear() {
		return cosmeticGear;
	}

	private int[] cosmeticEquipment = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	public int[] getCosmeticEquipment() {
		return cosmeticEquipment;
	}

	public void setCosmeticEquipment(int index, int id) {
		cosmeticEquipment[index] = id;
	}

	public void setCosmeticEquipment(int[] cosmeticEquipment) {
		this.cosmeticEquipment = cosmeticEquipment;
	}

	public boolean x2DMG = false;

	public boolean isx2DMG() {
		return x2DMG;
	}

	public void setx2DMG(boolean x2) {
		this.x2DMG = x2;
	}

	/** SKILL TREE DAMAGE **/
	public boolean level1Damage = false;

	public void setLevel1Damage(boolean dmg) {
		this.level1Damage = dmg;
	}

	public boolean level2Damage = false;

	public void setLevel2Damage(boolean dmg) {
		this.level2Damage = dmg;
	}

	public boolean level3Damage = false;

	public void setLevel3Damage(boolean dmg) {
		this.level3Damage = dmg;
	}

	public boolean level4Damage = false;

	public void setLevel4Damage(boolean dmg) {
		this.level4Damage = dmg;
	}

	public boolean level5Damage = false;

	public void setLevel5Damage(boolean dmg) {
		this.level5Damage = dmg;
	}

	public boolean level6Damage = false;

	public void setLevel6Damage(boolean dmg) {
		this.level6Damage = dmg;
	}

	public boolean level7Damage = false;

	public void setLevel7Damage(boolean dmg) {
		this.level7Damage = dmg;
	}

	public boolean level8Damage = false;

	public void setLevel8Damage(boolean dmg) {
		this.level8Damage = dmg;
	}

	public boolean level9Damage = false;

	public void setLevel9Damage(boolean dmg) {
		this.level9Damage = dmg;
	}

	public boolean level10Damage = false;

	public void setLevel10Damage(boolean dmg) {
		this.level10Damage = dmg;
	}

	/** SKILL TREE DR **/
	public boolean level1DR = false;

	public void setLevel1DR(boolean dmg) {
		this.level1DR = dmg;
	}

	public boolean level2DR = false;

	public void setLevel2DR(boolean dmg) {
		this.level2Damage = dmg;
	}

	public boolean level3DR = false;

	public void setLevel3DR(boolean dmg) {
		this.level3Damage = dmg;
	}

	public boolean level4DR = false;

	public void setLevel4DR(boolean dmg) {
		this.level4DR = dmg;
	}

	public boolean level5DR = false;

	public void setLevel5DR(boolean dmg) {
		this.level5DR = dmg;
	}

	public boolean level6DR = false;

	public void setLevel6DR(boolean dmg) {
		this.level6DR = dmg;
	}

	public boolean level7DR = false;

	public void setLevel7DR(boolean dmg) {
		this.level7DR = dmg;
	}

	public boolean level8DR = false;

	public void setLevel8DR(boolean dmg) {
		this.level8DR = dmg;
	}

	public boolean level9DR = false;

	public void setLevel9DR(boolean dmg) {
		this.level9DR = dmg;
	}

	public boolean level10DR = false;

	public void setLevel10DR(boolean dmg) {
		this.level10DR = dmg;
	}

	/** SKILL TREE SPEED **/
	public boolean level1Speed = false;

	public void setLevel1Speed(boolean dmg) {
		this.level1Speed = dmg;
	}

	public boolean level2Speed = false;

	public void setLevel2Speed(boolean dmg) {
		this.level2Speed = dmg;
	}

	public boolean level3Speed = false;

	public void setLevel3Speed(boolean dmg) {
		this.level3Speed = dmg;
	}

	public boolean level4Speed = false;

	public void setLevel4Speed(boolean dmg) {
		this.level4Speed = dmg;
	}

	public boolean level5Speed = false;

	public void setLevel5Speed(boolean dmg) {
		this.level5Speed = dmg;
	}

	public boolean level6Speed = false;

	public void setLevel6Speed(boolean dmg) {
		this.level6Speed = dmg;
	}

	public boolean level7Speed = false;

	public void setLevel7Speed(boolean dmg) {
		this.level7Speed = dmg;
	}

	public boolean level8Speed = false;

	public void setLevel8Speed(boolean dmg) {
		this.level8Speed = dmg;
	}

	public boolean level9Speed = false;

	public void setLevel9Speed(boolean dmg) {
		this.level9Speed = dmg;
	}

	public boolean level10Speed = false;

	public void setLevel10Speed(boolean dmg) {
		this.level10Speed = dmg;
	}

	private boolean viewingCosmeticTab;

	public boolean isViewingCosmeticTab() {
		return viewingCosmeticTab;
	}

	public void setViewingCosmeticTab(boolean viewingCosmeticTab) {
		this.viewingCosmeticTab = viewingCosmeticTab;
	}

	private int praiseTime;

	private int cleansingTime;

	public int getCleansingTime() {
		return cleansingTime;
	}

	public void setCleansingTime(int cleansingTime) {
		this.cleansingTime = cleansingTime;
	}

	public void incrementCleansingTime(int cleansingTime) {
		this.cleansingTime += cleansingTime;
	}

	public void decrementCleansingTime(int cleansingTime) {
		this.cleansingTime -= cleansingTime;
	}

	public int getPraiseTime() {
		return praiseTime;
	}

	public void setPraiseTime(int praiseTime) {
		this.praiseTime = praiseTime;
	}

	public boolean inDragon = false;

	public boolean attackable = true;

	public void incrementPraiseTime(int praiseTime) {
		this.praiseTime += praiseTime;
	}

	public void decrementPraiseTime(int praiseTime) {
		this.praiseTime -= praiseTime;
	}

	@Override
	public int getBaseAttack(CombatType type) {
		if (type == CombatType.RANGED) {
			return skillManager.getCurrentLevel(Skill.RANGED);
		} else if (type == CombatType.MAGIC) {
			return skillManager.getCurrentLevel(Skill.MAGIC);
		}
		return skillManager.getCurrentLevel(Skill.ATTACK);
	}

	@Override
	public int getBaseDefence(CombatType type) {
		if (type == CombatType.MAGIC) {
			return skillManager.getCurrentLevel(Skill.MAGIC);
		}
		return skillManager.getCurrentLevel(Skill.DEFENCE);
	}

	@Override
	public int getAttackSpeed() {
		int speed = weapon.getSpeed();
		String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
		if (getCurrentlyCasting() != null) {
			if (getCurrentlyCasting() == CombatSpells.ADAM_SPELL.getSpell()) {
				return 1;
			} else if (getCurrentlyCasting() == CombatSpells.UNHOLYSPELL.getSpell()) {
				return -4;
			} else if (getCurrentlyCasting() == CombatSpells.REAPERSSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.RAGINGSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.WINDSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.TRIBRIDSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.DEADLYSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.MAGICSPELL.getSpell()) {
				return -5;
			} else if (getCurrentlyCasting() == CombatSpells.TINFUSED.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.THANOSI.getSpell()) {
				return -8;
			} else if (getCurrentlyCasting() == CombatSpells.LORIENSTAFF.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.SOULSTAFF.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.RAZERSPELL.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.MDMASPELL.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.DZONESSPELL.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.FRANKYSPELL.getSpell()) {
				return -7;
			} else if (getCurrentlyCasting() == CombatSpells.MINI1SPELL.getSpell()) {
				return -1;

			} else if (getCurrentlyCasting() == CombatSpells.MINI2SPELL.getSpell()) {
				return -2;

			} else if (getCurrentlyCasting() == CombatSpells.MINI3SPELL.getSpell()) {
				return -3;

			} else if (getCurrentlyCasting() == CombatSpells.MINI4SPELL.getSpell()) {
				return -3;

			} else if (getCurrentlyCasting() == CombatSpells.MINI5SPELL.getSpell()) {
				return -4;

			} else if (getCurrentlyCasting() == CombatSpells.MINI6SPELL.getSpell()) {
				return -4;

			} else if (getCurrentlyCasting() == CombatSpells.MINI7SPELL.getSpell()) {
				return -5;

			} else if (getCurrentlyCasting() == CombatSpells.MINI8SPELL.getSpell()) {
				return -5;

			} else if (getCurrentlyCasting() == CombatSpells.MINI9SPELL.getSpell()) {
				return -6;

			} else if (getCurrentlyCasting() == CombatSpells.MINI10SPELL.getSpell()) {
				return -7;
			}

			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 20565) {
				return 4;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 20656) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13080) {
				return -5;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 21022) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 11653) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 10502) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 7082) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 1005) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 15485) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 2545) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 18991) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 19072) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 19077) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 11651) {
				return 1;
			}
			if (equipment.get(Equipment.WEAPON_SLOT).getId() == 20134) {
				return 1;
			}

			return 5;
		}
		int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId == 1419) {
			speed = 2;

		}
		if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.LONGBOW_RAPID
				|| weaponId == 6522 && fightType == FightType.KNIFE_RAPID || weapon.contains("rapier")) {
			if (weaponId != 20519) {
				speed--;
			}
			if (weaponId != 20520) {
				speed--;
			}

		} else if (weaponId != 6522 && weaponId != 15241
				&& (fightType == FightType.SHORTBOW_RAPID || fightType == FightType.DART_RAPID
						|| fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID
						|| fightType == FightType.JAVELIN_RAPID)
				|| weaponId == 11730) {
			speed -= 2;
		}
		int weaponId1 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId1 == 13045) {
			speed = 4;
		}
		int weaponId2 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId2 == 20134) {
			speed = 1;
		}
		int weaponId3 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId3 == 20160) {
			speed = 2;
		}
		int weaponId7 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId7 == 11658) {
			speed = 1;
		}
		int weaponId4 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId4 == 14018) {
			speed = 1;
		}
		int weaponId5 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId5 == 18856) {
			speed = 3;
		}
		int weaponId6 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId6 == 20061) {
			speed = 2;
		}
		int weaponId8 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId8 == 7080) {
			speed = 1;
		}
		int weaponId9 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId9 == 19920) {
			speed = 2;
		}
		int weaponId10 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId10 == 8871) {
			speed = -1;
		}
		int weaponId11 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId11 == 10504) {
			speed = 0;
		}
		int weaponId12 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId12 == 18373) {
			speed = 2;
		}
		int weaponId13 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId13 == 20519) {
			speed = 0;
		}
		int weaponId14 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId14 == 20656) {
			speed = 0;
		}
		int weaponId15 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId15 == 20521) {
			speed = 1;
		}

		int weaponId16 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId16 == 11653) {
			speed = 2;
		}
		int weaponId17 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId17 == 7082) {
			speed = 1;
		}
		int weaponId19 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId19 == 10502) {
			speed = 0;
		}
		int weaponId18 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId18 == 938) {
			speed = 0;
		}
		int weaponId20 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId20 == 940) {
			speed = 1;
		}

		int weaponId22 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId22 == 19915) {
			speed = 1;
		}
		int weaponId23 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId23 == 7081) {
			speed = 1;
		}
		int weaponId24 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId24 == 20520) {
			speed = 1;
		}
		int weaponId25 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId25 == 13047) {
			speed = 0;
		}
		int weaponId26 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId26 == 20146) {
			speed = 0;
		}
		int weaponId27 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId27 == 997) {
			speed = 0;
		}
		int weaponId28 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId28 == 19918) {
			speed = 1;
		}
		int weaponId29 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId29 == 937) {
			speed = 1;
		}
		int weaponId30 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId30 == 19919) {
			speed = 1;
		}
		int weaponId31 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId31 == 1005) {
			speed = 1;
		}
		int weaponId32 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId32 == 15485) {
			speed = 1;
		}
		int weaponId33 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId33 == 11651) {
			speed = 0;
		}
		int weaponId34 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId34 == 10505) {
			speed = 2;
		}
		int weaponId35 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId35 == 2545) {
			speed = 1;
		}
		int weaponId36 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId36 == 19921) {
			speed = 1;
		}
		int weaponId37 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId37 == 18991) {
			speed = 0;
		}
		int weaponId38 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId38 == 19914) {
			speed = 5;
		}
		int weaponId39 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId39 == 19913) {
			speed = 4;
		}
		int weaponId40 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId40 == 19061) {
			speed = 0;
		}
		int weaponId41 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId41 == 19054) {
			speed = 1;
		}
		int weaponId42 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId42 == 19072) {
			speed = 2;
		}
		int weaponId43 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId43 == 19077) {
			speed = 0;
		}
		int weaponId44 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId44 == 19082) {
			speed = 0;
		}
		int weaponId45 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId45 == 19096) {
			speed = 0;
		}
		int weaponId46 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId46 == 20522) {
			speed = 1;
		}
		int weaponId48 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId48 == 11650) {
			speed = 1;
		}
		int weaponId49 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId49 == 16137) {
			speed = 3;
		}
		int weaponId50 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId50 == 18856) {
			speed = 2;
		}
		int weaponId51 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId51 == 10503) {
			speed = 1;
		}
		int weaponId52 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId52 == 11654) {
			speed = 2;
		}
		int weaponId53 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId53 == 11659) {
			speed = 2;
		}
		int weaponId54 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId54 == 1013) {
			speed = -3;
		}
		int weaponId57 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId57 == 21034) {
			speed = -4;
		}
		int weaponId55 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId55 == 19084) {
			speed = 0;
		}
		int weaponId56 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId56 == 19085) {
			speed = -2;
		}
		int weaponId58 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId58 == 21022) {
			speed = -4;
		}
		int weaponId59 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId59 == 21026) {
			speed = -5;
		}
		int weaponId60 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId60 == 21039) {
			speed = -4;
		}
		int weaponId61 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId61 == 21081) {
			speed = -3;
		}
		int weaponId62 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId62 == 18866) {
			speed = -4;
		}
		int weaponId63 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId63 == 21071) {
			speed = -5;
		}
		int weaponId64 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId64 == 21063) {
			speed = -6;
		}
		int weaponId65 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId65 == 21062) {
			speed = -6;
		}
		int weaponId66 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId66 == 21064) {
			speed = -6;
		}
		int weaponId67 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId67 == 19104) {
			speed = -6;
		}
		int weaponId68 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId68 == 19116) {
			speed = -6;
		}
		int weaponId69 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId69 == 19107) {
			speed = -6;
		}
		int weaponId70 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId70 == 2764) {
			speed = -4;
		}
		int weaponId71 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId71 == 2756) {
			speed = -5;
		}
		int weaponId72 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId72 == 2789) {
			speed = -5;
		}
		int weaponId73 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId73 == 2787) {
			speed = -5;
		}
		int weaponId74 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId74 == 2796) {
			speed = -7;
		}
		int weaponId75 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId75 == 2833) {
			speed = -1;
		}
		int weaponId76 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId76 == 2835) {
			speed = -2;
		}
		int weaponId77 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId77 == 2837) {
			speed = -3;
		}
		int weaponId78 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId78 == 2839) {
			speed = -4;
		}
		int weaponId79 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId79 == 2841) {
			speed = -4;
		}
		int weaponId80 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId80 == 2842) {
			speed = -5;
		}
		int weaponId81 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId81 == 2843) {
			speed = -5;
		}
		int weaponId82 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId82 == 2844) {
			speed = -6;
		}
		int weaponId83 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId83 == 2847) {
			speed = -6;
		}
		int weaponId84 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId84 == 2846) {
			speed = -7;
		}
		int weaponId85 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId85 == 18953) {
			speed = -5;
		}
		int weaponId86 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId86 == 2677) {
			speed = -5;
		}
		int weaponId87 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId87 == 18966) {
			speed = -5;
		}
		int weaponId88 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId88 == 2680) {
			speed = -5;
		}
		int weaponId89 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId89 == 2691) {
			speed = -1;
		}
		int weaponId90 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId90 == 2692) {
			speed = -1;
		}
		int weaponId91 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId91 == 2693) {
			speed = -2;
		}
		int weaponId92 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId92 == 2694) {
			speed = -2;
		}
		int weaponId93 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId93 == 2695) {
			speed = -3;
		}
		int weaponId94 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId94 == 2696) {
			speed = -3;
		}
		int weaponId95 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId95 == 2697) {
			speed = -4;
		}
		int weaponId96 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId96 == 2698) {
			speed = -4;
		}
		int weaponId97 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId97 == 2699) {
			speed = -6;
		}
		int weaponId98 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId98 == 2700) {
			speed = -7;
		}
		int weaponId99 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId99 == 2681) {
			speed = -1;
		}
		int weaponId100 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId100 == 2682) {
			speed = -1;
		}
		int weaponId101 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId101 == 2683) {
			speed = -2;
		}
		int weaponId102 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId102 == 16401) {
			speed = -7;
		}
		int weaponId103 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId103 == 2685) {
			speed = -3;
		}
		int weaponId104 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId104 == 2686) {
			speed = -3;
		}
		int weaponId105 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId105 == 2687) {
			speed = -4;
		}
		int weaponId106 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId106 == 2688) {
			speed = -5;
		}
		int weaponId107 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId107 == 2689) {
			speed = -6;
		}
		int weaponId108 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId108 == 2690) {
			speed = -7;
		}
		int weaponId109 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId109 == 13080) {
			speed = -6;
		}
		int weaponId110 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId110 == 13081) {
			speed = -7;
		}
		int weaponId111 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId111 == 2708) {
			speed = -3;
		}
		int weaponId112 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId112 == 13083) {
			speed = -7;
		}
		int weaponId113 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId113 == 13084) {
			speed = -7;
		}
		int weaponId114 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId114 == 2853) {
			speed = -7;
		}
		int weaponId115 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId115 == 2744) {
			speed = -6;
		}
		int weaponId116 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId116 == 2746) {
			speed = -7;
		}
		int weaponId117 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId117 == 2722) {
			speed = -5;
		}
		int weaponId118 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId118 == 3510) {
			speed = -7;
		}
		int weaponId119 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId119 == 3495) {
			speed = -7;
		}
		int weaponId120 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId120 == 3525) {
			speed = -7;
		}
		int weaponId121 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId121 == 3516) {
			speed = -7;
		}
		int weaponId122 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId122 == 3498) {
			speed = -7;
		}
		int weaponId123 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId123 == 13023) {
			speed = -7;
		}
		int weaponId125 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId125 == 21046) {
			speed = -7;
		}
		int weaponId124 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId124 == 3497) {
			speed = -7;
		}
		int weaponId128 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId128 == 8670) {
			speed = -7;
		}
		int weaponId129 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId129 == 19035) {
			speed = -7;
		}
		int weaponId126 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId126 == 10946) {
			speed = -5;
		}
		int weaponId127 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId127 == 21014) {
			speed = 3;
		}
		int weaponId130 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId130 == 15426) {
			speed = -5;
		}

		int weaponId131 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId131 == 4080) {
			speed = -7;
		}
		int weaponId132 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId132 == 19078) {
			speed = -7;
		}
		int weaponId133 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId133 == 19012) {
			speed = -7;
		}
		int weaponId134 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId134 == 7083) {
			speed = -7;
		}
		
		int weaponId135 = equipment.get(Equipment.WEAPON_SLOT).getId();
		if (weaponId135 == 2732) {
			speed = -5;
		}

		return speed;
		// return DesolaceFormulas.getAttackDelay(this);
	}

	public int clue1Amount;
	public int clue2Amount;
	public int clue3Amount;
	public int clueLevel;
	public Item[] puzzleStoredItems;
	public int sextantGlobalPiece;
	public double sextantBarDegree;
	public int rotationFactor;
	public int sextantLandScapeCoords;
	private boolean accountCompromised;
	public int sextantSunCoords;
	private String lastHostAddress;

	public String getLastHostAddress() {
		return lastHostAddress;
	}

	public Player setLastHostAddress(String lastHostAddress) {
		this.lastHostAddress = lastHostAddress;
		return this;
	}

	private Bank bank = new Bank(this);

	public Bank getBank() {
		return bank;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Player)) {
			return false;
		}

		Player p = (Player) o;
		return p.getIndex() == getIndex() || p.getUsername().equals(username);
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public void poisonVictim(Character victim, CombatType type) {
		if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
				|| weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
			CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
		} else if (type == CombatType.RANGED) {
			CombatFactory.poisonEntity(victim,
					CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
		}
	}

	@Override
	public void venomVictim(Character victim, CombatType type) {
		int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
		int helmet = equipment.get(Equipment.HEAD_SLOT).getId();
		if ((type == CombatType.MAGIC && weaponId == 12284)) {
			CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
		}
		if ((type == CombatType.RANGED && weaponId == 12926)) {
			CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.WEAPON_SLOT)));
		} else if (helmet == 12282) {
			CombatFactory.venomEntity(victim, CombatVenomData.getVenomType(equipment.get(Equipment.HEAD_SLOT)));
		}
	}

	@Override
	public CombatStrategy determineStrategy() {
		if (specialActivated && castSpell == null) {

			if (combatSpecial.getCombatType() == CombatType.MELEE) {
				return CombatStrategies.getDefaultMeleeStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.RANGED) {
				setRangedWeaponData(RangedWeaponData.getData(this));
				return CombatStrategies.getDefaultRangedStrategy();
			} else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
				return CombatStrategies.getDefaultMagicStrategy();
			}
		}

		if (CustomMagicStaff.checkCustomStaff(this)) {
			CustomMagicStaff.handleCustomStaff(this);
			this.setCastSpell(CustomMagicStaff.CustomStaff
					.getSpellForWeapon(this.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()));
			return CombatStrategies.getDefaultMagicStrategy();
		}

		if (castSpell != null || autocastSpell != null
				|| this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13058) {
			return CombatStrategies.getDefaultMagicStrategy();
		}

		RangedWeaponData data = RangedWeaponData.getData(this);
		if (data != null) {
			setRangedWeaponData(data);
			return CombatStrategies.getDefaultRangedStrategy();
		}

		return CombatStrategies.getDefaultMeleeStrategy();
	}

	public void process() {
		process.sequence();
	}

	public void dispose() {
		save();
		packetSender.sendLogout();
	}

	public void save() {
		if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
			return;
		}
		PlayerSaving.save(this);
	}

	public boolean logout() {
		if (getCombatBuilder().isBeingAttacked()) {
			getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
			return false;
		}

		if (getInstanceManager().inInstance == true) {
			getPacketSender().sendMessage("You cannot log out at the moment,do ::leave then try again.");
			return false;
		}

		if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
			getPacketSender().sendMessage("You cannot log out at the moment.");
			return false;
		}
		return true;

	}

	public void restart() {
		setFreezeDelay(0);
		setOverloadPotionTimer(0);
		setPrayerRenewalPotionTimer(0);
		setSpecialPercentage(100);
		setSpecialActivated(false);
		CombatSpecial.updateBar(this);
		setHasVengeance(false);
		setSkullTimer(0);
		setSkullIcon(0);
		setTeleblockTimer(0);
		setPoisonDamage(0);
		setVenomDamage(0);
		setStaffOfLightEffect(0);
		performAnimation(new Animation(65535));
		getPacketSender().sendConstitutionOrbPoison(false);
		getPacketSender().sendConstitutionOrbVenom(false);
		WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
		PrayerHandler.deactivateAll(this);
		CurseHandler.deactivateAll(this);
		getEquipment().refreshItems();
		getInventory().refreshItems();
		for (Skill skill : Skill.values()) {
			getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
		}
		setRunEnergy(100);
		setDying(false);
		getMovementQueue().setLockMovement(false).reset();
		getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public boolean busy() {
		return interfaceId > 0 || isBanking || dicing.inDice || shopping || trading.inTrade() || dueling.inDuelScreen
				|| isResting;
	}

	public boolean isAccountCompromised() {
		return accountCompromised;
	}

	public void setAccountCompromised(boolean accountCompromised) {
		this.accountCompromised = accountCompromised;
	}

	private String username;
	private String password;
	private String serial_number;
	private String super_serial_number;
	private String emailAddress;
	private String hostAddress;
	private String clanChatName;
	private HouseLocation houseLocation;
	private HouseTheme houseTheme;
	private Long longUsername;
	private long moneyInPouch;
	private long totalPlayTime;
	private final Stopwatch sqlTimer = new Stopwatch();
	private final Stopwatch protpraydelay = new Stopwatch().headStart(9000);
	private final Stopwatch foodTimer = new Stopwatch();
	private final Stopwatch potionTimer = new Stopwatch();
	private final Stopwatch lastRunRecovery = new Stopwatch();
	private final Stopwatch clickDelay = new Stopwatch();
	private final Stopwatch lastItemPickup = new Stopwatch();
	private final Stopwatch lastYell = new Stopwatch();
	private final Stopwatch lastSql = new Stopwatch();
	private final Stopwatch krakenRespawn = new Stopwatch();
	private final Stopwatch lastVengeance = new Stopwatch();
	private final Stopwatch emoteDelay = new Stopwatch();
	private final Stopwatch specialRestoreTimer = new Stopwatch();
	private final Stopwatch lastSummon = new Stopwatch();
	private final Stopwatch recordedLogin = new Stopwatch();
	private final Stopwatch creationDate = new Stopwatch();
	private final Stopwatch tolerance = new Stopwatch();
	private final Stopwatch lougoutTimer = new Stopwatch();
	private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
	private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
	private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
	private ArrayList<Portal> housePortals = new ArrayList<>();
	private final List<Player> localPlayers = new LinkedList<Player>();
	private final List<NPC> localNpcs = new LinkedList<NPC>();
	private PlayerSession session;
	private final PlayerProcess process = new PlayerProcess(this);
	private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
	private final MinigameAttributes minigameAttributes = new MinigameAttributes();
	private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
	private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
	private CharacterAnimations characterAnimations = new CharacterAnimations();
	private final BonusManager bonusManager = new BonusManager();
	private final PointsHandler pointsHandler = new PointsHandler(this);
	private final PacketSender packetSender = new PacketSender(this);
	private final Appearance appearance = new Appearance(this);
	private final FrameUpdater frameUpdater = new FrameUpdater();
	private PlayerRights rights = PlayerRights.PLAYER;
	private SkillManager skillManager = new SkillManager(this);
	private PlayerRelations relations = new PlayerRelations(this);
	private ChatMessage chatMessages = new ChatMessage();
	private Inventory inventory = new Inventory(this);
	private Equipment equipment = new Equipment(this);
	private PriceChecker priceChecker = new PriceChecker(this);
	private Trading trading = new Trading(this);
	private Dueling dueling = new Dueling(this);
	private Slayer slayer = new Slayer(this);
	private Farming farming = new Farming(this);
	private Summoning summoning = new Summoning(this);
	private Bank[] bankTabs = new Bank[9];
	private Bank[] tempBankTabs = null;

	public Bank[] getTempBankTabs() {
		return tempBankTabs;
	}

	public void setTempBankTabs(Bank[] tempBankTabs) {
		this.tempBankTabs = tempBankTabs;
	}

	private Room[][][] houseRooms = new Room[5][13][13];
	private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
	private GameMode gameMode = GameMode.NORMAL;
	private CombatType lastCombatType = CombatType.MELEE;
	private FightType fightType = FightType.UNARMED_PUNCH;
	private Prayerbook prayerbook = Prayerbook.NORMAL;
	private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
	private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;
	private ClanChat currentClanChat;
	private Input inputHandling;
	private WalkToTask walkToTask;
	private Shop shop;
	private GameObject interactingObject;
	private Item interactingItem;
	private Dialogue dialogue;
	private DwarfCannon cannon;
	private CombatSpell autocastSpell, castSpell, previousCastSpell;
	private RangedWeaponData rangedWeaponData;
	private CombatSpecial combatSpecial;
	private WeaponInterface weapon;
	private Item untradeableDropItem;
	private Object[] usableObject;
	private GrandExchangeSlot[] grandExchangeSlots = new GrandExchangeSlot[6];
	private Task currentTask;
	private Position resetPosition;
	private Pouch selectedPouch;
	private BlowpipeLoading blowpipeLoading = new BlowpipeLoading(this);
	public int destination = 0;
	public int lastClickedTab = 0;
	public int prayblock = 0;
	private int[] brawlerCharges = new int[9];
	private int[] forceMovement = new int[7];
	private int[] leechedBonuses = new int[7];
	private int[] ores = new int[2];
	private int[] constructionCoords;
	private int recoilCharges;
	private int runEnergy = 100;
	private int currentBankTab;
	private int interfaceId, walkableInterfaceId, multiIcon;
	private int dialogueActionId;
	private int overloadPotionTimer, prayerRenewalPotionTimer;
	private int fireImmunity, fireDamageModifier;
	private int amountDonated;
	private int totalJailKills;
	private int wildernessLevel;
	private int fireAmmo;
	private int specialPercentage = 100;
	private int skullIcon = -1, skullTimer;
	private int teleblockTimer;
	private int dragonFireImmunity;
	private int poisonImmunity;
	public int scrollAmount;
	public int numOfScrolls;
	private int shadowState;
	private int effigy;
	public int gameType;
	private int dfsCharges;
	private int playerViewingIndex;
	private int staffOfLightEffect;
	private int minutesBonusExp = -1;
	private int selectedGeSlot = -1;
	private int selectedGeItem = -1;
	private int geQuantity;
	private int gePricePerItem;
	private int selectedSkillingItem;
	private int currentBookPage;
	private int storedRuneEssence, storedPureEssence;
	private int trapsLaid;
	private int skillAnimation;
	private int houseServant;
	private int houseServantCharges;
	private int servantItemFetch;
	private int portalSelected;
	private int constructionInterface;
	private int buildFurnitureId;
	private int buildFurnitureX;
	private int buildFurnitureY;
	private int combatRingType;
	private boolean unlockedLoyaltyTitles[] = new boolean[12];
	private boolean[] crossedObstacles = new boolean[7];
	private boolean processFarming;
	private boolean crossingObstacle;
	private boolean targeted;
	private boolean isBanking, noteWithdrawal, swapMode;
	private boolean regionChange, allowRegionChangePacket;
	private boolean isDying;
	private boolean isRunning = true, isResting;
	private boolean experienceLocked;
	private boolean clientExitTaskActive;
	private boolean drainingPrayer;
	private boolean shopping;
	private boolean settingUpCannon;
	private boolean hasVengeance;
	private boolean killsTrackerOpen;
	private boolean acceptingAid;
	private boolean autoRetaliate;
	private boolean autocast;
	public boolean boxAlertEnabled = true;
	private boolean specialActivated;
	public boolean playerInstanced;
	private boolean isCoughing;
	private boolean playerLocked;
	private boolean recoveringSpecialAttack;
	private boolean soundsActive, musicActive;
	private boolean newPlayer;
	private boolean passPlayer;
	private boolean openBank;
	private boolean inActive;
	public int timeOnline;
	private boolean inConstructionDungeon;
	private boolean isBuildingMode;
	private boolean voteMessageSent;
	private boolean receivedStarter;

	public RaidsConnector getRaidsOne() {
		return raids3;
	}

	public RaidsPartyConnector getRaidsPartyConnector() {
		return raidsPartyConnector;
	}

	public void setRaidsOne(RaidsConnector raids3) {
		this.raids3 = raids3;
	}

	/*
	 * Getters & Setters
	 */
	public PlayerSession getSession() {
		return session;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public PriceChecker getPriceChecker() {
		return priceChecker;
	}

	private RaidsParty raidsParty;

	public RaidsParty getRaidsParty() {
		return raidsParty;
	}

	public void setRaidsParty(RaidsParty raidsParty) {
		this.raidsParty = raidsParty;
	}

	/*
	 * Getters and setters
	 */
	public String getUsername() {
		return username;
	}

	public Player setUsername(String username) {
		this.username = username;
		return this;
	}

	public Long getLongUsername() {
		return longUsername;
	}

	public Player setLongUsername(Long longUsername) {
		this.longUsername = longUsername;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String address) {
		this.emailAddress = address;
	}

	public Player setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public Player setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
		return this;
	}

	public String getSerialNumber() {
		return serial_number;
	}

	public String getSuperSerialNumber() {
		return super_serial_number;
	}

	public Player setSerialNumber(String serial_number) {
		this.serial_number = serial_number;
		return this;
	}

	public Player setSuperSerialNumber(String super_serial_number) {
		this.super_serial_number = super_serial_number;
		return this;
	}

	public FrameUpdater getFrameUpdater() {
		return this.frameUpdater;
	}

	public PlayerRights getRights() {
		return rights;
	}

	public Player setRights(PlayerRights rights) {
		this.rights = rights;
		return this;
	}

	public ChatMessage getChatMessages() {
		return chatMessages;
	}

	public PacketSender getPacketSender() {
		return packetSender;
	}

	private final BestInSlotInterface bis = new BestInSlotInterface(this);

	public BestInSlotInterface getBis() {
		return bis;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public PlayerRelations getRelations() {
		return relations;
	}

	public PlayerKillingAttributes getPlayerKillingAttributes() {
		return playerKillingAttributes;
	}

	public PointsHandler getPointsHandler() {
		return pointsHandler;
	}

	public boolean isImmuneToDragonFire() {
		return dragonFireImmunity > 0;
	}

	public int getDragonFireImmunity() {
		return dragonFireImmunity;
	}

	public void setDragonFireImmunity(int dragonFireImmunity) {
		this.dragonFireImmunity = dragonFireImmunity;
	}

	public void incrementDragonFireImmunity(int amount) {
		dragonFireImmunity += amount;
	}

	public void decrementDragonFireImmunity(int amount) {
		dragonFireImmunity -= amount;
	}

	public int getPoisonImmunity() {
		return poisonImmunity;
	}

	public void setPoisonImmunity(int poisonImmunity) {
		this.poisonImmunity = poisonImmunity;
	}

	public int getScrollAmount() {
		return scrollAmount;
	}

	public int getScrollNum() {
		return numOfScrolls;
	}

	public void setScrollAmount(int scrollAmount) {
		this.scrollAmount = scrollAmount;
	}

	public void setScrollNum(int numOfScrolls) {
		this.numOfScrolls = numOfScrolls;
	}

	public boolean reSizeable = false;

	public void setVenomImmunity(int venomImmunity) {
		this.venomImmunity = venomImmunity;
	}

	public void incrementPoisonImmunity(int amount) {
		poisonImmunity += amount;
	}

	public void decrementPoisonImmunity(int amount) {
		poisonImmunity -= amount;
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	/**
	 * @return the castSpell
	 */
	public CombatSpell getCastSpell() {
		return castSpell;
	}

	/**
	 * @param castSpell the castSpell to set
	 */
	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}

	public CombatSpell getPreviousCastSpell() {
		return previousCastSpell;
	}

	public void setPreviousCastSpell(CombatSpell previousCastSpell) {
		this.previousCastSpell = previousCastSpell;
	}

	/**
	 * @return the autocast
	 */
	public boolean isAutocast() {
		return autocast;
	}

	/**
	 * @param autocast the autocast to set
	 */
	public void setAutocast(boolean autocast) {
		this.autocast = autocast;
	}

	/**
	 * @return the skullTimer
	 */
	public int getSkullTimer() {
		return skullTimer;
	}

	/**
	 * @param skullTimer the skullTimer to set
	 */
	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public void decrementSkullTimer() {
		skullTimer -= 50;
	}

	/**
	 * @return the skullIcon
	 */
	public int getSkullIcon() {
		return skullIcon;
	}

	/**
	 * @param skullIcon the skullIcon to set
	 */
	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}

	/**
	 * @return the teleblockTimer
	 */
	public int getTeleblockTimer() {
		return teleblockTimer;
	}

	private final CopyOnWriteArrayList<NPC> npc_faces_updated = new CopyOnWriteArrayList<NPC>();

	public CopyOnWriteArrayList<NPC> getNpcFacesUpdated() {
		return npc_faces_updated;
	}

	/**
	 * @param teleblockTimer the teleblockTimer to set
	 */
	public void setTeleblockTimer(int teleblockTimer) {
		this.teleblockTimer = teleblockTimer;
	}

	public void decrementTeleblockTimer() {
		teleblockTimer--;
	}

	/**
	 * @return the autocastSpell
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}

	/**
	 * @param autocastSpell the autocastSpell to set
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
	}

	/**
	 * @return the specialPercentage
	 */
	public int getSpecialPercentage() {
		return specialPercentage;
	}

	/**
	 * @param specialPercentage the specialPercentage to set
	 */
	public void setSpecialPercentage(int specialPercentage) {
		this.specialPercentage = specialPercentage;
	}

	/**
	 * @return the fireAmmo
	 */
	public int getFireAmmo() {
		return fireAmmo;
	}

	/**
	 * @param fireAmmo the fireAmmo to set
	 */
	public void setFireAmmo(int fireAmmo) {
		this.fireAmmo = fireAmmo;
	}

	public int getWildernessLevel() {
		return wildernessLevel;
	}

	public void setWildernessLevel(int wildernessLevel) {
		this.wildernessLevel = wildernessLevel;
	}

	/**
	 * @return the combatSpecial
	 */
	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}

	/**
	 * @param combatSpecial the combatSpecial to set
	 */
	public void setCombatSpecial(CombatSpecial combatSpecial) {
		this.combatSpecial = combatSpecial;
	}

	/**
	 * @return the specialActivated
	 */
	public boolean isSpecialActivated() {
		return specialActivated;
	}

	/**
	 * @param specialActivated the specialActivated to set
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}

	public void decrementSpecialPercentage(int drainAmount) {
		this.specialPercentage -= drainAmount;

		if (specialPercentage < 0) {
			specialPercentage = 0;
		}
	}

	public void incrementSpecialPercentage(int gainAmount) {
		this.specialPercentage += gainAmount;

		if (specialPercentage > 100) {
			specialPercentage = 100;
		}
	}

	/**
	 * @return the rangedAmmo
	 */
	public RangedWeaponData getRangedWeaponData() {
		return rangedWeaponData;
	}

	/**
	 * @param rangedAmmo the rangedAmmo to set
	 */
	public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
		this.rangedWeaponData = rangedWeaponData;
	}

	/**
	 * @return the weapon.
	 */
	public WeaponInterface getWeapon() {
		return weapon;
	}

	public ArrayList<Integer> walkableInterfaceList = new ArrayList<>();
	public long lastHelpRequest;
	public long lastAuthClaimed;
	public GameModes selectedGameMode;
	private boolean areCloudsSpawned;

	public void resetInterfaces() {
		walkableInterfaceList.stream().filter((i) -> !(i == 41005 || i == 41000)).forEach((i) -> {
			getPacketSender().sendWalkableInterface(i, false);
		});

		walkableInterfaceList.clear();
	}

	public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
		if (this != null && this.getPacketSender() != null) {
			if (visible) {
				if (walkableInterfaceList.contains(interfaceId)) {
					return;
				} else {
					walkableInterfaceList.add(interfaceId);
				}
			} else {
				if (!walkableInterfaceList.contains(interfaceId)) {
					return;
				} else {
					walkableInterfaceList.remove((Object) interfaceId);
				}
			}

			getPacketSender().sendWalkableInterface(interfaceId, visible);
		}
	}

	/**
	 * @param weapon the weapon to set.
	 */
	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the fightType
	 */
	public FightType getFightType() {
		return fightType;
	}

	/**
	 * @param fightType the fightType to set
	 */
	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public Bank[] getBanks() {
		return tempBankTabs == null ? bankTabs : tempBankTabs;
	}

	public Bank getBank(int index) {
		return tempBankTabs == null ? bankTabs[index] : tempBankTabs[index];
	}

	public Player setBank(int index, Bank bank) {
		if (tempBankTabs == null) {
			this.bankTabs[index] = bank;
		} else {
			this.tempBankTabs[index] = bank;
		}
		return this;
	}

	public boolean isAcceptAid() {
		return acceptingAid;
	}

	public void setAcceptAid(boolean acceptingAid) {
		this.acceptingAid = acceptingAid;
	}

	public Trading getTrading() {
		return trading;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
		return killsTracker;
	}

	public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
		return dropLog;
	}

	public void setWalkToTask(WalkToTask walkToTask) {
		this.walkToTask = walkToTask;
	}

	public WalkToTask getWalkToTask() {
		return walkToTask;
	}

	public Player setSpellbook(MagicSpellbook spellbook) {
		this.spellbook = spellbook;
		return this;
	}

	public MagicSpellbook getSpellbook() {
		return spellbook;
	}

	public Player setPrayerbook(Prayerbook prayerbook) {
		this.prayerbook = prayerbook;
		return this;
	}

	public Prayerbook getPrayerbook() {
		return prayerbook;
	}

	/**
	 * The player's local players list.
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * The player's local npcs list getter
	 */
	public List<NPC> getLocalNpcs() {
		return localNpcs;
	}

	public Player setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}

	public int getInterfaceId() {
		return this.interfaceId;
	}

	public boolean isDying() {
		return isDying;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}

	public int[] getForceMovement() {
		return forceMovement;
	}

	public Player setForceMovement(int[] forceMovement) {
		this.forceMovement = forceMovement;
		return this;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public CharacterAnimations getCharacterAnimations() {
		return characterAnimations;
	}

	/**
	 * @return the equipmentAnimation
	 */
	public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
		this.characterAnimations = equipmentAnimation.clone();
	}

	public LoyaltyTitles getLoyaltyTitle() {
		return loyaltyTitle;
	}

	public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
		this.loyaltyTitle = loyaltyTitle;
	}

	public void setWalkableInterfaceId(int interfaceId2) {
		this.walkableInterfaceId = interfaceId2;
	}

	public PlayerInteractingOption getPlayerInteractingOption() {
		return playerInteractingOption;
	}

	public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
		this.playerInteractingOption = playerInteractingOption;
		return this;
	}

	public int getMultiIcon() {
		return multiIcon;
	}

	public Player setMultiIcon(int multiIcon) {
		this.multiIcon = multiIcon;
		return this;
	}

	public int getWalkableInterfaceId() {
		return walkableInterfaceId;
	}

	public boolean soundsActive() {
		return soundsActive;
	}

	public void setSoundsActive(boolean soundsActive) {
		this.soundsActive = soundsActive;
	}

	public boolean musicActive() {
		return musicActive;
	}

	public void setMusicActive(boolean musicActive) {
		this.musicActive = musicActive;
	}

	public BonusManager getBonusManager() {
		return bonusManager;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public Player setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
		return this;
	}

	public Stopwatch getLastRunRecovery() {
		return lastRunRecovery;
	}

	public Player setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public Player setResting(boolean isResting) {
		this.isResting = isResting;
		return this;
	}

	public boolean isResting() {
		return isResting;
	}

	public void setMoneyInPouch(long moneyInPouch) {
		this.moneyInPouch = moneyInPouch;
	}

	public long getMoneyInPouch() {
		return moneyInPouch;
	}

	public int getMoneyInPouchAsInt() {
		return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
	}

	public boolean experienceLocked() {
		return experienceLocked;
	}

	public void setExperienceLocked(boolean experienceLocked) {
		this.experienceLocked = experienceLocked;
	}

	public void setClientExitTaskActive(boolean clientExitTaskActive) {
		this.clientExitTaskActive = clientExitTaskActive;
	}

	public boolean isClientExitTaskActive() {
		return clientExitTaskActive;
	}

	public Player setCurrentClanChat(ClanChat clanChat) {
		this.currentClanChat = clanChat;
		return this;
	}

	public ClanChat getCurrentClanChat() {
		return currentClanChat;
	}

	public String getClanChatName() {
		return clanChatName;
	}

	public Player setClanChatName(String clanChatName) {
		this.clanChatName = clanChatName;
		return this;
	}

	private int timer;

	public int getTimer() {
		return timer;
	}

	public void setTimer(int time) {
		this.timer = time;
	}

	public void setInputHandling(Input inputHandling) {
		this.inputHandling = inputHandling;
	}

	public Input getInputHandling() {
		return inputHandling;
	}

	public boolean isDrainingPrayer() {
		return drainingPrayer;
	}

	public void setDrainingPrayer(boolean drainingPrayer) {
		this.drainingPrayer = drainingPrayer;
	}

	public Stopwatch getClickDelay() {
		return clickDelay;
	}

	public int[] getLeechedBonuses() {
		return leechedBonuses;
	}

	public Stopwatch getLastItemPickup() {
		return lastItemPickup;
	}

	public Stopwatch getLastSummon() {
		return lastSummon;
	}

	public BankSearchAttributes getBankSearchingAttribtues() {
		return bankSearchAttributes;
	}

	public BankPinAttributes getBankPinAttributes() {
		return bankPinAttributes;
	}

	public int getCurrentBankTab() {
		return currentBankTab;
	}

	public Player setCurrentBankTab(int tab) {
		this.currentBankTab = tab;
		return this;
	}

	public boolean isBanking() {
		return isBanking;
	}

	public Player setBanking(boolean isBanking) {
		this.isBanking = isBanking;
		return this;
	}

	public void setNoteWithdrawal(boolean noteWithdrawal) {
		this.noteWithdrawal = noteWithdrawal;
	}

	public boolean withdrawAsNote() {
		return noteWithdrawal;
	}

	public void setSwapMode(boolean swapMode) {
		this.swapMode = swapMode;
	}

	public boolean swapMode() {
		return swapMode;
	}

	public boolean isShopping() {
		return shopping;
	}

	public void setShopping(boolean shopping) {
		this.shopping = shopping;
	}

	public Shop getShop() {
		return shop;
	}

	public Player setShop(Shop shop) {
		this.shop = shop;
		if (shop == null) {
			setShopping(false);
		}
		return this;
	}

	public GameObject getInteractingObject() {
		return interactingObject;
	}

	public Player setInteractingObject(GameObject interactingObject) {
		this.interactingObject = interactingObject;
		return this;
	}

	public Item getInteractingItem() {
		return interactingItem;
	}

	public void setInteractingItem(Item interactingItem) {
		this.interactingItem = interactingItem;
	}

	public Dialogue getDialogue() {
		return this.dialogue;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public int getDialogueActionId() {
		return dialogueActionId;
	}

	public void setDialogueActionId(int dialogueActionId) {
		this.dialogueActionId = dialogueActionId;
	}

	public void setSettingUpCannon(boolean settingUpCannon) {
		this.settingUpCannon = settingUpCannon;
	}

	public boolean isSettingUpCannon() {
		return settingUpCannon;
	}

	public Player setCannon(DwarfCannon cannon) {
		this.cannon = cannon;
		return this;
	}

	public DwarfCannon getCannon() {
		return cannon;
	}

	public int getOverloadPotionTimer() {
		return overloadPotionTimer;
	}

	public void setOverloadPotionTimer(int overloadPotionTimer) {
		this.overloadPotionTimer = overloadPotionTimer;
	}

	public int getPrayerRenewalPotionTimer() {
		return prayerRenewalPotionTimer;
	}

	public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
		this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
	}

	public Stopwatch getSpecialRestoreTimer() {
		return specialRestoreTimer;
	}

	public boolean[] getUnlockedLoyaltyTitles() {
		return unlockedLoyaltyTitles;
	}

	public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
		this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
	}

	public void setUnlockedLoyaltyTitle(int index) {
		unlockedLoyaltyTitles[index] = true;
	}

	public Stopwatch getEmoteDelay() {
		return emoteDelay;
	}

	public MinigameAttributes getMinigameAttributes() {
		return minigameAttributes;
	}

	public Minigame getMinigame() {
		return minigame;
	}

	public void setMinigame(Minigame minigame) {
		this.minigame = minigame;
	}

	public int getFireImmunity() {
		return fireImmunity;
	}

	public Player setFireImmunity(int fireImmunity) {
		this.fireImmunity = fireImmunity;
		return this;
	}

	public int getFireDamageModifier() {
		return fireDamageModifier;
	}

	public Player setFireDamageModifier(int fireDamageModifier) {
		this.fireDamageModifier = fireDamageModifier;
		return this;
	}

	public boolean hasVengeance() {
		return hasVengeance;
	}

	public void setHasVengeance(boolean hasVengeance) {
		this.hasVengeance = hasVengeance;
	}

	public Stopwatch getLastVengeance() {
		return lastVengeance;
	}

	public void setHouseRooms(Room[][][] houseRooms) {
		this.houseRooms = houseRooms;
	}

	public void setHousePortals(ArrayList<Portal> housePortals) {
		this.housePortals = housePortals;
	}

	/*
	 * Construction instancing Aj
	 */
	public boolean isVisible() {
		if (getLocation() == Locations.Location.CONSTRUCTION) {
			return false;
		}
		return true;
	}

	public void setHouseFurtinture(ArrayList<HouseFurniture> houseFurniture) {
		this.houseFurniture = houseFurniture;
	}

	public Stopwatch getTolerance() {
		return tolerance;
	}

	public boolean isTargeted() {
		return targeted;
	}

	public void setTargeted(boolean targeted) {
		this.targeted = targeted;
	}

	public Stopwatch getLastYell() {
		return lastYell;
	}

	public Stopwatch getLastSql() {
		return lastSql;
	}

	public int getAmountDonated() {
		return amountDonated;
	}

	public void incrementAmountDonated(int amountDonated) {
		this.amountDonated += amountDonated;
	}

	public long getTotalPlayTime() {
		return totalPlayTime;
	}

	public void setTotalPlayTime(long amount) {
		this.totalPlayTime = amount;
	}

	public Stopwatch getRecordedLogin() {
		return recordedLogin;
	}

	/*
	 * jail cell
	 */

	public Player setRegionChange(boolean regionChange) {
		this.regionChange = regionChange;
		return this;
	}

	public boolean isChangingRegion() {
		return this.regionChange;
	}

	public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
		this.allowRegionChangePacket = allowRegionChangePacket;
	}

	public boolean isAllowRegionChangePacket() {
		return allowRegionChangePacket;
	}

	public boolean isKillsTrackerOpen() {
		return killsTrackerOpen;
	}

	public void setKillsTrackerOpen(boolean killsTrackerOpen) {
		this.killsTrackerOpen = killsTrackerOpen;
	}

	public boolean isCoughing() {
		return isCoughing;
	}

	public void setCoughing(boolean isCoughing) {
		this.isCoughing = isCoughing;
	}

	public int getShadowState() {
		return shadowState;
	}

	public void setShadowState(int shadow) {
		this.shadowState = shadow;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public boolean isPlayerLocked() {
		return playerLocked;
	}

	public Player setPlayerLocked(boolean playerLocked) {
		this.playerLocked = playerLocked;
		return this;
	}

	/*
	 * Handles setting and checking of boss instancing
	 */
	public boolean isPlayerInstanced() {
		return playerInstanced;
	}

	public Player setPlayerInstanced(boolean playerInstanced) {
		this.playerInstanced = playerInstanced;
		return this;
	}

	public Stopwatch getSqlTimer() {
		return sqlTimer;
	}

	public Stopwatch getProtPrayDelay() {
		return protpraydelay;
	}

	public Stopwatch getFoodTimer() {
		return foodTimer;
	}

	public Stopwatch getPotionTimer() {
		return potionTimer;
	}

	public Item getUntradeableDropItem() {
		return untradeableDropItem;
	}

	public void setUntradeableDropItem(Item untradeableDropItem) {
		this.untradeableDropItem = untradeableDropItem;
	}

	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
	}

	public CombatType getLastCombatType() {
		return lastCombatType;
	}

	public void setLastCombatType(CombatType lastCombatType) {
		this.lastCombatType = lastCombatType;
	}

	public int getEffigy() {
		return this.effigy;
	}

	public void setEffigy(int effigy) {
		this.effigy = effigy;
	}

	public int getDfsCharges() {
		return dfsCharges;
	}

	public void incrementDfsCharges(int amount) {
		this.dfsCharges += amount;
	}

	public void setNewPlayer(boolean newPlayer) {
		this.newPlayer = newPlayer;
	}

	public boolean newPlayer() {
		return newPlayer;
	}

	public void setPassPlayer(boolean passPlayer) {
		this.passPlayer = passPlayer;
	}

	public boolean passPlayer() {
		return passPlayer;
	}

	public Stopwatch getLogoutTimer() {
		return lougoutTimer;
	}

	public Player setUsableObject(Object[] usableObject) {
		this.usableObject = usableObject;
		return this;
	}

	public Player setUsableObject(int index, Object usableObject) {
		this.usableObject[index] = usableObject;
		return this;
	}

	public Object[] getUsableObject() {
		return usableObject;
	}

	public int getPlayerViewingIndex() {
		return playerViewingIndex;
	}

	public void setPlayerViewingIndex(int playerViewingIndex) {
		this.playerViewingIndex = playerViewingIndex;
	}

	public boolean hasStaffOfLightEffect() {
		return staffOfLightEffect > 0;
	}

	public int getStaffOfLightEffect() {
		return staffOfLightEffect;
	}

	public void setStaffOfLightEffect(int staffOfLightEffect) {
		this.staffOfLightEffect = staffOfLightEffect;
	}

	public void decrementStaffOfLightEffect() {
		this.staffOfLightEffect--;
	}

	public boolean openBank() {
		return openBank;
	}

	public void setOpenBank(boolean openBank) {
		this.openBank = openBank;
	}

	public int getMinutesBonusExp() {
		return minutesBonusExp;
	}

	public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
		this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
	}

	public void setInactive(boolean inActive) {
		this.inActive = inActive;
	}

	public boolean isInActive() {
		return inActive;
	}

	public int getSelectedGeItem() {
		return selectedGeItem;
	}

	public void setSelectedGeItem(int selectedGeItem) {
		this.selectedGeItem = selectedGeItem;
	}

	public int getGeQuantity() {
		return geQuantity;
	}

	public void setGeQuantity(int geQuantity) {
		this.geQuantity = geQuantity;
	}

	public int getGePricePerItem() {
		return gePricePerItem;
	}

	public void setGePricePerItem(int gePricePerItem) {
		this.gePricePerItem = gePricePerItem;
	}

	public GrandExchangeSlot[] getGrandExchangeSlots() {
		return grandExchangeSlots;
	}

	public void setGrandExchangeSlots(GrandExchangeSlot[] GrandExchangeSlots) {
		this.grandExchangeSlots = GrandExchangeSlots;
	}

	public void setGrandExchangeSlot(int index, GrandExchangeSlot state) {
		this.grandExchangeSlots[index] = state;
	}

	public void setSelectedGeSlot(int slot) {
		this.selectedGeSlot = slot;
	}

	public int getSelectedGeSlot() {
		return selectedGeSlot;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public int getSelectedSkillingItem() {
		return selectedSkillingItem;
	}

	public void setSelectedSkillingItem(int selectedItem) {
		this.selectedSkillingItem = selectedItem;
	}

	public boolean shouldProcessFarming() {
		return processFarming;
	}

	public void setProcessFarming(boolean processFarming) {
		this.processFarming = processFarming;
	}

	public Pouch getSelectedPouch() {
		return selectedPouch;
	}

	public void setSelectedPouch(Pouch selectedPouch) {
		this.selectedPouch = selectedPouch;
	}

	public int getCurrentBookPage() {
		return currentBookPage;
	}

	public void setCurrentBookPage(int currentBookPage) {
		this.currentBookPage = currentBookPage;
	}

	public int getStoredRuneEssence() {
		return storedRuneEssence;
	}

	public void setStoredRuneEssence(int storedRuneEssence) {
		this.storedRuneEssence = storedRuneEssence;
	}

	public int getStoredPureEssence() {
		return storedPureEssence;
	}

	public void setStoredPureEssence(int storedPureEssence) {
		this.storedPureEssence = storedPureEssence;
	}

	public int getTrapsLaid() {
		return trapsLaid;
	}

	public void setTrapsLaid(int trapsLaid) {
		this.trapsLaid = trapsLaid;
	}

	public boolean isCrossingObstacle() {
		return crossingObstacle;
	}

	public Player setCrossingObstacle(boolean crossingObstacle) {
		this.crossingObstacle = crossingObstacle;
		return this;
	}

	public boolean[] getCrossedObstacles() {
		return crossedObstacles;
	}

	public boolean getCrossedObstacle(int i) {
		return crossedObstacles[i];
	}

	public Player setCrossedObstacle(int i, boolean completed) {
		crossedObstacles[i] = completed;
		return this;
	}

	public void setCrossedObstacles(boolean[] crossedObstacles) {
		this.crossedObstacles = crossedObstacles;
	}

	public int getSkillAnimation() {
		return skillAnimation;
	}

	public Player setSkillAnimation(int animation) {
		this.skillAnimation = animation;
		return this;
	}

	public int[] getOres() {
		return ores;
	}

	public void setOres(int[] ores) {
		this.ores = ores;
	}

	public void setResetPosition(Position resetPosition) {
		this.resetPosition = resetPosition;
	}

	public Position getResetPosition() {
		return resetPosition;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	public Farming getFarming() {
		return farming;
	}

	public boolean inConstructionDungeon() {
		return inConstructionDungeon;
	}

	public void setInConstructionDungeon(boolean inConstructionDungeon) {
		this.inConstructionDungeon = inConstructionDungeon;
	}

	public int getHouseServant() {
		return houseServant;
	}

	public HouseLocation getHouseLocation() {
		return houseLocation;
	}

	public HouseTheme getHouseTheme() {
		return houseTheme;
	}

	public void setHouseTheme(HouseTheme houseTheme) {
		this.houseTheme = houseTheme;
	}

	public void setHouseLocation(HouseLocation houseLocation) {
		this.houseLocation = houseLocation;
	}

	public void setHouseServant(int houseServant) {
		this.houseServant = houseServant;
	}

	public int getHouseServantCharges() {
		return this.houseServantCharges;
	}

	public void setHouseServantCharges(int houseServantCharges) {
		this.houseServantCharges = houseServantCharges;
	}

	public void incrementHouseServantCharges() {
		this.houseServantCharges++;
	}

	public int getServantItemFetch() {
		return servantItemFetch;
	}

	public void setServantItemFetch(int servantItemFetch) {
		this.servantItemFetch = servantItemFetch;
	}

	public int getPortalSelected() {
		return portalSelected;
	}

	public void setPortalSelected(int portalSelected) {
		this.portalSelected = portalSelected;
	}

	public boolean isBuildingMode() {
		return this.isBuildingMode;
	}

	public void setIsBuildingMode(boolean isBuildingMode) {
		this.isBuildingMode = isBuildingMode;
	}

	public int[] getConstructionCoords() {
		return constructionCoords;
	}

	public void setConstructionCoords(int[] constructionCoords) {
		this.constructionCoords = constructionCoords;
	}

	public int getBuildFurnitureId() {
		return this.buildFurnitureId;
	}

	public void setBuildFuritureId(int buildFuritureId) {
		this.buildFurnitureId = buildFuritureId;
	}

	public int getBuildFurnitureX() {
		return this.buildFurnitureX;
	}

	public void setBuildFurnitureX(int buildFurnitureX) {
		this.buildFurnitureX = buildFurnitureX;
	}

	public int getBuildFurnitureY() {
		return this.buildFurnitureY;
	}

	public void setBuildFurnitureY(int buildFurnitureY) {
		this.buildFurnitureY = buildFurnitureY;
	}

	public int getCombatRingType() {
		return this.combatRingType;
	}

	public void setCombatRingType(int combatRingType) {
		this.combatRingType = combatRingType;
	}

	public Room[][][] getHouseRooms() {
		return houseRooms;
	}

	public ArrayList<Portal> getHousePortals() {
		return housePortals;
	}

	public ArrayList<HouseFurniture> getHouseFurniture() {
		return houseFurniture;
	}

	public int getConstructionInterface() {
		return this.constructionInterface;
	}

	public void setConstructionInterface(int constructionInterface) {
		this.constructionInterface = constructionInterface;
	}

	public int[] getBrawlerChargers() {
		return this.brawlerCharges;
	}

	public void setBrawlerCharges(int[] brawlerCharges) {
		this.brawlerCharges = brawlerCharges;
	}

	public int getRecoilCharges() {
		return this.recoilCharges;
	}

	public int setRecoilCharges(int recoilCharges) {
		return this.recoilCharges = recoilCharges;
	}

	public boolean voteMessageSent() {
		return this.voteMessageSent;
	}

	public void setVoteMessageSent(boolean voteMessageSent) {
		this.voteMessageSent = voteMessageSent;
	}

	public boolean didReceiveStarter() {
		return receivedStarter;
	}

	public void sendMessage(String string) {
		packetSender.sendMessage(string);
	}

	public void setReceivedStarter(boolean receivedStarter) {
		this.receivedStarter = receivedStarter;
	}

	public BlowpipeLoading getBlowpipeLoading() {
		return blowpipeLoading;
	}

	public boolean cloudsSpawned() {
		return areCloudsSpawned;
	}

	public void setCloudsSpawned(boolean cloudsSpawned) {
		this.areCloudsSpawned = cloudsSpawned;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isShopUpdated() {
		return shopUpdated;
	}

	public void setShopUpdated(boolean shopUpdated) {
		this.shopUpdated = shopUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYellmsg() {
		return yellmsg;
	}

	public void setYellMsg(String yellmsg) {
		this.yellmsg = yellmsg;
	}

	public void write(Packet packet) {
		// TODO Auto-generated method stub

	}

	public boolean isHidePlayer() {
		return hidePlayer;
	}

	public void setHidePlayer(boolean hidePlayer) {
		this.hidePlayer = hidePlayer;
	}

	public int[] getCompCapeColors() {
		return compCapeColors;
	}

	public void setCompCapeColors(int[] compCapeColors) {
		this.compCapeColors = compCapeColors;
	}

	public int getCurrentCape() {
		return currentCape;
	}

	public void setCurrentCape(int currentCape) {
		this.currentCape = currentCape;
	}

	public boolean isPrayerInjured() {
		// TODO Auto-generated method stub
		return false;
	}

	public long getDragonScimInjury() {
		return dragonScimInjury;
	}

	public void setDragonScimInjury(long dragonScimInjury) {
		this.dragonScimInjury = dragonScimInjury;
	}

	public Stopwatch getKrakenRespawn() {
		return krakenRespawn;
	}

	public byte[] getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}

	public void setCachedUpdateBlock(byte[] cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}

	public GamblingManager getGambling() {
		return gamblingManager;
	}

	public GambleOfferItemContainer getGambleOffer() {
		return gambleOffer;
	}

	private final GamblingManager gamblingManager = new GamblingManager();
	private final GambleOfferItemContainer gambleOffer = new GambleOfferItemContainer(this);

	public String getMac() {
		return mac;
	}

	public int summoned = -1;
	public Object getMoneyInPouch;
	private List<Integer> blockedCollectorsList = new ArrayList<>();
	public boolean hasReferral;

	public int getSummoned() {
		return summoned;
	}

	public int getTransform() {
		return transform;
	}

	public void setTransform(int npcId) {
		this.transform = npcId;
	}

	public int getNpcDropRate() {

		switch (getSummoning().getFamiliar().getSummonNpc().getId()) {

		// case npcid return droprate
		case 5089:
			return 2;

		default:
			return 0;
		}
	}

	public List<Integer> getBlockedCollectorsList() {
		return blockedCollectorsList;
	}

	public void giveItem(int casketOrFragment, int i) {
		// TODO Auto-generated method stub

	}

	public boolean isDoubleRateActive() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean insideRaids = false;

	private ChestViewer chestViewer = new ChestViewer(this);

	public ChestViewer getChestViewer() {
		return chestViewer;
	}

	public Item upgradeSelection;

	public Item getUpgradeSelection() {
		return upgradeSelection;
	}

	public void setUpgradeSelection(Item upgradeSelection) {
		this.upgradeSelection = upgradeSelection;
	}

	public boolean isInsideRaids() {
		return insideRaids;
	}

	public void setInsideRaids(boolean insideRaids) {
		this.insideRaids = insideRaids;
	}

	private Item raidsLoot;
	private Item raidsLootSecond;
	private Item raidsLootThird;

	public Item getRaidsLoot() {
		return raidsLoot;
	}

	public void setRaidsLoot(Item raidsLoot) {
		this.raidsLoot = raidsLoot;
	}

	public Item getRaidsLootSecond() {
		return raidsLootSecond;
	}

	public void setRaidsLootSecond(Item raidsLootSecond) {
		this.raidsLootSecond = raidsLootSecond;
	}

	private CollectionLogInterfaceHandler collectionLogInterfaceHandler = new CollectionLogInterfaceHandler();

	public CollectionLogInterfaceHandler getCollectionLog() {
		return collectionLogInterfaceHandler;
	}

	public Item getRaidsLootThird() {
		return raidsLootThird;
	}

	public void setRaidsLootThird(Item raidsLootThird) {
		this.raidsLootThird = raidsLootThird;
	}

	private int vaderkills;

	public void setVaderKills(int amount) {
		this.vaderkills = amount;
	}

	public void decrementVaderKills(int amount) {
		this.vaderkills -= amount;
	}

	public void incrementVaderKills(int amount) {
		this.vaderkills += amount;
	}

	public void multiplyVaderKills(int amount) {
		this.vaderkills *= amount;
	}

	public int getVaderKills() {
		return vaderkills;
	}

	private int dragonkills;

	public void setDragonKills(int amount) {
		this.dragonkills = amount;
	}

	public void decrementDragonKills(int amount) {
		this.dragonkills -= amount;
	}

	public void incrementDragonKills(int amount) {
		this.dragonkills += amount;
	}

	public void multiplyDragonKills(int amount) {
		this.dragonkills *= amount;
	}

	public int getDragonKills() {
		return dragonkills;
	}

	public void incrementAmountDonated2(float price) {
		this.amountDonated += price;
	}

	public void addItemToAny(int itemid, int amount) {
		/*
		 * ItemDefinition item = ItemDefinition.forId(itemid); if
		 * (getInventory().getFreeSlots() < (item.isNoted() ? 1 : item.isStackable() ? 1
		 * : amount)) { for (int index = 0; index < 7; index++) { if
		 * (!getBank(index).isFull()) getBank(index).add(item.isNoted() ?
		 * item.getUnnotedId() : itemid, amount); if (Prayer.isBone(itemid)) {
		 * 
		 * sendMessage(item.getName() + " has been placed in your bank, tab " + (index +
		 * 1) + "."); } return; } } else { getInventory().add(item.getId(), amount); }
		 */
		ItemDefinition item = ItemDefinition.forId(itemid);
		Item toAdd = new Item(item.getId(), amount);
		if (getInventory().getFreeSlots() < (item.isNoted() ? 1 : item.isStackable() ? 1 : amount)) {
			depositItemBank(toAdd, false);
		} else {
			getInventory().add(item.getId(), amount);
		}
	}

	public boolean isSoulSplit() {
		if (summoning.getFamiliar() != null && summoning.getFamiliar().getSummonNpc() != null) {
			switch (summoning.getFamiliar().getSummonNpc().getId()) {
			case 164:
				return true;
			}
		}
		return getCurseActive()[CurseHandler.CurseData.SOUL_SPLIT.ordinal()];
	}

	public boolean hasSoulSplitPet() {
		if (summoning.getFamiliar() != null && summoning.getFamiliar().getSummonNpc() != null) {
			switch (summoning.getFamiliar().getSummonNpc().getId()) {
			case 164:
				return true;
			}
		}
		return false;
	}

	public MagicSpellbook previousSpellbook;

	public boolean inBattleRoyale;
	public boolean inBattleLobby;
	public boolean inMageBattleLobby;
	public boolean inRangeBattleLobby;

	public String pollTitle;
	public String pollOptionOne;
	public String pollOptionTwo;
	public int pollHours;
	public String currentTime, date;
	public String connectedFrom = "";
	private int ultCharges;

	public int getUltBowCharges() {
		return this.ultCharges;
	}

	public int setUltBowCharges(int blowpipeCharges) {
		return this.ultCharges = blowpipeCharges;
	}

	private RaidsSaving raidsPlayer = new RaidsSaving(this);

	public RaidsSaving getRaidsSaving() {
		return raidsPlayer;
	}

	private final StarterProgression starterProgression = new StarterProgression(this);

	public StarterProgression getStarterProgression() {
		return starterProgression;
	}

	private boolean[] starterProgressionCompletions = new boolean[9];

	public boolean[] getStarterProgressionCompletions() {
		return starterProgressionCompletions;
	}

	public void setStarterProgressionCompletions(boolean[] starterProgressionCompletions) {
		this.starterProgressionCompletions = starterProgressionCompletions;
	}

	public void setStarterProgressionCompleted() {
		for (int i = 0; i < starterProgressionCompletions.length; i++) {
			if (!starterProgressionCompletions[i]) {
				starterProgressionCompletions[i] = true;
				break;
			}
		}
	}

	public int getCurrentStarterProgression() {
		for (int i = 0; i < starterProgressionCompletions.length; i++) {
			if (!starterProgressionCompletions[i]) {
				return i;
			}
		}
		return -1;
	}

	public Map<Integer, Integer> getNpcKillCount() {
		return npcKillCountMap;
	}

	public void setNpcKillCount(Map<Integer, Integer> dataMap) {
		this.npcKillCountMap = dataMap;
	}

	public void addNpcKillCount(int npcId) {
		npcKillCountMap.merge(npcId, 1, Integer::sum);
	}

	public int getNpcKills() {
		int totalNpcKills = 0;
		for (int count : npcKillCountMap.values()) {
			totalNpcKills += count;
		}
		return totalNpcKills;
	}

	public int currentBossTaskAmount;
	private Map<Integer, Integer> npcKillCountMap = new HashMap<>();

	public int[] oldSkillLevels = new int[25];
	public int[] oldSkillXP = new int[25];
	public int[] oldSkillMaxLevels = new int[25];

	public boolean kcMessage = true;
	public int combiner;

	public int getNpcKillCount(int npcId) { // + already exists yeah but not in playersaving/loading
		return npcKillCountMap.get(npcId) == null ? 0 : npcKillCountMap.get(npcId);
	}

	public int getCurrentBossTaskAmount() {
		return currentBossTaskAmount;
	}

	public void setCurrentBossTaskAmount(int currentBossTaskAmount) {
		this.currentBossTaskAmount = currentBossTaskAmount;
	}

	public void forceLeaveRaid(boolean redo) {
		this.getRaidsOne().getRaidsConnector().leaveRaidsOne();
		this.getRaidsOne().getRaidsConnector().leave(true);
		RaidsParty partyy = this.getMinigameAttributes().getRaidsAttributes().getParty();
		this.moveTo(new Position(3038, 2785, 0));
		this.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
		if (redo)
			partyy.remove(this, false, true);

	}

	private x2DamageTimer dmg = new x2DamageTimer(this);

	public x2DamageTimer getDmg() {
		return dmg;
	}

	private int dmgTimeLeft;

	public int getDmgTimeLeft() {
		return dmgTimeLeft;
	}

	public void setDmgTimeLeft(int timee) {
		this.dmgTimeLeft = timee;
	}

	private OverloadTimer ovl = new OverloadTimer(this);

	public OverloadTimer getOvl() {
		return ovl;
	}

	private int timeLeft;

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int time) {
		this.timeLeft = time;
	}

	public boolean isOpMode() {
		return opMode;
	}

	public void setOpMode(boolean newMode) {
		this.opMode = newMode;
	}

	private boolean opMode;

	private GroupIronmanGroup groupIronmanGroup;

	public GroupIronmanGroup getGroupIronmanGroup() {
		return groupIronmanGroup;
	}

	public void setGroupIronmanGroup(GroupIronmanGroup groupIronmanGroup) {
		this.groupIronmanGroup = groupIronmanGroup;
	}

	private GroupIronmanGroup groupIronmanGroupInvitation = null;

	public GroupIronmanGroup getGroupIronmanGroupInvitation() {
		return groupIronmanGroupInvitation;
	}

	public void setGroupIronmanGroupInvitation(GroupIronmanGroup groupIronmanGroupInvitation) {
		this.groupIronmanGroupInvitation = groupIronmanGroupInvitation;
	}

	private String groupOwnerName = null;

	public String getGroupOwnerName() {
		return groupOwnerName;
	}

	private AchievementInterface achievementInterface1;

	public void setAchievementInterface1(AchievementInterface achievementInterface) {
		this.achievementInterface = achievementInterface;
	}

	public AchievementInterface getAchievementInterface1() {
		return this.achievementInterface;
	}

	private AchievementTracker achievementTracker1 = new AchievementTracker(this);

	public AchievementTracker getAchievementTracker1() {
		return this.achievementTracker;
	}

	public void setGroupOwnerName(String groupOwnerName) {
		this.groupOwnerName = groupOwnerName;
	}

	private GroupIronman groupIronman = new GroupIronman(this);

	public GroupIronman getGroupIronman() {
		return groupIronman;
	}

	private boolean gim;

	public boolean isGim() {
		return gim;
	}

	public void setGim(boolean state) {
		gim = state;
	}

	private InstanceManager instanceManager = new InstanceManager();

	public InstanceManager getInstanceManager() {
		return instanceManager;

	}

	public String getBronzeBattlepassClaimed() {
		return bronzeBattlepassClaimed;
	}

	public String getGoldBattlepassClaimed() {
		return goldBattlepassClaimed;
	}

	public String getGoldBattlepassExpires() {
		return goldBattlepassExpires;
	}

	public void setbronzeBattlepassClaimed(String bronzeBattlepassClaimed) {
		this.bronzeBattlepassClaimed = bronzeBattlepassClaimed;
	}

	public void setgoldBattlepassClaimed(String goldBattlepassClaimed) {
		this.goldBattlepassClaimed = goldBattlepassClaimed;
	}

	public void setgoldBattlepassExpires(String goldBattlepassExpires) {
		this.goldBattlepassExpires = goldBattlepassExpires;
	}

	public String bronzeBattlepassClaimed;

	public String goldBattlepassClaimed;

	public String goldBattlepassExpires;

	public int currentPlayerPanelIndex = 1;

	@Getter
	private final BattlePass battlePass = new BattlePass(this);
	public String bronzeBattlepassExpires;

	public BattlePass getBattlePass() {
		return battlePass;
	}

	public String getBronzeBattlepassExpires() {
		return bronzeBattlepassExpires;
	}

	public void setbronzeBattlepassExpires(String bronzeBattlepassExpires) {
		this.bronzeBattlepassExpires = bronzeBattlepassExpires;
	}

	private boolean inCashZone = false;

	public boolean isInCashZone() {
		return inCashZone;
	}

	public void setInCashZone(boolean inCashZone) {
		this.inCashZone = inCashZone;
	}

	public NpcDefinition def;

	private boolean applyDr = false;

	public boolean isApplyDr() {
		return applyDr;
	}

	public void setApplyDr(boolean applyDr) {
		this.applyDr = applyDr;
	}

}