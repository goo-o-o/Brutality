package net.goo.brutality.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.bettercombat.mixin.client.MinecraftClientAccessor;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    // Thank you BetterCombat
    @Inject(
            method = {"render"},
            at = {@At("TAIL")}
    )
    public void renderColliderDebug(PoseStack matrixStack, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (((MinecraftClientAccessor) client).getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            LocalPlayer player = client.player;
            if (player != null) {
                Camera camera = client.gameRenderer.getMainCamera();
                if (camera.isInitialized()) {
                    if (player.isHolding(BrutalityModItems.DEATHSAW.get())) {

                        OrientedBoundingBox.RayData rayData = OrientedBoundingBox.findRayTargets(player, Deathsaw.hitbox);

                        Vec3 cameraOffset = camera.getPosition().reverse();
                        OrientedBoundingBox offsetObb = rayData.obb.offset(cameraOffset).updateVertex();
                        this.brutality$drawOutline(matrixStack, offsetObb, !rayData.entityList.isEmpty());
                    }
                }
            }
        }
    }

    @Unique
    private void brutality$outlineOBB(PoseStack matrixStack, OrientedBoundingBox box, BufferBuilder buffer, float red1, float green1, float blue1, float red2, float green2, float blue2, float alpha) {
        Matrix4f matrix4f = matrixStack.last().pose();
        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(0, 0, 0, 0).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex2.x, (float) box.vertex2.y, (float) box.vertex2.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex3.x, (float) box.vertex3.y, (float) box.vertex3.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex4.x, (float) box.vertex4.y, (float) box.vertex4.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex6.x, (float) box.vertex6.y, (float) box.vertex6.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex2.x, (float) box.vertex2.y, (float) box.vertex2.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex6.x, (float) box.vertex6.y, (float) box.vertex6.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex7.x, (float) box.vertex7.y, (float) box.vertex7.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex3.x, (float) box.vertex3.y, (float) box.vertex3.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex7.x, (float) box.vertex7.y, (float) box.vertex7.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex8.x, (float) box.vertex8.y, (float) box.vertex8.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex4.x, (float) box.vertex4.y, (float) box.vertex4.z).color(red1, green1, blue1, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex8.x, (float) box.vertex8.y, (float) box.vertex8.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(red2, green2, blue2, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(0, 0, 0, 0).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 0, 0).endVertex();
    }

    @Unique
    private void brutality$look(PoseStack matrixStack, OrientedBoundingBox box, BufferBuilder buffer, float alpha) {
        Matrix4f matrix4f = matrixStack.last().pose();

        // Center to axisZ end (red - forward)
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0.0F, 0.0F, 0.0F, alpha).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(1.0F, 0.0F, 0.0F, alpha).endVertex();
        Vec3 zEnd = box.center.add(box.axisZ.scale(1.0));  // Scale to visible length
        buffer.vertex(matrix4f, (float) zEnd.x, (float) zEnd.y, (float) zEnd.z).color(1.0F, 0.0F, 0.0F, alpha).endVertex();

        // Center to axisY (green - up)
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0.0F, 1.0F, 0.0F, alpha).endVertex();
        Vec3 yEnd = box.center.add(box.axisY.scale(1.0));
        buffer.vertex(matrix4f, (float) yEnd.x, (float) yEnd.y, (float) yEnd.z).color(0.0F, 1.0F, 0.0F, alpha).endVertex();

        // Center to axisX (blue - right)
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0.0F, 0.0F, 1.0F, alpha).endVertex();
        Vec3 xEnd = box.center.add(box.axisX.scale(1.0));
        buffer.vertex(matrix4f, (float) xEnd.x, (float) xEnd.y, (float) xEnd.z).color(0.0F, 0.0F, 1.0F, alpha).endVertex();
    }

    @Unique
    private void brutality$drawOutline(PoseStack matrixStack, OrientedBoundingBox obb, boolean collides) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0F);
        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        if (collides) {
            this.brutality$outlineOBB(matrixStack, obb, bufferBuilder, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
        } else {
            this.brutality$outlineOBB(matrixStack, obb, bufferBuilder, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.5F);
        }

        this.brutality$look(matrixStack, obb, bufferBuilder, 0.5F);

//        for(OrientedBoundingBox otherObb : otherObbs) {
//            this.brutality$outlineOBB(matrixStack, otherObb, bufferBuilder, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
//        }

        tesselator.end();
        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
    }
}
