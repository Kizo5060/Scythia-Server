package com.ruseps.world.content.combat.magic;


import com.ruseps.model.Graphic;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.player.Player;

public class CustomMagicStaff {

    public static enum CustomStaff {
    	BEGINNER_STAFF(new int[] { 21014 }, CombatSpells.BEGINNER_STAFF_NEW.getSpell()),
    	LAZER_POWERED(new int[] { 10502 }, CombatSpells.LAZER_POWERED_GUN.getSpell()),
        KRYPTIC_STAFF(new int[] { 1005 }, CombatSpells.KRYPTIC_SPELL.getSpell()),
        THANOS_STAFF(new int[] { 15485 }, CombatSpells.NEWDEDISPELL.getSpell()),
        ADAM_STAFF(new int[] { 17293, 19961, 20908, 22012, 22010, 20602, 22025}, CombatSpells.NEWDEDISPELL.getSpell()),
        OBLIVIONSTAFF(new int[] { 7082 }, CombatSpells.OBLIVION.getSpell()),
        WATER_STAFF(new int[] {11653 }, CombatSpells.KRYPTIC_SPELL.getSpell()),
    	FAST_STAFF(new int[] { 20656 }, CombatSpells.OBLIVION2.getSpell()),
        LORIEN(new int[] { 3510 }, CombatSpells.LORIENSTAFF.getSpell()),
    	THANOSI(new int[] { 13084 }, CombatSpells.THANOSI.getSpell()),
    	INFUSEDTHANOS(new int[] { 13080 }, CombatSpells.TINFUSED.getSpell()),
    	DEADLY_STAFF(new int[] { 2764 }, CombatSpells.DEADLYSPELL.getSpell()),
    	OP_STAFFS(new int[] { 11650 }, CombatSpells.OBLIVION3.getSpell()),
    	BLASTERB(new int[] { 2545 }, CombatSpells.BLASTERSPELL.getSpell()),
        RAZER(new int[] { 3495 }, CombatSpells.RAZERSPELL.getSpell()),
        MDMA(new int[] { 3525 }, CombatSpells.MDMASPELL.getSpell()),
    	GOLEMSTAFF(new int[] { 19072 }, CombatSpells.GOLEMSSPELL.getSpell()),
    	UNHOLYSTAFF(new int[] { 21022 }, CombatSpells.UNHOLYSPELL.getSpell()),
    	REAPERSSTAFF(new int[] { 21048 }, CombatSpells.REAPERSSPELL.getSpell()),
        RAGINGSTAFF(new int[] { 2746 }, CombatSpells.RAGINGSPELL.getSpell()),
    	WINDSTAFF(new int[] { 21039 }, CombatSpells.WINDSPELL.getSpell()),
        FRANKYGUN(new int[] { 13023 }, CombatSpells.FRANKYSPELL.getSpell()),
    	KANA(new int[] { 19077 }, CombatSpells.KANASPELL.getSpell()),
        DZONE(new int[] { 3498 }, CombatSpells.DZONESSPELL.getSpell()),
    	MAGICM(new int[] { 18966 }, CombatSpells.MAGICSPELL.getSpell()),
    	TRIBRID(new int[] { 21063 }, CombatSpells.TRIBRIDSPELL.getSpell()),
    	MINI1(new int[] { 2691 }, CombatSpells.MINI1SPELL.getSpell()),
    	MINI2(new int[] { 2692 }, CombatSpells.MINI2SPELL.getSpell()),
    	MINI3(new int[] { 2693 }, CombatSpells.MINI3SPELL.getSpell()),
    	MINI4(new int[] { 2694 }, CombatSpells.MINI4SPELL.getSpell()),
    	MINI5(new int[] { 2695 }, CombatSpells.MINI5SPELL.getSpell()),
    	MINI6(new int[] { 2696 }, CombatSpells.MINI6SPELL.getSpell()),
    	MINI7(new int[] { 2697 }, CombatSpells.MINI7SPELL.getSpell()),
    	MINI8(new int[] { 2698 }, CombatSpells.MINI8SPELL.getSpell()),
    	MINI9(new int[] { 2699 }, CombatSpells.MINI9SPELL.getSpell()),
    	MINI10(new int[] { 2700 }, CombatSpells.MINI10SPELL.getSpell());
    	

        private int[] itemIds;
        private CombatSpell spell;

        CustomStaff(int[] itemIds, CombatSpell spell) {
            this.itemIds = itemIds;
            this.spell = spell;
        }

        public int[] getItems() {
            return this.itemIds;
        }

        public CombatSpell getSpell() {
            return this.spell;
        }

        public static CombatSpell getSpellForWeapon(int weaponId) {
            for (CustomStaff staff : CustomStaff.values()) {
                for (int itemId : staff.getItems())
                    if (weaponId == itemId)
                        return staff.getSpell();
            }
            return null;
        }
    }

    public static boolean checkCustomStaff(Character c) {
        int weapon;
        if (!c.isPlayer())
            return false;
        Player player = (Player)c;
        weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        return CustomStaff.getSpellForWeapon(weapon) != null;
    }

    public static void handleCustomStaff(Character c) {
        Player player = (Player) c;
        int weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        CombatSpell spell = CustomStaff.getSpellForWeapon(weapon);
        player.setCastSpell(spell);
        player.setAutocast(true);
        player.setAutocastSpell(spell);
        player.setCurrentlyCasting(spell);
        player.setLastCombatType(CombatType.MAGIC);

    }
    public static CombatContainer getCombatContainer(Character player, Character target) {
        ((Player)player).setLastCombatType(CombatType.MAGIC);
        return new CombatContainer(player, target, 1, 1, CombatType.MAGIC, true) {
            @Override
            public void onHit(int damage, boolean accurate) {

                target.performGraphic(new Graphic(1730));
            }
        };
    }

}
