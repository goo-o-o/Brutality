package net.goo.brutality.magic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBrutalitySpell {

    int getBaseManaCost();

    float getBaseDamage();

    int getBaseCooldown();

    MagicSchool getSchool();

    SpellType getType();

    String getSpellName();

    default boolean canCast(Player player, ItemStack stack, int spellLevel) {
        return true;
    }

    void onCast(Player player, ItemStack stack, int spellLevel);


    enum MagicSchool {
        DAEMONIC("daemonium"),
        SCULKBEARER("dimlite"),
        EVERGREEN("virentium"),
        VOLTWEAVER("conductite"),
        COSMIC("cosmilite"),
        CELESTIA("reverium"),
        UMBRANCY("nyxium"),
        EXODIC("exodium"),
        HELLFIRE("hellspec");

        private final String material;

        MagicSchool(String material) {
            this.material = material;
        }

        public String getMaterial() {
            return material;
        }
    }

    enum SpellType {
        OFFENSIVE,
        DEFENSIVE,
        UTIL
    }


}
