package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ScribesIndex extends BrutalityCurioItem {


    public ScribesIndex(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(SlotContext slotContext, ItemStack stack, AttributeInstance attributeInstance, double currentBonus) {
        if (attributeInstance.getAttribute() == BrutalityAttributes.SPELL_DAMAGE.get() && slotContext.entity() instanceof Player player) {
            return ManaHelper.isMaxMana(player) ? 0.25F : -0.1F;
        }
        return super.getDynamicAttributeBonus(slotContext, stack, attributeInstance, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            float newBonus = ManaHelper.isMaxMana(player) ? 0.25F : -0.1F;

            builder.put(BrutalityAttributes.SPELL_DAMAGE.get(), new AttributeModifier(uuid, "Spell Damage Buff", newBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
