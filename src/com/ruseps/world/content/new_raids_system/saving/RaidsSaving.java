package com.ruseps.world.content.new_raids_system.saving;

import com.ruseps.world.entity.impl.player.Player;

public class RaidsSaving
{
	/*
	 * This file was created so that new Booleans may save to the Player, without causing resets, to control access, and phase automation.
	 */
	private Player p;
	
	public boolean initPhase2 = false;
	public boolean initPhase3 = false;
	public boolean initPhase4 = false;
	public boolean initPhase5 = false;
	public boolean initPhase6 = false;
	public boolean initPhase7 = false;
	public boolean initPhase8 = false;
	public boolean initPhase9 = false;
	public boolean initPhase10 = false;
	
	public boolean completedRaids1 = false;
	public boolean completedRaids2 = false;
    public boolean completedRaids3 = false;
    public boolean completedRaids4 = false;
    public boolean completedRaids5 = false;
    public boolean completedRaids6 = false;
    public boolean completedRaids7 = false;
    public boolean completedRaids8 = false;
    public boolean completedRaids9 = false;
    public boolean completedRaids10 = false;
	
	public RaidsSaving(Player player) 
	{
		this.p = player;
	}
}