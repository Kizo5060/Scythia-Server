package com.ruseps.world.content.battle_pass;

import java.util.Calendar;
import java.util.Date;

import com.ruseps.util.Misc;
import com.ruseps.model.Item;
import com.ruseps.model.container.impl.Bank;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class BattlePass
{
	protected Player player;
	
	public BattlePass(Player player)
	{
		this.player = (player);
	}
	
	private boolean ResetSeasonOne = false;
    private int level = 0, experience = 0;
    private BattlePassType type = BattlePassType.NONE;
    private BattlePassPages currentPage = BattlePassPages.PAGE_ONE;

    public boolean isResetSeasonOne() {
        return ResetSeasonOne;
    }

    public void setResetSeasonOne(boolean resetSeasonOne) {
        ResetSeasonOne = resetSeasonOne;
    }

    public int getLevel() {
        return level;
    }
    
    public void incrementLevel(int lvl) 
	 {
		 this.level += lvl;
	 }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public BattlePassType getType() {
        return type;
    }

    public void setType(BattlePassType type) {
        this.type = type;
    }

    public BattlePassPages getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(BattlePassPages currentPage) {
        this.currentPage = currentPage;
    }

	public void displayPage()
	{
		int count1 = 0;
		int count2 = 0;
		player.getPA().resetItemsOnInterface(13798, 8)
		.resetItemsOnInterface(13799, 8);
		for(BattlePassData data : BattlePassData.values()) 
		{
			if(currentPage.equals(data.getPage())) {
				player.getPacketSender().sendItemOnInterface(13798, data.getBronzeReward().getId(), count1++, data.getBronzeReward().getAmount());
				player.getPacketSender().sendItemOnInterface(13799, data.getGoldReward().getId(), count2++, data.getGoldReward().getAmount());
				
			}
		}
		
		player.getPA().sendString(13789, "Current Level: " + "@gre@" + this.level);
		player.getPA().sendString(13790, "Experience: " + "@gre@" + Misc.formatNumberToLetter(this.experience));
		player.getPA().sendString(13791, "Pass Type: " + "@whi@" + this.type);
		
		checkLevelUp();
		updateLevels(player);
		player.getPA().sendInterface(13782);
		player.getPA().sendString(13788, "There are two types of Battle Pass available to purchase Bronze and Gold.");
		player.getPA().sendString(13800, "You will recieve your reward upon leveling up");
	}

	public void unlockBronzePass(){
		if(player.getBattlePass().getType() == BattlePassType.BRONZE) 
		{
			player.getPacketSender().sendMessage("@blu@You already have the <col=964B00>Bronze Pass.");
			return;
		}
		if(player.getBattlePass().getType() == BattlePassType.GOLD) 
		{
			player.getPacketSender().sendMessage("@blu@You already have the @yel@Gold Pass.");
			return;
		}
		player.getBattlePass().setType(BattlePassType.BRONZE);
		World.sendMessage("<shad=1>@whi@"+ player.getUsername() +" Has Unlocked <col=964B00>Bronze Battle Pass@whi@, Congratulation!");
		
	
		if(this.level == 0) {
			this.level =1 ;
		}
    	//player.getInventory().delete(new Item(21879, 1));
		
		player.getInventory().delete(2996);
		
    	Calendar c = Calendar.getInstance();

		c.setTime(new Date());
		player.bronzeBattlepassClaimed = c.getTime().toString();
		Calendar c1 = Calendar.getInstance();

		c1.setTime(new Date()); // Now use today date.

		c1.add(Calendar.DATE, 30);
		player.bronzeBattlepassExpires = c1.getTime().toString();
		player.getPA().sendMessage("@blu@You have claimed and unlocked bronze battle pass! until " +c1.getTime());
	}
	public void unlockGoldPass() 
	{
		if(player.getBattlePass().getType() == BattlePassType.GOLD) 
		{
			player.getPacketSender().sendMessage("@blu@You already have the @yel@Gold Pass.");
			return;
		}
		if(this.level == 0) {
			this.level =1 ;
		}
		player.getBattlePass().setType(BattlePassType.GOLD);
		World.sendMessage("<shad=1>@whi@"+ player.getUsername() +" Has Unlocked @yel@Gold Battle Pass@whi@, Congratulation!");
    	player.getInventory().delete(4067);
		for(BattlePassData data : BattlePassData.values()) {
			if(data.getLevel() <= player.getBattlePass().getLevel())
			{
				player.getBank(Bank.getTabForItem(player, data.getBronzeReward().getId())).add(data.getBronzeReward());
				player.getBank(Bank.getTabForItem(player, data.getGoldReward().getId())).add(data.getGoldReward());
			}
		}

    	Calendar c = Calendar.getInstance();

		c.setTime(new Date()); // Now use today date.
		player.goldBattlepassClaimed = c.getTime().toString();
		Calendar c1 = Calendar.getInstance();

		c1.setTime(new Date()); // Now use today date.

		c1.add(Calendar.DATE, 30);
		player.goldBattlepassExpires = c1.getTime().toString();
		player.getPA().sendMessage("@blu@You have claimed and unlocked Gold battlepass!");
		player.getPA().sendMessage("@blu@All Gold Pass rewards until your current level has been sent to your bank.");
		player.getPA().sendMessage("@blu@Your gold pass expires "+c1.getTime().toString());
	}
	
	public void updateLevels(Player player) {
        if(player.getBattlePass().getCurrentPage() == BattlePassPages.PAGE_ONE) {
            /** BRONZE **/
            player.getPA().sendString(13802, "Lvl: 1");
            player.getPA().sendString(13803, "Lvl: 2");
            player.getPA().sendString(13804, "Lvl: 3");
            player.getPA().sendString(13805, "Lvl: 4");
            player.getPA().sendString(13806, "Lvl: 5");
            player.getPA().sendString(13807, "Lvl: 6");
            player.getPA().sendString(13808, "Lvl: 7");
            player.getPA().sendString(13809, "Lvl: 8");
            /** GOLD **/
            player.getPA().sendString(13810, "Lvl: 1");
            player.getPA().sendString(13811, "Lvl: 2");
            player.getPA().sendString(13812, "Lvl: 3");
            player.getPA().sendString(13813, "Lvl: 4");
            player.getPA().sendString(13814, "Lvl: 5");
            player.getPA().sendString(13815, "Lvl: 6");
            player.getPA().sendString(13816, "Lvl: 7");
            player.getPA().sendString(13817, "Lvl: 8");
        } else if(player.getBattlePass().getCurrentPage() == BattlePassPages.PAGE_TWO) {
            /** BRONZE **/
            player.getPA().sendString(13802, "Lvl: 9");
            player.getPA().sendString(13803, "Lvl: 10");
            player.getPA().sendString(13804, "Lvl: 11");
            player.getPA().sendString(13805, "Lvl: 12");
            player.getPA().sendString(13806, "Lvl: 13");
            player.getPA().sendString(13807, "Lvl: 14");
            player.getPA().sendString(13808, "Lvl: 15");
            player.getPA().sendString(13809, "Lvl: 16");
            /** GOLD **/
            player.getPA().sendString(13810, "Lvl: 9");
            player.getPA().sendString(13811, "Lvl: 10");
            player.getPA().sendString(13812, "Lvl: 11");
            player.getPA().sendString(13813, "Lvl: 12");
            player.getPA().sendString(13814, "Lvl: 13");
            player.getPA().sendString(13815, "Lvl: 14");
            player.getPA().sendString(13816, "Lvl: 15");
            player.getPA().sendString(13817, "Lvl: 16");
        } else if(player.getBattlePass().getCurrentPage() == BattlePassPages.PAGE_THREE){
            /** BRONZE **/
            player.getPA().sendString(13802, "Lvl: 17");
            player.getPA().sendString(13803, "Lvl: 18");
            player.getPA().sendString(13804, "Lvl: 19");
            player.getPA().sendString(13805, "Lvl: 20");
            player.getPA().sendString(13806, "Lvl: 21");
            player.getPA().sendString(13807, "Lvl: 22");
            player.getPA().sendString(13808, "Lvl: 23");
            player.getPA().sendString(13809, "Lvl: 24");
            /** GOLD **/
            player.getPA().sendString(13810, "Lvl: 17");
            player.getPA().sendString(13811, "Lvl: 18");
            player.getPA().sendString(13812, "Lvl: 19");
            player.getPA().sendString(13813, "Lvl: 20");
            player.getPA().sendString(13814, "Lvl: 21");
            player.getPA().sendString(13815, "Lvl: 22");
            player.getPA().sendString(13816, "Lvl: 23");
            player.getPA().sendString(13817, "Lvl: 24");
        } else if(player.getBattlePass().getCurrentPage() == BattlePassPages.PAGE_FOUR) {
        	/** BRONZE **/
            player.getPA().sendString(13802, "Lvl: 25");
            player.getPA().sendString(13803, "Lvl: 26");
            player.getPA().sendString(13804, "Lvl: 27");
            player.getPA().sendString(13805, "Lvl: 28");
            player.getPA().sendString(13806, "Lvl: 29");
            player.getPA().sendString(13807, "Lvl: 30");
            player.getPA().sendString(13808, "Lvl: 31");
            player.getPA().sendString(13809, "Lvl: 32");
            /** GOLD **/
            player.getPA().sendString(13810, "Lvl: 25");
            player.getPA().sendString(13811, "Lvl: 26");
            player.getPA().sendString(13812, "Lvl: 27");
            player.getPA().sendString(13813, "Lvl: 28");
            player.getPA().sendString(13814, "Lvl: 29");
            player.getPA().sendString(13815, "Lvl: 30");
            player.getPA().sendString(13816, "Lvl: 31");
            player.getPA().sendString(13817, "Lvl: 32");
        } else if(player.getBattlePass().getCurrentPage() == BattlePassPages.PAGE_FIVE) {
        	/** BRONZE **/
            player.getPA().sendString(13802, "Lvl: 33");
            player.getPA().sendString(13803, "Lvl: 34");
            player.getPA().sendString(13804, "Lvl: 35");
            player.getPA().sendString(13805, "Lvl: 36");
            player.getPA().sendString(13806, "Lvl: 37");
            player.getPA().sendString(13807, "Lvl: 38");
            player.getPA().sendString(13808, "Lvl: 39");
            player.getPA().sendString(13809, "Lvl: 40");
            /** GOLD **/
            player.getPA().sendString(13810, "Lvl: 33");
            player.getPA().sendString(13811, "Lvl: 34");
            player.getPA().sendString(13812, "Lvl: 35");
            player.getPA().sendString(13813, "Lvl: 36");
            player.getPA().sendString(13814, "Lvl: 37");
            player.getPA().sendString(13815, "Lvl: 38");
            player.getPA().sendString(13816, "Lvl: 39");
            player.getPA().sendString(13817, "Lvl: 40");
        } 
    }
	
	public void checkLevelUp() {
	    BattlePassData e = BattlePassData.getByLevel(this.level);

	    if (e == null) {
	        System.out.println("Error: BattlePassData not found for level " + this.level);
	        return;
	    }

	    int exp = e.getExp();

	    if (this.level == 40) {
	        System.out.println("Already at max level");
	        return;
	    }

	    System.out.println("BATTLEPASS LEVEL: " + this.level);
	    System.out.println("BATTLEPASS EXP: " + this.experience);
	    System.out.println("REQUIRED EXP: " + exp);

	    if (e.isExperienceEnough(this.experience)) {
	        this.experience -= exp;
	        this.level++;
	        if(this.level == 20) {
	        	World.sendMessage("<shad=1>@red@" + player.getUsername() +" Has Reached Level 20 In The Battle Pass!");
	        }
	        if(this.level == 40) {
	        	World.sendMessage("<shad=1>@red@" + player.getUsername() +" Has Completed The Battle Pass, Congratulation!");
	        }
	        player.getPA().sendMessage("@blu@Congratulations! Your Battle Pass leveled up! Your level now is @red@" + this.level);
	        giveReward(level, type);
	        System.out.println("LEVEL UP! New BATTLEPASS LEVEL: " + this.level);
	        System.out.println("Remaining BATTLEPASS EXP: " + this.experience);
	    }
	}

	
	public void giveReward(int level, BattlePassType type) 
	{
		BattlePassData e = BattlePassData.getByLevel(level);
		String bronzeReward = e.getBronzeReward().getDefinition().getName();
		String goldReward = e.getGoldReward().getDefinition().getName();
		
		if(type == BattlePassType.BRONZE)
		{
			if(e.getBronzeReward().getId() != -1) 
			{
				player.addItemToAny(e.getBronzeReward().getId(),e.getBronzeReward().getAmount());
				player.getPA().sendMessage("@blu@<shad=1>[BattlePass] <col=964B00>" + bronzeReward + ",@blu@ has been sent to your Inventory or bank." );
			}
		} else if (type == BattlePassType.GOLD)
		{
			if(e.getBronzeReward().getId() != -1)
			{
				player.getPA().sendMessage("@blu@<shad=1>[BattlePass] <col=964B00>" + bronzeReward + ",@blu@ has been sent to your Inventory or bank." );
				player.addItemToAny(e.getBronzeReward().getId(),e.getBronzeReward().getAmount());
			}
			if(e.getGoldReward().getId() != -1)
			{
				player.getPA().sendMessage("@blu@<shad=1>[BattlePass] @yel@" + goldReward + ",@blu@ has been sent to your Inventory or bank." );
				player.addItemToAny(e.getGoldReward().getId(),e.getGoldReward().getAmount());
			}
		}
	}
	
	public void nextPage() 
	{
		switch(getCurrentPage())
		{
		case PAGE_ONE:
			setCurrentPage(BattlePassPages.PAGE_TWO);
			updateLevels(player);
			break;
		case PAGE_TWO:
			setCurrentPage(BattlePassPages.PAGE_THREE);
			updateLevels(player);
			break;
		case PAGE_THREE:
			setCurrentPage(BattlePassPages.PAGE_FOUR);
			updateLevels(player);
			break;
		case PAGE_FOUR:
			setCurrentPage(BattlePassPages.PAGE_FIVE);
			updateLevels(player);
			break;
		case PAGE_FIVE:
			updateLevels(player);
			player.getPA().sendMessage("@blu@You have reached the last page for the battlepass");
			break;
		default:
			break;
		}
	}
	public void prevPage()
	{
		switch(getCurrentPage()) 
		{
		case PAGE_ONE:
			player.getPA().sendMessage("@blu@You have reached the first page for the battlepass");
			break;
		case PAGE_TWO:
			setCurrentPage(BattlePassPages.PAGE_ONE);
			updateLevels(player);
			break;
		case PAGE_THREE:
			setCurrentPage(BattlePassPages.PAGE_TWO);
			updateLevels(player);
			break;
		case PAGE_FOUR:
			setCurrentPage(BattlePassPages.PAGE_THREE);
			updateLevels(player);
			break;
		case PAGE_FIVE:
			setCurrentPage(BattlePassPages.PAGE_FOUR);
			updateLevels(player);
			break;
		default:
			break;
		}
	}
	
	public void handleClick(int Id) 
    {
        if(Id == 13795)
        {
            prevPage();
            displayPage();
        } else if(Id == 13794) {
            nextPage();
            displayPage();
        } else if(Id == 22168) {
            player.getPA().closeAllWindows();
        } else if(Id == 13797) {
            player.getPA().sendString(1, "https://nexarch.teamgames.io/services/store"); // GOLD PASS LINK
        } else if(Id == 13796) {
            player.getPA().sendString(1, "https://nexarch.teamgames.io/services/store"); // BRONZE PASS LINK
        }
    }
	
	public void addExperience(int exp)
	{
		if(player.getBattlePass().getType() != BattlePassType.GOLD && player.getBattlePass().getType() != BattlePassType.BRONZE) 
		{
			return;
		}
		
		if(this.level == 40) 
		{
			return;
		} 
		player.sendMessage("@red@You have gained " + exp + " experience to your Battle Pass!");
		this.experience += exp;
		checkLevelUp();
	
	}
}
