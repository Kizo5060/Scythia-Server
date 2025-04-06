package com.ruseps.world.content;

import com.ruseps.util.Misc;
import com.ruseps.world.content.EvilTrees;
import com.ruseps.world.content.ShootingStar;
import com.ruseps.world.content.bossevents.GameEvent;
import com.ruseps.world.content.bossevents.GameEventManager;
import com.ruseps.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ruseps.model.definitions.DropUtils;

public class PlayerPanel {

	// #1
	public static final String LINE_START = "            ";
	public static final String LINE5_START = " ";
	public static final String LINE1_START = "                ";
	public static final String LINE3_START = "              ";
	public static final String LINE2_START = " ";
	// #2
	public static final String LINE6_START = " ";
	public static final String MIDDLE_START = " ";
	public static final String MIDDLE2_START = " ";
	// #3
	public static final String LINE7_START = "          ";
	public static final String LINE9_START = " ";
	public static final String MIDDLE3_START = "           ";
	public static final String MIDDLE8_START = " ";
	public static final String MIDDLE7_START = "              ";
	public static final String MIDDLE6_START = " ";
	// #4
	public static final String MIDDLE9_START = " ";
	public static final String MIDDLE10_START = "            ";
	public static final String MIDDLE11_START = " ";
	public static final String MIDDLE12_START = "          ";
	// #5
	public static final String MIDDLE13_START = " ";
	public static final String MIDDLE14_START = " ";

	// #quest tab1
	public static final String Quest_START = "               ";
	public static final String Quest2_START = "                ";
	public static final String Quest3_START = "                 ";
	public static final String Quest4_START = "                     ";
	public static final String Quest5_START = "                   ";
	public static final String Quest6_START = "                  ";
	// #quest tab 1 under writting
	// #quest tab1
	public static final String QuestU_START = "                    ";
	public static final String QuestU2_START = "                      ";
	public static final String QuestU3_START = "-                    ";
	public static final String QuestU4_START = "                    ";
	public static final String QuestU5_START = "                       ";
	public static final String QuestU6_START = "                  ";

	private static int FIRST_STRING = 39159;
	private static int LAST_STRING = 39234;
	
	public static void sendQuestTabLogin(Player player) {
		
	}
    public static String getFormatedTime(GameEvent event) {

        long timeLeft = event.getLastEventInstant() + event.getDelayBetweenEvents() - System.currentTimeMillis();
        int hoursLeft = (int) TimeUnit.MILLISECONDS.toHours(timeLeft);
        int minutesLeft = (int) TimeUnit.MILLISECONDS.toMinutes(timeLeft) - (hoursLeft * 60);
        int secondsLeft = (int) TimeUnit.MILLISECONDS.toSeconds(timeLeft) - (hoursLeft * 60 * 60) - (minutesLeft * 60);

        if (hoursLeft < 0 || minutesLeft < 0 || secondsLeft < 0) {
            return "Soon";
        }

        return (hoursLeft < 10 ? "0" : "") + hoursLeft + ":" + (minutesLeft < 10 ? "0" : "") + minutesLeft + ":" + (secondsLeft < 10 ? "0" : "") + secondsLeft;
    }


	public static void handleSwitch(Player player, int index, boolean fromCurrent) {
		if (!fromCurrent) {
			resetStrings(player);
		}
		player.currentPlayerPanelIndex = index;
		switch (index) {
		case 1: //no bro just figuring out this kk bet
			refreshPanel(player); // first tab, cba rename just yet.
			break;

		case 2:
			sendSecondTab(player);
			break;
		case 3:
			sendThirdTab(player);
			break;
		case 4:
			sendForthTab(player);
			break;
		}
	}

	public static void refreshCurrentTab(Player player) {
		handleSwitch(player, player.currentPlayerPanelIndex, true);
	}

