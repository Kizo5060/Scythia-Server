package com.ruseps.world.content.loot_display;

import com.ruseps.world.entity.impl.player.Player;

public class RewardsHandler {
	
	protected Player player;
	
	private static final RewardsData information[] = RewardsData.values();
	
	public RewardsHandler(Player player) {
		this.player = player;
	}
	
	public static void open(Player player) {
		SendRewardStrings(player);
		player.getPacketSender().sendInterface(17550);
	}
	
	public void open(int id) {
		RewardsData data = RewardsData.byId.get(id);
		id = data.getIndex();
		SendRewardStrings(player);
		ItemGroup(id);
		player.getPacketSender().sendInterface(17550);
	}
	
	public void ItemGroup(int id) {
		RewardsData data = RewardsData.byId.get(id);
		if (data != null) {
			for (int index = 0; index < data.getItemID().length; index++) {
				player.getPacketSender().sendItemOnInterface(17752, data.getItemID()[index], index, 1);
			}
		}
        player.getPacketSender().sendString(17556, "Rewards List From: " + data.getText());
	}
	
	public static void SendRewardStrings(Player player) {
		int startID = 17651;
		for (RewardsData data : information) {
           player.getPacketSender().sendString(startID++, data.getText());
		}
	}	
	
	public void ResetFrame34() {
		int interfaceId = 17752;
			player.getPacketSender().sendItemOnInterface(interfaceId, 0, 0);
			interfaceId++;
	}
	
	public boolean button(int buttonId) {
		switch (buttonId) {
		case 17651:
			ResetFrame34();
			ItemGroup(1);
			return true;
		case 17652:
			ResetFrame34();
			ItemGroup(2);
			return true;
		case 17653:
			ResetFrame34();
			ItemGroup(3);
			break;
		case 17654:
			ResetFrame34();
			ItemGroup(4);
			break;
		case 17655:
			ResetFrame34();
			ItemGroup(5);
			break;
		case 17656:
			ResetFrame34();
			ItemGroup(6);
			break;
		case 17657:
			ResetFrame34();
			ItemGroup(7);
			break;
		case 17658:
			ResetFrame34();
			ItemGroup(8);
			break;
		case 17659:
			ResetFrame34();
			ItemGroup(9);
			break;
		case 17660:
			ResetFrame34();
			ItemGroup(10);
			break;
		case 17661:
			ResetFrame34();
			ItemGroup(11);
			break;
		case 17662:
			ResetFrame34();
			ItemGroup(12);
			break;
		case 17663:
			ResetFrame34();
			ItemGroup(13);
			break;
		case 17664:
			ResetFrame34();
			ItemGroup(14);
			break;
		case 17665:
			ResetFrame34();
			ItemGroup(15);
			break;
		case 17666:
			ResetFrame34();
			ItemGroup(16);
			break;
		case 17667:
			ResetFrame34();
			ItemGroup(17);
			break;
		case 17668:
			ResetFrame34();
			ItemGroup(18);
			break;
			
		case 17669:
			ResetFrame34();
			ItemGroup(19);
			break;	
		case 17670:
			ResetFrame34();
			ItemGroup(20);
			break;
		case 17671:
			ResetFrame34();
			ItemGroup(21);
			break;
		case 17672:
			ResetFrame34();
			ItemGroup(22);
			break;	
		case 17673:
			ResetFrame34();
			ItemGroup(23);
			break;
		case 17674:
			ResetFrame34();
			ItemGroup(24);
			break;
		case 17675:
			ResetFrame34();
			ItemGroup(25);
			break;
		case 17676:
			ResetFrame34();
			ItemGroup(26);
			break;

		}
	return false;
	}
}
