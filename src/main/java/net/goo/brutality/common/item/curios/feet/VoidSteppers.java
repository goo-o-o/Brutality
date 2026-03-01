package net.goo.brutality.common.item.curios.feet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class VoidSteppers extends BrutalityCurioItem {
    protected float speedPerLevel;
    protected float stealth;

    public VoidSteppers(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, float speedPerLevel, float stealth) {
        super(rarity, descriptionComponents);
        this.speedPerLevel = speedPerLevel;
        this.stealth = stealth;
    }



    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.MOVEMENT_SPEED) {
            return speedPerLevel * getSpeedMultiplier(owner);
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    private int getSpeedMultiplier(LivingEntity owner) {
        return 15 - owner.level().getMaxLocalRawBrightness(owner.getOnPos().above());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(BrutalityAttributes.STEALTH.get(), new AttributeModifier(uuid, "Stealth Buff", 0.5F, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Speed Buff", speedPerLevel * getSpeedMultiplier(slotContext.entity()), AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
