package net.goo.brutality.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
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
                        Deathsaw.HITBOX.inWorld(player, Deathsaw.OFFSET).render(matrixStack, vertexConsumers.getBuffer(RenderType.LINES), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
//                    } else if (player.isHolding(BrutalityModItems.RHONGOMYNIAD.get())) {
//                        targetResult = OrientedBoundingBox.findEntitiesHit(player, LivingEntity.class, Rhongomyniad.HITBOX, new Vec3(0, 0, 3), true);
//                    } else if (player.isHolding(BrutalityModItems.LAST_PRISM_ITEM.get())) {
//                        targetResult = OrientedBoundingBox.findEntitiesHit(player, LivingEntity.class, LastPrism.HITBOX, new Vec3(0, 0, 3), true);
//                    } else if (player.isHolding(BrutalityModItems.CALDRITH.get())) {
//                        targetResult = OrientedBoundingBox.findEntitiesHit(player, LivingEntity.class, Caldrith.HITBOX, new Vec3(0, 2.5, -9), 0, player.getYRot(), 0, false);
//                    } else if (player.isHolding(BrutalityModItems.SCHISM.get())) {
//                        targetResult = OrientedBoundingBox.findEntitiesHit(player, LivingEntity.class, Schism.ARC_SWEEP_45, new Vec3(0, 0, 8), false);
//                    } else {
//                        targetResult = null;
                    }

                }

        }
    }

}
