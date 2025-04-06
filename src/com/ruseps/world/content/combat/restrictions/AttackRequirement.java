package com.ruseps.world.content.combat.restrictions;

import com.ruseps.model.definitions.NpcDefinition;

public enum AttackRequirement {

	TEST(-1, 0,
            new int[] {1},
            new int[] {0},
            new int[] {100}
    ),
	RYUK(4990, 0,
            new int[] {5049},
            new int[] {0},
            new int[] {100}
    ),
	JESUS(4991, 0,
            new int[] {4990},
            new int[] {0},
            new int[] {100}
    ),	
	SIMBA(4992, 0,
            new int[] {4991},
            new int[] {0},
            new int[] {150}
    ),
	SORA(4999, 0,
            new int[] {4992},
            new int[] {0},
            new int[] {200}
    ),	
	SULLY(4994, 0,
            new int[] {4999},
            new int[] {0},
            new int[] {250}
    ),
	MEGA_CHARIZARD(4981, 0,
            new int[] {4994},
            new int[] {0},
            new int[] {300}
    ),
	SAURON(4997, 0,
            new int[] {4981},
            new int[] {0},
            new int[] {350}
    ),
	SQUIDWARD(4993, 0,
            new int[] {4997},
            new int[] {0},
            new int[] {400}
    ),
	ICE_DEMON(4980, 0,
            new int[] {4993},
            new int[] {0},
            new int[] {1000}
    ),
	EVE(4271, 0,
            new int[] {4980},
            new int[] {0},
            new int[] {1500}
    ),
	GIMLEE(4265, 0,
            new int[] {4271},
            new int[] {0},
            new int[] {2000}
    ),
	BLOOD_ELEMENTAL(4267, 0,
            new int[] {4265},
            new int[] {0},
            new int[] {2500}
    ),
	TIKI_DEMON(4268, 0,
            new int[] {4267},
            new int[] {0},
            new int[] {3500}
    ),
	ARAGORN(4270, 0,
            new int[] {4268},
            new int[] {0},
            new int[] {4000}
    ),
	RAYQUAZA(4275, 0,
            new int[] {4270},
            new int[] {0},
            new int[] {5500}
    ),
	LEGOLAS(3008, 0,
            new int[] {4275},
            new int[] {0},
            new int[] {6500}
    ),
	DARTH_MAUL(5048, 0,
            new int[] {3008},
            new int[] {0},
            new int[] {7500}
    ),
	DIAMOND_HEAD(4998, 0,
            new int[] {5048},
            new int[] {0},
            new int[] {8000}
    ),
	DARIUS(4263, 0,
            new int[] {4998},
            new int[] {0},
            new int[] {8500}
    ),
	DEADLY(4264, 0,
            new int[] {4263},
            new int[] {0},
            new int[] {9250}
    ),
	ZELDORADO(4606, 0,
            new int[] {4264},
            new int[] {0},
            new int[] {9750}
    ),
	HEAT_BLAST(4266, 0,
            new int[] {4606},
            new int[] {0},
            new int[] {10000}
    ),
	KEVIN_FOUR_ARMS(4269, 0,
            new int[] {4266},
            new int[] {0},
            new int[] {10000}
    ),
	RA_THE_SUN_GOD(4272, 0,
            new int[] {4269},
            new int[] {0},
            new int[] {11500}
    ),
	DARK_KNIGHT(3009, 0,
            new int[] {4272},
            new int[] {0},
            new int[] {11500}
    ),
	CANNONBOLT(3010, 0,
            new int[] {3009},
            new int[] {0},
            new int[] {12000}
    ),
	BAD_BITCH(4274, 0,
            new int[] {3010},
            new int[] {0},
            new int[] {12000}
    ),
	RED_ASSASSIN(3011, 0,
            new int[] {4274},
            new int[] {0},
            new int[] {12000}
    ),
	EVIL_CLOW(3014, 0,
            new int[] {3011},
            new int[] {0},
            new int[] {14250}
    ),
	YVALTAL(190, 0,
            new int[] {3014},
            new int[] {0},
            new int[] {14250}
    ),
	DOOMS_DAY(185, 0,
            new int[] {190},
            new int[] {0},
            new int[] {15000}
    ),
	MT_DWELLER(4387, 0,
            new int[] {185},
            new int[] {0},
            new int[] {15000}
    ),
	DARK_MAGICIAN(1506, 0,
            new int[] {4387},
            new int[] {0},
            new int[] {15000}
    ),
	OBELISK(1510, 0,
            new int[] {1506},
            new int[] {0},
            new int[] {17500}
    ),
	BLOOD_QUEEN(1508, 0,
            new int[] {1510},
            new int[] {0},
            new int[] {17500}
    ),
	FOUR_ARMS(1509, 0,
            new int[] {1508},
            new int[] {0},
            new int[] {20000}
    ),
	GALACTIC_TITAN(1016, 0,
            new int[] {1509},
            new int[] {0},
            new int[] {22250}
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
	WORLD(4867, 10000,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	INHERITED(1416, 100,
            new int[] {0},
            new int[] {0},
            new int[] {0}
    ),
	OCTANE(1417, 15000,
            new int[] {1016},
            new int[] {0},
            new int[] {1000}
    ),
	LIMES(1017, 0,
            new int[] {1417},
            new int[] {0},
            new int[] {2500}
    ),
	SLIFER(1018, 0,
            new int[] {1017},
            new int[] {0},
            new int[] {3500}
    ),
    FALLEN_LORD(1039, 0,
            new int[] {1018},
            new int[] {0},
            new int[] {5500}
    ),
    KNIGHT_OF_TORMENT(1038, 0,
            new int[] {1039},
            new int[] {0},
            new int[] {8500}
    ),
    THE_LITCH(1494, 0,
            new int[] {1038},
            new int[] {0},
            new int[] {13500}
    ),
    SUPDERMAN(3012, 0,
            new int[] {1494},
            new int[] {0},
            new int[] {22500}
    ),
    DR_STRANGE(3007, 0,
            new int[] {3012},
            new int[] {0},
            new int[] {37500}
    ),
    GODS_RULER(1511, 0,
            new int[] {3007},
            new int[] {0},
            new int[] {45000}
    ),
    GOLDEN_FREEZA(5148, 0,
            new int[] {1511},
            new int[] {0},
            new int[] {72500}
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