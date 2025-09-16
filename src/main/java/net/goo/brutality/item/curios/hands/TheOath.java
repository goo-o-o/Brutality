package net.goo.brutality.item.curios.hands;

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

public class TheOath extends BrutalityCurioItem {
    public TheOath(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HANDS;
    }

    UUID THE_OATH_BLUNT_DAMAGE = UUID.fromString("18ea46f8-dd82-4e3a-9a5f-2acfd23ad70f");
    UUID THE_OATH_CELESTIA_SCHOOL_LEVEL = UUID.fromString("6a56ae66-ee05-47cc-8ee7-0a230690ddca");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.BLUNT_DAMAGE.get(), new AttributeModifier(THE_OATH_BLUNT_DAMAGE, "Blunt Damage Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.CELESTIA_SCHOOL_LEVEL.get(), new AttributeModifier(THE_OATH_CELESTIA_SCHOOL_LEVEL, "Celestia Buff", 1, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
