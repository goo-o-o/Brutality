package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
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

public class DavysAnklet extends BrutalityAnkletItem {


    public DavysAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID DAVY_ANKLET_DODGE_UUID = UUID.fromString("b757b3e8-be44-4e14-8fab-864592d7f8c2");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(DAVY_ANKLET_DODGE_UUID, "Dodge Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (source.getEntity() instanceof LivingEntity attacker && dodger instanceof Player wearer) {
            if (!wearer.getCooldowns().isOnCooldown(this)) {
                attacker.addEffect(new MobEffectInstance(TerramityModMobEffects.VULNERABLE.get(), 200));
                wearer.getCooldowns().addCooldown(this, 100);
            }
        }
    }
}
