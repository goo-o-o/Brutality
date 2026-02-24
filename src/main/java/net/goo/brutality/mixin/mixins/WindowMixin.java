package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.platform.Window;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    private void capFps(CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.isHolding(BrutalityItems.OLD_GPU.get())) cir.setReturnValue(10);
        }
    }
}
