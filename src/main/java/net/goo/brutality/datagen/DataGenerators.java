package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BrutalityRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));

        generator.addProvider(event.includeClient(), new BrutalityBlockModelProvider(packOutput, Brutality.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeClient(), new BrutalityBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new BrutalityItemModelProvider(packOutput, existingFileHelper));

        BrutalityBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new BrutalityBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(), new BrutalityItemTagProvider(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeServer(), new BrutalityGlobalLootModifiersProvider(packOutput));
        generator.addProvider(event.includeServer(), new BrutalityPoiTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
//        generator.addProvider(event.includeServer(), new BrutalityDataFetcher(packOutput));
        
    }
}
