package com.ruseps.world.content.skill.impl.crafting;


public enum BattleStaffData {
	
		AIR(1391, 573, 1397, 1000, 1),
		WATER(1391, 571, 1395, 1500, 10),
		EARTH(1391, 575, 1399, 2000, 20),
		FIRE(1391, 569, 1393, 2500, 40);

		public int item1, item2, outcome, xp, levelReq;
		private BattleStaffData(int item1, int item2, int outcome, int xp, int levelReq) {
			this.item1 = item1;
			this.item2 = item2;
			this.outcome = outcome;
			this.xp = xp;
			this.levelReq = levelReq;
		}
		public int getItem1() {
			return item1;
		}

		public int getItem2() {
			return item2;
		}

		public int getOutcome() {
			return outcome;
		}

		public int getXp() {
			return xp;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public static BattleStaffData forStaff(int id) {
			for (BattleStaffData ar : BattleStaffData.values()) {
				if (ar.getItem2() == id) {
					return ar;
				}
			}
			return null;
		}
}
