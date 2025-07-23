package net.goo.brutality.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DigDurabilityEnchantment.class)
public abstract class DigDurabilityEnchantmentMixin {

    @Inject(method = "shouldIgnoreDurabilityDrop", at = @At("HEAD"), cancellable = true)
    private static void doubleDownBypass(ItemStack pStack, int pLevel, RandomSource pRandom, CallbackInfoReturnable<Boolean> cir) {
        if (pStack.getOrCreateTag().getBoolean("fromDoubleDown")) {
            cir.setReturnValue(false);
        }
    }

}