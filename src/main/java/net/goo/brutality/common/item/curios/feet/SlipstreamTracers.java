package net.goo.brutality.common.item.curios.feet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
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

public class SlipstreamTracers extends BrutalityCurioItem {
    public SlipstreamTracers(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    Object2IntOpenHashMap<UUID> sprintTimeMap = new Object2IntOpenHashMap<>();

    private float getSpeedBonusFromSprintTime(LivingEntity livingEntity) {
        // 10% per second so 1% per 2 ticks
        return 0.01F * sprintTimeMap.getOrDefault(livingEntity.getUUID(), 0);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        sprintTimeMap.put(slotContext.entity().getUUID(), 0);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        sprintTimeMap.removeInt(slotContext.entity().getUUID());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        UUID uuid = livingEntity.getUUID();
        if (livingEntity.tickCount % 2 == 0)
            if (livingEntity.isSprinting())
                sprintTimeMap.mergeInt(uuid, 2, Integer::sum);
            else sprintTimeMap.removeInt(uuid);

    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.MOVEMENT_SPEED) {
            return currentBonus * getSpeedBonusFromSprintTime(owner);
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement Speed Buff", getSpeedBonusFromSprintTime(slotContext.entity()), AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
