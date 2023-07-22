package com.ruseps.world;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import com.ruseps.model.Position;
import com.ruseps.world.entity.impl.player.Player;

import com.ruseps.world.content.EvilTrees;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.PlayerRights;
import com.ruseps.util.Misc;
import com.ruseps.world.content.ShootingStar;
import com.ruseps.world.content.ShopRestocking;
import com.ruseps.world.content.StaffList;
import com.ruseps.world.content.TriviaBot;
import com.ruseps.world.content.WellOfGoodwill;
import com.ruseps.world.content.afk_spots.AfkTree;
import com.ruseps.world.content.afk_spots.IceCoinRock;
import com.ruseps.world.content.battle_royale.BattleRoyale;
import com.ruseps.world.content.battle_royale.rotators.melee.ChestRotator;
import com.ruseps.world.content.battle_royale.rotators.melee.ChestRotator2;
import com.ruseps.world.content.battle_royale.rotators.nightmare.NightmareRotator;
import com.ruseps.world.content.battle_royale.rotators.nightmare.NightmareRotator2;
import com.ruseps.world.content.Cows;
import com.ruseps.world.content.DonationYeller;
import com.ruseps.world.content.Reminders;
import com.ruseps.world.content.SecurityYeller;
import com.ruseps.world.content.minigames.MinigameHandler2;
import com.ruseps.world.content.minigames.impl.FightPit;
import com.ruseps.world.content.minigames.impl.PestControl;
import com.ruseps.world.entity.Entity;
import com.ruseps.world.entity.EntityHandler;
import com.ruseps.world.entity.impl.CharacterList;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerHandler;
import com.ruseps.world.entity.updating.NpcUpdateSequence;
import com.ruseps.world.entity.updating.PlayerUpdateSequence;
import com.ruseps.world.entity.updating.UpdateSequence;

/**
 * @author Gabriel Hannason
 * Thanks to lare96 for help with parallel updating system
 */
public class World {

	/** All of the registered players. */
	private static CharacterList<Player> players = new CharacterList<>(1000);

	/** All of the registered NPCs. */
	private static CharacterList<NPC> npcs = new CharacterList<>(30000);

	/** Used to block the game thread until updating has completed. */
	private static Phaser synchronizer = new Phaser(1);

	/** A thread pool that will update players in parallel. */
	private static ExecutorService updateExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

	/** The queue of {@link Player}s waiting to be logged in. **/
    private static Queue<Player> logins = new ConcurrentLinkedQueue<>();

    /**The queue of {@link Player}s waiting to be logged out. **/
    private static Queue<Player> logouts = new ConcurrentLinkedQueue<>();
    
    /**The queue of {@link Player}s waiting to be given their vote reward. **/
    private static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

	public static MinigameHandler2 minigameHandler;
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void initPlayersOnline() {
		com.everythingrs.playersonline.PlayersOnline.service.scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
		        int online = 0;
		        for (Player player: World.getPlayers()) {
		            if (player != null) {
		                online += 1;
		            }
		        }
		        com.everythingrs.playersonline.PlayersOnline.insert("vulcanps", online, false);
		    }
		}, 0, 30, TimeUnit.SECONDS);
	}
    
    public static void register(Entity entity) {
		EntityHandler.register(entity);
	}

	public static void deregister(Entity entity) {
		EntityHandler.deregister(entity);
	}

	public static Player getPlayerByName(String username) {
		Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
		return op.isPresent() ? op.get() : null;
	}
	
	public static Player getPlayerByIndex(int username) {
		Optional<Player> op = players.search(p -> p != null && p.getIndex() == username);
		return op.isPresent() ? op.get() : null;
	}

	public static Player getPlayerByLong(long encodedName) {
		Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
		return op.isPresent() ? op.get() : null;
	}

	public static void sendMessage(String message) {
		players.forEach(p -> p.getPacketSender().sendMessage(message));
	}
	
	public static void sendStaffMessage(String message) {
		players.stream().filter(p -> p != null && (p.getRights() == PlayerRights.OWNER || p.getRights() == PlayerRights.MANAGER ||p.getRights() == PlayerRights.DEVELOPER || p.getRights() == PlayerRights.ADMINISTRATOR || p.getRights() == PlayerRights.MODERATOR || p.getRights() == PlayerRights.SUPPORT)).forEach(p -> p.getPacketSender().sendMessage(message));
	}
	
	public static void sendAdminMessage(String message) {
		players.stream().filter(p -> p != null && (p.getRights() == PlayerRights.OWNER ||p.getRights() == PlayerRights.MANAGER || p.getRights() == PlayerRights.DEVELOPER || p.getRights() == PlayerRights.ADMINISTRATOR )).forEach(p -> p.getPacketSender().sendMessage(message));
	}
	
	public static void updateServerTime() {
		players.forEach(p -> p.getPacketSender().sendString(26702, "@or2@Server time: @or2@[ @gre@"+Misc.getCurrentServerTime()+"@or2@ ]"));
	}

	public static void updatePlayersOnline() {
		players.forEach(p -> p.getPacketSender().sendString(28011, "@or2@Players Online: @gre@"+(int)(players.size() )+""));
		players.forEach(p -> p.getPacketSender().sendString(57003, "Players:  @gre@"+(int)(World.getPlayers().size() + 3)+""));
	}

	public static void updateStaffList() {
		TaskManager.submit(new Task(false) {
			@Override
			protected void execute() {
				stop();
			}
		});
	}
	
	public static void savePlayers() {
		players.forEach(p -> p.save());
	}

	public static CharacterList<Player> getPlayers() {
		return players;
	}

	public static CharacterList<NPC> getNpcs() {
		return npcs;
	}
	
	public static void sequence() {
		
        for (int amount = 0; amount < GameSettings.LOGIN_THRESHOLD; amount++) {
        	
            Player player = logins.poll();
            
            if (player == null) {
                break;
            }
            
            if(logouts.contains(player)) {
            	continue;
            }
            
            PlayerHandler.handleLogin(player);
            
        }

        AfkTree.sequence();
        IceCoinRock.sequence();
        FightPit.sequence();
        Reminders.sequence();
        SecurityYeller.sequence();
		PestControl.sequence();
		ShootingStar.sequence();
		EvilTrees.sequence();
		TriviaBot.sequence();
		BattleRoyale.meleeLobbySequence();
		/*BattleRoyale.mageLobbySequence();*/
		BattleRoyale.rangeLobbySequence();
		ChestRotator.sequence();
		ChestRotator2.sequence();
		NightmareRotator.sequence();
		NightmareRotator2.sequence();
		
		UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
		UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();
		players.shuffle();
		npcs.shuffle();
		
		// Then we execute pre-updating code.
		players.shuffledEach(playerUpdate::executePreUpdate);
		npcs.shuffledEach(npcUpdate::executePreUpdate);
		
		// Then we execute parallelized updating code.
		synchronizer.bulkRegister(players.size());
		players.forEach(playerUpdate::executeUpdate);
		synchronizer.arriveAndAwaitAdvance();

		// Then we execute post-updating code.
		players.forEach(playerUpdate::executePostUpdate);
		npcs.forEach(npcUpdate::executePostUpdate);
		
	}
	
	public static Queue<Player> getLoginQueue() {
		return logins;
	}
	
	public static Queue<Player> getLogoutQueue() {
		return logouts;
	}
	
	public static Queue<Player> getVoteRewardingQueue() {
		return voteRewards;
	}

    public static WellOfGoodwill getWell() {
        return getWell();
    
	}
	
}
