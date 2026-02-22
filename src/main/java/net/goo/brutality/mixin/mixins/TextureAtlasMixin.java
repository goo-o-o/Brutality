package net.goo.brutality.mixin.mixins;

import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(TextureAtlas.class)
public class TextureAtlasMixin {
    @Shadow
    private Map<ResourceLocation, TextureAtlasSprite> texturesByName;

    @Inject(method = "getSprite", at = @At("HEAD"), cancellable = true)
    private void forceMissingTexture(ResourceLocation pName, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            ResourceLocation missingLoc = MissingTextureAtlasSprite.getLocation();
            if (!pName.equals(missingLoc)) {

                TextureAtlasSprite missingSprite = this.texturesByName.get(missingLoc);

                if (missingSprite != null) {
                    cir.setReturnValue(missingSprite);
                }
            }
        }
    }
}
