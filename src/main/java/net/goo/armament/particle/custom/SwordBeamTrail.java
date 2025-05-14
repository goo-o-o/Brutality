package net.goo.armament.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.armament.Armament;
import net.goo.armament.particle.base.AbstractTrailParticle;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Locale;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class SwordBeamTrail extends AbstractTrailParticle {

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/particle/generic_trail_particle.png");

    private final int EntityId;
    private final float width;
    private final float height;
    private final float rotateByAge;
    private final float pitch; // Rotation around X-axis (vertical tilt)
    private final float yaw;   // Rotation around Y-axis (horizontal turn)
    private final float roll;

    public SwordBeamTrail(ClientLevel world, double x, double y, double z, float r, float g, float b,
                          float width, float height, int EntityId,
                          float pitch, float yaw, float roll) {
        super(world, x, y, z, 0, 0, 0, r, g, b);
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.EntityId = EntityId;
        this.gravity = 0;
        this.lifetime = 160 + this.random.nextInt(40);
        rotateByAge = (10 + random.nextFloat() * 10F) * (random.nextBoolean() ? -1F : 1F);
        this.width = width;
        this.height = height;
        Vec3 vec3 = getEntityPosition();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    public Vec3 getPlaneNormal() {
        Vec3 normal = new Vec3(0, 1, 0); // Default: vertical (Y-axis)
        // Apply rotations (pitch, yaw, roll)
        normal = rotateAroundX(normal, pitch);
        normal = rotateAroundY(normal, yaw);
        normal = rotateAroundZ(normal, roll);
        return normal;
    }

    // Helper methods for 3D rotations
    private Vec3 rotateAroundX(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x,
                vec.y * cos - vec.z * sin,
                vec.y * sin + vec.z * cos
        );
    }

    private Vec3 rotateAroundY(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x * cos + vec.z * sin,
                vec.y,
                -vec.x * sin + vec.z * cos
        );
    }

    private Vec3 rotateAroundZ(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x * cos - vec.y * sin,
                vec.x * sin + vec.y * cos,
                vec.z
        );
    }

    public Vec3 getEntityPosition() {
        Entity from = this.getFromEntity();
        if (from != null) {
            Vec3 movement = getRenderedDirection();

            return from.position().add(movement.scale(-1F));
        }
        return new Vec3(this.x, this.y, this.z);
    }

    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        if (trailPointer > -1) {
            Vec3 normal = getPlaneNormal();
            Vec3 right = normal.cross(new Vec3(0, 1, 0)).normalize();
            Vec3 up = right.cross(normal).normalize();

            Vec3 topOffset = up.scale(getTrailHeight() / 2F);
            Vec3 bottomOffset = up.scale(-getTrailHeight() / 2F);

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = getVertexConsumer(bufferSource);
            Vec3 cameraPos = camera.getPosition();

            double x = Mth.lerp(partialTick, this.xo, this.x);
            double y = Mth.lerp(partialTick, this.yo, this.y);
            double z = Mth.lerp(partialTick, this.zo, this.z);

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

            int samples = 0;
            Vec3 drawFrom = new Vec3(x, y, z);
            int j = getLightColor(partialTick);

            while (samples < sampleCount()) {
                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
                float u1 = samples / (float) sampleCount();
                float u2 = u1 + 1 / (float) sampleCount();

                PoseStack.Pose pose = poseStack.last();
                Matrix4f matrix4f = pose.pose();
                Matrix3f matrix3f = pose.normal();

                // Render as a flat horizontal plane
                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomOffset.x,
                                (float) drawFrom.y + (float) bottomOffset.y, (float) drawFrom.z + (float) bottomOffset.z)
                        .color(r, g, b, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) bottomOffset.x,
                                (float) sample.y + (float) bottomOffset.y, (float) sample.z + (float) bottomOffset.z)
                        .color(r, g, b, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) topOffset.x,
                                (float) sample.y + (float) topOffset.y, (float) sample.z + (float) topOffset.z)
                        .color(r, g, b, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) topOffset.x,
                                (float) drawFrom.y + (float) topOffset.y, (float) drawFrom.z + (float) topOffset.z)
                        .color(r, g, b, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                samples++;
                drawFrom = sample;
            }

            bufferSource.endBatch();
            poseStack.popPose();
        }
    }

    public Vec3 getRenderedDirection() {
        Entity owner = this.getFromEntity();
        if (owner != null) {
            Vec3 movement = owner.getDeltaMovement();
            if (movement.lengthSqr() < 0.0001) { // Small value to account for floating point errors
                return new Vec3(0, 0, 0); // Not moving
            }
            return movement.normalize();
        }
        return new Vec3(this.x, this.y, this.z);
    }

    public Entity getFromEntity() {
        return EntityId == -1 ? null : level.getEntity(EntityId);
    }

    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.trailA = fade * 2F;
        Vec3 vec3 = getEntityPosition();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if (from == null) {
            remove();
        }

    }

    public Vec3 getOrbitPosition() {
        Vec3 dinoPos = getEntityPosition();
        Vec3 vec3 = new Vec3(0, height * 2, width * 2).yRot((float) Math.toRadians(rotateByAge * age));
        return dinoPos.add(vec3);
    }

    public int sampleCount() {
        return 4;
    }

    public int sampleStep() {
        return 1;
    }

    @Override
    public float getTrailHeight() {
        return 1F;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OrbFactory implements ParticleProvider<SwordBeamTrail.OrbData> {

        @Override
        public Particle createParticle(SwordBeamTrail.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwordBeamTrail particle;
            particle = new SwordBeamTrail(worldIn, x, y, z, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getWidth(),
                    typeIn.getHeight(), typeIn.getEntityID(), typeIn.getPitch(), typeIn.getYaw(), typeIn.getRoll());

            return particle;
        }
    }


    public static class OrbData implements ParticleOptions {
        public static final Deserializer<SwordBeamTrail.OrbData> DESERIALIZER = new Deserializer<SwordBeamTrail.OrbData>() {
            public SwordBeamTrail.@NotNull OrbData fromCommand(@NotNull ParticleType<SwordBeamTrail.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = reader.readFloat();
                reader.expect(' ');
                float g = reader.readFloat();
                reader.expect(' ');
                float b = reader.readFloat();
                reader.expect(' ');
                float width = reader.readFloat();
                reader.expect(' ');
                float height = reader.readFloat();
                reader.expect(' ');
                int EntityId = reader.readInt();
                reader.expect(' ');
                float pitch = reader.readFloat();
                reader.expect(' ');
                float yaw = reader.readFloat();
                reader.expect(' ');
                float roll = reader.readFloat();
                return new SwordBeamTrail.OrbData(r, g, b, width, height, EntityId, pitch, yaw, roll);
            }

            public SwordBeamTrail.OrbData fromNetwork(ParticleType<SwordBeamTrail.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new SwordBeamTrail.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readInt(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            }
        };

        private final float r, g, b, width, height, pitch, yaw, roll;
        private final int entityID;

        public OrbData(float r, float g, float b, float width, float height,
                       int entityID, float pitch, float yaw, float roll) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.width = width;
            this.height = height;
            this.entityID = entityID;
            this.pitch = pitch;
            this.yaw = yaw;
            this.roll = roll;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.width);
            buffer.writeFloat(this.height);
            buffer.writeInt(this.entityID);
            buffer.writeFloat(this.pitch);
            buffer.writeFloat(this.yaw);
            buffer.writeFloat(this.roll);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %d %.2f %.2f %.2f",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b,
                    this.width, this.height,
                    this.entityID,
                    this.pitch, this.yaw, this.roll
            );
        }

        @Override
        public ParticleType<SwordBeamTrail.OrbData> getType() {
            return ModParticles.SWORD_BEAM_TRAIL_PARTICLE.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getR() {
            return this.r;
        }

        @OnlyIn(Dist.CLIENT)
        public float getG() {
            return this.g;
        }

        @OnlyIn(Dist.CLIENT)
        public float getB() {
            return this.b;
        }

        @OnlyIn(Dist.CLIENT)
        public float getWidth() {
            return this.width;
        }

        @OnlyIn(Dist.CLIENT)
        public float getHeight() {
            return this.height;
        }

        @OnlyIn(Dist.CLIENT)
        public int getEntityID() {
            return this.entityID;
        }

        @OnlyIn(Dist.CLIENT)
        public float getPitch() {
            return this.pitch;
        }

        @OnlyIn(Dist.CLIENT)
        public float getYaw() {
            return this.yaw;
        }

        @OnlyIn(Dist.CLIENT)
        public float getRoll() {
            return this.roll;
        }

        @OnlyIn(Dist.CLIENT)
        public Entity getOwner() {
            return this.getOwner();
        }

        public static Codec<SwordBeamTrail.OrbData> CODEC(ParticleType<SwordBeamTrail.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                    Codec.FLOAT.fieldOf("r").forGetter(SwordBeamTrail.OrbData::getR),
                    Codec.FLOAT.fieldOf("g").forGetter(SwordBeamTrail.OrbData::getG),
                    Codec.FLOAT.fieldOf("b").forGetter(SwordBeamTrail.OrbData::getB),
                    Codec.FLOAT.fieldOf("width").forGetter(SwordBeamTrail.OrbData::getWidth),
                    Codec.FLOAT.fieldOf("height").forGetter(SwordBeamTrail.OrbData::getHeight),
                    Codec.INT.fieldOf("entityID").forGetter(SwordBeamTrail.OrbData::getEntityID),
                    Codec.FLOAT.fieldOf("pitch").forGetter(SwordBeamTrail.OrbData::getPitch),
                    Codec.FLOAT.fieldOf("yaw").forGetter(SwordBeamTrail.OrbData::getYaw),
                    Codec.FLOAT.fieldOf("roll").forGetter(SwordBeamTrail.OrbData::getRoll)
                    ).apply(codecBuilder, SwordBeamTrail.OrbData::new)
            );
        }
    }

}