package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
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

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name));
        }
    }
}
