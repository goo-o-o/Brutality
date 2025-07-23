package net.goo.brutality.item;

public interface BrutalityCategories {

        enum ItemType implements BrutalityCategories {
                ARMOR, AXE, BOW, GENERIC, HAMMER, PICKAXE, SCYTHE, SWORD, TRIDENT, LANCE, STAFF, BLOCK, TOME
        }

        enum CurioType implements BrutalityCategories {
                HANDS, HEAD, CHARM, NECKLACE, BELT, HEART, RING
        }

}

