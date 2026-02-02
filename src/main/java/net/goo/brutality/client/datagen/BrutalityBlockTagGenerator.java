package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityBlockFamilies;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BrutalityBlockTagGenerator extends BlockTagsProvider {
    public BrutalityBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Brutality.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addTagsToFamily(BrutalityBlockFamilies.SOLIDIFIED_MANA, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL);
        addTagsToConcreteVariants();


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                BrutalityBlocks.TABLE_OF_WIZARDRY.get(),
                BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get(),
                BrutalityBlocks.PEDESTAL_OF_WIZARDRY.get(),
                BrutalityBlocks.WATER_COOLER_BLOCK.get(),
                BrutalityBlocks.OLD_SERVER_CASING.get(),
                BrutalityBlocks.OLD_SERVER_PANEL.get(),
                BrutalityBlocks.EXIT_SIGN.get(),
                BrutalityBlocks.TOILET.get(),
                BrutalityBlocks.URINAL.get(),
                BrutalityBlocks.UPPER_HVAC.get(),
                BrutalityBlocks.LOWER_HVAC.get(),
                BrutalityBlocks.OLD_AIR_CONDITIONER.get(),
                BrutalityBlocks.DUSTBIN.get(),
                BrutalityBlocks.BLACK_OFFICE_CHAIR.get(),
                BrutalityBlocks.WHITE_OFFICE_CHAIR.get(),
                BrutalityBlocks.LCD_MONITOR.get(),
                BrutalityBlocks.CRT_MONITOR.get(),
                BrutalityBlocks.WET_FLOOR_SIGN.get(),
                BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get(),
                BrutalityBlocks.GRAY_FILING_CABINET.get(),
                BrutalityBlocks.WHITE_FILING_CABINET.get(),
                BrutalityBlocks.COFFEE_MACHINE_BLOCK.get()
        );

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
                BrutalityBlocks.TABLE_OF_WIZARDRY.get(),
                BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get(),
                BrutalityBlocks.PEDESTAL_OF_WIZARDRY.get(),
                BrutalityBlocks.WATER_COOLER_BLOCK.get(),
                BrutalityBlocks.OLD_SERVER_CASING.get(),
                BrutalityBlocks.OLD_SERVER_PANEL.get(),
                BrutalityBlocks.TOILET.get(),
                BrutalityBlocks.URINAL.get(),
                BrutalityBlocks.UPPER_HVAC.get(),
                BrutalityBlocks.LOWER_HVAC.get(),
                BrutalityBlocks.OLD_AIR_CONDITIONER.get(),
                BrutalityBlocks.DUSTBIN.get()
        );

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(
                BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get(),
                BrutalityBlocks.EXIT_SIGN.get(),
                BrutalityBlocks.BLACK_OFFICE_CHAIR.get(),
                BrutalityBlocks.WHITE_OFFICE_CHAIR.get(),
                BrutalityBlocks.LCD_MONITOR.get(),
                BrutalityBlocks.CRT_MONITOR.get(),
                BrutalityBlocks.WET_FLOOR_SIGN.get(),
                BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get(),
                BrutalityBlocks.GRAY_FILING_CABINET.get(),
                BrutalityBlocks.WHITE_FILING_CABINET.get()
        );

    }

    protected final void addTagsToConcreteVariants() {
        BrutalityBlocks.CONCRETE_SLABS.forEach(registryBlock -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(registryBlock.get()));
        BrutalityBlocks.CONCRETE_STAIRS.forEach(registryBlock -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(registryBlock.get()));
    }

    @SafeVarargs
    protected final void addTagsToFamily(BlockFamily family, TagKey<Block>... mineableTags) {
        Block base = family.getBaseBlock();

        for (TagKey<Block> tagKey : mineableTags) {
            var tagBuilder = this.tag(tagKey).add(base);
            family.getVariants().values().forEach(tagBuilder::add);
        }

        family.getVariants().forEach((variant, block) -> {
            switch (variant) {
                case SLAB -> this.tag(BlockTags.SLABS).add(block);
                case STAIRS -> this.tag(BlockTags.STAIRS).add(block);
                case WALL -> this.tag(BlockTags.WALLS).add(block);
                case DOOR -> this.tag(BlockTags.DOORS).add(block);
                case TRAPDOOR -> this.tag(BlockTags.TRAPDOORS).add(block);
                case BUTTON -> this.tag(BlockTags.BUTTONS).add(block);
                case PRESSURE_PLATE -> this.tag(BlockTags.PRESSURE_PLATES).add(block);
                case FENCE -> this.tag(BlockTags.FENCES).add(block);
                case FENCE_GATE -> this.tag(BlockTags.FENCE_GATES).add(block);
            }
        });
    }
}
