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

public class BrokenHeart extends BrutalityCurioItem {


    public BrokenHeart(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }

    UUID BROKEN_HEART_HEALTH = UUID.fromString("7ab29a91-5d28-4781-9f2d-481e079174fd");
    UUID BROKEN_HEART_CRIT_DAMAGE = UUID.fromString("3a32cd8b-f464-4008-83b6-a61121180604");
    UUID BROKEN_HEART_TENACITY = UUID.fromString("12352f4b-406a-45f7-b9f1-70846497bf8f");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH,
                new AttributeModifier(BROKEN_HEART_HEALTH, "Health Nerf", -3, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                new AttributeModifier(BROKEN_HEART_CRIT_DAMAGE, "Crit Damage Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(ModAttributes.TENACITY.get(),
                new AttributeModifier(BROKEN_HEART_TENACITY, "Tenacity Buff", 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));

        return builder.build();

    }
}
