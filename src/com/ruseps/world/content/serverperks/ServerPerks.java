package com.ruseps.world.content.serverperks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.util.StringUtils;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class ServerPerks {

	public ServerPerks(Perk cURRENT_PERK, Perk activePerk, int currentTime, boolean active, int cOST, int contributed,
			long cOOL_DOWN) {
		super();
		CURRENT_PERK = cURRENT_PERK;
		this.activePerk = activePerk;
		this.currentTime = currentTime;
		this.active = active;
		COST = cOST;
		this.contributed = contributed;
		COOL_DOWN = cOOL_DOWN;
	}

	public Perk getCURRENT_PERK() {
		return CURRENT_PERK;
	}

	public void setCURRENT_PERK(Perk cURRENT_PERK) {
		CURRENT_PERK = cURRENT_PERK;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCOST() {
		return COST;
	}

	public void setCOST(int cOST) {
		COST = cOST;
	}

	public int getContributed() {
		return contributed;
	}

	public void setContributed(int contributed) {
		this.contributed = contributed;
	}

	public long getCOOL_DOWN() {
		return COOL_DOWN;
	}

	public void setCOOL_DOWN(long cOOL_DOWN) {
		COOL_DOWN = cOOL_DOWN;
	}

	public int getTIME() {
		return TIME;
	}

	public int getINTERFACE_ID() {
		return INTERFACE_ID;
	}

	public int getOVERLAY_ID() {
		return OVERLAY_ID;
	}

	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	public void setActivePerk(Perk activePerk) {
		this.activePerk = activePerk;
	}

	public static void setInstance(ServerPerks instance) {
		ServerPerks.instance = instance;
	}

	public enum Perk {
		DR("x1.5 DR boost", "x1.5 DR", 1471, 60_000_000), // DONE
		XP("x2 EXP Boost", "x2 EXP", 1472, 30_000_000), // DONE
		DOUBLE_DROPS("Double Drops", "x2 Drops", 1473, 90_000_000), // DONE
		// PEST_CONTROL("x2 PC Points", "x2 PC", 1575, 50_000_000),// DONE
		SLAYER_POINTS("x1.5 Slayer Points", "x1.5 Slay", 1474, 60_000_000), // DONE
		RAIDS_LOOT("x2 Raids Loot", "x2 Raids", 1473, 90_000_000),// DONE
		;

		private Perk(String name, String smallName, int spriteId, int cost) {
			this.name = name;
			this.smallName = smallName;
			this.spriteId = spriteId;
			this.cost = cost;
		}

		public String getName() {
			return name;
		}

		public String getSmallName() {
			return smallName;
		}

		public int getSpriteId() {
			return spriteId;
		}

		public int getCost() {
			return cost;
		}

		private final String name;
		private final String smallName;
		private final int spriteId;
		private final int cost;
	}

	private final int TIME = 6000; // 1 hour
	private final int INTERFACE_ID = 46300;
	private final int OVERLAY_ID = 42400; //TBD
	public Perk CURRENT_PERK = Perk.values()[0];
	private Perk activePerk;
	private int currentTime = 0;
	private boolean active = false;
	public int COST = 100_000_000;
	private final Stopwatch stopwatch = new Stopwatch();
	public int contributed = 0;

	private long COOL_DOWN = 60 * 60 * 1000;

	public Perk getActivePerk() {
		return activePerk;
	}

	public void open(Player player) {
		player.getPacketSender().sendInterface(INTERFACE_ID);
		updateInterface(player);
	}

	public void contribute(Player player, int amount) {
		if (active) {
			player.sendMessage("@red@A perk is already active");
			return;
		}

		if (!player.getInventory().contains(19994, amount)) {
			amount = player.getInventory().getAmount(19994);
		}
		amount = Math.min(amount, COST - contributed);

		long cooldown = stopwatch.elapsed();
		if (cooldown != 0) {
			if (cooldown < COOL_DOWN) {
				player.sendMessage("You cannot contribute to the server perks while there is a cool down.");
				return;
			}
		}
		// StarterTasks.doProgress(player, StarterTasks.StarterTask.CONTRIBUTE_PERKS,
		// amount);

		player.getInventory().delete(19994, amount);
		contributed += amount;
		updateInterface(player);

		//player.getDailyTaskManager().submitProgressToIdentifier(25, amount);

		player.setServerPerksContributions(player.getServerPerksContributions() + amount);
		// player.getTitlesManager().checkMiscTitles();

		// PerksRaffle.addEntry(player, amount);

		if (amount >= 1000000) {
			World.sendMessage("<img=16><shad=1>@or2@[" + player.getUsername() + "] @yel@has just donated @gre@"
					+ Misc.insertCommasToNumber(amount) + " @yel@Upgrade Tokens to the Server Perks!");
		}
		if (contributed >= COST) {
			start(CURRENT_PERK);
		}
		save();
	}

	public void contribute(int amount) {
		if (active) {
			return;
		}

		amount = Math.min(amount, COST - contributed);

		contributed += amount;

		if (contributed >= COST) {
			start(CURRENT_PERK);
		}
		save();
	}

	public void setPerk(Perk perk) {
		CURRENT_PERK = perk;
		COST = perk.getCost();
	}

	public void setPerk(Player player, int index) {
		if (active) {
			player.sendMessage("@red@A perk is already active");
			return;
		}
		int amount = COST - contributed;
		contributed += COST - contributed;

		World.sendMessage("<img=16><shad=1>@red@[" + player.getUsername() + "] @yel@has just donated @gre@" + amount
				+ " @yel@Coins to the Server-Wide Perks!");
		if (contributed >= COST) {
			start(Perk.values()[index]);
		}
		save();
	}

	public void tick() {
		if (!active) {
			return;
		}

		currentTime--;

		if (currentTime == 0) {
			end();
		}

		if (currentTime % 100 == 0) {
			updateOverlay();
		}
	}

	private void start(Perk perk) {
		currentTime = TIME;
		active = true;

		activePerk = perk;
		updateOverlay();

		World.sendMessage("[Perk] " + perk.getName() + " has been activated for 1 hour!");
	}

	public void end() {
		active = false;
		contributed = 0;
		stopwatch.reset();

		World.sendMessage("<img=16>@cya@[WORLD]<img=16> @blu@[" + StringUtils.usToSpace(activePerk.getName())
				+ "] @red@Perk has ended");
		activePerk = null;
		resetInterface();

		if (CURRENT_PERK.ordinal() >= Perk.values().length - 1) {
			setPerk(Perk.values()[0]);
		} else
			setPerk(Perk.values()[CURRENT_PERK.ordinal() + 1]);

	}

	private void updateOverlay() {
		World.getPlayers().forEach(player -> {
			updateOverlay(player);
		});
	}

	public void updateOverlay(Player player) {
		if (activePerk == null) {
			player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
			return;
		}
		int minutes = (int) QuickUtils.tickToMin(currentTime);
		player.getPacketSender().sendSpriteChange(OVERLAY_ID + 1, activePerk.getSpriteId());
		player.getPacketSender().sendWalkableInterface(OVERLAY_ID, true);
		player.getPacketSender().sendString(OVERLAY_ID + 3, StringUtils.usToSpace(activePerk.getSmallName()));
		player.getPacketSender().sendString(OVERLAY_ID + 2, minutes + " min");
	}

	private void resetInterface() {
		World.getPlayers().forEach(player -> {
			player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
			player.getPacketSender().updateProgressBar(INTERFACE_ID + 10, 0);
		});
	}

	public void updateInterface(Player player) {
		loadList(player);
		int percentage = getPercentage(contributed, COST);
		player.getPacketSender().updateProgressBar(INTERFACE_ID + 7, percentage);
		player.getPacketSender().sendString(INTERFACE_ID + 8,
				Misc.insertCommasToNumber(contributed) + "/" + Misc.insertCommasToNumber(COST) + "");

		long cooldown = stopwatch.elapsed();

		if ((COOL_DOWN) - cooldown <= 0) {
			cooldown = 0;
		}

		if (cooldown == 0)
			player.getPacketSender().sendString(INTERFACE_ID + 11, "No Cooldown");
		else
			player.getPacketSender().sendString(INTERFACE_ID + 11,
					Misc.getHoursLeft((COOL_DOWN) - cooldown) + " Cooldown");
	}

	private void loadList(Player player) {
		player.getPacketSender().sendString(46312, active ? "Current Perk" : "Next Perk");

		int interID = 46352; // check here
		player.getPacketSender().sendString(interID++, CURRENT_PERK.getName());
		player.getPacketSender().sendSpriteChange(interID++, CURRENT_PERK.getSpriteId());
		player.getPacketSender().sendSpriteChange(interID++, CURRENT_PERK.getSpriteId());
	}

	private int getPercentage(int n, int total) {
		float proportion = ((float) n) / ((float) total);
		return (int) (proportion * 100f);
	}

	public boolean handleButton(Player player, int id) {
		if (id >= -23435 && id <= -23405) {
			updateInterface(player);
			return true;
		}

		return false;
	}

	public void reset() {
		contributed = 0;
		World.getPlayers().forEach(this::updateInterface);
	}

	private static ServerPerks instance = null;

	public static ServerPerks getInstance() {
		if (instance == null) {
			instance = new ServerPerks();
		}
		return instance;
	}

	private ServerPerks() {

	}

	public void save() {
		Path path = Paths.get("./data/saves/saved-info.json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();

			object.addProperty("contributed", contributed);
			object.addProperty("current-perk", CURRENT_PERK.ordinal());

			writer.write(builder.toJson(object));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try (FileReader fileReader = new FileReader("./data/saves/saved-info.json")) {
			JsonParser fileParser = new JsonParser();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("contributed"))
				contributed = reader.get("contributed").getAsInt();
			if (reader.has("current-perk"))
				setPerk(Perk.values()[reader.get("current-perk").getAsInt()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
