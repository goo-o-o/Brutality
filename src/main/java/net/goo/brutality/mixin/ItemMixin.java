package net.goo.brutality.mixin;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.util.SealUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void addHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced, CallbackInfo ci) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(pStack);
        if (sealType != null && sealType.showDescription()) {
            String key = Brutality.MOD_ID + "." + sealType.name().toLowerCase(Locale.ROOT) + "_seal";
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable(key));
            if (pStack.is(Tags.Items.ARMORS) || pStack.getItem() instanceof ArmorItem) {
                pTooltipComponents.add(Component.translatable(key + ".armor"));
            } else if (pStack.getItem() instanceof BrutalityThrowingItem || pStack.getItem() instanceof BowItem || pStack.is(Tags.Items.TOOLS_BOWS)) {
                pTooltipComponents.add(Component.translatable(key + ".projectile"));
            } else
                pTooltipComponents.add(Component.translatable(key + ".item"));

            if (pStack.isEnchanted() && ((6 & ItemStack.TooltipPart.ENCHANTMENTS.getMask()) == 0)) {
                pTooltipComponents.add(Component.empty());
            }
        }
    }

}
