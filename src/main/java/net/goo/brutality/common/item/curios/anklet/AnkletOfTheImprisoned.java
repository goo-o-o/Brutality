package net.goo.brutality.common.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AnkletOfTheImprisoned extends BrutalityAnkletItem {


    public AnkletOfTheImprisoned(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 20 == 0)
            slotContext.entity().hurt(slotContext.entity().damageSources().generic(), 1);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {

        if (attribute == BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get() || attribute == BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get()) {
            float missingHealth = owner.getMaxHealth() - owner.getHealth(); // only get attribute values once you are sure they do not recurse
            return missingHealth * 0.075F;
        }
        return 0;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity livingEntity = slotContext.entity();
            float missingHealth = livingEntity.getMaxHealth() - livingEntity.getHealth();
            float newBonus = missingHealth * 0.075F;

            builder.put(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(),
                    new AttributeModifier(uuid, "Crit Chance Buff", newBonus, AttributeModifier.Operation.ADDITION));
            builder.put(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                    new AttributeModifier(uuid, "Crit Damage Buff", newBonus, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
