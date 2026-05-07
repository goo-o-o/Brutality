package net.goo.brutality.common.registry.items;

import com.google.common.util.concurrent.AtomicDouble;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.curios.BrutalityGastronomyCurioItem;
import net.goo.brutality.common.item.curios.charm.IndustrialFreezer;
import net.goo.brutality.common.item.weapon.hammer.WhiskHammer;
import net.goo.brutality.common.registry.*;
import net.goo.brutality.util.BrutalityTags;
import net.goo.brutality.util.CooldownUtils;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.build_archetypes.GastronomyDebuffContainer;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static net.goo.brutality.common.registry.BrutalityItems.ITEMS;
import static net.goo.brutality.util.tooltip.ItemDescriptionComponent.ItemDescriptionComponents.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL;

public class BrutalityGastronomyItems {

    public static final RegistryObject<Item> WHISK_HAMMER = ITEMS.register("whisk", () -> new WhiskHammer(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> CRIMSON_DELIGHT = ITEMS.register("crimson_delight", () -> new BrutalityThrowingItem(
            2, -1.5F, Rarity.EPIC,
            BrutalityEntities.CRIMSON_DELIGHT));
    public static final RegistryObject<Item> CANNONBALL_CABBAGE = ITEMS.register("cannonball_cabbage", () -> new BrutalityThrowingItem(
            5, -2.2F, Rarity.EPIC,
            BrutalityEntities.CANNONBALL_CABBAGE));
    public static final RegistryObject<Item> CAVENDISH = ITEMS.register("cavendish", () -> new BrutalityThrowingItem(
            2, -2.2F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.CAVENDISH));
    public static final RegistryObject<Item> STICK_OF_BUTTER = ITEMS.register("stick_of_butter", () -> new BrutalityThrowingItem(
            0, -3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.STICK_OF_BUTTER));
    public static final RegistryObject<Item> WINTER_MELON = ITEMS.register("winter_melon", () -> new BrutalityThrowingItem(
            7, -2.75F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.WINTER_MELON));
    public static final RegistryObject<Item> GOLDEN_PHOENIX = ITEMS.register("golden_phoenix", () -> new BrutalityThrowingItem(
            9, -2.65F, Rarity.EPIC,
            BrutalityEntities.GOLDEN_PHOENIX));
    public static final RegistryObject<Item> OVERCLOCKED_TOASTER = ITEMS.register("overclocked_toaster", () -> new BrutalityThrowingItem(
            10, -3.5F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.OVERCLOCKED_TOASTER));


    public static final RegistryObject<Item> CHOCOLATE_BAR = ITEMS.register("chocolate_bar", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.15, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ATTACK_SPEED, 0.15, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> BOX_OF_CHOCOLATES = ITEMS.register("box_of_chocolates", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.STEALTH.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> ESSENTIAL_OILS = ITEMS.register("essential_oils", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0) {
                slotContext.entity().addEffect(new MobEffectInstance(BrutalityEffects.OILED.get(), 60, 1));
            }
        }
    });
    public static final RegistryObject<Item> FRIDGE = ITEMS.register("fridge", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1)))
            .withAttributes(new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_DURATION_MULTIPLIER.get(), 1, ADDITION)));
    public static final RegistryObject<Item> SMART_FRIDGE = ITEMS.register("smart_fridge", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(LORE, 1)))
            .withAttributes(new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_DURATION_MULTIPLIER.get(), 2, ADDITION)));
    public static final RegistryObject<Item> INDUSTRIAL_FREEZER = ITEMS.register("industrial_freezer", () -> new IndustrialFreezer(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(LORE, 1)))
            .withAttributes(new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_DURATION_MULTIPLIER.get(), 3, ADDITION)));

    public static final RegistryObject<Item> PICKLE_JAR = ITEMS.register("pickle_jar", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY)
            .withDebuffs(new GastronomyDebuffContainer(true, BrutalityEffects.PICKLED, 200, 1))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_DURATION_MULTIPLIER.get(), 0.75F, ADDITION),
                    new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_LEVEL_MODIFIER.get(), -1, ADDITION)
            ));

    public static final RegistryObject<Item> SALT_SHAKER = ITEMS.register("salt_shaker", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SALTED, 60, 1)));

    public static final RegistryObject<Item> PEPPER_SHAKER = ITEMS.register("pepper_shaker", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.PEPPERED, 60, 1)));

    public static final RegistryObject<Item> SALT_AND_PEPPER = ITEMS.register("salt_and_pepper", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.MYTHIC).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SALTED, 120, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.PEPPERED, 120, 2)
    ));


    public static final RegistryObject<Item> BAMBOO_STEAMER = ITEMS.register("bamboo_steamer", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.STEAMED, 60, 1)));

    public static final RegistryObject<Item> SMOKE_STONE = ITEMS.register("smoke_stone", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SMOKED, 60, 1)));

    public static final RegistryObject<Item> THE_SMOKEHOUSE = ITEMS.register("the_smokehouse", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.FABLED).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SMOKED, 120, 2)));

    public static final RegistryObject<Item> CONVECTION_SMOKER = ITEMS.register("convection_smoker", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.MYTHIC).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SMOKED, 120, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.STEAMED, 120, 2)
    ));

    public static final RegistryObject<Item> SMOKED_PAPRIKA = ITEMS.register("smoked_paprika", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.BARK, 60, 1)));

    public static final RegistryObject<Item> APPLE_CIDER_VINEGAR = ITEMS.register("apple_cider_vinegar", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.PICKLED, 60, 1)));

    public static final RegistryObject<Item> BBQ_RUB = ITEMS.register("bbq_rub", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.BARK, 60, 1),
            new GastronomyDebuffContainer(false, BrutalityEffects.SALTED, 60, 1),
            new GastronomyDebuffContainer(false, BrutalityEffects.PEPPERED, 60, 1)
    ));
    public static final RegistryObject<Item> BBQ_SAUCE = ITEMS.register("bbq_sauce", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.BARK, 60, 1),
            new GastronomyDebuffContainer(true, BrutalityEffects.SLICKED, 60, 1),
            new GastronomyDebuffContainer(true, BrutalityEffects.PICKLED, 60, 1)
    ));
    public static final RegistryObject<Item> COMMERCIALIZED_BBQ_SEASONING = ITEMS.register("commercialized_bbq_seasoning", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_HIT, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingEntity && !source.is(BrutalityDamageTypes.PULVERIZED)) {
                livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.PULVERIZED.get(), 2, 1));
            }
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    }.withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.BARK, 120, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.SALTED, 120, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.PEPPERED, 120, 2),
            new GastronomyDebuffContainer(true, BrutalityEffects.SLICKED, 120, 2),
            new GastronomyDebuffContainer(true, BrutalityEffects.PICKLED, 120, 2)
    ));


    public static final RegistryObject<Item> SUGAR_GLAZE = ITEMS.register("sugar_glaze", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.GLAZED, 60, 1)));

    public static final RegistryObject<Item> RAINBOW_SPRINKLES = ITEMS.register("rainbow_sprinkles", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SPRINKLED, 60, 1)));

    public static final RegistryObject<Item> ROCK_CANDY_RING = ITEMS.register("rock_candy_ring", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.CANDIED, 60, 1)));

    public static final RegistryObject<Item> SEARED_SUGAR_BROOCH = ITEMS.register("seared_sugar_brooch", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.CARAMELIZED, 60, 1)));

    public static final RegistryObject<Item> CARAMEL_CRUNCH_MEDALLION = ITEMS.register("caramel_crunch_medallion", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.CARAMELIZED, 80, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.CANDIED, 80, 2)
    ));

    public static final RegistryObject<Item> DUNKED_DONUT = ITEMS.register("dunked_donut", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SPRINKLED, 80, 2),
            new GastronomyDebuffContainer(false, BrutalityEffects.GLAZED, 80, 2)
    ));

    public static final RegistryObject<Item> LOLLIPOP_OF_ETERNITY = ITEMS.register("lollipop_of_eternity", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GODLY).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SPRINKLED, 120, 3),
            new GastronomyDebuffContainer(false, BrutalityEffects.GLAZED, 120, 3),
            new GastronomyDebuffContainer(false, BrutalityEffects.CARAMELIZED, 120, 3),
            new GastronomyDebuffContainer(false, BrutalityEffects.CANDIED, 120, 3)
    ));

    public static final RegistryObject<Item> ICE_CREAM_SANDWICH = ITEMS.register("ice_cream_sandwich", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL).withAttributes(
            new AttributeContainer(BrutalityAttributes.GASTRONOMY_DAMAGE_DEALT_BOOST.get(), 0.2, ADDITION)));

    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.PULVERIZED, 3, 2)
    ));

    public static final RegistryObject<Item> BUTTER_GAUNTLETS = ITEMS.register("butter_gauntlets", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_TRUE_MELEE_HIT, 1, 60))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, float amount) {
            if (curio.is(BrutalityTags.Items.GASTRONOMIST_ITEMS) && victim instanceof LivingEntity livingVictim && attacker instanceof Player playerAttacker) {
                if (AttributeCalculationHelper.Luck.roll(attacker, 0.1F, 0.1F))
                    CooldownUtils.validateCurioCooldown(playerAttacker, curio.getItem(), 60, () ->
                            livingVictim.addEffect(new MobEffectInstance(BrutalityEffects.STUNNED.get(), 4, 0)));
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, amount);
        }
    });

    public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SLICKED, 60, 1)));

    public static final RegistryObject<Item> CHEESE_SAUCE = ITEMS.register("cheese_sauce", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SLICKED, 60, 1)));

    public static final RegistryObject<Item> PIZZA_SLOP = ITEMS.register("pizza_slop", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SLICKED, 120, 2)));
    public static final RegistryObject<Item> ITALIAN_CLASSIC = ITEMS.register("italian_classic", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC).withDebuffs(
            new GastronomyDebuffContainer(false, BrutalityEffects.SLICKED, 120, 3),
            new GastronomyDebuffContainer(false, BrutalityEffects.OILED, 120, 5)
    ));
    public static final RegistryObject<Item> UVOGRE_STEAK = ITEMS.register("uvogre_steak", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))
    ) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
        }
    }.withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SLICKED, 60, 1)
    ).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, 6, ADDITION)));
    public static final RegistryObject<Item> SHADOWFLAME_SEARED_STEAK = ITEMS.register("shadowflame_seared_steak", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.NOCTURNAL, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))
    ) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
        }
    }.withDebuffs(
            new GastronomyDebuffContainer(true, BrutalityEffects.SEARED, 90, 2)
    ).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, 12, ADDITION),
            new AttributeContainer(Attributes.MAX_HEALTH, 0.1, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_LEVEL_MODIFIER.get(), 1, ADDITION)
    ));
    public static final RegistryObject<Item> GOLDEN_DELIGHT = ITEMS.register("golden_delight", () -> new BrutalityGastronomyCurioItem(BrutalityRarities.GODLY)
            .withAttributes(
                    new AttributeContainer(Attributes.MAX_HEALTH, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ATTACK_KNOCKBACK, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ATTACK_SPEED, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ARMOR, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.LUCK, 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.BLOCK_REACH.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.ENTITY_REACH.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.NAMETAG_DISTANCE.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.COIN_COOLDOWN.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.GASTRONOMY_DAMAGE_DEALT_BOOST.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.GASTRONOMY_DEBUFF_LEVEL_MODIFIER.get(), 1, ADDITION),
                    new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.RAGE_LEVEL.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.MANA_COST.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.MANA_REGEN.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SPELL_COOLDOWN.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.CAST_TIME.get(), -0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SPELL_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.OMNIVAMP.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.JUMP_HEIGHT.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.STUN_CHANCE.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.STUN_DURATION.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.TENACITY.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.PIERCING_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.AXE_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SWORD_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.HAMMER_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SPEAR_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.SCYTHE_DAMAGE.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.STEALTH.get(), 0.08, ADDITION),
                    new AttributeContainer(BrutalityAttributes.THROW_STRENGTH.get(), 0.08, MULTIPLY_TOTAL),
                    new AttributeContainer(BrutalityAttributes.DAMAGE_TAKEN.get(), -0.08, MULTIPLY_TOTAL)
            ));


    public static final RegistryObject<Item> HOT_SAUCE = ITEMS.register("hot_sauce", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(BrutalityEffects.HOT_AND_SPICY.get(), 41, 1));
        }
    });

    public static final RegistryObject<Item> STRAWBERRY_SMOOTHIE = ITEMS.register("strawberry_smoothie", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL, List.of(new ItemDescriptionComponent(ON_TRUE_MELEE_HIT, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, float amount) {
            victim.setTicksFrozen(victim.getTicksFrozen() + 40);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, amount);
        }
    });

    public static final RegistryObject<Item> BANANA_SMOOTHIE = ITEMS.register("banana_smoothie", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL, List.of(new ItemDescriptionComponent(ON_TRUE_MELEE_HIT, 1, 20 * 5))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, float amount) {
            if (attacker instanceof Player player) {
                AtomicDouble damage = new AtomicDouble();
                CooldownUtils.validateCooldown(player, BrutalityGastronomyItems.BANANA_SMOOTHIE.get(), 20 * 5, () ->
                        damage.set((double) victim.getTicksFrozen() / 20 * 0.5F));
                return (float) (amount + damage.get());
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, amount);
        }
    });
    public static final RegistryObject<Item> BLUEBERRY_SMOOTHIE = ITEMS.register("blueberry_smoothie", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim.getTicksFrozen() > 0) return 1.15F * amount;
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });
    public static final RegistryObject<Item> MANGO_SMOOTHIE = ITEMS.register("mango_smoothie", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (attacker.level().getBiome(attacker.blockPosition()).value().coldEnoughToSnow(attacker.blockPosition())) return amount * 1.15F;
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });
    public static final RegistryObject<Item> MIXED_BERRY_SMOOTHIE = ITEMS.register("mixed_berry_smoothie", () -> new BrutalityGastronomyCurioItem(
            BrutalityRarities.GLACIAL, List.of(
                    new ItemDescriptionComponent(PASSIVE, 2),
                    new ItemDescriptionComponent(ON_TRUE_MELEE_HIT, 2, 100)
            )) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, float amount) {
            victim.setTicksFrozen(victim.getTicksFrozen() + 40);
            if (attacker instanceof Player player) {
                AtomicDouble damage = new AtomicDouble();
                CooldownUtils.validateCooldown(player, BrutalityGastronomyItems.MIXED_BERRY_SMOOTHIE.get(), 20 * 5, () ->
                        damage.set((double) victim.getTicksFrozen() / 20 * 0.75F));
                return (float) (amount + damage.get());
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, amount);
        }
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            float mult = 1;
            if (victim.getTicksFrozen() > 0) mult += 0.2F;
            if (attacker.level().getBiome(attacker.blockPosition()).value().coldEnoughToSnow(attacker.blockPosition())) mult += 0.2F;
            return amount * mult;
        }
    });

    public static final RegistryObject<Item> OLIVE_OIL = ITEMS.register("olive_oil", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC)
            .withDebuffs(
                    new GastronomyDebuffContainer(true, BrutalityEffects.OILED, 60, 2)));

    public static final RegistryObject<Item> EXTRA_VIRGIN_OLIVE_OIL = ITEMS.register("extra_virgin_olive_oil", () -> new BrutalityGastronomyCurioItem(
            Rarity.EPIC)
            .withDebuffs(
                    new GastronomyDebuffContainer(false, BrutalityEffects.OILED, 120, 4)));
}
