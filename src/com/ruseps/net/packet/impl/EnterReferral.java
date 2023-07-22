package com.ruseps.net.packet.impl;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class EnterReferral extends Input {
	public static final String[] rewardableUsers = new String[] {"rsorig", "jipy", "", ""};
	public static final String[] temporaryCodes = new String[] {"solara", "", "", "", ""};
	public static final String[] merkCode = new String[] {"fpk merk", "merk", "Fpk Merk"};
	public static final String[] OwenCode = new String[] {"Gamer Owen", "Owen", "7ds"};
	public static final String[] CasaCode = new String[] {"Casalusio", "Casa", "casalusio"};
	public static final String[] solaraCode = new String[] {"solara", "solara", "solarascape"};
	public static final String[] NoobsCode = new String[] {"noob", "Noobs own", "Noobsown", "Noobs Own", "noobs own"};
	public static final String[] WreckedCode = new String[] {"wr3ckedyou", "Wr3ckedyou", "wreckedyou"};
	public static final String[] GhostCode = new String[] {"ghost", "ghostrsps", "ghost rsps"};
	public static final String[] LanoCode = new String[] {"Lano", "lano", "lano rsps"};
	public static final String[] WalkCode = new String[] {"walkchaos", "Walkchaos", "walk chaos"};
    @Override
    public void handleSyntax(Player player, String syntax) {
    	if (checkIps(player.getHostAddress())) {
            player.getPacketSender().sendMessage("@red@You have already received a referral reward!");
            return;
    	} // no clue its same on mine.
        player.hasReferral = true;
        referralResponse(player, syntax);
    
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("./data/saves/referral_data.txt", true));
            w.write(player.getHostAddress());
            w.newLine();
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	
    public static void referralResponse(Player player, String username) {
        	if(Arrays.stream(rewardableUsers).anyMatch(username::equalsIgnoreCase)) {
        	        player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
            } else if(Arrays.stream(temporaryCodes).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(18338, 1);
            	player.getInventory().add(19992, 5);
            	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
            	
            } else if(Arrays.stream(merkCode).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(18338, 1);
            	player.getInventory().add(19992, 5);
            	player.getInventory().add(915, 2);
            	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
            	
        	} else if(Arrays.stream(OwenCode).anyMatch(username::equalsIgnoreCase)) {
        	player.getInventory().add(18338, 1);
        	player.getInventory().add(19992, 5);
        	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
        	else if(Arrays.stream(CasaCode).anyMatch(username::equalsIgnoreCase)) {
			player.getInventory().add(18338, 2);
			player.getInventory().add(19992, 5);
			player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
			else if(Arrays.stream(GhostCode).anyMatch(username::equalsIgnoreCase)) {
				player.getInventory().add(18338, 4);
				player.getInventory().add(19992, 10);
				player.getInventory().add(915, 3);
				player.getInventory().add(18057, 1);
				player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
			}
        	else if(Arrays.stream(solaraCode).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(18338, 4);
            	player.getInventory().add(19992, 10);
            	player.getInventory().add(915, 3);
            	player.getInventory().add(18057, 1);
			player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
        	else if(Arrays.stream(NoobsCode).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(18338, 4);
            	player.getInventory().add(19992, 10);
            	player.getInventory().add(915, 3);
            	player.getInventory().add(18057, 1);
			player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
			else if(Arrays.stream(LanoCode).anyMatch(username::equalsIgnoreCase)) {
				player.getInventory().add(18338, 4);
				player.getInventory().add(19992, 10);
				player.getInventory().add(915, 3);
				player.getInventory().add(18057, 1);
				player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
			}
			else if(Arrays.stream(WalkCode).anyMatch(username::equalsIgnoreCase)) {
				player.getInventory().add(18338, 4);
				player.getInventory().add(19992, 10);
				player.getInventory().add(915, 3);
				player.getInventory().add(18057, 1);
				player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
			}
        	else if(Arrays.stream(WreckedCode).anyMatch(username::equalsIgnoreCase)) {
            	player.getInventory().add(18338, 4);
            	player.getInventory().add(19992, 10);
            	player.getInventory().add(915, 3);
            	player.getInventory().add(18057, 1);
			player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
    }
    
    public static boolean checkIps(String ip) {
        File file = new File("./data/saves/referral_data.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String content = null;
        while(scanner.hasNext()) {
            content = scanner.nextLine();
            if (ip.equalsIgnoreCase(content)) {
                return true;
            }
        }
        return false;
    }
}