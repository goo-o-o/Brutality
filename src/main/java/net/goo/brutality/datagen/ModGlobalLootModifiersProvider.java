package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Brutality.MOD_ID);
    }

    @Override
    protected void start() {

        add("supernova_sword_from_end_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/end_city_treasure")).build() }, ModItems.SUPERNOVA_SWORD.get()));

    }
}
