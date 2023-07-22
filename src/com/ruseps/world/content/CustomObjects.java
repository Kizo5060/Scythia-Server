package com.ruseps.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.GameObject;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Locations;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.world.World;
import com.ruseps.world.clip.region.RegionClipping;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Handles customly spawned objects (mostly global but also privately for players)
 * @author Gabriel Hannason
 */
public class CustomObjects {
	
	private static final int DISTANCE_SPAWN = 70; //Spawn if within 70 squares of distance
	public static final CopyOnWriteArrayList<GameObject> CUSTOM_OBJECTS = new CopyOnWriteArrayList<GameObject>();

	public static void init() {
		for(int i = 0; i < CLIENT_OBJECTS.length; i++) {
			int id = CLIENT_OBJECTS[i][0];
			int x = CLIENT_OBJECTS[i][1];
			int y = CLIENT_OBJECTS[i][2];
			int z = CLIENT_OBJECTS[i][3];
			int face = CLIENT_OBJECTS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setFace(face);
			RegionClipping.addObject(object);
		}
		for(int i = 0; i < CUSTOM_OBJECTS_SPAWNS.length; i++) {
			int id = CUSTOM_OBJECTS_SPAWNS[i][0];
			int x = CUSTOM_OBJECTS_SPAWNS[i][1];
			int y = CUSTOM_OBJECTS_SPAWNS[i][2];
			int z = CUSTOM_OBJECTS_SPAWNS[i][3];
			int face = CUSTOM_OBJECTS_SPAWNS[i][4];
			GameObject object = new GameObject(id, new Position(x, y, z));
			object.setFace(face);
			CUSTOM_OBJECTS.add(object);
			World.register(object);
		}
	}
	
	private static void handleList(GameObject object, String handleType) {
		switch(handleType.toUpperCase()) {
		case "DELETE":
			for(GameObject objects : CUSTOM_OBJECTS) {
				if(objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
					CUSTOM_OBJECTS.remove(objects);
				}
			}
			break;
		case "ADD":
			if(!CUSTOM_OBJECTS.contains(object)) {
				CUSTOM_OBJECTS.add(object);
			}
			break;
		case "EMPTY":
			CUSTOM_OBJECTS.clear();
			break;
		}
	}

	public static void spawnObject(Player p, GameObject object) {
		if(object.getId() != -1) {
			p.getPacketSender().sendObject(object);
			if(!RegionClipping.objectExists(object)) {
				RegionClipping.addObject(object);
			}
		} else {
			deleteObject(p, object);
		}
	}
	
	public static void deleteObject(Player p, GameObject object) {
		p.getPacketSender().sendObjectRemoval(object);
		if(RegionClipping.objectExists(object)) {
			RegionClipping.removeObject(object);
		}
	}
	
	public static void deleteGlobalObject(GameObject object) {
		handleList(object, "delete");
		World.deregister(object);
	}

	public static void spawnGlobalObject(GameObject object) {
		handleList(object, "add");
		World.register(object);
	}
	
