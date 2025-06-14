package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Brutality.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Brutality.MOD_ID, name));
        }
    }
}
