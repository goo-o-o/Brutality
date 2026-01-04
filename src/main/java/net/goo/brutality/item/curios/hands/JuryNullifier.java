package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHandsCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class JuryNullifier extends BaseHandsCurio {


    public JuryNullifier(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID JURY_CRIT_CHANCE_UUID = UUID.fromString("128da7d1-526f-48b6-b82e-80459db1a5c4");
    UUID JURY_AD_UUID = UUID.fromString("988c0d1f-20d1-4a7f-a08a-6692da001a11");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(JURY_CRIT_CHANCE_UUID, "Crit Chance Debuff", -1.5, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(JURY_AD_UUID, "Attack Damage Buff", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
    }

}
