package com.ruseps.world.content.battle_pass;

import com.ruseps.world.content.battle_pass.NonItemReward;
import com.ruseps.world.entity.impl.player.Player;
import lombok.Getter;

public class BattlePassExperience implements NonItemReward 
{	
	@Getter
	private final int amount;
	
	public BattlePassExperience(int amount) 
	{
		this.amount = amount;
	}
	
	@Override
	public void giveReward(Player player) 
	{
		player.getBattlePass().addExperience(amount);
	}

	@Override
	public String rewardDescription() 
	{
		return "-" + amount + " BattlePass Experience.";
	}
}
