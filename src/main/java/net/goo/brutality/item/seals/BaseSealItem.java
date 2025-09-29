package net.goo.brutality.item.seals;

import net.goo.brutality.util.SealUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class BaseSealItem extends Item {
    public BaseSealItem(Properties pProperties) {
        super(pProperties);
    }

    public SealUtils.SEAL_TYPE getSealType() {
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.brutality." + getSealType().name().toLowerCase(Locale.ROOT) + "_seal.passive.1"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
