package com.ruseps;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ruseps.util.RSAKeyGenerator;
import com.ruseps.util.ShutdownHook;
import com.ruseps.world.content.Cows;
import com.ruseps.world.content.DiscordBot.DiscordBot;

public class GameServer {

	private static final GameLoader loader = new GameLoader(GameSettings.GAME_PORT);
	private static final Logger logger = Logger.getLogger("Simplicity");
	private static boolean updating;

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			
			logger.info("Starting Up Scythia...");
			loader.init();
			loader.finish();
			/** COMPILE **/
			logger.info("Scythia is now ONLINE!");
			
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start Scythia Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		GameServer.updating = updating;
	}

	public static boolean isUpdating() {
		return GameServer.updating;
	}
}