	public static void spawnGlobalObjectWithinDistance(GameObject object) {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				spawnObject(player, object);
			}
		}
	}
	
	public static void deleteGlobalObjectWithinDistance(GameObject object) {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
				deleteObject(player, object);
			}
		}
	}
	
		public static boolean objectExists(Position pos) {
			return getGameObject(pos) != null;
		}

		public static GameObject getGameObject(Position pos) {
			for(GameObject objects : CUSTOM_OBJECTS) {
				if(objects != null && objects.getPosition().equals(pos)) {
					return objects;
				}
			}
			return null;
		}

		public static void handleRegionChange(Player p) {
			for(GameObject object: CUSTOM_OBJECTS) {
				if(object == null)
					continue;
				if(object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
					spawnObject(p, object);
				}
			}
		}
	
		public static void objectRespawnTask(Player p, final GameObject tempObject, final GameObject mainObject, final int cycles) {
			deleteObject(p, mainObject);
			spawnObject(p, tempObject);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteObject(p, tempObject);
					spawnObject(p, mainObject);
					this.stop();
				}
			});
		}
		
		public static void globalObjectRespawnTask(final GameObject tempObject, final GameObject mainObject, final int cycles) {
			deleteGlobalObject(mainObject);
			spawnGlobalObject(tempObject);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteGlobalObject(tempObject);
					spawnGlobalObject(mainObject);
					this.stop();
				}
			});
		}

		public static void globalObjectRemovalTask(final GameObject object, final int cycles) {
			spawnGlobalObject(object);
			TaskManager.submit(new Task(cycles) {
				@Override
				public void execute() {
					deleteGlobalObject(object);
					this.stop();
				}
			});
		}

	public static void globalFiremakingTask(final GameObject fire, final Player player, final int cycles) {
		spawnGlobalObject(fire);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(fire);
				if(player.getInteractingObject() != null && player.getInteractingObject().getId() == 2732) {
					player.setInteractingObject(null);
				}
				this.stop();
			}
			@Override
			public void stop() {
				setEventRunning(false);
				GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(592), fire.getPosition(), player.getUsername(), false, 150, true, 100));
			}
		});
	}
	
	
	
	/**
	 * Contains
	 * @param ObjectId - The object ID to spawn
	 * @param absX - The X position of the object to spawn
	 * @param absY - The Y position of the object to spawn
	 * @param Z - The Z position of the object to spawn
	 * @param face - The position the object will face
	 */
	
	//Only adds clips to these objects, they are spawned clientsided
	//NOTE: You must add to the client's customobjects array to make them spawn, this is just clipping!
	private static final int[][] CLIENT_OBJECTS = {
			
			//dzone
			
			
			/**Start Of Skilling Zone**/
			
			{11933, 2040, 4632, 0, 0}, //tin ore
			{11933, 2040, 4631, 0, 0}, //tin ore
			{11933, 2040, 4630, 0, 0}, //tin ore
			{11936, 2040, 4629, 0, 0}, //copper ore
			{11936, 2040, 4628, 0, 0}, //copper ore
			{11936, 2040, 4627, 0, 0}, //copper ore
			{11954, 2040, 4626, 0, 0}, //iron ore
			{11954, 2040, 4625, 0, 0}, //iron ore
			{11954, 2040, 4624, 0, 0}, //iron ore
			{11954, 2040, 4623, 0, 0}, //iron ore
			{11963, 2031, 4622, 0, 0}, //coal ore
			{11963, 2031, 4623, 0, 0}, //coal ore
			{11963, 2031, 4624, 0, 0}, //coal ore
			{11951, 2031, 4625, 0, 0}, //gold ore
			{11951, 2031, 4626, 0, 0}, //gold ore
			{11951, 2031, 4627, 0, 0}, //gold ore
			{11941, 2031, 4628, 0, 0}, //adamant ore
			{11941, 2031, 4629, 0, 0}, //adamant ore
			{11941, 2031, 4630, 0, 0}, //adamant ore
			{14859, 2031, 4631, 0, 0}, //rune ore
			{14859, 2031, 4632, 0, 0}, //rune ore
			{2491, 2035, 4655, 0, 0}, //rune ess rock
			
			{2213, 2044, 4636, 0, 0}, //Bank Booth
			{2213, 2044, 4635, 0, 0}, //Bank Booth
			{2213, 2015, 4664, 0, 0}, //Bank Booth
			{2213, 2016, 4664, 0, 0}, //Bank Booth
			{2213, 1987, 4635, 0, 0}, //Bank Booth
			{2213, 1987, 4636, 0, 0}, //Bank Booth

			{6189, 2039, 4643, 0, 2}, //furnace
			{4306, 2040, 4642, 0, 0}, //anvil
			{4306, 2040, 4645, 0, 0}, //anvil
			{28716, 2024, 4641, 0, 0}, //Summoning Obelisk
			
			{4875, 2031, 4642, 0, 0}, //Banana Stall
			{4874, 2031, 4643, 0, 0}, //Ring Stall
			{4876, 2031, 4644, 0, 0}, //General Stall
			{4877, 2031, 4645, 0, 0}, //Magic Stall
			{4878, 2031, 4646, 0, 0}, //Scimitar Stall
			
			{16604, 2021, 4644, 0, 0}, //Afk Tree
			{16604, 2009, 4644, 0, 0}, //Afk Tree
			{16604, 2009, 4626, 0, 0}, //Afk Tree
			{16604, 2021, 4626, 0, 0}, //Afk Tree
			
			{1278, 2038, 4618, 0, 0}, //Tree
			{1278, 2038, 4615, 0, 0}, //Tree
			{1278, 2036, 4613, 0, 0}, //Tree
			{1278, 2034, 4612, 0, 0}, //Tree
			{1281, 2030, 4612, 0, 0}, //Oak Tree
			{1281, 2026, 4612, 0, 0}, //Oak Tree
			{1281, 2022, 4612, 0, 0}, //Oak Tree
			{1308, 2019, 4612, 0, 0}, //Willow Tree
			{1308, 2016, 4612, 0, 0}, //Willow Tree
			{1308, 2013, 4612, 0, 0}, //Willow Tree
			{1307, 2010, 4612, 0, 0}, //Maple Tree
			{1307, 2007, 4612, 0, 0}, //Maple Tree
			{1307, 2004, 4612, 0, 0}, //Maple Tree
			{1306, 1999, 4612, 0, 0}, //Magic Tree
			{1306, 1992, 4620, 0, 0}, //Magic Tree
			{1306, 1992, 4623, 0, 0}, //Magic Tree
			{1309, 1995, 4612, 0, 0}, //Yew Tree
			{1309, 1993, 4614, 0, 0}, //Yew Tree
			{1309, 1992, 4617, 0, 0}, //Yew Tree

















			
			
			
			//runerocks
			{14859, 2331, 9798, 0, 0},
			{14859, 2331, 9799, 0, 0},
			{14859, 2331, 9800, 0, 0},
			{14859, 2331, 9801, 0, 0},
			{14859, 2331, 9802, 0, 0},
			{1993, 3354, 3095, 0, 1},

			{12269, 3250, 2866, 0, 0},
			
			{14859, 2330, 9796, 0, 0},
			{14859, 2330, 9795, 0, 0},
			{14859, 2330, 9794, 0, 0},
			//banks
			{2213, 2323, 9800, 0, 0},
			{2213, 2322, 9800, 0, 0},
			
			{2213, 2320, 9800, 0, 0},
			{2213, 2319, 9800, 0, 0},
			{2213, 2318, 9800, 0, 0},
			
			
			//magetrees
			{1306, 2326, 9795, 0, 0},
			{4002, 3279, 2896, 0, 0},
			
			

			

			{6189, 2328, 9798, 0, 0},

			{-1, 2324, 9799, 0, 0},
			{-1, 2324, 9798, 0, 0},
			{-1, 2325, 9798, 0, 0},
			{-1, 2326, 9798, 0, 0},
			{-1, 2328, 9799, 0, 0},
			{-1, 2327, 9799, 0, 0},
			{-1, 2327, 9800, 0, 0},
			{-1, 2327, 9798, 0, 0},
			
			{-1, 2321, 9800, 0, 0},
			{-1, 2320, 9798, 0, 0},
			{-1, 2319, 9799, 0, 0},
			{-1, 2318, 9801, 0, 0},
			{-1, 2321, 9798, 0, 0},
			{-1, 2318, 9798, 0, 0},
			{-1, 2316, 9798, 0, 0},
			{-1, 2309, 9799, 0, 0},
			
			{-1, 2309, 9806, 0, 0},
			
            //Treasure island chests
            
			{-1, 2309, 9804, 0, 0},
			{-1, 2310, 9804, 0, 0},
			{-1, 2308, 9804, 0, 0},
			{-1, 2307, 9804, 0, 0},
			{-1, 2309, 9805, 0, 0},
			{-1, 2310, 9805, 0, 0},
			{-1, 2308, 9805, 0, 0},
			{-1, 2307, 9805, 0, 0},
			
			{-1, 2319, 9798, 0, 0},

			{2213, 2321, 9800, 0, 0},
			
			{4306, 2325, 9798, 0, 0},
			{2644, 2326, 9798, 0, 0},
			
			{2732, 2323, 9806, 0, 0},

			{2646, 2328, 9804, 0, 0},
			{2646, 2329, 9804, 0, 0},
			{2646, 2327, 9804, 0, 0},
			{2646, 2328, 9803, 0, 0},
			{2646, 2329, 9803, 0, 0},
			{2646, 2327, 9803, 0, 0},
			{2646, 2330, 9803, 0, 0},

			
			//rocktail fishing
			{8702, 2327, 9801, 0, 0},
			//frost drags portal
			//rune armour stealing
			{13493, 2316, 9801, 0, 2},
			//special altar
			{8749, 2342, 9805, 0, 3},
			
			//dzone thieve altars
			{4875, 3221, 3431, 0, 0},
			{4874, 3221, 3432, 0, 0},
			{4876, 3221, 3433, 0, 0},
			{4877, 3221, 3434, 0, 0},
			{4878, 3221, 3435, 0, 0},
			
			
			//dzone
			{2213, 2340, 9808, 0, 0},
			{2213, 2339, 9808, 0, 0},
			{2213, 2341, 9808, 0, 0},
			{2213, 2338, 9808, 0, 0},
			{2213, 2337, 9808, 0, 0},
			{2213, 2336, 9808, 0, 0},
			{2213, 2335, 9808, 0, 0},
			{2213, 2334, 9808, 0, 0},
			{2213, 2313, 9798, 0, 0},
			
			{3194, 1293, 1246, 0, 0},
			{3194, 1293, 1247, 0, 0},
			/**Donator Zone**/
			{ 11758, 3035, 4413, 0, 0},//bank
			{ 11758, 3036, 4413, 0, 0},//bank
			{ 11758, 3037, 4413, 0, 0},//bank
			{ 11758, 3038, 4413, 0, 0},//bank
			{ 11758, 3039, 4413, 0, 0},//bank
			{ 11758, 3040, 4413, 0, 0},//bank
			{ 11758, 3041, 4413, 0, 0},//bank
			{ 11758, 3042, 4413, 0, 0},//bank
			{ 11758, 3043, 4413, 0, 0},//bank
			
			
			{ 11758, 3043, 4413, 0, 0},//bank
			
			//staffzone
			{ 2213, 2030, 4498, 0, 0},//bank
			{ 2213, 2031, 4498, 0, 0},//bank
			{ 2213, 2032, 4498, 0, 0},//bank
			{ 2213, 2033, 4498, 0, 0},//bank
			{ 2213, 2034, 4498, 0, 0},//bank
			{ 2213, 2035, 4498, 0, 0},//bank
			{ 2213, 2036, 4498, 0, 0},//bank
			{ 2213, 2037, 4498, 0, 0},//bank
			{ 2213, 2038, 4498, 0, 0},//bank
			{ 2213, 2039, 4498, 0, 0},//bank
			{ 2213, 2040, 4498, 0, 0},//bank
			{ 2213, 2041, 4498, 0, 0},//bank
			{ 2213, 2042, 4498, 0, 0},//bank
			{ 2213, 2043, 4498, 0, 0},//bank
			{ 2213, 2785, 2614, 0, 0},//bank
			
			
			
			{-1, 3037, 4402, 0, 0}, //Pipes around rocktail
			{-1, 3038, 4402, 0, 0}, //Pipes around rocktail
			{-1, 3038, 4401, 0, 0}, //Pipes around rocktail
			{-1, 3037, 4398, 0, 0}, //Pipes around rocktail
			{-1, 3038, 4398, 0, 0}, //Pipes around rocktail
			{-1, 3040, 4398, 0, 0}, //Pipes around rocktail
			{-1, 3041, 4398, 0, 0}, //Pipes around rocktail
			{-1, 3041, 4401, 0, 0}, //Pipes around rocktail
			{-1, 3041, 4402, 0, 0}, //Pipes around rocktail
			{-1, 3041, 4399, 0, 0}, //Pipes around rocktail
			{-1, 3037, 4399, 0, 0}, //Pipes around rocktail
			{-1, 3037, 4401, 0, 0}, //Pipes around rocktail
			{-1, 3040, 4402, 0, 0}, //Pipes around rocktail
			{-1, 3043, 4391, 0, 0}, //cages
			{-1, 3043, 4393, 0, 0}, //cages
			{-1, 3041, 4391, 0, 0}, //cages
			{-1, 3041, 4393, 0, 0}, //cages
			{-1, 3039, 4391, 0, 0}, //cages
			{-1, 3039, 4393, 0, 0}, //cages
			{-1, 3037, 4393, 0, 0}, //cages
			{-1, 3037, 4391, 0, 0}, //cages
			{-1, 3042, 4388, 0, 0}, //Expiriment Tables
			{-1, 3037, 4388, 0, 0}, //Expiriment Tables
			{-1, 3036, 4385, 0, 0}, //Expiriment Tables
			{-1, 3044, 4385, 0, 0}, //Expiriment Tables
			{-1, 3037, 4384, 0, 0}, //Expiriment Tables
			{-1, 3042, 4384, 0, 0}, //Expiriment Tables
			{-1, 3036, 4388, 0, 0}, //Expiriment Tables
			{-1, 3044, 4388, 0, 0}, //Expiriment Tables
			{-1, 3041, 4385, 0, 0}, //Expiriment Tables
			{-1, 3039, 4385, 0, 0}, //Expiriment Tables
			/*End of new Donator Zone*/
			//H'ween even chest
			{2079, 2576, 9876, 0, 0},
			/**Grand EXCHANGE**/
			{8799, 3091, 3495, 0, 3},
		//lumby cows gate
		{2344, 3253, 3266, 0, 0},
		{409, 3085, 3508, 0, 1},
		{6552, 3085, 3510, 0, 1},
		{411, 3090, 3508, 0, 3},
		{ 172 , 3083, 3497, 0, 1},
		{3192, 3084, 3485, 0, 4},
		{-1, 3084, 3487, 0, 2},
		{ 10091 , 2352, 3703, 0, 2},
		
		//Aoe Zone Chest
		{2006, 2721, 2856, 0, 0},
		
		{409, 2783, 2609, 0, 0},
		{6552, 2779, 2609, 0, 0},
		{13179, 2777, 2609, 0, 0},
		{411, 2775, 2609, 0, 0},
		//banks
				{2213, 2323, 9800, 0, 0},
				{2213, 2322, 9800, 0, 0},
				{2213, 2321, 9800, 0, 0},
				{2213, 2320, 9800, 0, 0},
				{2213, 2319, 9800, 0, 0},
				{2213, 2318, 9800, 0, 0},
				
				
		/**
		 * ZULRAH
		 */
		{13405, 3083, 3492, 0, 1},
		{1, 3038, 3415, 0, 0},
		{357, 3034, 3422, 0, 0},
	{ 25136, 2278, 3070, 0, 0 },
	{ 25136, 2278, 3065, 0, 0 },
	{ 25138, 2273, 3066, 0, 0 },
	{ 25136, 2272, 3065, 0, 0 },
	{ 25139, 2267, 3065, 0, 0 },
	{ 25136, 2260, 3081, 0, 0 },
		{401, 3503, 3567, 0, 0},
		{401, 3504, 3567, 0, 0},
		{1309, 2715, 3465, 0, 0},
		{1309, 2709, 3465, 0, 0},
		{1309, 2709, 3458, 0, 0},
		{1306, 2705, 3465, 0, 0},
		{1306, 2705, 3458, 0, 0},
		{-1, 2715, 3466, 0, 0},
		{-1, 2712, 3466, 0, 0},
		{-1, 2713, 3464, 0, 0},
		{-1, 2711, 3467, 0, 0},
		{-1, 2710, 3465, 0, 0},
		{-1, 2709, 3464, 0, 0},
		{-1, 2708, 3466, 0, 0},
		{-1, 2707, 3467, 0, 0},
		{-1, 2704, 3465, 0, 0},
		{-1, 2714, 3466, 0, 0},
		{-1, 2705, 3457, 0, 0},
		{-1, 2709, 3461, 0, 0},
		{-1, 2709, 3459, 0, 0},
		{-1, 2708, 3458, 0, 0},
		{-1, 2710, 3457, 0, 0},
		{-1, 2711, 3461, 0, 0},
		{-1, 2713, 3461, 0, 0},
		{-1, 2712, 3459, 0, 0},
		{-1, 2712, 3457, 0, 0},
		{-1, 2714, 3458, 0, 0},
		{-1, 2705, 3459, 0, 0},
		{-1, 2705, 3464, 0, 0},
		{2274, 2912, 5300, 2, 0},
		{2274, 2914, 5300, 1, 0},
		{2274, 2919, 5276, 1, 0},
		{2274, 2918, 5274, 0, 0},
		{2274, 3001, 3931, 0, 0},
		{-1, 2884, 9797, 0, 2},
		{29942, 3090, 3499, 0, 2},
		{4483, 2909, 4832, 0, 3},
		{4483, 2901, 5201, 0, 2},
		{4483, 2902, 5201, 0, 2},
		{9326, 3001, 3960, 0, 0},
		{1662, 3112, 9677, 0, 2},
		{1661, 3114, 9677, 0, 2},
		{1661, 3122, 9664, 0, 1},
		{1662, 3123, 9664, 0, 2},
		{1661, 3124, 9664, 0, 3},
		{4483, 2918, 2885, 0, 3},
		{12356, 3081, 3500, 0, 0},
		{2182, 3081, 3497, 0, 0},
		{1746, 3090, 3492, 0, 1},
		{2644, 2737, 3444, 0, 0},
		{-1, 2608, 4777, 0, 0},
		{-1, 2601, 4774, 0, 0},
		{-1, 2611, 4776, 0, 0},
		/**New Member Zone*/
		{2344, 3421, 2908, 0, 0}, //Rock blocking
        {2345, 3438, 2909, 0, 0},
        {2344, 3435, 2909, 0, 0},
        {2344, 3432, 2909, 0, 0},
        {2345, 3431, 2909, 0, 0},
        {2344, 3428, 2921, 0, 1},
        {2344, 3428, 2918, 0, 1},
        {2344, 3428, 2915, 0, 1},
        {2344, 3428, 2912, 0, 1},
        {2345, 3428, 2911, 0, 1},
        {2344, 3417, 2913, 0, 1},
        {2344, 3417, 2916, 0, 1},
        {2344, 3417, 2919, 0, 1},
        {2344, 3417, 2922, 0, 1},
        {2345, 3417, 2912, 0, 0},
        {2346, 3418, 2925, 0, 0},
        {10378, 3426, 2907, 0, 0},
        {8749, 3426, 2923, 0, 2}, //Altar
        {-1, 3420, 2909, 0, 10}, //Remove crate by mining
        {-1, 3420, 2923, 0, 10}, //Remove Rockslide by Woodcutting
        {14859, 3421, 2909, 0, 0}, //Mining
        {14859, 3419, 2909, 0, 0},
        {14859, 3418, 2910, 0, 0},
        {14859, 3418, 2911, 0, 0},
        {14859, 3422, 2909, 0, 0},
        {1306, 3418, 2921, 0, 0}, //Woodcutting
        {1306, 3421, 2924, 0, 0},
        {1306, 3420, 2924, 0, 0},
        {1306, 3419, 2923, 0, 0},
        {1306, 3418, 2922, 0, 0},
        
	
		
		
		{-1, 3430, 2912, 0, 2}, 
		{13493, 3424, 2916, 0, 1},//Armour  stall
        /**New Member Zone end*/
		{-1, 3098, 3496, 0, 1},
		{-1, 3095, 3498, 0, 1},
		{-1, 3088, 3509, 0, 1},
		{-1, 3095, 3499, 0, 1},
		{-1, 3085, 3506, 0, 1},
		{30205, 3085, 3509, 0, 3},
		{-1, 3206, 3263, 0, 0},
		{-1, 2794, 2773, 0, 0},
		{2, 2692, 3712, 0, 3},
		{2, 2688, 3712, 0, 1},
		{4483, 3081, 3496, 0, 1},
		{4483, 3081, 3495, 0, 1},
		{409, 3084, 3495, 0, 1},
		{411, 3084, 3493, 0, 1},
		{6552, 3084, 3489, 0, 1},
		{410, 3084, 3487, 0, 2},
		{4875, 3221, 3431, 0, 0},
		{4874, 3221, 3432, 0, 0},
		{4876, 3221, 3433, 0, 0},
		{4877, 3221, 3434, 0, 0},
		{4878, 3221, 3435, 0, 0},
		{ 11758, 3019, 9740, 0, 0},
		{ 11758, 3020, 9739, 0, 1},
		{ 11758, 3019, 9738, 0, 2},
		{ 11758, 3018, 9739, 0, 3},
		{ 11933, 3028, 9739, 0, 1},
		{ 11933, 3032, 9737, 0, 2},
		{ 11933, 3032, 9735, 0, 0},
		{ 11933, 3035, 9742, 0, 3},
		{ 11933, 3034, 9739, 0, 0},
		{ 11936, 3028, 9737, 0, 1},
		{ 11936, 3029, 9734, 0, 2},
		{ 11936, 3031, 9739, 0, 0},
		{ 11936, 3032, 9741, 0, 3},
		{ 11936, 3035, 9734, 0, 0},
		{ 11954, 3037, 9739, 0, 1},
		{ 11954, 3037, 9735, 0, 2},
		{ 11954, 3037, 9733, 0, 0},
		{ 11954, 3039, 9741, 0, 3},
		{ 11954, 3039, 9738, 0, 0},
		{ 11963, 3039, 9733, 0, 1},
		{ 11964, 3040, 9732, 0, 2},
		{ 11965, 3042, 9734, 0, 0},
		{ 11965, 3044, 9737, 0, 3},
		{ 11963, 3042, 9739, 0, 0},
		{ 11963, 3045, 9740, 0, 1},
		{ 11965, 3043, 9742, 0, 2},
		{ 11964, 3045, 9744, 0, 0},
		{ 11965, 3048, 9747, 0, 3},
		{ 11951, 3048, 9743, 0, 0},
		{ 11951, 3049, 9740, 0, 1},
		{ 11951, 3047, 9737, 0, 2},
		{ 11951, 3050, 9738, 0, 0},
		{ 11951, 3052, 9739, 0, 3},
		{ 11951, 3051, 9735, 0, 0},
		{ 11947, 3049, 9735, 0, 1},
		{ 11947, 3049, 9734, 0, 2},
		{ 11947, 3047, 9733, 0, 0},
		{13179, 3090, 3510, 0, 3},
		{ 11947, 3046, 9733, 0, 3},
		{ 11947, 3046, 9735, 0, 0},
		{ 11941, 3053, 9737, 0, 1},
		{ 11939, 3054, 9739, 0, 2},
		{ 11941, 3053, 9742, 0, 0},
		{ 14859, 3038, 9748, 0, 3},
		{ 14859, 3044, 9753, 0, 0},
		{ 14859, 3048, 9754, 0, 1},
		{ 14859, 3054, 9746, 0, 2},
		{ 4306, 3026, 9741, 0, 0},
		{ 6189, 3022, 9742, 0, 1},
		{ 172 , 3077, 3497, 0, 0},
		{ 75 , 2914, 3452, 0, 2},
		{ 11758, 3449, 3722, 0, 0},
		{ 11758, 3450, 3722, 0, 0},
		{ 50547, 3445, 3717, 0, 3},
		{2465, 3085, 3512, 0, 0},
		{ -1, 3090, 3496, 0, 0},
		{ -1, 3090, 3494, 0, 0},
		{ -1, 3092, 3496, 0, 0},
		
		{ -1, 3659, 3508, 0, 0},
		{ 4053, 3660, 3508, 0, 0},
		{ 4051, 3659, 3508, 0, 0},
		{ 1, 3649, 3506, 0, 0},
		{ 1, 3650, 3506, 0, 0},
		{ 1, 3651, 3506, 0, 0},
		{ 1, 3652, 3506, 0, 0},
		{ 8702, 3423, 2911, 0, 0},
		{ 11356, 1297, 1246, 0, 0},
		{ -1, 2860, 9734, 0, 1},
		{ -1, 2857, 9736, 0, 1},
		{ 664, 2859, 9742, 0, 1},
		{ 1167, 2860, 9742, 0, 1},
		{ 5277, 3691, 3465, 0, 2},
		{ 5277, 3690, 3465, 0, 2},
		{ 5277, 3688, 3465, 0, 2},
		{ 5277, 3687, 3465, 0, 2},
		{ 114, 3093, 3933, 0, 0},
		
		
		{2213, 3212, 3439, 0, 0},
		{2213, 3213, 3439, 0, 0},
		{13405, 3205, 3438, 0, 0},
		
		{4875, 3259, 2886, 0, 1},
		{4874, 3260, 2886, 0, 1},
		{4005, 3265, 2889, 0, 1},
		{4876, 3261, 2885, 0, 0},
		{4877, 3261, 2884, 0, 0},
		{4878, 3261, 2883, 0, 0},
		{28716, 3258, 2854, 0, 0},


		
		{-1, 3217, 3436, 0, 0},
		{-1, 3219, 3436, 0, 0},
		{-1, 3220, 3431, 0, 0},
		{-1, 3220, 3425, 0, 0},
		
		{172, 3220, 3426, 0, 1},

		{2654, 3269, 2889, 0, 0},
		{2654, 3269, 2878, 0, 0},



		{409, 3225, 3432, 0, 3},
		{6552, 3225, 3429, 0, 1},
		{411, 3225, 3427, 0, 1},
		{13179, 3225, 3425, 0, 1},

		{1, 3302, 9830, 0, 0},
		{1, 3293, 9828, 0, 0},
		{1, 3301, 9821, 0, 0},
		{1, 3292, 9820, 0, 0},
		
		
		{14859, 3283, 3946, 0, 0},
		{14859, 3284, 3946, 0, 0},
		{14859, 3285, 3946, 0, 0},
		{14859, 3286, 3946, 0, 0},
		{14859, 3287, 3946, 0, 0},
		{11963, 3288, 3946, 0, 0},
		{11963, 3289, 3946, 0, 0},
		{11951, 3290, 3946, 0, 0},
		
		{4306, 3284, 3942, 0, 0},
		
		{8702, 3281, 3941, 0, 0},
		
		{2646, 3289, 3940, 0, 0},
		{2646, 3288, 3940, 0, 0},
		{2646, 3289, 3941, 0, 0},
		{2646, 3288, 3941, 0, 0},
		
		{2644, 3285, 3942, 0, 0},
		
		{1306, 3277, 3941, 0, 0},
		{1306, 3279, 3943, 0, 0},
		{1306, 3281, 3945, 0, 0},
		
		{-1, 3283, 3944, 0, 0},
		{-1, 3286, 3944, 0, 0},
		
		{170, 3290, 3944, 0, 3},
		
		{2732, 3276, 3938, 0, 0},
		
		{-1, 3294, 3946, 0, 0},
		{-1, 3293, 3945, 0, 0},
		
		{-1, 3092, 3488, 0, 0},
	};
	/** SERVER SPAWNS **/
	private static final int[][] CUSTOM_OBJECTS_SPAWNS = {
			{2213, 3267, 3884, 2, 0}, // home banks
			{2213, 3266, 3883, 2, 0}, // home banks
			{2213, 3265, 3884, 2, 0}, // home banks
			
			{15_000, 1830, 3091, 2, 0}, // ICE COIN START
			{15_000, 1829, 3091, 2, 0},
			{15_000, 1828, 3091, 2, 0},
			{15_000, 1827, 3091, 2, 0},
			{15_000, 1826, 3091, 2, 0},
			{15_000, 1825, 3091, 2, 0},
			{15_000, 1824, 3091, 2, 0}, //ICE COIN END
		
			{2009, 2295, 3330, 0, 0}, //raids 1 entrance
			{3012, 1242, 1246, 0, 0}, //silver raids entrance
			{2007, 2904, 2513, 0, 0}, // gold raids entrance
			{3000, 3098, 3505, 0, 0}, //plat door
			{4001, 3096, 3939, 0, 0}, // plat chest
			{2014, 2301, 3327, 0, 0}, // pokemon raids chest
			{2043, 3059, 2844, 0, 0}, // Anime raids chest
			{2044, 1245, 1249, 0, 0}, // Silver chest
			{3009, 2901, 2516, 0, 0}, // gold chest
			{3008, 3282, 2893, 0, 0},
			{3010, 3061, 2840, 0, 0},
			{5273, 2549, 3757, 0, 0},
			{5274, 2550, 3756, 0, 0},
			{5275, 2547, 3756, 0, 0},
			{5278, 2545, 3756, 0, 0},
			{5279, 2543, 3756, 0, 0},
			{3013, 2847, 3104, 0, 1}, //mortal door

			{2008, 2837, 3098, 0, 0},
			
			{-1, 3084, 3510, 0, 0},
			{-1, 3078, 3510, 0, 0},
			{-1, 3080, 3510, 0, 0},
			{-1, 3081, 3510, 0, 0},
			{-1, 3077, 3512, 0, 0},

			{-1, 3076, 3509, 0, 0},
			{-1, 3076, 3510, 0, 0},
			{-1, 3076, 3511, 0, 0},
			{-1, 3076, 3512, 0, 0},
			
			{-1, 3077, 3513, 0, 0},
			{-1, 3078, 3513, 0, 0},
			{-1, 3079, 3513, 0, 0},
			{-1, 3080, 3513, 0, 0},

			{-1, 3084, 3512, 0, 0},
			{-1, 3084, 3509, 0, 0},
			{-1, 3083, 3507, 0, 0},
			
			{-1, 3079, 3507, 0, 0},
			{-1, 3077, 3507, 0, 0},




			/*
			 * END HOME
			 */
			{-1, 3092, 3488, 0, 0},
			
			{-1, 3294, 3946, 0, 0},
			{-1, 3293, 3945, 0, 0},
			
			{2732, 3276, 3938, 0, 0},
			
			
			{170, 3290, 3944, 0, 3},
			
			{-1, 3283, 3944, 0, 0},
			{-1, 3286, 3944, 0, 0},
			
			{14859, 3283, 3946, 0, 0},
			{14859, 3284, 3946, 0, 0},
			{14859, 3285, 3946, 0, 0},
			{14859, 3286, 3946, 0, 0},
			{14859, 3287, 3946, 0, 0},
			{11963, 3288, 3946, 0, 0},
			{11963, 3289, 3946, 0, 0},
			{11951, 3290, 3946, 0, 0},
			
			{4306, 3284, 3942, 0, 0},
			
			{8702, 3281, 3941, 0, 0},
			
			{2646, 3289, 3940, 0, 0},
			{2646, 3288, 3940, 0, 0},
			{2646, 3289, 3941, 0, 0},
			{2646, 3288, 3941, 0, 0},
			
			{2644, 3285, 3942, 0, 0},
			
			{1306, 3277, 3941, 0, 0},
			{1306, 3279, 3943, 0, 0},
			{1306, 3281, 3945, 0, 0},
			
			
			{1, 3302, 9830, 0, 0},
			
			
			{-1, 2309, 9804, 0, 0},
			{-1, 2310, 9804, 0, 0},
			{-1, 2308, 9804, 0, 0},
			{-1, 2307, 9804, 0, 0},
			{-1, 2309, 9805, 0, 0},
			{-1, 2310, 9805, 0, 0},
			{-1, 2308, 9805, 0, 0},
			{-1, 2307, 9805, 0, 0},
			
			
			{409, 3225, 3432, 0, 3},
			{6552, 3225, 3429, 0, 1},
			{411, 3225, 3427, 0, 1},
			{13179, 3225, 3425, 0, 1},
			
			{2213, 3212, 3439, 0, 0},
			{2213, 3213, 3439, 0, 0},
			{13405, 3205, 3438, 0, 0},
			
			{884, 3207, 3422, 0, 1},
			
			{172, 3220, 3426, 0, 1},

			
			{4875, 3221, 3431, 0, 0},
			{4874, 3221, 3432, 0, 0},
			{4876, 3221, 3433, 0, 0},
			{4877, 3221, 3434, 0, 0},
			{4878, 3221, 3435, 0, 0},
			
			{-1, 3217, 3436, 0, 0},
			{-1, 3219, 3436, 0, 0},
			{-1, 3220, 3431, 0, 0},
			{-1, 3220, 3425, 0, 0},
			{2079, 2576, 9876, 0, 0},	
			
			
			/**
			 * ZULRAH
			 */
			{ 25136, 2278, 3070, 0, 0 },
			{ 25136, 2278, 3065, 0, 0 },
			{ 25138, 2273, 3066, 0, 0 },
			{ 25136, 2272, 3065, 0, 0 },
			{ 25139, 2267, 3065, 0, 0 },
			{ 25136, 2260, 3081, 0, 0 },
			{1, 3038, 3415, 0, 0},
			{357, 3034, 3422, 0, 0},
			{ -1, 2265, 3071, 0, 0 },
			{ -1, 2271, 3071, 0, 0 },
			{ 172 , 3083, 3497, 0, 1},
			{3192, 3084, 3485, 0, 4},
			{409, 3085, 3508, 0, 1},
			{6552, 3085, 3510, 0, 1},
			{411, 3090, 3508, 0, 3},
			
			//staffzone
			{ 2213, 2030, 4498, 0, 0},//bank
			{ 2213, 2031, 4498, 0, 0},//bank
			{ 2213, 2032, 4498, 0, 0},//bank
			{ 2213, 2033, 4498, 0, 0},//bank
			{ 2213, 2034, 4498, 0, 0},//bank
			{ 2213, 2035, 4498, 0, 0},//bank
			{ 2213, 2036, 4498, 0, 0},//bank
			{ 2213, 2037, 4498, 0, 0},//bank
			{ 2213, 2038, 4498, 0, 0},//bank
			{ 2213, 2039, 4498, 0, 0},//bank
			{ 2213, 2040, 4498, 0, 0},//bank
			{ 2213, 2041, 4498, 0, 0},//bank
			{ 2213, 2042, 4498, 0, 0},//bank
			{ 2213, 2043, 4498, 0, 0},//bank
			
			{-1, 3084, 3487, 0, 2},
			
			{ 2274, 3652, 3488, 0, 0 },
			
				{13405, 3083, 3492, 0, 1},
			
		//lumby cows gate
		{2344, 3253, 3266, 0, 1},
		
		
		
		//gamble zone
		{2213, 2842, 5143, 0, 0},
		{2213, 2843, 5143, 0, 0},
		{2213, 2844, 5143, 0, 0},
		{2213, 2845, 5143, 0, 0},
		{2213, 2846, 5143, 0, 0},
		{2213, 2847, 5143, 0, 0},
		{2213, 2848, 5143, 0, 0},
		{2213, 2849, 5143, 0, 0},
		{2213, 2850, 5143, 0, 0},
		{2213, 2851, 5143, 0, 0},
		
		
		{2274, 3652, 3488, 0, 0},
		/**Jail Start*/
		
	};
	public static boolean cloudExists(Location loc) {
		return getCloudObject(loc);
	}

	
	
	public static boolean getCloudObject(Location loc) {
		for (GameObject objects : CUSTOM_OBJECTS) {
			System.out.println(loc);
			if (objects.inLocation(objects.getPosition().getX(), objects.getPosition().getY(), Locations.Location.ZULRAH_CLOUD_FIVE)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	
	
	public static void zulrahToxicClouds(final GameObject cloud, final Player player, final int cycles) {
		player.setInteractingObject(cloud);
		spawnGlobalObject(cloud);
		TaskManager.submit(new Task(cycles) {
			@Override
			public void execute() {
				deleteGlobalObject(cloud);
				World.deregister(cloud);
				player.setCloudsSpawned(false);
				if (player.getInteractingObject() != null
						&& player.getInteractingObject().getId() == 11700) {
					player.setInteractingObject(null);
				}
				this.stop();
			}

			@Override
			public void stop() {
				setEventRunning(false);
			}
		});

	}
}
