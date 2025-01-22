package net.goo.armament.datagen;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItems;
import net.goo.armament.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Armament.MOD_ID);
    }

    @Override
    protected void start() {

        add("supernova_sword_from_end_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/end_city_treasure")).build() }, ModItems.SUPERNOVA_SWORD.get()));

    }
}
