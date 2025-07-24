//package net.goo.brutality.particle.custom;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.goo.brutality.entity.base.BrutalityAbstractTrident;
//import net.goo.brutality.particle.base.AbstractCameraAlignedTrailParticle;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.client.Camera;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.particle.ParticleProvider;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.NotNull;
//import org.joml.Matrix3f;
//import org.joml.Matrix4f;
//
//import java.util.Locale;
//
//import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;
//
//public class RainbowTrailParticle extends AbstractCameraAlignedTrailParticle {
//
//
//    protected float trailA = 0.4F;
//
//    public RainbowTrailParticle(ClientLevel world, double x, double y, double z, float width, int entityId, int sampleCount) {
//        super(world, x, y, z, 0, 0, 0, width, entityId, sampleCount);
//        this.lifetime = 200;
//    }
//
//    public Vec3 getRenderedDirection() {
//        Entity owner = this.getFromEntity();
//        if (owner != null) {
//            Vec3 movement = owner.getDeltaMovement();
//            if (movement.lengthSqr() < 0.0001) { // Small value to account for floating point errors
//                return new Vec3(0, 0, 0); // Not moving
//            }
//            return movement.normalize();
//        }
//        return new Vec3(this.x, this.y, this.z);
//    }
//
//    @Override
//    public Vec3 getEntityCenter() {
//        Entity from = this.getFromEntity();
//        if (from != null) {
//            if (from instanceof BrutalityAbstractTrident) {
//                Vec3 movement = getRenderedDirection();
//
//                return from.position().add(movement.scale(-1F));
//            }
//            return from.position().add(0, from.getBbHeight() / 2, 0);
//
//        }
//        return new Vec3(this.x, this.y - 0.25, this.z);
//    }
//
//    public void tick() {
//        super.tick();
//        float fade = 1F - age / (float) lifetime;
//        this.trailA = fade * 2F;
//        Vec3 vec3 = getEntityCenter();
//        this.x = vec3.x;
//        this.y = vec3.y;
//        this.z = vec3.z;
//
//        Entity from = this.getFromEntity();
//        if (from == null) {
//            remove();
//        }
//
//    }
//
//    private static final int COLOR_RES = 64;
//    private static final float[][] CACHED_COLORS = new float[COLOR_RES][3];
//
//    static {
//        for (int i = 0; i < COLOR_RES; i++) {
//            int[] rgb = BrutalityTooltipHelper.intToRgb(Mth.hsvToRgb(i / (float) COLOR_RES, 1.0f, 0.75f));
//            CACHED_COLORS[i][0] = rgb[0] / 255F;
//            CACHED_COLORS[i][1] = rgb[1] / 255F;
//            CACHED_COLORS[i][2] = rgb[2] / 255F;
//        }
//    }
//
//
//    @Override
//    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
//        if (trailPointer > -1) {
//            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
//            VertexConsumer vertexconsumer = getVertexConsumer(multibuffersource$buffersource);
//
//            Vec3 cameraPos = camera.getPosition();
//            double x = (float) (Mth.lerp(partialTick, this.xo, this.x));
//            double y = (float) (Mth.lerp(partialTick, this.yo, this.y));
//            double z = (float) (Mth.lerp(partialTick, this.zo, this.z));
//
//            PoseStack posestack = new PoseStack();
//            posestack.pushPose();
//            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//            int samples = 0;
//            Vec3 drawFrom = new Vec3(x, y, z);
//            float zRot = getTrailRot(camera);
//            Vec3 topAngleVec = new Vec3(0, getTrailWidth() / 2F, 0).zRot(zRot);
//            Vec3 bottomAngleVec = new Vec3(0, getTrailWidth() / -2F, 0).zRot(zRot);
//            PoseStack.Pose posestack$pose = posestack.last();
//            Matrix4f matrix4f = posestack$pose.pose();
//            Matrix3f matrix3f = posestack$pose.normal();
//            int totalSamples = sampleCount();
//            float invSampleCount = 1.0f / totalSamples;
//
//            int j = getLightColor(partialTick);
//            while (samples < sampleCount()) {
//                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
//                float u1 = samples * invSampleCount;
//                float u2 = u1 + invSampleCount;
//
//                Vec3 draw1 = drawFrom;
//
//                float hue = (u1 + (this.age % 1000) / 1000.0f) % 1.0f;
//                int idx = (int)(hue * COLOR_RES) % COLOR_RES;
//                float[] color = CACHED_COLORS[idx];
//                float r = color[0], g = color[1], b = color[2];
//
//
//
//                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y,
//                                (float) draw1.z + (float) bottomAngleVec.z)
//                        .color(r, g, b, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) bottomAngleVec.x, (float) sample.y + (float) bottomAngleVec.y,
//                                (float) sample.z + (float) bottomAngleVec.z)
//                        .color(r, g, b, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) topAngleVec.x, (float) sample.y + (float) topAngleVec.y,
//                                (float) sample.z + (float) topAngleVec.z)
//                        .color(r, g, b, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y,
//                                (float) draw1.z + (float) topAngleVec.z)
//                        .color(r, g, b, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                samples++;
//                drawFrom = sample;
//            }
//            multibuffersource$buffersource.endBatch();
//            posestack.popPose();
//        }
//    }
//
//
//    public static final class OrbFactory implements ParticleProvider<RainbowTrailParticle.OrbData> {
//
//        @Override
//        public Particle createParticle(RainbowTrailParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
//            RainbowTrailParticle particle;
//            particle = new RainbowTrailParticle(worldIn, x, y, z, typeIn.width(),
//                    typeIn.entityID(), typeIn.sampleCount());
//
//            return particle;
//        }
//
//
//    }
//
//
//    public record OrbData(float width, int entityID, int sampleCount)
//            implements ParticleOptions {
//        public static final Deserializer<RainbowTrailParticle.OrbData> DESERIALIZER = new Deserializer<>() {
//            public RainbowTrailParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<RainbowTrailParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
//                reader.expect(' ');
//                float width = reader.readFloat();
//                reader.expect(' ');
//                int EntityId = reader.readInt();
//                reader.expect(' ');
//                int sampleCount = reader.readInt();
//                return new RainbowTrailParticle.OrbData(width, EntityId, sampleCount);
//            }
//
//            public RainbowTrailParticle.OrbData fromNetwork(ParticleType<RainbowTrailParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
//                return new RainbowTrailParticle.OrbData(buffer.readFloat(), buffer.readInt(), buffer.readInt());
//            }
//        };
//
//        @Override
//        public void writeToNetwork(FriendlyByteBuf buffer) {
//            buffer.writeFloat(this.width);
//            buffer.writeInt(this.entityID);
//            buffer.writeInt(this.sampleCount);
//
//        }
//
//        @Override
//        public String writeToString() {
//            return String.format(Locale.ROOT, "%s %.2f %d  %d",
//                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
//                    this.width,
//                    this.entityID,
//                    this.sampleCount
//            );
//        }
//
//        @Override
//        public ParticleType<RainbowTrailParticle.OrbData> getType() {
//            return BrutalityModParticles.RAINBOW_TRAIL_PARTICLE.get();
//        }
//
//        @Override
//        public float width() {
//            return this.width;
//        }
//
//        @Override
//        public int entityID() {
//            return this.entityID;
//        }
//
//
//        public static Codec<RainbowTrailParticle.OrbData> CODEC(ParticleType<RainbowTrailParticle.OrbData> particleType) {
//            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
//                            Codec.FLOAT.fieldOf("width").forGetter(RainbowTrailParticle.OrbData::width),
//                            Codec.INT.fieldOf("entityID").forGetter(RainbowTrailParticle.OrbData::entityID),
//                            Codec.INT.fieldOf("sampleCount").forGetter(RainbowTrailParticle.OrbData::sampleCount)
//                    ).apply(codecBuilder, RainbowTrailParticle.OrbData::new)
//            );
//        }
//    }
//}