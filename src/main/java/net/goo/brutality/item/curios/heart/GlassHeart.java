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

public class GlassHeart extends BrutalityCurioItem {


    public GlassHeart(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }

    UUID GLASS_HEART_ARMOR = UUID.fromString("34358f05-8824-41dc-a358-a6628ee696f2");
    UUID GLASS_HEART_AD = UUID.fromString("da1ab729-dc94-49bd-8302-0ceb78b18e6d");
    UUID GLASS_HEART_CRIT_DAMAGE = UUID.fromString("9946b1b9-0f81-4ae8-b7f8-1b7da9d0c955");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.ARMOR,
                new AttributeModifier(GLASS_HEART_ARMOR, "Armor Nerf", -1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(GLASS_HEART_AD, "AD Buff", 0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                new AttributeModifier(GLASS_HEART_CRIT_DAMAGE, "Crit Damage Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE));

        return builder.build();

    }
}
