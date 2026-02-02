package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Greed extends BrutalityCurioItem {

    public Greed(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_DAMAGE) {
            return Math.min(getMobCount(owner) * 2, 50);
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    private int getMobCount(LivingEntity wearer) {
        return wearer.level().getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.DEFAULT.ignoreLineOfSight(),
                wearer,
                wearer.getBoundingBox().inflate(7)
        ).size();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            LivingEntity wearer = slotContext.entity();


            int newBonus = Math.min(getMobCount(wearer) * 2, 50);

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Greed bonus", newBonus, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


    //    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (slotContext.entity() instanceof Player player) {
//            int mobCount = player.level().getNearbyEntities(
//                    LivingEntity.class,
//                    TargetingConditions.DEFAULT.ignoreLineOfSight(),
//                    player,
//                    player.getBoundingBox().inflate(7)
//            ).size();
//
//            int newBonus = Math.min(mobCount * 2, 50);
//
//            CompoundTag tag = stack.getOrCreateTag();
//            int currentBonus = tag.getInt(GREED_BONUS);
//
//            if (newBonus == currentBonus) return;
//
//            tag.putInt(GREED_BONUS, newBonus);
//
//        }
//    }
//
//
//    @Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
//        super.appendHoverText(stack, world, tooltip, flag);
//        tooltip.add(Component.empty());
//
//        int bonus = stack.getOrCreateTag().getInt(GREED_BONUS);
//        tooltip.add(Component.literal((bonus >= 0 ? "+" : "") + bonus +"% ").append(Component.translatable("attribute.name.generic.attack_damage"))
//                .withStyle(bonus >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
//
//    }

}
