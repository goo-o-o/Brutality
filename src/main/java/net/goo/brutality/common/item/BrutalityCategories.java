package net.goo.brutality.common.item;

public interface BrutalityCategories {

    enum ItemType implements BrutalityCategories {
        ARMOR, AXE, BOW, GENERIC, HAMMER, PICKAXE, SCYTHE, SWORD, TRIDENT, SPEAR, STAFF, BLOCK, TOME, THROWING, CURIO;
    }

    enum AttackType implements BrutalityCategories {
        BLUNT, SLASH, PIERCE, NONE
    }

}

