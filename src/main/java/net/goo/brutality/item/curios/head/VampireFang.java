package net.goo.brutality.item.curios.head;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class VampireFang extends BrutalityCurioItem {


    public VampireFang(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }

    UUID VAMPIRE_FANG_LIFESTEAL_UUID = UUID.fromString("85a82b28-7e3e-498d-911c-b9a314f43c1f");
    UUID VAMPIRE_FANG_CRIT_DAMAGE_UUID = UUID.fromString("9dfd2614-48d5-46ad-9800-27b07f4ed721");
    UUID VAMPIRE_FANG_HEALTH_UUID = UUID.fromString("acd3f4eb-d209-40fc-84a8-82c6422a2d5f");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(VAMPIRE_FANG_LIFESTEAL_UUID,
                    "Lifesteal Boost", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(), new AttributeModifier(VAMPIRE_FANG_CRIT_DAMAGE_UUID,
                    "Crit Damage Boost", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(VAMPIRE_FANG_HEALTH_UUID,
                    "Health Nerf", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            AttributeInstance lifesteal = slotContext.entity().getAttribute(ModAttributes.LIFESTEAL.get());
            if (lifesteal != null) {
                lifesteal.removeModifier(VAMPIRE_FANG_LIFESTEAL_UUID);
            }
            AttributeInstance critDamage = slotContext.entity().getAttribute(ModAttributes.CRITICAL_STRIKE_DAMAGE.get());
            if (critDamage != null) {
                critDamage.removeModifier(VAMPIRE_FANG_CRIT_DAMAGE_UUID);
            }
            AttributeInstance health = slotContext.entity().getAttribute(Attributes.MAX_HEALTH);
            if (health != null) {
                health.removeModifier(VAMPIRE_FANG_HEALTH_UUID);
            }
    }
}
