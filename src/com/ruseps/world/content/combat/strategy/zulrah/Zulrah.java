package com.ruseps.world.content.combat.strategy.zulrah;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruseps.model.Animation;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Blue;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Green;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Red;
import com.ruseps.world.entity.impl.player.Player;


public class Zulrah {

	public final static Position NORTH_POSITION = new Position(2268, 3074);
	public final static Position WEST_POSITION = new Position(2258, 3075);
	public final static Position EAST_POSITION = new Position(2278, 3075);
	public final static Position SOUTH_POSITION = new Position(2268, 3064);
	
	
	
	public final static int[][] TOXIC_FUME_LOCATIONS_1 = { { 2263, 3076 }, { 2263, 3073 }, { 2263, 3070 }, { 2266, 3069 },
													{ 2269, 3069 }, { 2272, 3070 }, { 2272, 3073 }, { 2273, 3076 } };
	
	public final static int[][] TOXIC_FUME_LOCATIONS_2 = { { 2263, 3070 }, { 2266, 3069 }, { 2269, 3069 }, { 2272, 3070 } };
	
	public final static int CRIMSON_ZULRAH_ID = 2044;
	public final static int GREEN_ZULRAH_ID = 2043;
	public final static int BLUE_ZULRAH_ID = 2042;
	
	public final static Animation RISE = new Animation(5073);
	public final static Animation DIVE = new Animation(5072);
	
	public static List<ZulrahRotation> zulrahRotations = new ArrayList<>();
	public static Map<Integer, ZulrahPhase> zulrahPhases = new HashMap<>();
	
	public static void initialize() {
		setRotations();
		setPhases();
	}

	private static void setRotations() {
		zulrahRotations.add(new ZulrahRotation(1, "first rotation"));
	}
	
	private static void setPhases() {
		zulrahPhases.put(1, new ZulrahPhase(2, "Full cloud", Green.class, NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(2, new ZulrahPhase(3, "Normal melee attack", Red.class, NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(3, new ZulrahPhase(4, "Normal mage attack", Blue.class, NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(4, new ZulrahPhase(5, "Snakelings, right clouds", Green.class, SOUTH_POSITION.getX(), SOUTH_POSITION.getY()));
		zulrahPhases.put(5, new ZulrahPhase(6, "Normal melee attack", Red.class, NORTH_POSITION.getX(), NORTH_POSITION.getY()));
		zulrahPhases.put(6, new ZulrahPhase(7, "Normal mage attack", Blue.class, WEST_POSITION.getX(), WEST_POSITION.getY()));
		zulrahPhases.put(7, new ZulrahPhase(8, "Snakelings, right clouds", Green.class, SOUTH_POSITION.getX(), SOUTH_POSITION.getY()));
		zulrahPhases.put(8, new ZulrahPhase(9, "Range/Mage switch", Green.class, WEST_POSITION.getX(), WEST_POSITION.getY()));
		zulrahPhases.put(9, new ZulrahPhase(1, "Normal melee", Red.class, NORTH_POSITION.getX(), NORTH_POSITION.getY()));
	}
	
	public static void startBossFight(Player player) {

		ZulrahRotation rotation = zulrahRotations.get(0);
		
		int initialPhase = 0;
		switch(rotation.getRotationID()) {
			case 1:
				initialPhase = 1;
				break;	
			case 2:
				initialPhase = 2;
				break;	
			case 3:
				initialPhase = 3;
				break;
		}

		player.sendMessage("Zulrah has decided to use "+rotation.getRotationName());
		player.sendMessage("Your fight with Zulrah has begun.");
		
		try {
			getNextPhase(player, 4000, initialPhase);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void getNextPhase(Player player, long zulrahConstitution, int phaseID) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ZulrahPhase phase = zulrahPhases.get(phaseID);
		Method method = phase.phaseClass().getMethod("spawn", Player.class, int.class, int.class);
		try {
			method.invoke(phase.phaseClass().newInstance(), player, (int) zulrahConstitution, phaseID);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

}
