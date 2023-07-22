package com.ruseps.world.content.collectionlog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.ruseps.model.Item;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class CollectionLogInterfaceHandler
{
	private static String DIR = "./data/saves/collectionLog/";

	public void openInterface(Player player) 
	{
		resetListings(player);
		sendListings(player);
		player.getPacketSender().resetItemsOnInterface(30375, 20);
		player.getPacketSender().sendNpcOnInterface(30367, 0, 0);
		player.getPacketSender().sendString(30369, "Killcount: ");
		player.getPacketSender().sendInterface(30360);
	}

	public void resetListings(Player player)
	{
		int start = 30560;
		
		for (int i = 0; i < 100; i++) 
		{
			player.getPacketSender().sendString(start, "");
			start++;
		}
	}
	
	public void selectNpc(Player player, CollectionLogNpcs npc) 
	{
	    player.getPacketSender().resetItemsOnInterface(30375, 20);
	    player.getPacketSender().sendNpcOnInterface(30367, npc.getNpcId(), 0);
	    player.getPacketSender().sendString(30369, "Killcount: " + player.getNpcKillCount(npc.getNpcId()));

	    int slot = 0;
	    Map<Integer, Integer> collectedItems = player.getCollectedItems().getOrDefault(npc.getNpcId(), new HashMap<>());

	    NPCDrops.NpcDropItem[] drops = NPCDrops.forId(npc.getNpcId()).getDropList();

	    for (int i = 1; i <= drops.length; i++)
	    {
	        NPCDrops.NpcDropItem drop = drops[i - 1];
	        int item = drop.getItem().getId();
	        int collectedAmount = collectedItems.getOrDefault(item, 0);

	        player.getPacketSender().sendItemOnInterface(30375, item, slot++, collectedAmount);
	    }
	}


	public void search(Player player, String name) 
	{
		int start = 30560;
		resetListings(player);
		for (CollectionLogNpcs npcs : CollectionLogNpcs.values()) 
		{
			if (NpcDefinition.forId(npcs.getNpcId()).getName().toLowerCase().contains(name.toLowerCase())) 
			{
				player.getPacketSender().sendString(start, Misc.capitalize(npcs.name().replace("_", " ")));
				start++;
			}
		}
	}

	public void sendListings(Player player)
	{
		int start = 30560;
		for (CollectionLogNpcs bosses : CollectionLogNpcs.values()) 
		{
			player.getPacketSender().sendString(start, Misc.capitalize(bosses.name().replace("_", " ")));
			start++;
		}
	}

	private int getCollectedAmount(Player player, int bossId, int itemId) 
	{
		if (player.getCollectedItems().isEmpty() || player.getCollectedItems().get(bossId) == null
				|| player.getCollectedItems().get(bossId).get(itemId) == null) 
		{
			return 0;
		}
		
		return player.getCollectedItems().get(bossId).get(itemId);
	}

	public void handleDrop(Player player, int npcId, Item item)
	{
	    player.handleCollectedItem(npcId, item);

	    int collectedAmount = getCollectedAmount(player, npcId, item.getId());
	    Map<Integer, Integer> npcItems = player.getCollectedItems().computeIfAbsent(npcId, k -> new HashMap<>());
	    npcItems.put(item.getId(), collectedAmount + 1);

	    if (hasCollectedAllItems(player, npcId))
	    {
	    	//player.getInventory().add(995, 5_000_000);
	    }
	    
	    selectNpc(player, CollectionLogNpcs.getNpcById(npcId));
	    saveCollectionLogs(player);
	}

	public void saveCollectionLogs(Player player)
	{
	    Path path = Paths.get(DIR + player.getUsername() + ".json");
	    File file = path.toFile();
	    file.getParentFile().setWritable(true);

	    if (!file.getParentFile().exists()) 
	    {
	        try {
	            file.getParentFile().mkdirs();
	        } catch (SecurityException e) {
	            System.out.println("Unable to create directory for perk data!");
	        }
	    }

	    try (FileWriter writer = new FileWriter(file)) {
	        Gson builder = new GsonBuilder().setPrettyPrinting().create();
	        JsonObject object = new JsonObject();
	        object.add("collectedItems", builder.toJsonTree(player.getCollectedItems()));

	        writer.write(builder.toJson(object));
	        writer.close();
	        System.out.println("Saved " + player.getUsername() + " CollectionLogs..");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	public void load(Player player, JsonElement jsonElement) 
	{
	    Gson gson = new Gson();
	    JsonObject collectedItemsObject = jsonElement.getAsJsonObject().getAsJsonObject("collectedItems");

	    for (Entry<String, JsonElement> entry : collectedItemsObject.entrySet()) 
	    {
	        int npcId = Integer.parseInt(entry.getKey());
	        JsonObject itemData = entry.getValue().getAsJsonObject();

	        for (Entry<String, JsonElement> itemEntry : itemData.entrySet()) 
	        {
	            int itemId = Integer.parseInt(itemEntry.getKey());
	            int amount = itemEntry.getValue().getAsInt();

	            Map<Integer, Integer> npcItems = player.getCollectedItems().computeIfAbsent(npcId, k -> new HashMap<>());
	            npcItems.put(itemId, amount);
	        }
	    }
	}

	public void loadCollectionLogs(Player player) 
	{
	    Path path = Paths.get(DIR + player.getUsername() + ".json");
	    File file = path.toFile();

	    if (player.isMiniPlayer() || !file.exists())
	        return;

	    try (FileReader fileReader = new FileReader(file)) {
	        Gson builder = new GsonBuilder().create();
	        JsonParser parser = new JsonParser();
	        JsonObject reader = parser.parse(fileReader).getAsJsonObject();

	        if (reader.has("collectedItems")) 
	        {
	            load(player, reader);
	        }

	        System.out.println("Loaded Collection Logs from " + player.getUsername() + ".json");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private boolean hasCollectedAllItems(Player player, int npcId) 
	{
	    NPCDrops.NpcDropItem[] drops = NPCDrops.forId(npcId).getDropList();
	    
	    for (NPCDrops.NpcDropItem drop : drops) 
	    {
	        int itemId = drop.getItem().getId();
	        int collectedAmount = getCollectedAmount(player, npcId, itemId);
	        
	        if (collectedAmount < 1) 
	        {
	            return false;
	        }
	    }
	    return true;
	}
}
