package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {

    @Inject(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z")
    )
    private void onFlattenDirt(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        StatTrakUtils.incrementStatTrakIfPossible(pContext.getItemInHand());
    }

}
