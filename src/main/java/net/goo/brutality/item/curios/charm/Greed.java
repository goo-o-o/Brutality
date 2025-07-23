package net.goo.brutality.item.curios;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Greed extends BrutalityCurioItem {
    private float bonus = 0;

    public Greed(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID GREED_AD_UUID = UUID.fromString("268f84ae-3357-49c0-9dfc-95b5563c7eed");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (player.level().isClientSide()) return;
            bonus = Math.min(2 * player.level().getNearbyEntities(
                    LivingEntity.class, TargetingConditions.DEFAULT.ignoreLineOfSight(), player, player.getBoundingBox().inflate(7)).size(), 50);


            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                // Remove old modifier (if exists)
                attackDamage.removeModifier(GREED_AD_UUID);

                // Add new modifier with dynamic value
                attackDamage.addTransientModifier(
                        new AttributeModifier(
                                GREED_AD_UUID,
                                "Greed AD Bonus",
                                bonus * 0.01,
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        )
                );
            }
        }
    }

    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                attackDamage.removeModifier(GREED_AD_UUID);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());

        String formattedValue = bonus % 1 == 0 ?
                String.format("%.0f", bonus) :
                String.format("%.1f", bonus);

        tooltip.add(Component.literal((bonus >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(bonus >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }

}
