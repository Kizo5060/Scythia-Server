package com.ruseps.world.content.raids;

import com.ruseps.GameSettings;
import com.ruseps.model.*;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.minigames.Minigame2;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.model.Locations.Location;

public abstract class Raid extends Minigame2 {
	protected Stage[] stages;
	protected int currentStage = -1;


	protected Position endPos = new Position(3038, 2785, 0);

	// delays
	public static final int START_DELAY = 30; // 120 seconds
	public static final int NEXT_STAGE_DELAY = 5; // 20 seconds

	// times
	protected int timeToStart = -1;
	protected int timeToNextStage = -1;

	// booleans
	protected boolean lastPlayerAnnouncement = false;
	protected boolean aboutToStartAnnouncement = false;
	public boolean killall = false;

	@Override
	public void init() {
		super.init();
		this.currentStage = -1;
		if (stages != null)
			for (int i = 0; i < stages.length; ++i)
				stages[i].restart();
		timeToStart = -1;
		timeToNextStage = -1;
		lastPlayerAnnouncement = false;
		aboutToStartAnnouncement = false;
	}

	public class Stage {
		private NPC[] npcs;
		private int[] npcConditions; // 1 = alive, -1 = dead
		public Stage(NPC[] npcs) {
			this.npcs = npcs;
			this.npcConditions = new int[npcs.length];
			for (int i = 0; i < npcConditions.length; ++i) {
				npcConditions[i] = 1;
			}
		}
		public boolean isInStage(int npcId) {
			for (int i = 0; i < npcs.length; ++i) {
				if (npcConditions[i] == -1)
					continue;
				if (npcId == npcs[i].getId())
					return true;
			}
			return false;
		}
		public int countNpcs() {
			int count = 0;
			for (int i = 0; i < npcs.length; ++i)
				if (npcConditions[i] != -1 && npcs[i] != null)
					count++;
			return count;
		}
		public boolean isFinished() {

			return countNpcs() == 0;
		}

		public boolean kill(int npcId) {
			for (int i = 0; i < npcs.length ; ++i) {
				if (npcConditions[i] == -1)
					continue;
				if (npcId == npcs[i].getId()) {
					npcConditions[i] = -1;
					return true;
				}
			}
			return false;
		}
		public void restart() {
			for (int i = 0; i < npcConditions.length; ++i) {
				npcConditions[i] = 1;
			}
		}
		public void killAll() {
			for (NPC npc : this.npcs) {
				if (npc != null)
					World.deregister(npc);
			}
		}
		public NPC getNpc(int index) {
			return this.npcs[index];
		}
	}

	public Raid(int maxPlayers, Location location, Stage[] stages) {
		super(maxPlayers, location);
		this.stages = stages;
		setName("Raid");
		initStages();
	}

	public Raid(int maxPlayers, Location location) {
		this(maxPlayers, location, null);
	}

	@Override
	public void stop() {
		super.stop();
		for (Stage stage : stages) {
			stage.killAll();
		}
	}

	@Override
	public void process() {
		if (!activated)
			return;
		if (active) {
			processActive();
		} else {
			processInActive();
		}
	}

	protected void processActive() {
		int currentTime = (int)System.currentTimeMillis();
		if (currentStage == -1) { // actual raid hasn't started yet
			startNextStage();
			GodsRaid.setStarted(true);
		}

		if (stages[currentStage].isFinished() && currentStage == stages.length - 1) {
			giveRewards();
			GodsRaid.setStarted(false);
			playersToEndPos();
			init();
			return;
		}
		if (playerIndex == 0) {
			World.sendMessage("@red@[Super Sayian Raids] <col=16777215>" + getName() + " ended with no winners");
			GodsRaid.setStarted(false);
			init();
			return;
		}
		if (playerIndex == 1 && !lastPlayerAnnouncement) {
			World.sendMessage("@red@[Super Sayian Raids] <col=16777215>" + players[0].getUsername() + " is left alone in " + getName());
			lastPlayerAnnouncement = true;
		}
		if (stages[currentStage].isFinished()) {
			if (timeToNextStage == -1) {
				timeToNextStage = currentTime + NEXT_STAGE_DELAY * 1000;
				sendLocalMessage("Next stage will start in " + NEXT_STAGE_DELAY + " seconds");
				return;
			}
			if (currentTime > timeToNextStage) {
				startNextStage();
				timeToNextStage = -1;
				if (currentStage == stages.length - 1)
					sendLocalMessage("this is the final stage, good luck!");
			}
		}
	}

