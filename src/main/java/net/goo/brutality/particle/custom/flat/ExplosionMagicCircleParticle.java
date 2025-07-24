//package net.goo.brutality.particle.custom.flat;
//
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.goo.brutality.client.BrutalityRenderTypes;
//import net.goo.brutality.item.weapon.generic.FirstExplosionStaff;
//import net.goo.brutality.particle.base.GenericMagicCircleParticle;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.goo.brutality.util.ModUtils;
//import net.minecraft.client.Camera;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.particle.ParticleProvider;
//import net.minecraft.client.particle.ParticleRenderType;
//import net.minecraft.client.particle.SpriteSet;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.Vec3;
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//
//import java.util.Locale;
//
//import static net.goo.brutality.particle.base.WaveParticle.MILIS_TO_SECONDS;
//
//public class ExplosionMagicCircleParticle extends GenericMagicCircleParticle {
//    private final int localRotationSpeed;
//    protected boolean shouldDespawn = false, shouldFollowPlayer = false;
//    private float currentRotationDegrees = 0f;
//
//    protected ExplosionMagicCircleParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int EntityId, float size, float rotX, float rotY, float rotZ, boolean rotateLeft, boolean shouldFollowPlayer) {
//        super(level, x, y, z, spriteSet, EntityId, size, rotX, rotY, rotZ);
//        this.EntityId = EntityId;
//        this.friction = 0.75F;
//        this.quadSize = size;
//        this.lifetime = 10000;
//        this.rotX = rotX;
//        this.rotY = rotY;
//        this.rotZ = rotZ;
//        this.rotateLeft = rotateLeft;
//        this.pickSprite(spriteSet);
//        this.shouldFollowPlayer = shouldFollowPlayer;
//        this.localRotationSpeed = level.random.nextIntBetweenInclusive(15, 45);
//    }
//
//    @Override
//    public ParticleRenderType getRenderType() {
//        return BrutalityRenderTypes.PARTICLE_SHEET_TRANSLUCENT_NO_CULL;
//    }
//
//    @Override
//    public float getQuadSize(float pScaleFactor) {
//        return (this.quadSize + 1) * ModUtils.ModEasings.easeQuadOut(growthProgress); // Scale size based on growth progress
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        Entity entity = getFromEntity();
//        if (entity instanceof Player player) {
//            if (player.getUseItem().getItem() instanceof FirstExplosionStaff) {
//                // Player is holding the rod
//                if (!player.isUsingItem()) {
//                    this.shouldDespawn = true;
//                }
//                if (shouldFollowPlayer) this.setPos(getFromEntity().getX(), getFromEntity().getY() + 0.1, getFromEntity().getZ());
//            } else {
//                // Player is not holding the rod at all
//                this.shouldDespawn = true;
//
//            }
//        } else {
//            // No player entity found
//            this.shouldDespawn = true;
//        }
//
//        // Remove if fully shrunk
//        if (shouldDespawn && growthProgress <= -0.1F) {
//            this.remove();
//
//        }
//    }
//
//    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
//        Vec3 cameraPos = camera.getPosition();
//        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
//        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
//        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());
//
//
//        long currentTime = System.currentTimeMillis();
//        float deltaTime = (currentTime - lastUpdateTime) * MILIS_TO_SECONDS;
//        lastUpdateTime = currentTime;
//        float enlargeFactor = 0.5F;
//        float shrinkFactor = 1F;
//        // Growth/shrinking logic
//        if (!shouldDespawn) {
//            // Growing phase
//            if (growthProgress < 1.0F) {
//                growthProgress += deltaTime * growthSpeed * enlargeFactor / growthDuration;
//                growthProgress = Math.min(growthProgress, 1.0F);
//            }
//        } else {
//            // Shrinking phase - make it faster by multiplying by 2
//            growthProgress -= deltaTime * growthSpeed * shrinkFactor / growthDuration;
//            growthProgress = Math.max(growthProgress, -0.1F);
//
//            // Make the circle fade out as it shrinks
//            alpha = growthProgress > 0 ? growthProgress : 0;
//        }
//
//        // Only render if we have some size
//        if (growthProgress <= 0) {
//            return;
//        }
//
//        // Create individual rotation quaternions
//        Quaternionf pitchRot = new Quaternionf().rotationX((float) Math.toRadians(this.rotX));
//        Quaternionf yawRot = new Quaternionf().rotationY((float) Math.toRadians(this.rotY));
//        Quaternionf rollRot = new Quaternionf().rotationZ((float) Math.toRadians(this.rotZ));
//
//        // Local rotation - only rotate when growing or fully grown
//        if (rotateLeft) {
//            // Accumulate rotation when growing/active
//            currentRotationDegrees += deltaTime * localRotationSpeed;
//        } else {
//            // Reverse rotation when despawning
//            currentRotationDegrees -= deltaTime * localRotationSpeed;
//        }
//        Quaternionf localYawRot = new Quaternionf().rotationZ((float) Math.toRadians(currentRotationDegrees));
//
//        // Combine rotations
//        Quaternionf combinedRot = new Quaternionf();
//        combinedRot.mul(yawRot)
//                .mul(pitchRot)
//                .mul(rollRot)
//                .mul(QUATERNION)
//                .mul(localYawRot);
//
//        // Base quad vertices - scale by growthProgress
//        Vector3f[] baseVertices = new Vector3f[]{
//                new Vector3f(-1.0F, -1.0F, 0.0F),
//                new Vector3f(-1.0F, 1.0F, 0.0F),
//                new Vector3f(1.0F, 1.0F, 0.0F),
//                new Vector3f(1.0F, -1.0F, 0.0F)
//        };
//
//        // Scale size by growthProgress
//        float size = this.getQuadSize(partialTicks) * growthProgress;
//        Vector3f[] transformedVertices = new Vector3f[4];
//
//        // Transform each vertex
//        for (int i = 0; i < 4; i++) {
//            transformedVertices[i] = new Vector3f(baseVertices[i]);
//            transformedVertices[i].rotate(combinedRot);
//            transformedVertices[i].mul(size);
//            transformedVertices[i].add(x, y, z);
//        }
//
//        // Get UV coordinates and light value
//        float u0 = this.getU0();
//        float u1 = this.getU1();
//        float v0 = this.getV0();
//        float v1 = this.getV1();
//        int light = this.getLightColor(partialTicks);
//
//        // Render only if visible
//        if (alpha > 0) {
//            // Render top face
//            buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
//                    .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
//                    .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
//                    .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
//                    .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//
//            // Render bottom face
//            buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
//                    .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
//                    .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
//                    .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//            buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
//                    .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
//        }
//    }
//
//    public record ParticleData(int entityId, float size, float rotX, float rotY,
//                               float rotZ, boolean rotateLeft, boolean shouldFollowPlayer) implements ParticleOptions {
//        public static final Deserializer<ParticleData> DESERIALIZER = new Deserializer<>() {
//            public ParticleData fromCommand(ParticleType<ParticleData> type, StringReader reader) throws CommandSyntaxException {
//                reader.expect(' ');
//                int entityId = reader.readInt();
//                reader.expect(' ');
//                float size = reader.readFloat();
//                reader.expect(' ');
//                float rotX = reader.readFloat();
//                reader.expect(' ');
//                float rotY = reader.readFloat();
//                reader.expect(' ');
//                float rotZ = reader.readFloat();
//                reader.expect(' ');
//                boolean rotateLeft = reader.readBoolean();
//                reader.expect(' ');
//                boolean shoudFollowPlayer = reader.readBoolean();
//                return new ParticleData(entityId, size, rotX, rotY, rotZ, rotateLeft, shoudFollowPlayer);
//            }
//
//            public ParticleData fromNetwork(ParticleType<ParticleData> type, FriendlyByteBuf buffer) {
//                return new ParticleData(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean());
//            }
//        };
//
//        @Override
//        public void writeToNetwork(FriendlyByteBuf buffer) {
//            buffer.writeInt(entityId);
//            buffer.writeFloat(size);
//            buffer.writeFloat(rotX);
//            buffer.writeFloat(rotY);
//            buffer.writeFloat(rotZ);
//            buffer.writeBoolean(rotateLeft);
//            buffer.writeBoolean(shouldFollowPlayer);
//        }
//
//        @Override
//        public String writeToString() {
//            return String.format(Locale.ROOT, "%s %d %.2f %.2f %.2f %b %b",
//                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
//                    entityId, size, rotX, rotY, rotZ, rotateLeft, shouldFollowPlayer);
//        }
//
//        @Override
//        public ParticleType<ParticleData> getType() {
//            return BrutalityModParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get();
//        }
//
//        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
//            return RecordCodecBuilder.create(instance ->
//                    instance.group(
//                            Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
//                            Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size),
//                            Codec.FLOAT.fieldOf("rotX").forGetter(ParticleData::rotX),
//                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotY),
//                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotZ),
//                            Codec.BOOL.fieldOf("rotateLeft").forGetter(ParticleData::rotateLeft),
//                            Codec.BOOL.fieldOf("shouldFollowPlayer").forGetter(ParticleData::shouldFollowPlayer)
//                    ).apply(instance, ParticleData::new)
//            );
//        }
//    }
//
//    public static class Provider implements ParticleProvider<ParticleData> {
//        private final SpriteSet spriteSet;
//
//        public Provider(SpriteSet spriteSet) {
//            this.spriteSet = spriteSet;
//        }
//
//        @Override
//        public Particle createParticle(ParticleData data, ClientLevel level,
//                                       double x, double y, double z,
//                                       double xSpeed, double ySpeed, double zSpeed) {
//            return new ExplosionMagicCircleParticle(level, x, y, z,
//                    this.spriteSet,
//                    data.entityId(),
//                    data.size(),
//                    data.rotX(),
//                    data.rotY(),
//                    data.rotZ(),
//                    data.rotateLeft(),
//                    data.shouldFollowPlayer()
//            );
//
//        }
//    }
//}