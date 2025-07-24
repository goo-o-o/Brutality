//package net.goo.brutality.particle.custom;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.math.Axis;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.goo.brutality.Brutality;
//import net.goo.brutality.client.BrutalityRenderTypes;
//import net.goo.brutality.particle.base.AbstractCameraAlignedTrailParticle;
//import net.goo.brutality.registry.BrutalityModParticles;
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
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.NotNull;
//import org.joml.Matrix3f;
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//
//import java.util.Locale;
//
//import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;
//
//public class RuinedParticle extends AbstractCameraAlignedTrailParticle {
//
//
//    public final ResourceLocation CENTER_TEXTURE = new ResourceLocation(Brutality.MOD_ID, "textures/particle/ruined_particle.png");
//    private float directionX;
//    private float directionY;
//    private float directionZ;
//
//    public RuinedParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, int sampleCount) {
//        super(level, x, y, z, r, g, b, Mth.nextFloat(level.random, 0.05F, 0.1F), -1, sampleCount);
//        this.xd = 0;
//        this.yd = 0;
//        this.zd = 0;
//        this.alpha = 0.5F;
//        this.hasPhysics = false;
//        this.xo = x;
//        this.yo = y;
//        this.zo = z;
//        this.rCol = r;
//        this.gCol = g;
//        this.bCol = b;
//        this.lifetime = 60;
//
//        this.directionX = (level.random.nextFloat() - 0.5F) * 0.02F;
//        this.directionY = (level.random.nextFloat() - 0.5F) * 0.02F;
//        this.directionZ = (level.random.nextFloat() - 0.5F) * 0.02F;
//    }
//
//
//    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
//
//        Vec3 vec3 = camera.getPosition();
//        float f = (float) (Mth.lerp(partialTick, this.xo, this.x) - vec3.x());
//        float f1 = (float) (Mth.lerp(partialTick, this.yo, this.y) - vec3.y());
//        float f2 = (float) (Mth.lerp(partialTick, this.zo, this.z) - vec3.z());
//        Quaternionf quaternion;
//        if (this.roll == 0.0F) {
//            quaternion = camera.rotation();
//        } else {
//            quaternion = new Quaternionf(camera.rotation());
//            float f3 = Mth.lerp(partialTick, this.oRoll, this.roll);
//            quaternion.mul(Axis.ZP.rotation(f3));
//        }
//
//        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
//        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(BrutalityRenderTypes.itemEntityTranslucentCull(CENTER_TEXTURE));
//
//        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
//        vector3f1.mul(getTrailWidth());
//        vector3f1.rotate(quaternion);
//        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
//        float f4 = getTrailWidth();
//
//        for (int i = 0; i < 4; ++i) {
//            Vector3f vector3f = avector3f[i];
//            vector3f.rotate(quaternion);
//            vector3f.mul(f4);
//            vector3f.add(f, f1, f2);
//        }
//        float f7 = 0;
//        float f8 = 1;
//        float f5 = 0;
//        float f6 = 1;
//        float alpha = 1;
//        int j = 240;
//        PoseStack posestack = new PoseStack();
//        PoseStack.Pose posestack$pose = posestack.last();
//        Matrix3f matrix3f = posestack$pose.normal();
//
//        vertexconsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f6).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        vertexconsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f5).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        vertexconsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f5).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        vertexconsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f6).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//
//        multibuffersource$buffersource.endBatch();
//        super.render(vertexConsumer, camera, partialTick);
//    }
//
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        // Slowly interpolate direction for smoother wandering
//        float changeRate = 0.01f; // Smaller = smoother
//        directionX += (this.level.random.nextFloat() - 0.5f) * changeRate;
//        directionY += (this.level.random.nextFloat() - 0.5f) * changeRate;
//        directionZ += (this.level.random.nextFloat() - 0.5f) * changeRate;
//
//        // Apply motion
//        this.x += directionX;
//        this.y += directionY;
//        this.z += directionZ;
//    }
//
//    @Override
//    public int sampleCount() {
//        return Math.min(10, lifetime - age);
//    }
//
//    public static final class OrbFactory implements ParticleProvider<RuinedParticle.OrbData> {
//
//        @Override
//        public Particle createParticle(RuinedParticle.OrbData typeIn, @NotNull ClientLevel levelIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
//            RuinedParticle particle;
//            particle = new RuinedParticle(levelIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.sampleCount());
//
//            return particle;
//        }
//
//
//    }
//
//
//    public record OrbData(float r, float g, float b, int sampleCount)
//            implements ParticleOptions {
//        public static final Deserializer<RuinedParticle.OrbData> DESERIALIZER = new Deserializer<>() {
//            public RuinedParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<RuinedParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
//                reader.expect(' ');
//                float r = reader.readFloat();
//                reader.expect(' ');
//                float g = reader.readFloat();
//                reader.expect(' ');
//                float b = reader.readFloat();
//                reader.expect(' ');
//                int sampleCount = reader.readInt();
//                return new RuinedParticle.OrbData(r, g, b, sampleCount);
//            }
//
//            public RuinedParticle.OrbData fromNetwork(ParticleType<RuinedParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
//                return new RuinedParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt());
//            }
//        };
//
//        @Override
//        public void writeToNetwork(FriendlyByteBuf buffer) {
//            buffer.writeFloat(this.r);
//            buffer.writeFloat(this.g);
//            buffer.writeFloat(this.b);
//            buffer.writeInt(this.sampleCount);
//
//        }
//
//        @Override
//        public String writeToString() {
//            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d",
//                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
//                    this.r, this.g, this.b,
//                    this.sampleCount
//            );
//        }
//
//        @Override
//        public ParticleType<RuinedParticle.OrbData> getType() {
//            return BrutalityModParticles.RUINED_PARTICLE.get();
//        }
//
//
//        public static Codec<RuinedParticle.OrbData> CODEC(ParticleType<RuinedParticle.OrbData> particleType) {
//            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
//                            Codec.FLOAT.fieldOf("r").forGetter(RuinedParticle.OrbData::r),
//                            Codec.FLOAT.fieldOf("g").forGetter(RuinedParticle.OrbData::g),
//                            Codec.FLOAT.fieldOf("b").forGetter(RuinedParticle.OrbData::b),
//                            Codec.INT.fieldOf("sampleCount").forGetter(RuinedParticle.OrbData::sampleCount)
//                    ).apply(codecBuilder, RuinedParticle.OrbData::new)
//            );
//        }
//    }
//}