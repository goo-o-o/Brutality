package net.goo.brutality.item.curios.head;

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

public class FallenAngelsHalo extends BrutalityCurioItem {


    public FallenAngelsHalo(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }

    UUID FALLEN_ANGELS_HALO_LIFESTEAL_UUID = UUID.fromString("2e2c3554-36db-4a59-9918-9a5d71efdb57");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(FALLEN_ANGELS_HALO_LIFESTEAL_UUID,
                    "Lifesteal Boost", 0.075, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


    @Override
    public boolean followHeadRotations() {
        return false;
    }

    @Override
    public boolean translateIfSneaking() {
        return false;
    }
}
