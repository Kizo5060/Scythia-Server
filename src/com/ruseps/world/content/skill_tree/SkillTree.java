package com.ruseps.world.content.skill_tree;

import java.util.ArrayList;
import java.util.List;

public class SkillTree
{
    private List<Perk> perks = new ArrayList<>();
    private int exp = 0;
    private int[] level = new int[10];

    public void addPerk(Perk perk) 
    {
        if (perks.size() < 10)
        {
            perks.add(perk);
        } else {
            System.out.println("Maximum number of perks reached");
        }
    }

    public Perk getPerk(String name) 
    {
        for (Perk perk : perks) 
        {
            if (perk.getPerkType().getName().equals(name))
            {
                return perk;
            }
        }
        return null;
    }

    public int getPerkLevel(String name) 
    {
        Perk perk = getPerk(name);
        if (perk != null) 
        {
            return perk.getLevel();
        } else {
            return -1;
        }
    }

    public void addExp(int exp) 
    {
        this.exp += exp;
        System.out.println("You gained " + exp + " exp. Current exp: " + this.exp);
    }

    public void subtractExp(int exp) 
    {
        this.exp -= exp;
    }

    public int getExp() 
    {
        return this.exp;
    }
}
