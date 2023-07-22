package com.ruseps.world.content.raids;

import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.npc.NPC;

public class MarvelsRaid extends Raid2 {

    public static boolean started;

    public static boolean getStarted(){
        return started;
    }

    public static void setStarted(boolean started) {
        MarvelsRaid.started = started;
    }

    public MarvelsRaid(int maxPlayers) {
        super(maxPlayers, Location.MARVELS_RAID);
        setName("Marvel Raid");
        init();
    }

    @Override
    protected void spawnCurrentStageNpcs() {
        Stage stage = stages[currentStage];
        int npcCount = stage.countNpcs();
        NPC npc;
        for (int i = 3; i < npcCount; ++i) {
            npc = stage.getNpc(i);
            npc.getDefinition().setMulti(true);
            npc.getDefinition().setRespawnTime(-1);
            World.register(stage.getNpc(i));
        }
    }

    @Override
    protected void giveRewards() {
        int[] rare = {}; // 1:333 chance
        int[] ultraRare = {}; // 1:1000 chance - should put pets here
        int abba[][] = {
                {}, {}, {}, {}, {}
        };

        for (int i = 0; i < playerIndex; ++i) {

                int random = Misc.random(250);
                Item reward = new Item(995);
                int randomCommon = RandomUtility.getRandom(abba.length - 1);
                reward = new Item(abba[randomCommon][0]);
                int amount = abba[randomCommon][1];

                if (random == 0) {
                    amount = 1;
                    int randomUltra = Misc.random(ultraRare.length - 1);
                    reward = new Item(ultraRare[randomUltra]);
                    String itemName = reward.getDefinition().getName();
                    String announcement = "@red@[Marvels Raids][Ultra Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
                    World.sendMessage(announcement);
                } else if (random >= 1 && random <= 14) {
                    amount = 1;
                    int randomRare = Misc.random(rare.length - 1);
                    reward = new Item(rare[randomRare]);
                    String itemName = reward.getDefinition().getName();
                    String announcement = "@red@[Marvels][Rare] @Bla@" + players[i].getUsername() + " has got " + itemName;
                    World.sendMessage(announcement);
                }
                reward.setAmount(amount);
                players[i].sendMessage("you got " + amount + "x " + reward.getDefinition().getName() + " as a reward");
                if (reward.getDefinition().isStackable() || players[i].getInventory().getFreeSlots() > amount)
                    players[i].getInventory().add(reward);
                else {
                    players[i].sendMessage("reward was added to your bank because you have no inventory spaces");
                    players[i].getBank(players[i].getCurrentBankTab()).add(reward);
                }
        }
    }

    @Override
    protected void initStages() {
        Stage[] stages = new Stage[4];
        NPC[] stage0npcs = new NPC[11];
        stage0npcs[0] = new NPC(4276, new Position(2264, 3321, 0));
        stage0npcs[1] = new NPC(3007, new Position(2264, 3315, 0));
        stage0npcs[2] = new NPC(3012, new Position(2259, 3306, 0));
        stage0npcs[3] = new NPC(4274, new Position(2260, 3300, 0));
        stage0npcs[4] = new NPC(4275, new Position(2257, 3318, 0));
        stages[0] = new Stage(stage0npcs);


        NPC[] stage1npcs = new NPC[11];
        stage1npcs[0] = new NPC(4276, new Position(2264, 3321, 0));
        stage1npcs[1] = new NPC(4276, new Position(2264, 3315, 0));
        stage1npcs[2] = new NPC(3007, new Position(2259, 3306, 0));
        stage1npcs[3] = new NPC(3012, new Position(2260, 3300, 0));
        stage1npcs[4] = new NPC(3012, new Position(2257, 3318, 0));
        stage1npcs[5] = new NPC(4274, new Position(2265, 3308, 0));
        stage1npcs[6] = new NPC(4275, new Position(2265, 3317, 0));
        stages[1] = new Stage(stage1npcs);


        NPC[] stage2npcs = new NPC[11];
        stage2npcs[0] = new NPC(4276, new Position(2264, 3315, 0));
        stage2npcs[1] = new NPC(4276, new Position(2264, 3321, 0));
        stage2npcs[2] = new NPC(3007, new Position(2257, 3318, 0));
        stage2npcs[3] = new NPC(3007, new Position(2265, 3317, 0));
        stage2npcs[4] = new NPC(3012, new Position(2260, 3300, 0));
        stage2npcs[5] = new NPC(4274, new Position(2265, 3308, 0));
        stage2npcs[6] = new NPC(4275, new Position(2262, 3303, 0));
        stage2npcs[7] = new NPC(4275, new Position(2263, 3305, 0));
        stage2npcs[8] = new NPC(3012, new Position(2260, 3310, 0));
        stages[2] = new Stage(stage2npcs);

        NPC[] stage3npcs = new NPC[11];
        stage3npcs[0] = new NPC(4276, new Position(2264, 3315, 0));
        stage3npcs[1] = new NPC(4276, new Position(2264, 3321, 0));
        stage3npcs[2] = new NPC(3007, new Position(2265, 3308, 0));
        stage3npcs[3] = new NPC(3007, new Position(2262, 3303, 0));
        stage3npcs[4] = new NPC(3012, new Position(2263, 3305, 0));
        stage3npcs[5] = new NPC(4274, new Position(2265, 3317, 0));
        stage3npcs[6] = new NPC(4275, new Position(2260, 3300, 0));
        stage3npcs[7] = new NPC(4275, new Position(2261, 3302, 0));
        stage3npcs[8] = new NPC(3012, new Position(2262, 3320, 0));
        stages[3] = new Stage(stage3npcs);
        
        this.stages = stages;
    }

    @Override
    protected void startNextStage() {
        super.startNextStage();
    }

    @Override
    public void init() {
        super.init();
        initStages();
    }



}
