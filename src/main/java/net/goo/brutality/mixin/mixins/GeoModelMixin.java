package net.goo.brutality.mixin.mixins;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(GeoModel.class)
public class GeoModelMixin {
    @Unique
    ResourceLocation brutality$missing = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/missingno.png");

    @Inject(method = "getTextureResource(Lsoftware/bernie/geckolib/core/animatable/GeoAnimatable;Lsoftware/bernie/geckolib/renderer/GeoRenderer;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true, remap = false)
    private <T extends GeoAnimatable> void force404Texture(T animatable, @Nullable GeoRenderer<T> renderer, CallbackInfoReturnable<ResourceLocation> cir) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            if (cir.getReturnValue() != null && cir.getReturnValue().equals(brutality$missing)) {
                return;
            }


            cir.setReturnValue(brutality$missing); // this doesnt actually exist, which actually works since we want the missing texture
        }
    }

}
