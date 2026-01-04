package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.item.weapon.generic.LastPrism;
import net.goo.brutality.item.weapon.scythe.Schism;
import net.goo.brutality.item.weapon.spear.Caldrith;
import net.goo.brutality.item.weapon.spear.Rhongomyniad;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {
    @Shadow public abstract void render(PoseStack pPoseStack, MultiBufferSource.BufferSource pBufferSource, double pCamX, double pCamY, double pCamZ);

    @Inject(
            method = {"render"},
            at = {@At("TAIL")}
    )
    public void renderColliderDebug(PoseStack matrixStack, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if ((client).getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            LocalPlayer player = client.player;
            if (player != null) {
                    if (player.isHolding(BrutalityModItems.DEATHSAW.get())) {
                        Deathsaw.HITBOX.inWorld(player, Deathsaw.OFFSET).render(matrixStack);
                    } else if (player.isHolding(BrutalityModItems.RHONGOMYNIAD.get())) {
                        Rhongomyniad.HITBOX.inWorld(player, player.getEyePosition(), Rhongomyniad.OFFSET).render(matrixStack);
                    } else if (player.isHolding(BrutalityModItems.LAST_PRISM_ITEM.get())) {
                        LastPrism.HITBOX.inWorld(player, player.getEyePosition(), LastPrism.OFFSET).render(matrixStack);
                    } else if (player.isHolding(BrutalityModItems.CALDRITH.get())) {
                        Caldrith.HITBOX.inWorld(player, Caldrith.OFFSET, 0, player.getYRot()).render(matrixStack);
                    } else if (player.isHolding(BrutalityModItems.SCHISM.get())) {
                        Schism.HITBOX.inWorld(player, Schism.OFFSET).render(matrixStack);
                    }

                }

        }
    }

}
