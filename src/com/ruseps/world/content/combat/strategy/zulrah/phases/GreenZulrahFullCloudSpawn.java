package com.ruseps.world.content.combat.strategy.zulrah.phases;

import com.ruseps.world.content.combat.strategy.zulrah.npc.Green;
import com.ruseps.world.entity.impl.Character;


public class GreenZulrahFullCloudSpawn {
	
	public static void init(Character entity, Character victim) {
		Green.setIdle(true);
	}

}
