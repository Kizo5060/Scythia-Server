package com.ruseps.world.entity.impl.npc;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.engine.task.impl.NPCDeathTask;
import com.ruseps.model.Direction;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.model.input.impl.playerYell;
import com.ruseps.util.JsonLoader;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.ruseps.world.content.combat.effect.CombatVenomEffect.VenomType;
import com.ruseps.world.content.combat.strategy.CombatStrategies;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_one.R1P1NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_one.R1P1NPC2;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC2;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_three.R1P3NPC3;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_two.R1P2NPC1;
import com.ruseps.world.content.new_raids_system.raids_loot.raids_one.phase_two.R1P2NPC2;
import com.ruseps.world.content.skill.impl.hunter.Hunter;
import com.ruseps.world.content.skill.impl.hunter.PuroPuro;
import com.ruseps.world.content.skill.impl.runecrafting.DesoSpan;
import com.ruseps.world.content.skill.impl.summoning.BossPets.BossPet;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.ruseps.world.entity.impl.npc.click_type.NpcClickType;
import com.ruseps.world.entity.impl.npc.impl.AuroraLoot;
import com.ruseps.world.entity.impl.npc.impl.BlessedSpartanLoot;
import com.ruseps.world.entity.impl.npc.impl.DemonLoot;
import com.ruseps.world.entity.impl.npc.impl.DragonLoot;
/*import com.ruseps.world.entity.impl.npc.impl.SantaLoot;*/
import com.ruseps.world.entity.impl.npc.impl.TelosLoot;
import com.ruseps.world.entity.impl.npc.impl.VaderLoot;
import com.ruseps.world.entity.impl.npc.impl.WreckedLoot;
import com.ruseps.world.entity.impl.npc.impl.AuroraLoot;
import com.ruseps.world.entity.impl.npc.summoning.TestPet;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.entity.impl.player.PlayerProcess;
import com.ruseps.world.content.Cows;


/**
 * Represents a non-playable character, which players can interact with.
 * @author Gabriel Hannason
 */

public class NPC extends Character {

	public NPC(int id, Position position) {
		super(position);
		NpcDefinition definition = NpcDefinition.forId(id);
		if(definition == null)
			throw new NullPointerException("NPC "+id+" is not defined!");
		this.defaultPosition = position;
		this.id = id;
		this.definition = definition;
		this.defaultConstitution = Math.max(definition.getHitpoints(), 100);
		this.constitution = defaultConstitution;
		
		setLocation(Location.getLocation(this));
	}

	public void sequence() {
		/**
		 * HP restoration
		 */
		if(constitution < defaultConstitution) {
			if(!isDying) {
				if(getLastCombat().elapsed((id == 13447 || id == 3200 ? 50000 : 5000)) && !getCombatBuilder().isAttacking() && getLocation() != Location.PEST_CONTROL_GAME && getLocation() != Location.DUNGEONEERING) {
					setConstitution(constitution + (int)(defaultConstitution * 0.1));
					if(constitution > defaultConstitution)
						setConstitution(defaultConstitution);
				}
			}
		}
	}

	public void clickNpc(Player player, NpcClickType npcClickType) {

	}
	
	public ArrayList<Player> getPossibleTargets() {
		ArrayList<Player> possibleTargets = new ArrayList<>();
		for(Player player : World.getPlayers()) {
			if(player == null) {
				continue;
			}
			if(this.getPosition().getDistance(player.getPosition()) <= 12 && this.getPosition().getZ() == player.getPosition().getZ()) {
				possibleTargets.add(player);
			}
		}
		return possibleTargets;
	}
	@Override
	public void appendDeath() {
		if(!isDying && !summoningNpc) {
			TaskManager.submit(new NPCDeathTask(this));
			isDying = true;
		}
	}
	
	@Override
	public long getConstitution() {
		return constitution;
	}

	@Override
	public NPC setConstitution(long constitution) {
		
		this.constitution = constitution;
		if(this.constitution <= 0)
			appendDeath();
		
		return this;
	}

	@Override
	public void heal(long heal) {
		if ((this.constitution + heal) > getDefaultConstitution()) {
			setConstitution(getDefaultConstitution());
			return;
		}
		setConstitution(this.constitution + heal);
	}


	@Override
	public int getBaseAttack(CombatType type) {
		return getDefinition().getAttackBonus();
	}

	@Override
	public int getAttackSpeed() {
		return this.getDefinition().getAttackSpeed();
	}


	@Override
	public int getBaseDefence(CombatType type) {

		if (type == CombatType.MAGIC)
			return getDefinition().getDefenceMage();
		else if (type == CombatType.RANGED)
			return getDefinition().getDefenceRange();

		return getDefinition().getDefenceMelee();
	}

	@Override
	public boolean isNpc() {
		return true;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof NPC && ((NPC)other).getIndex() == getIndex();
	}

	@Override
	public int getSize() {
		return getDefinition().getSize();
	}

