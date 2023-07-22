package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.Graphic;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.movement.MovementQueue;
import com.ruseps.util.Misc;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Gambling {

	public static void rollDice(Player player) {
		if(player.getClanChatName() == null) {
			player.forceChat("I should join a CC to gamble!");
			player.getPacketSender().sendMessage("<img=10>@red@You need to be in a clanchat channel to roll a dice.");
			return;
		} else if(player.getClanChatName().equalsIgnoreCase("help")) {
			player.getPacketSender().sendMessage("@red@You can't roll a dice in this clanchat channel!");
			return;
		}
		if(!player.getClickDelay().elapsed(5000)) {
			player.getPacketSender().sendMessage("@red@You must wait 5 seconds between each dice cast.");
			return;
		}
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(11900));
		player.performGraphic(new Graphic(2075));
		player.forceChat(player.getUsername()+" just rolled @bla@" +Misc.getRandom(100)+ " on the percentile dice.");
		ClanChatManager.sendMessage(player.getCurrentClanChat(), "@bla@[ClanChat] @whi@"+player.getUsername()+" just rolled @bla@" +Misc.getRandom(100)+ "@whi@ on the percentile dice.");
		player.getClickDelay().reset();
	}
	
	public static void plantSeed2(Player player) {	
		for(NPC npc : player.getLocalNpcs()) {
			if(npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		if(CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersAlt flowers = FlowersAlt.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setInteractingObject(flower);
		MovementQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
	}
	public static void plantSeed69(Player player) {	
		for(NPC npc : player.getLocalNpcs()) {
			if(npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		if(CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersAlt flowers = FlowersAlt.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setInteractingObject(flower);
		MovementQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
	}
	public static void plantSeed77(Player player) {	
		for(NPC npc : player.getLocalNpcs()) {
			if(npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		if(CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersAlt77 flowers = FlowersAlt77.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setInteractingObject(flower);
		MovementQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
	}
	public enum FlowersAlt {
		PASTEL_FLOWERS(2980, 2460),
		ORANGE_FLOWERS(2985, 2470),
		BLUE_FLOWERS(2982, 2464);

		FlowersAlt(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		
		
		public int objectId;
		public int itemId;
		
		public static FlowersAlt forObject(int object) {
			for(FlowersAlt data : FlowersAlt.values()) {
				if(data.objectId == object)
					return data;
			}
			return null;
		}
		
		public static FlowersAlt generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if(RANDOM >= 1) {
				return values()[Misc.getRandom(3)];
			}
			return null;
		}
	}
	
	public enum FlowersAlt77 {
		YELLOW_FLOWERS(2983, 2466),
		PURPLE_FLOWERS(2984, 2468),
		ORANGE_FLOWERS(2985, 2470),
		RAINBOW_FLOWERS(2986, 2472);

		FlowersAlt77(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		
		
		public int objectId;
		public int itemId;
		
		public static FlowersAlt77 forObject(int object) {
			for(FlowersAlt77 data : FlowersAlt77.values()) {
				if(data.objectId == object)
					return data;
			}
			return null;
		}
		
		public static FlowersAlt77 generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if(RANDOM >= 1) {
				return values()[Misc.getRandom(3)];
			}
			return null;
		}
	}
	public static void plantSeed3(Player player) {	
		for(NPC npc : player.getLocalNpcs()) {
			if(npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		if(CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersAlt2 flowers = FlowersAlt2.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setInteractingObject(flower);
		MovementQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
	}
	
	public enum FlowersAlt2 {
		RED_FLOWERS(2981, 2462),
		YELLOW_FLOWERS(2983, 2466),
		RAINBOW_FLOWERS(2986, 2472);

		FlowersAlt2(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		
		public int objectId;
		public int itemId;
		
		public static FlowersAlt2 forObject(int object) {
			for(FlowersAlt2 data : FlowersAlt2.values()) {
				if(data.objectId == object)
					return data;
			}
			return null;
		}
		
		public static FlowersAlt2 generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if(RANDOM >= 1) {
				return values()[Misc.getRandom(3)];
			}
			return null;
		}
	}
	
	public enum FlowersAlt69 {
		
		RED_FLOWERS(2981, 2462),
		BLUE_FLOWERS(2982, 2464),
		YELLOW_FLOWERS(2983, 2466),
		RAINBOW_FLOWERS(2986, 2472);

		FlowersAlt69(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		
		public int objectId;
		public int itemId;
		
		public static FlowersAlt69 forObject(int object) {
			for(FlowersAlt69 data : FlowersAlt69.values()) {
				if(data.objectId == object)
					return data;
			}
			return null;
		}
		
		public static FlowersAlt69 generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if(RANDOM >= 1) {
				return values()[Misc.getRandom(3)];
			}
			return null;
		}
	}

	public static void plantSeed(Player player) {	
		if(!player.getClickDelay().elapsed(2000))
			return;
		for(NPC npc : player.getLocalNpcs()) {
			if(npc != null && npc.getPosition().equals(player.getPosition())) {
				player.getPacketSender().sendMessage("You cannot plant a seed right here.");
				return;
			}
		}
		
		if(player.getAmountDonated() > 19 || player.getRights() == PlayerRights.VETERAN) {
			
		} else {
			player.getPA().sendMessage("You need to be a donator + to do this!");
			return;	
		}
		if(CustomObjects.objectExists(player.getPosition().copy())) {
			player.getPacketSender().sendMessage("You cannot plant a seed right here.");
			return;
		}
		FlowersData flowers = FlowersData.generate();
		final GameObject flower = new GameObject(flowers.objectId, player.getPosition().copy());
		player.getMovementQueue().reset();
		player.getInventory().delete(299, 1);
		player.performAnimation(new Animation(827));
		player.getPacketSender().sendMessage("You plant the seed..");
		player.getMovementQueue().reset();
		player.setDialogueActionId(42);
		player.setInteractingObject(flower);
		DialogueManager.start(player, 78);
		MovementQueue.stepAway(player);
		CustomObjects.globalObjectRemovalTask(flower, 90);
		player.setPositionToFace(flower.getPosition());
		player.getClickDelay().reset();
	}


	public enum FlowersData {
		PASTEL_FLOWERS(2980, 2460),
		RED_FLOWERS(2981, 2462),
		BLUE_FLOWERS(2982, 2464),
		YELLOW_FLOWERS(2983, 2466),
		PURPLE_FLOWERS(2984, 2468),
		ORANGE_FLOWERS(2985, 2470),
		RAINBOW_FLOWERS(2986, 2472),

		WHITE_FLOWERS(2987, 2474),
		BLACK_FLOWERS(2988, 2476);
		FlowersData(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}

		public int objectId;
		public int itemId;
		
		public static FlowersData forObject(int object) {
			for(FlowersData data : FlowersData.values()) {
				if(data.objectId == object)
					return data;
			}
			return null;
		}
		
		public static FlowersData generate() {
			double RANDOM = (java.lang.Math.random() * 100);
			if(RANDOM >= 1) {
				return values()[Misc.getRandom(6)];
			} else {
				return Misc.getRandom(3) == 1 ? WHITE_FLOWERS : BLACK_FLOWERS;
			}
		}
	}
}
