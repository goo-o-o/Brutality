package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin<T> {

    @Shadow
    T value;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void brutality$shadowOptionValue(CallbackInfoReturnable<T> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Options options = mc.options;
        Object currentOption = this; // The specific OptionInstance being queried

        if (mc.player.isHolding(BrutalityItems.OLD_GPU.get())) {
            if (currentOption == options.enableVsync()) {
                cir.setReturnValue((T) Boolean.FALSE);
                return;
            } else if (currentOption == options.ambientOcclusion()) {
                cir.setReturnValue((T) Boolean.FALSE);
                return;
            } else if (currentOption == options.graphicsMode()) {
                cir.setReturnValue((T) GraphicsStatus.FAST);
                return;
            } else if (currentOption == options.renderDistance()) {
                cir.setReturnValue((T) Integer.valueOf(2));
                return;
            }
        }

        if (mc.player.hasEffect(BrutalityEffects.DESPAIR.get()) && !mc.player.hasEffect(BrutalityEffects.HOPE.get())) {
            int level = mc.player.getEffect(BrutalityEffects.DESPAIR.get()).getAmplifier() + 1;

            if (currentOption == options.sensitivity()) {
                double originalSens = (Double) value;
                // Calculate total: 100% - (4% * levels)
                double modifier = 1.0 - (0.04 * level);
                cir.setReturnValue((T) Double.valueOf(originalSens * modifier));
            } else if (currentOption == options.gamma()) {
                double originalGamma = (Double) value;
                // Calculate total: 100% - (10% * levels)
                double modifier = 1.0 - (0.10 * level);
                cir.setReturnValue((T) Double.valueOf(originalGamma * modifier));
            }
        }
    }
}
