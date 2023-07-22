package com.ruseps.world.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ruseps.GameSettings;
import com.ruseps.model.Position;
import com.ruseps.world.entity.impl.player.Player;



public class PlayerPunishments {

	public static class Jail {

		private static Player[] JAILED_PLAYERS = new Player[10];

		public static int findSlot() {
			for (int i = 0; i < JAILED_PLAYERS.length; i++) {
				if (JAILED_PLAYERS[i] == null) {
					return i;
				}
			}
			return -1;
		}

		public static int getIndex(Player plr) {
			for (int i = 0; i < JAILED_PLAYERS.length; i++) {
				Player p = JAILED_PLAYERS[i];
				if (p == null)
					continue;
				if (p == plr) {
					return i;
				}
			}
			return -1;
		}

		public static boolean isJailed(Player plr) {
			return getIndex(plr) >= 0;
		}

		public static boolean jailPlayer(Player p) {
			int emptyCell = findSlot();
			if (emptyCell == -1) {
				return false;
			}
			Position pos = null;
			switch (emptyCell) {
			case 0:
				pos = new Position(1905, 4277);
				break;
			case 1:
				pos = new Position(1903, 4279);
				break;
			case 2:
				pos = new Position(1906, 4276);
				break;
			case 3:
				pos = new Position(1906, 4280);
				break;
			case 4:
				pos = new Position(1904, 4282);
				break;
			}
			p.moveTo(pos);
			JAILED_PLAYERS[emptyCell] = p;
			return true;
		}

		public static void unjail(Player plr) {
			int index = getIndex(plr);
			if (index >= 0) {
				JAILED_PLAYERS[index] = null;
			}
			plr.moveTo(GameSettings.DEFAULT_POSITION.copy());
		}

	}

	private static boolean readPunished(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			br.close();
			if (line == null) {
				return true;
			} else {
				Long time = Long.parseLong(line);
				if (System.currentTimeMillis() - time > 0) {
					return true;
				} else {
					f.delete();
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private static void checkPunishment(File f) {
		try {
			f.createNewFile();
			writePunished(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writePunished(File f) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPlayerBanned(String name) {
		File f = new File(PLAYER_BAN_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isIpBanned(String ip) {
		File f = new File(IP_BAN_DIRECTORY + ip.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isPcBanned(String add) {
		if (add.length() <= 1 || add.equals("")) {
			return false;
		}
		File f = new File(PC_BAN_DIRECTORY + add);
		return f.exists() && readPunished(f);
	}

	public static boolean isMacBanned(String add) {
		File f = new File(MAC_BAN_DIRECTORY + add);
		return f.exists() && readPunished(f);
	}

	public static boolean isIpMuted(String ip) {
		File f = new File(IP_MUTE_DIRECTORY + ip.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isMacMuted(String mac) {
		File f = new File(MAC_MUTE_DIRECTORY + mac.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isMuted(String name) {
		File f = new File(PLAYER_MUTE_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isYellMuted(String name) {
		File f = new File(YELL_MUTE_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static boolean isVoteBanned(String name) {
		File f = new File(VOTE_BAN_DIRECTORY + name.toLowerCase());
		return f.exists() && readPunished(f);
	}

	public static void ipBan(String ip) {
		File f = new File(IP_BAN_DIRECTORY + ip);
		checkPunishment(f);
	}

	public static void voteBan(String vote) {
		File f = new File(VOTE_BAN_DIRECTORY + vote);
		checkPunishment(f);
	}

	public static void pcBan(String add) {
		if (add.toLowerCase().equals("none") || add.toLowerCase().equals("not_set")) {
			return;
		}
		File f = new File(PC_BAN_DIRECTORY + add);
		checkPunishment(f);
	}

	public static void macBan(String add) {
		if (add.toLowerCase().equals("none") || add.toLowerCase().equals("not_set")) {
			return;
		}
		File f = new File(MAC_BAN_DIRECTORY + add);
		checkPunishment(f);
	}

	public static void ban(String name) {
		File f = new File(PLAYER_BAN_DIRECTORY + name);
		checkPunishment(f);
	}

	public static void ipMute(String ip) {
		File f = new File(IP_MUTE_DIRECTORY + ip);
		checkPunishment(f);
	}

	public static void macMute(String mac) {
		File f = new File(MAC_MUTE_DIRECTORY + mac);
		checkPunishment(f);
	}

	public static void massBan(String victimUsername) {
		// Player victim = new Player(null);
		// to-do
	}

	/*public static void unmassBan(String victimUsername) {
		Player victim = new Player(null);
		String serial = String.valueOf(victim.getLastMacAddress());
		String ip = victim.getLastIpAddress();
		String mac = victim.getMac();
		unBan(victimUsername);
		unPcBan(serial);
		unIpBan(ip);
		unMacBan(mac);
	}*/
	


	public static void mute(String name) {
		try {
			new File(PLAYER_MUTE_DIRECTORY + name).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void yellMute(String name) {
		try {
			new File(YELL_MUTE_DIRECTORY + name.toLowerCase()).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void unBan(String name) {
		new File(PLAYER_BAN_DIRECTORY + name).delete();
	}

	public static void unIpBan(String ip) {
		new File(IP_BAN_DIRECTORY + ip).delete();
	}

	public static void unIpMute(String ip) {
		new File(IP_MUTE_DIRECTORY + ip).delete();
	}

	public static void unMacMute(String mac) {
		new File(MAC_MUTE_DIRECTORY + mac).delete();
	}

	public static void unMute(String name) {
		new File(PLAYER_MUTE_DIRECTORY + name).delete();
	}

	public static void unYellMute(String name) {
		new File(YELL_MUTE_DIRECTORY + name).delete();
	}

	public static void unMacBan(String mac) {
		new File(MAC_BAN_DIRECTORY + mac).delete();
	}

	public static void unVoteBan(String vote) {
		new File(VOTE_BAN_DIRECTORY + vote).delete();
	}

	public static void unPcBan(String add) {
		new File(PC_BAN_DIRECTORY + add).delete();
	}

	/**
	 * The general punishment folder directory.
	 */
	public static final String PUNISHMENT_DIRECTORY = "./punishments";

	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String PLAYER_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/player_bans/";

	/**
	 * Leads to directory where muted account files are stored.
	 */
	public static final String PLAYER_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/player_mutes/";

	/**
	 * Leads to directory where yell muted account files are stored.
	 */
	public static final String YELL_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/yell_mutes/";

	/**
	 * Leads to directory where muted account files are stored.
	 */
	public static final String VOTE_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/vote_bans/";

	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String IP_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/ip_bans/";

	/**
	 * Leads to directory where muted account files are stored.
	 */
	public static final String IP_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/ip_mutes/";

	public static final String MAC_MUTE_DIRECTORY = PUNISHMENT_DIRECTORY + "/mac_mutes/";

	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String MAC_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/mac_bans/";

	/**
	 * Leads to directory where banned account files are stored.
	 */
	public static final String PC_BAN_DIRECTORY = PUNISHMENT_DIRECTORY + "/pc_bans/";

}
