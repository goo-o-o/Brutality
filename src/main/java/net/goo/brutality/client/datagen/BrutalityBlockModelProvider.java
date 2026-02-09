package net.goo.brutality.client.datagen;

import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BrutalityBlockModelProvider extends BlockModelProvider {
    public BrutalityBlockModelProvider(PackOutput output, String modId, ExistingFileHelper existingFileHelper) {
        super(output, modId, existingFileHelper);
    }

    public void cubeWithParticle(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west, ResourceLocation particle) {
        withExistingParent(name, "cube")
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west)
                .texture("particle", particle);
    }



    @Override
    protected void registerModels() {

        cubeAll(BrutalityBlocks.PLASTERBOARD.getId().getPath(), modLoc("block/" + BrutalityBlocks.PLASTERBOARD.getId().getPath()));

        cross(BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.getId().getPath(),
                modLoc("block/small_mana_crystal_bud")).renderType("cutout");
        cross(BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.getId().getPath(),
                modLoc("block/medium_mana_crystal_bud")).renderType("cutout");
        cross(BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.getId().getPath(),
                modLoc("block/large_mana_crystal_bud")).renderType("cutout");
        cross(BrutalityBlocks.MANA_CRYSTAL_CLUSTER.getId().getPath(),
                modLoc("block/mana_crystal_cluster")).renderType("cutout");

        cubeWithParticle(
                BrutalityBlocks.UPPER_HVAC.getId().getPath(),
                modLoc("block/hvac/border_bottom_left"),
                modLoc("block/hvac/upper/up"),
                modLoc("block/hvac/upper/north"),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/border_top_right"),
                modLoc("block/hvac/upper/west"),
                modLoc("block/hvac/upper/north")
        );

        cubeWithParticle(
                BrutalityBlocks.LOWER_HVAC.getId().getPath(),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/lower/north"),
                modLoc("block/hvac/left"),
                modLoc("block/hvac/right"),
                modLoc("block/hvac/lower/west"),
                modLoc("block/hvac/lower/north")
        );

        singleTexture(BrutalityBlocks.WHITE_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityBlocks.WHITE_FILING_CABINET.getId().getPath()));

        singleTexture(BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.getId().getPath()));

        singleTexture(BrutalityBlocks.GRAY_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityBlocks.GRAY_FILING_CABINET.getId().getPath()));


        for (DyeColor dyeColor : DyeColor.values()) {
            String colorName = dyeColor.getName();
            ResourceLocation texture = ResourceLocation.withDefaultNamespace("block/" + colorName + "_concrete");

            // Stairs
            ResourceLocation stairName = BrutalityBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();

            stairs(String.valueOf(stairName), texture, texture, texture);
            stairsInner(stairName + "_inner", texture, texture, texture);
            stairsOuter(stairName + "_outer", texture, texture, texture);

            // Slabs
            ResourceLocation slabName = BrutalityBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();

            slab(String.valueOf(slabName), texture, texture, texture);
            slabTop(slabName + "_top", texture, texture, texture);

        }

    }
}