	@Override
	public void poisonVictim(Character victim, CombatType type) {
		if (getDefinition().isPoisonous()) {
			CombatFactory.poisonEntity(
					victim,
					type == CombatType.RANGED || type == CombatType.MAGIC ? PoisonType.MILD
							: PoisonType.EXTRA);
		}

	}
	
    @Override
    public void venomVictim(Character victim, CombatType type) {
        if (getDefinition().isVenomous()) {
            CombatFactory.venomEntity(victim, VenomType.SUPER);
        }

    }
    
    private boolean respawnNpc = true;

	public boolean shouldRespawnNpc() {
		return respawnNpc;
	}

	public void setShouldRespawn(boolean shouldRespawn) {
		this.respawnNpc = shouldRespawn;
	}

	/**
	 * Prepares the dynamic json loader for loading world npcs.
	 * 
	 * @return the dynamic json loader.
	 * @throws Exception
	 *             if any errors occur while preparing for load.
	 */
	public static void init() {
		new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {

				int id = reader.get("npc-id").getAsInt();
				Position position = builder.fromJson(reader.get("position").getAsJsonObject(), Position.class);
				Coordinator coordinator = builder.fromJson(reader.get("walking-policy").getAsJsonObject(), Coordinator.class);
				Direction direction = Direction.valueOf(reader.get("face").getAsString());
				NPC npc = new NPC(id, position);
				npc.movementCoordinator.setCoordinator(coordinator);
				npc.setDirection(direction);
				
				World.register(npc);
				if(id > 5070 && id < 5081) {
					Hunter.HUNTER_NPC_LIST.add(npc);
				}
				position = null;
				coordinator = null;
				direction = null;
			}

			@Override
			public String filePath() {
				return "./data/def/json/world_npcs.json";
			}
		}.load();

		Nex.spawn();
		PuroPuro.spawn();
		DesoSpan.spawn();
		Cows.spawnMainNPCs();

