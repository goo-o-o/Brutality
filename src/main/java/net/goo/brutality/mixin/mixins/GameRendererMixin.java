package net.goo.brutality.mixin.mixins;

import net.goo.brutality.client.event.forge.ShaderRenderEvents;
import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "resize", at = @At("HEAD"))
    private void onResize(int width, int height, CallbackInfo ci) {
        // Resize post chain internals
        if (BrutalityShaders.itemOutlinePostShader != null)
            BrutalityShaders.itemOutlinePostShader.resize(width, height);
        if (BrutalityShaders.maxSwordOutlinePostShader != null)
            BrutalityShaders.maxSwordOutlinePostShader.resize(width, height);

        ShaderRenderEvents.resizeAllTargets(width, height);
    }
}