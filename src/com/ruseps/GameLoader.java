package com.ruseps;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ruseps.world.content.bis.BestInSlotInterface;
import com.ruseps.world.content.newdroptable.DropTableInterface;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruseps.engine.GameEngine;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.ServerTimeUpdateTask;
import com.ruseps.model.container.impl.Shop.ShopManager;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.net.PipelineFactory;
import com.ruseps.net.packet.interaction.PacketInteractionManager;
import com.ruseps.net.security.ConnectionHandler;
import com.ruseps.world.World;
import com.ruseps.world.clip.region.RegionClipping;
import com.ruseps.world.content.CustomObjects;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.Wildywyrm;
import com.ruseps.world.content.achievements.AchievementData;
import com.ruseps.world.content.aoesystem.AOESystem;
import com.ruseps.world.content.bossevents.GameEventManager;
import com.ruseps.world.content.Lottery;
import com.ruseps.world.content.Scoreboards;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.ruseps.world.content.combat.effect.CombatVenomEffect.CombatVenomData;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.strategy.zulrah.Zulrah;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.groupironman.GroupIronmanGroup;
import com.ruseps.world.content.minigames.MinigameHandler2;
import com.ruseps.world.content.pos.PlayerOwnedShopManager;
import com.ruseps.world.content.starter_progression.StarterProgression;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.PlayerLogout;

/**
 * Credit: lare96, Gabbe
 */////
public final class GameLoader {
	/*
	 * Daily events
	 * Handles the checking of the day to represent
	 * which event will be active on such day
	 */  
	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	public static final int SATURDAY = 7;
	//public static Object getSpecialDay;
		//Double EXP days
	public static int getDoubleEXPWeekend() {
		return (getDay() == THURSDAY || getDay() == FRIDAY || getDay() == SUNDAY || getDay() == SATURDAY) ? 2 : 1;
	}
		//Finds the day of the week
	public static int getDay() {
		Calendar calendar = new GregorianCalendar();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
			//events for each day
	public static String getSpecialDay() {
		switch (getDay()) {
		case MONDAY: 
			return "Double Vote Points";
		case TUESDAY:
			return "Double Slayer Points";
		case WEDNESDAY:
			return "Double Vote Points";
		case THURSDAY:
			return "Double Exp & Double Slayer Points";
		case FRIDAY:
			return "Double Exp";
		case SATURDAY:
			return "Double Exp";
		case SUNDAY:
			return "Double Exp & Double Slayer Points";
		}
		return "Double Exp\\nDouble Lottery";
	}

	private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
	private final GameEngine engine;
	private final int port;

	protected GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();
	}

	public void init() {
		Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
		executeServiceLoad();
		serviceLoader.shutdown();
	}

	public void finish() throws IOException, InterruptedException {
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
			throw new IllegalStateException("The background service load took too long!");
		ExecutorService networkExecutor = Executors.newCachedThreadPool();
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
		executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);
		TaskManager.submit(new ServerTimeUpdateTask());
		// TaskManager.submit(new WeaponGame());

		World.minigameHandler = MinigameHandler2.defaultHandler();
		World.minigameHandler.loadMinigames();

	}
        
	private void executeServiceLoad() {
		
		try {
			serviceLoader.execute(StarterProgression::loadTasks);
			System.out.println("PROGRESSION TASKS SUCCESSFULLY LOADED");
		} catch (Exception ex)
		{
			System.out.println("AN ISSUE WITH THE PROGRESSION TASKS HAS OCCURED");
		}
		serviceLoader.execute(() -> ItemDefinition.init());
		serviceLoader.execute(() -> Zulrah.initialize());
		serviceLoader.execute(() -> PlayerLogout.init());
		serviceLoader.execute(() -> ConnectionHandler.init());
		serviceLoader.execute(() -> {
			RegionClipping.init();
		});
		serviceLoader.execute(() -> CustomObjects.init());
		serviceLoader.execute(() -> ItemDefinition.init());
		serviceLoader.execute(() -> Lottery.init());
		serviceLoader.execute(() -> AchievementData.checkDuplicateIds());
		serviceLoader.execute(() -> Scoreboards.init());
		serviceLoader.execute(() -> WellOfGoodwill.init());
		serviceLoader.execute(() -> ClanChatManager.init());
		serviceLoader.execute(() -> CombatPoisonData.init());
		serviceLoader.execute(() -> CombatVenomData.init());
		serviceLoader.execute(() -> CombatStrategies.init());
		serviceLoader.execute(() -> NpcDefinition.parseNpcs().load());
		serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(() -> ShopManager.parseShops().load());
		serviceLoader.execute(() -> DialogueManager.parseDialogues().load());
		serviceLoader.execute(() -> NPC.init());
		serviceLoader.execute(() -> PacketInteractionManager.init());
		serviceLoader.execute(() -> PlayerOwnedShopManager.loadShops());
		serviceLoader.execute(() -> AOESystem.getSingleton().parseData());
		serviceLoader.execute(() -> Wildywyrm.initialize());
		serviceLoader.execute(BestInSlotInterface::loadAllItems);
		serviceLoader.execute(DropTableInterface.getInstance()::loadNpcs);
		serviceLoader.execute(GameEventManager::loadEvents);
		serviceLoader.execute(GroupIronmanGroup::loadGroups);	
	}

	public GameEngine getEngine() {
		return engine;
	}
}