package com.ruseps.net.packet.impl;

import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import com.ruseps.model.input.Input;
import com.ruseps.world.entity.impl.player.Player;

public class EnterReferral extends Input {
	public static final String[] rewardableUsers = new String[] {"rsorig", "jipy", "", ""};
	public static final String[] temporaryCodes = new String[] {"", "", "", "", ""};
	public static final String[] ReaperCode = new String[] {"reaper", "", ""};
	public static final String[] WreckedCode = new String[] {"wr3ckedyou", "Wr3ckedyou", "wreckedyou"};
	public static final String[] NexArchCode = new String[] {"NexArch", "NexArchrsps", ""};
	public static final String[] InheritedCode = new String[] {"Inherited", "inherited", "inheritedrs"};
	
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
            	player.getInventory().add(18338, 4);
				player.getInventory().add(19994, 10);
				player.getInventory().add(915, 5);
				player.getInventory().add(21056, 1);
				player.getInventory().add(18057, 1);
            	player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
        }
			else if(Arrays.stream(NexArchCode).anyMatch(username::equalsIgnoreCase)) {
				player.getInventory().add(18338, 4);
				player.getInventory().add(19994, 10);
				player.getInventory().add(915, 5);
				player.getInventory().add(21056, 1);
				player.getInventory().add(18057, 1);
				player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
			}
        	else if(Arrays.stream(ReaperCode).anyMatch(username::equalsIgnoreCase)) {
        		player.getInventory().add(18338, 4);
				player.getInventory().add(19994, 10);
				player.getInventory().add(915, 5);
				player.getInventory().add(21056, 1);
				player.getInventory().add(18057, 1);
			player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
        }
			else if(Arrays.stream(InheritedCode).anyMatch(username::equalsIgnoreCase)) {
				player.getInventory().add(18338, 4);
				player.getInventory().add(19994, 10);
				player.getInventory().add(915, 5);
				player.getInventory().add(21056, 1);
				player.getInventory().add(18057, 1);
				player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a special reward!");
			}
        	else if(Arrays.stream(WreckedCode).anyMatch(username::equalsIgnoreCase)) {
        		player.getInventory().add(18338, 4);
				player.getInventory().add(19994, 10);
				player.getInventory().add(915, 5);
				player.getInventory().add(21056, 1);
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