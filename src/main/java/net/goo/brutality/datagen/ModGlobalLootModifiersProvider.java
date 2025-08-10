package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.loot.AddItemModifier;
import net.goo.brutality.registry.BrutalityModItems;
import net.mcreator.terramity.init.TerramityModEntities;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Brutality.MOD_ID);
    }

    @Override
    protected void start() {

//        add("supernova_sword_from_end_city", new AddItemModifier(new LootItemCondition[] {
//                new LootTableIdCondition.Builder(new ResourceLocation("chests/end_city_treasure")).build() }, BrutalityModItems.SUPERNOVA_SWORD.get()));

        add("ender_dragon_drops_dragonheart", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(EntityType.ENDER_DRAGON)
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.25F).build()
                },
                BrutalityModItems.DRAGONHEART.get()
        ));

        add("uvogre_drops_uvogre_heart", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.UVOGRE.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()
                },
                BrutalityModItems.UVOGRE_HEART.get()
        ));

        add("zombie_drops_zombie_heart", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(EntityType.ZOMBIE)
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()
                },
                BrutalityModItems.ZOMBIE_HEART.get()
        ));
        add("trial_guardian_drops_trial_guardian_eyebrows", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.TRIAL_GUARDIAN.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.75F).build()
                },
                BrutalityModItems.TRIAL_GUARDIAN_EYEBROWS.get()
        ));
        add("trial_guardian_drops_trial_guardian_hands", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.TRIAL_GUARDIAN.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.75F).build()
                },
                BrutalityModItems.TRIAL_GUARDIAN_HANDS.get()
        ));



    }
}
