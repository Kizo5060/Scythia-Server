package com.ruseps.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	/*
	 * A regular member of the server.
	 */
	PLAYER(-1, null, 1, 1),
	/*
	 * A moderator who has more privilege than other regular members and donators.
	 */
	MODERATOR(-1, "<col=20B2AA>", 1, 1.5),

	/*
	 * The second-highest-privileged member of the server.
	 */
	ADMINISTRATOR(-1, "<col=FFFF64>", 1, 1.75),
	
	
	
	/*
	 * The highest-privileged member of the server
	 */
	OWNER(-1, "<col=B40404>", 1, 2),

	/*
	 * The Developer of the server, has same rights as the owner.
	 */
	DEVELOPER(-1, "<col=fa0505>", 1, 1),


	/*
	 * A member who has donated to the server. 
	 */
	BRONZE_MEMBER(60, "<shad=FF7F00>", 1.5, 1.1),
	SILVER_MEMBER(40, "<col=787878>", 1.5, 1.25),
 	GOLD_MEMBER(20, "<col=D9D919>", 2, 1.5),
	PLATINUM_MEMBER(10, "<shad=697998>", 2.5, 1.75),
	DIAMOND_MEMBER(0, "<col=99CCCC>", 3, 2),
	//RUBY_MEMBER(0, "<col=B40404>", 1, 2.5),

	/*
	 * A member who has the ability to help people better.
	 */
	SUPPORT(-1, "@blu@", 1, 1.35),

	/*
	 * A member who has been with the server for a long time.
	 */
	VETERAN(30, "<col=CD661D>", 1, 1),
	
	/*
	 * Manager rank for the fucking cunt ass nigger fucks of the server fucking
	 * cunt ass mother fucking annoying fucking idiots aka DJ - IsmokeDope was here says Alex
	 */
	RUBY_MEMBER(0, "<col=B40404>", 4, 2.5),
	DRAGONSTONE_MEMBER(0, "<col=351C75>", 6, 7.5),
	MANAGER(-1, "<col=FFFF64>", 1, 2),;

	PlayerRights(int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColorPrefix = yellHexColorPrefix;
		this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
		this.experienceGainModifier = experienceGainModifier;
	}
	
	private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR, ADMINISTRATOR, OWNER, MANAGER, DEVELOPER);
	private static final ImmutableSet<PlayerRights> MEMBERS = Sets.immutableEnumSet(BRONZE_MEMBER, SILVER_MEMBER, GOLD_MEMBER, PLATINUM_MEMBER, RUBY_MEMBER, DIAMOND_MEMBER, DRAGONSTONE_MEMBER);
	
	/*
	 * The yell delay for the rank
	 * The amount of seconds the player with the specified rank must wait before sending another yell message.
	 */
	private int yellDelay;
	private String yellHexColorPrefix;
	private double loyaltyPointsGainModifier;
	private double experienceGainModifier;
	
	public int getYellDelay() {
		return yellDelay;
	}
	
	/*
	 * The player's yell message prefix.
	 * Color and shadowing.
	 */
	
	public String getYellPrefix() {
		return yellHexColorPrefix;
	}
	
	/**
	 * The amount of loyalty points the rank gain per 4 seconds
	 */
	public double getLoyaltyPointsGainModifier() {
		return loyaltyPointsGainModifier;
	}
	
	public double getExperienceGainModifier() {
		return experienceGainModifier;
	}
	
	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	public boolean isMember() {
		return MEMBERS.contains(this);
	}
	
	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}
}
