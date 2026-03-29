package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.charm.Censored;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    /**
     * 1. Modify the DisplayName (pDisplayName)
     * We target the HEAD and use argsOnly to swap the component before 
     * the rest of the method (like font width calculation) runs.
     */
    @ModifyVariable(method = "renderNameTag", at = @At("HEAD"), argsOnly = true)
    private Component modifyDisplayName(Component original, T pEntity) {
        if (Censored.shouldRedact(pEntity)) {
            return Component.literal("REDACTED");
        }
        return original;
    }

    /**
     * 3. Modify drawInBatch (Text and Plate Color)
     * We use Redirect to catch both calls to drawInBatch.
     */
    @Redirect(
            method = "renderNameTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"
            )
    )
    private int customDrawInBatch(Font instance, Component pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLight, T pEntity) {

        if (Censored.shouldRedact(pEntity)) {
            return instance.drawInBatch(pText, pX, pY, 0xFF000000, false, pMatrix, pBuffer, pDisplayMode, 0xFF000000, pPackedLight);
        }

        return instance.drawInBatch(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLight);
    }

}