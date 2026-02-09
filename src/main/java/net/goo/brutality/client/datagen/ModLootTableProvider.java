package net.goo.brutality.client.datagen;

import net.goo.brutality.client.datagen.loot.BrutalityBlockLootSubProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BrutalityBlockLootSubProvider::new, LootContextParamSets.BLOCK)
        ));
    }
}
