package net.goo.brutality.common.registry;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class BrutalityBlockFamilies {
    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();
    public static final BlockFamily SOLIDIFIED_MANA = familyBuilder(BrutalityBlocks.SOLIDIFIED_MANA_BLOCK.get())
            .wall(BrutalityBlocks.SOLIDIFIED_MANA_WALL.get())
            .stairs(BrutalityBlocks.SOLIDIFIED_MANA_STAIRS.get())
            .door(BrutalityBlocks.SOLIDIFIED_MANA_DOOR.get())
            .trapdoor(BrutalityBlocks.SOLIDIFIED_MANA_TRAPDOOR.get())
            .button(BrutalityBlocks.SOLIDIFIED_MANA_BUTTON.get())
            .pressurePlate(BrutalityBlocks.SOLIDIFIED_MANA_PRESSURE_PLATE.get())
            .slab(BrutalityBlocks.SOLIDIFIED_MANA_SLAB.get()).getFamily();

    private static BlockFamily.Builder familyBuilder(Block pBaseBlock) {
        BlockFamily.Builder blockfamily$builder = new BlockFamily.Builder(pBaseBlock);
        BlockFamily blockfamily = MAP.put(pBaseBlock, blockfamily$builder.getFamily());
        if (blockfamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(pBaseBlock));
        } else {
            return blockfamily$builder;
        }
    }
}
