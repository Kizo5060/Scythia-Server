package com.ruseps.world.content;

import java.util.Random;

import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class TriviaBot {

	enum TriviaData {
		QUESION_1("what is the max skill level", "150"),
		QUESION_2("Re-arrange these letters to make a npc.  'stvaneneR'", "Revenants"), 
		QUESION_3("Re-arrange these letters to make a city.  'rorckVa'", "Varrock"), 
		QUESION_4("What drop rate bonus does godzilla armour give individually", "4%"),
		QUESION_6("How many vote points does $10 bond cost from vote store", "200"), 
		QUESION_7("How many easy achievements are on Scythia", "18"),
		QUESION_8("Re-arrange these letters to make a place.  'tnDoar ozne'", "Donator zone"), 
		QUESION_9("Which osrs barrows brother hits through prayer?", "Verac"), 
		QUESION_10("How many blood bags are required to unlock blood slayer?", "10k"), 
		QUESION_11("How many taxbags does it cost to forge a supreme herbal bow?", "250k"), 
		QUESION_12("Who can you purchase a max cape from?", "Sir Percival"), 
		QUESION_13("what is the name of the final boss in Yu-Gi-Oh raids?", "Yugi Moto"), 
		QUESION_14("What is the lightest chemical element", "Hydrogen"), 
		QUESION_15("Which planet is nearest the sun", "Mercury"), 
		QUESION_16( " Which river flows through London?", "The Thames" ),
		QUESION_17("  Which Italian city is famous for its leaning tower?", "Pisa"),
		QUESION_18("  Pls say Scythia", "Scythia"),
		QUESION_19("  Pls say Scythia backwards", "aihtycS"),
		QUESION_20("  eBn", "Ben"),
		QUESION_21(" ioMl", "Scythia"),
		QUESION_22("  esnRi rnSie", "Risen Siren"),
		QUESION_23("   What is the Aloha State?", "Hawaii"),
		QUESION_24("  What weapon do Jedi Knights use?", "Light saber"),
		QUESION_25("  Black-eyed peas are not peas, what are they?", "Beans"), 
		QUESION_26(" Say noob", "noob"),
		QUESION_27(" What is the most widley eaten fish in the world?", "The Herring"), 
		QUESION_28(" An interferometer is used to measure what?", "The wavelength of light"),
		QUESION_29("  What does TRIVIA literally mean?", "Three Roads"),
		QUESION_30("  What is Carambola?", "Starfruit"),
		QUESION_33("  Thalassophobia is a fear of what?", "The Sea"),
		QUESION_34("  If you freeze water, what do you get?", "Ice"),
		QUESION_35("  What colors are the stars on the American flag?",
				"White"),
		QUESION_36("  How many planets are in our solar system?", "Eight"),
		QUESION_37("  What city is the most populated city on earth?", "Tokyo"),
		QUESION_38("  Which state is famous for Hollywood?", "California"),
		QUESION_39("  How many pairs of wings do bees have?", "Two"),
		QUESION_40("  How many days are in a year?", "365"),
		QUESION_41("  What was the first holiday event item ever on osrs? ", "Pumpkin"),
		QUESION_42("  what year did Nintendo release its first game console in NA? ?", "1985"),
		QUESION_43("  Name the biggest Island of the world", "Greenland"),
		QUESION_44("  What is the largest continent of the world?", "Asia"),
		QUESION_45("  What is the color of a 10M money stack?", "Green"),
		QUESION_46("  Name the largest ocean of the world", "Pacific Ocean"),
		QUESION_47("  What year was Microsoft Established?", "1975"),
		QUESION_48("  How many 	Countries are there in the United Kingdom?", "4"),
		QUESION_49("  How many years are there in a century?", "100"),
		QUESION_50("  What is the Capital of Italy?", "Rome"),
		QUESION_51("  How many sides does a Hexagon have?", "6"),
		QUESION_52("  How many elements are in the periodic table?", "118"),
		QUESION_53("  Which country invented pizza?", "Italy"),
		QUESION_54("  What year was Bitcoin launched?", "2009"),
		QUESION_55("  How much water is present in a watermelon?", "92%"),
		QUESION_56("  Which Ocean is the Deepest? ", "Pacific Ocean"),
		QUESION_57("  What year did we discover the Element Oxygen?", "1774"),
		QUESION_58(" How much Constitution does a Pumpkin heal?", "140"),
		QUESION_59(" How many Barrows Brothers are there?", "6"),
		QUESION_60(" What color of logs do you get if you use a blue firelighter on a log?", "Blue"),
		QUESION_61(" What level must you be to smith a Steel Scimitar?", "35"),
		QUESION_62(" Which area of the game is notorious for players dying within it, then complain about?",
				"Wilderness"),
		QUESION_63(" What level do you need to be to farm watermelon?", "47"),
		QUESION_64(" At what level can you purchase a skill cape at?", "99"),
		QUESION_65(" ]Of these, which requires the highest level to catch? Lobster, Herring, Salmon, Swordfish",
				"Swordfish"),
		QUESION_66(" Who is the last boss in Recipe for Disaster?", "Culinaromancer"),
		QUESION_67(" Which skill does ranging go with most?", "Fletching"),
		QUESION_68(" How many bars do you need to make a platebody?", "5" ),
		QUESION_69("Unscramble these letters 'pgoru minrona'", "group ironman"), 
		QUESION_70("Unscramble these letters 'eomh peletrot'", "home teleport"), 
		QUESION_71("Type the following : '21042919422'", "21042919422"), 
		QUESION_72("Type the following : 'ximf2mc292m92'", "ximf2mc292m92"), 
		QUESION_73("Type the following : '0k0k02k02kd2d'", "0k0k02k02kd2d"), 
		QUESION_74("Type the following : 'v2img903m'", "v2img903m"), 
		QUESION_75("Type the following : 'omv039f290k'", "omv039f290k"), 
		QUESION_76("Type the following : 'foem30ffo3mfo2'", "foem30ffo3mfo2"), 
		QUESION_77("Type the following : 'zomofo2mf20mf'", "zomofo2mf20mf"), 
		QUESION_78("Type the following : 'pp20d20d2l02'", "pp20d20d2l02"), 
		QUESION_79("Type the following : 'ao10fm30mg02'", "ao10fm30mg02"), 
		QUESION_80("Type the following : 'omv2mf029fm290f2'", "omv2mf029fm290f2"), 
		QUESION_81("What is 5 x 5?", "25"), 
		
		QUESION_82("Guess a number 1-10", "1"), 
		QUESION_83("Guess a number 1-10", "2"), 
		QUESION_84("Guess a number 1-10", "3"), 
		QUESION_85("Guess a number 1-10", "4"), 
		QUESION_86("Guess a number 1-10", "5"), 
		QUESION_87("Guess a number 1-10", "6"), 
		QUESION_88("Guess a number 1-10", "7"), 
		QUESION_89("Guess a number 1-10", "8"), 
		QUESION_90("Guess a number 1-10", "9"), 
		QUESION_91("Guess a number 1-10", "10");																	// are
																														// good
																														// enough
																														// for
																														// testing

		TriviaData(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}

		private String question, answer;

		public String getQuestion() {
			return question;
		}

		public String getAnswer() {
			return answer;
		}
	}

	private static int timer = 1500; // 15 minutes
	public static boolean active = false;
	private static TriviaData currentQuestion = null;
	
	public static String getCurrentQuestion() {
		return currentQuestion == null ? "None" : currentQuestion.getQuestion().toUpperCase().substring(0, 1) + currentQuestion.getQuestion().toLowerCase().substring(1);
	}

	public static void tick() {

		if (!active) {
			if (timer < 1) {
				startTrivia();
				timer = 2000;
			} else {
				timer--;
			}
		}
	}

	private static final TriviaData[] TRIVIA_DATA = TriviaData.values();

	private static void startTrivia() {
		setAndAskQuestion();
	}

	private static void setAndAskQuestion() {
		active = true;
		currentQuestion = TRIVIA_DATA[new Random().nextInt(TRIVIA_DATA.length)];
		World.sendMessage("<shad=1>@red@[TRIVIA] @red@" + currentQuestion.getQuestion() + "");
		World.getPlayers().forEach(PlayerPanel::refreshPanel);
	}
	
	public static void answer(Player player, String answer) {
		if(!active) {
			player.sendMessage("<shad=1>@red@There is no trivia going on at the moment");
			return;
		}
		if(answer.equalsIgnoreCase(currentQuestion.getAnswer())) {
			player.getInventory().add(621, 1);
			player.getPointsHandler().getTriviaPoints();
			active = false;
			World.sendMessage("<shad=1>@red@[TRIVIA]: <shad=1>@blu@" + player.getUsername() + "<shad=1>@bla@ has recieved @red@10 points and $1 bond @bla@from Trivia");
			World.sendMessage("<shad=1>@red@[TRIVIA]: <shad=1>@bla@ The answer for the trivia to the question was @red@" + currentQuestion.answer);
			currentQuestion = null;
			World.getPlayers().forEach(PlayerPanel::refreshPanel);
			player.sendMessage("<shad=1>@bla@congrats, you've guessed correctly and received a@blu@$1 bond and 10 Trivia poins@bla@!");
			
		} else {
			player.sendMessage("<shad=1>@bla@Incorrect answer - your answer was: <shad=1>@red@" + answer +", " + "the question was");
			player.sendMessage("<shad=1>@red@[TRIVIA]: @red@" + currentQuestion.getQuestion() + "");
		}
	}

}