package net.goo.brutality.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.item.weapon.generic.LastPrism;
import net.goo.brutality.item.weapon.scythe.Schism;
import net.goo.brutality.item.weapon.spear.Caldrith;
import net.goo.brutality.item.weapon.spear.Rhongomyniad;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    // Thank you BetterCombat
    @Inject(
            method = {"render"},
            at = {@At("TAIL")}
    )
    public void renderColliderDebug(PoseStack matrixStack, MultiBufferSource.BufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if ((client).getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            LocalPlayer player = client.player;
            if (player != null) {
                Camera camera = client.gameRenderer.getMainCamera();
                if (camera.isInitialized()) {
                    OrientedBoundingBox.TargetResult<LivingEntity> targetResult;

                    if (player.isHolding(BrutalityModItems.DEATHSAW.get())) {
                        targetResult = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, Deathsaw.HITBOX, new Vec3(0, 0, 1), true);
                    } else if (player.isHolding(BrutalityModItems.RHONGOMYNIAD.get())) {
                        targetResult = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, Rhongomyniad.HITBOX, new Vec3(0, 0, 3), true);
                    } else if (player.isHolding(BrutalityModItems.LAST_PRISM_ITEM.get())) {
                        targetResult = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, LastPrism.HITBOX, new Vec3(0, 0, 3), true);
                    } else if (player.isHolding(BrutalityModItems.CALDRITH.get())) {
                        targetResult = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, Caldrith.HITBOX, new Vec3(0, 2.5, -9), 0, player.getYRot(), 0, false);
                    } else if (player.isHolding(BrutalityModItems.SCHISM.get())) {
                        targetResult = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, Schism.ARC_SWEEP_45, new Vec3(0, 0, 8), false);
                    } else {
                        targetResult = null;
                    }

                    if (targetResult != null) {
                        this.brutality$drawOutline(camera, matrixStack, targetResult);
                    }
                }
            }
        }
    }

    @Unique
    private static void brutality$renderOBB(PoseStack pose, OrientedBoundingBox obb, BufferBuilder buffer, float r, float g, float b, float a) {
        Matrix4f m = pose.last().pose();
        Vec3 c = obb.center;
        Vec3 hx = obb.getLocalAxis(0).scale(obb.halfExtents.x); // right
        Vec3 hy = obb.getLocalAxis(1).scale(obb.halfExtents.y); // up
        Vec3 hz = obb.getLocalAxis(2).scale(obb.halfExtents.z); // forward

        Vec3 v000 = c.subtract(hx).subtract(hy).subtract(hz);
        Vec3 v100 = c.add(hx).subtract(hy).subtract(hz);
        Vec3 v110 = c.add(hx).add(hy).subtract(hz);
        Vec3 v010 = c.subtract(hx).add(hy).subtract(hz);
        Vec3 v001 = c.subtract(hx).subtract(hy).add(hz);
        Vec3 v101 = c.add(hx).subtract(hy).add(hz);
        Vec3 v111 = c.add(hx).add(hy).add(hz);
        Vec3 v011 = c.subtract(hx).add(hy).add(hz);

        Vec3[] verts = {v000, v100, v110, v010, v001, v101, v111, v011};

        int[] indices = {
                0, 1, 1, 2, 2, 3, 3, 0, // bottom
                4, 5, 5, 6, 6, 7, 7, 4, // top
                0, 4, 1, 5, 2, 6, 3, 7  // sides
        };

        for (int i : indices) {
            Vec3 v = verts[i];
            buffer.vertex(m, (float) v.x, (float) v.y, (float) v.z).color(r, g, b, a).endVertex();
        }
    }

    @Unique
    private <T extends Entity> void brutality$drawOutline(Camera camera, PoseStack matrixStack, OrientedBoundingBox.TargetResult<T> targetResult) {


        boolean collides = !targetResult.entities.isEmpty();
        List<OrientedBoundingBox> hitboxes = targetResult.hitboxes;

        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();


        hitboxes.forEach(obb -> {
            OrientedBoundingBox offset = obb.translateWorld(camera.getPosition().reverse());

            bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
            brutality$renderOBB(matrixStack, offset, bufferBuilder,
                    collides ? 1f : 0f,
                    collides ? 0f : 1f,
                    0f, 0.5f);
            tesselator.end();
        });

        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableBlend();
    }
}
