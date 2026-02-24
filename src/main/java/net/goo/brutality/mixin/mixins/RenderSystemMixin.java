package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = RenderSystem.class, remap = false)
public class RenderSystemMixin {

    // TODO: This is interesting for future reference, it makes the entire texture atlas a single image, could be useful for doing a pure white world
//    @Inject(
//        method = "_setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
//        at = @At("HEAD"),
//        cancellable = true
//    )
//    private static void redirectTextures(int unit, ResourceLocation location, CallbackInfo ci) {
//        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
//
//            // Filter out things you DON'T want to be 404 (like the UI/Font)
//            if (location.getPath().contains("gui") || location.getPath().contains("font")) {
//                return;
//            }
//
//            // Redirect to the missing texture
//            ResourceLocation missing = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/item/big_bertha.png");
//
//            // We call the original method with the missing texture instead
//            // Use the internal GL binding call to avoid recursion
//            if (!location.equals(missing)) {
//                RenderSystem._setShaderTexture(unit, missing);
//                ci.cancel();
//            }
//        }
//    }
}