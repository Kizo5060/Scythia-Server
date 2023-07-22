package com.ruseps.world.content.combat.restrictions;

import com.ruseps.model.definitions.NpcDefinition;

public enum AttackRequirement {

	TEST(-1, 0,
            new int[] {1},
            new int[] {0},
            new int[] {100}
    ),
	BEGINNER(4990, 0,
            new int[] {5049},
            new int[] {0},
            new int[] {50}
    ),
	TRANS(4991, 0,
            new int[] {4990},
            new int[] {0},
            new int[] {100}
    ),	
	AURORA(4992, 0,
            new int[] {4991},
            new int[] {0},
            new int[] {150}
    ),
	DONALD(4999, 0,
            new int[] {4992},
            new int[] {0},
            new int[] {200}
    ),	
	SORA(4994, 0,
            new int[] {4999},
            new int[] {0},
            new int[] {250}
    ),
	MARIO(4981, 0,
            new int[] {4994},
            new int[] {0},
            new int[] {300}
    ),
	CHARI(4997, 0,
            new int[] {4981},
            new int[] {0},
            new int[] {350}
    ),
	GREEN(4993, 0,
            new int[] {4997},
            new int[] {0},
            new int[] {400}
    ),
	LUIGI(4980, 0,
            new int[] {4993},
            new int[] {0},
            new int[] {450}
    ),
	DEMON(4271, 0,
            new int[] {4980},
            new int[] {0},
            new int[] {500}
    ),
	YODA(4265, 0,
            new int[] {4271},
            new int[] {0},
            new int[] {550}
    ),
	DONKEY(4267, 0,
            new int[] {4265},
            new int[] {0},
            new int[] {600}
    ),
	SPITFIRE(4268, 0,
            new int[] {4267},
            new int[] {0},
            new int[] {650}
    ),
	GODZILLA(4270, 0,
            new int[] {4268},
            new int[] {0},
            new int[] {700}
    ),
	MORTY(4275, 0,
            new int[] {4270},
            new int[] {0},
            new int[] {750}
    ),
	HULK(3008, 0,
            new int[] {4275},
            new int[] {0},
            new int[] {800}
    ),
	GROUDON(5048, 0,
            new int[] {3008},
            new int[] {0},
            new int[] {850}
    ),
    CORTEX(4998, 0,
            new int[] {5048},
            new int[] {0},
            new int[] {200}
    ),
	MEWTWO(4263, 0,
            new int[] {4998},
            new int[] {0},
            new int[] {250}
    ),
	THANOS(4264, 0,
            new int[] {4263},
            new int[] {0},
            new int[] {300}
    ),
	DEADLY(4606, 0,
            new int[] {4264},
            new int[] {0},
            new int[] {100}
    ),
	LAGONA(4266, 0,
            new int[] {4606},
            new int[] {0},
            new int[] {150}
    ),
	DILLONS(4269, 0,
            new int[] {4266},
            new int[] {0},
            new int[] {200}
    ),
	RICK(4272, 0,
            new int[] {4269},
            new int[] {0},
            new int[] {250}
    ),
	GOLDEN(3009, 0,
            new int[] {4272},
            new int[] {0},
            new int[] {150}
    ),
	DARK(3010, 0,
            new int[] {3009},
            new int[] {0},
            new int[] {200}
    ),
	BADDIE(4274, 0,
            new int[] {3010},
            new int[] {0},
            new int[] {200}
    ),
	YELLOW(3011, 0,
            new int[] {4274},
            new int[] {0},
            new int[] {250}
    ),
	RED(3014, 0,
            new int[] {3011},
            new int[] {0},
            new int[] {300}
    ),
	IT(190, 0,
            new int[] {3014},
            new int[] {0},
            new int[] {350}
    ),
	DIGI(185, 0,
            new int[] {190},
            new int[] {0},
            new int[] {450}
    ),
	DOOMS(4387, 0,
            new int[] {185},
            new int[] {0},
            new int[] {550}
    ),
	BOONY(1506, 0,
            new int[] {4387},
            new int[] {0},
            new int[] {250}
    ),
	MAGIC(1510, 0,
            new int[] {1506},
            new int[] {0},
            new int[] {350}
    ),
	HORNED(1508, 0,
            new int[] {1510},
            new int[] {0},
            new int[] {450}
    ),
	BLOODKING(1509, 0,
            new int[] {1508},
            new int[] {0},
            new int[] {500}
    ),
	LAVA(-1, 0,
            new int[] {1509},
            new int[] {0},
            new int[] {300}
    ),
	DEFENDERZ(2237, 300,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	WEAPONZ(5763, 300,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	AOE(13458, 100,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	MAGICZ(1515, 300,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	RANGE(1414, 300,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	WORLD(4867, 5000,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	WRECKED(1416, 100,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	OCTANE(1417, 15000,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
    CERBLOVER(1017, 0,
            new int[] {1417},
            new int[] {0},
            new int[] {300}
    ),
    WITCH(1018, 0,
            new int[] {1017},
            new int[] {0},
            new int[] {600}
    ),
    FALLENGOD(1039, 0,
            new int[] {1018},
            new int[] {0},
            new int[] {800}
    ),
    Lava(1038, 0,
            new int[] {1039},
            new int[] {0},
            new int[] {1000}
    ),
    ;
 
    private final int targetId;
    private final int[] reqKillId;
    private final int[] reqKillCT;
    private final int[] requiredRunningCT;
    private final int requiredTotalCT;

    AttackRequirement(final int targetId, final int requiredTotalCT, final int[] reqKillId, final int[] reqKillCT, final int[] requiredRunningCT) {
        this.targetId = targetId;
        this.reqKillId = reqKillId;
        this.reqKillCT = reqKillCT;
        this.requiredRunningCT = requiredRunningCT;
        this.requiredTotalCT = requiredTotalCT;
    }

    public static AttackRequirement byId(final int npcId) {
        for (AttackRequirement target : values()) {
            if(target.getTargetId() == npcId)
                return target;
        }
        return null;
    }

    public int getTargetId() {
        return targetId;
    }

    public int[] getReqKillId() {
        return reqKillId;
    }

    public int getReqKillCT(int index) {
        if(reqKillCT.length >= index)
            return reqKillCT[index];
        else {
            sendError();
            return 2147000000;
        }
    }

    public int getRequiredRunningCT(int index) {
        if(requiredRunningCT.length >= index)
            return requiredRunningCT[index];
        else {
            sendError();
            return 2_147_000_000;
        }
    }

    private void sendError() {
        System.err.println("[AttackRequirement.java][Warning]: Potentially improper enum constant setup detected!");
        System.err.println("[AttackRequirement.java][Warning]: Please make sure the indices count of each array matches!");
        System.err.println("[AttackRequirement.java][Warning]: Effected Entity: " + NpcDefinition.forId(getTargetId()).getName() + ".");
    }

    public int getRequiredTotalCT() {
        return requiredTotalCT;
    }

}