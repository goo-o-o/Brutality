package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class MechanicalAorta extends BrutalityCurioItem {


    public MechanicalAorta(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    UUID MECH_AORTA_RAGE_GAIN_UUID = UUID.fromString("5a5cbb13-3506-4102-99c6-9c0b39bbb8c7");
    UUID MECH_AORTA_RAGE_TIME_UUID = UUID.fromString("daa0998d-794d-4998-8734-0a77b161c4e4");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.RAGE_GAIN_MULTIPLIER.get(), new AttributeModifier(MECH_AORTA_RAGE_GAIN_UUID, "Rage Gain Buff", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(ModAttributes.RAGE_TIME_MULTIPLIER.get(), new AttributeModifier(MECH_AORTA_RAGE_TIME_UUID, "Rage Time Debuff", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (player.tickCount % 5 == 0) {
                if (player.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                    player.invulnerableTime = 0;
                    player.hurt(player.damageSources().indirectMagic(player, player), 1);
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance rageGain = player.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
            if (rageGain != null) {
                rageGain.removeModifier(MECH_AORTA_RAGE_GAIN_UUID);
            }
            AttributeInstance rageTime = player.getAttribute(ModAttributes.RAGE_TIME_MULTIPLIER.get());
            if (rageTime != null) {
                rageTime.removeModifier(MECH_AORTA_RAGE_TIME_UUID);
            }
        }
    }
}
