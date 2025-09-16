package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class UltraDodgeAnklet extends BrutalityAnkletItem {


    public UltraDodgeAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID ULTRA_DODGE_ANKLET_DODGE_UUID = UUID.fromString("8953c020-a12e-43d5-9f49-840691a6d802");
    UUID ULTRA_DODGE_ANKLET_CRIT_CHANCE_UUID = UUID.fromString("fcbc6aef-75bb-4d8f-8be6-b302dd38759a");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(ULTRA_DODGE_ANKLET_DODGE_UUID, "Dodge Buff", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                    new AttributeModifier(ULTRA_DODGE_ANKLET_CRIT_CHANCE_UUID, "Crit Buff", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (dodger instanceof Player wearer) {
            if (!wearer.getCooldowns().isOnCooldown(this)) {
                wearer.addEffect(new MobEffectInstance(BrutalityModMobEffects.ULTRA_DODGE.get(), 100));
                wearer.getCooldowns().addCooldown(this, 100);
            }
        }
    }
}
