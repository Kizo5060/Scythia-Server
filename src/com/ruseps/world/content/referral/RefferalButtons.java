package com.ruseps.world.content.referral;

import com.ruseps.world.entity.impl.player.Player;

public class RefferalButtons {

    public static boolean isRefferalButton(Player player, int buttonId) {
        switch (buttonId) {
            case -7773:
                player.getRefferalHandler().setRefferalType(RefferalType.YOUTUBERS);
                player.getRefferalHandler().sendOptions(player);
                player.getRefferalHandler().sendRewards(player);
                return true;

            case -7772:
                player.getRefferalHandler().setRefferalType(RefferalType.WEBSITES);
                player.getRefferalHandler().sendOptions(player);
                player.getRefferalHandler().sendRewards(player);
                return true;

            case -7771:
                player.getRefferalHandler().setRefferalType(RefferalType.OTHER);
                player.getRefferalHandler().sendOptions(player);
                player.getRefferalHandler().sendRewards(player);
                return true;

            case -7783: // Close button
                player.getPA().closeAllWindows();
                return true;

            case -7777: // Confirm
                player.getRefferalHandler().confirmOption(player, player.getHostAddress());
                return true;

            case -7768: // Option #1
                for (RefferalOptions options : RefferalOptions.values()) {
                    if (options.getType() == player.getRefferalHandler().getRefferalType()) {
                        player.getRefferalHandler().setOptionSelected(options.getOptions()[0]);
                        player.getPA().sendMessage("Option selected: " + options.getOptions()[0]);
                    }
                }
                return true;
			
		case -7766: // Option #2
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[1]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[1]);
				}
			}
			return true;
			
		case -7764: // Option #3
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[2]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[2]);
				}
			}
			return true;
			
		case -7762: // Option #4
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[3]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[3]);
				}
			}
			return true;
			
		case -7760: // Option #5
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[4]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[4]);
				}
			}
			return true;
			
		case -7758: // Option #6
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[5]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[5]);
				}
			}
			return true;
			
		case -7756: // Option #7
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[6]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[6]);
				}
			}
			return true;
			
		case -7754: // Option #8
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[7]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[7]);
				}
			}
			return true;
			
		case -7752: // Option #9
			for(RefferalOptions options : RefferalOptions.values()) {
				if(options.getType() == player.getRefferalHandler().getRefferalType()) {
					player.getRefferalHandler().setOptionSelected(options.getOptions()[8]);
					player.getPA().sendMessage("Option selected: " + options.getOptions()[8]);
				}
			}
			return true;
			
		}

		return false;
	}

}
