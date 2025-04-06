package com.ruseps.world.content.combat.strategy;

import java.util.HashMap;

import java.util.Map;

import com.ruseps.world.content.combat.strategy.custom_bosses.*;
import com.ruseps.world.content.combat.strategy.impl.Aviansie;
import com.ruseps.world.content.combat.strategy.impl.BandosAvatar;
import com.ruseps.world.content.combat.strategy.impl.ChaosElemental;
import com.ruseps.world.content.combat.strategy.impl.Cobra;
import com.ruseps.world.content.combat.strategy.impl.CorporealBeast;
import com.ruseps.world.content.combat.strategy.impl.DagannothSupreme;
import com.ruseps.world.content.combat.strategy.impl.Death;
import com.ruseps.world.content.combat.strategy.impl.DefaultMagicCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.DefaultMeleeCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.DefaultRangedCombatStrategy;
import com.ruseps.world.content.combat.strategy.impl.Dragon;
import com.ruseps.world.content.combat.strategy.impl.Fear;
import com.ruseps.world.content.combat.strategy.impl.Geerin;
import com.ruseps.world.content.combat.strategy.impl.GiantMole;
import com.ruseps.world.content.combat.strategy.impl.Glacor;
import com.ruseps.world.content.combat.strategy.impl.Graardor;
import com.ruseps.world.content.combat.strategy.impl.Grimspike;
import com.ruseps.world.content.combat.strategy.impl.Gritch;
import com.ruseps.world.content.combat.strategy.impl.Growler;
import com.ruseps.world.content.combat.strategy.impl.JackPets;
import com.ruseps.world.content.combat.strategy.impl.Jad;
import com.ruseps.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruseps.world.content.combat.strategy.impl.KreeArra;
import com.ruseps.world.content.combat.strategy.impl.Kreeyath;
import com.ruseps.world.content.combat.strategy.impl.LizardMan;
import com.ruseps.world.content.combat.strategy.impl.Nex;
import com.ruseps.world.content.combat.strategy.impl.Nomad;
import com.ruseps.world.content.combat.strategy.impl.PlaneFreezer;
import com.ruseps.world.content.combat.strategy.impl.Revenant;
import com.ruseps.world.content.combat.strategy.impl.Sire;
import com.ruseps.world.content.combat.strategy.impl.Skotizo;
import com.ruseps.world.content.combat.strategy.impl.Spinolyp;
import com.ruseps.world.content.combat.strategy.impl.Steelwill;
import com.ruseps.world.content.combat.strategy.impl.Thermonuclear;
import com.ruseps.world.content.combat.strategy.impl.TormentedDemon;
import com.ruseps.world.content.combat.strategy.impl.Tsutsuroth;
import com.ruseps.world.content.combat.strategy.impl.UnholyCursebearer;
import com.ruseps.world.content.combat.strategy.impl.WingmanSkree;
import com.ruseps.world.content.combat.strategy.impl.Venenatis;
import com.ruseps.world.content.combat.strategy.impl.Vetion;
import com.ruseps.world.content.combat.strategy.impl.WildyWyrm;
import com.ruseps.world.content.combat.strategy.impl.Callisto;
import com.ruseps.world.content.combat.strategy.impl.Cerberus;
import com.ruseps.world.content.combat.strategy.impl.Zilyana;
import com.ruseps.world.content.combat.strategy.multi_formula.B00NY;
import com.ruseps.world.content.combat.strategy.multi_formula.SharkBeast;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_five.phase_four.ArmouredMew;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_five.phase_four.SummonedSkull;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_five.phase_one.DeadlyAurora;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_five.phase_three.WingedGoat;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_five.phase_two.Shild;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_one.Goro;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_one.GreenGoro;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_three.Scropion;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_three.SubZero;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_two.CyanMileena;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_four.phase_two.Mileena;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_one.Lugia;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_one.Seadra;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_one.Starmie;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_three.Charizard;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_three.MegaGengar;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_three.Mew;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_three.Rotom;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_two.Beedrill;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_two.Lucario;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_one.phase_two.Voltorb;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_one.DarkGhoul;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_one.HunterxHunter;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_one.OnePiece;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_three.ArmourTitan;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_three.Erwin;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_three.GirlTitan;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_three.Itachi;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_two.AssertredDemon;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_two.BlackDemonKing;
import com.ruseps.world.content.combat.strategy.raids_formulas.raids_two.phase_two.WingedMonster;
import com.ruseps.world.content.combat.strategy.custom_bosses.FallenLord;
import com.ruseps.world.content.combat.strategy.vote_boss.RaichuFormula;
import com.ruseps.world.content.combat.strategy.world_boss.BlessedSpartan;
import com.ruseps.world.content.combat.strategy.world_boss.Aurora;
import com.ruseps.world.content.combat.strategy.world_boss.BlueTelos;
import com.ruseps.world.content.combat.strategy.world_boss.Demon;
import com.ruseps.world.content.combat.strategy.world_boss.DiamondDragon;
import com.ruseps.world.content.combat.strategy.world_boss.DonatorBossAttack;
import com.ruseps.world.content.combat.strategy.world_boss.DragonStoneStrat;
import com.ruseps.world.content.combat.strategy.world_boss.EmeraldDragon;
import com.ruseps.world.content.combat.strategy.world_boss.Octane;
import com.ruseps.world.content.combat.strategy.world_boss.PurpleDragonFormula;
import com.ruseps.world.content.combat.strategy.world_boss.RubyDragon;
import com.ruseps.world.content.combat.strategy.world_boss.VaderFormula;
import com.ruseps.world.content.combat.strategy.world_boss.Inherited;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Blue;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Green;
import com.ruseps.world.content.combat.strategy.zulrah.npc.Red;



