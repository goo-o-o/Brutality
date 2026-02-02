package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.loot.AddItemModifier;
import net.goo.brutality.common.registry.BrutalityItems;
import net.mcreator.terramity.init.TerramityModEntities;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class BrutalityGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public BrutalityGlobalLootModifiersProvider(PackOutput output) {
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
                        LootItemRandomChanceCondition.randomChance(0.66F).build()
                },
                BrutalityItems.DRAGONHEART.get()
        ));

        add("uvogre_drops_uvogre_heart", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.UVOGRE.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()
                },
                BrutalityItems.UVOGRE_HEART.get()
        ));

        add("zombie_drops_zombie_heart", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(EntityType.ZOMBIE)
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()
                },
                BrutalityItems.ZOMBIE_HEART.get()
        ));
        add("trial_guardian_drops_trial_guardian_eyebrows", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.TRIAL_GUARDIAN.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.75F).build()
                },
                BrutalityItems.TRIAL_GUARDIAN_EYEBROWS.get()
        ));
        add("trial_guardian_drops_trial_guardian_hands", new AddItemModifier(
                new LootItemCondition[] {
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().of(TerramityModEntities.TRIAL_GUARDIAN.get())
                        ).build(),
                        LootItemRandomChanceCondition.randomChance(0.75F).build()
                },
                BrutalityItems.TRIAL_GUARDIAN_HANDS.get()
        ));



    }
}
