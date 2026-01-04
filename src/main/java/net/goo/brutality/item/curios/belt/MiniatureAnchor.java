package net.goo.brutality.item.curios.belt;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseBeltCurio;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class MiniatureAnchor extends BaseBeltCurio {


    public MiniatureAnchor(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID MINIATURE_ANCHOR_SWIM_SPEED = UUID.fromString("bcece337-55e7-4246-ab1e-09b3faace18b");
    UUID MINIATURE_ANCHOR_KB_RESIST = UUID.fromString("60bf6e05-ce6d-4cf4-91df-c7141f621317");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(MINIATURE_ANCHOR_KB_RESIST,
                "KB Resist Buff", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(MINIATURE_ANCHOR_SWIM_SPEED,
                "Swim Speed Nerf", -0.9F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
            if (slotContext.entity().isUnderWater()) {
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 11, 3));
            }
        }
    }
}
