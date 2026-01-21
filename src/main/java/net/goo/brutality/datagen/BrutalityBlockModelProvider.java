package net.goo.brutality.datagen;

import net.goo.brutality.registry.BrutalityModBlocks;
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

        cubeAll(BrutalityModBlocks.PLASTERBOARD.getId().getPath(), modLoc("block/" + BrutalityModBlocks.PLASTERBOARD.getId().getPath()));

//        cubeColumn(BrutalityModBlocks.GRAY_OFFICE_RUG.getId().getPath(),
//                modLoc("block/" + BrutalityModBlocks.GRAY_OFFICE_CARPET.getId().getPath()),
//                modLoc("block/" + BrutalityModBlocks.GRAY_OFFICE_CARPET.getId().getPath()));
//
//        cubeColumn(BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.getId().getPath(),
//                modLoc("block/" + BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.getId().getPath()),
//                modLoc("block/" + BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.getId().getPath()));


        cubeWithParticle(
                BrutalityModBlocks.UPPER_HVAC.getId().getPath(),
                modLoc("block/hvac/border_bottom_left"),
                modLoc("block/hvac/upper/up"),
                modLoc("block/hvac/upper/north"),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/border_top_right"),
                modLoc("block/hvac/upper/west"),
                modLoc("block/hvac/upper/north")
        );

        cubeWithParticle(
                BrutalityModBlocks.LOWER_HVAC.getId().getPath(),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/border_top_left"),
                modLoc("block/hvac/lower/north"),
                modLoc("block/hvac/left"),
                modLoc("block/hvac/right"),
                modLoc("block/hvac/lower/west"),
                modLoc("block/hvac/lower/north")
        );

        singleTexture(BrutalityModBlocks.WHITE_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityModBlocks.WHITE_FILING_CABINET.getId().getPath()));

        singleTexture(BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.getId().getPath()));

        singleTexture(BrutalityModBlocks.GRAY_FILING_CABINET.getId().getPath(),
                modLoc("block/template_filing_cabinet"),
                "block", modLoc("block/" + BrutalityModBlocks.GRAY_FILING_CABINET.getId().getPath()));



        for (DyeColor dyeColor : DyeColor.values()) {
            String colorName = dyeColor.getName();
            ResourceLocation texture = ResourceLocation.withDefaultNamespace("block/" + colorName + "_concrete");

            // Stairs
            ResourceLocation stairName = BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();

            stairs(String.valueOf(stairName), texture, texture, texture);
            stairsInner(stairName + "_inner", texture, texture, texture);
            stairsOuter(stairName + "_outer", texture, texture, texture);

            // Slabs
            ResourceLocation slabName = BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();

            slab(String.valueOf(slabName), texture, texture, texture);
            slabTop(slabName + "_top", texture, texture, texture);

        }

    }
}
