package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.BrutalityRenderTypes;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.Locale;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class AbstractCameraAlignedTrailParticle extends Particle {

    private final Vec3[] trailPositions = new Vec3[64];
    private final int entityId;
    public int trailPointer = -1;
    public int sampleCount;

    public float trailR, trailG, trailB, width;

    protected float trailA = 0.4F;

    public AbstractCameraAlignedTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, int entityId, int sampleCount) {
        super(world, x, y, z);
        this.trailR = r;
        this.trailG = g;
        this.trailB = b;
        this.entityId = entityId;
        this.width = width;
        this.sampleCount = sampleCount;
    }

    public Entity getFromEntity() {
        return entityId == -1 ? null : level.getEntity(entityId);
    }


    public Vec3 getEntityCenter() {
        Entity from = this.getFromEntity();
        if (from != null) {
            // Get center position of the entity's hitbox
            return from.position().add(0, from.getBbHeight() / 2, 0);
        }
        return new Vec3(this.x, this.y, this.z);
    }


    @Override
    public boolean shouldCull() {
        return false;
    }

    public void tick() {
        super.tick();
        tickTrail();
//        float fade = 1F - age / (float) lifetime;
//        this.trailA = fade * 2F;
        Vec3 vec3 = getEntityCenter();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if (from == null) {
            remove();
        }
    }


    public void tickTrail() {

        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        if (trailPointer == -1) {
            Arrays.fill(trailPositions, currentPosition);
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }

    public float getTrailWidth() {
        return this.width;
    }

    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        if (trailPointer > -1) {
            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexconsumer = getVertexConsumer(multibuffersource$buffersource);

            Vec3 cameraPos = camera.getPosition();
            double x = (float) (Mth.lerp((double) partialTick, this.xo, this.x));
            double y = (float) (Mth.lerp((double) partialTick, this.yo, this.y));
            double z = (float) (Mth.lerp((double) partialTick, this.zo, this.z));

            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            int samples = 0;
            Vec3 drawFrom = new Vec3(x, y, z);
            float zRot = getTrailRot(camera);
            Vec3 topAngleVec = new Vec3(0, getTrailWidth() / 2F, 0).zRot(zRot);
            Vec3 bottomAngleVec = new Vec3(0, getTrailWidth() / -2F, 0).zRot(zRot);
            int j = getLightColor(partialTick);
            while (samples < sampleCount()) {
                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
                float u1 = samples / (float) sampleCount();
                float u2 = u1 + 1 / (float) sampleCount();

                Vec3 draw1 = drawFrom;
                Vec3 draw2 = sample;

                PoseStack.Pose posestack$pose = posestack.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y, (float) draw1.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) bottomAngleVec.x, (float) draw2.y + (float) bottomAngleVec.y, (float) draw2.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) draw2.x + (float) topAngleVec.x, (float) draw2.y + (float) topAngleVec.y, (float) draw2.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y, (float) draw1.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                samples++;
                drawFrom = sample;
            }
            multibuffersource$buffersource.endBatch();
            posestack.popPose();
        }
    }

    protected VertexConsumer getVertexConsumer(MultiBufferSource.BufferSource multibuffersource$buffersource) {
        return multibuffersource$buffersource.getBuffer(BrutalityRenderTypes.LIGHT_TRAIL_EFFECT.apply(getTrailTexture()));
    }

    protected ResourceLocation getTrailTexture() {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/particle/circle_trail_particle.png");
    }

    public float getTrailRot(Camera camera) {
        return -0.017453292F * camera.getXRot();
    }

    public int sampleCount() {
        return this.sampleCount;
    }

    public int sampleStep() {
        return 1;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.removed) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }


    public static final class OrbFactory implements ParticleProvider<AbstractCameraAlignedTrailParticle.OrbData> {

        @Override
        public Particle createParticle(AbstractCameraAlignedTrailParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AbstractCameraAlignedTrailParticle particle;
            particle = new AbstractCameraAlignedTrailParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(),
                    typeIn.entityID(), typeIn.sampleCount());

            return particle;
        }


    }


    public record OrbData(float r, float g, float b, float width, int entityID, int sampleCount)
            implements ParticleOptions {
        public static final Deserializer<AbstractCameraAlignedTrailParticle.OrbData> DESERIALIZER = new Deserializer<>() {
            public AbstractCameraAlignedTrailParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<AbstractCameraAlignedTrailParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = reader.readFloat();
                reader.expect(' ');
                float g = reader.readFloat();
                reader.expect(' ');
                float b = reader.readFloat();
                reader.expect(' ');
                float width = reader.readFloat();
                reader.expect(' ');
                int EntityId = reader.readInt();
                reader.expect(' ');
                int sampleCount = reader.readInt();
                return new AbstractCameraAlignedTrailParticle.OrbData(r, g, b, width, EntityId, sampleCount);
            }

            public AbstractCameraAlignedTrailParticle.OrbData fromNetwork(ParticleType<AbstractCameraAlignedTrailParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new AbstractCameraAlignedTrailParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readInt(), buffer.readInt());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.width);
            buffer.writeInt(this.entityID);
            buffer.writeInt(this.sampleCount);

        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d  %d",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b,
                    this.width,
                    this.entityID,
                    this.sampleCount
            );
        }

        @Override
        public ParticleType<AbstractCameraAlignedTrailParticle.OrbData> getType() {
            return BrutalityModParticles.GENERIC_CAMERA_ALIGNED_TRAIL_PARTICLE.get();
        }

        @Override
        public float r() {
            return this.r;
        }

        @Override
        public float g() {
            return this.g;
        }

        @Override
        public float b() {
            return this.b;
        }

        @Override
        public float width() {
            return this.width;
        }

        @Override
        public int entityID() {
            return this.entityID;
        }


        public static Codec<AbstractCameraAlignedTrailParticle.OrbData> CODEC(ParticleType<AbstractCameraAlignedTrailParticle.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(AbstractCameraAlignedTrailParticle.OrbData::r),
                            Codec.FLOAT.fieldOf("g").forGetter(AbstractCameraAlignedTrailParticle.OrbData::g),
                            Codec.FLOAT.fieldOf("b").forGetter(AbstractCameraAlignedTrailParticle.OrbData::b),
                            Codec.FLOAT.fieldOf("width").forGetter(AbstractCameraAlignedTrailParticle.OrbData::width),
                            Codec.INT.fieldOf("entityID").forGetter(AbstractCameraAlignedTrailParticle.OrbData::entityID),
                            Codec.INT.fieldOf("sampleCount").forGetter(AbstractCameraAlignedTrailParticle.OrbData::sampleCount)
                    ).apply(codecBuilder, AbstractCameraAlignedTrailParticle.OrbData::new)
            );
        }
    }
}