		KalphiteQueen.spawn(1158, new Position(3485, 9509));
	}

	@Override
	public CombatStrategy determineStrategy() {
		return CombatStrategies.getStrategy(id);
	}

	public boolean switchesVictim() {
		if(getLocation() == Location.DUNGEONEERING) {
			return true;
		}
		return id == 6263 || id == 6265 || id == 6203 || id == 6208 || id == 6206 || id == 6247 || id == 6250 || id == 3200 || id == 4540 || id == 1158 || id == 1160 || id == 8133 || id == 13447 || id == 13451 || id == 13452 || id == 13453 || id == 13454 || id == 2896 || id == 2882 || id == 2881 || id == 6260;
	}

	public int getAggressionDistance() {
		int distance = 7;
		
		/*switch(id) {
		}*/
		if(Nex.nexMob(id)) {
			distance = 60;
		} else if(id == 2896) {
			distance = 50;
		}
		return distance;
	}

	/*
	 * Fields
	 */
	/** INSTANCES **/
	private final Position defaultPosition;
	private NPCMovementCoordinator movementCoordinator = new NPCMovementCoordinator(this);
	private Player spawnedFor;
	private NpcDefinition definition;

	/** INTS **/
	private final int id;
	private long constitution = 100;
	private long defaultConstitution;
	private int transformationId = -1;

	/** BOOLEANS **/
	private boolean[] attackWeakened = new boolean[3], strengthWeakened = new boolean[3];
	private boolean summoningNpc, summoningCombat;
	private boolean isDying;
	private boolean visible = true;
	private boolean healed, chargingAttack;
	private boolean findNewTarget;
	
	/*
	 * Getters and setters
	 */

	public int getId() {
		return id;
	}

	public Position getDefaultPosition() {
		return defaultPosition;
	}

	public long getDefaultConstitution() {
		return defaultConstitution;
	}

	public int getTransformationId() {
		return transformationId;
	}

	public void setTransformationId(int transformationId) {
		this.transformationId = transformationId;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}
	
	public void setDefaultConstitution(int defaultConstitution) {
		this.defaultConstitution = defaultConstitution;
	}

	/**
	 * @return the statsWeakened
	 */
	public boolean[] getDefenceWeakened() {
		return attackWeakened;
	}

	public void setSummoningNpc(boolean summoningNpc) {
		this.summoningNpc = summoningNpc;
	}

	public boolean isSummoningNpc() {
		return summoningNpc;
	}

	public boolean isDying() {
		return isDying;
	}

	/**
	 * @return the statsBadlyWeakened
	 */
	public boolean[] getStrengthWeakened() {
		return strengthWeakened;
	}

	public NPCMovementCoordinator getMovementCoordinator() {
		return movementCoordinator;
	}

	public NpcDefinition getDefinition() {
		return definition;
	}

	public Player getSpawnedFor() {
		return spawnedFor;
	}

	public NPC setSpawnedFor(Player spawnedFor) {
		this.spawnedFor = spawnedFor;
		return this;
	}

	public boolean hasHealed() {
		return healed;
	}

	public void setHealed(boolean healed) {
		this.healed = healed;
	}

	public boolean isChargingAttack() {
		return chargingAttack;
	}

	public NPC setChargingAttack(boolean chargingAttack) {
		this.chargingAttack = chargingAttack;
		return this;
	}
	
	public boolean findNewTarget() {
		return findNewTarget;
	}
	
	public void setFindNewTarget(boolean findNewTarget) {
		this.findNewTarget = findNewTarget;
	}
	
	public boolean summoningCombat() {
		return summoningCombat;
	}
	
	public void setSummoningCombat(boolean summoningCombat) {
		this.summoningCombat = summoningCombat;
	}

	public void walkToPosition(Position targetPosition) {
		Position myPosition = this.getPosition().copy();

		int x;
		int y;

		x = targetPosition.getX() - myPosition.getX();
		y = targetPosition.getY() - myPosition.getY();

		this.getMovementQueue().walkStep(x, y);
	}
	
	public void removeInstancedNpcs(Location loc, int height) {
		int checks = loc.getX().length - 1;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						World.deregister(this);
					}
				}
			}
		}
	}
	public void countInstancedNpcs(Location loc, int height, int npcId) {
		int checks = loc.getX().length - 1;
		int ii = 0;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						if(getId() == npcId) {
							ii++;
							System.out.println(ii);
						}
					}
				}
			}
		}
	}
	
	public void dropItems(Player killer) {
		NPCDrops.dropItems(killer, this);
	}
	
	public static NPC of(int id, Position position) {
		switch (id) {

		case TestPet.ID:
			return new TestPet(id, position);
			case VaderLoot.NPC_ID:
				return new VaderLoot(position);
			case AuroraLoot.NPC_ID:
				return new AuroraLoot(position);
			case WreckedLoot.NPC_ID:
				return new WreckedLoot(position);
			case DemonLoot.NPC_ID:
				return new DemonLoot(position);
			case DragonLoot.BLUE_NPC_ID:
				return new DragonLoot(position);
			case DragonLoot.GREEN_NPC_ID:
				return new DragonLoot(position);
			case DragonLoot.PURPLE_NPC_ID:
				return new DragonLoot(position);
			case DragonLoot.RED_NPC_ID:
				return new DragonLoot(position);
			case BlessedSpartanLoot.NPC_ID:
				return new BlessedSpartanLoot(position);
			case TelosLoot.BLUE_NPC_ID:
				return new TelosLoot(position);
			case TelosLoot.GREEN_NPC_ID:
				return new TelosLoot(position);
			case TelosLoot.RED_NPC_ID:
				return new TelosLoot(position);
			case R1P1NPC1.FIRST_NPC_ID:
				return new R1P1NPC1(position);
			case R1P1NPC2.FIRST_NPC_ID:
				return new R1P1NPC2(position);
			case R1P2NPC1.FIRST_NPC_ID:
				return new R1P2NPC1(position);
			case R1P2NPC2.FIRST_NPC_ID:
				return new R1P2NPC2(position);
			case R1P3NPC1.FIRST_NPC_ID:
				return new R1P3NPC1(position);
			case R1P3NPC2.FIRST_NPC_ID:
				return new R1P1NPC2(position);
			case R1P3NPC3.FIRST_NPC_ID:
			case 5157:
			case 5158:
			case 5159:
				return new R1P3NPC3(position);
			/*case 8540:
				return new SantaLoot(position);*/
				
		}
		
		return new NPC(id, position);
	}
	
	public boolean clearDamageMapOnDeath() {
		return getId() != 466
			&& getId() != 163
			&& getId() != 164
			&& getId() != 165
			&& getId() != 175
			&& getId() != 175
			&& getId() != 29
			&& getId() != 6820
			&& getId() != 3169
			&& getId() != 3168
			&& getId() != 3167
			&& getId() != 4867
			&& getId() != 1417
			&& getId() != 1041
			/** RAIDS 1 **/
			&& getId() != 5151
			&& getId() != 5156
			&& getId() != 5150
			&& getId() != 5147
			&& getId() != 5154
			&& getId() != 5155
			&& getId() != 5153
			&& getId() != 5157
			&& getId() != 5158
			&& getId() != 5159
			&& getId() != 8540
			&& getId() != 4340
			&& getId() != 1500
			&& getId() != 1501
			&& getId() != 1507
			&& getId() != 1504
			&& getId() != 8495
			&& getId() != 8497
			&& getId() != 8496
			&& getId() != 135
			&& getId() != 1472
			&& getId() != 133
			&& getId() != 132
			&& getId() != 183
			&& getId() != 3018
			&& getId() != 1488
			&& getId() != 1489
			&& getId() != 1490
			&& getId() != 1491
			&& getId() != 1492
			&& getId() != 1493
			&& getId() != 1495
			&& getId() != 1496
			&& getId() != 1497
			&& getId() != 1498
			&& getId() != 1499
			&& getId() != 4862
			&& getId() != 1416
			
				;
	}
	
}
