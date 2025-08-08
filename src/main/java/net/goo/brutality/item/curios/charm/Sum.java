package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Sum extends BrutalityCurioItem {
    public static String SUM_DAMAGE = "sumDamage";

    public Sum(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(BrutalityModItems.SCIENTIFIC_CALCULATOR_BELT.get()).isPresent()
                )
                .orElse(false);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item.brutality.sum_charm.damage_stored.1"));
        pTooltipComponents.add(Component.literal(String.valueOf(pStack.getOrCreateTag().getFloat(SUM_DAMAGE))).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }
}
