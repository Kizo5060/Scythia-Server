package com.ruseps.world.content.skill.impl.slayer;

import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.world.content.PlayerPanel;
import com.ruseps.world.entity.impl.player.Player;

public enum SlayerMaster {
	
	VANNAKA(1, 1597, new Position(3026, 2782)), 
	DURADEL(50, 8275, new Position(3026, 2782)),
	KURADEL(80, 9085, new Position(3026, 2782)),
	SUMONA(92, 7780, new Position(3026, 2782)),
	CHAELDAR(130, 1598, new Position(3026, 2782)),
	;
	
	private SlayerMaster(int slayerReq, int npcId, Position telePosition) {
		this.slayerReq = slayerReq;
		this.npcId = npcId;
		this.position = telePosition;
	}
	
	private int slayerReq;
	private int npcId;
	private Position position;
	
	public int getSlayerReq() {
		return this.slayerReq;
	}
	
	public int getNpcId() {
		return this.npcId;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public static SlayerMaster forId(int id) {
		for (SlayerMaster master : SlayerMaster.values()) {
			if (master.ordinal() == id) {
				return master;
			}
		}
		return null;
	}
	
	public static void changeSlayerMaster(Player player, SlayerMaster master) {
		player.getPacketSender().sendInterfaceRemoval();
		if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < master.getSlayerReq()) {
			player.getPacketSender().sendMessage("This Slayer master does not teach noobies. You need a Slayer level of at least "+master.getSlayerReq()+".");
			return;
		}
		if(player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Please finish your current task before changing Slayer master.");
			return;
		}
		if(player.getSlayer().getSlayerMaster() == master) {
			player.getPacketSender().sendMessage(""+Misc.formatText(master.toString().toLowerCase())+" is already your Slayer master.");
			return;
		}
		player.getSlayer().setSlayerMaster(master);
		PlayerPanel.refreshPanel(player);
		player.getPacketSender().sendMessage("You've sucessfully changed Slayer master.");
	}
}
