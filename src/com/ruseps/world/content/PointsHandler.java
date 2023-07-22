package com.ruseps.world.content;

import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.world.content.battle_royale.BattleRoyale;
public class PointsHandler {

	private Player p;
	
	public PointsHandler(Player p) {
		this.p = p;
	}
	
	public void reset() {
		dungTokens = commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = BattleRoyalPoints = 0);
		p.getPlayerKillingAttributes().setPlayerKillStreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	public PointsHandler refreshPanel() {
			
		p.getPacketSender().sendString(26719, "@or2@  <img=40>@whi@ Player Statistics");

		p.getPacketSender().sendString(26722, "@or2@Trivia Points: @gre@"+triviaPoints);

		p.getPacketSender().sendString(26721, "@or2@Prestige Points: @gre@"+prestigePoints);
		p.getPacketSender().sendString(26725, "@or2@Commendations: @gre@ "+commendations);
		p.getPacketSender().sendString(26720, "@or2@Loyalty Points: @gre@"+(int)loyaltyPoints);
		p.getPacketSender().sendString(26727, "@or2@Bossing Points: @gre@ "+p.getBossPoints());

		p.getPacketSender().sendString(26726, "@or2@Dung. Tokens: @gre@ "+dungTokens);
		p.getPacketSender().sendString(26723, "@or2@Voting Points: @gre@ "+votingPoints);
		p.getPacketSender().sendString(26728, "@or2@Slayer Points: @gre@"+slayerPoints);
		p.getPacketSender().sendString(26729, "@or2@Pk Points: @gre@"+pkPoints);
		p.getPacketSender().sendString(26724, "@or2@Donation Points: @gre@"+donationPoints);
		p.getPacketSender().sendString(26735, "@or2@BattleRoyalPoints: @gre@"+BattleRoyalPoints);

		p.getPacketSender().sendString(26730, "@or2@Wilderness Killstreak: @gre@"+p.getPlayerKillingAttributes().getPlayerKillStreak());
		p.getPacketSender().sendString(26731, "@or2@Wilderness Kills: @gre@"+p.getPlayerKillingAttributes().getPlayerKills());		
		p.getPacketSender().sendString(26732, "@or2@Wilderness Deaths: @gre@"+p.getPlayerKillingAttributes().getPlayerDeaths());
		p.getPacketSender().sendString(26733, "@or2@Arena Victories: @gre@"+p.getDueling().arenaStats[0]);
		p.getPacketSender().sendString(26734, "@or2@Arena Losses: @gre@"+p.getDueling().arenaStats[1]);
		p.getPacketSender().sendString(26735, "@or2@Raids 1 Points: @gre@"+(int)raidsOnePoints);
		return this;
	}

	private int prestigePoints;
	private int triviaPoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	private int achievementPoints;
	private int donationPoints;
	private int BattleRoyalPoints;
	private int raidsOnePoints;
	private int raidsTwoPoints;
	
	public int getRaidsTwoPoints() {
		return (int)this.raidsTwoPoints;
	}

	public void setRaidsTwoPoints(int raidsTwoPoints) {
		this.raidsTwoPoints = raidsTwoPoints;
	}
	
	public void incrementRaidsTwoPoints(double amount) {
		this.raidsTwoPoints += amount;
	}
	
	public void decrementRaidsTwoPoints(double amount) {
		this.raidsTwoPoints -= amount;
	}
	
	public int getRaidsOnePoints() {
		return (int)this.raidsOnePoints;
	}

	public void setRaidsOnePoints(int raidsOnePoints) {
		this.raidsOnePoints = raidsOnePoints;
	}
	
	public void incrementRaidsOnePoints(double amount) {
		this.raidsOnePoints += amount;
	}
	
	public void decrementRaidsOnePoints(double amount) {
		this.raidsOnePoints -= amount;
	}
	
	public void multiplyRaidOnePoints(double amount) {
		this.raidsOnePoints *= amount;
	}
	
	public int getPrestigePoints() {
		return prestigePoints;
	}
	
	public void setPrestigePoints(int points, boolean add) {
		if(add)
			this.prestigePoints += points;
		else
			this.prestigePoints = points;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}
	
	public int getDonationPoints(){
		return donationPoints;
	}

	public void setDonationPoints(int donationPoints, boolean add) {
		if(add)
			this.donationPoints += donationPoints;
		else
			this.donationPoints = donationPoints;
	}
		public void incrementDonationPoints(double amount) {
		this.donationPoints += amount;
	}
		public int getTriviaPoints(){
			return triviaPoints;
		}

		public void setTriviaPoints(int triviaPoints, boolean add) {
			if(add)
				this.triviaPoints += triviaPoints;
			else
				this.triviaPoints = triviaPoints;
		}
			public void incrementTriviaPoints(double amount) {
			this.triviaPoints += amount;
		}
	public void setSlayerPoints(int slayerPoints, boolean add) {
		if(add)
			this.slayerPoints += slayerPoints;
		else
			this.slayerPoints = slayerPoints;
	}

	public int getCommendations() {
		return this.commendations;
	}

	public void setCommendations(int commendations, boolean add) {
		if(add)
			this.commendations += commendations;
		else
			this.commendations = commendations;
	}

	public int getLoyaltyPoints() {
		return (int)this.loyaltyPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if(add)
			this.loyaltyPoints += points;
		else
			this.loyaltyPoints = points;
	}
	
	public void incrementLoyaltyPoints(double amount) {
		this.loyaltyPoints += amount;
	}
	
	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if(add)
			this.pkPoints += points;
		else
			this.pkPoints = points;
	}
	public int getBattleRoyalPoints() {
		return this.BattleRoyalPoints;
	}

	public void setBattleRoyalPoints(int points, boolean add) {
		if(add)
			this.BattleRoyalPoints += points;
		else
			this.BattleRoyalPoints = points;
	}
	
	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if(add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}
	
	public int getVotingPoints() {
		return votingPoints;
	}
	
	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}
	
	public void incrementVotingPoints() {
		this.votingPoints++;
	}
	
	public void decrementVotingPoints(int amt) {
		this.votingPoints -= amt;
	}
	
	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}
	
	public void setVotingPoints(int points, boolean add) {
		if(add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}
	
	public int getAchievementPoints() {
		return achievementPoints;
	}
	
	public void setAchievementPoints(int points, boolean add) {
		if(add)
			this.achievementPoints += points;
		else
			this.achievementPoints = points;
	}

	public void incrementBattleRoyalPoints(int points, boolean add) {
		if(add)
			this.BattleRoyalPoints += points;
		else
			this.BattleRoyalPoints = points;
	}
}
