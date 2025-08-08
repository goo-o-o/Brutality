package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModParticles;
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
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Locale;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class AbstractWorldAlignedTrailParticle extends AbstractCameraAlignedTrailParticle {


    private final int entityId, sampleCount;
    private final float width, pitch, yaw, roll;
    private final String texture;

    public AbstractWorldAlignedTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b,
                                             float width, int entityId,
                                             float pitch, float yaw, float roll, String texture, int sampleCount) {
        super(world, x, y, z, r, g, b, width, entityId, sampleCount);
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.entityId = entityId;
        this.gravity = 0;
        this.lifetime = 2000;
        this.width = width;
        Vec3 vec3 = getEntityCenter();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setAlpha(0.25F);
        this.texture = texture;
        this.sampleCount = sampleCount;
    }

    public Vec3 getPlaneNormal() {
        Vector3f normal = new Vector3f(0, 1, 0); // Upward-facing normal

        Matrix4f rotation = new Matrix4f()
                .rotateY(yaw)   // horizontal turn
                .rotateZ(pitch) // vertical tilt
                .rotateX(roll); // roll

// Transform the normal vector using the rotation matrix
        Vector4f rotated = new Vector4f(normal, 0.0f).mul(rotation);

// Return as Vec3
        return new Vec3(rotated.x(), rotated.y(), rotated.z());

    }




    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        if (trailPointer > -1) {
            Vec3 normal = getPlaneNormal();

            // Apply width scaling in world space
            Vec3 topOffset = normal.scale(width * 1.5);
            Vec3 bottomOffset = normal.scale(-width * 1.5);

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

                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomOffset.x,
                                (float) drawFrom.y + (float) bottomOffset.y, (float) drawFrom.z + (float) bottomOffset.z)
                        .color(trailR, trailG, trailB, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j)
                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) bottomOffset.x,
                                (float) sample.y + (float) bottomOffset.y, (float) sample.z + (float) bottomOffset.z)
                        .color(trailR, trailG, trailB, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j)
                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) topOffset.x,
                                (float) sample.y + (float) topOffset.y, (float) sample.z + (float) topOffset.z)
                        .color(trailR, trailG, trailB, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j)
                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();

                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) topOffset.x,
                                (float) drawFrom.y + (float) topOffset.y, (float) drawFrom.z + (float) topOffset.z)
                        .color(trailR, trailG, trailB, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j)
                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();

                samples++;
                drawFrom = sample;
            }

            bufferSource.endBatch();
            poseStack.popPose();
        }
    }


    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.trailA = fade * 2F;

        Entity from = this.getFromEntity();
        if (from == null) {
            remove();
        }

    }

    @Override
    public int sampleCount() {
        return this.sampleCount;
    }

    @Override
    protected ResourceLocation getTrailTexture() {
        return new ResourceLocation(Brutality.MOD_ID, "textures/particle/" + texture + "_trail_particle.png");
    }

    public static final class OrbFactory implements ParticleProvider<AbstractWorldAlignedTrailParticle.OrbData> {

        @Override
        public Particle createParticle(AbstractWorldAlignedTrailParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AbstractWorldAlignedTrailParticle particle;
            particle = new AbstractWorldAlignedTrailParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(),
                    typeIn.entityID(), typeIn.pitch(), typeIn.yaw(), typeIn.roll(), typeIn.texture(), typeIn.sampleCount());

            return particle;
        }
    }


    public record OrbData(float r, float g, float b, float width, int entityID, float pitch, float yaw, float roll, String texture, int sampleCount)
            implements ParticleOptions {
            public static final Deserializer<OrbData> DESERIALIZER = new Deserializer<>() {
                public AbstractWorldAlignedTrailParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                    float pitch = reader.readFloat();
                    reader.expect(' ');
                    float yaw = reader.readFloat();
                    reader.expect(' ');
                    float roll = reader.readFloat();
                    reader.expect(' ');
                    String texture = reader.readString();
                    reader.expect(' ');
                    int sampleCount = reader.readInt();
                    return new AbstractWorldAlignedTrailParticle.OrbData(r, g, b, width, EntityId, pitch, yaw, roll, texture, sampleCount);
                }

                public AbstractWorldAlignedTrailParticle.OrbData fromNetwork(ParticleType<OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                    return new AbstractWorldAlignedTrailParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                            buffer.readFloat(), buffer.readInt(),
                            buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readUtf(), buffer.readInt());
                }
            };

        @Override
            public void writeToNetwork(FriendlyByteBuf buffer) {
                buffer.writeFloat(this.r);
                buffer.writeFloat(this.g);
                buffer.writeFloat(this.b);
                buffer.writeFloat(this.width);
                buffer.writeInt(this.entityID);
                buffer.writeFloat(this.pitch);
                buffer.writeFloat(this.yaw);
                buffer.writeFloat(this.roll);
                buffer.writeUtf(this.texture);
                buffer.writeInt(this.sampleCount);

            }

            @Override
            public String writeToString() {
                return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %.2f %.2f %.2f %s %d",
                        BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                        this.r, this.g, this.b,
                        this.width,
                        this.entityID,
                        this.pitch, this.yaw, this.roll, this.sampleCount
                );
            }

            @Override
            public ParticleType<OrbData> getType() {
                return BrutalityModParticles.GENERIC_WORLD_ALIGNED_TRAIL_PARTICLE.get();
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

        @Override
        public float pitch() {
                return this.pitch;
            }

        @Override
        public float yaw() {
                return this.yaw;
            }

        @Override
        public float roll() {
                return this.roll;
            }

        @Override
        public String texture() {
                return this.texture;
            }

        @Override
        public int sampleCount() {
                return this.sampleCount;
            }


        public Entity getOwner() {
                return this.getOwner();
            }

            public static Codec<OrbData> CODEC(ParticleType<OrbData> particleType) {
                return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.FLOAT.fieldOf("r").forGetter(AbstractWorldAlignedTrailParticle.OrbData::r),
                        Codec.FLOAT.fieldOf("g").forGetter(AbstractWorldAlignedTrailParticle.OrbData::g),
                        Codec.FLOAT.fieldOf("b").forGetter(AbstractWorldAlignedTrailParticle.OrbData::b),
                        Codec.FLOAT.fieldOf("width").forGetter(AbstractWorldAlignedTrailParticle.OrbData::width),
                        Codec.INT.fieldOf("entityID").forGetter(AbstractWorldAlignedTrailParticle.OrbData::entityID),
                        Codec.FLOAT.fieldOf("pitch").forGetter(AbstractWorldAlignedTrailParticle.OrbData::pitch),
                        Codec.FLOAT.fieldOf("yaw").forGetter(AbstractWorldAlignedTrailParticle.OrbData::yaw),
                        Codec.FLOAT.fieldOf("roll").forGetter(AbstractWorldAlignedTrailParticle.OrbData::roll),
                        Codec.STRING.fieldOf("texture").forGetter(AbstractWorldAlignedTrailParticle.OrbData::texture),
                        Codec.INT.fieldOf("sampleCount").forGetter(AbstractWorldAlignedTrailParticle.OrbData::sampleCount)
                        ).apply(codecBuilder, AbstractWorldAlignedTrailParticle.OrbData::new)
                );
            }
        }

}