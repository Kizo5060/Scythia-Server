package com.ruseps.world.content;

import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.model.PlayerRights;
import com.ruseps.util.Misc;

/*
 * @author Aj - SolaraRsPs rsps
 */
public class TriviaBot {
	
	public static final int TIMER = 1800; //1800
	public static int botTimer = TIMER;
	
	public static int answerCount;
	public static String firstPlace;
	public static String secondPlace;
	public static String thirdPlace;

	public static void sequence() {
		
		if(botTimer > 0)
			botTimer--;
		if(botTimer <= 0) {
			botTimer = TIMER;
			didSend = false;
			askQuestion();
		}
	}
	
	public static void attemptAnswer(Player p, String attempt) {
		
		if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {
			
			if (answerCount == 0) {
				answerCount++;
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(20);
				} else {
					p.getPointsHandler().incrementTriviaPoints(10);	
				}
				
				p.getPacketSender().sendMessage("You Recieved 10 trivia points for @red@1st Place.");
				p.getPointsHandler().refreshPanel();
				firstPlace = p.getUsername();
				return;
			}
			if (answerCount == 1) {
				if (p.getUsername() == firstPlace) {
					p.getPacketSender().sendMessage("Already answered");
					return;
				}
				answerCount++;
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(12);
				} else {
					p.getPointsHandler().incrementTriviaPoints(6);	
				}
				p.getPacketSender().sendMessage("You Recieved 6 trivia points for @red@2nd Place.");
				p.getPointsHandler().refreshPanel();
				secondPlace = p.getUsername();
				return;
			
			}
			if (answerCount == 2) {
				if (p.getUsername() == firstPlace || p.getUsername() == secondPlace) {
					p.getPacketSender().sendMessage("Already answered");
					return;
				}
				if(p.getRights() == PlayerRights.RUBY_MEMBER) {
					p.getPointsHandler().incrementTriviaPoints(8);
				} else {
					p.getPointsHandler().incrementTriviaPoints(4);	
				}
				p.getPacketSender().sendMessage("You Recieved 4 trivia points for @red@3rd Place.");
				p.getPointsHandler().refreshPanel();
				thirdPlace = p.getUsername();
				World.sendMessage("<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA] @bla@[1st:@blu@" +firstPlace+"@red@ (10 pts)@bla@] @bla@[2nd:@blu@" +secondPlace+"@red@ (6 pts)@bla@] [3rd:@blu@" +thirdPlace+"@red@  (4 pts)@bla@]");

				currentQuestion = "";
				didSend = false;
				botTimer = TIMER;
				answerCount = 0;
				return;
			}
			
			
		} else {
			if(attempt.contains("question") || attempt.contains("repeat")){
				p.getPacketSender().sendMessage("<col=800000>"+(currentQuestion));
				return;
			}
			p.getPacketSender().sendMessage("<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]Sorry! Wrong answer! "+(currentQuestion));
			return;
		}
		
	}
	
	public static boolean acceptingQuestion() {
		return !currentQuestion.equals("");
	}
	
	private static void askQuestion() {
		for (int i = 0; i < TRIVIA_DATA.length; i++) {
			if (Misc.getRandom(TRIVIA_DATA.length - 1) == i) {
				if(!didSend) {
					didSend = true;
				currentQuestion = TRIVIA_DATA[i][0];
				currentAnswer = TRIVIA_DATA[i][1];
				World.sendMessage(currentQuestion);
				
				
				}
			}
		}
	}
	
	public static boolean didSend = false;
	
	private static final String[][] TRIVIA_DATA = {
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many thieving stalls are there at the home area?", "5"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the name of the server?", "Solara"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What attack level do you need to wield an abyssal whip?", "70"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where is the home area located?", "Solara Castle"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What mining level do you need to mine shooting stars?", "80"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the bandos boss called?", "General graardor"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the name of the clan chat everyone is in?", "help"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What boss drops White Blaster?", "evil clown"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What npc drops Ultimate Bows?", "boony"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What npc drops Obsidian Sword?", "lava dragon"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What npc drops Lost Soul?", "blood king"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What boss drops staff of the wind?", "dark magician girl"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the level requirement to wear skillcapes?", "99"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the maximum combat level in Solara?", "209"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What defence level is required to wear barrows?", "70"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where can you get super sayian armour in Solara?", "anime raid"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where can you gamble in Solara?", "gamble"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is best in slot range weapon?", "Solaras shooter"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the most powerful Melee Weapon in the game?", "bone collector"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Who is the owner of Solara?", "Cutter", "Cutter"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where can you get dharoks armour?", "barrows"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What miniquest grants access to barrows gloves?", "recipe for disaster"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What combat level are Lava Dragons?", "999"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What combat level is Blood King?", "999"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What combat level is Beginner Digimon", "999"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What combat level are rock crabs?", "13"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What combat level is Evil Clown?", "580"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What boss drops Doomed Pieces?", "doomsday"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the best offensive range prayer in the normal prayer book?", "rigour"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the best offensive mage prayer in the normal prayer book?", "augury"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many skills are there in Solara", "25"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the best offensive range prayer in the normal prayer book?", "Rigour"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is your total level if you have 99 in every skill in SolaraRsPs?", "2475"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What trees do you cut for magic logs?", "Magic"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the highest level rock to mine", "runite"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where can you fight other players for their loot?", "wilderness"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the cape for complete players?", "completionist cape"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the cape for max players?", "max cape"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What skill makes potions?", "herblore"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What skill lets you make weapons and armour?", "smithing"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where can you store money other than the bank", "money pouch"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Where do you store all of your items?", "bank"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What points do you get for killing bosses?", "boss points"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many free slots does each bank tab have?", "600"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What skill advances your combat level past 200?", "summoning"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What food heals the most in SolaraRsPs", "rocktail"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What should I do every day to help the server?", "vote"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What skill do I use when crafting runes", "runecrafting"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many elite achievement tasks are there?", "8"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What boss drops Rippers Pieces?", "red digimon bird"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many dungeoneering tokens is an arcane stream necklace?", "75000"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many dungeoneering tokens are chaotics?", "200000"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the cube root of 216?", "6"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many time can you vote a day?", "2"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ How many auths can you get a day?", "10"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What slayer level does the master Sumona require?", "92"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What slayer level does the master Kuradel require?", "80"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What slayer level does the master Duradel require?", "50"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Who is the default slayer master?", "Vannaka"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What level herblore is required to make overloads?", "96"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What summoning level is required to make a Talon Beast?", "77"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What summoning level is required to make a Ravenous Locust?", "70"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What summoning level is required to make an Iron Minotaur?", "46"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What summoning level is required to make a Spirit Larupia?", "57"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What summoning level is required to make an Moss Titan?", "79"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What is the maximum amount of cash you can hold in your inventory?", "2147483647"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ At what level prayer can you use Hawk Eye?", "26"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ Which NPC will be able to give you a title?", "Sir Vyvin"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ What was Herblore's name originally called?", "Herblaw"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA]@red@ In what month was Solara released?", "June"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "1"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "2"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "3"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "4"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "5"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "6"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "7"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "8"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "9"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Guess a number 1-10?", "10"},
			
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ What smithing level is required to smith a Steel Plateskirt?", "46"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ What is the 40th spell on the regular spell book?", "vulnerability"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ What is the 17th emote?", "cry"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ What is the 24th emote?", "goblin salute"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ How many Runes are on the Magic thieving stall?", "3"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ How many Agility courses are there?", "3"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Where is ::home2 located?", "varrock"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ How much money can the Well of Goodwill hold?", "100000000"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Which NPC sells Skill Capes?", "wise old man"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ How much gold do you need to pay to get through the gate to Al Kharid?", "10"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Which skill shows an image of a Fist", "strength"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Which skill shows an image of a Wolf?", "summoning"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Which skill shows an image of a Ring?", "dungeoneering"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-GUESS]@red@ Which skill shows an image of Paw?", "hunter"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser jdj49a39ru357cnf", "jdj49a39ru357cnf"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser qpal29djeifh58cjid", "qpal29djeifh58cjid"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser qd85d4r0md42u2mssd", "qd85d4r0md42u2mssd"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser loski4893dhncbv7539", "loski4893dhncbv7539"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser 9esmf03na9admieutapdz9", "9esmf03na9admieutapdz9"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser djs83adm39s88s84masl", "djs83adm39s88s84masl"},
			{"<img=2><col=1aa3ff><shad=1aa3ff>[TRIVIA-TYPE]@red@ type the following ::anwser alskpwru39020dmsa3aeamap", "alskpwru39020dmsa3aeamap"}
		};
	
	public static String currentQuestion;
	private static String currentAnswer;
}