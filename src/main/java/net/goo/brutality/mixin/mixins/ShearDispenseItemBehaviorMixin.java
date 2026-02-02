package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearDispenseItemBehaviorMixin {

    @Inject(method = "execute", at = @At(value = "RETURN"))
    private void successfulAction(BlockSource pSource, ItemStack pStack, CallbackInfoReturnable<ItemStack> cir) {
        ShearsDispenseItemBehavior shearsDispenseItemBehavior = (((ShearsDispenseItemBehavior) (Object) this));
        if (shearsDispenseItemBehavior.isSuccess())
            StatTrakUtils.incrementStatTrakIfPossible(pStack);
    }
}
