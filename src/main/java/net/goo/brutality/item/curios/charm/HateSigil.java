package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class HateSigil extends BrutalityCurioItem {


    public HateSigil(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    UUID HATE_SIGIL_MAX_RAGE_UUID = UUID.fromString("192e8875-c1fd-44ac-852b-c69903c53dd1");
    UUID HATE_SIGIL_RAGE_GAIN_UUID = UUID.fromString("bee635ef-d345-4d24-9295-ff7fad619785");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.MAX_RAGE.get(), new AttributeModifier(HATE_SIGIL_MAX_RAGE_UUID, "Max Rage Buff", 100, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.RAGE_GAIN_MULTIPLIER.get(), new AttributeModifier(HATE_SIGIL_RAGE_GAIN_UUID, "Rage Gain Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance maxRage = player.getAttribute(ModAttributes.MAX_RAGE.get());
            if (maxRage != null) {
                maxRage.removeModifier(HATE_SIGIL_MAX_RAGE_UUID);
            }
            AttributeInstance rageGain = player.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
            if (rageGain != null) {
                rageGain.removeModifier(HATE_SIGIL_RAGE_GAIN_UUID);
            }
        }
    }
}
