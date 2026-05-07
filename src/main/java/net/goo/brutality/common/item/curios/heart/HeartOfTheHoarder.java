package net.goo.brutality.common.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.build_archetypes.CoinHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class HeartOfTheHoarder extends BrutalityCurioItem {
    public HeartOfTheHoarder(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(SlotContext slotContext, ItemStack stack, AttributeInstance attributeInstance, double currentBonus) {
        if (attributeInstance.getAttribute() == Attributes.ATTACK_DAMAGE) {
            return CoinHelper.getNearbyCoinCount(slotContext.entity(), 5, null);
        }
        return super.getDynamicAttributeBonus(slotContext, stack, attributeInstance, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity player = slotContext.entity();

            double healthBonus = CoinHelper.getNearbyCoinCount(player, 5, null);

            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "MaxHealth", healthBonus, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
