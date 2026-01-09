package net.goo.brutality.item;

import net.goo.brutality.Brutality;

public interface BrutalityCategories {

    enum ItemType implements BrutalityCategories {
        ARMOR, AXE, BOW, GENERIC, HAMMER, PICKAXE, SCYTHE, SWORD, TRIDENT, SPEAR, STAFF, BLOCK, TOME, THROWING, CURIO;

        static {
            try {
                ItemType[] allValues = values();
                Brutality.LOGGER.info("[Brutality] BrutalityCategories.ItemType loaded: {} types", allValues.length);
            } catch (Throwable t) {
                Brutality.LOGGER.error("[Brutality] CRITICAL: Failed to initialize ItemType enum!", t);
                throw new ExceptionInInitializerError(t);
            }
        }
    }

    enum AttackType implements BrutalityCategories {
        BLUNT, SLASH, PIERCE, NONE
    }

}

