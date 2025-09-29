package net.goo.brutality.item;

public interface BrutalityCategories {

    enum ItemType implements BrutalityCategories {
        ARMOR, AXE, BOW, GENERIC, HAMMER, PICKAXE, SCYTHE, SWORD, TRIDENT, SPEAR, STAFF, BLOCK, TOME, THROWING
    }

    enum AttackType implements BrutalityCategories {
        BLUNT, SLASH, PIERCE, NONE
    }

    enum CurioType implements BrutalityCategories {
        HANDS, HEAD, CHARM, NECKLACE, BELT, HEART, RING, ANKLET
    }

}

