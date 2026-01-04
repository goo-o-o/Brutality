package net.goo.brutality.item.curios.belt;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseBeltCurio;
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

public class ArchmagesTrick extends BaseBeltCurio {
    public ArchmagesTrick(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID ARCHMAGES_TRICK_HEALTH_UUID = UUID.fromString("3253917d-aae7-48b9-8f1d-1b7d629d256c");
    UUID ARCHMAGES_TRICK_MAX_MANA_UUID = UUID.fromString("5f93fe5b-d5a5-457a-ab5e-eeb8f4686550");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(ARCHMAGES_TRICK_HEALTH_UUID, "Health Debuff", -0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        AttributeInstance health = slotContext.entity().getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            AttributeInstance mana = slotContext.entity().getAttribute(ModAttributes.MAX_MANA.get());
            if (mana != null) {
                mana.addTransientModifier(new AttributeModifier(ARCHMAGES_TRICK_MAX_MANA_UUID, "Mana Buff", health.getValue() / 2F * 15F, AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        AttributeInstance mana = slotContext.entity().getAttribute(ModAttributes.MAX_MANA.get());
        if (mana != null) {
            mana.removeModifier(ARCHMAGES_TRICK_MAX_MANA_UUID);
        }
    }

//    AttributeModifier healthMod = health.getModifier(ARCHMAGES_TRICK_HEALTH_UUID);
//            if (healthMod != null) {
//        double healthLoss = healthMod.getAmount();
//        AttributeInstance mana = slotContext.entity().getAttribute(ModAttributes.MAX_MANA.get());
//        if (mana != null) {
//            mana.addTransientModifier(new AttributeModifier(ARCHMAGES_TRICK_MAX_MANA_UUID, "Mana Buff", healthLoss * -10F, AttributeModifier.Operation.ADDITION));
//        }
//    }
}
