package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V")
    )
    private void onTill(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        StatTrakUtils.incrementStatTrakIfPossible(pContext.getItemInHand());
    }

}
