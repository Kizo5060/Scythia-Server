package com.ruseps.world.content.new_raids_system.raids_system;

import com.ruseps.GameSettings;
import com.ruseps.model.*;
import com.ruseps.model.Locations.Location;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.new_raids_system.InterfaceConstants;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.player.Player;

public class Raids {
	
    private Player player;

    private com.ruseps.world.content.new_raids_system.raids_party.RaidsParty party;

    public Raids(Player player) {
        this.player = player;
    }

    public void forceEnd() {
        sendMessageToParty("The party has ended due to a server error. Please contact an administrator with this.");

        for(Player partyMember : party.getPlayers()) {
            if(partyMember == null)
                continue;
            teleportToEntrance();
        }
    }

    public void sendMessageToParty(String message) {
        for(Player partyMember : party.getPlayers()) {
            if(partyMember == null)
                continue;
            partyMember.sendMessage("<col=830303>"+message);
        }
    }

    public void teleportToEntrance() {
        TeleportHandler.teleportPlayer(player, new Position(2769, 3285, 0), player.getSpellbook().getTeleportType());
    }

    public void moveToEntrance() {
    	player.moveTo(new Position(2769, 3285));
    	player.getPacketSender().sendMessage("@red@Sadly a member of your party has died, and your party must start over.");
    }

    public void entranceProcess() {
        processParty();
    }

    public void bossRoomProcess() {
        processParty();
    }

    public void processParty() {
        if(party == null)
            return;
        
            if (party.getPlayers().size() <= 1) {
            }
        

        int memberIndex = 0;
        for(Player partyMember : party.getPlayers()) {
            boolean kickMember = false;
            if(partyMember == null) {
                kickMember = true;
            } else if(partyMember.getLocation() != Locations.Location.RAIDS_ONE_ENTRANCE
                    || partyMember.getLocation() != Locations.Location.RAIDS_ONE_PHASE_ONE
                    || partyMember.getLocation() != Locations.Location.RAIDS_ONE_PHASE_TWO
                    || partyMember.getLocation() != Locations.Location.RAIDS_ONE_PHASE_THREE
                    || partyMember.getLocation() != Locations.Location.RAIDS_TWO_ENTRANCE
                    || partyMember.getLocation() != Locations.Location.RAIDS_TWO_PHASE_TWO
                    || partyMember.getLocation() != Locations.Location.RAIDS_TWO_PHASE_THREE
                    || partyMember.getLocation() != Locations.Location.RAIDS_TWO_PHASE_ONE
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_ENTRANCE
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_PHASE_ONE
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_PHASE_TWO
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_PHASE_THREE
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_PHASE_FOUR
                    || partyMember.getLocation() != Locations.Location.RAIDS_THREE_PHASE_FIVE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FOUR_ENTRANCE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FOUR_PHASE_ONE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FOUR_PHASE_TWO
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FOUR_PHASE_THREE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FIVE_ENTRANCE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FIVE_PHASE_ONE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FIVE_PHASE_TWO
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FIVE_PHASE_THREE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_FIVE_PHASE_FOUR
                	|| partyMember.getLocation() != Locations.Location.RAIDS_SIX_ENTRANCE
                	|| partyMember.getLocation() != Locations.Location.RAIDS_SIX_PHASE_ONE
                    || partyMember.getLocation() != Locations.Location.RAIDS_SEVEN_ENTRANCE
                    || partyMember.getLocation() != Locations.Location.RAIDS_SEVEN_PHASE_ONE) {

                kickMember = true;
            } else if(!partyMember.isRegistered()) {
                kickMember = true;
            }
            if(kickMember) {
                forceOut(memberIndex);
            }
            memberIndex++;
        }
    }
    
