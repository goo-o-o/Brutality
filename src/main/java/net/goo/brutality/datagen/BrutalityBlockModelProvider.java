package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nl.requios.effortlessbuilding.create.foundation.utility.NBTHelper;

public class BrutalityBlockModelProvider extends BlockModelProvider {
    public BrutalityBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }


    @Override
    protected void registerModels() {

        for (DyeColor dyeColor : DyeColor.values()) {
            String colorName = dyeColor.getName();
            ResourceLocation texture = ResourceLocation.withDefaultNamespace("block/" + colorName + "_concrete");

            // Stairs
            ResourceLocation stairName =  BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();

            stairs(String.valueOf(stairName), texture, texture, texture);
            stairsInner(stairName + "_inner", texture, texture, texture);
            stairsOuter(stairName + "_outer", texture, texture, texture);

            // Slabs
            ResourceLocation slabName =  BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();

            slab(String.valueOf(slabName), texture, texture, texture);
            slabTop(slabName + "_top", texture, texture, texture);

        }

    }
}
