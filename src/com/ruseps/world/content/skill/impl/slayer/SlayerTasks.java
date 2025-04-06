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
	ABOMBINABLE_SNOWMAN(SlayerMaster.VANNAKA, 5049, "Abombinable Snowman can be found in the Monsters Teleport.", 50, new Position(2520, 4839, 0)),
	RYUK(SlayerMaster.VANNAKA, 4990, "Ryuk can be found in the Monster Teleport." , 75, new Position(2465, 4779, 0)),
	JESUS(SlayerMaster.VANNAKA, 4991, "Jesus can be found in the Monster Teleport.", 75, new Position(2335, 4633, 2)),
	SIMBA(SlayerMaster.VANNAKA, 4992, "Simba can be found in Monster Teleport.", 75, new Position(2524, 4762, 0)),
	/**
	 * Medium tasks
	 */
	SULLY(SlayerMaster.DURADEL, 4994, "Sully can be found in the Monster Teleport.", 250, new Position(2532, 4650)),
	KIDSORA(SlayerMaster.DURADEL, 4999, "Kid Sora can be found in the Monster Teleport.", 300, new Position(2844, 4068)),
	CHARIZARD(SlayerMaster.DURADEL, 4981, "Mega Charizard can be found in the Monster Teleport.", 300, new Position(2524, 2532)),
	SAURON(SlayerMaster.DURADEL, 4997, "Sauron can be found in the Monster Teleport.", 300, new Position(2588, 2593)),
	SQUIDWARD(SlayerMaster.DURADEL, 4993, "Squidward-o-saurus can be found in the Monster Teleport.", 300, new Position(3295, 4065)),
	ICEDEMON(SlayerMaster.DURADEL, 4980, "Ice Demons can be found in the Monster Teleport.", 300, new Position(2655, 4069)),
	/**
	 * Hard tasks
	 */
	EVE(SlayerMaster.KURADEL, 4271, "Eve can be found in the Monster Teleport.", 450, new Position(2665, 3992)),
	GIMLEE(SlayerMaster.KURADEL, 4265, "Gimlee can be found in the Monster Teleport.", 450, new Position(2336, 4582)),
	BLOOD_ELEMENTAL(SlayerMaster.KURADEL, 4267, "Blood Elemental can be found in the Monster Teleport.", 450, new Position(3487, 2717)),
	TIKI(SlayerMaster.KURADEL, 4268, "Tiki Demon can be found in the Monster Teleport.", 450, new Position(3289, 4123)),
	ARAGORN(SlayerMaster.KURADEL, 4270, "Aragorn can be found in the Monster Teleport.", 450, new Position(2719, 9500)),
	RAYQUAZA(SlayerMaster.KURADEL, 4275, "Rayquaza can be found in the Monster Teleport.", 450, new Position(3035, 4497)),
	LEGOLAS(SlayerMaster.KURADEL, 3008, "Legolas can be found in the Monster Teleport.", 450, new Position(2409, 4691)),
	/**
	 * Elite
	 */
	DEADLY_ROBOT(SlayerMaster.SUMONA, 4264, "Deadly Robot's can be found in the Boss Teleport.", 575, new Position(3114, 4388)),
	DARIUS(SlayerMaster.SUMONA, 4263, "Darius can be found in the Boss Teleport.", 575, new Position(2704, 9756)),
	DIAMOND_HEAD(SlayerMaster.SUMONA, 4998, "Diamond Head can be found in the Boss Teleport.", 575, new Position(2717, 3048)),
	DARTH_MAUL(SlayerMaster.SUMONA, 5048, "Darth Maul can be found in the Boss Teleport.", 575, new Position(1904, 5408)),
	ZELDORADO(SlayerMaster.SUMONA, 4606, "Zeldorado can be found in the Boss Teleport.", 575, new Position(2798, 3367)),
	HEAT_BLAST(SlayerMaster.SUMONA, 4266, "Heat Blast can be found in the Boss Teleport.", 575, new Position(3561, 9948, 0)),
	KEVIN(SlayerMaster.SUMONA, 4269, "Kevin Four Arms can be found in the Boss Teleport.", 575, new Position(2867, 9946)),
	GOLDEN_KNIGHT(SlayerMaster.SUMONA, 4272, "Ra can be found in the Boss Teleport.", 575, new Position(2403, 3482)),
	DARK_KNIGHT(SlayerMaster.SUMONA, 3009, "Dark Knight can be found in the Boss Teleport.", 575, new Position(2835, 4580)),
	BAD_BITCH(SlayerMaster.SUMONA, 4274, "Bad Bitches can be found in the Boss Teleport.", 575, new Position(2595, 4579)),
	CANNONBOLT(SlayerMaster.SUMONA, 3010, "Cannonbolt can be found in the Boss Teleport.", 575, new Position(2720, 9828)),
	RED_ASSASSIN(SlayerMaster.SUMONA, 3011, "Red Assassin be found in the Boss Teleport.", 575, new Position(3284,4972)),
	EVIL_ASS_CLOWN_IT(SlayerMaster.SUMONA, 3014, "Evil Ass Clown It can be found in the Boss Teleport.", 575, new Position(2899, 3618)),
	DARK_MAGICIAN_GIRL(SlayerMaster.SUMONA, 1506, "Magician Girl can be found in the Boss Teleport.", 575, new Position(2777, 3287)),
	DOOMS_DAY(SlayerMaster.SUMONA, 185, "Dooms Day can be found in the Boss Teleport.", 575, new Position(3420, 5276)),
	BLOOD_QUEEN(SlayerMaster.SUMONA, 1508, "Blood Queen can be found in the Boss Teleport.", 575, new Position(3039, 4835)),
	OBELISK_DESTROYER(SlayerMaster.SUMONA, 1510, "Obelisk the Destroyer can be found in the Boss Teleport.", 575, new Position(2383, 3890)),
	
	/**
	 * Multi Tasks
	 */
	OCANTE(SlayerMaster.CHAELDAR, 1417, "Octane can be found in the Multi-Boss Teleport.", 1500, new Position(2403, 2910)),
	LIMES(SlayerMaster.CHAELDAR, 1017, "Limes can be found in the Multi-Boss Teleport.", 1500,new Position(2719, 3874)),
	SLIFER(SlayerMaster.CHAELDAR, 1018, "Slifer can be found in the Multi-Boss Teleport.", 1500,new Position(2208, 3802)),
	FALLEN_GOD(SlayerMaster.CHAELDAR, 1039, "Itachi can be found in the Multi-Boss Teleport.", 1500,new Position(3421, 3996)),
	KNIGHT_OF_TORMENT(SlayerMaster.CHAELDAR, 1038, "Knights can be found in the Multi-Boss Teleport.", 1500,new Position(3232, 3039)),
	THE_LITCH(SlayerMaster.CHAELDAR, 1494, "Kurama can be found in the Multi-Boss Teleport.", 1500,new Position(2517, 10147)),
	SPUDERMAN(SlayerMaster.CHAELDAR, 3012, "SPUDERMAN can be found in the Multi-Boss Teleport.", 1500,new Position(3376, 3214)),
	DR_STRANGE(SlayerMaster.CHAELDAR, 3007, "Dr. Strange can be found in the Multi-Boss Teleport.", 1500,new Position(2272, 4696)),
	//GODS_RULER(SlayerMaster.(CHAELDAR), 1511, "Gods Ruler can be found in the Multi-Boss Teleport.", 68000,new Position(3038, 5346)),
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
		int easyTasks = 0, mediumTasks = 0, hardTasks = 0, eliteTasks = 0, multiTasks = 0;

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
			else if (task.getTaskMaster() == SlayerMaster.CHAELDAR)
				multiTasks++;
		}

		if (master == SlayerMaster.VANNAKA) {
			slayerTaskId = 1 + Misc.getRandom(easyTasks);
			if (slayerTaskId > easyTasks)
				slayerTaskId = easyTasks;
			slayerTaskAmount = 30 + Misc.getRandom(25);
		} else if (master == SlayerMaster.DURADEL) {
			slayerTaskId = easyTasks - 1 + Misc.getRandom(mediumTasks);
			slayerTaskAmount = 45 + Misc.getRandom(35);
		} else if (master == SlayerMaster.KURADEL) {
			slayerTaskId = 1 + easyTasks + mediumTasks + Misc.getRandom(hardTasks - 1);
			slayerTaskAmount = 55 + Misc.getRandom(50);
		} else if (master == SlayerMaster.SUMONA) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + Misc.getRandom(eliteTasks - 1);
			slayerTaskAmount = 75 + Misc.getRandom(80);
		} else if (master == SlayerMaster.CHAELDAR) {
			slayerTaskId = 1 + easyTasks + mediumTasks + hardTasks + eliteTasks + Misc.getRandom(multiTasks - 1);
			slayerTaskAmount = 65 + Misc.getRandom(45);	
		}
		return new int[] { slayerTaskId, slayerTaskAmount };
	}
}
