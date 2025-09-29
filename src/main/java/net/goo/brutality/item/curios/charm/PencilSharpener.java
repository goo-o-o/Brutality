package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class PencilSharpener extends BrutalityCurioItem {


    public PencilSharpener(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID PENCIL_SHARPENER_PIERCING = UUID.fromString("da7c068a-7e9c-46c0-b679-8c87051fbaec");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.PIERCING_DAMAGE.get(), new AttributeModifier(PENCIL_SHARPENER_PIERCING,
                "Piercing Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }
}
