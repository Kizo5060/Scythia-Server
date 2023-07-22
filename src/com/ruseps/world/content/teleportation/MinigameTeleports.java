package com.ruseps.world.content.teleportation;


import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.world.content.instances.Cerberus;
import com.ruseps.world.content.instances.KingBlackDragon;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

/**
 * Boss Teleports
 * 
 * @author Tibo
 */ 
public class MinigameTeleports {
	public enum Minigames {
		MARVEL("Marvel Raids",2026,"Obtain a variety of different barrows", "equipment rewards from this minigame!", "", "Also.. don't forget to bring a @gre@spade", "","", "", "", "", "", new int[] {1823, 5154, 2}),
		TELEPORT_1("Barrows",2026,"Obtain a variety of different barrows", "equipment rewards from this minigame!", "", "Also.. don't forget to bring a @gre@spade", "","", "", "", "", "", new int[] {3565, 3313, 0}),
		TELEPORT_2("Fight Caves",2611,"Battle head-to-head with the TzTok-Jad", "and receive the powerful tokhaar-kal", "and fire cape!", "", "","", "", "", "", "", new int[] {2445, 5177, 0}),
		TELEPORT_3("Fight Pits",2611,"The rules of the Fight Pits are simple,", " the last man standing, wins.", "Winners will receive a red skull icon above their head,", "signifying them as the winner.", "","@gre@Start collecting Tokkul today!", "", "", "", "", new int[] {2399, 5177, 0}),
		TELEPORT_4("Pest Control",3789,"Players must defend an NPC", "known as the Void Knight from an onslaught of monsters", "while at the same time destroying the portals", "from which the monsters spawn.", "","@gre@Start collecting pc points today!", "", "", "", "", new int[] {2663, 2654, 0}),
		TELEPORT_5("Duel Arena",373,"Stake your items against other players!", "", "Only stake items that you can lose", "@red@Read rules before accepting", "","", "", "", "", "", new int[] {3364, 3267, 0}),
		TELEPORT_6("Warrior's Guild",650,"Fight animated armors to obtain warrior", "guild tokens.", "", "With your tokens, you can acces", "the cyclopse room and earn defenders!","", "", "", "", "", new int[] {2855, 3543, 0}),
		TELEPORT_7("Recipe For Disaster",3385,"Help Gypse to destroy the", "Culinaromancer to gain gloves from", "Bronze to dragon", "", "","", "", "", "", "", new int[] {3080, 3498, 0}),
		TELEPORT_8("Nomad's Requeim",8591,"Defeat nomad to gain acces to", "his store", "", "", "","", "", "", "", "", new int[] {1891, 3177, 0}),
		TELEPORT_9("Treasure Island",133,"Welcome to Treasure Island", "Kill all the npcs to gain the ", "chest with great rewards", "", "","", "", "", "", "", new int[] {3039, 2910, 0})

;
		private String name;
		private String line1;
		private String line2;
		private String line3;
		private String line4;
		private String line5;
		private String line6;
		private String line7;
		private String line8;
		private String line9;
		private String line10;
		private int[] teleportCoordinates;
		private int Npcid;

		/**
		 * Setting the teleport coordinates.
		 * @return
		 */
		public int[] getCoordinates() {
			return teleportCoordinates;
		}

		private Minigames(String name1,int Npcid, String line1, String line2, String line3, String line4, String line5, String line6, String line7, String line8, String line9, String line10,final int[] teleportCoordinates) {
			this.name = name1;
			this.Npcid = Npcid;
			this.line1 = line1;
			this.line2 = line2;
			this.line3 = line3;
			this.line4 = line4;
			this.line5 = line5;
			this.line6 = line6;
			this.line7 = line7;
			this.line8 = line8;
			this.line9 = line9;
			this.line10 = line10;
			this.teleportCoordinates = teleportCoordinates;


		}
	}
	
	

	public static boolean handleButton(Player player, int id) {
		
		switch(id) {
		case -26731:
		    TeleportHandler.teleportPlayer(player, new Position(x, y), player.getSpellbook().getTeleportType());
		    break;
		case -26726:
			Sendinfo(player, Minigames.TELEPORT_1);
			break;
		case -26725:
			Sendinfo(player, Minigames.TELEPORT_2);
			break;
		case -26724:
			Sendinfo(player, Minigames.TELEPORT_3);
			break;
		case -26723:
			Sendinfo(player, Minigames.TELEPORT_4);
			break;
		case -26722:
			Sendinfo(player, Minigames.TELEPORT_5);
			break;
		case -26721:
			Sendinfo(player, Minigames.TELEPORT_6);
			break;
		case -26720:
			Sendinfo(player, Minigames.TELEPORT_7);
			break;
		case -26719:
			Sendinfo(player, Minigames.TELEPORT_8);
			break;
		case -26718:
			Sendinfo(player, Minigames.TELEPORT_9);
			break;

	}
		return false;}

	public static void Sendinfo(Player player, Minigames mode) {
		sendName(player, mode);
		sendDescription(player, mode);
		SendTeleport(player, mode);

	}
	
	public static void SendTeleport(Player player, Minigames mode) {
		x = mode.getCoordinates()[0];
        y = mode.getCoordinates()[1];
	}
	public static int x;
	public static int y;
	public static void sendName(Player player, Minigames mode) {
		player.getPacketSender().sendString(38808, mode.name);

	}
	
	public static void sendDescription(Player player, Minigames mode) {
		int s = 38830;
		player.getPacketSender().sendString(s, mode.line1);
		player.getPacketSender().sendString(s+1, mode.line2);
		player.getPacketSender().sendString(s+2, mode.line3);
		player.getPacketSender().sendString(s+3, mode.line4);
		player.getPacketSender().sendString(s+4, mode.line5);
		player.getPacketSender().sendString(s+5, mode.line6);
		player.getPacketSender().sendString(s+6, mode.line7);
		player.getPacketSender().sendString(s+7, mode.line8);
		player.getPacketSender().sendString(s+8, mode.line9);
		player.getPacketSender().sendString(s+9, mode.line10);

	}


	
}
