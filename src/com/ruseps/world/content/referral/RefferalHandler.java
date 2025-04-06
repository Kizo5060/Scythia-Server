package com.ruseps.world.content.referral;

import com.ruseps.model.Item;
import com.ruseps.world.entity.impl.player.Player;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class RefferalHandler {
    private HashMap<String, Integer> ipClaimCount = new HashMap<>();
    private RefferalType refType;
    private String optionSelected;

    public void openRefferal(Player player) {
        clearInterface(player);
        setRefferalType(RefferalType.YOUTUBERS);
        sendOptions(player);
        sendRewards(player);
        player.getPA().sendInterface(57750);
    }

    public void sendOptions(Player player) {
        int start = 57768;
        for (RefferalOptions options : RefferalOptions.values()) {
            if (options.getType() == getRefferalType()) {
                for (String option : options.getOptions()) {
                    player.getPacketSender().sendString(start, option);
                    start += 2;
                }
            }
        }
    }

    public void sendRewards(Player player) {
        for (RefferalOptions options : RefferalOptions.values()) {
            if (options.getType() == getRefferalType()) {
                player.getPA().sendItemContainer2(options.getRewards(), 57766);
            }
        }
    }

    public void confirmOption(Player player, String ipAddress) {
        if (canClaimRewards(ipAddress)) {
            for (RefferalOptions options : RefferalOptions.values()) {
                if (options.getType() == getRefferalType()) {
                    for (Item item : options.getRewards()) {
                        player.getInventory().addItem(item);
                    }
                }
            }
            player.referaledby = getOptionSelected();
            player.getPA().sendMessage("Thank you.");

            try {
                BufferedWriter w = new BufferedWriter(new FileWriter("./data/saves/referral_data.txt", true));
                w.write(ipAddress);
                w.newLine();
                w.flush();
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            player.getPA().closeAllWindows();
            player.getAppearance().setCanChangeAppearance(true);
        } else {
            // Handle the case where the user has already claimed rewards twice from the same IP
            player.getPA().sendMessage("You have already claimed rewards twice from this IP address.");
            // You can add additional logic here as needed
        }
    }

    private boolean canClaimRewards(String ipAddress) {
        if (ipClaimCount.containsKey(ipAddress)) {
            int claims = ipClaimCount.get(ipAddress);
            if (claims < 2) {
                ipClaimCount.put(ipAddress, claims + 1);
                return true;
            } else {
                return false;
            }
        } else {
            ipClaimCount.put(ipAddress, 1);
            return true;
        }
    }

    private void clearInterface(Player player) {
        int start = 0;
        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(57768 + start, "");
            start += 2;
        }
    }

    public RefferalType getRefferalType() {
        return refType;
    }

    public void setRefferalType(RefferalType refType) {
        this.refType = refType;
    }

    public String getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(String optionSelected) {
        this.optionSelected = optionSelected;
    }
}
