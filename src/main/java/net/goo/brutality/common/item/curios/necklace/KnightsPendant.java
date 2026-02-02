package net.goo.brutality.common.item.curios.necklace;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
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

public class KnightsPendant extends BrutalityCurioItem {
    public KnightsPendant(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }



    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.ARMOR_PENETRATION.get()) {
            if (owner.getHealth() / owner.getMaxHealth() < 0.75f) {
                return 0.05;
            }
        }
        return 0;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(BrutalityAttributes.SWORD_DAMAGE.get(), new AttributeModifier(uuid, "Sword Damage Buff", 4, AttributeModifier.Operation.ADDITION));
        builder.put(BrutalityAttributes.LETHALITY.get(), new AttributeModifier(uuid, "Lethality Buff", 4, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.75F) {
                builder.put(BrutalityAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(uuid, "Armor Pen Buff", 0.05, AttributeModifier.Operation.ADDITION));
            }
        }
        return builder.build();
    }
}
