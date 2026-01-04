package net.goo.brutality.item.curios.head;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHeadCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ScopeGoggles extends BaseHeadCurio {


    public ScopeGoggles(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID SCOPE_CRIT_DAMAGE_UUID = UUID.fromString("e2b95665-f4a8-4018-aeb9-0cfdfa2e9f80");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(), new AttributeModifier(SCOPE_CRIT_DAMAGE_UUID, "Crit Damage Buff", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
    }
}
