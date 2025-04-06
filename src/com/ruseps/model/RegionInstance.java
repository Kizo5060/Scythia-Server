package com.ruseps.model;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ruseps.world.World;
import com.ruseps.world.content.minigames.impl.Barrows;
import com.ruseps.world.content.minigames.impl.WarriorsGuild;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class RegionInstance {

	public enum RegionInstanceType {
		BARROWS,
		GRAVEYARD,
		FIGHT_CAVE,
		RAICHU_INSTANCE,
		RAIDS_ONE_PHASE_ONE_INSTANCE,
		RAIDS_ONE_PHASE_TWO_INSTANCE,
		RAIDS_ONE_PHASE_THREE_INSTANCE,
		RAIDS_TWO_PHASE_ONE_INSTANCE,
		RAIDS_TWO_PHASE_TWO_INSTANCE,
		RAIDS_TWO_PHASE_THREE_INSTANCE,
		RAIDS_THREE_PHASE_ONE_INSTANCE,
		RAIDS_THREE_PHASE_TWO_INSTANCE,
		RAIDS_THREE_PHASE_THREE_INSTANCE,
		RAIDS_THREE_PHASE_FOUR_INSTANCE,
		RAIDS_THREE_PHASE_FIVE_INSTANCE,
		RAIDS_FOUR_PHASE_ONE_INSTANCE,
		RAIDS_FOUR_PHASE_TWO_INSTANCE,
		RAIDS_FOUR_PHASE_THREE_INSTANCE,
		RAIDS_FIVE_PHASE_ONE_INSTANCE,
		RAIDS_FIVE_PHASE_TWO_INSTANCE,
		RAIDS_FIVE_PHASE_THREE_INSTANCE,
		RAIDS_FIVE_PHASE_FOUR_INSTANCE,
		RAIDS_SIX_PHASE_ONE_INSTANCE,
		RAIDS_SEVEN_PHASE_ONE_INSTANCE,
		RAIDS_EIGHT_PHASE_ONE_INSTANCE,
		CASH_ZONE_INSTANCE,
		RAIDS_DD_PHASE_ONE_INSTANCE,
		RAIDS_DD_PHASE_TWO_INSTANCE,
		RAIDS_DD_PHASE_THREE_INSTANCE,
		RAIDS_DS_PHASE_ONE_INSTANCE,
		WARRIORS_GUILD,
		NOMAD,
		BUNNY,
		CLOWN,
		KRAKEN,
		KING_BLACK_DRAGON,
		RECIPE_FOR_DISASTER,
		CONSTRUCTION_HOUSE,
		CONSTRUCTION_DUNGEON,
		ZULRAH, RAIDS;
	}
	
	private Player owner;
	private RegionInstanceType type;
	private CopyOnWriteArrayList<NPC> npcsList;
	private CopyOnWriteArrayList<Player> playersList;

	public RegionInstance(Player p, RegionInstanceType type) {
		this.owner = p;
		this.type = type;
		this.npcsList = new CopyOnWriteArrayList<NPC>();
		if(type == RegionInstanceType.CONSTRUCTION_HOUSE) {
			this.playersList = new CopyOnWriteArrayList<Player>();
		}
	}

	public void destruct() {
		for(NPC n : npcsList) {
			if(n != null && n.getConstitution() > 0 && World.getNpcs().get(n.getIndex()) != null && n.getSpawnedFor() != null && n.getSpawnedFor().getIndex() == owner.getIndex() && !n.isDying()) {
				if(n.getId() >= 4278 && n.getId() <= 4284) {
					owner.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(false);
				}
				if(n.getId() >= 2024 && n.getId() <= 2034)
					Barrows.killBarrowsNpc(owner, n, false);
				if(n.getId() == 4340 
						|| n.getId() == 5156
						|| n.getId() == 5159
						|| n.getId() == 5158
						|| n.getId() == 5157
						|| n.getId() == 5150
						|| n.getId() == 5147
						|| n.getId() == 5155)
				
				World.deregister(n);
			}
		}
		npcsList.clear();
		owner.setRegionInstance(null);
	}

	public void add(Character c) {
		if(type == RegionInstanceType.CONSTRUCTION_HOUSE) {
			if(c.isPlayer()) {
				playersList.add((Player)c);
			} else if(c.isNpc()) {
				npcsList.add((NPC)c);
			}

			if(c.getRegionInstance() == null || c.getRegionInstance() != this) {
				c.setRegionInstance(this);
			}
		}
	}

	public void remove(Character c) {
		if(type == RegionInstanceType.CONSTRUCTION_HOUSE) {
			if(c.isPlayer()) {
				playersList.remove((Player)c);
				if(owner == ((Player)c)) {
					destruct();
				}
			} else if(c.isNpc()) {
				npcsList.remove((NPC)c);
			}

			if(c.getRegionInstance() != null && c.getRegionInstance() == this) {
				c.setRegionInstance(null);
			}
		}
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public RegionInstanceType getType() {
		return type;
	}
	
	public CopyOnWriteArrayList<NPC> getNpcsList() {
		return npcsList;
	}

	public CopyOnWriteArrayList<Player> getPlayersList() {
		return playersList;
	}
	
	@Override
	public boolean equals(Object other) {
		return (RegionInstanceType)other == type;
	}

	public void despawnNPC(NPC npc) {
		System.out.println("Despawning NPCID="+npc.getId()+" from = "+new Throwable().getStackTrace()[1].toString());
		World.deregister(npc);
		getNpcsList().remove(npc);
	}
	
	public void spawnNPC(NPC npc) {
		System.out.println("Spawning NPCID="+npc.getId()+" from = "+new Throwable().getStackTrace()[1].toString());
		World.register(npc);
		getNpcsList().add(npc);
	}
	
	public NPC spawnAoeNPC(NPC npc) {
		System.out.println("spawning NPCID="+npc.getId()+" from = "+new Throwable().getStackTrace()[1].toString());
		World.register(npc);
		getNpcsList().add(npc);
		return npc;
	}
}
	
