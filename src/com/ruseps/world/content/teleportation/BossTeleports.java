package com.ruseps.world.content.teleportation;


import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.model.RegionInstance;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.world.content.combat.strategy.zulrah.Zulrah;
import com.ruseps.world.content.instances.Cerberus;
import com.ruseps.world.content.instances.KingBlackDragon;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Boss Teleports
 * 
 * @author Tibo
 */ 
public class BossTeleports {
	public enum GameModes {
		Zulrah("Zulrah", new Item[] { new Item (12926, 1),new Item (12282, 1),new Item (6914, 1),new Item (6889, 1),new Item (2581, 1),new Item (2577, 1),new Item (19778, 1)}, "This is a @gre@safe @whi@& @gre@instanced @whi@area", "", "", "", ""),

		KBD("King Black Dragon",  new Item[] { new Item (11996, 1), new Item(7980, 1), new Item(11613, 1), new Item(6585, 1), new Item(13462, 1), new Item(11286, 1), new Item(18832, 1), new Item(995, 50000000)}, "In a @gre@instanced @whi@or @gre@multi area", "Remember to bring anti fires", "or a dragonfire shield!", "", ""),
		Godwards("Godwars", new Item[] { new Item (11694, 1), new Item(11696, 1), new Item(11698, 1), new Item(11700, 1), new Item(11997, 1), new Item(12002, 1), new Item(12004, 1), new Item(12003, 1), new Item(11724, 1), new Item(11726, 1), new Item(11718, 1), new Item(11720, 1), new Item(11722, 1)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area ", "Kill 25 minions to enter", "bosses as General graardor", "", ""),
		Kalphite("Kalphite Queen", new Item[] { new Item (11993, 1),new Item (8266, 1),new Item (995, 50000000)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area", "Be sure to bring prayer pots", "@gre@Kalphite Queen @whi@ has 2 attack", "styles with 100% accuracy", ""),
		Slashbash("Slash Bash", new Item[] { new Item (11994, 1),new Item (11335, 1),new Item (14481, 1),new Item (995, 100000000)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area", "Great for starters", "", "", ""),
		Frostdraon("Frost Dragons", new Item[] { new Item (11991, 1),new Item (11286, 1),new Item (18832, 1)}, "This is a @gre@Safe @whi@area", "Collect @gre@Frost dragon bones", "and sell them to players or", "use them for yourself", ""),
		Dagannoth("Dagannoth King", new Item[] { new Item (11990, 1),new Item (12005, 1),new Item (12006, 1),new Item (13429, 1),new Item (13428, 1),new Item (13427, 1),new Item (13426, 1),new Item (12601, 1),new Item (6739, 1),new Item (6562, 1)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area ", "Be sure use @gre@Prayer", "", "", ""),
		TDS("Tormented Demons", new Item[] { new Item (11992, 1),new Item (14486, 1),new Item (2581, 1),new Item (10696, 1),new Item (995, 50000000),new Item (14472, 1),new Item (14474, 1),new Item (14476, 1)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area", "Be sure to bring different", "attack styles and use", "Prayer to block hits", ""),
		Barrelchest("Barrelchests", new Item[] { new Item (11972, 1),new Item (10887, 1),new Item (995, 50000000)}, "This is a @gre@Safe @whi@and @gre@multi @whi@area", "", "", "", ""),
		Chaosele("Chaos Elemental", new Item[] { new Item (11995, 1),new Item (13896, 1),new Item (13867, 1),new Item (13883, 1),new Item (13870, 1)}, "This is a @red@unsafe @whi@and @gre@multi @whi@area", "", "", "", ""),
		Lizardman("Lizardman Shaman", new Item[] { new Item (11969, 1),new Item (20555, 1)}, "This is a @red@unsafe @whi@and @gre@multi @whi@area", "", "", "", ""),
		Corp("Corporeal Beast", new Item[] { new Item (13748, 1),new Item (13759, 1),new Item (13750, 1),new Item (13746, 1),new Item (13752, 1)}, "This is a @red@unsafe @whi@and @gre@multi @whi@area", "", "", "", ""),
		Phoenix("Phoenix", new Item[] { new Item (11989, 1),new Item (15126, 1),new Item (19335, 1),new Item (11235, 1),new Item (15241, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Bandosavatar("Bandos Avatar", new Item[] { new Item (11988, 1),new Item (15126, 1),new Item (995, 10000000)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Glacors("Glacors", new Item[] { new Item (20000, 1),new Item (20001, 1),new Item (20002, 1),new Item (18778, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Nex("Nex", new Item[] { new Item (11987, 1),new Item (14008, 1),new Item (14009, 1),new Item (14010, 1),new Item (14011, 1),new Item (14012, 1),new Item (14013, 1),new Item (14014, 1),new Item (14015, 1),new Item (14016, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Scorpia("Scorpia", new Item[] { new Item (11975, 1),new Item (11732, 1),new Item (995, 300000000)}, "This is a @red@unsafe @whi@& @gre@multi @whi@area", "@red@Located in 52 wilderness ", "", "", ""),
		Venenatis("Venenatis", new Item[] { new Item (11976, 1),new Item (12601, 1),new Item (11732, 1),new Item (18778, 1)}, "This is a @red@unsafe @whi@& @gre@multi @whi@area", "@red@Located in 27 wilderness", "", "", ""),
		Cerbertus("Cerbertus", new Item[] { new Item (13247, 1),new Item (13239, 1),new Item (13235, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Skotizo("Skotizo", new Item[] { new Item (11967, 1),new Item (12284, 1),new Item (18778, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		AbyssalSire("Abyssal Sire", new Item[] { new Item (11973, 1),new Item (13045, 1)}, "This is a @red@unsafe @whi@& @gre@multi @whi@area", "@red@Located in 47 wilderness", "", "", ""),
		Giantmole("Giant Mole", new Item[] { new Item (12646, 1),new Item (995, 200000000),new Item (11732, 1),new Item (6739, 1),new Item (15259, 1),new Item (6585, 1),new Item (2572, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Kraken("Kraken", new Item[] { new Item (12655, 1),new Item (13058, 1),new Item (11554, 1),new Item (6914, 1),new Item (6889, 1),new Item (2581, 1),new Item (2577, 1),new Item (989, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", ""),
		Bork("Bork", new Item[] { new Item (17291, 1),new Item (15441, 1)}, "This is a @gre@safe @whi@& @gre@multi @whi@area", "", "", "", "")
;
		private String name;
		private Item[] starterPackItems;
		private String line1;
		private String line2;
		private String line3;
		private String line4;
		private String line5;


		private GameModes(String name, Item[] starterPackItems, String line1, String line2, String line3, String line4, String line5) {
			this.name = name;
			this.starterPackItems = starterPackItems;
			this.line1 = line1;
			this.line2 = line2;
			this.line3 = line3;
			this.line4 = line4;
			this.line5 = line5;

		}
	}
	
	
	public static int tele;

	public static boolean HandleTeleport(Player player) {
		switch(tele) {
		    case 1:
			KingBlackDragon.getDialogue(player);
			break;
		    case 2:
			TeleportHandler.teleportPlayer(player, new Position(2871, 5319, 2), player.getSpellbook().getTeleportType());		
			break;
		    case 3:
		    TeleportHandler.teleportPlayer(player, new Position(3488, 9516, 0), player.getSpellbook().getTeleportType());
			break;
		    case 4:
		    TeleportHandler.teleportPlayer(player, new Position(2547, 9447, 0), player.getSpellbook().getTeleportType());
			break;
		    case 5:
		    TeleportHandler.teleportPlayer(player, new Position(2835, 9517, 0), player.getSpellbook().getTeleportType());
			break;
		    case 6:
		    TeleportHandler.teleportPlayer(player, new Position(1912, 4367, 0), player.getSpellbook().getTeleportType());	
			break;
		    case 7:
		    TeleportHandler.teleportPlayer(player, new Position(2540, 5774, 0), player.getSpellbook().getTeleportType());
			break;
		    case 8:
		    TeleportHandler.teleportPlayer(player, new Position(2973, 9517, 1), player.getSpellbook().getTeleportType());
			break;
		    case 9:
		    TeleportHandler.teleportPlayer(player, new Position(3276, 3915, 0), player.getSpellbook().getTeleportType());
			break;
		    case 10:
		    TeleportHandler.teleportPlayer(player, new Position(2718, 9811, 0), player.getSpellbook().getTeleportType());
			break;
		    case 11:
		    TeleportHandler.teleportPlayer(player, new Position(3104, 5536, 0), player.getSpellbook().getTeleportType());
			break;
		    case 12:
		    TeleportHandler.teleportPlayer(player, new Position(2886, 4376, 0), player.getSpellbook().getTeleportType());
			break;
		    case 13:
			    TeleportHandler.teleportPlayer(player, new Position(2839, 9557, 0), player.getSpellbook().getTeleportType());
				break;
		    case 14:
			    TeleportHandler.teleportPlayer(player, new Position(2891, 4767, 0), player.getSpellbook().getTeleportType());
				break;
		    case 15:
			    TeleportHandler.teleportPlayer(player, new Position(3050, 9573, 0), player.getSpellbook().getTeleportType());
				break;
		    case 16:
			    TeleportHandler.teleportPlayer(player, new Position(2903, 5203, 0), player.getSpellbook().getTeleportType());
				break;
		    case 17:
			    TeleportHandler.teleportPlayer(player, new Position(3236, 3941, 0), player.getSpellbook().getTeleportType());
				break;
		    case 18:
			    TeleportHandler.teleportPlayer(player, new Position(3350, 3734, 0), player.getSpellbook().getTeleportType());
				break;
		    case 19:
				Cerberus.getDialogue(player);
				break;
		    case 20:
			    TeleportHandler.teleportPlayer(player, new Position(3378, 9816, 0), player.getSpellbook().getTeleportType());
				break;
		    case 21:
			    TeleportHandler.teleportPlayer(player, new Position(3370, 3888, 0), player.getSpellbook().getTeleportType());
				break;
		    case 22:
				player.getKraken().enter(player, true);
				break;
		    case 23:
				   player.moveTo(new Position(2268, 3070, player.getIndex() * 4));
					player.setRegionInstance(new RegionInstance(player, RegionInstanceType.ZULRAH));
				   player.getPA().sendMessage("starting zulrah");
				   Zulrah.startBossFight(player);	
					player.getPacketSender().sendInterfaceRemoval();

				   break;
			}
				return false;}
	public static boolean handleButton(Player player, int id) {
		switch(id) {
		case -28725:
			
			HandleTeleport(player);
			break;
	case -28705:
		tele =1;
		Sendinfo(player, GameModes.KBD);
	break;
	case -28704:
		tele =2;
		Sendinfo(player, GameModes.Godwards);
		break;
	case -28703:
		tele =3;
		Sendinfo(player, GameModes.Kalphite);
		break;
	case -28702:
		tele =4;
		Sendinfo(player, GameModes.Slashbash);
		break;
	case -28701:
		tele =5;
		Sendinfo(player, GameModes.Frostdraon);
		break;
	case -28700:
		tele =6;
		Sendinfo(player, GameModes.Dagannoth);
	break;
	case -28699:
		tele =7;
		Sendinfo(player, GameModes.TDS);
	break;
	case -28698:
		tele =8;
		Sendinfo(player, GameModes.Barrelchest);
	break;
	case -28697:
		tele =9;
		Sendinfo(player, GameModes.Chaosele);
		break;
	case -28696:
		tele =10;
		Sendinfo(player, GameModes.Lizardman);
		break;
	case -28695:
		tele =11;
		Sendinfo(player, GameModes.Bork);
		break;
	case -28694:
		tele =12;
		Sendinfo(player, GameModes.Corp);
	break;
	case -28693:
		tele =13;
		Sendinfo(player, GameModes.Phoenix);
	break;
	case -28692:
		tele =14;
		Sendinfo(player, GameModes.Bandosavatar);
	break;
	case -28691:
		tele =15;
		Sendinfo(player, GameModes.Glacors);
	break;
	case -28690:
		tele =16;
		Sendinfo(player, GameModes.Nex);
	break;
	case -28689:
		tele =17;
		Sendinfo(player, GameModes.Scorpia);
	break;
	case -28688:
		tele =18;
		Sendinfo(player, GameModes.Venenatis);
	break;
	case -28687:
		tele =19;
		Sendinfo(player, GameModes.Cerbertus);
	break;
	case -28686:
		tele =20;
		Sendinfo(player, GameModes.Skotizo);
	break;
	case -28685:
		tele =21;
		Sendinfo(player, GameModes.AbyssalSire);
	break;
	case -28684:
		tele =22;
		Sendinfo(player, GameModes.Giantmole);
	break;
	case -28683:
		tele =22;
		Sendinfo(player, GameModes.Kraken);
		break;
	case -28682:
		tele =23;
		Sendinfo(player, GameModes.Zulrah);
		break;

	}
		return false;}

	public static void Sendinfo(Player player, GameModes mode) {
		sendName(player, mode);
		sendStartPackItems(player, mode);
		sendDescription(player, mode);

	}
	public static void sendName(Player player, GameModes mode) {
		player.getPacketSender().sendString(36808, mode.name);

	}
	public static void sendDescription(Player player, GameModes mode) {
		int s = 36926;
		player.getPacketSender().sendString(s, mode.line1);
		player.getPacketSender().sendString(s+1, mode.line2);
		player.getPacketSender().sendString(s+2, mode.line3);
		player.getPacketSender().sendString(s+3, mode.line4);
		player.getPacketSender().sendString(s+4, mode.line5);

	}

	public static void sendStartPackItems(Player player, GameModes mode) {
		final int START_ITEM_INTERFACE = 39921;
		for (int i = 0; i < 28; i++) {
			int id = -1;
			int amount = 0;
			try {
				id = mode.starterPackItems[i].getId();
				amount = mode.starterPackItems[i].getAmount();
			} catch (Exception e) {

			}
			player.getPacketSender().sendItemOnInterface(START_ITEM_INTERFACE + i, id, amount);
		}
	}

	
}
