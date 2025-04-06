package com.ruseps.model;

import com.ruseps.model.container.impl.Equipment;
import com.ruseps.GameSettings;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.instances.Cerberus;
import com.ruseps.world.content.instances.KingBlackDragon;
import com.ruseps.world.content.PlayerPunishments.Jail;
import com.ruseps.world.content.battle_royale.BattleRoyale;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.content.combat.pvp.BountyHunter;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.droppreview.AVATAR;
import com.ruseps.world.content.droppreview.BARRELS;
import com.ruseps.world.content.droppreview.BORKS;
import com.ruseps.world.content.droppreview.CORP;
import com.ruseps.world.content.droppreview.DAGS;
import com.ruseps.world.content.droppreview.GLAC;
import com.ruseps.world.content.droppreview.GWD;
import com.ruseps.world.content.droppreview.KALPH;
import com.ruseps.world.content.droppreview.LIZARD;
import com.ruseps.world.content.droppreview.NEXX;
import com.ruseps.world.content.droppreview.PHEON;
import com.ruseps.world.content.droppreview.SKOT;
import com.ruseps.world.content.droppreview.SLASHBASH;
import com.ruseps.world.content.droppreview.TDS;
import com.ruseps.world.content.minigames.custom_minigames.Defenderz;
import com.ruseps.world.content.minigames.custom_minigames.Weaponz;
import com.ruseps.world.content.minigames.custom_minigames.Magicz;
import com.ruseps.world.content.minigames.custom_minigames.Rangez;
import com.ruseps.world.content.minigames.impl.Barrows;
import com.ruseps.world.content.minigames.impl.FightCave;
import com.ruseps.world.content.minigames.impl.FightPit;
import com.ruseps.world.content.minigames.impl.Graveyard;
import com.ruseps.world.content.minigames.impl.Nomad;
import com.ruseps.world.content.minigames.impl.PestControl;
import com.ruseps.world.content.minigames.impl.RaichuInstance;
import com.ruseps.world.content.minigames.impl.WarriorsGuild;
import com.ruseps.world.content.new_raids_system.instances.*;
import com.ruseps.world.content.new_raids_system.raids_party.RaidsParty;
import com.ruseps.world.content.minigames.impl.RecipeForDisaster;
import com.ruseps.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.ruseps.world.entity.Entity;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class Locations {

	public static void login(Player player) {
		player.setLocation(Location.getLocation(player));
		player.getLocation().login(player);
		player.getLocation().enter (player);
	}

	
	public static void logout(Player player) {
		player.getLocation().logout(player);
		if(player.getRegionInstance() != null)
			player.getRegionInstance().destruct();
		if(player.getLocation() != Location.GODWARS_DUNGEON) {
			player.getLocation().leave(player);
		}
	}
	
	public static int PLAYERS_IN_WILD;
	public static int PLAYERS_IN_DUEL_ARENA;
	
	 
	public enum Location {
		 RAIDS_DS_ENTRANCE(new int[]  {2665, 2675}, new int[]{2770, 2798}, false, false, true, false, true, true) {
           	 @Override
                public void process(Player player) {
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                        player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                }

                @Override
                public void login(Player player) {
                		player.getRaidsOne().getRaidsConnector().enter(player);
                }
                
                @Override
                public void leave(Player player) {
                		player.getRaidsOne().getRaidsConnector().leaveRaidsDSEntrance();
                }
                
                @Override
                public boolean canTeleport(Player player) {
                		player.getRaidsOne().getRaidsConnector().leaveRaidsDSEntrance();
                		return true;
                		
                }
                
                @Override
                public void enter(Player player) {
                	if(player.getAmountDonated() < 3500) {
                		player.performAnimation(new Animation(866));
                		player.performGraphic(new Graphic(2009));
                		player.moveTo(GameSettings.DEFAULT_POSITION);
                		player.sendMessage("<img=10>@red@Sorry " + player.getUsername() + " you require at least 3500 total donated.");
                		return;
                	}
                    if (player.getSummoning().getFamiliar() != null) {
                        player.getSummoning().unsummon(true, true);
                        player.getPacketSender().sendMessage("You've dismissed your familiar.");
                    }
             
                    int id = 58016;
                    for (int i = 58016; i < 58064; i++) {
                        id++;
                        player.getPacketSender().sendString(id++, "");
                        player.getPacketSender().sendString(id++, "");
                        player.getPacketSender().sendString(id++, "");
                    }
                    player.getPacketSender().sendString(58009, "Create");
                    player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                	
                }
              
           }, RAIDS_DS_PHASE_ONE(new int[]{2637, 2664}, new int[]{2770, 2798}, true, false, true, false, false, true) {
               @Override
               public void process(Player player) {
            	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                   
                   final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
           				for(Player partyMember : party.getPlayers())  
           				{
           						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
           						{
           							DragonStoneRaids.exitRaidsDS(player);
           							return;
           						}
           					
           				}
               		
               }

               @Override
               public void login(Player player) {
            	      player.getRaidsOne().getRaidsConnector().leaveRaidsDS();
            	   	  player.moveTo(GameSettings.RAIDS_DS_LOBBY.copy());
            	   	  player.getRaidsOne().getRaidsConnector().enter(player);
            	   	  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
            	   
               }
               @Override
               public void logout(Player player) {
            	    	player.getRaidsOne().getRaidsConnector().leaveRaidsDS();
               	 	player.getRaidsOne().getRaidsConnector().leave(true);
               		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
            	   
               }
               
               @Override
               public void onDeath(Player player) {
            	    	player.getRaidsOne().getRaidsConnector().leaveRaidsDS();
                 	player.getRaidsOne().getRaidsConnector().leave(true);
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                     player.moveTo(GameSettings.RAIDS_DS_LOBBY.copy());
                 	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
            	   
               }
           

               @Override
               public boolean canTeleport(Player player) {
                   	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                   	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                      partyy.remove(player, false, true);
                   	 return true;
               }

               @Override
               public void enter(Player player) {
            		   if (player.getSummoning().getFamiliar() != null) {
                       	player.getSummoning().unsummon(true, true);
                       	player.getPacketSender().sendMessage("You've dismissed your familiar.");
                   		 }  
	               
	           }
           },
         
		CASH_ZONE(new int[]  {2304, 2367}, new int[]{3584, 3647}, true, true, true, true, true, true) {
		    @Override
	        public void logout(Player player) {
		    	if(player.getRegionInstance() != null)
	        		player.getRegionInstance().destruct();
	             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CASH_ZONE, player.getPosition().getZ()));    	 
	        }
            @Override
            public boolean canTeleport(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CASH_ZONE, player.getPosition().getZ()));
                return true;
            }
            @Override
             public void onDeath(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CASH_ZONE, player.getPosition().getZ()));
             }
		},
		INSTANCE_MANAGER_ZONE(new int[]  {2832, 2863}, new int[] {5068, 5107}, true, true, true, true, true, true) {},
		
		INSTANCE_ZONE(new int[]  {3406, 3445}, new int[]{4766, 4787}, true, true, true, true, true, true) {
		    @Override
	        public void logout(Player player) {
		    	if(player.getRegionInstance() != null)
	        		player.getRegionInstance().destruct();
	             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.INSTANCE_ZONE, player.getPosition().getZ()));    	 
	             	
	             	player.moveTo(GameSettings.DEFAULT_POSITION);
	        }
            @Override
            public boolean canTeleport(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.INSTANCE_ZONE, player.getPosition().getZ()));
                return true;
            }
		},
		AOE_INSTANCE_ZONE(new int[]  {3406, 3444}, new int[]{4766, 4791}, true, true, true, true, true, true) {
		    @Override
	        public void logout(Player player) {
		    	if(player.getRegionInstance() != null)
	        		player.getRegionInstance().destruct();
	             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.AOE_INSTANCE_ZONE, player.getPosition().getZ()));    	 
	        }
            @Override
            public boolean canTeleport(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.AOE_INSTANCE_ZONE, player.getPosition().getZ()));
                return true;
            }
            @Override
             public void onDeath(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.AOE_INSTANCE_ZONE, player.getPosition().getZ()));
             }
		},
		BUNNY(new int[]  {2605, 2610}, new int[]{9667, 9675}, false, false, true, false, true, true) {
	            @Override
	            public boolean canTeleport(Player player) {
	            	if(player.getRegionInstance() != null)
	        			player.getRegionInstance().destruct();
	             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.BUNNY, player.getPosition().getZ()));
	                return true;
	            }
	            @Override
	             public void onDeath(Player player) {
	            	if(player.getRegionInstance() != null)
	        			player.getRegionInstance().destruct();
	             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.BUNNY, player.getPosition().getZ()));
	             }
	    },
		CLOWN(new int[]  {3011, 3028}, new int[]{4394, 4410}, false, false, true, false, true, true) {
            @Override
            public boolean canTeleport(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CLOWN, player.getPosition().getZ()));
                return true;
            }
            @Override
             public void onDeath(Player player) {
            	if(player.getRegionInstance() != null)
        			player.getRegionInstance().destruct();
             	World.getNpcs().forEach(n -> n.removeInstancedNpcs(Location.CLOWN, player.getPosition().getZ()));
             }
    },
		RAIDS_ONE_ENTRANCE(new int[]  {2289, 2315}, new int[]{3318, 3347}, false, false, true, false, true, true) {
       	 @Override
            public void process(Player player) {
                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                    player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
            }

            @Override
            public void login(Player player) {
           	 player.getRaidsOne().getRaidsConnector().enter(player);
           	 
            }
            
            @Override
            public void leave(Player player) {
           	 player.getRaidsOne().getRaidsConnector().leaveRaidsOneEntrance();
            }
            
            @Override
            public boolean canTeleport(Player player) {
           	 player.getRaidsOne().getRaidsConnector().leaveRaidsOneEntrance();
                return true;
            }
            
            @Override
            public void enter(Player player) {
                if (player.getSummoning().getFamiliar() != null) {
                    player.getSummoning().unsummon(true, true);
                    player.getPacketSender().sendMessage("You've dismissed your familiar.");
                }

                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                        .sendTab(GameSettings.ACHIEVEMENT_TAB);

                int id = 58016;
                for (int i = 58016; i < 58064; i++) {
                    id++;
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                }
                
                player.getPacketSender().sendString(58009, "Create");
                player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
            }
       },
		
		RAIDS_TWO_ENTRANCE(new int[]  {3029, 3066}, new int[]{2833, 2855}, false, false, true, false, true, true) {
         	 @Override
              public void process(Player player) { 
                  if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                      player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
              }

              @Override
              public void login(Player player) {
             	 player.getRaidsOne().getRaidsConnector().enter(player);
             	 
              }
              @Override
              public void leave(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsTwoEntrance();
              }
              @Override
              public boolean canTeleport(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsTwoEntrance();
                  return true;
              }
              @Override
              public void enter(Player player) {
                  if (player.getSummoning().getFamiliar() != null) {
                      player.getSummoning().unsummon(true, true);
                      player.getPacketSender().sendMessage("You've dismissed your familiar.");
                  }

                  player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                          .sendTab(GameSettings.ACHIEVEMENT_TAB);

                  int id = 58016;
                  for (int i = 58016; i < 58064; i++) {
                      id++;
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                  }
                  player.getPacketSender().sendString(58009, "Create");
                  player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                  
              }
          
         },
		RAIDS_FOUR_ENTRANCE(new int[]  {2837, 2855}, new int[]{3096, 3113}, false, false, true, false, true, true) {
         	 @Override
              public void process(Player player) { 
                  if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                      player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
              }

              @Override
              public void login(Player player) {
             	 player.getRaidsOne().getRaidsConnector().enter(player);
             	 
              }
              @Override
              public void leave(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsFourEntrance();
              }
              @Override
              public boolean canTeleport(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsFourEntrance();
                  return true;
              }
              @Override
              public void enter(Player player) {
                  if (player.getSummoning().getFamiliar() != null) {
                      player.getSummoning().unsummon(true, true);
                      player.getPacketSender().sendMessage("You've dismissed your familiar.");
                  }

                  player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                          .sendTab(GameSettings.ACHIEVEMENT_TAB);

                  int id = 58016;
                  for (int i = 58016; i < 58064; i++) {
                      id++;
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                  }
                  player.getPacketSender().sendString(58009, "Create");
                  player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                  
              }
          
         },
		RAIDS_FIVE_ENTRANCE(new int[]  {2898, 2923}, new int[]{2509, 2528}, false, false, true, false, true, true) {
         	 @Override
              public void process(Player player) { 
                  if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                      player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
              }

              @Override
              public void login(Player player) {
             	 player.getRaidsOne().getRaidsConnector().enter(player);
             	 
              }
              @Override
              public void leave(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsFiveEntrance();
              }
              @Override
              public boolean canTeleport(Player player) {
             	 player.getRaidsOne().getRaidsConnector().leaveRaidsFiveEntrance();
                  return true;
              }
              @Override
              public void enter(Player player) {
                  if (player.getSummoning().getFamiliar() != null) {
                      player.getSummoning().unsummon(true, true);
                      player.getPacketSender().sendMessage("You've dismissed your familiar.");
                  }

                  player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                          .sendTab(GameSettings.ACHIEVEMENT_TAB);

                  int id = 58016;
                  for (int i = 58016; i < 58064; i++) {
                      id++;
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                      player.getPacketSender().sendString(id++, "");
                  }
                  player.getPacketSender().sendString(58009, "Create");
                  player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                  
              }
          
         },
		RAIDS_SIX_ENTRANCE(new int[]  {3087, 3103}, new int[]{3930, 3945}, false, false, true, false, true, true) {
           	 @Override
                public void process(Player player) {
                    if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                        player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                }

                @Override
                public void login(Player player) {
               	 player.getRaidsOne().getRaidsConnector().enter(player);
               	 
                }
                @Override
                public void leave(Player player) {
               	 player.getRaidsOne().getRaidsConnector().leaveRaidsSixEntrance();
                }
                @Override
                public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().leaveRaidsSixEntrance();
                    return true;
                }
                @Override
                public void enter(Player player) {
                	if(player.getAmountDonated() < 500) {
                		player.performAnimation(new Animation(866));
                		player.performGraphic(new Graphic(2009));
                		player.moveTo(GameSettings.DEFAULT_POSITION);
                		player.sendMessage("<img=10>@red@Sorry " + player.getUsername() + " you require at least 500 total donated.");
                		return;
                	}
                    if (player.getSummoning().getFamiliar() != null) {
                        player.getSummoning().unsummon(true, true);
                        player.getPacketSender().sendMessage("You've dismissed your familiar.");
                    }

                    player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                            .sendTab(GameSettings.ACHIEVEMENT_TAB);

                    int id = 58016;
                    for (int i = 58016; i < 58064; i++) {
                        id++;
                        player.getPacketSender().sendString(id++, "");
                        player.getPacketSender().sendString(id++, "");
                        player.getPacketSender().sendString(id++, "");
                    }
                    player.getPacketSender().sendString(58009, "Create");
                    player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                    
                }
            
           },
        RAIDS_SEVEN_ENTRANCE(new int[]  {1812, 1829}, new int[]{3094, 3111}, false, false, true, false, true, true) {
            @Override
            public void process(Player player) {
                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                    player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
            }

            @Override
            public void login(Player player) {
                player.getRaidsOne().getRaidsConnector().enter(player);

            }
            @Override
            public void leave(Player player) {
                player.getRaidsOne().getRaidsConnector().leaveRaidsOneEntrance();
            }
            @Override
            public boolean canTeleport(Player player) {
                player.getRaidsOne().getRaidsConnector().leaveRaidsSevenEntrance();
                return true;
            }
            @Override
            public void enter(Player player) {
                if (player.getSummoning().getFamiliar() != null) {
                    player.getSummoning().unsummon(true, true);
                    player.getPacketSender().sendMessage("You've dismissed your familiar.");
                }

                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                        .sendTab(GameSettings.ACHIEVEMENT_TAB);

                int id = 58016;
                for (int i = 58016; i < 58064; i++) {
                    id++;
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                }
                player.getPacketSender().sendString(58009, "Create");
                player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");

            }

        },
        RAIDS_EIGHT_ENTRANCE(new int[]  {3276, 3397}, new int[]{3985, 4012}, false, false, true, false, true, true) {
          	 @Override
               public void process(Player player) {
                   if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                       player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               }

               @Override
               public void login(Player player) {
              	 player.getRaidsOne().getRaidsConnector().enter(player);
               }
               
               @Override
               public void leave(Player player) {
              	 player.getRaidsOne().getRaidsConnector().leaveRaidsEightEntrance();
               }
               
               @Override
               public boolean canTeleport(Player player) {
              	 player.getRaidsOne().getRaidsConnector().leaveRaidsEightEntrance();
                   return true;
               }
               
               @Override
               public void enter(Player player) {
               	if(player.getAmountDonated() < 1700) {
               		player.performAnimation(new Animation(866));
               		player.performGraphic(new Graphic(2009));
               		player.moveTo(GameSettings.DEFAULT_POSITION);
               		player.sendMessage("<img=10>@red@Sorry " + player.getUsername() + " you require at least 1700 total donated.");
               		return;
               	}
                   if (player.getSummoning().getFamiliar() != null) {
                       player.getSummoning().unsummon(true, true);
                       player.getPacketSender().sendMessage("You've dismissed your familiar.");
                   }

                   player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                           .sendTab(GameSettings.ACHIEVEMENT_TAB);

                   int id = 58016;
                   for (int i = 58016; i < 58064; i++) {
                       id++;
                       player.getPacketSender().sendString(id++, "");
                       player.getPacketSender().sendString(id++, "");
                       player.getPacketSender().sendString(id++, "");
                   }
                   player.getPacketSender().sendString(58009, "Create");
                   player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                   
               }
           
          },
        
        
        RAIDS_DD_ENTRANCE(new int[]  {3357, 3364}, new int[]{3237, 3246}, false, false, true, false, true, true) {
            	 @Override
                 public void process(Player player) {
                     if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                         player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 }

                 @Override
                 public void login(Player player) {
                	 player.getRaidsOne().getRaidsConnector().enter(player);
                	 
                 }
                 
                 @Override
                 public void leave(Player player) {
                	 player.getRaidsOne().getRaidsConnector().leaveRaidsOneEntrance();
                 }
                 
                 @Override
                 public boolean canTeleport(Player player) {
                	 player.getRaidsOne().getRaidsConnector().leaveRaidsOneEntrance();
                     return true;
                 }
                 
                 @Override
                 public void enter(Player player) {
                     if (player.getSummoning().getFamiliar() != null) {
                         player.getSummoning().unsummon(true, true);
                         player.getPacketSender().sendMessage("You've dismissed your familiar.");
                     }


                     int id = 58016;
                     for (int i = 58016; i < 58064; i++) {
                         id++;
                         player.getPacketSender().sendString(id++, "");
                         player.getPacketSender().sendString(id++, "");
                         player.getPacketSender().sendString(id++, "");
                     }
                     
                     player.getPacketSender().sendString(58009, "Create");
                     player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                 }
            },	RAIDS_DD_PHASE_ONE(new int[]{3363, 3388}, new int[]{3225, 3239}, true, false, true, false, false, true) {
                @Override
                public void process(Player player) {
             	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                    
                    final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
            				for(Player partyMember : party.getPlayers())  
            				{
            					
            						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
            						{
            							DarkDementionRaids.sendPhaseTwo(party);
            							return;
            						}
            					
            					
            				}
                		}

                @Override
                public void login(Player player) { 
             	   player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	 player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                    player.getRaidsOne().getRaidsConnector().enter(player);
                    player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
                }

                @Override
                public void logout(Player player) {
             	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                	 	player.getRaidsOne().getRaidsConnector().leave(true);
                		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                }
                
                @Override
                public void onDeath(Player player) {
             	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                  	player.getRaidsOne().getRaidsConnector().leave(true);
                  	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                     player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                  	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
                }
            

                @Override
                public boolean canTeleport(Player player) {
                    	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                    	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                    	 return true;
                }

                @Override
                public void enter(Player player) {
             	   if (player.getSummoning().getFamiliar() != null) {
                        player.getSummoning().unsummon(true, true);
                        player.getPacketSender().sendMessage("You've dismissed your familiar.");
                    }

                   
                }

            },RAIDS_DD_PHASE_TWO(new int[]{3333, 3358}, new int[]{3225, 3239}, true, false, true, false, false, true) {

           	   @Override
                  public void process(Player player) {
               	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                      
                      final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
              				for(Player partyMember : party.getPlayers())  
              				{
              					
              						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 2) 
              						{
              							DarkDementionRaids.sendPhaseThree(party);
              							return;
              						
              					}
              					
              				}
                  		}

                  @Override
                  public void login(Player player) { 
               	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
               	  player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                       player.getRaidsOne().getRaidsConnector().enter(player);
                       player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
                  }

                  @Override
                  public void logout(Player player) {
               	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                  	 	player.getRaidsOne().getRaidsConnector().leave(true);
                  		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                       partyy.remove(player, false, true);
                  }
                  
                  @Override
                  public void onDeath(Player player) {
               	     player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                    	 player.getRaidsOne().getRaidsConnector().leave(true);
                    	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                        partyy.remove(player, false, true);
                        player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                    	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
                  }
              

                  @Override
                  public boolean canTeleport(Player player) {
                      	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                      	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                        partyy.remove(player, false, true);
                      	 return true;
                  }

                  @Override
                  public void enter(Player player) {
               	   if (player.getSummoning().getFamiliar() != null) {
                          player.getSummoning().unsummon(true, true);
                          player.getPacketSender().sendMessage("You've dismissed your familiar.");
                      }

                     
                  }
              },
              RAIDS_DD_PHASE_THREE(new int[]{3333, 3358}, new int[]{3206, 3220}, true, false, true, false, false, true) {

           	   @Override
                  public void process(Player player) {
               	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                      
                      final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
              				for(Player partyMember : party.getPlayers())  
              				{
              						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 3) 
              						{
              							DarkDementionRaids.exitRaidsDD(player);
              							return;
              						}
              					
              				}
                  		}

                  @Override
                  public void login(Player player) { 
               	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
               	  player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                       player.getRaidsOne().getRaidsConnector().enter(player);
                       player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
                  }

                  @Override
                  public void logout(Player player) {
               	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                  	 	player.getRaidsOne().getRaidsConnector().leave(true);
                  		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                       partyy.remove(player, false, true);
                  }
                  
                  @Override
                  public void onDeath(Player player) {
               	     player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
                    	 player.getRaidsOne().getRaidsConnector().leave(true);
                    	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                        partyy.remove(player, false, true);
                        player.moveTo(GameSettings.RAIDS_DD_LOBBY.copy());
                    	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
                  }
              

                  @Override
                  public boolean canTeleport(Player player) {
                      	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                      	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                       partyy.remove(player, false, true);
                      	 return true;
                  }

                  @Override
                  public void enter(Player player) {
               	   if (player.getSummoning().getFamiliar() != null) {
                          player.getSummoning().unsummon(true, true);
                          player.getPacketSender().sendMessage("You've dismissed your familiar.");
                      }

                     
                  }

              },
        
        
        // x1, x2, y1, y2 this draws a square start bottom left corner, ends top right
        RAIDS_EIGHT_PHASE_ONE(new int[]{3019, 3060}, new int[]{3026, 3053}, true, false, true, false, false, true) {
              @Override
              public void process(Player player) {
           	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                  
                  final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
          				for(Player partyMember : party.getPlayers())  
          				{
          						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
          						{
          							RubyRaid.exitRaidsEight(player);
          							return;
          						}
          					
          				}
              		}

              @Override
              public void login(Player player) { 
           	      player.getRaidsOne().getRaidsConnector().leaveRaidsEight();
           	   	  player.moveTo(new Position(3285, 4000));
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
              }

              @Override
              public void logout(Player player) {
           	    	player.getRaidsOne().getRaidsConnector().leaveRaidsEight();
              	 	player.getRaidsOne().getRaidsConnector().leave(true);
              		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
              }
              
              @Override
              public void onDeath(Player player) {
           	    	player.getRaidsOne().getRaidsConnector().leaveRaidsEight();
                	player.getRaidsOne().getRaidsConnector().leave(true);
                	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
                    player.moveTo(new Position(3285, 4000));
                	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
              }
          

              @Override
              public boolean canTeleport(Player player) {
                  	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                  	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                  	 return true;
              }

              @Override
              public void enter(Player player) {
           	   if (player.getSummoning().getFamiliar() != null) {
                      player.getSummoning().unsummon(true, true);
                      player.getPacketSender().sendMessage("You've dismissed your familiar.");
                  }  
              }
          },

        
		RAIDS_SIX_PHASE_ONE(new int[]{1746, 1768}, new int[]{3215, 3239}, true, false, true, false, false, true) {
               @Override
               public void process(Player player) {
            	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                   
                   final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
           				for(Player partyMember : party.getPlayers())  
           				{
           					
           						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
           						{
           							PlatRaids.exitRaidsSix(player);
           							return;
           						}
           					
           					
           				}
               		}

               @Override
               public void login(Player player) { 
            	   player.getRaidsOne().getRaidsConnector().leaveRaidsSix();
            	 player.moveTo(GameSettings.RAIDS_SIX_LOBBY.copy());
                   player.getRaidsOne().getRaidsConnector().enter(player);
                   player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
               }

               @Override
               public void logout(Player player) {
            	    player.getRaidsOne().getRaidsConnector().leaveRaidsSix();
               	 	player.getRaidsOne().getRaidsConnector().leave(true);
               		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
               }
               
               @Override
               public void onDeath(Player player) {
            	    player.getRaidsOne().getRaidsConnector().leaveRaidsSix();
                 	player.getRaidsOne().getRaidsConnector().leave(true);
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
                    player.moveTo(GameSettings.RAIDS_SIX_LOBBY.copy());
                 	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
               }
           

               @Override
               public boolean canTeleport(Player player) {
                   	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                   	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
                   	 return true;
               }

               @Override
               public void enter(Player player) {
            	   if (player.getSummoning().getFamiliar() != null) {
                       player.getSummoning().unsummon(true, true);
                       player.getPacketSender().sendMessage("You've dismissed your familiar.");
                   }

                  
               }

           },
        RAIDS_SEVEN_PHASE_ONE(new int[]{2756, 2811}, new int[]{2948, 3003}, true, false, true, false, false, true) {
            @Override
            public void process(Player player) {
                player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();

                final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
                for(Player partyMember : party.getPlayers())
                {

                    if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1)
                    {
                        DiamondRaids.exitRaidsSeven(player);
                        return;
                    }


                }
            }

            @Override
            public void login(Player player) {
                player.getRaidsOne().getRaidsConnector().leaveRaidsSeven();
                player.moveTo(GameSettings.RAIDS_SEVEN_LOBBY.copy());
                player.getRaidsOne().getRaidsConnector().enter(player);
                player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
            }

            @Override
            public void logout(Player player) {
                player.getRaidsOne().getRaidsConnector().leaveRaidsSeven();
                player.getRaidsOne().getRaidsConnector().leave(true);
                RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
            }

            @Override
            public void onDeath(Player player) {
                player.getRaidsOne().getRaidsConnector().leaveRaidsSeven();
                player.getRaidsOne().getRaidsConnector().leave(true);
                RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
                player.moveTo(GameSettings.RAIDS_SEVEN_LOBBY.copy());
                player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
            }


            @Override
            public boolean canTeleport(Player player) {
                player.getRaidsOne().getRaidsConnector().attemptLeave();
                RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
                return true;
            }

            @Override
            public void enter(Player player) {
                if (player.getSummoning().getFamiliar() != null) {
                    player.getSummoning().unsummon(true, true);
                    player.getPacketSender().sendMessage("You've dismissed your familiar.");
                }


            }

        },
		RAIDS_FIVE_PHASE_ONE(new int[]{3185, 3198}, new int[]{3777, 3791}, true, false, true, false, false, true) {
             @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
         						{
         							GoldRaids.sendPhaseTwo(party);
         							return;
         						}
         					
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	   player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
          	 player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
                 player.getRaidsOne().getRaidsConnector().enter(player);
                 player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
               	player.getRaidsOne().getRaidsConnector().leave(true);
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                  player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
               	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }

         },
         RAIDS_FIVE_PHASE_TWO(new int[]{3184, 3198}, new int[]{3825, 3838}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 2) 
         						{
         							GoldRaids.sendPhaseThree(party);
         							return;
         						
         					}
         				}
             		}

             @Override
             public void login(Player player) { 
          	      player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
          	      player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }
         },
         RAIDS_FIVE_PHASE_THREE(new int[]{3137, 3150}, new int[]{3824, 3838}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 3) 
         						{
         							GoldRaids.sendPhaseFour(party);
         							return;
         						}
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
          	  player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }
         },
             RAIDS_FIVE_PHASE_FOUR(new int[]{3137, 3150}, new int[]{3777, 3791}, true, false, true, false, false, true) {

          	   @Override
                 public void process(Player player) {
              	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                     
                     final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
             				for(Player partyMember : party.getPlayers())  
             				{
             						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 5) 
             						{
             							GoldRaids.exitRaidsFive(player);
             							return;
             						}
             					
             				}
                 		}

                 @Override
                 public void login(Player player) { 
              	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
              	  player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
                      player.getRaidsOne().getRaidsConnector().enter(player);
                      player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
                 }

                 @Override
                 public void logout(Player player) {
              	    player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
                 	 	player.getRaidsOne().getRaidsConnector().leave(true);
                 		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                      partyy.remove(player, false, true);
                 }
                 
                 @Override
                 public void onDeath(Player player) {
              	     player.getRaidsOne().getRaidsConnector().leaveRaidsFive();
                   	 player.getRaidsOne().getRaidsConnector().leave(true);
                   	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                       partyy.remove(player, false, true);
                       player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
                   	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
                 }
             

                 @Override
                 public boolean canTeleport(Player player) {
                     	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                     	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                      partyy.remove(player, false, true);
                     	 return true;
                 }

                 @Override
                 public void enter(Player player) {
              	   if (player.getSummoning().getFamiliar() != null) {
                         player.getSummoning().unsummon(true, true);
                         player.getPacketSender().sendMessage("You've dismissed your familiar.");
                     }

                    
                 }
             },
		RAIDS_FOUR_PHASE_ONE(new int[]{2946, 2964}, new int[]{2947, 2964}, true, false, true, false, false, true) {
             @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 2) 
         						{
         							MortalKombatRaids.sendPhaseTwo(party);
         							return;
         						}
         					
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	   player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
          	 player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
                 player.getRaidsOne().getRaidsConnector().enter(player);
                 player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
               	player.getRaidsOne().getRaidsConnector().leave(true);
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                  player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
               	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }

         },
         RAIDS_FOUR_PHASE_TWO(new int[]{2957, 2975}, new int[]{2982, 2999}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 4) 
         						{
         							MortalKombatRaids.sendPhaseThree(party);
         							return;
         						
         					}
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
          	  player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }
         },
         RAIDS_FOUR_PHASE_THREE(new int[]{2986, 3004}, new int[]{2947, 2964}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 6) 
         						{
         							MortalKombatRaids.exitRaidsFour(player);
         							return;
         						}
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
          	  player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsFour();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }

         },
		RAIDS_TWO_PHASE_ONE(new int[]{2965, 2980}, new int[]{2840, 2855}, true, false, true, false, false, true) {
             @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 3) 
         						{
         							AnimeRaids.sendPhaseTwo(party);
         							return;
         						}
         					
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	   player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
          	 player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
                 player.getRaidsOne().getRaidsConnector().enter(player);
                 player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
               	player.getRaidsOne().getRaidsConnector().leave(true);
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                  player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
               	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }

         },
         RAIDS_TWO_PHASE_TWO(new int[]{2988, 3003}, new int[]{2860, 2875}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         					
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 6) 
         						{
         							AnimeRaids.sendPhaseThree(party);
         							return;
         						
         					}
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
          	  player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }
         },
         RAIDS_TWO_PHASE_THREE(new int[]{2988, 3003}, new int[]{2820, 2835}, true, false, true, false, false, true) {

      	   @Override
             public void process(Player player) {
          	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                 
                 final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
         				for(Player partyMember : party.getPlayers())  
         				{
         						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 10) 
         						{
         							AnimeRaids.exitRaidsTwo(player);
         							return;
         						}
         					
         				}
             		}

             @Override
             public void login(Player player) { 
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
          	  player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
                  player.getRaidsOne().getRaidsConnector().enter(player);
                  player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
             }

             @Override
             public void logout(Player player) {
          	    player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
             	 	player.getRaidsOne().getRaidsConnector().leave(true);
             		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
             }
             
             @Override
             public void onDeath(Player player) {
          	     player.getRaidsOne().getRaidsConnector().leaveRaidsTwo();
               	 player.getRaidsOne().getRaidsConnector().leave(true);
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                   partyy.remove(player, false, true);
                   player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
               	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
             }
         

             @Override
             public boolean canTeleport(Player player) {
                 	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                 	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                  partyy.remove(player, false, true);
                 	 return true;
             }

             @Override
             public void enter(Player player) {
          	   if (player.getSummoning().getFamiliar() != null) {
                     player.getSummoning().unsummon(true, true);
                     player.getPacketSender().sendMessage("You've dismissed your familiar.");
                 }

                
             }

         },
       RAIDS_ONE_PHASE_ONE(new int[]{3041, 3062}, new int[]{2890, 2911}, true, false, true, false, false, true) {
           @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       					
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 3) 
       						{
       							PokemonRaids.sendPhaseTwo(party);
       							return;
       						}
       					
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	   player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
               player.moveTo(new Position(2297, 3331, 0));
               player.getRaidsOne().getRaidsConnector().enter(player);
               player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
             	player.getRaidsOne().getRaidsConnector().leave(true);
             	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
             	player.moveTo(new Position(2297, 3331, 0));
             	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }

       },
       RAIDS_ONE_PHASE_TWO(new int[]{3020, 3040}, new int[]{2889, 2911}, true, false, true, false, false, true) {

    	   @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       					
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 6) 
       						{
       							PokemonRaids.sendPhaseThree(party);
       							return;
       						
       					}
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
                player.moveTo(new Position(2297, 3331, 0));
                player.getRaidsOne().getRaidsConnector().enter(player);
                player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	     player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
             	 player.getRaidsOne().getRaidsConnector().leave(true);
             	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
             	 player.moveTo(new Position(2297, 3331, 0));
             	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }
       },
       RAIDS_ONE_PHASE_THREE(new int[]{3033, 3052}, new int[]{2914, 2933}, true, false, true, false, false, true) {

    	   @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 10) 
       						{
       							PokemonRaids.exitRaidsOne(player);
       							return;
       						}
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
                player.moveTo(new Position(2297, 3331, 0));
                player.getRaidsOne().getRaidsConnector().enter(player);
                player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	     player.getRaidsOne().getRaidsConnector().leaveRaidsOne();
             	 player.getRaidsOne().getRaidsConnector().leave(true);
             	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
             	 player.moveTo(new Position(2297, 3331, 0));
             	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }

       },
       RAIDS_THREE_ENTRANCE(new int[]  {1238, 1252}, new int[]{1239, 1253}, false, false, true, false, true, true) {
       	 @Override
            public void process(Player player) { 
                if (player.getMinigameAttributes().getRaidsAttributes().getParty() != null)
                    player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
            }

            @Override
            public void login(Player player) {
           	 player.getRaidsOne().getRaidsConnector().enter(player);
           	 
            }
            @Override
            public void leave(Player player) {
           	 player.getRaidsOne().getRaidsConnector().leaveRaidsThreeEntrance();
            }
            @Override
            public boolean canTeleport(Player player) {
           	 player.getRaidsOne().getRaidsConnector().leaveRaidsThreeEntrance();
                return true;
            }
            @Override
            public void enter(Player player) {
                if (player.getSummoning().getFamiliar() != null) {
                    player.getSummoning().unsummon(true, true);
                    player.getPacketSender().sendMessage("You've dismissed your familiar.");
                }

                player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 58000)
                        .sendTab(GameSettings.ACHIEVEMENT_TAB);

                int id = 58016;
                for (int i = 58016; i < 58064; i++) {
                    id++;
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                    player.getPacketSender().sendString(id++, "");
                }
                player.getPacketSender().sendString(58009, "Create");
                player.getPacketSender().sendString(58002, "Raiding Party: @whi@0");
                
            }
        
       },
		RAIDS_THREE_PHASE_ONE(new int[]{3364, 3377}, new int[]{3907, 3922}, true, false, true, false, false, true) {
           @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       					
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 1) 
       						{
       							SilverRaids.sendPhaseTwo(party);
       							return;
       						}
       					
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	   player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
        	 player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
               player.getRaidsOne().getRaidsConnector().enter(player);
               player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
             	player.getRaidsOne().getRaidsConnector().leave(true);
             	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
                player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
             	player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }

       },
       RAIDS_THREE_PHASE_TWO(new int[]{3329, 3342}, new int[]{3910, 3925}, true, false, true, false, false, true) {

    	   @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       					
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 2) 
       						{
       							SilverRaids.sendPhaseThree(party);
       							return;
       						
       					}
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
        	  player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                player.getRaidsOne().getRaidsConnector().enter(player);
                player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	     player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
             	 player.getRaidsOne().getRaidsConnector().leave(true);
             	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
                 player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
             	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }
       },
       RAIDS_THREE_PHASE_THREE(new int[]{3329, 3342}, new int[]{3950, 3965}, true, false, true, false, false, true) {

    	   @Override
           public void process(Player player) {
        	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
               
               final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
       				for(Player partyMember : party.getPlayers())  
       				{
       						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 3) 
       						{
       							SilverRaids.sendPhaseFour(party);
       							return;
       						}
       					
       				}
           		}

           @Override
           public void login(Player player) { 
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
        	  player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                player.getRaidsOne().getRaidsConnector().enter(player);
                player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
           }

           @Override
           public void logout(Player player) {
        	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
           	 	player.getRaidsOne().getRaidsConnector().leave(true);
           		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
           }
           
           @Override
           public void onDeath(Player player) {
        	     player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
             	 player.getRaidsOne().getRaidsConnector().leave(true);
             	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                 partyy.remove(player, false, true);
                 player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
             	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
           }
       

           @Override
           public boolean canTeleport(Player player) {
               	 player.getRaidsOne().getRaidsConnector().attemptLeave();
               	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                partyy.remove(player, false, true);
               	 return true;
           }

           @Override
           public void enter(Player player) {
        	   if (player.getSummoning().getFamiliar() != null) {
                   player.getSummoning().unsummon(true, true);
                   player.getPacketSender().sendMessage("You've dismissed your familiar.");
               }

              
           }
       },
           RAIDS_THREE_PHASE_FOUR(new int[]{3362, 3375}, new int[]{3951, 3966}, true, false, true, false, false, true) {

        	   @Override
               public void process(Player player) {
            	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                   
                   final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
           				for(Player partyMember : party.getPlayers())  
           				{
           						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 4) 
           						{
           							SilverRaids.sendPhaseFive(party);
           							return;
           						}
           					
           				}
               		}

               @Override
               public void login(Player player) { 
            	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
            	  player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                    player.getRaidsOne().getRaidsConnector().enter(player);
                    player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
               }

               @Override
               public void logout(Player player) {
            	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
               	 	player.getRaidsOne().getRaidsConnector().leave(true);
               		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
               }
               
               @Override
               public void onDeath(Player player) {
            	     player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
                 	 player.getRaidsOne().getRaidsConnector().leave(true);
                 	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                     partyy.remove(player, false, true);
                     player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                 	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
               }
           

               @Override
               public boolean canTeleport(Player player) {
                   	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                   	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                    partyy.remove(player, false, true);
                   	 return true;
               }

               @Override
               public void enter(Player player) {
            	   if (player.getSummoning().getFamiliar() != null) {
                       player.getSummoning().unsummon(true, true);
                       player.getPacketSender().sendMessage("You've dismissed your familiar.");
                   }

                  
               }
           },
               RAIDS_THREE_PHASE_FIVE(new int[]{3347, 3360}, new int[]{3929, 3944}, true, false, true, false, false, true) {

            	   @Override
                   public void process(Player player) {
                	   player.getMinigameAttributes().getRaidsAttributes().getParty().refreshInterface();
                       
                       final RaidsParty party = player.getMinigameAttributes().getRaidsAttributes().getParty();
               				for(Player partyMember : party.getPlayers())  
               				{
               						if(partyMember.getMinigameAttributes().getRaidsAttributes().getKillcount() == 6) 
               						{
               							SilverRaids.exitRaidsThree(player);
               							return;
               						}
               					
               				}
                   		}

                   @Override
                   public void login(Player player) { 
                	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
                	  player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                        player.getRaidsOne().getRaidsConnector().enter(player);
                        player.getPacketSender().sendMessage("@red@You have been moved back to the Lobby since you logged out.");
                   }

                   @Override
                   public void logout(Player player) {
                	    player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
                   	 	player.getRaidsOne().getRaidsConnector().leave(true);
                   		RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                        partyy.remove(player, false, true);
                   }
                   
                   @Override
                   public void onDeath(Player player) {
                	     player.getRaidsOne().getRaidsConnector().leaveRaidsThree();
                     	 player.getRaidsOne().getRaidsConnector().leave(true);
                     	 RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                         partyy.remove(player, false, true);
                         player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
                     	 player.getMinigameAttributes().getRaidsAttributes().incrementDeaths();
                   }
               

                   @Override
                   public boolean canTeleport(Player player) {
                       	 player.getRaidsOne().getRaidsConnector().attemptLeave();
                       	RaidsParty partyy = player.getMinigameAttributes().getRaidsAttributes().getParty();
                        partyy.remove(player, false, true);
                       	 return true;
                   }

                   @Override
                   public void enter(Player player) {
                	   if (player.getSummoning().getFamiliar() != null) {
                           player.getSummoning().unsummon(true, true);
                           player.getPacketSender().sendMessage("You've dismissed your familiar.");
                       }

                      
                   }

       },
               
       DZONE_SOUTH(new int[]  {1817, 1824}, new int[]{3083, 3094}, false, false, true, false, true, true) {
       },
       DZONE_SOUTH_WEST(new int[]  {1800, 1809}, new int[]{3096, 3103}, false, false, true, false, true, true) {
       },
       DZONE_SOUTH_EAST(new int[]  {1833, 1847}, new int[]{3097, 3102}, false, false, true, false, true, true) {
       },
       DZONE_NORTH_WEST(new int[]  {1803, 1819}, new int[]{3114, 3121}, false, false, true, false, true, true) {
       },
       DZONE_NORTH_EAST(new int[]  {1825, 1841}, new int[]{3116, 3121}, false, false, true, false, true, true) {
       },
       
		TRAIN(new int[] { 2660, 2727 }, new int[] { 3708, 3732 }, true, true, true, true, true, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			 @Override
	           public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Training Zone "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
                return;
            }
		}
		},
		
        RAIDS_ONE_REWARD_ROOM(new int[]{1795, 1845}, new int[]{3841, 3878}, false, false, true, false, false, true) {

        },
       
		GAMBLING_ZONE(new int[] { 2907, 2922, }, new int[] { 2589, 2601 }, false, false, false, false, false, false) {
            @Override
            public void enter(Player player) {
                if (player.getPlayerInteractingOption() != PlayerInteractingOption.GAMBLE)
                    player.getPacketSender().sendInteractionOption("Gamble with", 2, false);
            }

			@Override
			public void leave(Player player) {
				player.getPacketSender().sendInteractionOption("null", 2, false);
			}
		},
		GODS_RAID(new int[] { 1800, 1900 }, new int[] { 5100, 5200 }, true, false, false, false, false, false) {
			@Override
			public void enter(Player player) {
				World.minigameHandler.getMinigame(0).handleLocationEntry(player);
				player.sendMessage("You just joined Super Sayian's Raid!");
			}

			@Override
			public void leave(Player player) {
				World.minigameHandler.getMinigame(0).handleLocationOutry(player);
				player.sendMessage("You just left Super Sayian's Raid!");

			}
		},
		NexArch_ZONE(new int[] { 2305, 2315 }, new int[] { 3205, 3212 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Vader Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				} else {
				player.getPacketSender().sendMessage(
						"<img=10>Use your Vader Kill Count at the shop for exclusive prizes!");
			}
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getVaderZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getVaderZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 6820) {
					killer.incrementVaderKills(1);
					killer.getPacketSender().sendString((41704), "" + killer.getVaderKills());
					killer.save();
				}
				return false;
			}
		},
		VADER_ZONE(new int[] { 2911, 2950 }, new int[] { 2706, 2736 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Vader Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				} else {
				player.getPacketSender().sendMessage(
						"<img=10>Use your Vader Kill Count at the shop for exclusive prizes!");
			}
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getVaderZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getVaderZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 466) {
					killer.incrementVaderKills(1);
					killer.getPacketSender().sendString((41704), "" + killer.getVaderKills());
					killer.save();
				}
				return false;
			}
		},
		WRECKED_ZONE(new int[] { 3345, 3384 }, new int[] { 3020, 3060 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendMessage(
						"<img=10>Use your Inherited Kill Count at the shop for exclusive prizes!");
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getVaderZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getVaderZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 1416) {
					killer.incrementVaderKills(1);
					killer.getPacketSender().sendString((41704), "" + killer.getVaderKills());
					killer.save();
				}
				return false;
			}
		},
		DRAGON_ZONE(new int[] { 2521, 2594 }, new int[] { 4931, 4985 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendMessage(
						"<img=10>Use your Dragon Kill Count at the shop for exclusive prizes!");
			
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 175) {
					killer.incrementDragonKills(1);
					killer.getPacketSender().sendString((41704), "" + killer.getDragonKills());
					killer.save();
				}
				return false;
			}
		},
        SMOOZY(new int[]{3542, 3569}, new int[]{3089, 3118}, true, true, true, true, true, true) {
            public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void process(Player player) {
                if (!player.walkableInterfaceList.contains(41700))
                    player.sendParallellInterfaceVisibility(41700, true);
            }

            @Override
            public void enter(Player player) {
                    player.getPacketSender().sendMessage(
                            "<img=10> Smoozies Kill Count");
               }
            

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendString((-1), "0");
                return true;
            }

            @Override
            public void onDeath(Player player) {
                leave(player);
            }

            @Override
            public void leave(Player p) {
                p.getPacketSender().sendString((-1), "0");
                for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
                        .getKillcount().length; i++) {
                    p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
                    p.getPacketSender().sendString((41704 + i), "0");
                }
            }

            @Override
            public boolean handleKilledNPC(Player p, NPC n) {
                int npc = n.getId();
                if (npc == 1041) {
                   p.incrementDragonKills(1);
                    p.save();
                }
                return false;
            }
        },
        
		OCTANE_ZONE(new int[] { 2378, 2422 }, new int[] { 2885, 2933 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Octane Zone "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				} else {
				player.getPacketSender().sendMessage(
						"<img=10> Octane Kill Count");
			}
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 1417) {
					killer.incrementDragonKills(1);
					killer.save();
				}
				return false;
			}
		},
		
		KNIGHT_OF_TORMENT_ZONE(new int[] { 3213, 3250 }, new int[] { 3021, 3058}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Knight of Torment Area "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Knight of Torment Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 1038) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		LIMES_ZONE(new int[] { 2742, 2693 }, new int[] { 3898, 3849}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Limes Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Limes Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 1017) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		SLIFER_ZONE(new int[] { 2177, 2238 }, new int[] { 3777, 3838}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Slifer Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Slifer Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 1018) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		

		FELLEN_ZONE(new int[] { 3411, 3437}, new int[] { 3982, 4016}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
				if(player.getPosition().getZ() == 0) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
			    }
                return false;
            }
		    @Override
		    public void process(Player player) {
		    	if(player.getPosition().getZ() == 0) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    	}
		    }

		    @Override
		    public void enter(Player player) {
		    	if(player.getPosition().getZ() == 0) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Fallen Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Fallen Kill Count");
		        }
		    	}
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		    	if(player.getPosition().getZ() == 0) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    	}
		    	return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		    	if(p.getPosition().getZ() == 0) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    	}
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		    	if(killer.getPosition().getZ() == 0) {
		        int npc = n.getId();
		        if (npc == 1039) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		    	return false;
		    }
		},
		
		LITCH_ZONE(new int[] { 2485, 2547 }, new int[] { 10124, 10166}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Litch Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Litch Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 1494) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		GODS_RULER_ZONE(new int[] { 3053, 3026 }, new int[] { 5358, 5333}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Gods Ruler Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Gods Ruler Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 1511) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		DR_STRANGE_ZONE(new int[] { 2256, 2287 }, new int[] { 4680, 4711}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to Dr. Strange Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Dr. Strange Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 3007) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		SPUDERMAN_ZONE(new int[] { 3389, 3363 }, new int[] { 3221, 3206}, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
			    for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
			        if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
			                .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
			        	 return true;
                    }
                }
                return false;
            }
		    @Override
		    public void process(Player player) {
		        if (!player.walkableInterfaceList.contains(41700))
		            player.sendParallellInterfaceVisibility(41700, true);
		    }

		    @Override
		    public void enter(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Spuderman Zone "
		                    + player.getUsername() + "!");
		            player.moveTo(GameSettings.DEFAULT_POSITION, true);
		        } else {
		            player.getPacketSender().sendMessage("<img=10> Spuderman Kill Count");
		        }
		    }

		    @Override
		    public boolean canTeleport(Player player) {
		        if (hasOverpoweredWeapon(player)) {
		            player.getPacketSender().sendMessage("@red@You can't teleport with AOE Weapons in this area!");
		            return false;
		        }
		        player.getPacketSender().sendString((-1), "0");
		        return true;
		    }

		    @Override
		    public void onDeath(Player player) {
		        leave(player);
		    }

		    @Override
		    public void leave(Player p) {
		        p.getPacketSender().sendString((-1), "0");
		        for (int i = 0; i < p.getMinigameAttributes().getDragonZoneAttributes()
		                .getKillcount().length; i++) {
		            p.getMinigameAttributes().getDragonZoneAttributes().getKillcount()[i] = 0;
		            p.getPacketSender().sendString((41704 + i), "0");
		        }
		    }

		    @Override
		    public boolean handleKilledNPC(Player killer, NPC n) {
		        int npc = n.getId();
		        if (npc == 3012) {
		            killer.incrementDragonKills(1);
		            killer.save();
		        }
		        return false;
		    }
		},
		
		MARVELS_RAID(new int[] { 2224, 2276 }, new int[] { 3300, 3323 }, true, false, false, false, false, false) {
			@Override
			public void enter(Player player) {
				World.minigameHandler.getMinigame(0).handleLocationEntry(player);
				player.sendMessage("You just joined Marvels Raid!");
			}

			@Override
			public void leave(Player player) {
				World.minigameHandler.getMinigame(0).handleLocationOutry(player);
				player.sendMessage("You just left Marvels Raid!");

			}
		},
		DUNGEONEERING(new int[]{3433, 3459, 2421, 2499}, new int[]{3694, 3729, 4915, 4990}, true, false, true, false, true, false) {
			@Override
			public void login(Player player) {
				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 26600).sendTab(GameSettings.QUESTS_TAB);
			}
			
			@Override
			public void logout(Player player) {
				if(player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				if(Dungeoneering.doingDungeoneering(player)) {
				player.getInventory().resetItems();
				player.getEquipment().resetItems();
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				}
			}

			@Override
			public void leave(Player player) {
				Dungeoneering.leave(player, true, true);
			}

			@Override
			public void enter(Player player) {
				player.getPacketSender().sendDungeoneeringTabIcon(true).sendTabInterface(GameSettings.QUESTS_TAB, 27224).sendTab(GameSettings.QUESTS_TAB);
				DialogueManager.start(player, 104);
			}

			@Override
			public void onDeath(Player player) {
				if(Dungeoneering.doingDungeoneering(player)) {
					Dungeoneering.handlePlayerDeath(player);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if(Dungeoneering.doingDungeoneering(killer)) {
					Dungeoneering.handleNpcDeath(killer, npc);
					return true;
				}
				return false;
			}

			@Override
			public void process(Player player) {
				if(Dungeoneering.doingDungeoneering(player)) {
					
					
				
				} else {
					player.sendParallellInterfaceVisibility(37500, false);
				}
			}
		},
        KRAKEN(new int[]{3680, 3712}, new int[]{5791, 5824}, true, false, false, false, false, true) {
            @Override
            public boolean canTeleport(Player player) {
                player.resetKraken();
                return true;
            }

            @Override
            public void logout(Player player) {
                player.resetKraken();
            }

            @Override
            public void onDeath(Player player) {
                player.resetKraken();
            }

            @Override
            public void leave(Player player) {
                player.resetKraken();
            }
        },
        
		MEMBER_ZONE(new int[]{3415, 3435}, new int[]{2900, 2926}, false, true, true, false, false, true) {
		},
		CORTEX_BOSS(new int[]{1877, 1901}, new int[]{5393, 5420}, true, true, true, true, true, true) {
		},
		DONKEY(new int[]{2304, 2352}, new int[]{4574, 4604}, true, true, true, true, true, true) {
		},
		SPIT(new int[]{3472, 3503}, new int[]{2701, 2732}, true, true, true, true, true, true) {
		},
		AOE_ZONE(new int[]{2702, 2731}, new int[]{2824, 2862}, true, true, true, true, true, true) {
		},
		REDASS(new int[]{3286, 3320}, new int[]{4957, 4987}, true, true, true, true, true, true) {
		},
		GROUDON(new int[]{2370, 2426}, new int[]{4674, 4686}, true, true, true, true, true, true) {
		},
		IT(new int[]{2903, 2919}, new int[]{3605, 3624}, true, true, true, true, true, true) {
		},
		TDIGI(new int[]{2457, 2473}, new int[]{4771, 4788}, true, true, true, true, true, true) {
		},
        Diamond(new int[]{1790, 1857}, new int[]{3070, 3136}, true, true, true, true, true, true) {
        },
        FALLEN(new int[]{3393, 3454}, new int[]{3969, 4029}, true, true, true, true, true, true) {
        },
        LAVA(new int[]{3202, 3252}, new int[]{3009, 3068}, true, true, true, true, true, true) {
        },
        VALENTINES(new int[]{3648, 3711}, new int[]{3584, 3647}, true, true, true, true, true, true) {
        },
        DIGI(new int[]{2506, 2534}, new int[]{4822, 4856}, true, true, true, true, true, true) {
        },
		BLOODK(new int[]{3023, 3058}, new int[]{4818, 4844}, true, true, true, true, true, true) {
		},
		ASALIAS(new int[]{2322, 4636}, new int[]{2336, 4646}, true, true, true, true, true, true) {
		},
		DONALD(new int[]{2513, 2540}, new int[]{4769, 4783}, true, true, true, true, true, true) {
		},
		KID(new int[]{2829, 2872}, new int[]{4045, 4084}, true, true, true, true, true, true) {
		},
		MARIO(new int[]{2512, 2536}, new int[]{4636, 4656}, true, true, true, true, true, true) {
		},
		CHARIZARD(new int[]{2509, 2539}, new int[]{2527, 2557}, true, true, true, true, true, true) {
		},
		LUIGI(new int[]{3300, 3325}, new int[]{4053, 4094}, true, true, true, true, true, true) {
		},
		ICED(new int[]{2630, 2650}, new int[]{4036, 4048}, true, true, true, true, true, true) {
		},
		GREEN(new int[]{2607, 2919}, new int[]{2581, 2612}, true, true, true, true, true, true) {
		},
		DILLONSPET(new int[]{3542, 3590}, new int[]{9930, 9956}, true, true, true, true, true, true) {
		},
		MORTY(new int[]{2688, 2731}, new int[]{9497, 9529}, true, true, true, true, true, true) {
		},
		MEWTWO(new int[]{2880, 2940}, new int[]{2770, 2800}, true, true, true, true, true, true) {
		},
		THANOS(new int[]{2702, 2800}, new int[]{9732, 9774}, true, true, true, true, true, true) {
		},
		DEADLYROBOT(new int[]{3100, 3140}, new int[]{4375, 4430}, true, true, true, true, true, true) {
		},
		LAGANO(new int[]{2790, 2799}, new int[]{3320, 3330}, true, true, true, true, true, true) {
		},
		RICK(new int[]{2838, 2875}, new int[]{9913, 9959}, true, true, true, true, true, true) {
		},
		REDD(new int[]{3334, 3385}, new int[]{5207, 5242}, true, true, true, true, true, true) {
		},
		Dooms_Day(new int[]{3392, 3455}, new int[]{5248, 5310}, true, true, true, true, true, true) {
		},
		GOLDENKNIGHT(new int[]{2389, 2403}, new int[]{3481, 3494}, true, true, true, true, true, true) {
		},
		BLACKKNIGHT(new int[]{2829, 2837}, new int[]{4579, 4595}, true, true, true, true, true, true) {
		},
		BADBITCH(new int[]{2574, 2598}, new int[]{4562, 4583}, true, true, true, true, true, true) {
		},
		RASS(new int[]{2505, 2532}, new int[]{10141, 10147}, true, true, true, true, true, true) {
		},
		Lugia(new int[]{2540, 2545 }, new int[]{3740, 3745}, true, true, true, true, true, true) {
		},
		BZONE(new int[]{2881, 2942 }, new int[]{2497, 2559}, true, true, true, true, true, true) {
		},
		DONATOR_ZONE(new int[]{1987, 2046}, new int[]{4670, 4610}, false, true, true, true, true, true) {
		},
		VARROCK(new int[]{3167, 3272}, new int[]{3376, 3504}, false, true, true, true, true, true) {
		},
        TREASURE_ISLAND(new int[]{3010, 3075}, new int[]{2870, 2950}, true, true, true, false, true, true) {
        },
        GAMBLING(new int[]{2400, 2470}, new int[]{3070, 3140}, false, false, true, true, true, true) {
        },
		DROPPARTY(new int[]{2725, 2750}, new int[]{3459, 3477}, false, false, true, true, true, true) {
		},
		BANK(new int[]{3090, 3099, 3089, 3090, 3248, 3258, 3179, 3191, 2944, 2948, 2942, 2948, 2944, 2950, 3008, 3019, 3017, 3022, 3203, 3213, 3212, 3215, 3215, 3220, 3220, 3227, 3227, 3230, 3226, 3228, 3227, 3229}, new int[]{3487, 3500, 3492, 3498, 3413, 3428, 3432, 3448, 3365, 3374, 3367, 3374, 3365, 3370, 3352, 3359, 3352, 3357, 3200, 3237, 3200, 3235, 3202, 3235, 3202, 3229, 3208, 3226, 3230, 3211, 3208, 3226}, false, false, false, false, false, false) {
		},
		EDGE(new int[]{3073, 3134}, new int[]{3457, 3518}, false, true, true, false, false, true) {
		},
		LUMBRIDGE(new int[]{3175, 3238}, new int[]{3179, 3302}, false, true, true, true, true, true) {
		},
		KING_BLACK_DRAGON(new int[]{2251, 2292}, new int[]{4673, 4717}, true, true, true, true, true, true) {
			
			@Override
			public boolean canTeleport(Player player) {
				if(player.isPlayerInstanced() == true) {
				KingBlackDragon.removeInstancesFull(player);
				}
				return true;
			}
			@Override
			public void login(Player player) {
				if(player.isPlayerInstanced() == true) {
				KingBlackDragon.createInstance(player);
				}
				
			}
			@Override
			public void logout(Player player) {
				if(player.isPlayerInstanced() == true) {
				KingBlackDragon.removeInstancesTemp(player);
				}
				
			}
			@Override
			public void onDeath(Player player) {
				if(player.isPlayerInstanced() == true) {
				KingBlackDragon.removeInstancesFull(player);
				}
			}
			
		},
		MELEE_BATTLE_ROYALE(new int[]{3302, 3331}, new int[]{9830, 9853}, true, false, true, false, true, true) {
			
			@Override
			public boolean canAttack(Player player, Player target) {
				return true;
			}
			
			@Override
			public void process(Player player) {		
				player.setWildernessLevel(60);
			
			}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You can't teleport here");
				return false;
			}
			
			@Override
			public void login(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void logout(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void onDeath(Player player) {
				BattleRoyale.removePlayer(player);
				BattleRoyale.leaveArena(player);
				BattleRoyale.checkMeleeEndGame();
			}
			
			@Override
			public void enter(Player player) {
				player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.getPacketSender().sendString(19000, "Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendInteractionOption("null", 2, false);
			}	
		},
		RANGE_BATTLE_ROYALE(new int[]{3302, 3331}, new int[]{9830, 9853}, true, false, true, false, true, true) {
			
			@Override
			public boolean canAttack(Player player, Player target) {
				return true;
			}
			
			@Override
			public void process(Player player) {		
				player.setWildernessLevel(60);
			
			}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You can't teleport here");
				return false;
			}
			
			@Override
			public void login(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void logout(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void onDeath(Player player) {
				BattleRoyale.removePlayer(player);
				BattleRoyale.leaveArena(player);
				BattleRoyale.checkRangeEndGame();
			}
			
			@Override
			public void enter(Player player) {
				player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.getPacketSender().sendString(19000, "Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendInteractionOption("null", 2, false);
			}	
		},
		MAGE_BATTLE_ROYALE(new int[]{3302, 3331}, new int[]{9830, 9853}, true, false, true, false, true, true) {
			
			@Override
			public boolean canAttack(Player player, Player target) {
				return true;
			}
			
			@Override
			public void process(Player player) {		
				player.setWildernessLevel(60);
			
			}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendInterfaceRemoval();
				player.getPacketSender().sendMessage("You can't teleport here");
				return false;
			}
			
			@Override
			public void login(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void logout(Player player) {
				BattleRoyale.leaveArena(player);
			}
			
			@Override
			public void onDeath(Player player) {
				BattleRoyale.removePlayer(player);
				BattleRoyale.leaveArena(player);
				BattleRoyale.checkMageEndGame();
			}
			
			@Override
			public void enter(Player player) {
				player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.getPacketSender().sendString(19000, "Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
			
			@Override
			public void leave(Player player) {
				player.getPacketSender().sendInteractionOption("null", 2, false);
			}	
		},
		UNHOLY_CURSEBEARER(new int[]{3047, 3070}, new int[]{4390, 4370}, true, true, true, true, true, true) {
		},
		
		BORK(new int[]{3080, 3120}, new int[]{5520, 5550}, true, true, true, true, true, true) {
		
			@Override
			public void enter(Player player) {
				
			}			
			@Override
			public void leave(Player player) {
				BORKS.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				BORKS.closeInterface(player);
			}
		},
		LIZARDMAN(new int[]{2700, 2730}, new int[]{9800, 9829}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//	LIZARD.startPreview(player);
			}			
			@Override
			public void leave(Player player) {
				LIZARD.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				LIZARD.closeInterface(player);
			}
		},
		BARRELCHESTS(new int[]{2960, 2990}, new int[]{9510, 9520}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//	BARRELS.startPreview(player);
			}			
			@Override
			public void leave(Player player) {
				BARRELS.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				BARRELS.closeInterface(player);
			}
		},
		SLASH_BASH(new int[]{2504, 2561}, new int[]{9401, 9473}, true, true, true, true, true, true) {

			@Override
			public void enter(Player player) {
				
				//SLASHBASH.startPreview(player);
			}			
			@Override
			public void leave(Player player) {
				SLASHBASH.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				SLASHBASH.closeInterface(player);
			}
		},
		BANDOS_AVATAR(new int[]{2877, 2928}, new int[]{4734, 4787}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//	AVATAR.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				AVATAR.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				AVATAR.closeInterface(player);
			}
		},
		TORM_DEMONS(new int[]{2520, 2560}, new int[]{5730, 5799}, true, true, true, true, true, true) {
		
			@Override
			public void enter(Player player) {
				
		//	TDS.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				TDS.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				TDS.closeInterface(player);
			}
		
		},
		KALPHITE_QUEEN(new int[]{3464, 3500}, new int[]{9478, 9523}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//KALPH.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				KALPH.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				KALPH.closeInterface(player);
			}
		
		},
		PHOENIX(new int[]{2824, 2862}, new int[]{9545, 9594}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
			
			}
			
			@Override
			public void leave(Player player) {
				PHEON.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				PHEON.closeInterface(player);
			}
		
		},
		GLACORS(new int[]{3000, 3100}, new int[]{9500, 9600}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//GLAC.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				GLAC.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				GLAC.closeInterface(player);
			}
		
		},
		SKOTIZO(new int[]{3350, 3390}, new int[]{9800, 9825}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//SKOT.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				SKOT.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				SKOT.closeInterface(player);
			}
		
		},
		CERBERUS(new int[]{1215, 1265}, new int[]{1220, 1265}, true, true, true, true, true, true) {
			
			@Override
			public boolean canTeleport(Player player) {
				if(player.isPlayerInstanced() == true) {
				KingBlackDragon.removeInstancesFull(player);
				}
				return true;
			}
			@Override
			public void login(Player player) {
				if(player.isPlayerInstanced() == true) {
				Cerberus.createInstance(player);
				}
				
			}
			@Override
			public void logout(Player player) {
				if(player.isPlayerInstanced() == true) {
				Cerberus.removeInstancesTemp(player);
				}
				
			}
			@Override
			public void onDeath(Player player) {
				if(player.isPlayerInstanced() == true) {
				Cerberus.removeInstancesFull(player);
				}
			}
			
		},
		NEX(new int[]{2900, 2945}, new int[]{5180, 5220}, true, true, true, true, true, true) {
			@Override
			public void enter(Player player) {
				
			//NEXX.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				NEXX.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				NEXX.closeInterface(player);
			}
		
		},
		BANDIT_CAMP(new int[]{3020, 3150, 3055, 3195}, new int[]{3684, 3711, 2958, 3003}, true, true, true, true, true, true) {
		},
		IN_JAIL(new int[]{2050, 2120}, new int[]{4410, 4450}, true, false, true, true, true, true) {
		
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here.");
				return false;
			}
			
			@Override
			public void logout(Player player) {
				
			}

			@Override
			public void leave(Player player) {
				
			}

			@Override
			public void onDeath(Player player) {
				Position position = new Position(2095,4429);
				player.moveTo(position);
			}
		
		
		},
		
		ROCK_CRABS(new int[]{2689, 2727}, new int[]{3691, 3730}, true, true, true, true, true, true) {
		},
		ARMOURED_ZOMBIES(new int[]{3077, 3132}, new int[]{9657, 9680}, true, true, true, true, true, true) {
		},
		CORPOREAL_BEAST(new int[]{2879, 2964}, new int[]{4368, 4409}, true, true, true, false, true, true) {
			@Override
			public void enter(Player player) {
				
			//CORP.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				CORP.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				CORP.closeInterface(player);
			}
		
		},
		DAGANNOTH_DUNGEON(new int[]{2886, 2938}, new int[]{4431, 4477}, true, true, true, false, true, true) {
			@Override
			public void enter(Player player) {
				
				//DAGS.startPreview(player);
			
			}			
			@Override
			public void leave(Player player) {
				DAGS.closeInterface(player);
			}			
			@Override
			public void onDeath(Player player) {
				DAGS.closeInterface(player);
			}
		},
		ZULRAH_CLOUD_ONE(new int[]{2262, 2264}, new int[]{3074, 3076}, true, true, true, true, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_TWO(new int[]{2262, 2264}, new int[]{3072, 3074}, true, true, true, true, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_THREE(new int[]{2262, 2264}, new int[]{3069, 3071}, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_FOUR(new int[]{2265, 2267}, new int[]{3068, 3070}, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_FIVE(new int[]{2268, 2270}, new int[]{3068, 3070}, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_SIX(new int[]{2271, 2273}, new int[]{3069, 3071}, true, true, true, true, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_SEVEN(new int[]{2272, 2274}, new int[]{3072, 3074}, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH_CLOUD_EIGHT(new int[]{2265, 2264}, new int[]{3069, 3071}, true, true, true, true, true, true) {
			
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));
				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		ZULRAH(new int[]{2257, 2281}, new int[]{3063, 3083}, true, true, true, true, true, true){
			@Override
			public boolean canTeleport(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				return true;
			}
			@Override
			public void logout(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				
			}
			@Override
			public void onDeath(Player player) {
				World.getNpcs().forEach(n -> n.removeInstancedNpcs(Locations.Location.ZULRAH,  player.getIndex() * 4));

			}
			
		},
		WILDERNESS(new int[]{2941, 3392, 2986, 3012, 3653, 3710, 3650, 3653}, new int[]{3523, 3968, 10338, 10366, 3441, 3538, 3457, 3472}, false, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				int x = player.getPosition().getX();
				int y = player.getPosition().getY();

				boolean ghostTown = x >= 3650 && y <= 3538;
				
				if(ghostTown) {
					
					player.setWildernessLevel(60);
					
				} else {
					player.setWildernessLevel(((((y > 6400 ? y - 6400 : y) - 3520) / 8)+1));
				}
				player.getPacketSender().sendString(25352, ""+player.getWildernessLevel());
				player.getPacketSender().sendString(25355, "Levels: "+CombatFactory.getLevelDifference(player, false) +" - "+CombatFactory.getLevelDifference(player, true));
				BountyHunter.process(player);
			}

			@Override
			public void leave(Player player) {
				if(player.getLocation() != this) {
					player.getPacketSender().sendString(19000, "Combat level: " + player.getSkillManager().getCombatLevel());
					player.getUpdateFlag().flag(Flag.APPEARANCE);
				}
				PLAYERS_IN_WILD--;
			}
		
			@Override
			public void enter(Player player) {
				for(int i = 0; i < 7; i++) {
					if(i == 3 || i == 5)
						continue;
					if(player.getSkillManager().getCurrentLevel(Skill.forId(i)) > 118) {
			
						player.getSkillManager().setCurrentLevel(Skill.forId(i), 118);

						if(player.getSkillManager().getCurrentLevel(Skill.forId(4)) > 118) {
							player.getSkillManager().setCurrentLevel(Skill.forId(4), 112);
						}
						if(player.getSkillManager().getCurrentLevel(Skill.forId(6)) > 118) {
							player.getSkillManager().setCurrentLevel(Skill.forId(6), 112);
						}
					}
				}
				
				player.getPacketSender().sendInteractionOption("Attack", 2, true);
				player.sendParallellInterfaceVisibility(25347, true);
				player.getPacketSender().sendString(19000, "Combat level: " + player.getSkillManager().getCombatLevel());
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				PLAYERS_IN_WILD++;
			}

			@Override
			public boolean canTeleport(Player player) {
				if(player.getWildernessLevel() > 20) {
					if(player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER) {
						player.getPacketSender().sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
						return true;
					}
					player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
					player.getPacketSender().sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
					return false;
				}
				return true;
			}

			@Override
			public void login(Player player) {
				player.performGraphic(new Graphic(2000, 8));
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(), target.getSkillManager().getCombatLevel());
				if (combatDifference > player.getWildernessLevel() || combatDifference > target.getWildernessLevel()) {
					player.getPacketSender().sendMessage("Your combat level difference is too great to attack that player here.");
					player.getMovementQueue().reset();
					return false;
				}
				if(target.getLocation() != Location.WILDERNESS) {
					player.getPacketSender().sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
					player.getMovementQueue().reset();
					return false;
				}
				if(Jail.isJailed(player)) {
					player.getPacketSender().sendMessage("You cannot do that right now.");
					return false;
				}
				if(Jail.isJailed(target)) {
					player.getPacketSender().sendMessage("That player cannot be attacked right now.");
					return false;
				}
				/*if(Misc.getMinutesPlayed(player) < 20) {
					player.getPacketSender().sendMessage("You must have played for at least 20 minutes in order to attack someone.");
					return false;
				}
				if(Misc.getMinutesPlayed(target) < 20) {
					player.getPacketSender().sendMessage("This player is a new player and can therefore not be attacked yet.");
					return false;
				}*/
				return true;
			}
		},
		CONSTRUCTION(new int[] {1880, 1951}, new int[] {5080, 5151}, false, true, true, false, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}
			
			@Override
			public void login(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			}

			@Override
			public void logout(Player player) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
			}
		},
		
		BARROWS(new int[] {3520, 3598, 3543, 3584, 3543, 3560}, new int[] {9653, 9750, 3265, 3314, 9685, 9702}, false, true, true, true, true, true) {
			@Override
			public void process(Player player) {
				if(player.getWalkableInterfaceId() != 37200)
					player.sendParallellInterfaceVisibility(37200,true);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void logout(Player player) {

			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				Barrows.killBarrowsNpc(killer, npc, true);
				return true;
			}
		},
		PEST_CONTROL_GAME(new int[]{2624, 2690}, new int[]{2550, 2619}, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
				if(player.getWalkableInterfaceId() != 21100)
					player.sendParallellInterfaceVisibility(21100,true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked on this island. Wait for the game to finish!");
				return false;
			}

			@Override
			public void leave(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2657, 2612, 0));
			}
		},
		PEST_CONTROL_BOAT(new int[]{2659, 2664}, new int[]{2637, 2645}, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
				if(!player.walkableInterfaceList.contains(21005))
					player.sendParallellInterfaceVisibility(21005, true);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the boat before teleporting.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if(player.getLocation() != PEST_CONTROL_GAME) {
					PestControl.leave(player, true);
				}
			}

			@Override
			public void logout(Player player) {
				PestControl.leave(player, true);
			}
		},
		SOULWARS(new int[]{-1, -1}, new int[]{-1, -1}, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {

			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("If you wish to leave, you must use the portal in your team's lobby.");
				return false;
			}

			@Override
			public void logout(Player player) {

			}

			@Override
			public void onDeath(Player player) {

			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {

				return false;
			}

		},
		SOULWARS_WAIT(new int[]{-1, -1}, new int[]{-1, -1}, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("You must leave the waiting room before being able to teleport.");
				return false;
			}

			@Override
			public void logout(Player player) {}
		},
		FIGHT_CAVES(new int[]{2360, 2445}, new int[]{5045, 5125}, true, true, false, false, false, false) {
			@Override
			public void process(Player player) {}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here. If you'd like to leave, use the north-east exit.");
				return false;
			}

			@Override
			public void login(Player player) {}

			@Override
			public void leave(Player player) {
				player.getCombatBuilder().reset(true);
				if(player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				player.moveTo(new Position(2439, 5169));
			}

			@Override
			public void onDeath(Player player) {
				FightCave.leaveCave(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				FightCave.handleJadDeath(killer, npc);
				return true;
			}
		},
		VOTE_BOSS(new int[]{2876, 2928}, new int[]{3195, 3261}, true, true, false, false, false, false) {
			@Override
			public void process(Player player) {}

			@Override
			public boolean canTeleport(Player player) {
				player.getCombatBuilder().reset(true);
				if(player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				player.moveTo(GameSettings.DEFAULT_POSITION);
				return true;
			}

			@Override
			public void login(Player player) {
				RaichuInstance.leaveVoteBoss(player, true);
			}

			@Override
			public void leave(Player player) {
				player.getCombatBuilder().reset(true);
				if(player.getRegionInstance() != null) {
					player.getRegionInstance().destruct();
				}
				player.moveTo(GameSettings.DEFAULT_POSITION);
			}

			@Override
			public void onDeath(Player player) {
				RaichuInstance.leaveVoteBoss(player, true);
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				RaichuInstance.handleRaichuDeath(killer, npc);
				return true;
			}
		},
		GRAVEYARD(new int[]{3485, 3517}, new int[]{3559, 3580}, true, true, false, true, false, false) {
			@Override
			public void process(Player player) {
			}

			@Override
			public boolean canTeleport(Player player) {
				if(player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					player.getPacketSender().sendInterfaceRemoval().sendMessage("A spell teleports you out of the graveyard.");
					Graveyard.leave(player);
					return false;
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				if(player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
					Graveyard.leave(player);
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				return killer.getMinigameAttributes().getGraveyardAttributes().hasEntered() && Graveyard.handleDeath(killer, npc);
			}

			@Override
			public void onDeath(Player player) {
				Graveyard.leave(player);
			}
		},
		FIGHT_PITS(new int[]{2370, 2425}, new int[]{5133, 5167}, true, true, true, false, false, true) {
			@Override
			public void process(Player player) {
				if(FightPit.inFightPits(player)) {
					FightPit.updateGame(player);
					if(player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
						player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave game");
			}

			@Override
			public void leave(Player player) {
				onDeath(player);
			}

			@Override
			public void onDeath(Player player) {
				if(FightPit.getState(player) != null) {
					FightPit.removePlayer(player, "death");
				}
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				String state1 = FightPit.getState(player);
				String state2 = FightPit.getState(target);
				return state1 != null && state1.equals("PLAYING") && state2 != null && state2.equals("PLAYING");
			}
		},
		FIGHT_PITS_WAIT_ROOM(new int[]{2393, 2404}, new int[]{5168, 5176}, false, false, false, false, false, true) {
			@Override
			public void process(Player player) {
				FightPit.updateWaitingRoom(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
				return false;
			}

			@Override
			public void logout(Player player) {
				FightPit.removePlayer(player, "leave room");
			}

			@Override
			public void leave(Player player) {
				if(player.getLocation() != FIGHT_PITS) {
					FightPit.removePlayer(player, "leave room");
				}
			}

		},
		DUEL_ARENA(new int[]{3322, 3394, 3311, 3323, 3331, 3391}, new int[]{3195, 3291, 3223, 3248, 3242, 3260}, false, false, false, false, false, false) {
			@Override
			public void process(Player player) {
				if(!player.walkableInterfaceList.contains(201))
					player.sendParallellInterfaceVisibility(201, true);
				if(player.getDueling().duelingStatus == 0) {
					if(player.getPlayerInteractingOption() != PlayerInteractingOption.CHALLENGE)
						player.getPacketSender().sendInteractionOption("Challenge", 2, false);
				} else if(player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
			}

			@Override
			public void enter(Player player) {
				PLAYERS_IN_DUEL_ARENA++;
				player.getPacketSender().sendMessage("<img=10> <col=996633>Warning! Do not stake items which you are not willing to lose.");
			}

			@Override
			public boolean canTeleport(Player player) {
				if(player.getDueling().duelingStatus == 5) {
					player.getPacketSender().sendMessage("To forfiet a duel, run to the west and use the trapdoor.");
					return false;
				}
				return true;
			}

			@Override
			public void logout(Player player) {
				boolean dc = false;
				if(player.getDueling().inDuelScreen && player.getDueling().duelingStatus != 5) {
					player.getDueling().declineDuel(player.getDueling().duelingWith > 0 ? true : false);
				} else if(player.getDueling().duelingStatus == 5) {
					if(player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if(duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
						} else {
							dc = true;
						}
					}
				}
				player.moveTo(new Position(3368, 3268));
				if(dc) {
					World.getPlayers().remove(player);
				}
			}

			@Override
			public void leave(Player player) {
				if(player.getDueling().duelingStatus == 5) {
					onDeath(player);
				}
				PLAYERS_IN_DUEL_ARENA--;
			}

			@Override
			public void onDeath(Player player) {
				if(player.getDueling().duelingStatus == 5) {
					if(player.getDueling().duelingWith > -1) {
						Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
						if(duelEnemy != null) {
							duelEnemy.getDueling().duelVictory();
							duelEnemy.getPacketSender().sendMessage("You won the duel! Congratulations!");
						}
					}
					player.getPacketSender().sendMessage("You've lost the duel.");
					player.getDueling().arenaStats[1]++; player.getDueling().reset();
				}
				player.moveTo(new Position(3368 + Misc.getRandom(5), 3267+ Misc.getRandom(3)));
				player.getDueling().reset();
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				if(target.getIndex() != player.getDueling().duelingWith) {
					player.getPacketSender().sendMessage("That player is not your opponent!");
					return false;
				}
				if(player.getDueling().timer != -1) {
					player.getPacketSender().sendMessage("You cannot attack yet!");
					return false;
				}
				return player.getDueling().duelingStatus == 5 && target.getDueling().duelingStatus == 5;
			}
		},
		GODWARS_DUNGEON(new int[]{2800, 2950, 2858, 2943}, new int[]{5200, 5400, 5180, 5230}, true, true, true, false, true, true) {
			@Override
			public void process(Player player) {
			
				if(!player.walkableInterfaceList.contains(16210))
					player.sendParallellInterfaceVisibility(16210, true);
			}

			@Override
			public void enter(Player player) {
			//	DialogueManager.start(player, 110);
				//player.getPacketSender().sendMessage("<img=10> If you die in a boss room, you will lose your items. You have been warned.");
				//GWD.startPreview(player);
			}

			@Override
			public boolean canTeleport(Player player) {
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
				GWD.closeInterface(player);
			}

			@Override
			public void leave(Player p) {
				GWD.closeInterface(p);

				for(int i = 0; i < p.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount().length; i++) {
					p.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((16216+i), "0");
				}
				p.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(0).setHasEnteredRoom(false);
				p.getPacketSender().sendMessage("Your Godwars dungeon progress has been reset.");
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int index = -1;
				int npc = n.getId();
				if(npc == 6246 || npc == 6229 || npc == 6230 || npc == 6231) //Armadyl
					index = 0;
				else if(npc == 102 || npc == 3583 || npc == 115 || npc == 113 || npc == 6273 || npc == 6276 || npc == 6277 || npc == 6288) //Bandos
					index = 1;
				else if(npc == 6258 || npc == 6259 || npc == 6254 || npc == 6255 || npc == 6257 || npc == 6256) //Saradomin
					index = 2;
				else if(npc == 10216 || npc == 6216 || npc == 1220 || npc == 6007 || npc == 6219 ||npc == 6220 || npc == 6221 || npc == 49 || npc == 4418) //Zamorak
					index = 3;
				if(index != -1) {
					killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]++;
					killer.getPacketSender().sendString((16216+index), ""+killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]);
				}
				return false;
			}
		},
		NOMAD(new int[]{3342, 3377}, new int[]{5839, 5877}, true, true, false, true, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here. If you'd like to leave, use the southern exit.");
				return false;
			}

			@Override
			public void leave(Player player) {
				if(player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(1890, 3177));
				player.restart();
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				if(npc.getId() == 8528) {
					Nomad.endFight(killer, true);
					return true;
				}
				return false;
			}
		},
		RECIPE_FOR_DISASTER(new int[]{1885, 1913}, new int[]{5340, 5369}, true, true, false, false, false, false) {

			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here. If you'd like to leave, use a portal.");
				return false;
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC npc) {
				RecipeForDisaster.handleNPCDeath(killer, npc);
				return true;
			}

			@Override
			public void leave(Player player) {
				if(player.getRegionInstance() != null)
					player.getRegionInstance().destruct();
				player.moveTo(new Position(3081, 3500));
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}
		},
		FREE_FOR_ALL_ARENA(new int[]{2755, 2876}, new int[]{5512, 5627}, true, true, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
				return false;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2815, 5511));
			}

			@Override
			public boolean canAttack(Player player, Player target) {
				if(target.getLocation() != FREE_FOR_ALL_ARENA) {
					player.getPacketSender().sendMessage("That player has not entered the dangerous zone yet.");
					player.getMovementQueue().reset();
					return false;
				}
				return true;
			}

			@Override
			public void enter(Player player) {
				if(player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK) {
					player.getPacketSender().sendInteractionOption("Attack", 2, true);
				}
			}

		},
		FREE_FOR_ALL_WAIT(new int[]{2755, 2876}, new int[]{5507, 5627}, false, false, true, false, false, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
				return false;
			}

			@Override
			public void onDeath(Player player) {
				player.moveTo(new Position(2815, 5511));
			}
		},
		COWS(new int[]{3239, 3270}, new int[]{3250, 3300}, false, true, true, false, false, true) {
			@Override
			public void logout(Player player) {
			player.moveTo(new Position(3038, 2785, 0));
				
			}
			@Override
			public void leave(Player player) {
			
			}
		},
		DEMON_ZONE(new int[] { 2305, 2316 }, new int[] { 3222, 3232 }, true, true, true, false, false, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONS.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONS[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONS[id]) {
                        return true;
                    }
                }
                return false;
            }
			
			@Override
			public void process(Player player) {
			if (!player.walkableInterfaceList.contains(41700))
					player.sendParallellInterfaceVisibility(41700, true);
			}

			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Vader Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				} else {
				player.getPacketSender().sendMessage(
						"<img=10>Use your Demon Kill Count at the shop for exclusive prizes!");
			}
		}
			
			@Override
			public boolean canTeleport(Player player) {
				player.getPacketSender().sendString((-1), "0");
				return true;
			}

			@Override
			public void onDeath(Player player) {
				leave(player);
			}

			@Override
			public void leave(Player p) {
				p.getPacketSender().sendString((-1), "0");
				for (int i = 0; i < p.getMinigameAttributes().getVaderZoneAttributes()
						.getKillcount().length; i++) {
					p.getMinigameAttributes().getVaderZoneAttributes().getKillcount()[i] = 0;
					p.getPacketSender().sendString((41704 + i), "0");
				}
			}

			@Override
			public boolean handleKilledNPC(Player killer, NPC n) {
				int npc = n.getId();
				if (npc == 4867) {
					killer.incrementVaderKills(1);
					killer.getPacketSender().sendString((41704), "" + killer.getVaderKills());
					killer.save();
				}
				return false;
			}
		},
		WARRIORS_GUILD(new int[]{2833, 2879}, new int[]{3531, 3559}, false, true, true, false, false, true) {
			@Override
			public void process(Player player) {
				int defender = WarriorsGuild.getDefender(player);
				if(player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, defender, 1);
				} else {			
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, 8851, 100);
				}
			}
			public void logout(Player player) {
				
				if(player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
					player.moveTo(new Position(2844, 3540, 2));
				}
				
			}
			@Override
			public void leave(Player player) {
				player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
				player.sendParallellInterfaceVisibility(54500, false);

			}
		},
		DEFENDERZ(new int[]{3008, 3043}, new int[]{5217, 5247}, true, true, true, true, true, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONZ.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONZ[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONZ[id]) {
                        return true;
                    }
                }
                return false;
            }
			@Override
			public void process(Player player) {
				int defender = Defenderz.getDefender(player);
				
				//	player.getPacketSender().sendWalkableInterface(42001, false);
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, defender, 1);
			}
			
			@Override
			public void logout(Player player) {
					player.moveTo(GameSettings.DEFAULT_POSITION);
			}
			
			@Override
			public void leave(Player player) {
				player.sendParallellInterfaceVisibility(54500, false);
				//player.getPacketSender().sendWalkableInterface(42001, true);
			}
			
			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Minigame Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				}else {
				player.sendMessage("<img=10>@blu@You will need your latest Defender on you to progress!");
				}
			}
		},
		RANGEZ(new int[]{3073, 3103}, new int[]{5186, 5214}, true, true, true, true, true, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONZ.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONZ[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONZ[id]) {
                        return true;
                    }
                }
                return false;
            }
			@Override
			public void process(Player player) {
				int range = Rangez.getRangez(player);

					player.getPA().sendString(54502, "Range Wep: ");
			//		player.getPacketSender().sendWalkableInterface(42001, false);
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, range, 1);
			}
			
			@Override
			public void logout(Player player) {
					player.moveTo(GameSettings.DEFAULT_POSITION);
			}
			
			@Override
			public void leave(Player player) {
				player.sendParallellInterfaceVisibility(54500, false);
		//		player.getPacketSender().sendWalkableInterface(42001, true);
			}
			
			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Minigame Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				}else {
				player.sendMessage("<img=10>@blu@You will need your latest Range Weapon on you to progress!");
				}
			}
		},
		MAGEZ(new int[]{3043, 3070}, new int[]{5182, 5214}, true, true, true, true, true, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONZ.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONZ[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONZ[id]) {
                        return true;
                    }
                }
                return false;
            }
			@Override
			public void process(Player player) {
				int mage = Magicz.getMagic(player);
				
					player.getPA().sendString(54502, "Magic Wep: ");
				//	player.getPacketSender().sendWalkableInterface(42001, false);
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, mage, 1);
			}
			
			@Override
			public void logout(Player player) {
					player.moveTo(GameSettings.DEFAULT_POSITION);
			}
			
			@Override
			public void leave(Player player) {
				player.sendParallellInterfaceVisibility(54500, false);
				//player.getPacketSender().sendWalkableInterface(42001, true);
			}
			
			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Minigame Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				}else {
				player.sendMessage("<img=10>@blu@You will need your latest Magic Weapon on you to progress!");
				}
			}
		},
		WEAPONZ(new int[]{3045, 3072}, new int[]{5217, 5250}, true, true, true, true, true, true) {
			public boolean hasOverpoweredWeapon(Player player) {
                for (int id = 0; id < GameSettings.AOE_WEAPONZ.length; id++) {
                    if (player.getInventory().contains(GameSettings.AOE_WEAPONZ[id]) || player.getEquipment()
                            .get(Equipment.WEAPON_SLOT).getId() == GameSettings.AOE_WEAPONZ[id]) {
                        return true;
                    }
                }
                return false;
            }
			@Override
			public void process(Player player) {
				int weapon = Weaponz.getWeaponz(player);
				
					player.getPA().sendString(54502, "Weaponz: ");
				//	player.getPacketSender().sendWalkableInterface(42001, true);
					player.sendParallellInterfaceVisibility(54500, true);
					player.getPacketSender().sendItemOnInterface(54503, weapon, 1);
			}
			
			@Override
			public void logout(Player player) {
					player.moveTo(GameSettings.DEFAULT_POSITION);
			}
			
			@Override
			public void leave(Player player) {
				player.sendParallellInterfaceVisibility(54500, false);
			//	player.getPacketSender().sendWalkableInterface(42001, true);
			}
			
			@Override
			public void enter(Player player) {
				if (hasOverpoweredWeapon(player)) {
					player.getPacketSender().sendMessage("@red@You can't bring AOE Weapons to the Minigame Area "
							+ player.getUsername() + "!");
					player.moveTo(GameSettings.DEFAULT_POSITION, true);
				}else {
				player.sendMessage("<img=10>@blu@You will need your latest Weapon on you to progress!");
				}
			}
		},
		PURO_PURO(new int[]{2556, 2630}, new int[]{4281, 4354}, false, true, true, false, false, true) {
		},
		FLESH_CRAWLERS(new int[]{2033, 2049}, new int[]{5178, 5197}, false, true, true, false, true, true) {
		},
		DEFAULT(null, null, false, true, true, true, true, true) {

		},;

		Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed) {
			this.x = x;
			this.y = y;
			this.multi = multi;
			this.summonAllowed = summonAllowed;
			this.followingAllowed = followingAllowed;
			this.cannonAllowed = cannonAllowed;
			this.firemakingAllowed = firemakingAllowed;
			this.aidingAllowed = aidingAllowed;
		}

		private int[] x, y;
		private boolean multi;
		private boolean resource;
		private boolean summonAllowed;
		private boolean followingAllowed;
		private boolean cannonAllowed;
		private boolean firemakingAllowed;
		private boolean aidingAllowed;

		public int[] getX() {
			return x;
		}

		public int[] getY() {
			return y;
		}

		public static boolean inMulti(Character gc) {
			if(gc.getLocation() == WILDERNESS) {
				int x = gc.getPosition().getX(), y = gc.getPosition().getY();
				if(x >= 3270 && x <= 3300 && y >= 3920 && y <= 3947) {
					return false;
			}
				if(x >= 3195 && x <= 3285 && y >= 3705 && y <= 3785 ||x >= 3120 && x <= 3350 && y >= 3865 && y <= 3903 || x >= 3250 && x <= 3350 && y >= 3905 && y <= 3960 || x >= 3650 && y <= 3538 || x >= 3020 && x <= 3055 && y >= 3684 && y <= 3711 || x >= 3150 && x <= 3195 && y >= 2958 && y <= 3003)
					return true;
			
			}
			
			
			/* 
			 * OUTSIDE_WILDY_MULTI
			 */
			int x = gc.getPosition().getX(), y = gc.getPosition().getY();
			if (x >= 3145 && x <= 3245 && y >= 3595 && y <= 3700 
					|| x >= 2700 && x <= 2730 && y >= 9800 && y <= 9829
					|| x >= 2463 && x <= 2480 && y >= 9669 && y <= 9686  // B00NY
					|| x >= 2520 && x <= 2594 && y >= 9800 && y <= 4932 // dragon_zone
					|| x >= 3080 && x <= 3120 && y >= 5520 && y <= 5550 
					|| x >= 2941 && x <= 2911 && y >= 2703 && y <= 2746 // vader_zone
					|| x >= 1901 && x <= 1872 && y >= 5420 && y <= 5394 // darth maul
					|| x >= 1979 && x <= 1924 && y >= 4859 && y <= 4804) { // Santa
				return true;
			}
			
		
			return gc.getLocation().multi;
		}
		
		
		public static boolean inResource(Character gc) {
			if(gc.getLocation() == WILDERNESS) {
				int x = gc.getPosition().getX(), y = gc.getPosition().getY();
				if(x >= 3270 && x <= 3300 && y >= 3920 && y <= 3947)
					return true;
			}
			
			return gc.getLocation().resource;
		}
		
		

		public boolean isSummoningAllowed() {
			return summonAllowed;
		}

		public boolean isFollowingAllowed() {
			return followingAllowed;
		}

		public boolean isCannonAllowed() {
			return cannonAllowed;
		}

		public boolean isFiremakingAllowed() {
			return firemakingAllowed;
		}

		public boolean isAidingAllowed() {
			return aidingAllowed;
		}

		public static Location getLocation(Entity gc) {
			for(Location location : Location.values()) {
				if(location != Location.DEFAULT)
					if(inLocation(gc, location))
						return location;
			}
			return Location.DEFAULT;
		}

		public static boolean inLocation(Entity gc, Location location) {
			if(location == Location.DEFAULT) {
				if(getLocation(gc) == Location.DEFAULT)
					return true;
				else
					return false;
			}
			/*if(gc instanceof Player) {
				Player p = (Player)gc;
				if(location == Location.TRAWLER_GAME) {
					String state = FishingTrawler.getState(p);
					return (state != null && state.equals("PLAYING"));
				} else if(location == FIGHT_PITS_WAIT_ROOM || location == FIGHT_PITS) {
					String state = FightPit.getState(p), needed = (location == FIGHT_PITS_WAIT_ROOM) ? "WAITING" : "PLAYING";
					return (state != null && state.equals(needed));
				} else if(location == Location.SOULWARS) {
					return (SoulWars.redTeam.contains(p) || SoulWars.blueTeam.contains(p) && SoulWars.gameRunning);
				} else if(location == Location.SOULWARS_WAIT) {
					return SoulWars.isWithin(SoulWars.BLUE_LOBBY, p) || SoulWars.isWithin(SoulWars.RED_LOBBY, p);
				}
			}
			 */
			return inLocation(gc.getPosition().getX(), gc.getPosition().getY(), location);
		}

		public static boolean inLocation(int absX, int absY, Location location) {
			int checks = location.getX().length - 1;
			for(int i = 0; i <= checks; i+=2) {
				if(absX >= location.getX()[i] && absX <= location.getX()[i + 1]) {
					if(absY >= location.getY()[i] && absY <= location.getY()[i + 1]) {
						return true;
					}
				}
			}
			return false;
		}

		public void process(Player player) {

		}

		public boolean canTeleport(Player player) {
			return true;
		}

		public void login(Player player) {

		}

		public void enter(Player player) {

		}

		public void leave(Player player) {

		}

		public void logout(Player player) {

		}

		public void onDeath(Player player) {

		}

		public boolean handleKilledNPC(Player killer, NPC npc) {
			return false;
		}

		public boolean canAttack(Player player, Player target) {
			return false;
		}

		/** SHOULD AN ENTITY FOLLOW ANOTHER ENTITY NO MATTER THE DISTANCE BETWEEN THEM? **/
		public static boolean ignoreFollowDistance(Character character) {
			Location location = character.getLocation();
			return location == Location.FIGHT_CAVES || location == Location.RECIPE_FOR_DISASTER || location == Location.NOMAD;
		}
	}

	public static void process(Character gc) {
		Location newLocation = Location.getLocation(gc);
		if(gc.getLocation() == newLocation) {
			if(gc.isPlayer()) {
				Player player = (Player) gc;
				gc.getLocation().process(player);
				if(Location.inMulti(player)) {
					if(player.getMultiIcon() != 1)
						player.getPacketSender().sendMultiIcon(1);
				} else if(player.getMultiIcon() == 1)
					player.getPacketSender().sendMultiIcon(0);
			}
		} else {
			Location prev = gc.getLocation();
			if(gc.isPlayer()) {
				Player player = (Player) gc;
				if(player.getMultiIcon() > 0)
					player.getPacketSender().sendMultiIcon(0);
				if(player.walkableInterfaceList.size() > 0)
					//player.getPacketSender().sendWalkableInterface(-1);
                                    player.resetInterfaces();
			/*	if (player.getPlayerInteractingOption() != PlayerInteractingOption.NONE)
					player.getPacketSender().sendInteractionOption("Dice with", 2, true);*/
			}
			gc.setLocation(newLocation);
			if (gc.isPlayer()) {
				prev.leave(((Player) gc));
				gc.getLocation().enter(((Player) gc));
			}
		}
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		if (playerX == objectX && playerY == objectY)
			return true;
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY
						|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY
						|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY
						|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean goodDistance(Position pos1, Position pos2, int distanceReq) {
		if(pos1.getZ() != pos2.getZ())
			return false;
		return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distanceReq);
	}

	public static int distanceTo(Position position, Position destination,
			int size) {
		final int x = position.getX();
		final int y = position.getY();
		final int otherX = destination.getX();
		final int otherY = destination.getY();
		int distX, distY;
		if (x < otherX)
			distX = otherX - x;
		else if (x > otherX + size)
			distX = x - (otherX + size);
		else
			distX = 0;
		if (y < otherY)
			distY = otherY - y;
		else if (y > otherY + size)
			distY = y - (otherY + size);
		else
			distY = 0;
		if (distX == distY)
			return distX + 1;
		return distX > distY ? distX : distY;
	}
}