public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();
	
	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(174, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(2882, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.put(133, defaultMagicStrategy);
	;
		
		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);
		
		/*STRATEGIES.put(1509,  new LavaDragon());*/
		STRATEGIES.put(1417,  new Octane());
		STRATEGIES.put(701,  new RubyRedDragon());
		STRATEGIES.put(1037,  new FallenLord());
		STRATEGIES.put(1416,  new Inherited());
		STRATEGIES.put(1495,  new DeadlyAurora());
		STRATEGIES.put(4862,  new SharkBeast());
		STRATEGIES.put(1496,  new Shild());
		STRATEGIES.put(1497,  new WingedGoat());
		STRATEGIES.put(1498,  new ArmouredMew());
		STRATEGIES.put(1499,  new SummonedSkull());
		STRATEGIES.put(1488,  new Goro());
		STRATEGIES.put(1489,  new GreenGoro());
		STRATEGIES.put(1490,  new Mileena());
		STRATEGIES.put(1491,  new CyanMileena());
		STRATEGIES.put(1492,  new Scropion());
		STRATEGIES.put(1493,  new SubZero());
		STRATEGIES.put(1504,  new AssertredDemon());
		STRATEGIES.put(1505,  new BlackDemonKing());
		STRATEGIES.put(1507,  new WingedMonster());
		STRATEGIES.put(8496,  new ArmourTitan());
		STRATEGIES.put(8497,  new GirlTitan());
		STRATEGIES.put(8495,  new Erwin());
		STRATEGIES.put(8498,  new Itachi());
		STRATEGIES.put(1502,  new DarkGhoul());
		STRATEGIES.put(1501,  new OnePiece());
		STRATEGIES.put(1500,  new HunterxHunter());
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(3048, new TormentedDemon()); 
		STRATEGIES.put(3200, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(13447, new Nex());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(3334, new WildyWyrm());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(1041, new Smoozies());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(3340, new GiantMole());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1015, new lover_titan());
		STRATEGIES.put(1016, new lover_keeper());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(1038, new LAVA_TOR());
		STRATEGIES.put(1039, new FALLENGOD());
		STRATEGIES.put(9939, new PlaneFreezer());
		STRATEGIES.put(2043, new Green());
		STRATEGIES.put(2042, new Blue());
		STRATEGIES.put(2044, new Red());
		STRATEGIES.put(135, new Fear());
		STRATEGIES.put(133, new Cobra());
		STRATEGIES.put(1472, new Death());
		STRATEGIES.put(132, new Death());
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(51, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		STRATEGIES.put(3030, dragonStrategy);
		STRATEGIES.put(5089, new JackPets());

		BlueTelos telosStrategy = new BlueTelos();
		STRATEGIES.put(3167, telosStrategy);	
		STRATEGIES.put(3168, telosStrategy);
		STRATEGIES.put(3169, telosStrategy);
		/** RAIDS ONE **/
		Starmie starmieStrategy = new Starmie();
		STRATEGIES.put(5151, starmieStrategy);	
		Seadra seadraStrategy = new Seadra();
		STRATEGIES.put(5156, seadraStrategy);
		Lugia lugiaStrategy = new Lugia();
		STRATEGIES.put(5159, lugiaStrategy);
		Beedrill beedrillStrategy = new Beedrill();
		STRATEGIES.put(5150, beedrillStrategy);
		Lucario lucarioStrategy = new Lucario();
		STRATEGIES.put(5158, lucarioStrategy);
		Voltorb voltorbStrategy = new Voltorb();
		STRATEGIES.put(5147, voltorbStrategy);
		Mew mewStrategy = new Mew();
		STRATEGIES.put(5154, mewStrategy);
		Rotom rotomStrategy = new Rotom();
		STRATEGIES.put(5155, rotomStrategy);
		MegaGengar megaStrategy = new MegaGengar();
		STRATEGIES.put(5153, megaStrategy);
		Charizard charStrategy = new Charizard();
		STRATEGIES.put(5157, charStrategy);
		
		RaichuFormula raichuStrategy = new RaichuFormula();
		STRATEGIES.put(4340, raichuStrategy);
		
		B00NY bunnyStrategy = new B00NY();
		STRATEGIES.put(4387, bunnyStrategy);
		STRATEGIES.put(4388, bunnyStrategy);
		STRATEGIES.put(4389, bunnyStrategy);
		
		BlessedSpartan spartanStrategy = new BlessedSpartan();
		STRATEGIES.put(29, spartanStrategy);
		
		Aurora auroraStrategy = new Aurora();
		STRATEGIES.put(6820, auroraStrategy);
		
		Demon demonStrategy = new Demon();
		STRATEGIES.put(4867, demonStrategy);
		
		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);
		
		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);
		
		VaderFormula vaderStrategy = new VaderFormula();
		STRATEGIES.put(466, vaderStrategy);


		DiamondDragon diamondStrategy = new DiamondDragon();
		STRATEGIES.put(163, diamondStrategy);
		
		EmeraldDragon emeraldStrategy = new EmeraldDragon();
		STRATEGIES.put(164, emeraldStrategy);
		
		PurpleDragonFormula purpleStrategy = new PurpleDragonFormula();
		STRATEGIES.put(165, purpleStrategy);
		
		RubyDragon rubyStrategy = new RubyDragon();
		STRATEGIES.put(175, rubyStrategy);
		
		/*SantaFormula santy = new SantaFormula();
		STRATEGIES.put(8540, santy);*/
		
		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(6715, revenantStrategy);
		STRATEGIES.put(6716, revenantStrategy);
		STRATEGIES.put(6701, revenantStrategy);
		STRATEGIES.put(6725, revenantStrategy);
		STRATEGIES.put(6691, revenantStrategy);
		
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(2006, new Vetion());
		STRATEGIES.put(2010, new Callisto());
		STRATEGIES.put(1999, new Cerberus());
		STRATEGIES.put(1017, new Cerb_Lover());
		STRATEGIES.put(1018, new Witch());
		STRATEGIES.put(6766, new LizardMan());
		STRATEGIES.put(499, new Thermonuclear());
		STRATEGIES.put(7286, demonStrategy); //owner boss
		STRATEGIES.put(5886, new Sire());
		STRATEGIES.put(10126, new UnholyCursebearer());
		DonatorBossAttack dBossStartegy = new DonatorBossAttack();
		STRATEGIES.put(704, dBossStartegy);
		
		STRATEGIES.put(706, new DragonStoneStrat());
		
		
	}
	
	public static CombatStrategy getStrategy(int npc) {
		if(STRATEGIES.get(npc) != null) {
			return STRATEGIES.get(npc);
		}
		return defaultMeleeCombatStrategy;
	}
	
	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}


	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}
}
