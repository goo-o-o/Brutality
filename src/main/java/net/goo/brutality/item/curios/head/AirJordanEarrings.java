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
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AirJordanEarrings extends BrutalityCurioItem {


    public AirJordanEarrings(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }


    UUID AJ_JUMP_HEIGHT_UUID = UUID.fromString("7e888136-b3ec-4c4b-ba64-1a3dcbcf24a7");
    UUID AJ_GRAVITY_UUID = UUID.fromString("3db0c5eb-2d3e-41c3-8fa1-ee14679ea2b1");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.JUMP_HEIGHT.get(), new AttributeModifier(AJ_JUMP_HEIGHT_UUID, "Jump Height Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(AJ_GRAVITY_UUID, "Gravity Buff", -0.4F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
    }
}
