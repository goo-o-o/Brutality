package net.goo.brutality.item.weapon.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.ModMobEffects;
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
import java.util.function.Consumer;

public class RoyalGuardianSword extends BrutalitySwordItem {
    public RoyalGuardianSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
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
            builder.putAll(modifiers); // keep existing modifiers

            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            BASE_ENTITY_INTERACTION_RANGE_UUID,
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
        pTarget.addEffect(new MobEffectInstance(ModMobEffects.PULVERIZED.get(), 3, 5));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