	protected void processInActive() {
		int currentTime = (int) System.currentTimeMillis();
		if (playerIndex > 0) {
			if (timeToStart == -1) {
				sendLocalMessage(getName() + " will start in " + START_DELAY + " seconds");
				timeToStart = currentTime + START_DELAY * 1000;
				return;
			}
			if (!aboutToStartAnnouncement) {
				World.sendMessage("@red@[Raids] @bla@" + getName() + " will start in " + START_DELAY + " seconds");
				aboutToStartAnnouncement = true;
			}

			if (currentTime > timeToStart) {
				killall = true;
				start();
			}
		} else {
			timeToStart = currentTime + START_DELAY * 1000;
			aboutToStartAnnouncement = false;
			//init();
			if (killall) {
				for (Stage stage : stages) {
					stage.killAll();
				}
				killall = false;
			}
		}
	}

	@Override
	public void end() {
		playersToEndPos();
		init();
	}


	@Override
	public void handleLogout(Player p) {
		if (removePlayer(p))
			p.moveTo(new Position(3095, 2990, 0));
	}

	@Override
	public void handlePlayerDeath(Player p) {
		if (!isInGame(p))
			return;
		removePlayer(p);
	}

	@Override
	public void handleLocationEntry(Player p) {
		addPlayer(p);
	}

	@Override
	public void handleLocationOutry(Player p) {
		removePlayer(p);
	}
	/*
	public void giveRewards() {
		int[] rare = {3242, 423}; // 1:500 chance
		int[] ultraRare = {1}; // 1:1000 chance - should put pets here
		int[][] common = {{1,1}}; // item and amount
		for (int i = 0; i < playerIndex; ++i) {
			int random = Misc.random(999);
			Item reward = null;
			int randomCommon = Misc.random(common.length - 1);
			reward = new Item(common[randomCommon][0]);
			int amount = common[randomCommon][1];
			if (random == 999) {
				amount = 1;
				int randomUltra = Misc.random(ultraRare.length - 1);
				reward = new Item(ultraRare[randomUltra]);
				String itemName = reward.getDefinition().getName();
				String announcement = "@red@[Raids][Ultra Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
				World.sendMessage(announcement);
			}
			else if (random == 999 || random == 998) {
				amount = 1;
				int randomRare = Misc.random(rare.length - 1);
				reward = new Item(ultraRare[randomRare]);
				String itemName = reward.getDefinition().getName();
				String announcement = "@red@[Raids][Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
				World.sendMessage(announcement);
			}
			reward.setAmount(amount);
			if (players[i].getInventory().getFreeSlots() > 0)
				players[i].getInventory().add(reward);
			else {
				players[i].sendMessage("reward was added to your bank because you have no inventory spaces");
				players[i].getBank(players[i].getCurrentBankTab()).add(reward);
			}
		}
	}*/

	protected void startNextStage() {
		currentStage++;
		sendLocalMessage("stage " + (currentStage + 1) + " has just started");
		spawnCurrentStageNpcs();
	}

	protected void playersToEndPos() {
		for (int i = 0; i < playerIndex; ++i) {
			players[i].moveTo(endPos);
		}
	}
	/*
	protected void spawnNpcsForStage(int stage) {
		int npcCount = this.stages[stage].countNpcs();
		for (int i = 0; i < npcCount; ++i) {
			int random = Misc.random(this.npcCoords.length - 1);
			Point p = npcCoords[random];
			NPC npc = new NPC(stages[stage].getNpc(i), new Position((int)p.getX(), (int)p.getY(), 0), false);
			npc.getDefinition().setAggressive(true);
			npc.getDefinition().setMulti(true);
		}
	}*/

	public boolean handleNpcDeath(NPC npc) {
		System.out.println("pass 0");
		if (!isInGame(npc)) {
			return false;
		}
		System.out.println("pass 1");
		for (int i = 0; i < playerIndex; ++i) {
			
			if(Misc.random(700) == 1) {
				players[i].getInventory().add(15501,1);
				World.sendMessage("<shad=0>@bla@[@mag@Marvels Box@bla@]@mag@ "+players[i].getUsername()+ "@bla@ has just received a @mag@Marvels Box!@bla@");
				
			}
			
			if(Misc.random(1000) == 1) {
					players[i].getInventory().add(18338,1);
					World.sendMessage("<shad=0>@bla@[@mag@Donator Box@bla@]@mag@ "+players[i].getUsername()+ "@bla@ has just received a @mag@Donator Box!@bla@");
					
				}
		}
		this.stages[currentStage].kill(npc.getId());
		return true;
	}

	@Override
	public boolean addPlayer(Player p) {
		if (this.active) {
			p.getPacketSender().sendMessage("it has already started");
			return false;
		}
		p.getPacketSender().sendMessage(getName() + " will start in " + START_DELAY + " seconds");
		return super.addPlayer(p);
	}

	protected Stage getStage(int index) { return this.stages[index]; }

	protected abstract void spawnCurrentStageNpcs();
	protected abstract void giveRewards();
	protected abstract void initStages();
}
