package net.goo.brutality.mixin.mixins;

import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Model.class)
public abstract class ModelMixin {
    @Shadow @Final protected Function<ResourceLocation, RenderType> renderType;

    @Inject(method = "renderType", at = @At(value = "RETURN"), cancellable = true)
    private void modifyTexture(ResourceLocation pLocation, CallbackInfoReturnable<RenderType> cir) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            ResourceLocation missing = MissingTextureAtlasSprite.getLocation();

            // 1. Check if the location is already missingno
            if (pLocation.equals(missing)) {
                return;
            }

            // 2. Return a RenderType using the missing texture
            cir.setReturnValue(this.renderType.apply(missing));
        }
    }
}