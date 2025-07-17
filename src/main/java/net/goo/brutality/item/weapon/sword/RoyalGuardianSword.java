package net.goo.brutality.item.weapon.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class RoyalGuardianSword extends BrutalitySwordItem {

    private final UUID ROYAL_GUARDIAN_SWORD_INTERACTION_RANGE_UUID = UUID.fromString("d5e9bd65-eedc-4fef-be5d-dc14ef8fdb0a");

    public RoyalGuardianSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public float shadowSize() {
        return 3F;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityItemRenderer.class);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);


        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);

            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            ROYAL_GUARDIAN_SWORD_INTERACTION_RANGE_UUID,
                            "Reach bonus",
                            14.0,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }

        return modifiers;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected && pEntity instanceof LivingEntity livingEntity)
            livingEntity.addEffect(new MobEffectInstance(TerramityModMobEffects.MISPROGRAMMED.get(), 1, 0));

    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 24));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
