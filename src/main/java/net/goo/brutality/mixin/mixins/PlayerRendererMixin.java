package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.charm.PoseidonsBlessing;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Redirect(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getSwimAmount(F)F"))
    private float modifySwimAmount(AbstractClientPlayer instance, float partialTicks) {
        if (PoseidonsBlessing.shouldApply(instance))
            return 0.0F;
        return instance.getSwimAmount(partialTicks);
    }
}
