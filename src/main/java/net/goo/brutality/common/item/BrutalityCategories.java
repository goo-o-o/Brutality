package net.goo.brutality.common.item;

import java.util.Locale;

public interface BrutalityCategories {

    static BrutalityCategories findByName(String name) {
        try {
            return ItemType.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            try {
                return MagicItemType.valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e2) {
                return null; // Or a default type like GENERIC
            }
        }
    }

    enum ItemType implements BrutalityCategories {
        ARMOR, AXE, BOW, GENERIC, HAMMER, PICKAXE, SCYTHE, SWORD, TRIDENT, SPEAR, BLOCK, THROWING, CURIO;
    }

    enum MagicItemType implements BrutalityCategories {
        STAFF, WAND, TOME;
    }

    enum AttackType implements BrutalityCategories {
        BLUNT, SLASH, PIERCE, NONE
    }

}

