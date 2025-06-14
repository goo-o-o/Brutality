package net.goo.brutality.item.weapon.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class TerratonHammer extends BrutalityHammerItem implements GeoItem {
    private static final UUID MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public TerratonHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(BASE_MOVEMENT_SPEED_UUID, "Speed debuff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }



    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected && pEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4, 8, false, false));
        }
    }
}
