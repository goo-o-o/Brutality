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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class NyxiumAnklet extends BrutalityAnkletItem {


    public NyxiumAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID NYXIUM_ANKLET_DODGE_UUID = UUID.fromString("37d04ae4-d0aa-44e6-aa0b-1548f4feae53");
    UUID NYXIUM_ANKLET_MAX_MANA_UUID = UUID.fromString("dd78dd83-f33a-44de-a3f9-785964839a8c");
    UUID NYXIUM_ANKLET_MANA_REGEN_UUID = UUID.fromString("15715117-1a06-4a5e-95c8-4d3aad702469");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.DODGE_CHANCE.get(),
                new AttributeModifier(NYXIUM_ANKLET_DODGE_UUID, "Dodge Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(ModAttributes.MAX_MANA.get(),
                new AttributeModifier(NYXIUM_ANKLET_MAX_MANA_UUID, "Max Mana", 50, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.MANA_REGEN.get(),
                new AttributeModifier(NYXIUM_ANKLET_MANA_REGEN_UUID, "Mana Regen", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();

    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        dodger.addEffect(new MobEffectInstance(BrutalityModMobEffects.FRUGAL_MANA.get(), 80, 9, false, false));
    }
}