    public void attemptLeave() {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Leave",
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
             
                switch(option) {
                case 1:

                if (party != null) {
                    party.getPlayers().remove(player);
                }
                    refreshAll();
                    player.getPacketSender().sendInterfaceRemoval();

                    removeRaidsInterface();
                    break;
                    case 2:
                        player.getPacketSender().sendInterfaceRemoval();
                        break;
                }
                return false;
            }

        });
    }

    public void attemptKick(Player partyMember) {
        if(partyMember == null) {
            return;
        }

        if(partyMember.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage("@red@You can't kick yourself. Leave the party instead!");
            return;
        }

        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Kick "+partyMember.getName(),
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        partyMember.getRaidsOne().getRaidsConnector().leave(player.getLocation() == Locations.Location.RAIDS_ONE_PHASE_ONE);
                        break;
                }
                return false;
            }

        });
    }

    public void leave(boolean teleport) {
        if(party == null)
            return;
        
        
        if(teleport)
        TeleportHandler.teleportPlayer(player, new Position(1611, 3808, 0), player.getSpellbook().getTeleportType());
        
        player.getPacketSender().sendCameraNeutrality();
		player.getMovementQueue().setLockMovement(false);
		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
		player.getPacketSender().sendDungeoneeringTabIcon(false);
		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);    
        refreshAll();
    }

    
    public void leaveRaidsOneEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_ONE_ENTRANCE) {
    	    player.getPacketSender().sendCameraNeutrality();
            player.getMovementQueue().setLockMovement(false);
            player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
            player.getPacketSender().sendDungeoneeringTabIcon(false);
            player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
            player.getEquipment().refreshItems();
            refreshAll();
    	}
    }
       
    public void leaveRaidsTwoEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_TWO_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsThreeEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_THREE_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsFourEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_FOUR_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsFiveEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_FIVE_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsSixEntrance() {
    	if (player.getLocation() == Locations.Location.RAIDS_SIX_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_SIX_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsSevenEntrance() {
        if (player.getLocation() == Locations.Location.RAIDS_SEVEN_ENTRANCE) {
            player.getPacketSender().sendCameraNeutrality();
            player.getMovementQueue().setLockMovement(false);
            player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 27601);
            player.getPacketSender().sendDungeoneeringTabIcon(false);
            player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
            player.getEquipment().refreshItems();
            refreshAll();
        }
    }
    public void leaveOther(Player player) {
    	player.getPacketSender().sendCameraNeutrality();
		player.getMovementQueue().setLockMovement(false);
		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
		player.getPacketSender().sendDungeoneeringTabIcon(false);
		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
		player.moveTo(GameSettings.DEFAULT_POSITION.copy());
		player.getEquipment().refreshItems();
		refreshAll();
    }
    public void removeRaidsInterface() {
    	if (player.getLocation() == Locations.Location.RAIDS_ONE_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26600);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		party.getPlayers().remove(player);
    		player.getPacketSender().sendMessage("<img=10>@blu@Please use ::raids to rejoin :)");
    		refreshAll();
    	}
    }
    
    public void leaveRaidsOne() {
    	if (player.getLocation() == Locations.Location.RAIDS_ONE_ENTRANCE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.DEFAULT_POSITION.copy());
    		player.getEquipment().refreshItems();
    		refreshAll();
    	}
    }
    
    public void leaveRaidsTwo() {
    	if (player.getLocation() == Locations.Location.RAIDS_TWO_PHASE_THREE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_TWO_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsThree() {
    	if (player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_FIVE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_THREE_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsFour() {
    	if (player.getLocation() == Locations.Location.RAIDS_FOUR_PHASE_THREE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_FOUR_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsFive() {
    	if (player.getLocation() == Locations.Location.RAIDS_FIVE_PHASE_FOUR) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_FIVE_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsSix() {
    	if (player.getLocation() == Locations.Location.RAIDS_SIX_PHASE_ONE) {
    		player.getPacketSender().sendCameraNeutrality();
    		player.getMovementQueue().setLockMovement(false);
    		player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
    		player.getPacketSender().sendDungeoneeringTabIcon(false);
    		player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
    		player.moveTo(GameSettings.RAIDS_SIX_LOBBY.copy());
    		player.getEquipment().refreshItems();
    		party = null;
    		refreshAll();
    	}
    }
    public void leaveRaidsSeven() {
        if (player.getLocation() == Locations.Location.RAIDS_SEVEN_PHASE_ONE) {
            player.getPacketSender().sendCameraNeutrality();
            player.getMovementQueue().setLockMovement(false);
            player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 26601);
            player.getPacketSender().sendDungeoneeringTabIcon(false);
            player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
            player.moveTo(GameSettings.RAIDS_SEVEN_LOBBY.copy());
            player.getEquipment().refreshItems();
            party = null;
            refreshAll();
        }
    }
    public void forceOut(int memberIndex) {
        if(party == null)
            return;

        if(party.getPlayers() != null) {

            party.getPlayers().remove(memberIndex);

            if(party.getPlayers().size() == 0) {
                //destroy();
                return;
            }
        }

        refreshAll();
    }



    public void enter(Player player) {
        displayParty();
    }

    public void displayParty() {
        player.getPacketSender().sendTabInterface(GameSettings.ACHIEVEMENT_TAB, InterfaceConstants.TAB_INTERFACE_ID);
        player.getPacketSender().sendDungeoneeringTabIcon(false);
        player.getPacketSender().sendTab(GameSettings.ACHIEVEMENT_TAB);
        displayMembers();
    }

    public void displayMembers() {
        player.getPacketSender().sendString(58002, "@whi@Scythia's Raiding Party: " + (party == null ? "0" : party.getPlayers().size()));

        int id = 58017;
        for(int i = 0; i < 12; i++) {

            Player partyMember = party != null && party.getPlayers().size() > i ? party.getPlayers().get(i) : null;

            player.getPacketSender().sendString(id++, partyMember == null ? "" : partyMember.getName());

            id++;
        }
    }

    public boolean isButton(int button) {
        if(!inArea()) {
           return false;
        }

        if(button >= 19480 && button <= 19524) {
            if(party == null) {
                return true;
            }

            int memberId = (19524 - button) / 4;

            Player partyMember = party.getPlayers().size() > memberId ? party.getPlayers().get(memberId) : null;

            if(partyMember != null) {
                attemptKick(partyMember);
            }
            return true;
        }
        switch(button) {
        }
        return false;
    }

    public void attemptInvite(Player toInvite) {
        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Invite "+toInvite.getName(),
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                player.getPA().closeDialogueOnly();
                switch(option) {
                    case 1:
                        invite(toInvite);
                        player.getPacketSender().sendInterfaceRemoval();
                        break;
                    case 2:
                        player.getPacketSender().sendInterfaceRemoval();
                        break;
                }
                return false;
            }

        });
    }

    public void invite(Player toInvite) {
        if(toInvite == null) {
            player.sendMessage("@red@You have attempted to invite an invalid player.");
            return;
        }

        if(!toInvite.getRaidsOne().getRaidsConnector().inEntrance() || !toInvite.getRaidsOne().getRaidsConnector().inEntrance2()) {
            player.sendMessage(toInvite.getName()+" isn't at the .");
            return;
        }

        DialogueManager.start(toInvite, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public String[] dialogue() {
                return new String[] {
                        "Join "+player.getName()+"'s Raiding Party",
                        "Cancel",
                };
            }

            @Override
            public boolean action(int option) {
                toInvite.getPA().closeDialogueOnly();
                player.getPacketSender().sendInterfaceRemoval();
                switch(option) {
                    case 1:
                        if(player == null) {
                            toInvite.sendMessage("@red@The party leader that invited you has now logged out.");
                            return false;
                        }

                        toInvite.getRaidsPartyConnector().getRaidsPartyConnector().invite(player);
                        player.getPacketSender().sendInterfaceRemoval();
                        break;
                    case 2:
                        player.getPacketSender().sendInterfaceRemoval();
                        break;
                }
                return false;
            }

        });
    }

    public void refreshAll() {
        if(this.party != null) {
            for(Player partyMember : party.getPlayers()) {
                if(partyMember != null) {
                    partyMember.getRaidsOne().getRaidsConnector().displayMembers();
                }
            }
        }
    }

    public boolean inEntrance() {
        return player.getLocation() == Locations.Location.RAIDS_ONE_ENTRANCE;
    }
    
    public boolean inEntrance2() {
        return player.getLocation() == Locations.Location.RAIDS_TWO_ENTRANCE;
    }
    public boolean inEntrance3() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_ENTRANCE;
    }
    public boolean inEntrance4() {
        return player.getLocation() == Locations.Location.RAIDS_FOUR_ENTRANCE;
    }
    public boolean inEntrance5() { 
        return player.getLocation() == Locations.Location.RAIDS_FIVE_ENTRANCE;
    }
    public boolean inEntrance6() { 
        return player.getLocation() == Locations.Location.RAIDS_SIX_ENTRANCE;}
        public boolean inEntrance7() { return player.getLocation() == Locations.Location.RAIDS_SEVEN_ENTRANCE;}


    public boolean inPhaseOne() {
        return player.getLocation() == Locations.Location.RAIDS_ONE_PHASE_ONE;
    }

    public boolean inPhaseTwo() {
        return player.getLocation() == Locations.Location.RAIDS_ONE_PHASE_TWO;
    }
    
    public boolean inPhaseThree() {
        return player.getLocation() == Locations.Location.RAIDS_ONE_PHASE_THREE;
    }
    
    public boolean inRaidsTwo() {
        return player.getLocation() == Locations.Location.RAIDS_TWO_PHASE_ONE;
    }
    
    public boolean inRaidsTwoP2() {
        return player.getLocation() == Locations.Location.RAIDS_TWO_PHASE_TWO;
    }
    
    public boolean inRaidsTwoP3() {
        return player.getLocation() == Locations.Location.RAIDS_TWO_PHASE_THREE;
    }
    public boolean inRaidsThree() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_ONE;
    }
    
    public boolean inRaidsThreeP2() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_TWO;
    }
    
    public boolean inRaidsThreeP3() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_THREE;
    }
    
    public boolean inRaidsThreeP4() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_FOUR;
    }
    public boolean inRaidsThreeP5() {
        return player.getLocation() == Locations.Location.RAIDS_THREE_PHASE_FIVE;
    }
    
    public boolean inRaidsFourP1() {
        return player.getLocation() == Locations.Location.RAIDS_FOUR_PHASE_ONE;
    }
    public boolean inRaidsFourP2() {
        return player.getLocation() == Locations.Location.RAIDS_FOUR_PHASE_TWO;
    }
    public boolean inRaidsFourP3() {
        return player.getLocation() == Locations.Location.RAIDS_FOUR_PHASE_THREE;
    }
    public boolean inRaidsFiveP1() {
        return player.getLocation() == Locations.Location.RAIDS_FIVE_PHASE_ONE;
    }
    public boolean inRaidsFiveP2() {
        return player.getLocation() == Locations.Location.RAIDS_FIVE_PHASE_TWO;
    }
    public boolean inRaidsFiveP3() {
        return player.getLocation() == Locations.Location.RAIDS_FIVE_PHASE_THREE;
    }
    public boolean inRaidsFiveP4() {
        return player.getLocation() == Locations.Location.RAIDS_FIVE_PHASE_FOUR;
    }
    public boolean inRaidsSixP1() {
        return player.getLocation() == Locations.Location.RAIDS_SIX_PHASE_ONE;
    }
    public boolean inRaidsSevenP1() {
        return player.getLocation() == Locations.Location.RAIDS_SEVEN_PHASE_ONE;
    }
 

    public boolean inArea() {
        return inEntrance() || inPhaseOne() || inPhaseTwo() || inPhaseThree() || inRaidsTwo() || inEntrance2() || inRaidsTwoP2() || inRaidsTwoP3() || inEntrance3() || inRaidsThreeP2() || inRaidsThreeP3() || inRaidsThreeP4() || inRaidsThreeP5() || inEntrance4() || inEntrance5() || inRaidsFourP1() || inRaidsFourP2() || inRaidsFourP3() || inRaidsFiveP1() || inRaidsFiveP2() || inRaidsFiveP3() || inRaidsFiveP4() || inEntrance6() || inRaidsSixP1()
                || inEntrance7() || inRaidsSevenP1();
    }

}
