package com.ruseps.world.content.combat;

import com.ruseps.model.GameMode;
import com.ruseps.model.Graphic;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.definitions.ItemDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.effect.EquipmentBonus;
import com.ruseps.world.content.combat.magic.CombatSpell;
import com.ruseps.world.content.combat.magic.CombatSpells;
import com.ruseps.world.content.combat.prayer.CurseHandler;
import com.ruseps.world.content.combat.prayer.PrayerHandler;
import com.ruseps.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.ruseps.world.content.combat.weapon.FightType;
import com.ruseps.world.content.skill.SkillManager;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class DesolaceFormulas {

	public static int calculateMaxMeleeHit(Character entity, Character victim) {
		double maxHit = 0;
		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			maxHit = npc.getDefinition().getMaxHit();
			if (npc.getStrengthWeakened()[0]) {
				maxHit -= (int) ((0.10) * (maxHit));
			} else if (npc.getStrengthWeakened()[1]) {
				maxHit -= (int) ((0.20) * (maxHit));
			} else if (npc.getStrengthWeakened()[2]) {
				maxHit -= (int) ((0.30) * (maxHit));
			}

			/** CUSTOM NPCS **/
			if(npc.getId() == 2026) { //Dharok the wretched
				maxHit += (int) ((int)(npc.getDefaultConstitution() - npc.getConstitution()) * 0.2);
			}
		} else {
			Player plr = (Player)entity;

			double base = 0;
			double effective = getEffectiveStr(plr);
			double specialBonus = 1;
			if (plr.isSpecialActivated()) {
				specialBonus = plr.getCombatSpecial().getStrengthBonus();
			}
			double strengthBonus = plr.getBonusManager().getOtherBonus()[0];
			base = (13 + effective + (strengthBonus / 8) + ((effective * strengthBonus) / 65)) / 11;
			if(plr.getEquipment().getItems()[3].getId() == 4718 && plr.getEquipment().getItems()[0].getId() == 4716 && plr.getEquipment().getItems()[4].getId() == 4720 && plr.getEquipment().getItems()[7].getId() == 4722)
				base += ((plr.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - plr.getConstitution()) * .045) + 1.1;
			if (specialBonus > 1)
				base = (base * specialBonus);
			if (hasObsidianEffect(plr) || EquipmentBonus.wearingVoid(plr, CombatType.MELEE))
				base = (base * 1.2);
			
			if(plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20061) {
				base *= 1.15;
			}
			if(plr.x2DMG = true) {
				base *= 10.00;
			}
			if (plr.getLocation() != Location.WILDERNESS) {
				if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16955 || plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16425 || plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16909) {
					
					base *=1.2;
				}
			}
			
			if (plr.getLocation() == Location.WILDERNESS) {
				if (plr.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20061) {
					base *= .82;
					
				}
			}
			
			boolean nexEffect = plr.getEquipment().wearingNexAmours();
			
			if(nexEffect) {
			base *= 1.17;
		}
		
		
			if (victim.isNpc()) {
				NPC npc = (NPC) victim;
				if (npc.getDefenceWeakened()[0]) {
					base += (int) ((0.10) * (base));
				} else if (npc.getDefenceWeakened()[1]) {
					base += (int) ((0.20) * (base));
				} else if (npc.getDefenceWeakened()[2]) {
					base += (int) ((0.30) * (base));
				}

				/** BOOSTS **///Slayer
				if (plr.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2858 
						&& plr.getEquipment().get(Equipment.BODY_SLOT).getId() == 2869 
						&& plr.getEquipment().get(Equipment.LEG_SLOT).getId() == 2859 
						&& plr.getEquipment().get(Equipment.FEET_SLOT).getId() == 2856 
						&& plr.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2857 
						&& plr.getEquipment().get(Equipment.CAPE_SLOT).getId() == 2867) {
					base *= .70;
				}//Obsidian's Upgraded
				if (plr.getEquipment().get(Equipment.HEAD_SLOT).getId() == 2711
						&& plr.getEquipment().get(Equipment.BODY_SLOT).getId() == 2712 
						&& plr.getEquipment().get(Equipment.LEG_SLOT).getId() == 2713 
						&& plr.getEquipment().get(Equipment.FEET_SLOT).getId() == 2715 
						&& plr.getEquipment().get(Equipment.HANDS_SLOT).getId() == 2714 ) {
					base *= 1.40;
				}// montreal
				if (plr.getEquipment().get(Equipment.HEAD_SLOT).getId() == 3511
						&& plr.getEquipment().get(Equipment.BODY_SLOT).getId() == 3512
						&& plr.getEquipment().get(Equipment.LEG_SLOT).getId() == 3513
						&& plr.getEquipment().get(Equipment.FEET_SLOT).getId() == 3514
						&& plr.getEquipment().get(Equipment.HANDS_SLOT).getId() == 3515) {
					base *= 2.45;
				}
				if (plr.getEquipment().get(Equipment.CAPE_SLOT).getId() == 1413 ){
					base *= 5.00;
				}
				//opSEt
				if (plr.getEquipment().get(Equipment.AMULET_SLOT).getId() == 996
						&& plr.getEquipment().get(Equipment.RING_SLOT).getId() == 965
						&& plr.getEquipment().get(Equipment.FEET_SLOT).getId() == 1007 ) {
					base *= 2.70;
				}
				if (plr.getGameMode() == GameMode.GROUP_IRONMAN) {
					base /= 25.00;
				}
				if (plr.getEquipment().get(Equipment.BOOSTER).getId() == 18941) {
					base *= 1.70;
				} 
				if(plr.getEquipment().get(Equipment.BOOSTER).getId() ==  18948) {
					base *= 1.70;
				}
				/** SLAYER HELMET **/
				if(npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
					if(plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
						base *= 1.12;
					}
				
					if(plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
						maxHit *= 1.15;
					}
					if(plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() ==  8465) {
						maxHit *= 1.15;
					}
					if(plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
						maxHit *= 1.15;
					}
				}
				if(npc.getId() == 8133) {
					if(plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 11716) {
						base *= 2.10;
					}
				}
			}
			maxHit = (base *= 10);
		}
		if(victim.isPlayer()) {
			Player p = (Player) victim;
			if(p.hasStaffOfLightEffect()) {
				maxHit = maxHit/2;
				p.performGraphic(new Graphic(2319));
			}
		}
		return (int) Math.floor(maxHit);
	}
	/**
	 * Calculates a player's Melee attack level (how likely that they're going to hit through defence)
	 * @param plr	The player's Meelee attack level
	 * @return		The player's Melee attack level
	 */
	@SuppressWarnings("incomplete-switch")
	public static int getMeleeAttack(Player plr) {
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.ATTACK);
		int prayerLevel = plr.getSkillManager().getCurrentLevel(Skill.PRAYER);
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
			attackLevel += 3;
			break;
		case CONTROLLED:
			attackLevel += 1;
			break;
		}
		boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.MELEE);
		if((plr.getRights() == PlayerRights.SILVER_MEMBER)) {
			prayerLevel += plr.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.75;
			plr.getSkillManager().setCurrentLevel(Skill.PRAYER, 99);
		}
		if (PrayerHandler.isActivated(plr,
				PrayerHandler.CLARITY_OF_THOUGHT)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05;
		} else if (PrayerHandler.isActivated(plr,
				PrayerHandler.IMPROVED_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
		} else if (PrayerHandler.isActivated(plr,
				PrayerHandler.INCREDIBLE_REFLEXES)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr,
				PrayerHandler.CHIVALRY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
		} else if (PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.2;
		} else if (CurseHandler.isActivated(plr, CurseHandler.LEECH_ATTACK)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05 + plr.getLeechedBonuses()[2];
		} else if (CurseHandler.isActivated(plr, CurseHandler.TURMOIL)) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK)
					* 0.3 + plr.getLeechedBonuses()[2];
		}
		
			
		if (hasVoid) {
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
		}
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
		int i = (int) plr.getBonusManager().getAttackBonus()[bestMeleeAtk(plr)];
		
		if(plr.getEquipment().getItems()[3].getId() == 4718 && plr.getEquipment().getItems()[0].getId() == 4716 && plr.getEquipment().getItems()[4].getId() == 4720 && plr.getEquipment().getItems()[7].getId() == 4722) {
			i *= 1.20;
		}
		if (hasObsidianEffect(plr) || hasVoid)
			i *= 1.20;
		return (int)(attackLevel + (attackLevel * 0.15) + (i + i * 0.04));
	}

	/**
	 * Calculates a player's Melee Defence level
	 * @param plr		The player to calculate Melee defence for
	 * @return		The player's Melee defence level
	 */
	public static int getMeleeDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		int i = (int) plr.getBonusManager().getDefenceBonus()[bestMeleeDef(plr)];
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if(plr.getEquipment().get(Equipment.SHIELD_SLOT).getId() ==  2716) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 1010200.15;
		}
		return (int)(defenceLevel + (defenceLevel * 1.15) + (i + i * .05));
		
	}
	
	

	public static int bestMeleeDef(Player p) {
		if(p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[1] && p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[2]) {
			return 0;
		}
		if(p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[0] && p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[0] || p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[1] ? 0 : 2;
	}

	public static int bestMeleeAtk(Player p) {
		if(p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[1] && p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[2]) {
			return 0;
		}
		if(p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[0] && p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[2]) {
			return 1;
		}
		return p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[1] || p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[0] ? 0 : 2;
	}

	
	
	/**
	 * Obsidian items
	 */

	public static final int[] obsidianWeapons = {
		746, 747, 6523, 6525, 6526, 6527, 6528
	};

	public static boolean hasObsidianEffect(Player plr) {		
		if (plr.getEquipment().getItems()[2].getId() != 11128) 
			return false;

		for (int weapon : obsidianWeapons) {
			if (plr.getEquipment().getItems()[3].getId() == weapon) 
				return true;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	public static int getStyleBonus(Player plr) {
		switch (plr.getFightType().getStyle()) {
		case AGGRESSIVE:
		case ACCURATE:
			return 3;
		case CONTROLLED:
			return 1;
		}
		return 0;
	}

	public static double getEffectiveStr(Player plr) {
		return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * getPrayerStr(plr)) + getStyleBonus(plr);		
	}

	public static double getPrayerStr(Player plr) {
		if (plr.getPrayerActive()[1] || plr.getCurseActive()[CurseHandler.LEECH_STRENGTH])
			return 1.05;
		else if (plr.getPrayerActive()[6]) 
			return 1.1;
		else if (plr.getPrayerActive()[14]) 
			return 1.15;
		else if (plr.getPrayerActive()[24]) 
			return 1.18;
		else if (plr.getPrayerActive()[25]) 
			return 1.23;	
		else if (plr.getCurseActive()[CurseHandler.TURMOIL]) 
			return 1.24;
		return 1;
	}

	/**
	 * Calculates a player's Ranged attack (level).
	 * Credits: Dexter Morgan
	 * @param plr	The player to calculate Ranged attack level for
	 * @return		The player's Ranged attack level
	 */
	public static int getRangedAttack(Player plr) {
		int rangeLevel = plr.getSkillManager().getCurrentLevel(Skill.RANGED);
		int prayerLevel = plr.getSkillManager().getCurrentLevel(Skill.PRAYER);
		boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.RANGED);
		double accuracy = plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
		rangeLevel *= accuracy;
		
		if((plr.getRights() == PlayerRights.SILVER_MEMBER)) {
			prayerLevel += plr.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.75;
			plr.getSkillManager().setCurrentLevel(Skill.PRAYER, 99);
		}
		if (hasVoid){
			rangeLevel *= 0.15;
		}
		if (plr.getCurseActive()[PrayerHandler.SHARP_EYE] || plr.getCurseActive()[CurseHandler.SAP_RANGER]) {
			rangeLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.HAWK_EYE]) {
			rangeLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.EAGLE_EYE]) {
			rangeLevel *= 1.15;
		}  else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			rangeLevel *= 1.22;
		} else if(plr.getCurseActive()[CurseHandler.LEECH_RANGED]) {
			rangeLevel *= 1.10;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			rangeLevel += plr.getSkillManager().getMaxLevel(Skill.RANGED)
					* 0.20 + plr.getLeechedBonuses()[0];
		}

		if (hasVoid && accuracy > 1.15)
			rangeLevel *= 1.8;
		/*
		 * Slay helm
		 *
		if(plr.getAdvancedSkills().getSlayer().getSlayerTask() != null && plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
			if(plr.getCombatAttributes().getCurrentEnemy() != null && plr.getCombatAttributes().getCurrentEnemy().isNpc()) {
				NPC n = (NPC)plr.getCombatAttributes().getCurrentEnemy();
				if(n != null && n.getId() == plr.getAdvancedSkills().getSlayer().getSlayerTask().getNpcId())
					rangeLevel *= 1.12;
			}
		}*/
		return (int) (rangeLevel + (plr.getBonusManager().getAttackBonus()[4] * 2));
	}

	/**
	 * Calculates a player's Ranged defence level.
	 * @param plr		The player to calculate the Ranged defence level for
	 * @return			The player's Ranged defence level
	 */
	public static int getRangedDefence(Player plr) {
		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE)
					* 0.20 + plr.getLeechedBonuses()[0];
		}
		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[4] + (plr.getBonusManager().getDefenceBonus()[4] / 2));
	}

	public static int getMagicAttack(Player plr) {
		boolean voidEquipment = EquipmentBonus.wearingVoid(plr, CombatType.MAGIC);
		int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.MAGIC);
		int prayerLevel = plr.getSkillManager().getCurrentLevel(Skill.PRAYER);
		
		if((plr.getRights() == PlayerRights.SILVER_MEMBER)) {
			prayerLevel += plr.getSkillManager().getMaxLevel(Skill.PRAYER) * 0.75;
			plr.getSkillManager().setCurrentLevel(Skill.PRAYER, 99);
		}
		
		
		
		if (voidEquipment)
			attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 0.2;
		if (plr.getPrayerActive()[PrayerHandler.MYSTIC_WILL] || plr.getCurseActive()[CurseHandler.SAP_MAGE]) {
			attackLevel *= 1.05;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_LORE]) {
			attackLevel *= 1.10;
		} else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_MIGHT]) {
			attackLevel *= 1.15;
		} else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			attackLevel *= 1.22;
		} else if (plr.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
			attackLevel *= 1.18;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			attackLevel += plr.getSkillManager().getMaxLevel(Skill.MAGIC)
					* 0.20 + plr.getLeechedBonuses()[0];
		}
		
		attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;

		
		return (int) (attackLevel + (plr.getBonusManager().getAttackBonus()[3] * 2));
	}

	/**
	 * Calculates a player's magic defence level
	 * @param player			The player to calculate magic defence level for
	 * @return			The player's magic defence level
	 */
	public static int getMagicDefence(Player plr) {


		int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE)/2 + plr.getSkillManager().getCurrentLevel(Skill.MAGIC)/2;

		if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
		} else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
		} else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
		} else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
		} else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		}  else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
		} else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
			defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE)
					* 0.20 + plr.getLeechedBonuses()[0];
		}

		return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[3] + (plr.getBonusManager().getDefenceBonus()[3] / 3));
	}

	/**
	 * Calculates a player's magic max hit
	 * @param player			The player to calculate magic max hit for
	 * @return			The player's magic max hit damage
	 */
	public static int getMagicMaxhit(Character c) {
		int damage = 0;
		CombatSpell spell = c.getCurrentlyCasting();
		if(spell != null) {
			if(spell.maximumHit() > 0)
				damage += spell.maximumHit();
			else {
				if(c.isNpc()) {
					damage = ((NPC)c).getDefinition().getMaxHit();
				} else {
					damage = 1;
				}
			}
		}

		if(c.isNpc()) {
			if(spell == null) {
				damage = Misc.getRandom(((NPC)c).getDefinition().getMaxHit());
			}
			return damage;
		}

		Player p = (Player)c;
		double damageMultiplier = 1;
		
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
			damageMultiplier += .10;
		}
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8469) {
			damageMultiplier += .10;
		}
		if(p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId() == 1413) {
			damageMultiplier += 8.10;
		}
		if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 2799
				&& p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() == 2800
				&& p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() == 2801
				&& p.getEquipment().getItems()[Equipment.FEET_SLOT].getId() == 2803
				&& p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() == 2802) {
			damageMultiplier += 25.45;
		}

		if (p.getGameMode() == GameMode.GROUP_IRONMAN) {
			damageMultiplier /= 25.00;
		}
		if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 2745
				&& p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() == 2734
				&& p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() == 2735
				&& p.getEquipment().getItems()[Equipment.FEET_SLOT].getId() == 2737
				&& p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() == 2736
				&& p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId() == 2738 ) {
			damageMultiplier += 1.45;
		}//mdma set
			if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13026
					&& p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() == 13024
					&& p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() == 13025
					&& p.getEquipment().getItems()[Equipment.FEET_SLOT].getId() == 13028
					&& p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() == 13027
					&& p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId() == 13029 ) {
				damageMultiplier += 7.45;

		}
		if (p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13034
				&& p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() == 13030
				&& p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() == 13031
				&& p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() == 13033 ) {
			damageMultiplier += 7.45;

		}
		if(p.getEquipment().getItems()[Equipment.BOOSTER].getId() == 18941) {
			damageMultiplier += 2.10;
		}
		if(p.getEquipment().getItems()[Equipment.BOOSTER].getId() == 18947) {
			damageMultiplier += 2.10;
		}
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() ==  8465) {
			damageMultiplier += .10;
		}
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 8467) {
			damageMultiplier += .10;
		}
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 12282) {
			damageMultiplier += .10;
		}
		
		boolean nexEffect = p.getEquipment().wearingNexAmours();
		if(nexEffect) {
			damageMultiplier += .18;
		}
		
		if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
				damageMultiplier += .15;
		}
		
		

		switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
		case 4675: 
		case 6914:
		case 15246:
			damageMultiplier += .10;
			break;
		
		case 18355:
			damageMultiplier += .20;
			break;
		}
		
		boolean specialAttack = p.isSpecialActivated();
		
		int maxHit = -1;
		
		if(specialAttack) {
			switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
			case 19780:
				damage = maxHit = 750;
				break;
			case 11730:
				damage = maxHit = 310;
				break;
			}
		} else {
			damageMultiplier += 0.25;
		}

		if(p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 18335) {
			damageMultiplier += .10;
		}

		damage *= damageMultiplier;
		
		if(maxHit > 0) {
			if(damage > maxHit) {
				damage = maxHit;
			}
		}
		
		return (int)damage;
	}


	public static int getAttackDelay(Player plr) {
		int id = plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
		String s = ItemDefinition.forId(id).getName().toLowerCase();
		if (id == -1)
			return 4;// unarmed
		if(id == 18357 || id == 14684 || id == 13051)
			return 3;
		
		RangedWeaponData rangedData = plr.getRangedWeaponData();
		if(rangedData != null) {
			int speed = rangedData.getType().getAttackDelay();
			if(plr.getFightType() == FightType.SHORTBOW_RAPID || plr.getFightType() == FightType.DART_RAPID || plr.getFightType() == FightType.KNIFE_RAPID || plr.getFightType() == FightType.THROWNAXE_RAPID || plr.getFightType() == FightType.JAVELIN_RAPID) {
				speed--;
			}
			return speed;
		}
		
		if(id == 18365)
			return 3;
		else if(id == 18349) //CCbow and rapier
			return 4;
		if (id == 18353) // cmaul
			return 7;// chaotic maul

		if (id == 20000)
			return 4;// gs	
		if (id == 20001)
			return 4;// gs	
		if (id == 20002)
			return 4;// gs	
		if (id == 20003)
			return 4;// gs	
		if (id == 18349)
			return 5;// chaotic rapier				
		if (id == 18353) // cmaul
			return 7;// chaotic maul
		if (id == 16877)
			return 4;// dung 16877 shortbow
		if (id == 19143)
			return 3;// sara shortbow
		if (id == 19146)
			return 4;// guthix shortbow
		if (id == 19149)
			return 3;// zammy shortbow

		switch (id) {
		case 11235:
		case 13405: //dbow
		case 15701: // dark bow
		case 15702: // dark bow
		case 15703: // dark bow
		case 15704: // dark bow
		case 19146: // guthix bow
			return 9;
		case 13879:
			return 8;
		case 15241: // hand cannon
			return 8;
		case 11730:
			return 4;
		case 14484:
			return 5;
		case 13883:
			return 6;
		case 10887:
		case 6528:
		case 15039:
			return 7;
		case 13905:
			return 5;
		case 13907:
			return 5;
		case 18353:
			return 7;
		case 18349:
			return 4;
		case 20000:
		case 20001:
		case 20002:
		case 20003:
			return 4;
			case 4706:
			case 4212:
			return 4;

		case 16403: //long primal
			return 5;
		}

		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.equals("chaotic crossbow"))
			return 4;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix")
					|| s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if (s.contains("aril")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt")
					|| s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.contains("crystal"))
				return 6;
			else if (s.contains("'bow"))
				return 7;

			return 5;
		} else if (s.contains("dagger"))
			return 4;
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword"))
			return 5;
		else if (s.contains("sword"))
			return 4;
		else if (s.contains("scimitar") || s.contains("katana"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;

		// sara sword, 2400ms
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak"))// sword
			return 4;
		else if (s.equals("tzhaar-ket-em"))// mace
			return 5;
		else if (s.equals("tzhaar-ket-om"))// maul
			return 7;
		else if (s.equals("chaotic maul"))// maul
			return 7;
		else if (s.equals("toktz-xil-ek"))// knife
			return 4;
		else if (s.equals("toktz-xil-ul"))// rings
			return 4;
		else if (s.equals("toktz-mej-tal"))// staff
			return 6;
		else if (s.contains("whip"))
			return 4;
		
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		return 5;
	}
}