	public static void refreshPanel(Player player) {

		if (player.currentPlayerPanelIndex != 1) {
			refreshCurrentTab(player);
			return;
		}
		String[] Messages = new String[] { "  ", "@or1@Player Data", "",
				"@or2@Username: @gre@" + player.getUsername(), "@or2@Rank: @gre@" + player.getRights().toString(),
				"@or2@Time Played: @gre@"
						+ Misc.getHoursPlayed((player.getTotalPlayTime() + player.getRecordedLogin().elapsed())),
				"@or2@Donated: @gre@" + player.getAmountDonated(),
				"@or2@Current Droprate: @gre@" + DropUtils.drBonus(player),
				"@or2@Server Time: @gre@" + Misc.getCurrentServerTime(),
				"@or2@Exp Lock: @gre@" + (player.experienceLocked() ? "@red@Locked" : "@gre@Unlocked"),
				"@or2@Game Mode: @gre@"+ (Misc.formatText(player.getGameMode().name().toLowerCase())),
				 "",
				"@or1@Slayer Data:", "", "@or2@Slayer Master: @gre@" + player.getSlayer().getSlayerMaster(),
				"@or2@Duo Partner: @gre@" + player.getSlayer().getDuoPartner(),
				"@or2@Slayer Task: @gre@" + player.getSlayer().getSlayerTask(),
				"@or2@Task Amount: @gre@" + player.getSlayer().getAmountToSlay(),
				"@or2@Task Streak: @gre@" + player.getSlayer().getTaskStreak(), "",
				};
		for (int i = 0; i < Messages.length; i++) {
			// System.out.println("i "+i+" FIRST_STRING "+FIRST_STRING+" LAST_STRING
			// "+LAST_STRING);
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("1PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);
		}
	}

	private static void sendSecondTab(Player player) {
		List<String> events = new ArrayList<>();
        int index = 0;

        for (GameEvent event : GameEventManager.getEvents().values()) {
            player.getPacketSender()
                    .sendString((index > 50 ? 12174 : 8145) + index++, event.name());
            if (index == 1) {
                index++;
            }
            events.add("@whi@" + event.name());
            if (event.isActive()) {
                events.add("The event is currently @gre@[ Active ]");
            } else {
                events.add("The event will start in:");
                events.add("@yel@"+ getFormatedTime(event));
            }
        }

		String[] Messages = new String[] { "                 @bla@World Events",
				/*"@or2@Evil Tree: @gre@"
						+ (EvilTrees.getRandom() != null ? EvilTrees.getRandom().playerPanelFrame : "N/A"),
				"",
				"@or2@Crashed Star:@gre@"
						+ (ShootingStar.getRandom() != null ? ShootingStar.getRandom().playerPanelFrame : "N/A"),
						*/} ;
		int i = 0;
		for (i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("2PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}
		for(String toAdd: events) {
			player.getPacketSender().sendString(i + FIRST_STRING, toAdd);
			i++;
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("1PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}
		}

	}

	private static void sendThirdTab(Player player) {

		String[] Messages = new String[] { "  ", "@or2@Points @or1@& @or2@Statistics", "",

				"@or2@Loyalty Points: @gre@" + player.getPointsHandler().getLoyaltyPoints(),
				
				"@or2@Boss Points: @gre@" + player.getBossPoints(),

				"@or2@Prestige Points: @gre@" + player.getPointsHandler().getPrestigePoints(),
				
				"@or2@Raid Points: @gre@" + player.getPointsHandler().getRaidsOnePoints(),

				"@or2@Trivia Points: @gre@" + player.getPointsHandler().getTriviaPoints(),

				"@or2@Voting Points: @gre@" + player.getPointsHandler().getVotingPoints(),

				"@or2@Donation Points:  @gre@" + player.getPointsHandler().getDonationPoints(),

				"@or2@Commendation Points: @gre@" + player.getPointsHandler().getCommendations(),
				
				"@or2@Slayer Points: @gre@" + player.getPointsHandler().getSlayerPoints(),
				
				//"@or2@Raids Two Points: @gre@" + player.getPointsHandler().getRaidsTwoPoints(),
				
		


				

		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("3PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void sendForthTab(Player player) {

		String[] Messages = new String[] {"                 @bla@KillTracker", "",
				"@or2@Snowman Kills: @gre@" + player.getNpcKillCount(5049),
				"@or2@Ryuk Kills: @gre@" + player.getNpcKillCount(4990),
				"@or2@Jesus Kills: @gre@" + player.getNpcKillCount(4991),
				"@or2@Simba Kills: @gre@" + player.getNpcKillCount(4992),
				"@or2@Kid Sora Kills: @gre@" + player.getNpcKillCount(4999),
				"@or2@Sully Kills: @gre@" + player.getNpcKillCount(4994),
				"@or2@Charizard Kills: @gre@" + player.getNpcKillCount(4981),
				"@or2@Sauron Kills: @gre@" + player.getNpcKillCount(4997),
				"@or2@Squidward Kills: @gre@" + player.getNpcKillCount(4993),
				"@or2@Ice Demon Kills: @gre@" + player.getNpcKillCount(4980),
				"@or2@Eve Kills: @gre@" + player.getNpcKillCount(4271),
				"@or2@Gimlee Kills: @gre@" + player.getNpcKillCount(4265),
				"@or2@Blood Ele Kills: @gre@" + player.getNpcKillCount(4267),
				"@or2@Tiki Demon Kills: @gre@" + player.getNpcKillCount(4268),
				"@or2@Aragorn Kills: @gre@" + player.getNpcKillCount(4270),
				"@or2@Rayquaza Kills: @gre@" + player.getNpcKillCount(4275),
				"@or2@Legolas Kills: @gre@" + player.getNpcKillCount(3008),
				"@or2@Darth Maul Kills: @gre@" + player.getNpcKillCount(5048),
				"@or2@Diamond Head Kills: @gre@" + player.getNpcKillCount(4998),
				"@or2@Darius Nex Kills: @gre@" + player.getNpcKillCount(4263),
				"@or2@Deadly Robot Kills: @gre@" + player.getNpcKillCount(4264),
				"@or2@Zeldorado Kills: @gre@" + player.getNpcKillCount(4606),
				"@or2@Heatblast Kills: @gre@" + player.getNpcKillCount(4266),
				"@or2@Kevin Four Arms Kills: @gre@" + player.getNpcKillCount(4269),
				"@or2@Golden Knights Kills: @gre@" + player.getNpcKillCount(4272),
				"@or2@Dark Knight Kills: @gre@" + player.getNpcKillCount(3009),
				"@or2@Bad Bitch Kills: @gre@" + player.getNpcKillCount(4274),
				"@or2@Cannonbolt Kills: @gre@" + player.getNpcKillCount(3010),
				"@or2@Red Assasin Kills: @gre@" + player.getNpcKillCount(3011),
				"@or2@Evil Ass Clown Kills: @gre@" + player.getNpcKillCount(3014),
				"@or2@Yvaltal Kills: @gre@" + player.getNpcKillCount(190),
				"@or2@Dooms Day Kills: @gre@" + player.getNpcKillCount(185),
				"@or2@Mountain Dweller Kills: @gre@" + player.getNpcKillCount(4387),
				"@or2@Dark Magician Kills: @gre@" + player.getNpcKillCount(1506),
				"@or2@Obelisk Kills: @gre@" + player.getNpcKillCount(1510),
				"@or2@Blood Queen Kills: @gre@" + player.getNpcKillCount(1508),
				"@or2@Four Arms Kills: @gre@" + player.getNpcKillCount(1509),
				"@or2@Galaxy Titans Kills: @gre@" + player.getNpcKillCount(1016),
				"@or2@Octane: @gre@" + player.getNpcKillCount(1417),
				"@or2@Limes: @gre@" + player.getNpcKillCount(1017),
				"@or2@Slifer: @gre@" + player.getNpcKillCount(1018),
				"@or2@Fallen God: @gre@" + player.getNpcKillCount(1039),
				"@or2@Torment: @gre@" + player.getNpcKillCount(1038),
				"@or2@Litch: @gre@" + player.getNpcKillCount(1494),
				"@or2@Spuderman: @gre@" + player.getNpcKillCount(3012),
				"@or2@Dr. Strange: @gre@" + player.getNpcKillCount(3007),
				"@or2@Gods Ruler: @gre@" + player.getNpcKillCount(1511),
				"@or2@Golden Freeza: @gre@" + player.getNpcKillCount(5148),
				"@dre@ Overall NPC Kills: @whi@" + player.getNpcKills(),

		};

		for (int i = 0; i < Messages.length; i++) {
			if (i + FIRST_STRING > LAST_STRING) {
				System.out.println("4PlayerPanel(" + player.getUsername() + "): " + i + " is larger than max string: "
						+ LAST_STRING + ". Breaking.");
				break;
			}

			player.getPacketSender().sendString(i + FIRST_STRING, Messages[i]);

		}

	}

	private static void resetStrings(Player player) {
		for (int i = FIRST_STRING; i < LAST_STRING; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}
}
