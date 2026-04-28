package net.goo.brutality.client.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeRenderTypes;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

// Trail particle with a head for better aesthetics
public class AbstractHeadedTrailParticle extends AbstractTrailParticle {
    private static final ResourceLocation CENTER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/particle/generic_circle_particle.png");
    private static final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/particle/trail/trail_particle.png");


    protected AbstractHeadedTrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.alpha = 1.0F;
        this.hasPhysics = false;
        this.lifetime = 1000;
        this.setColor(1, 1, 1);
    }

    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    protected boolean shouldNotRenderHead() {
        return trailPointer == -1 || age > sampleCount();
    }

    protected void renderHead(Camera camera, float partialTick) {
        Vec3 cameraPos = camera.getPosition();

        // 1. Smoothly interpolate the position between ticks
        double renderX = Mth.lerp(partialTick, this.xo, this.x) - cameraPos.x();
        double renderY = Mth.lerp(partialTick, this.yo, this.y) - cameraPos.y();
        double renderZ = Mth.lerp(partialTick, this.zo, this.z) - cameraPos.z();

        PoseStack posestack = new PoseStack();
        posestack.pushPose();

        // 2. Move the stack to the interpolated position
        posestack.translate(renderX, renderY, renderZ);

        // 3. Apply Billboard rotation (remains the same)
        Quaternionf rotation = new Quaternionf(camera.rotation());
        if (this.roll != 0.0F) {
            float lerpedRoll = Mth.lerp(partialTick, this.oRoll, this.roll);
            rotation.mul(Axis.ZP.rotation(lerpedRoll));
        }
        posestack.mulPose(rotation);

        float scale = this.getTrailHeight() / (age * 0.5F + 6);
        posestack.scale(scale, scale, scale);

        // 5. Get the matrix
        PoseStack.Pose lastPose = posestack.last();
        Matrix4f matrix4f = lastPose.pose();

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer buffer = bufferSource.getBuffer(BrutalityRenderTypes.entityTranslucentCull(this.getTexture()));

        int light = 240;
        float alpha = this.getAlpha();

        // 6. Render 4 corners of the quad
        Vec3[] vertices = {
                new Vec3(-1.0, -1.0, 0.0), // Bottom-Left
                new Vec3(-1.0, 1.0, 0.0), // Top-Left
                new Vec3(1.0, 1.0, 0.0), // Top-Right
                new Vec3(1.0, -1.0, 0.0)  // Bottom-Right
        };

        float[][] uvs = {
                {1.0F, 1.0F},
                {1.0F, 0.0F},
                {0.0F, 0.0F},
                {0.0F, 1.0F}
        };

        for (int i = 0; i < 4; i++) {
            Vec3 v = vertices[i];
            Vector4f pos = new Vector4f((float) v.x, (float) v.y, (float) v.z, 1.0F);
            pos.mul(matrix4f);

            buffer.vertex(matrix4f, (float)v.x, (float)v.y, (float)v.z)
                    .color(this.rCol, this.gCol, this.bCol, alpha)
                    .uv(uvs[i][0], uvs[i][1])
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(posestack.last().normal(), 0.0F, 1.0F, 0.0F)
                    .endVertex();
        }

        posestack.popPose();

        // Flush the buffer to ensure rendering
//        bufferSource.endBatch();
    }

    public void render(VertexConsumer ignoredConsumer, Camera camera, float partialTick) {
        super.render(ignoredConsumer, camera, partialTick);
        if (shouldNotRenderHead()) return;
        renderHead(camera, partialTick);
    }

    protected VertexConsumer getVertexConsumer(MultiBufferSource.BufferSource bufferSource) {
        return bufferSource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(this.getTrailTexture()));
    }

    public float getAlpha() {
        return Mth.clamp(1.0F - (float) this.age / (float) this.lifetime, 0.0F, 1.0F);
    }

    public ResourceLocation getTexture() {
        return CENTER_TEXTURE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;


        // Entity is gone, start aging to fade out
        this.age++;
        // Remove once the trail has fully dispersed
        if (this.age > this.sampleCount()) {
            this.remove();
        }

        this.tickTrail();
    }

    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    @Override
    public int getTrailColor(float percentageFromHead) {
        return WHITE;
    }

    public float getTrailHeight() {
        return 0.15F;
    }

    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    public int sampleCount() {
        return 30;
    }
}
