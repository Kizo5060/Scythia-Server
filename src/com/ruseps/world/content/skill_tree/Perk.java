package com.ruseps.world.content.skill_tree;

public class Perk 
{
    private PerkType perkType;
    private int level = 0;
    private static final int MAX_LEVEL = 3;
    private int[] expRequired = new int[MAX_LEVEL];

    public Perk(PerkType perkType, int[] expRequired) 
    {
        this.perkType = perkType;
        if(expRequired.length == MAX_LEVEL) 
        {
            this.expRequired = expRequired;
        }
    }

    public PerkType getPerkType() 
    {
        return perkType;
    }

    public int getLevel() 
    {
        return level;
    }

    public int getExpRequired() 
    {
        return level < MAX_LEVEL ? expRequired[level] : -1;
    }

    public void claim(SkillTree tree) 
    {
        if (level < MAX_LEVEL)
        {
            if (tree.getExp() >= expRequired[level]) 
            {
                tree.subtractExp(expRequired[level]);
                level++;
                int rewardId = 0;
                switch(level) 
                {
                    case 1:
                        rewardId = perkType.getLevelOneReward();
                        break;
                    case 2:
                        rewardId = perkType.getLevelTwoReward();
                        break;
                    case 3:
                        rewardId = perkType.getLevelThreeReward();
                        break;
                }
                System.out.println("You have claimed " + perkType.getName() + ". Current level: " + level + ". You've received " + rewardId + " as a reward.");
            } else {
                System.out.println("Not enough experience to claim " + perkType.getName());
            }
        } else {
            System.out.println("You have reached the max level for " + perkType.getName());
        }
    }

    public void process() 
    {
        // Implement your own perk's effect here
        System.out.println("Processing " + perkType.getName() + " at level " + level);
    }
}
