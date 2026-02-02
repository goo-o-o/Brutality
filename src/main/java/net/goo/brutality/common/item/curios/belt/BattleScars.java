package net.goo.brutality.common.item.curios.belt;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BattleScars extends BrutalityCurioItem {


    public BattleScars(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get()) {
            int count = 0;
            for (MobEffectInstance mobEffectInstance : owner.getActiveEffects())
                if (mobEffectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) count++;

            return count * 0.15;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();

            int count = 0;
            for (MobEffectInstance mobEffectInstance : slotContext.entity().getActiveEffects())
                if (mobEffectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) count++;

            builder.put(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(),
                    new AttributeModifier(uuid, "Damage to Rage buff", count * 0.15F, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (slotContext.entity() instanceof Player player) {
//            AttributeInstance rageGainAttr = player.getAttribute(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get());
//            if (rageGainAttr != null) {
//                List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream().toList().stream()
//                        .filter(mobEffectInstance -> mobEffectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL).toList();
//
//
//                if (!harmfulEffects.isEmpty()) {
//                    rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);
//
//                    rageGainAttr.addTransientModifier(
//                            new AttributeModifier(
//                                    BATTLE_SCARS_RAGE_GAIN_UUID,
//                                    "Temporary Speed Bonus",
//                                    harmfulEffects.size() * 0.15F,
//                                    AttributeModifier.Operation.MULTIPLY_TOTAL
//                            )
//                    );
//                } else {
//                    rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
//        AttributeInstance rageGainAttr = slotContext.entity().getAttribute(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get());
//        if (rageGainAttr != null) {
//            rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);
//        }
//    }
}
