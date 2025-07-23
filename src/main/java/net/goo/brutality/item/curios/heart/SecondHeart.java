package net.goo.brutality.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class SecondHeart extends BrutalityCurioItem {

    public SecondHeart(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID SECOND_HEART_HP_UUID = UUID.fromString("207f139a-cd1d-4ce6-8f12-0bd57b55a61e");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(SECOND_HEART_HP_UUID, "HP Buff", 12, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

//    @Override
//    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
//        if (slotContext.entity() instanceof Player player) {
//            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
//            if (maxHealth != null) {
//                maxHealth.removeModifier(SECOND_HEART_HP_UUID);
//            }
//        }
//    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }
}
