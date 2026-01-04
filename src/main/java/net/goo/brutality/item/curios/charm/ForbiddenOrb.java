package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ForbiddenOrb extends BaseCharmCurio {


    public ForbiddenOrb(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID FORBIDDEN_ORB_MANA_UUID = UUID.fromString("cb1dff4a-00e8-427e-9170-56d7274c4fac");
    UUID FORBIDDEN_ORB_MANA_COST_UUID = UUID.fromString("7f8ac53e-6eed-41b3-bf37-5608a2d84179");
    UUID FORBIDDEN_ORB_MANA_REGEN_UUID = UUID.fromString("3a14914f-c051-4669-a6a5-b30f14fac799");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.MAX_MANA.get(), new AttributeModifier(FORBIDDEN_ORB_MANA_UUID, "Mana Buff", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.MANA_COST.get(), new AttributeModifier(FORBIDDEN_ORB_MANA_COST_UUID, "Mana Cost Debuff", 0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.MANA_REGEN.get(), new AttributeModifier(FORBIDDEN_ORB_MANA_REGEN_UUID, "Mana Regen Buff", 0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

}
