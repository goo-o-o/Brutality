package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityBlockStateProvider extends BlockStateProvider {
    public BrutalityBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Brutality.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BrutalityModBlocks.WATER_COOLER_BLOCK);
        blockWithItem(BrutalityModBlocks.COFFEE_MACHINE_BLOCK);
        blockWithItem(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("particle", modLoc("block/" + name));

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }
}
