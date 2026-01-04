package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Greed extends BaseCharmCurio {
    public static final String GREED_BONUS = "greed_bonus";
    public Greed(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            int mobCount = player.level().getNearbyEntities(
                    LivingEntity.class,
                    TargetingConditions.DEFAULT.ignoreLineOfSight(),
                    player,
                    player.getBoundingBox().inflate(7)
            ).size();

            int newBonus = Math.min(mobCount * 2, 50);

            CompoundTag tag = stack.getOrCreateTag();
            int currentBonus = tag.getInt(GREED_BONUS);

            if (newBonus == currentBonus) return;

            tag.putInt(GREED_BONUS, newBonus);

        }
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());

        int bonus = stack.getOrCreateTag().getInt(GREED_BONUS);
        tooltip.add(Component.literal((bonus >= 0 ? "+" : "") + bonus +"% ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(bonus >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }

}
