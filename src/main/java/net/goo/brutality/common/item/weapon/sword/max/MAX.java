package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MAX extends Maximus {


    public MAX(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float secondAttackDamage, int lightningQuota, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, secondAttackDamage, lightningQuota, rarity, List.of());
    }


    public static float getDamageBonusFromHealth(Player player, ItemStack stack) {
        float missingHealth = player.getMaxHealth() - player.getHealth();
        if (stack.is(BrutalityItems.MAXIM.get())) {
            return missingHealth * 2;
        } else if (stack.is(BrutalityItems.MAXIMA.get())) {
            return missingHealth * 3;
        } else if (stack.is(BrutalityItems.MAXIMUS.get())) {
            return missingHealth * 4;
        } else if (stack.is(BrutalityItems.MAX.get())) {
            return missingHealth * 5;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.1"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.2"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.3"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.4")
                .withStyle(style -> style.withInsertion(ColorUtils.ColorData.MAX.name()).withBold(true)));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}
