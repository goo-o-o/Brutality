package net.goo.brutality.particle.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.BrutalityRenderTypes;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sDamageEntityPacket;
import net.goo.brutality.particle.base.AbstractCameraAlignedTrailParticle;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class TerratomereParticle extends AbstractCameraAlignedTrailParticle {

    private final int entityId;

    public final ResourceLocation CENTER_TEXTURE = new ResourceLocation(Brutality.MOD_ID, "textures/particle/generic_circle_particle.png");

    public TerratomereParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, int entityId, int sampleCount) {
        super(world, x, y, z, r, g, b, width, entityId, sampleCount);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.alpha = 0.5F;
        this.entityId = entityId;
        this.hasPhysics = false;

        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.lifetime = 200;

        this.setParticleSpeed(
                Mth.nextFloat(this.level.random, -0.25F, 0.25F),
                Mth.nextFloat(this.level.random, -0.25F, 0.25F),
                Mth.nextFloat(this.level.random, -0.25F, 0.25F));
    }


    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {

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
        vector3f1.mul(getTrailWidth() / 5);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = getTrailWidth() / 10;

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
        this.trailA = 0.2F * Mth.clamp(age / (float) this.lifetime * 32.0F, 0.0F, 1.0F);

        if (entityId != -1) {
            Entity owner = level.getEntity(entityId);

            if (owner instanceof Player player) {

                LivingEntity nearestEntity = level.getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.ignoreLineOfSight(), player,
                        this.getPos().x, this.getPos().y, this.getPos().z, this.getBoundingBox().inflate(10F));

                if (nearestEntity != null) {

                    float lerpFactor = .35f;

                    double newX = Mth.lerp(lerpFactor, this.x, nearestEntity.position().x);
                    double newY = Mth.lerp(lerpFactor, this.y, nearestEntity.position().y + nearestEntity.getBbHeight() / 2);
                    double newZ = Mth.lerp(lerpFactor, this.z, nearestEntity.position().z);

                    this.setPos(newX, newY, newZ);

                    if (nearestEntity.distanceToSqr(this.getPos()) < 3.5) {
                        if (!hit) {
                            PacketHandler.sendToServer(new c2sDamageEntityPacket(nearestEntity.getId(), 18, nearestEntity.damageSources().playerAttack(player)));
                            level.addParticle(BrutalityModParticles.TERRATOMERE_EXPLOSION.get(), true, this.x, this.y, this.z, 0, 0, 0);
                            level.playLocalSound(BlockPos.containing(this.getPos()), BrutalityModSounds.VORTEX_EXPLOSION.get(), SoundSource.AMBIENT,
                                    1, Mth.nextFloat(this.level.getRandom(), 0.5F, 1.2F), false);
                            hit = true;
                        }

                        tickCount++;
                        if (tickCount > 20)
                            this.remove();
                    }
                }
            }
        }
    }

    private boolean hit = false;
    private int tickCount = 0;

    @Override
    public int sampleCount() {
        return Math.min(10, lifetime - age);
    }

    public static final class OrbFactory implements ParticleProvider<TerratomereParticle.OrbData> {

        @Override
        public Particle createParticle(TerratomereParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TerratomereParticle particle;
            particle = new TerratomereParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(),
                    typeIn.entityID(), typeIn.sampleCount());

            return particle;
        }


    }


    public record OrbData(float r, float g, float b, float width, int entityID, int sampleCount)
            implements ParticleOptions {
        public static final Deserializer<TerratomereParticle.OrbData> DESERIALIZER = new Deserializer<>() {
            public TerratomereParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<TerratomereParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                return new TerratomereParticle.OrbData(r, g, b, width, EntityId, sampleCount);
            }

            public TerratomereParticle.OrbData fromNetwork(ParticleType<TerratomereParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new TerratomereParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
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
        public ParticleType<TerratomereParticle.OrbData> getType() {
            return BrutalityModParticles.TERRATOMERE_PARTICLE.get();
        }


        public static Codec<TerratomereParticle.OrbData> CODEC(ParticleType<TerratomereParticle.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(TerratomereParticle.OrbData::r),
                            Codec.FLOAT.fieldOf("g").forGetter(TerratomereParticle.OrbData::g),
                            Codec.FLOAT.fieldOf("b").forGetter(TerratomereParticle.OrbData::b),
                            Codec.FLOAT.fieldOf("width").forGetter(TerratomereParticle.OrbData::width),
                            Codec.INT.fieldOf("entityID").forGetter(TerratomereParticle.OrbData::entityID),
                            Codec.INT.fieldOf("sampleCount").forGetter(TerratomereParticle.OrbData::sampleCount)
                    ).apply(codecBuilder, TerratomereParticle.OrbData::new)
            );
        }
    }
}