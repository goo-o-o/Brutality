package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.Curios;

public class BrutalityTags {
    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GASTRONOMIST_ITEMS = tag("gastronomist_items");
        public static final TagKey<Item> MAGIC_ITEMS = tag("magic_items");
        public static final TagKey<Item> MATH_ITEMS = tag("math_items");
        public static final TagKey<Item> RAGE_ITEMS = tag("rage_items");

        public static final TagKey<Item> ANKLET = tag(Curios.MODID, "anklet");
        public static final TagKey<Item> BELT = tag(Curios.MODID, "belt");
        public static final TagKey<Item> CHARM = tag(Curios.MODID, "charm");
        public static final TagKey<Item> HANDS = tag(Curios.MODID, "hands");
        public static final TagKey<Item> HEAD = tag(Curios.MODID, "head");
        public static final TagKey<Item> HEART = tag(Curios.MODID, "heart");
        public static final TagKey<Item> NECKLACE = tag(Curios.MODID, "necklace");
        public static final TagKey<Item> RING = tag(Curios.MODID, "ring");
        public static final TagKey<Item> FEET = tag(Curios.MODID, "feet");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name));
        }
        private static TagKey<Item> tag(String namespace, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
