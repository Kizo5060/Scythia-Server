package com.ruseps.world.content.skill.impl.slayer;

import com.ruseps.model.Position;
import com.ruseps.util.Misc;

/**
 * @author Gabriel Hannason 
 */

public enum SlayerTasks {

	NO_TASK(null, -1, null, -1, null),

	/**
	 * Easy tasks
	 */
	ABOMBINABLE_SNOWMAN(SlayerMaster.VANNAKA, 5049, "Abombinable Snowman can be found in the Monsters Teleport.", 2100, new Position(2520, 4839, 0)),
	RYUK(SlayerMaster.VANNAKA, 4990, "Ryuk can be found in the Monster Teleport." , 1000, new Position(2465, 4779, 0)),
	JESUS(SlayerMaster.VANNAKA, 4991, "Jesus can be found in the Monster Teleport.", 2150, new Position(2329, 4637, 0)),
	SIMBA(SlayerMaster.VANNAKA, 4992, "Simba can be found in Monster Teleport.", 2000, new Position(2524, 4777, 0)),
	/**
	 * Medium tasks
	 */
	SULLY(SlayerMaster.DURADEL, 4994, "Sully can be found in the Monster Teleport.", 6500, new Position(2532, 4650)),
	KIDSORA(SlayerMaster.DURADEL, 4999, "Kid Sora can be found in the Monster Teleport.", 6700, new Position(1819, 4268)),
	CHARIZARD(SlayerMaster.DURADEL, 4981, "Mega Charizard can be found in the Monster Teleport.", 6600, new Position(2523, 2542)),
	SAURON(SlayerMaster.DURADEL, 4997, "Sauron can be found in the Monster Teleport.", 6900, new Position(2588, 2593)),
	SQUIDWARD(SlayerMaster.DURADEL, 4993, "Squidward-o-saurus can be found in the Monster Teleport.", 7500, new Position(2892, 2726)),
	ICEDEMON(SlayerMaster.DURADEL, 4980, "Ice Demons can be found in the Monster Teleport.", 8000, new Position(2639, 4054, 1)),
	/**
	 * Hard tasks
	 */
	EVE(SlayerMaster.KURADEL, 4271, "Eve can be found in the Monster Teleport.", 12500, new Position(2670, 3984, 1)),
	GIMLEE(SlayerMaster.KURADEL, 4265, "Gimlee can be found in the Monster Teleport.", 12500, new Position(2306, 4590)),
	BLOOD_ELEMENTAL(SlayerMaster.KURADEL, 4267, "Blood Elemental can be found in the Monster Teleport.", 14000, new Position(1930, 4641)),
	TIKI(SlayerMaster.KURADEL, 4268, "Tiki Demon can be found in the Monster Teleport.", 13400, new Position(2796, 2776)),
	ARAGORN(SlayerMaster.KURADEL, 4270, "Aragorn can be found in the Monster Teleport.", 13877, new Position(2711, 9510)),
	RAYQUAZA(SlayerMaster.KURADEL, 4275, "Rayquaza can be found in the Monster Teleport.", 15600, new Position(3035, 4497)),
	LEGOLAS(SlayerMaster.KURADEL, 3008, "Legolas can be found in the Monster Teleport.", 15600, new Position(2421, 4686)),
	/**
	 * Elite
	 */
	DEADLY_ROBOT(SlayerMaster.SUMONA, 4264, "Deadly Robot's can be found in the Boss Teleport.", 68000, new Position(3130, 4410)),
	DARIUS(SlayerMaster.SUMONA, 4263, "Darius can be found in the Boss Teleport.", 68000, new Position(2704, 9756)),
	DIAMOND_HEAD(SlayerMaster.SUMONA, 4998, "Diamond Head can be found in the Boss Teleport.", 68000, new Position(2704, 9756)),
	DARTH_MAUL(SlayerMaster.SUMONA, 5048, "Darth Maul can be found in the Boss Teleport.", 68000, new Position(1904, 5408)),
	ZELDORADO(SlayerMaster.SUMONA, 4606, "Zeldorado can be found in the Boss Teleport.", 68000, new Position(2799, 3321)),
	HEAT_BLAST(SlayerMaster.SUMONA, 4266, "Heat Blast can be found in the Boss Teleport.", 68000, new Position(3561, 9948, 0)),
	KEVIN(SlayerMaster.SUMONA, 4269, "Kevin Four Arms can be found in the Boss Teleport.", 68000, new Position(2867, 9946)),
	GOLDEN_KNIGHT(SlayerMaster.SUMONA, 4272, "Ra can be found in the Boss Teleport.", 68000, new Position(2403, 3482)),
	DARK_KNIGHT(SlayerMaster.SUMONA, 3009, "Dark Knight can be found in the Boss Teleport.", 68000, new Position(2835, 4580)),
	BAD_BITCH(SlayerMaster.SUMONA, 4274, "Bad Bitches can be found in the Boss Teleport.", 68000, new Position(2595, 4579)),
	CANNONBOLT(SlayerMaster.SUMONA, 3010, "Cannonbolt can be found in the Boss Teleport.", 68000, new Position(2718, 9805)),
	RED_ASSASSIN(SlayerMaster.SUMONA, 3011, "Red Assassin be found in the Boss Teleport.", 68000, new Position(2538,10143, 4)),
	EVIL_ASS_CLOWN_IT(SlayerMaster.SUMONA, 3014, "Evil Ass Clown It can be found in the Boss Teleport.", 68000, new Position(2899, 3618));
	
	/*
	 * Suomi add new boss tasks in here:
	 * 
	 * look below for example
	 *  npc name     slayer master        npc id        description of location                        xp per kill   position for teleing to task
	 * 	BARREL_CHEST(SlayerMaster.SUMONA, 5666, "The Barrelchests can be found using the Boss teleport.", 26000, new Position(2973, 9517));

	 */
	
	
	
	/*
	 * end of suomis new tasks
	 */

	;

	private SlayerTasks(SlayerMaster taskMaster, int npcId, String npcLocation, int XP, Position taskPosition) {
		this.taskMaster = taskMaster;
		this.npcId = npcId;
		this.npcLocation = npcLocation;
		this.XP = XP;
		this.taskPosition = taskPosition;
	}

	private SlayerMaster taskMaster;
	private int npcId;
	private String npcLocation;
	private int XP;
	private Position taskPosition;

	public SlayerMaster getTaskMaster() {
		return this.taskMaster;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String getNpcLocation() {
		return this.npcLocation;
	}

	public int getXP() {
		return this.XP;
	}

	public Position getTaskPosition() {
		return this.taskPosition;
	}

	public static SlayerTasks forId(int id) {
		for (SlayerTasks tasks : SlayerTasks.values()) {
			if (tasks.ordinal() == id) {
				return tasks;
			}
		}
		return null;
	}

	public static int[] getNewTaskData(SlayerMaster master) {
		int slayerTaskId = 1, slayerTaskAmount = 20;
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0;

		/*
		 * Calculating amount of tasks
		 */
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.getTaskMaster() == SlayerMaster.VANNAKA)
				easyTasks++;
			else if (task.getTaskMaster() == SlayerMaster.DURADEL)
				mediumTasks++;
			else if (task.getTaskMaster() == SlayerMaster.KURADEL)
				hardTasks++;
			else if (task.getTaskMaster() == SlayerMaster.SUMONA)
				eliteTasks++;
		}

		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 40 + Misc.getRandom(15);
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = 28 + Misc.getRandom(13);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = 37 + Misc.getRandom(20);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = 45 + Misc.getRandom(10);
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}
}
