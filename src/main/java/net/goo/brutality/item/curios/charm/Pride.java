package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Pride extends BrutalityCurioItem {
    private static final String CURRENT_BONUS = "current_bonus";

    public Pride(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    private static boolean updateCurrentBonus(Player player, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        float currentBonus = tag.getFloat(CURRENT_BONUS);
        float newBonus = player.getHealth() / player.getMaxHealth() * 40;
        if (currentBonus == newBonus) return false;
        else {
            tag.putFloat(CURRENT_BONUS, newBonus);
            return true;
        }
    }

    UUID PRIDE_AD_UUID = UUID.fromString("81d46a1b-a3aa-4cb8-8999-d60df56c0cb7");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                if (updateCurrentBonus(player, stack)) {
                    attackDamage.removeModifier(PRIDE_AD_UUID);
                    attackDamage.addTransientModifier(
                            new AttributeModifier(
                                    PRIDE_AD_UUID,
                                    "Pride AD Bonus",
                                    stack.getOrCreateTag().getFloat(CURRENT_BONUS) * 0.01,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed != null) {
                attackSpeed.removeModifier(PRIDE_AD_UUID);
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        float value = stack.getOrCreateTag().getFloat("current_bonus");

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }

}
