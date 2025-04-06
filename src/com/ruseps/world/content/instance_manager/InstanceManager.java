package com.ruseps.world.content.instance_manager;

import java.time.Duration;
import java.util.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NpcDefinition;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.restrictions.AttackRequirement;
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.overlaytimer.OverlayTimer;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class InstanceManager {
	
	public int kcAllowed = 0;
	
	public int currKc =0;
	
	private SpawnType kcAmount;
	
	public SpawnType spawnType;

	private InstanceTime time;

	private InstanceNpcs npcToSelect;

	public LocalDateTime timeSelected = LocalDateTime.MIN;

	public boolean inInstance;

	public Map<Position ,NPC> npcMap = new HashMap<>();
	
	public void openInterface(Player player) {
		player.getPacketSender().sendString(58716, "");
		player.getPacketSender().sendString(58717, "");
		player.getPacketSender().sendString(58718, "");
		player.getPacketSender().sendString(58926, "");
		player.getPacketSender().sendNpcOnInterface(58927, -1, 0);
		sendNpcs(player);

		player.getInstanceManager().setNpcToSelect(InstanceNpcs.values()[0]);
		player.getInstanceManager().refreshNpcDisplayed(player);

		player.getPacketSender().sendInterface(58705);

		player.sendMessage("<shad=1>@red@ Welcome to Instance Manager,Choose what types of spawns you want!");
		player.getInstanceManager().setTime(InstanceTime.TIME_30MIN);
	}

	public void sendNpcs(Player player) {
		int start = 58720;
		for (InstanceNpcs npcs : InstanceNpcs.values()) {
			player.getPacketSender().sendString(start, Misc.capitalize(npcs.name().replace("_", " ")));
			start++;
		}
	}

	public void spawnButtonClick(Player player) {
		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Spawns 4x4 Instance", "Spawn 5x5 Instance" };
			}

			@Override
			public int npcId() {
				return -1;
			}

			@Override
			public void specialAction() {
				player.setDialogueActionId(5000);
			}
			@Override
			public boolean closeInterface() {
				return false;
			}
		});
	}

	public void KCButtonClick(Player player) {
		DialogueManager.start(player, new Dialogue() {
			@Override
			public boolean closeInterface() {
				return false; //check clickactionbutton
			}

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "500 Spawns", "1000 Spawns" };
			}

			@Override
			public int npcId() {
				return -1;
			}

			@Override
			public void specialAction() {
				player.setDialogueActionId(5001);
			}
			
		});
	}

	public void teleportToInstance(Player player, SpawnType type) {
		player.getInstanceManager().currKc = 0;
		player.getInstanceManager().kcAllowed = 0;
		if (player.getInstanceManager().spawnType == SpawnType.SPAWN_4X4) {
			if (player.getInstanceManager().getKc() == SpawnType.SPAWN_500) {
				if(!(player.getInventory().contains(19990, 15) && player.getInventory().contains(19994, 15_000))) {
					player.sendMessage("@red@<shad=1>You need 15 Instance tokens and 15k NexArch coins enter this instance");
					player.getPA().closeAllWindows();
					return;
				}			
				if (player.getInventory().contains(19990, 15) && player.getInventory().contains(19994, 15_000)) {
					player.getInventory().delete(19994, 15_000);
					player.getInventory().delete(19990, 15);
					player.getInstanceManager().kcAllowed  = 500;
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 1800);

					TeleportHandler.teleportPlayer(player, new Position(2845, 5090, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player, false);
				}
			} else if (player.getInstanceManager().getKc() == SpawnType.SPAWN_1000) {
				if(!(player.getInventory().contains(19990, 30) && player.getInventory().contains(19994, 30_000))) {
					player.sendMessage("@red@<shad=1>You need 30 Instance tokens and 30k NexArch coins enter this instance");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getInventory().contains(19990, 30) && player.getInventory().contains(19994, 30_000)) {
					player.getInventory().delete(19990, 30);
					player.getInventory().delete(19994,30_000);
					player.getInstanceManager().kcAllowed  = 1000;
				
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}
					
					OverlayTimer.sendOverlayTimer(player, 1206, 3600);

					TeleportHandler.teleportPlayer(player, new Position(2845, 5090, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player,false);
				}
			}
		} else if (player.getInstanceManager().spawnType == SpawnType.SPAWN_5X5) {
			if (player.getInstanceManager().getKc() == SpawnType.SPAWN_500) {
				if(!(player.getInventory().contains(19990, 45) && player.getInventory().contains(19994, 45_000))) {
					player.sendMessage("@red@<shad=1>You need 45 Instance tokens and 45k NexArch coins enter this instance");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getInventory().contains(19990, 45) && player.getInventory().contains(19994, 45_000)) {
					player.getInventory().delete(19990, 45);
					player.getInventory().delete(19994, 45_000);
					player.getInstanceManager().kcAllowed  = 500;
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 1800);

					TeleportHandler.teleportPlayer(player, new Position(2845, 5090, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player, true);
				}
			} else if (player.getInstanceManager().getKc() == SpawnType.SPAWN_1000) {
				if(!(player.getInventory().contains(19990, 60) && player.getInventory().contains(19994, 60_000))) {
					player.sendMessage("@red@<shad=1>You need 60 Instance tokens and 60k NexArch coins enter this instance");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getInventory().contains(19990, 60) && player.getInventory().contains(19994, 60_000)) {
					player.getInventory().delete(19990, 60);
					player.getInventory().delete(19994,60_000);
					player.getInstanceManager().kcAllowed = 1000;
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 3600);

					TeleportHandler.teleportPlayer(player, new Position(2845, 5090, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player, true);
				}
			}
		}
	}

	public void startInstance(Player player ,boolean spawnType) {
	    player.sendMessage("@red@<shad=1>Welcome to your AOE Instance");
	    player.sendMessage("@red@<shad=1>You will be fighting @blu@"
	            + NpcDefinition.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getName() + "@red@ for @blu@"
	            + player.getInstanceManager().kcAllowed + "@red@ Spawns, have fun!");
	    player.getInstanceManager().inInstance = true;
	    TaskManager.submit(new Task(5) {
			@Override
			protected void execute() {
				
				if(!spawnType) {
					
					for(int i = 0; i < 8; i+=2) {
						for(int j =0 ; j < 8 ; j+=2) {
							player.getInstanceManager().npcMap.put(new Position(2845 + i, 5080 + j, player.getIndex() * 4) ,new NPC(
							player.getInstanceManager().getNpcToSelect().getNpcId(),
							new Position(2845 + i, 5080 + j, player.getIndex() * 4)));
						}
					}
				}
				else {
			
					for(int i = 0; i < 10; i+=2) {
						for(int j =0 ; j < 10 ; j+=2) {
							player.getInstanceManager().npcMap.put(new Position(2845 + i, 5080 + j, player.getIndex() * 4) ,new NPC(
							player.getInstanceManager().getNpcToSelect().getNpcId(),
							new Position(2845 + i, 5080 + j, player.getIndex() * 4)));
						}
					}
				}
				
				for(Map.Entry<Position, NPC> npc : player.getInstanceManager().npcMap.entrySet()) {
					World.register(npc.getValue());	
				}
				stop();
			}
		});
		
		TaskManager.submit(new Task(1, player, false) {
            @Override
            protected void execute() {
                if (player.getInstanceManager().kcAllowed < player.getInstanceManager().currKc) {

                        player.getInstanceManager().endInstance(player, false);
                        this.stop();                
                }
            }
        });
		
	}

	public void endInstance(Player player, boolean death) {
		List<NPC> instance = World.getNpcs().stream()
				.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
				.collect(Collectors.toList());

		for (NPC fl : instance) {
			if(player.getSummoning().getFamiliar() != null && player.getSummoning().getFamiliar().getSummonNpc() != null) {
				if(player.getSummoning().getFamiliar().getSummonNpc().getId() == fl.getId()) {
					continue;
				}
				if(fl.getPosition().getZ() == 0 || fl.getPosition().getZ() == 4) {
                    continue;
                }
			}
			World.deregister(fl);
		}
		player.getInstanceManager().npcMap.clear();
	//	player.sendMessage("@red@<shad=1>You have no respawns left in the instance");
		if(player.getInstanceManager().inInstance)
			player.sendMessage("@red@<shad=1>Your instance is over!");
		OverlayTimer.stopOverlayTimer(player, 1206);
		player.getInstanceManager().inInstance = false;
		
		player.getInstanceManager().setKc(SpawnType.SPAWN_4X4);
		if (death) {
			player.moveTo(GameSettings.DEFAULT_POSITION);
		}
	}

	public void respawnNpc(Player player, NPC npc) {
		if(player.getInstanceManager().currKc + 1 > player.getInstanceManager().kcAllowed) {
			player.getInstanceManager().endInstance(player, true);
		}
	    if (!player.getInstanceManager().inInstance ){
	        return;
	    }
	    World.deregister(npc);
	    
	    for(Map.Entry<Position, NPC> entry : player.getInstanceManager().npcMap.entrySet()) {
	    	if(entry.getValue() == null) {
	    		entry.setValue(new NPC(npc.getId() ,entry.getKey()));
	    	}
	    }
	}

	public void refreshNpcDisplayed(Player player) {
		player.getPacketSender()
				.sendString(58716,
						"Npc killcount: @gre@"
								+ player.getNpcKillCount(player.getInstanceManager().getNpcToSelect().getNpcId()))
				.sendString(58717,
						"Npc hitpoints: @gre@" + NpcDefinition
								.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getHitpoints())
				.sendString(58718,
						"Npc level: @gre@" + NpcDefinition
								.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getCombatLevel())
				.sendString(58719, "Npc MaxHit: @gre@" + NpcDefinition
						.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getMaxHit())
				.sendString(58926,
						NpcDefinition.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getName());
		player.getPacketSender().sendNpcOnInterface(58927, player.getInstanceManager().getNpcToSelect().getNpcId(), 0);
		sendDrops(player, player.getInstanceManager().getNpcToSelect().getNpcId());
	}

	private void sendDrops(Player player, int npcId) {
		player.getPacketSender().resetItemsOnInterface(58936, 100);
		try {
			NPCDrops drops = NPCDrops.forId(npcId);
			if (drops == null) {
				return;
			}
			for (int i = 0; i < drops.getDropList().length; i++) {

				player.getPacketSender().sendItemOnInterface(58936, drops.getDropList()[i].getId(), i,
						drops.getDropList()[i].getItem().getAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public InstanceTime getTime() {
		return time;
	}

	public void setTime(InstanceTime time) {
		this.time = time;
	}
	
	public SpawnType getKc() {
		return kcAmount;
	}
	public void setKc(SpawnType kc) {
		this.kcAmount = kc;
	}
	public InstanceNpcs getNpcToSelect() {
		return npcToSelect;
	}

	public void setNpcToSelect(InstanceNpcs npcToSelect) {
		this.npcToSelect = npcToSelect;
	}
}
