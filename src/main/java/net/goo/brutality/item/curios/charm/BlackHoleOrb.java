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

public class BlackHoleOrb extends BaseCharmCurio {


    public BlackHoleOrb(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BLACK_HOLE_ORB_MANA_UUID = UUID.fromString("e8317b7b-6efe-46c5-b4de-00600383081e");
    UUID BLACK_HOLE_ORB_MANA_REGEN_UUID = UUID.fromString("d5695526-4e73-4037-8356-6aacbed2209a");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.MAX_MANA.get(), new AttributeModifier(BLACK_HOLE_ORB_MANA_UUID, "Mana Buff", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.MANA_REGEN.get(), new AttributeModifier(BLACK_HOLE_ORB_MANA_REGEN_UUID, "Mana Regen Buff", 10, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

}
