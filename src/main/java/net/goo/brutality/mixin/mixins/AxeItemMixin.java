package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z")
    )
    private void onStripOrWaxOrScrape(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        StatTrakUtils.incrementStatTrakIfPossible(pContext.getItemInHand());
    }

}
