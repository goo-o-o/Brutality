package net.goo.brutality.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.BrutalityRenderTypes;
import net.goo.brutality.particle.base.TrailParticle;
import net.goo.brutality.util.ModUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class CreaseOfCreationParticle extends TrailParticle {

    private final int entityId;
    private final float offset, radius;
    private final boolean reverseOrbit;

    public final ResourceLocation CENTER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/particle/generic_square_particle.png");

    public CreaseOfCreationParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.alpha = 0.5F;
        this.entityId = entityId;
        this.offset = world.getRandom().nextIntBetweenInclusive(0, 360);
        this.hasPhysics = false;
        this.reverseOrbit = random.nextBoolean();

        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.radius = world.getRandom().nextFloat() * 5;
        this.lifetime = 200;
    }


    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float partialTick) {

        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
        Quaternionf quaternion;
        if (this.roll == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTick, this.oRoll, this.roll);
            quaternion.mul(Axis.ZP.rotation(f3));
        }

        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(BrutalityRenderTypes.itemEntityTranslucentCull(CENTER_TEXTURE));

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.mul(width / 5);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = width / 10;

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        float alpha = 1;
        int j = 240;
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();
        vertexconsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f6).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f5).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f5).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f6).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.endBatch();
        super.render(vertexConsumer, camera, partialTick);
    }

    public void tick() {
        this.tickTrail();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd *= 0.99;
        this.yd *= 0.99;
        this.zd *= 0.99;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd -= this.gravity;
        }

        
        this.oRoll = this.roll;
        this.roll = (float) ((float) Math.PI * Math.sin(age * 0.6F) * 0.3F);
        this.a = 0.2F * Mth.clamp(age / (float) this.lifetime * 32.0F, 0.0F, 1.0F);

        if (entityId != -1) {
            Entity owner = level.getEntity(entityId);

            if (owner instanceof Player) {
                Vec3 entityPos;
                Vec3 targetOrbitPos = getOrbitPos(age * 50 + offset);
                Entity target = ModUtils.getEntityPlayerLookingAt((Player) owner, 25);
                if (target != null) {
                    this.setColor(1F, 0,0);
                    entityPos = new Vec3(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ());
                } else {
                    entityPos = new Vec3(owner.getX(), owner.getY() + owner.getBbHeight() / 2, owner.getZ());
                    this.setColor(0.5F, 0.3F,1F);
                }


                Vec3 targetPos = targetOrbitPos.add(entityPos);

                float lerpFactor = 0.2f;

                double newX = Mth.lerp(lerpFactor, this.x, targetPos.x);
                double newY = Mth.lerp(lerpFactor, this.y, targetPos.y);
                double newZ = Mth.lerp(lerpFactor, this.z, targetPos.z);

//            this.setPos(targetPos.x, targetPos.y, targetPos.z);

                this.setPos(newX, newY, newZ);
            }
        }
    }

    public Vec3 getOrbitPos(float angle) {
        float yOrbit = reverseOrbit ? angle : -angle;
        double x = Mth.cos(angle) * radius;
        double y = Mth.sin(yOrbit) * level.getEntity(entityId).getBbHeight() * (radius / 2);
        double z = Mth.sin(angle) * radius;

        return new Vec3(x, y, z);
    }

}