package net.goo.brutality.common.item.curios.necklace;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AbyssalNecklace extends BrutalityCurioItem {

    public AbyssalNecklace(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_DAMAGE) {
           return Math.max(5F - ((owner.getY() + 64F) / 384F) * 5F, 0);
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, "MS Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide() && slotContext.entity().isInWaterRainOrBubble()) {
            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            uuid,
                            "Depth AD Bonus",
                            Math.max(5F - ((slotContext.entity().getY() + 64F) / 384F) * 5F, 0),

                            AttributeModifier.Operation.ADDITION
                    ));
        }
        return builder.build();
    }

}
