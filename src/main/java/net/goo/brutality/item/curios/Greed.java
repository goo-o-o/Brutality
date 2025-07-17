package net.goo.brutality.item.curios;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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

public class Pride extends BrutalityCurioItem {


    public Pride(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    private static float getCurrentBonus() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return Math.min(2 * player.level().getNearbyEntities(
                    LivingEntity.class, TargetingConditions.DEFAULT.ignoreLineOfSight(), player, player.getBoundingBox().inflate(7)).size(), 50);
        } else return 0;
    }

    UUID PRIDE_AD_UUID = UUID.fromString("268f84ae-3357-49c0-9dfc-95b5563c7eed");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                // Remove old modifier (if exists)
                attackDamage.removeModifier(PRIDE_AD_UUID);

                // Add new modifier with dynamic value
                attackDamage.addTransientModifier(
                        new AttributeModifier(
                                PRIDE_AD_UUID,
                                "Pride AD Bonus",
                                getCurrentBonus(),
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        )
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        float value = getCurrentBonus();

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }

}
