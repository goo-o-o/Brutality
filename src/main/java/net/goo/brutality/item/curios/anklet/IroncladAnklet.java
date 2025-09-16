package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class IroncladAnklet extends BrutalityAnkletItem {


    public IroncladAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID IRONCLAD_ANKLET_DODGE_UUID = UUID.fromString("fec0ada6-e38f-49c6-b650-6127ef334255");
    UUID IRONCLAD_ANKLET_SPEED_UUID = UUID.fromString("dd78dd83-f33a-44de-a3f9-785964839a8c");
    UUID IRONCLAD_ANKLET_ARMOR_UUID = UUID.fromString("15715117-1a06-4a5e-95c8-4d3aad702469");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.DODGE_CHANCE.get(),
                new AttributeModifier(IRONCLAD_ANKLET_DODGE_UUID, "Dodge Buff", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(IRONCLAD_ANKLET_SPEED_UUID, "Speed", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ARMOR,
                new AttributeModifier(IRONCLAD_ANKLET_ARMOR_UUID, "Armor", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();

    }

    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        dodger.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, false, false));
    }
}
