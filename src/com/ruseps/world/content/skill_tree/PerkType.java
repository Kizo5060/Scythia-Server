package com.ruseps.world.content.skill_tree;

public enum PerkType 
{
    DROPRATE_PERK(1, "Perk 1", "This is Perk 1", 666, 666, 666),
    DAMAGE_PERK(2, "Perk 2", "This is Perk 2", 666, 666, 666),
    SPEED_PERK(3, "Perk 3", "This is Perk 3", 666, 666, 666);
 
    private final int id;
    private final String name;
    private final String description;
    private final int reward_1;
    private final int reward_2;
    private final int reward_3;

    PerkType(int id, String name, String description, int reward_1, int reward_2, int reward_3)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reward_1 = reward_1;
        this.reward_2 = reward_2;
        this.reward_3 = reward_3;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public int getLevelOneReward()
    {
        return reward_1;
    }
    
    public int getLevelTwoReward() 
    {
        return reward_2;
    }
    
    public int getLevelThreeReward() 
    {
        return reward_3;
    }
}
