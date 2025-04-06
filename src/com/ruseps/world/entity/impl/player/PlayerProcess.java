package com.ruseps.world.entity.impl.player;

import com.ruseps.world.content.PlayerPanel;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.RegionInstance.RegionInstanceType;
import com.ruseps.util.Misc;
import com.ruseps.world.content.Kraken;
import com.ruseps.world.content.LoyaltyProgramme;
import com.ruseps.world.content.combat.pvp.BountyHunter;
import com.ruseps.world.content.skill.impl.construction.House;
import com.ruseps.world.entity.impl.GroundItemManager;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player will be given loyalty
	 * points. 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	private int playerPanelTick;
	/*
	 * The timer tick, once this reaches 2, the player's total play time will be
	 * updated. 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getPosition().getZ();
	}

	public void sequence() {

		/** SKILLS **/
		if (player.shouldProcessFarming()) {
			player.getFarming().sequence();
		}

		if (player.getCombatBuilder().isAttacking()) {

		} else {
			if (player.walkableInterfaceList.contains(41020)) {
				player.sendParallellInterfaceVisibility(41020, false);
			}
		}

		/** MISC **/

		if (player.getDailyTaskManager().getDailyDate() != player.getDailyTaskManager().getTodayDate()) {
			player.getDailyTaskManager().refresh();
			player.getDailyTaskManager().setDailyDate(player.getDailyTaskManager().getTodayDate());
//                   Achievements.resetDailys(player);
		}

		if (previousHeight != player.getPosition().getZ()) {
			GroundItemManager.handleRegionChange(player);
			previousHeight = player.getPosition().getZ();
		}

		if (playerPanelTick >= 100) {
			PlayerPanel.refreshPanel(player);
			playerPanelTick = 0;
		}
		playerPanelTick++;

		if (!player.isInActive()) {
			if (loyaltyTick >= 6) {
				LoyaltyProgramme.incrementPoints(player);
				loyaltyTick = 0;
			}
			loyaltyTick++;
		}

		if (player.getLocation() == Location.KRAKEN) {
			if (player.getKrakenRespawn().elapsed(7500)
					&& player.getKraken().getKrakenStage() == Kraken.KrakenStage.DEFAULT) {
				// System.out.println("got here kraken respawn");
				player.getKraken().enter(player, false);
			}
		}

		if (timerTick >= 1) {
			player.getPacketSender().sendString(26710, "@or2@Time played:  @gre@"
					+ Misc.getTimePlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())));
			timerTick = 0;
		}
		timerTick++;

		BountyHunter.sequence(player);

		if (player.getRegionInstance() != null
				&& (player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_HOUSE
						|| player.getRegionInstance().getType() == RegionInstanceType.CONSTRUCTION_DUNGEON)) {
			((House) player.getRegionInstance()).process();
		}
	}
}
