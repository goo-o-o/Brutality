package net.goo.brutality.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
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

public class NinjaHeart extends BrutalityCurioItem {

    public NinjaHeart(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }

    UUID NINJA_HEART_SLASH_DAMAGE = UUID.fromString("adff9854-92f9-11f0-ac42-325096b39f47");
    UUID NINJA_HEART_SPEED = UUID.fromString("adff9a16-92f9-11f0-b386-325096b39f47");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.SLASH_DAMAGE.get(), new AttributeModifier(NINJA_HEART_SLASH_DAMAGE, "Slash Damage Buff", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(NINJA_HEART_SPEED, "MS Buff", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

}
