package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BrutalityFluidTagProvider extends FluidTagsProvider {
    public BrutalityFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Brutality.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
//        this.tag(FluidTags.WATER)
//            .add(BrutalityFluids.LIQUID_MANA_SOURCE.get())
//            .add(BrutalityFluids.LIQUID_MANA_FLOWING.get());
    }
}