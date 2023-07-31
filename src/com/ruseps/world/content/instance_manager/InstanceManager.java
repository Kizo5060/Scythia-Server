package com.ruseps.world.content.instance_manager;

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
import com.ruseps.world.content.dialogue.Dialogue;
import com.ruseps.world.content.dialogue.DialogueExpression;
import com.ruseps.world.content.dialogue.DialogueManager;
import com.ruseps.world.content.dialogue.DialogueType;
import com.ruseps.world.content.overlaytimer.OverlayTimer;
import com.ruseps.world.content.transportation.TeleportHandler;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class InstanceManager {

	private InstanceTime time;

	private InstanceNpcs npcToSelect;

	public LocalDateTime timeSelected = LocalDateTime.MIN;

	public boolean inInstance;

	public NPC npcToSpawn1, npcToSpawn2, npcToSpawn3, npcToSpawn4, npcToSpawn5, npcToSpawn6, npcToSpawn7, npcToSpawn8;

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

		player.sendMessage("Time selected at 30 Minutes by default.");
		player.getInstanceManager().setTime(InstanceTime.TIME_30MIN);
	}

	public void sendNpcs(Player player) {
		int start = 58720;
		for (InstanceNpcs npcs : InstanceNpcs.values()) {
			player.getPacketSender().sendString(start, Misc.capitalize(npcs.name().replace("_", " ")));
			start++;
		}
	}

	public void teleportButtonClick(Player player) {
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
				return new String[] { "Use Instance Token", "Consume AOE Voucher" };
			}

			@Override
			public int npcId() {
				return -1;
			}

			@Override
			public void specialAction() {
				player.setDialogueActionId(5000);
			}
		});
	}

	public void timeButtonClick(Player player) {
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
				return new String[] { "30 Minutes", "1 Hour" };
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

	public void teleportToInstance(Player player, PaymentType type) {
		if (type == PaymentType.INSTANCE_TOKEN) {
			if (player.getInstanceManager().getTime() == InstanceTime.TIME_30MIN) {
				if (player.getInventory().contains(19990, 25_000)) {
					player.getInventory().delete(19990, 25_000);
					player.getInstanceManager().timeSelected = LocalDateTime.now().plusMinutes(30);
					
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 1800);

					TeleportHandler.teleportPlayer(player, new Position(3421, 4777, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player);
				}
			} else if (player.getInstanceManager().getTime() == InstanceTime.TIME_1H) {
				if (player.getInventory().contains(19990, 50_000)) {
					player.getInventory().delete(19990, 50_000);
					player.getInstanceManager().timeSelected = LocalDateTime.now().plusHours(1);
					
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}
					
					OverlayTimer.sendOverlayTimer(player, 1206, 3600);

					TeleportHandler.teleportPlayer(player, new Position(3421, 4777, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player);
				}
			}
		} else if (type == PaymentType.AOE_VOUCHERS) {
			if (player.getInstanceManager().getTime() == InstanceTime.TIME_30MIN) {
				if (player.getInventory().contains(5606, 1)) {
					player.getInventory().delete(5606, 1);
					player.getInstanceManager().timeSelected = LocalDateTime.now().plusMinutes(30);
					
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 1800);

					TeleportHandler.teleportPlayer(player, new Position(3421, 4777, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player);
				}
			} else if (player.getInstanceManager().getTime() == InstanceTime.TIME_1H) {
				if (player.getInventory().contains(5606, 2)) {
					player.getInventory().delete(5606, 2);
					player.getInstanceManager().timeSelected = LocalDateTime.now().plusHours(1);
					
					List<NPC> instance = World.getNpcs().stream()
							.filter(t -> t != null && t.getPosition().getZ() == player.getIndex() * 4)
							.collect(Collectors.toList());

					for (NPC fl : instance) {
						World.deregister(fl);
					}

					OverlayTimer.sendOverlayTimer(player, 1206, 3600);

					TeleportHandler.teleportPlayer(player, new Position(3421, 4777, player.getIndex() * 4),
							player.getSpellbook().getTeleportType());
					player.getInstanceManager().startInstance(player);
				}
			}
		}
	}

	public void startInstance(Player player) {
		player.sendMessage("Welcome to your AOE Instance");
		player.sendMessage("You will be fighting "
				+ NpcDefinition.forId(player.getInstanceManager().getNpcToSelect().getNpcId()).getName() + " for "
				+ player.getInstanceManager().getTime() + " have fun!");
		player.getInstanceManager().inInstance = true;

		TaskManager.submit(new Task(5) {
			@Override
			protected void execute() {

				player.getInstanceManager().npcToSpawn1 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3418, 4773, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn2 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3418, 4774, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn3 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3423, 4773, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn4 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3424, 4774, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn5 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3423, 4780, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn6 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3424, 4779, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn7 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3418, 4780, player.getIndex() * 4));

				player.getInstanceManager().npcToSpawn8 = new NPC(
						player.getInstanceManager().getNpcToSelect().getNpcId(),
						new Position(3418, 4779, player.getIndex() * 4));

				World.register(player.getInstanceManager().npcToSpawn1);
				World.register(player.getInstanceManager().npcToSpawn2);
				World.register(player.getInstanceManager().npcToSpawn3);
				World.register(player.getInstanceManager().npcToSpawn4);
				World.register(player.getInstanceManager().npcToSpawn5);
				World.register(player.getInstanceManager().npcToSpawn6);
				World.register(player.getInstanceManager().npcToSpawn7);
				World.register(player.getInstanceManager().npcToSpawn8);
				stop();
			}
		});
	}

	public void endInstance(Player player, boolean death) {
		List<NPC> instance = World.getNpcs().stream()
				.filter(t -> t != null && t.getPosition().getZ() == (player.getIndex() * 4))
				.collect(Collectors.toList());

		for (NPC fl : instance) {
			if(player.getSummoning().getFamiliar() != null && player.getSummoning().getFamiliar().getSummonNpc() != null) {
				if(player.getSummoning().getFamiliar().getSummonNpc().getId() == fl.getId()) {
					continue;
				}
				if(fl.getPosition().getZ() == 0 || fl.getPosition().getZ() == 4)  {
                    continue;
                }
			}
			World.deregister(fl);
		}

		player.getInstanceManager().npcToSpawn1 = null;
		player.getInstanceManager().npcToSpawn2 = null;
		player.getInstanceManager().npcToSpawn3 = null;
		player.getInstanceManager().npcToSpawn4 = null;
		player.getInstanceManager().npcToSpawn5 = null;
		player.getInstanceManager().npcToSpawn6 = null;
		player.getInstanceManager().npcToSpawn7 = null;
		player.getInstanceManager().npcToSpawn8 = null;

		player.sendMessage("Your instance is over!");
		OverlayTimer.stopOverlayTimer(player, 1206);
		player.getInstanceManager().inInstance = false;
		player.getInstanceManager().timeSelected = LocalDateTime.MIN;
		if (death) {
			player.moveTo(GameSettings.DEFAULT_POSITION);
		}
	}

	public void respawnNpc(Player player, NPC npc) {

		if (npc.getPosition().getZ() != player.getPosition().getZ()) {
			return;
		}
		
		if(player.isTeleporting())
			return;
		
		if(!player.getInstanceManager().inInstance) {
			return;
		}

		TaskManager.submit(new Task(3) {
			@Override
			protected void execute() {
				
				if(!player.getInstanceManager().inInstance) {
					return;
				}

				while (player.getInstanceManager().npcToSpawn1 == null) {
					player.getInstanceManager().npcToSpawn1 = new NPC(npc.getId(),
							new Position(3418, 4773, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn1);
				}

				while (player.getInstanceManager().npcToSpawn2 == null) {
					player.getInstanceManager().npcToSpawn2 = new NPC(npc.getId(),
							new Position(3418, 4774, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn2);
				}

				while (player.getInstanceManager().npcToSpawn3 == null) {
					player.getInstanceManager().npcToSpawn3 = new NPC(npc.getId(),
							new Position(3423, 4773, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn3);
				}

				while (player.getInstanceManager().npcToSpawn4 == null) {
					player.getInstanceManager().npcToSpawn4 = new NPC(npc.getId(),
							new Position(3424, 4774, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn4);
				}

				while ( player.getInstanceManager().npcToSpawn5 == null) {
					player.getInstanceManager().npcToSpawn5 = new NPC(npc.getId(),
							new Position(3423, 4780, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn5);
				}

				while (player.getInstanceManager().npcToSpawn6 == null) {
					player.getInstanceManager().npcToSpawn6 = new NPC(npc.getId(),
							new Position(3424, 4779, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn6);
				}

				while (player.getInstanceManager().npcToSpawn7 == null) {
					player.getInstanceManager().npcToSpawn7 = new NPC(npc.getId(),
							new Position(3418, 4780, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn7);
				}

				while (player.getInstanceManager().npcToSpawn8 == null) {
					player.getInstanceManager().npcToSpawn8 = new NPC(npc.getId(),
							new Position(3418, 4779, player.getIndex() * 4));
					World.register(player.getInstanceManager().npcToSpawn8);
				}
				System.out.print(" NPC SPAWNED " + npc.getId() + " || ");
				stop();
			}
		});
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

	public InstanceNpcs getNpcToSelect() {
		return npcToSelect;
	}

	public void setNpcToSelect(InstanceNpcs npcToSelect) {
		this.npcToSelect = npcToSelect;
	}
}
